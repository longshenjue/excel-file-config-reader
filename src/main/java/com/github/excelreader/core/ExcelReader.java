package com.github.excelreader.core;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.read.metadata.holder.csv.CsvReadWorkbookHolder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.github.excelreader.config.ColumnConfig;
import com.github.excelreader.config.ColumnConfigDetail;
import com.github.excelreader.handler.ColumnFormatHandler;
import com.github.excelreader.model.ExcelData;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.*;

@Slf4j
public class ExcelReader {
    private final ColumnFormatHandler columnFormatHandler;
    private final List<ExcelData> dataList = new ArrayList<>();
    private final List<Map<String, Object>> originData = new ArrayList<>();

    public ExcelReader(ColumnFormatHandler columnFormatHandler) {
        this.columnFormatHandler = columnFormatHandler;
    }

    public List<ExcelData> readData(InputStream inputStream, ColumnConfig columnConfig, boolean saveOriginData) {
        dataList.clear();
        originData.clear();

        ExcelTypeEnum excelType = getExcelType(columnConfig.getSuffix());
        Map<String, ColumnConfigDetail> columnMapping = parseColumnMapping(columnConfig.getColumnConfig());
        Map<String, ColumnConfigDetail> newColumnMapping = createNewColumnMapping(columnMapping);

        com.alibaba.excel.ExcelReader excelReader = buildExcelReader(
                inputStream,
                columnMapping,
                newColumnMapping,
                saveOriginData,
                columnConfig.getHeadNumber(),
                excelType
        );

        configureCsvIfNeeded(excelReader, excelType, columnConfig.getSeparator());

        ReadSheet readSheet = EasyExcel.readSheet(0).build();
        excelReader.read(readSheet);

        return dataList;
    }

    private ExcelTypeEnum getExcelType(String suffix) {
        if (suffix.equals(ExcelTypeEnum.XLS.getValue())) {
            return ExcelTypeEnum.XLS;
        } else if (suffix.equals(ExcelTypeEnum.CSV.getValue())) {
            return ExcelTypeEnum.CSV;
        }
        return ExcelTypeEnum.XLSX;
    }

    private Map<String, ColumnConfigDetail> parseColumnMapping(String columnConfig) {
        Map<String, ColumnConfigDetail> columnMapping = new HashMap<>();
        Map<String, JSONObject> bankColumnStringMap = JSONUtil.toBean(columnConfig, Map.class);

        bankColumnStringMap.forEach((k, v) -> {
            ColumnConfigDetail bean = BeanUtil.toBean(v, ColumnConfigDetail.class);
            columnMapping.put(k, bean);
        });

        return columnMapping;
    }

    private Map<String, ColumnConfigDetail> createNewColumnMapping(Map<String, ColumnConfigDetail> columnMapping) {
        Map<String, ColumnConfigDetail> newColumnMapping = new HashMap<>();
        columnMapping.forEach((k, v) -> {
            String newColumn = v.getNewColumn();
            newColumnMapping.put(newColumn, v);
        });
        return newColumnMapping;
    }

    private com.alibaba.excel.ExcelReader buildExcelReader(
            InputStream inputStream,
            Map<String, ColumnConfigDetail> columnMapping,
            Map<String, ColumnConfigDetail> newColumnMapping,
            boolean saveOriginData,
            Integer headNumber,
            ExcelTypeEnum excelType) {

        DataMapper dataMapper = new DataMapper();

        return EasyExcel.read(inputStream, new AnalysisEventListener<Map<Integer, String>>() {
                    Map<Integer, String> columnMap = new HashMap<>();

                    @Override
                    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
                        headMap.forEach((key, originColumn) -> {
                            if (columnMapping.containsKey(originColumn)) {
                                ColumnConfigDetail detail = columnMapping.get(originColumn);
                                String newColumn = detail.getNewColumn();
                                columnMap.put(key, newColumn);
                            }
                        });
                        log.info("解析header完成：{}", columnMap);
                    }

                    @Override
                    public void invoke(Map<Integer, String> data, AnalysisContext context) {
                        ExcelData excelData = new ExcelData();
                        Map<String, Object> originItem = new HashMap<>();

                        columnMap.forEach((key, column) -> {
                            if (saveOriginData) {
                                originItem.put(newColumnMapping.get(columnMap.get(key)).getOldColumn(), data.get(key));
                            }

                            String cellValue = data.get(key);
                            ColumnConfigDetail detail = newColumnMapping.get(column);

                            if (StrUtil.isNotEmpty(detail.getSaveOriginalData())) {
                                dataMapper.setFieldValueByFieldName(detail.getSaveOriginalData(), data.get(key), excelData);
                            }

                            cellValue = columnFormatHandler.format(cellValue, detail);
                            if (cellValue == null) {
                                cellValue = "";
                            }

                            dataMapper.setFieldValueByFieldName(column, cellValue, excelData);
                        });

                        dataList.add(excelData);
                        if (saveOriginData) {
                            originData.add(originItem);
                        }
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext context) {
                        log.info("所有数据解析完成！");
                    }
                })
                .headRowNumber(headNumber)
                .excelType(excelType)
                .build();
    }

    private void configureCsvIfNeeded(com.alibaba.excel.ExcelReader excelReader, ExcelTypeEnum excelType, String separator) {
        if (excelType.equals(ExcelTypeEnum.CSV)) {
            CsvReadWorkbookHolder csvReadWorkbookHolder = (CsvReadWorkbookHolder) excelReader.analysisContext().readWorkbookHolder();
            char separatorChar = StrUtil.isNotEmpty(separator) ? separator.charAt(0) : ',';
            csvReadWorkbookHolder.setCsvFormat(csvReadWorkbookHolder.getCsvFormat().withDelimiter(separatorChar));
        }
    }
} 
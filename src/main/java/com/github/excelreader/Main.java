package com.github.excelreader;

import com.github.excelreader.config.ColumnConfig;
import com.github.excelreader.core.ExcelReader;
import com.github.excelreader.handler.DefaultColumnFormatHandler;
import com.github.excelreader.model.ExcelData;

import java.io.FileInputStream;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 创建列配置
        String columnConfig = "{\"订单号\":{\"newColumn\":\"orderId\",\"type\":\"STRING\"}," +
                "\"金额\":{\"newColumn\":\"amount\",\"type\":\"CURRENCY\"}," +
                "\"时间\":{\"newColumn\":\"orderTime\",\"type\":\"DATE\",\"format\":\"yyyy-MM-dd HH:mm:ss\"}}";

        ColumnConfig config = ColumnConfig.builder()
                .suffix("xlsx")
                .columnConfig(columnConfig)
                .headNumber(1)
                .build();

        // 创建读取器
        ExcelReader reader = new ExcelReader(new DefaultColumnFormatHandler());

        FileInputStream inputStream = null;
        // 读取数据
        List<ExcelData> dataList = reader.readData(inputStream, config, false);
    }
}

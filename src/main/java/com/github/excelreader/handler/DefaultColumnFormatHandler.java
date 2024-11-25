package com.github.excelreader.handler;

import com.github.excelreader.config.ColumnConfigDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultColumnFormatHandler implements ColumnFormatHandler {
    private static final Logger log = LoggerFactory.getLogger(DefaultColumnFormatHandler.class);

    @Override
    public String format(String cellValue, ColumnConfigDetail detail) {
        if (cellValue == null) {
            return "";
        }
        
        try {
            switch (detail.getType()) {
                case "STRING":
                    return cellValue.trim();
                case "NUMBER":
                    return cellValue.replaceAll("[^\\d.]", "");
                case "DATE":
                    // 实现日期格式化逻辑
                    return cellValue;
                default:
                    return cellValue;
            }
        } catch (Exception e) {
            log.error("格式化单元格值失败: value={}, type={}", cellValue, detail.getType(), e);
            return cellValue;
        }
    }
}
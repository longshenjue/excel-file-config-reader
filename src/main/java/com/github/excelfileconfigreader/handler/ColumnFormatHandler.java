package com.github.excelfileconfigreader.handler;

import com.github.excelfileconfigreader.config.ColumnConfigDetail;

public interface ColumnFormatHandler {
    String format(String cellValue, ColumnConfigDetail detail);
} 
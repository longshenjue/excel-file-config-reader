package com.github.excelreader.handler;

import com.github.excelreader.config.ColumnConfigDetail;

public interface ColumnFormatHandler {
    String format(String cellValue, ColumnConfigDetail detail);
} 
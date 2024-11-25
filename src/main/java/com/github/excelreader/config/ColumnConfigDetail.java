package com.github.excelreader.config;

import lombok.Data;

@Data
public class ColumnConfigDetail {
    private String oldColumn;
    private String newColumn;
    private String type;
    private String saveOriginalData;
    private String format;
}
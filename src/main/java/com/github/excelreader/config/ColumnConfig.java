package com.github.excelreader.config;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ColumnConfig {
    private String suffix;
    private String columnConfig;
    private String separator;
    private Integer headNumber;
} 
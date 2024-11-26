package com.github.excelfileconfigreader.config;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ColumnConfig {
    /**
     * 列配置JSON字符串
     */
    private String columnConfig;
    
    /**
     * 文件后缀
     */
    private String suffix;
    
    /**
     * CSV分隔符
     */
    private String separator;
    
    /**
     * 表头行数
     */
    private Integer headNumber;
} 
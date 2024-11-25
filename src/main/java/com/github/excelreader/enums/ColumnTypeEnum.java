package com.github.excelreader.enums;

public enum ColumnTypeEnum {
    STRING("STRING"),
    NUMBER("NUMBER"),
    DATE("DATE"),
    CURRENCY("CURRENCY");

    private final String value;

    ColumnTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
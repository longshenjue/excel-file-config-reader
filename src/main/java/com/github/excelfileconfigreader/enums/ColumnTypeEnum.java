package com.github.excelfileconfigreader.enums;

import lombok.Getter;

@Getter
public enum ColumnTypeEnum {
    STRING("STRING", "字符串类型"),
    NUMBER("NUMBER", "数字类型"),
    DATE("DATE", "日期类型"),
    CURRENCY("CURRENCY", "货币类型"),
    ORDER_TYPE("ORDER_TYPE", "订单类型"),
    ORDER_STATUS("ORDER_STATUS", "订单状态"),
    ORDER_TIME("ORDER_TIME", "订单时间"),
    ORDER_AMOUNT("ORDER_AMOUNT", "订单金额"),
    ORDER_TYPE_CHECK("ORDER_TYPE_CHECK", "订单类型检查");

    private final String value;
    private final String desc;

    ColumnTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

}
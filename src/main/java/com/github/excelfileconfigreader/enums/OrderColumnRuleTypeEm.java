package com.github.excelfileconfigreader.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 订单 - 字段rule type
 *
 * @author zhangyuting 2024-08-06
 */
@Getter
@AllArgsConstructor
public enum OrderColumnRuleTypeEm {

    ORDER_TIME_NORMAL("ORDER_TIME_NORMAL", OrderColumnTypeEm.ORDER_TIME.getValue(), "时间-普通格式"),
    ORDER_TIME_FIX_SUFFIX("ORDER_TIME_FIX_SUFFIX", OrderColumnTypeEm.ORDER_TIME.getValue(), "时间-添加后缀"),
    ORDER_TIME_FORMAT("ORDER_TIME_FORMAT", OrderColumnTypeEm.ORDER_TIME.getValue(), "时间-自定义格式"),
    ORDER_TIME_MULTI("ORDER_TIME_MULTI", OrderColumnTypeEm.ORDER_TIME.getValue(), "时间-多字段格式(未实现)"),

    ORDER_AMOUNT_FIX("ORDER_AMOUNT_FIX", OrderColumnTypeEm.ORDER_AMOUNT.getValue(), "金额-固定值"),
    ORDER_AMOUNT_MAP("ORDER_AMOUNT_MAP", OrderColumnTypeEm.ORDER_AMOUNT.getValue(), "金额-映射配置值(暂时用不到)"),
    ORDER_AMOUNT_DIVIDE_NUMBER("ORDER_AMOUNT_DIVIDE_NUMBER", OrderColumnTypeEm.ORDER_AMOUNT.getValue(), "金额-除以特定值"),

    ORDER_STATUS_FIX("ORDER_STATUS_FIX", OrderColumnTypeEm.ORDER_STATUS.getValue(), "状态-固定值"),
    ORDER_STATUS_MAP("ORDER_STATUS_MAP", OrderColumnTypeEm.ORDER_STATUS.getValue(), "状态-映射配置值"),
    ORDER_STATUS_MULTI("ORDER_STATUS_MULTI", OrderColumnTypeEm.ORDER_STATUS.getValue(), "状态-复合条件判断(未实现)"),

    ;
    private final String value;

    private final String columnType;

    private final String desc;

    public static OrderColumnRuleTypeEm toInfo(String typeStr) {
        return Stream.of(OrderColumnRuleTypeEm.values())
                .filter(p -> Objects.equals(p.getValue(), typeStr))
                .findAny()
                .orElse(null);
    }
}

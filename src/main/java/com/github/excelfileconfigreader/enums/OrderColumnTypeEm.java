package com.github.excelfileconfigreader.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.stream.Stream;

/**
 * 订单 - 字段type
 *
 * @author zhangyuting 2024-08-06
 */
@Getter
@AllArgsConstructor
public enum OrderColumnTypeEm {

    ORDER_STRING("OrderString", "对账单普通字符", Arrays.asList("orderId", "e2eId", "desc", "direction", "feeDirection", "account", "code", "orderCurrency", "settlementCurrency")),
    ORDER_AMOUNT("OrderAmount", "对账单金额字段", Arrays.asList("orderAmount", "settlementAmount", "feeAmount")),
    ORDER_TIME("OrderTime", "对账单时间字段", Arrays.asList("orderTime", "updateTime", "createTime")),
    ORDER_STATUS("OrderStatus", "对账单状态字段", Collections.singletonList("status")),
    ;

    private final String value;

    private final String desc;

    private final List<String> newColumnList;

    public static OrderColumnTypeEm toInfo(String typeStr) {
        return Stream.of(OrderColumnTypeEm.values())
                .filter(p -> Objects.equals(p.getValue(), typeStr))
                .findAny()
                .orElse(null);
    }
}

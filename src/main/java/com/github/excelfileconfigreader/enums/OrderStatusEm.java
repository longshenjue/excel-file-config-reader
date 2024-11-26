package com.github.excelfileconfigreader.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单状态枚举
 *
 * @author zhangyuting 2024-04-26
 */
@Getter
@AllArgsConstructor
public enum OrderStatusEm {

    SUCCESS("SUCCESS"),
    REFUNDED("REFUNDED"),
    UNSAVE("UNSAVE"),
    UNKNOWN("UNKNOWN");

    private final String value;

}

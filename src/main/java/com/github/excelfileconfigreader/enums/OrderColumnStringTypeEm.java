package com.github.excelfileconfigreader.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单 - 字段字符串处理的type
 *
 * @author zhangyuting 2024-08-06
 */
@Getter
@AllArgsConstructor
public enum OrderColumnStringTypeEm {

    DEL_PRE("DEL_PRE", "删除前置的x个字符"),
    DEL_AFTER("DEL_AFTER", "删除后置的x个字符"),
    DEL_CHAR("DEL_CHAR", "删除某个特定字符"),
    REPLACE_TWO_CHAR("REPLACE_TWO_CHAR", "替换A->B"),
    DIVIDE_NUMBER("DIVIDE_NUMBER", "除以特定值"),
    ABS_VALUE("ABS_VALUE", "转为绝对值"),
    ADD_CHAR_PRE("ADD_CHAR_PRE", "前置添加字符"),
    ADD_CHAR_AFTER("ADD_CHAR_AFTER", "后置添加字符");

    private final String value;

    private final String desc;
}

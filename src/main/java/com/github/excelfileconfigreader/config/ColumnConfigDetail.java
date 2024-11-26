package com.github.excelfileconfigreader.config;

import lombok.Data;

@Data
public class ColumnConfigDetail {
    private String oldColumn; // 文件中的列名
    private String newColumn; // 存入数据表中字段的对应的model 的字段名： 通常是驼峰格式

    private String type;// 不同字符串对应的字段类型： 1. 普通字符串 string 2.orderStatus  3.orderAmount 4. orderCurrency 5.orderTime 6. check payin or payout
    private String ruleType;// BankOrderColumnRuleTypeEm

    private String columnFixValue;//  orderStatus 固定值 ：SUCCESS ; orderTime固定值： 00:00:00 ; orderCurrency 固定值： USD; orderAmount 固定值：10 ; 金额处理固定值： 除以 100
    private String columnMapConfig; //单独字段.equal(设定值) => 某个Em值 {"oldColumnxx":"SUCCESS", "yy":"REFUND", "zz":"REJECTED"}

    private String zoneId; // "America/Sao_Paulo"

    private String preStringType; // 详见： OrderColumnStringTypeEm
    private String preStringRuleInfo; // pre具体配置值

    private String afterStringType; // 详见： OrderColumnStringTypeEm
    private String afterStringRuleInfo; // after具体配置值

    private String saveOriginalData;// 如果有值，则代表将当前字段存储到 saveOriginalData 设置的字段
}
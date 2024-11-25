package com.github.excelreader.handler;

import cn.hutool.core.date.DateUtil;
import com.github.excelreader.config.ColumnConfigDetail;
import com.github.excelreader.enums.ColumnTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
public class DefaultColumnFormatHandler implements ColumnFormatHandler {

    @Override
    public String format(String cellValue, ColumnConfigDetail detail) {
        if (cellValue == null) {
            return "";
        }

        try {
            String type = detail.getType();
            if (type.equals(ColumnTypeEnum.STRING.getValue())) {
                return formatString(cellValue);
            } else if (type.equals(ColumnTypeEnum.NUMBER.getValue())) {
                return formatNumber(cellValue);
            } else if (type.equals(ColumnTypeEnum.DATE.getValue())) {
                return formatDate(cellValue, detail.getFormat());
            } else if (type.equals(ColumnTypeEnum.CURRENCY.getValue())) {
                return formatCurrency(cellValue);
            } else if (type.equals(ColumnTypeEnum.ORDER_TIME.getValue())) {
                return formatOrderTime(cellValue);
            } else if (type.equals(ColumnTypeEnum.ORDER_AMOUNT.getValue())) {
                return formatOrderAmount(cellValue);
            }
            return cellValue;
        } catch (Exception e) {
            log.error("格式化单元格值失败: value={}, type={}", cellValue, detail.getType(), e);
            return cellValue;
        }
    }

    protected String formatString(String value) {
        return value.trim();
    }

    protected String formatNumber(String value) {
        return value.replaceAll("[^\\d.]", "");
    }

    protected String formatDate(String value, String pattern) {
        try {
            Date date = DateUtil.parse(value);
            return DateUtil.format(date, pattern);
        } catch (Exception e) {
            return value;
        }
    }

    protected String formatCurrency(String value) {
        try {
            return new BigDecimal(value.replaceAll("[^\\d.-]", "")).toString();
        } catch (Exception e) {
            return "0";
        }
    }

    protected String formatOrderTime(String value) {
        try {
            Date date = DateUtil.parse(value);
            return String.valueOf(date.getTime());
        } catch (Exception e) {
            return value;
        }
    }

    protected String formatOrderAmount(String value) {
        try {
            return new BigDecimal(value.replaceAll("[^\\d.-]", ""))
                    .setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toString();
        } catch (Exception e) {
            return "0.00";
        }
    }
}
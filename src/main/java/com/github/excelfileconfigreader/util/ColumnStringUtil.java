package com.github.excelfileconfigreader.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.github.excelfileconfigreader.config.ColumnConfigDetail;
import com.github.excelfileconfigreader.enums.OrderColumnRuleTypeEm;
import com.github.excelfileconfigreader.enums.OrderColumnStringTypeEm;
import com.github.excelfileconfigreader.enums.OrderColumnTypeEm;
import com.github.excelfileconfigreader.enums.OrderStatusEm;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class ColumnStringUtil {

    private ColumnStringUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static final Map<String, String> MONTH_MAP = new HashMap<>();
    
    static {
        MONTH_MAP.put("Jan", "01");
        MONTH_MAP.put("Feb", "02");
        MONTH_MAP.put("Mar", "03");
        MONTH_MAP.put("Apr", "04");
        MONTH_MAP.put("May", "05");
        MONTH_MAP.put("Jun", "06");
        MONTH_MAP.put("Jul", "07");
        MONTH_MAP.put("Aug", "08");
        MONTH_MAP.put("Sep", "09");
        MONTH_MAP.put("Oct", "10");
        MONTH_MAP.put("Nov", "11");
        MONTH_MAP.put("Dec", "12");
    }

    public static String formatString(String cellValue, String stringType, String stringRule) {
        if (StrUtil.isEmpty(cellValue)) {
            return "";
        }
        String result = cellValue;
        // 如果是多组字符处理，是否需要设定先后顺序
        if (stringType.contains(OrderColumnStringTypeEm.DEL_PRE.getValue())) {
            result = result.substring(Integer.parseInt(stringRule));
        }
        if (stringType.contains(OrderColumnStringTypeEm.DEL_AFTER.getValue())) {
            result = result.substring(0, result.length() - Integer.parseInt(stringRule));
        }
        if (stringType.contains(OrderColumnStringTypeEm.DEL_CHAR.getValue())) {
            result = result.replace(stringRule, "");
        }
        if (stringType.contains(OrderColumnStringTypeEm.REPLACE_TWO_CHAR.getValue())) {
            result = result.replace(stringRule.substring(0, 1), stringRule.substring(1, 2));
        }
        if (stringType.contains(OrderColumnStringTypeEm.DIVIDE_NUMBER.getValue())) {
            result = new BigDecimal(result).divide(new BigDecimal(stringRule), 2, RoundingMode.HALF_UP).toString();
        }
        if (stringType.contains(OrderColumnStringTypeEm.ABS_VALUE.getValue())) {
            result = new BigDecimal(result).abs().toString();
        }
        if (stringType.contains(OrderColumnStringTypeEm.ADD_CHAR_PRE.getValue())) {
            result = stringRule + result;
        }
        if (stringType.contains(OrderColumnStringTypeEm.ADD_CHAR_AFTER.getValue())) {
            result = result + stringRule;
        }
        if (stringType.contains(OrderColumnStringTypeEm.XENDIT_TIME.getValue())) {
            // 处理类似 "16 Aug 2024" 格式的日期
            for (Map.Entry<String, String> entry : MONTH_MAP.entrySet()) {
                if (result.contains(entry.getKey())) {
                    result = result.replace(entry.getKey(), entry.getValue());
                    break;
                }
            }
            // 移除多余的空格和逗号
            result = result.replace(",", "").trim();
        }
        return result;
    }

    public static String formatRuleString(String cellValue, ColumnConfigDetail detail) {
        String type = detail.getType();
        try {
            switch (OrderColumnTypeEm.toInfo(type)) {
                case ORDER_STATUS:
                    cellValue = formatOrderStatus(cellValue, detail);
                    break;
                case ORDER_AMOUNT:
                    cellValue = formatOrderAmount(cellValue, detail);
                    break;
                case ORDER_TIME:
                    cellValue = formatOrderTime(cellValue, detail);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.error("formatRuleString error,cellValue:{}, detail:{}", cellValue, detail, e);
        }
        return cellValue;
    }

    private static String formatOrderTime(String cellValue, ColumnConfigDetail detail) {
        if (StrUtil.isEmpty(cellValue)) {
            return "0";
        }
        
        ZoneId sourceZone = ZoneId.of(detail.getZoneId());
        ZoneId utcZone = ZoneId.of("UTC");
        
        if (isNormalTimeFormat(detail.getRuleType())) {
            return processNormalTimeFormat(cellValue, sourceZone, utcZone);
        }
        
        return processCustomTimeFormat(cellValue, detail, sourceZone, utcZone);
    }
    
    private static boolean isNormalTimeFormat(String ruleType) {
        return ruleType.equals(OrderColumnRuleTypeEm.ORDER_TIME_NORMAL.getValue());
    }
    
    private static String processNormalTimeFormat(String cellValue, ZoneId sourceZone, ZoneId utcZone) {
        ZonedDateTime zonedDateTime = ZonedDateTimeUtil.parseToZonedNormal(cellValue, sourceZone, utcZone);
        return zonedDateTime == null ? "0" : String.valueOf(zonedDateTime.toInstant().toEpochMilli());
    }
    
    private static String processCustomTimeFormat(String cellValue, ColumnConfigDetail detail, ZoneId sourceZone, ZoneId utcZone) {
        String orderTimeRes = cellValue;
        String format = "yyyy-MM-dd HH:mm:ss";
        
        format = processTimeFormatByRule(orderTimeRes, detail, format);
        orderTimeRes = appendTimeIfNeeded(orderTimeRes);
        
        return convertToTimestamp(orderTimeRes, format, sourceZone, utcZone);
    }
    
    private static String processTimeFormatByRule(String orderTimeRes, ColumnConfigDetail detail, String defaultFormat) {
        String format = defaultFormat;
        OrderColumnRuleTypeEm ruleType = OrderColumnRuleTypeEm.toInfo(detail.getRuleType());
        
        switch (ruleType) {
            case ORDER_TIME_FIX_SUFFIX:
                format = orderTimeRes + detail.getColumnFixValue();
                break;
            case ORDER_TIME_FORMAT:
                format = findMatchingTimeFormat(orderTimeRes, detail.getColumnMapConfig(), defaultFormat);
                break;
            default:
                break;
        }
        return format;
    }
    
    private static String findMatchingTimeFormat(String timeStr, String formatConfig, String defaultFormat) {
        if (StrUtil.isEmpty(formatConfig)) {
            return defaultFormat;
        }
        
        String[] formats = formatConfig.split(",");
        for (String format : formats) {
            if (isDateFormatValid(timeStr, format)) {
                return format;
            }
        }
        return defaultFormat;
    }
    
    private static String appendTimeIfNeeded(String timeStr) {
        if (timeStr.length() <= 10) {
            return timeStr + " 08:00:00";
        }
        return timeStr;
    }
    
    private static String convertToTimestamp(String timeStr, String format, ZoneId sourceZone, ZoneId utcZone) {
        ZonedDateTime zonedDateTime = ZonedDateTimeUtil.parseToZoned(timeStr, format, sourceZone, utcZone);
        return zonedDateTime == null ? "0" : String.valueOf(zonedDateTime.toInstant().toEpochMilli());
    }

    private static String formatOrderAmount(String cellValue, ColumnConfigDetail detail) {
        String orderAmount = StrUtil.isEmpty(cellValue) ? "0" : cellValue;
        String ruleType = detail.getRuleType();

        switch (OrderColumnRuleTypeEm.toInfo(ruleType)) {
            case ORDER_AMOUNT_FIX:
                orderAmount = detail.getColumnFixValue();
                break;
            case ORDER_AMOUNT_DIVIDE_NUMBER:
                String divideStr = detail.getColumnFixValue();
                orderAmount = new BigDecimal(orderAmount).divide(new BigDecimal(divideStr), 2, RoundingMode.HALF_UP).toString();
                break;
            case ORDER_AMOUNT_MAP:
                String mapConfig = detail.getColumnMapConfig();
                if (JSONUtil.isTypeJSONObject(mapConfig)) {
                    @SuppressWarnings("unchecked")
                    Map<String, String> orderStatusMapping = JSONUtil.parseObj(mapConfig).toBean(Map.class);
                    orderAmount = orderStatusMapping.getOrDefault(detail.getOldColumn(), orderAmount);
                } else {
                    log.warn("Invalid JSON format in columnMapConfig: {}", mapConfig);
                }
                break;
            default:
                break;
        }
        return orderAmount;
    }

    private static String formatOrderStatus(String cellValue, ColumnConfigDetail detail) {
        String orderStatusRes = cellValue;
        String ruleType = detail.getRuleType();
        
        switch (OrderColumnRuleTypeEm.toInfo(ruleType)) {
            case ORDER_STATUS_FIX:
                orderStatusRes = detail.getColumnFixValue();
                break;
            case ORDER_STATUS_MAP:
                String mapConfig = detail.getColumnMapConfig();
                if (JSONUtil.isTypeJSONObject(mapConfig)) {
                    @SuppressWarnings("unchecked")
                    Map<String, String> orderStatusMapping = JSONUtil.parseObj(mapConfig).toBean(Map.class);
                    orderStatusRes = orderStatusMapping.getOrDefault(cellValue, OrderStatusEm.UNKNOWN.getValue());
                } else {
                    log.warn("Invalid JSON format in columnMapConfig: {}", mapConfig);
                    orderStatusRes = OrderStatusEm.UNKNOWN.getValue();
                }
                break;
            case ORDER_STATUS_MULTI:
                break;
            default:
                break;
        }
        return orderStatusRes;
    }

    public static boolean isDateFormatValid(String dateTimeString, String dateTimeFormat) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormat);
            LocalDateTime.parse(dateTimeString, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}

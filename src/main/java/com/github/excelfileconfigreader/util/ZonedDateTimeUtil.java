package com.github.excelfileconfigreader.util;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class ZonedDateTimeUtil {

    public static ZonedDateTime parseToZoned(String timeString, String format, ZoneId sourceZoneId, ZoneId targetZoneId) {
        // 定义日期时间格式模式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime localDateTime = null;
        // 解析时间字符串为 LocalDateTime 对象
        try {
            localDateTime = LocalDateTime.parse(timeString, formatter);
        } catch (Exception e) {
            return null;
        }
        // 将原时间转换为 sourceZoneId 时区的 ZonedDateTime 对象
        ZonedDateTime sourceZonedDateTime = localDateTime.atZone(sourceZoneId);
        // 将 sourceZoneId 时区的 ZonedDateTime 对象转换为 targetZoneId 时区的 ZonedDateTime 对象
        ZonedDateTime targetZonedDateTime = sourceZonedDateTime.withZoneSameInstant(targetZoneId);
        // 输出结果
//        log.debug("源数据 Time: " + timeString);
//        log.debug("转化后 Time: " + targetZonedDateTime.toString());
        return targetZonedDateTime;
    }

    public static ZonedDateTime parseToZonedNormal(String timeString, ZoneId sourceZoneId, ZoneId targetZoneId) {
       // 解析时间字符串为 LocalDateTime 对象
        LocalDateTime localDateTime;
        try {
            localDateTime = DateUtil.parse(timeString).toLocalDateTime();
        } catch (Exception e) {
            return null;
        }
        // 将原时间转换为 sourceZoneId 时区的 ZonedDateTime 对象
        ZonedDateTime sourceZonedDateTime = localDateTime.atZone(sourceZoneId);
        // 将 sourceZoneId 时区的 ZonedDateTime 对象转换为 targetZoneId 时区的 ZonedDateTime 对象
        ZonedDateTime targetZonedDateTime = sourceZonedDateTime.withZoneSameInstant(targetZoneId);
        // 输出结果
//        log.debug("源数据 Time: " + timeString);
//        log.debug("转化后 Time: " + targetZonedDateTime.toString());
        return targetZonedDateTime;
    }
}

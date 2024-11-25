package com.github.excelreader.core;

import com.github.excelreader.model.ExcelData;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public class DataMapper {

    public void setFieldValueByFieldName(String fieldName, String value, ExcelData target) {
        try {
            Field field = ExcelData.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            log.error("设置字段值失败: fieldName={}, value={}", fieldName, value, e);
        }
    }

    public String getFieldValueByFieldName(String fieldName, ExcelData source) {
        try {
            Field field = ExcelData.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(source);
            return value == null ? "" : value.toString();
        } catch (Exception e) {
            log.error("获取字段值失败: fieldName={}", fieldName, e);
            return "";
        }
    }
}
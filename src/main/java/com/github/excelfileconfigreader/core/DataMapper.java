package com.github.excelfileconfigreader.core;

import com.github.excelfileconfigreader.model.ExcelData;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public class DataMapper {

    /**
     * 使用反射设置字段值
     * 由于需要处理私有字段的映射，这里必须使用反射来修改字段的可访问性
     */
    public void setFieldValueByFieldName(String fieldName, String value, ExcelData target) {
        try {
            Field field = ExcelData.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            log.error("设置字段值失败: fieldName={}, value={}", fieldName, value, e);
        }
    }
}
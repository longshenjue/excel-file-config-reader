package com.github.excelfileconfigreader.util;

import com.github.excelfileconfigreader.config.ColumnConfigDetail;
import com.github.excelfileconfigreader.enums.OrderColumnRuleTypeEm;
import com.github.excelfileconfigreader.enums.OrderColumnStringTypeEm;
import com.github.excelfileconfigreader.enums.OrderColumnTypeEm;
import com.github.excelfileconfigreader.enums.OrderStatusEm;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ColumnStringUtilTest {

    @Test
    void testFormatString() {
        // 测试删除前缀字符
        assertEquals("456", ColumnStringUtil.formatString("123456",
            OrderColumnStringTypeEm.DEL_PRE.getValue(), "3"));
            
        // 测试删除后缀字符
        assertEquals("123", ColumnStringUtil.formatString("123456", 
            OrderColumnStringTypeEm.DEL_AFTER.getValue(), "3"));
            
        // 测试删除指定字符
        assertEquals("13456", ColumnStringUtil.formatString("123456",
            OrderColumnStringTypeEm.DEL_CHAR.getValue(), "2"));
            
        // 测试字符替换
        assertEquals("hello", ColumnStringUtil.formatString("hallo", 
            OrderColumnStringTypeEm.REPLACE_TWO_CHAR.getValue(), "ae"));
            
        // 测试数字除法
        assertEquals("50.00", ColumnStringUtil.formatString("100", 
            OrderColumnStringTypeEm.DIVIDE_NUMBER.getValue(), "2"));
            
        // 测试绝对值
        assertEquals("100", ColumnStringUtil.formatString("-100", 
            OrderColumnStringTypeEm.ABS_VALUE.getValue(), ""));
            
        // 测试添加前缀
        assertEquals("pre123", ColumnStringUtil.formatString("123", 
            OrderColumnStringTypeEm.ADD_CHAR_PRE.getValue(), "pre"));
            
        // 测试添加后缀
        assertEquals("123post", ColumnStringUtil.formatString("123", 
            OrderColumnStringTypeEm.ADD_CHAR_AFTER.getValue(), "post"));
            
        // 测试时间格式转换
        assertEquals("16 01 2024", ColumnStringUtil.formatString("16 Jan 2024", 
            OrderColumnStringTypeEm.XENDIT_TIME.getValue(), ""));
    }

    @Test
    void testFormatRuleString() {
        // 测试订单状态格式化
        ColumnConfigDetail statusDetail = new ColumnConfigDetail();
        statusDetail.setType(OrderColumnTypeEm.ORDER_STATUS.getValue());
        statusDetail.setRuleType(OrderColumnRuleTypeEm.ORDER_STATUS_FIX.getValue());
        statusDetail.setColumnFixValue(OrderStatusEm.SUCCESS.getValue());
        
        assertEquals(OrderStatusEm.SUCCESS.getValue(), 
            ColumnStringUtil.formatRuleString("any", statusDetail));

        // 测试订单金额格式化
        ColumnConfigDetail amountDetail = new ColumnConfigDetail();
        amountDetail.setType(OrderColumnTypeEm.ORDER_AMOUNT.getValue());
        amountDetail.setRuleType(OrderColumnRuleTypeEm.ORDER_AMOUNT_DIVIDE_NUMBER.getValue());
        amountDetail.setColumnFixValue("100");
        
        assertEquals("1.00", 
            ColumnStringUtil.formatRuleString("100", amountDetail));

        // 测试订单时间格式化
        ColumnConfigDetail timeDetail = new ColumnConfigDetail();
        timeDetail.setType(OrderColumnTypeEm.ORDER_TIME.getValue());
        timeDetail.setRuleType(OrderColumnRuleTypeEm.ORDER_TIME_NORMAL.getValue());
        timeDetail.setZoneId("Asia/Shanghai");
        
        String timestamp = ColumnStringUtil.formatRuleString("2024-01-16 10:00:00", timeDetail);
        assertNotEquals("0", timestamp);
        assertTrue(Long.parseLong(timestamp) > 0);
    }

    @Test
    void testFormatOrderStatus() {
        // 测试状态映射
        ColumnConfigDetail mapDetail = new ColumnConfigDetail();
        mapDetail.setType(OrderColumnTypeEm.ORDER_STATUS.getValue());
        mapDetail.setRuleType(OrderColumnRuleTypeEm.ORDER_STATUS_MAP.getValue());
        mapDetail.setColumnMapConfig("{\"COMPLETED\":\"SUCCESS\",\"FAILED\":\"FAIL\"}");
        
        assertEquals("SUCCESS", 
            ColumnStringUtil.formatRuleString("COMPLETED", mapDetail));
        assertEquals("FAIL", 
            ColumnStringUtil.formatRuleString("FAILED", mapDetail));
        assertEquals(OrderStatusEm.UNKNOWN.getValue(), 
            ColumnStringUtil.formatRuleString("INVALID_STATUS", mapDetail));
    }

    @Test
    void testFormatOrderTime() {
        // 测试不同时区的时间转换
        ColumnConfigDetail timeDetail = new ColumnConfigDetail();
        timeDetail.setType(OrderColumnTypeEm.ORDER_TIME.getValue());
        timeDetail.setRuleType(OrderColumnRuleTypeEm.ORDER_TIME_FORMAT.getValue());
        timeDetail.setZoneId("Asia/Shanghai");
        timeDetail.setColumnMapConfig("yyyy-MM-dd HH:mm:ss,dd/MM/yyyy HH:mm:ss");
        
        String timestamp1 = ColumnStringUtil.formatRuleString("2024-01-16 10:00:00", timeDetail);
        String timestamp2 = ColumnStringUtil.formatRuleString("16/01/2024 10:00:00", timeDetail);
        
        assertNotEquals("0", timestamp1);
        assertNotEquals("0", timestamp2);
        assertEquals(timestamp1, timestamp2);
    }

    @Test
    void testInvalidInputs() {
        // 测试空输入
        assertEquals("", ColumnStringUtil.formatString("", 
            OrderColumnStringTypeEm.DEL_PRE.getValue(), "3"));
            
        // 测试无效的JSON配置
        ColumnConfigDetail detail = new ColumnConfigDetail();
        detail.setType(OrderColumnTypeEm.ORDER_STATUS.getValue());
        detail.setRuleType(OrderColumnRuleTypeEm.ORDER_STATUS_MAP.getValue());
        detail.setColumnMapConfig("invalid_json");
        
        assertEquals(OrderStatusEm.UNKNOWN.getValue(), 
            ColumnStringUtil.formatRuleString("any", detail));
    }
} 
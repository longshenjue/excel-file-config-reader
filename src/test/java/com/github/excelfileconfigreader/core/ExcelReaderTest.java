package com.github.excelfileconfigreader.core;

import com.github.excelfileconfigreader.config.ColumnConfig;
import com.github.excelfileconfigreader.handler.DefaultColumnFormatHandler;
import com.github.excelfileconfigreader.model.ExcelData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExcelReaderTest {
    
    private ExcelReader excelReader;
    
    @BeforeEach
    void setUp() {
        excelReader = new ExcelReader(new DefaultColumnFormatHandler());
    }
    
    private String getFileSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
    
    @Test
    void testReadXlsxFile() {
        // 准备测试数据
        String columnConfig = "{"
            + "\"订单号\": {"
            + "    \"type\": \"OrderString\","
            + "    \"newColumn\": \"orderId\","
            + "    \"oldColumn\": \"订单号\""
            + "},"
            + "\"状态\": {"
            + "    \"type\": \"OrderStatus\","
            + "    \"newColumn\": \"status\","
            + "    \"oldColumn\": \"状态\","
            + "    \"ruleType\": \"ORDER_STATUS_MAP\","
            + "    \"columnMapConfig\": {\"成功\":\"SUCCESS\",\"失败\":\"FAIL\"}"
            + "},"
            + "\"金额\": {"
            + "    \"type\": \"OrderAmount\","
            + "    \"newColumn\": \"amount\","
            + "    \"oldColumn\": \"金额\","
            + "    \"ruleType\": \"ORDER_AMOUNT_DIVIDE_NUMBER\","
            + "    \"columnFixValue\": \"100\""
            + "}"
            + "}";
            
        ColumnConfig config = new ColumnConfig();
        config.setColumnConfig(columnConfig);
        config.setSuffix("xlsx");
        config.setHeadNumber(1);

        // 获取资源的 URL
        ClassLoader classLoader = ExcelReader.class.getClassLoader();
        // 获取资源的 URL
        File file = new File(classLoader.getResource("test-orders.xlsx").getFile());
        // 读取测试文件
        try (FileInputStream is = new FileInputStream(file)) {
            List<ExcelData> result = excelReader.readData(is, config, true);
            
            // 验证结果
            assertNotNull(result);
            assertFalse(result.isEmpty());
            
            // 验证第一条数据
            ExcelData firstData = result.get(0);
            assertEquals("ORDER001", firstData.getOrderId());
            assertEquals("SUCCESS", firstData.getStatus());
            assertEquals("1.00", firstData.getAmount());
        } catch (Exception e) {
            fail("Should not throw exception", e);
        }
    }
    
    @Test
    void testReadCsvFile() {
        String columnConfig = "{"
            + "\"Order No\": {"
            + "    \"type\": \"OrderString\","
            + "    \"newColumn\": \"orderId\","
            + "    \"oldColumn\": \"Order No\""
            + "},"
            + "\"Time\": {"
            + "    \"type\": \"OrderTime\","
            + "    \"newColumn\": \"orderTime\","
            + "    \"oldColumn\": \"Time\","
            + "    \"ruleType\": \"ORDER_TIME_NORMAL\","
            + "    \"zoneId\": \"Asia/Shanghai\""
            + "}"
            + "}";

        String fileName = "test.csv";
        ColumnConfig config = new ColumnConfig();
        config.setColumnConfig(columnConfig);
        config.setHeadNumber(1);
        config.setSeparator(",");
        // 设置文件后缀
        config.setSuffix(getFileSuffix(fileName));

        // 获取资源的 URL
        ClassLoader classLoader = ExcelReader.class.getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());


        try (FileInputStream is = new FileInputStream(file)) {
            List<ExcelData> result = excelReader.readData(is, config, false);
            
            assertNotNull(result);
            assertFalse(result.isEmpty());
            
            ExcelData firstData = result.get(0);
            assertNotNull(firstData.getOrderId());
            assertTrue(Long.parseLong(firstData.getOrderTime()) > 0);
        } catch (Exception e) {
            fail("Should not throw exception", e);
        }
    }
    
    @Test
    void testInvalidJsonConfig() {
        ColumnConfig config = new ColumnConfig();
        config.setColumnConfig("invalid json");
        config.setSuffix(".xlsx");
        config.setHeadNumber(1);

        ClassLoader classLoader = ExcelReader.class.getClassLoader();
        File file = new File(classLoader.getResource("test-orders.xlsx").getFile());
        try (FileInputStream is = new FileInputStream(file)) {
            List<ExcelData> result = excelReader.readData(is, config, false);
            
            // 即使配置无效，也应该返回空列表而不是抛出异常
            assertNotNull(result);
        } catch (Exception e) {
            fail("Should not throw exception", e);
        }
    }
    
    @Test
    void testMissingColumns() {
        // 配置中包含文件中不存在的列
        String columnConfig = "{"
            + "\"不存在的列\": {"
            + "    \"type\": \"OrderString\","
            + "    \"newColumn\": \"orderId\","
            + "    \"oldColumn\": \"不存在的列\""
            + "}"
            + "}";
            
        ColumnConfig config = new ColumnConfig();
        config.setColumnConfig(columnConfig);
        config.setSuffix("xlsx");
        config.setHeadNumber(1);

        ClassLoader classLoader = ExcelReader.class.getClassLoader();
        File file = new File(classLoader.getResource("test-orders.xlsx").getFile());
        try (FileInputStream is = new FileInputStream(file)) {
            List<ExcelData> result = excelReader.readData(is, config, false);
            
            assertNotNull(result);
            assertFalse(result.isEmpty());
            
            // 不存在的列应该被忽略
            ExcelData firstData = result.get(0);
            assertNull(firstData.getOrderId());
        } catch (Exception e) {
            fail("Should not throw exception", e);
        }
    }
} 
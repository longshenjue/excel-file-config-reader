# Excel Reader Utils

基于 easyexcel 的 Excel 文件读取工具， 可以通过自定义列映射的方式读取 Excel 文件

## 特性

- 支持Excel文件格式：XLSX、CSV
- 支持自定义列映射配置： 详见 OrderColumnRuleTypeEm 
-     ORDER_STATUS_MAP("ORDER_STATUS_MAP", OrderColumnTypeEm.ORDER_STATUS.getValue(), "状态-映射配置值"),
- 支持数据格式化处理：  详见 OrderColumnStringTypeEm
-     DEL_PRE("DEL_PRE", "删除前置的x个字符"),
-     DEL_AFTER("DEL_AFTER", "删除后置的x个字符"),
-     DEL_CHAR("DEL_CHAR", "删除某个特定字符")
- 支持原始数据保存: saveOriginalData
- 基于EasyExcel，性能优异

## 快速开始
- 使用方法： 参考 ExcelReaderTest


````
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
        config.setSuffix(".xlsx");// 注意这里需要加一个 .
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
````

### Maven依赖
- easyexcel
- hutool
- lombok
- slf4j
- junit
package com.github.excelreader.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelData {
    private String orderId;
    private String orderTime;
    private String amount;
    private String currency;
    private String status;
    private String type;
    private String remark;
    private String originalData;
}
package com.github.excelfileconfigreader.handler;

import cn.hutool.core.util.StrUtil;
import com.github.excelfileconfigreader.config.ColumnConfigDetail;
import com.github.excelfileconfigreader.enums.OrderColumnTypeEm;
import com.github.excelfileconfigreader.util.ColumnStringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class DefaultColumnFormatHandler implements ColumnFormatHandler {

    private String cellValue;
    private String type;
    private ColumnConfigDetail detail;

    private String preStringType;
    private String preStringRuleInfo;

    private String afterStringType;
    private String afterStringRuleInfo;

    public String format(String cellValue, ColumnConfigDetail detail) {
        this.cellValue = cellValue;
        this.type = detail.getType();
        this.preStringType = detail.getPreStringType();
        this.preStringRuleInfo = detail.getPreStringRuleInfo();

        this.afterStringType = detail.getAfterStringType();
        this.afterStringRuleInfo = detail.getAfterStringRuleInfo();

        this.detail = detail;

        // 依次调用方法
        this.preStringProcess();
        this.columnRuleProcess();
        this.afterStringProcess();

        return this.cellValue;
    }

    private void preStringProcess() {
        if (StrUtil.isNotEmpty(this.preStringType)) {
            this.cellValue = ColumnStringUtil.formatString(this.cellValue, this.preStringType, this.preStringRuleInfo);
        }
    }

    private void columnRuleProcess() {
        if (StrUtil.isNotEmpty(this.type) && StrUtil.isNotEmpty(this.detail.getRuleType()) && !Objects.equals(this.type, OrderColumnTypeEm.ORDER_STRING.getValue())) {
            this.cellValue = ColumnStringUtil.formatRuleString(this.cellValue, this.detail);
        }
    }

    private void afterStringProcess() {
        if (StrUtil.isNotEmpty(this.afterStringType)) {
            this.cellValue = ColumnStringUtil.formatString(this.cellValue, this.afterStringType, this.afterStringRuleInfo);
        }
    }


}
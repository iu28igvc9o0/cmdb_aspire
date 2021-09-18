package com.aspire.ums.cmdb.cmic.payload;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

/**
 * excel
 *
 * @author jiangxuwen
 * @date 2020/7/6 17:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExcelData implements Serializable {

    private static final long serialVersionUID = 382029451047203164L;

    private String fieldCode;

    private String fieldName;

    private String fieldType;

    private Integer colIndex;

    /** parent or child. */
    private String levelType;

    /** 下拉值. */
    private List<Object> fieldValueList = Lists.newArrayList();

    /** 级联子表的下拉值. */
    private List<ExcelData> childList = Lists.newArrayList();

    /** 级联下拉的父节点当前值. */
    private String parentFieldValue;

    /** 是否必填. */
    private boolean required;

    private ExcelData parent;

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getFieldCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ExcelData)) {
            return false;
        }
        ExcelData that = (ExcelData) obj;
        return Objects.equal(this.getFieldCode(), that.getFieldCode());
    }
}

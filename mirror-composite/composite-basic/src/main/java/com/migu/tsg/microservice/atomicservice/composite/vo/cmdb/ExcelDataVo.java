package com.migu.tsg.microservice.atomicservice.composite.vo.cmdb;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

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
public class ExcelDataVo implements Serializable {

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
    private List<ExcelDataVo> childList = Lists.newArrayList();

    /** 级联下拉的父节点当前值. */
    private String parentFieldValue;

    /** 是否必填. */
    private boolean required;

    private ExcelDataVo parent;

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getFieldCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ExcelDataVo)) {
            return false;
        }
        ExcelDataVo that = (ExcelDataVo) obj;
        return Objects.equal(this.getFieldCode(), that.getFieldCode());
    }
}

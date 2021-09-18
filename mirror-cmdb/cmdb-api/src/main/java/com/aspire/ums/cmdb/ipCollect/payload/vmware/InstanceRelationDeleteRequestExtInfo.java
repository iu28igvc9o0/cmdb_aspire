package com.aspire.ums.cmdb.ipCollect.payload.vmware;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import lombok.Data;

/**
 * 云管实例关联关系删除extInfo.
 *
 * @author jiangxuwen
 * @date 2020/3/11 14:37
 */
@Data
public class InstanceRelationDeleteRequestExtInfo extends BaseInstanceRelationRequestExtInfo implements Serializable {

    private static final long serialVersionUID = 1725663982659931462L;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}

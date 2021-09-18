package com.aspire.ums.cmdb.ipCollect.payload.vmware;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import lombok.Data;

/**
 * 云管实例关联关系删除.
 *
 * @author jiangxuwen
 * @date 2020/3/11 14:35
 */
@Data
public class InstanceRelationDeleteRequest extends BaseInstanceRequest<InstanceRelationDeleteRequestExtInfo>
        implements Serializable {

    private static final long serialVersionUID = 4112197671887758068L;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}

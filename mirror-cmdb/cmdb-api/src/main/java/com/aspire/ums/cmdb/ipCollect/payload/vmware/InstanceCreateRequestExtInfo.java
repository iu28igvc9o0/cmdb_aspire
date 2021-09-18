package com.aspire.ums.cmdb.ipCollect.payload.vmware;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 云管实例新增
 * @Author: fanshenquan
 * @Datetime: 2020/5/18 14:25
 */
@Data
@ToString
public class InstanceCreateRequestExtInfo extends BaseInstanceRequestExtInfo implements Serializable {
    private static final long serialVersionUID = 9088683530762762497L;
}

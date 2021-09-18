package com.aspire.ums.cmdb.ipCollect.payload.vmware;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 云管实例删除.
 * @Author: fanshenquan
 * @Datetime: 2020/5/18 17:03
 */
@Data
@ToString
public class InstanceDeleteRequestExtInfo extends BaseInstanceRequestExtInfo implements Serializable {
    private static final long serialVersionUID = -3159341895806514063L;
}

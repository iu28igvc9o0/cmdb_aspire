package com.aspire.ums.cmdb.ipCollect.payload.vmware;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 云管实例删除回调请求.
 *
 * @Author: fanshenquan
 * @Datetime: 2020/5/18 17:04
 */
@Data
@ToString
public class InstanceDeleteRequest extends BaseInstanceRequest<InstanceDeleteRequestExtInfo> implements Serializable {
    private static final long serialVersionUID = 3555953935249668732L;
}

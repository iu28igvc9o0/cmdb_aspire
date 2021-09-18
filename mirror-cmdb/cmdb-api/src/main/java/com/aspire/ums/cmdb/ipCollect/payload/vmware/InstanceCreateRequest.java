package com.aspire.ums.cmdb.ipCollect.payload.vmware;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 云管vmware Instance回调新增请求.
 * @Author: fanshenquan
 * @Datetime: 2020/5/18 14:24
 */
@Data
@ToString
public class InstanceCreateRequest extends BaseInstanceRequest<InstanceCreateRequestExtInfo> implements Serializable {
    private static final long serialVersionUID = 8502005474339170316L;
}

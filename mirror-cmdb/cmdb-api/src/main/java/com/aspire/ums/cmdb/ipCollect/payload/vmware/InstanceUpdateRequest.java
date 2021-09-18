package com.aspire.ums.cmdb.ipCollect.payload.vmware;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/5/18 15:08
 */
@Data
@ToString
public class InstanceUpdateRequest extends BaseInstanceRequest<InstanceUpdateRequestExtInfo> implements Serializable {
    private static final long serialVersionUID = 648006230653847137L;
}

package com.aspire.ums.cmdb.ipCollect.payload.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 存活IP实体类-网络设备配置文件解析
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/5 11:26
 */
@Data
@ToString(callSuper = true)
public class CmdbIpConfPoolEntity extends BaseIpCollectEntity implements Serializable {
    private static final long serialVersionUID = 3141620838028606070L;
}

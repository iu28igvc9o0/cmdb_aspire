package com.aspire.ums.cmdb.ipCollect.payload.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 虚拟IP实体类
 *
 * @Author: fanshenquan
 * @Datetime: 2020/5/18 18:08
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CmdbVipCollectEntity extends BaseCollectEntity implements Serializable {

    private static final long serialVersionUID = -7555617300901054064L;
    // IP地址
    private String vip;

    // 当前绑定ip
    private String bindip;

    // 飘移IP列表
    private String iplist;

    // 虚拟IP使用类型
    private String usetype;
}

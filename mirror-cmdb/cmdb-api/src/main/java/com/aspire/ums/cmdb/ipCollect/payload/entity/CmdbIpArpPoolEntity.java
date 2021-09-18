package com.aspire.ums.cmdb.ipCollect.payload.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/6/5 11:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class CmdbIpArpPoolEntity extends BaseIpCollectEntity implements Serializable {
    private static final long serialVersionUID = 4584363131788010797L;

    // 指令占用内存百分比
    private String cpu;

    // 指令占用内存百分比
    private String mem;
}

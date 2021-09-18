package com.aspire.mirror.elasticsearch.api.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * @author baiwp
 * @title: ItemDto
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/6/219:58
 */
@Data
public class IdcTypePhysicalReq {
    private String idcType;
    private String deviceType;//物理机、虚拟机
    private String sourceType;//cpu、内存
    private String bizSystem;
    private String startDate;
    private String endDate;
    private String stateTimeType;//日周月
    private String stateType;//平均、最大
    private String segmentAddr;// 网段地址
    private String department1;
    private String department2;
    
    private Map<String,List<String>> authMap;//转化的鉴权的数据
}

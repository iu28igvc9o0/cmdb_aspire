package com.aspire.mirror.composite.service.alert.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author baiwp
 * @title: ItemDto
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/6/219:58
 */
@Data
public class ComIdcTypePhysicalReq {
    private String idcType;
    private String deviceType;//物理机、虚拟机
    private String sourceType;//资源池、内存
    private String bizSystem;
    private String segmentAddr;// 网段地址

    @JsonProperty("startDate")
    private String startDate;
    @JsonProperty("endDate")
    private String endDate;
    
    private String stateTimeType;//日周月
    private String stateType;//平均、最大
    
    private int indexType;//租户：1
    
    
}

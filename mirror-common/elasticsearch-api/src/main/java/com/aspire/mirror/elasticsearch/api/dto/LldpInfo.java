package com.aspire.mirror.elasticsearch.api.dto;

import lombok.Data;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.cmdb.topo
 * 类名称:    LldpInfo.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/9/19 14:05
 * 版本:      v1.0
 */
@Data
public class LldpInfo {
    private String id;
    /**
     * 本机地址
     */
    private String host;

    /**
     * 主机名称
     */
    private String hostname;

    /**
     *设备型号
     */
    private String deviceModel;

    /**
     *设备类型
     */
    private String deviceType;

    /**
     *设备品牌
     */
    private String deviceBrand;

    /**
     *资源池英文名称
     */
    private String idccode;

    /**
     *资源池中文名称
     */
    private String poolname;

    /**
     *LLDP信息数据
     */
    private List<LldpData> lldpData;
}

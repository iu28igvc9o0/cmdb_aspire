package com.aspire.mirror.composite.service.inspection.payload;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 设备详情返回
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.inspection.payload
 * 类名称:    CompDeviceDetailResponse.java
 * 类描述:    设备详情返回
 * 创建人:    JinSu
 * 创建时间:  2018/8/15 18:56
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@ToString
public class CompDeviceDetailResponse implements Serializable {
    private static final long serialVersionUID = -3306740151483261597L;

    /**
     * ip--名称
     */
    private String name;
    /**
     * 实体Id
     */
    private String instanceId;

//    /**
//     * ip地址
//     */
//    private String keyIP;

//
//    /**
//     * 业务系统
//     */
//    private String keybizSystem;

//    /**
//     * 业务模快
//     */
//    private String keybizModel;

    //    /**
//     * 机房信息
//     */
//    private String keyroomId;
//
//    /**
//     * 设备分类
//     */
//    private String keycategoryId;
//
//    /**
//     * 厂商
//     */
//    private String keymanufactor;
    private String moduleId;
    private String insertTime;
    private String updateTime;
    private String moduleName;
    private String roomId;
    private String roomName;
    private String bizSystemName;
    private String ip;
    private String bizSystem;

}

package com.aspire.ums.cmdb.maintenance.payload;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2019-08-04 18:44:09
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbMaintenanceSoftwareRecord {

    /**
     * id
     */
    private String id;
    /**
     * 项目名称
     */
    private String project;
    /**
     * 分类
     */
    private String classify;
    /**
     * 软件维保id
     */
    private String softwareId;
    /**
     * 软件维保name
     */
    private String softwareName;
    /**
     * 服务厂商
     */
    private String company;
    /**
     * 服务开始时间
     */
    private Date serverStartTime;
    /**
     * 服务开始时间
     */
    private Date serverEndTime;
    /**
     * 服务人员
     */
    private String serverPerson;
    /**
     * 服务等级
     */
    private String serverLevel;
    /**
     * 处理时长
     */
    private String handleLong;
    /**
     * 实际人天
     */
    private String realDays;
    /**
     * 移动审批人
     */
    private String yidongApprover;
    /**
     * 运维审批人
     */
    private String devopsApprover;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 运维审批人
     */
    private String createUser;
}
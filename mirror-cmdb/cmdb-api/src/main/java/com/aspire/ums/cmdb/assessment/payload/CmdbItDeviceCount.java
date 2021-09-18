package com.aspire.ums.cmdb.assessment.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
* 描述：
* @author
* @date 2019-06-25 16:07:55
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbItDeviceCount {

    /**
     * 设备量id
     */
    private String id;
    /**
     * 省份
     */
    private String province;
    /**
     * 厂家id
     */
    private String produce;
    /**
     * 设备类型id
     */
    private String deviceTypeId;
    /**
     * 填报人
     */
    private String createUsername;

    /**
     * 填报人电话
     */
    private String createUserPhone;
    /**
     * 数量
     */
    private String count;
    /**
     * 审批状态 (审批通过，审批驳回，录入待审批)
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 所属季度
     */
    private String quarter;
    /**
     * 厂家设备评分
     */
    private List<CmdbProduceAssessment> produceAssessment;

}
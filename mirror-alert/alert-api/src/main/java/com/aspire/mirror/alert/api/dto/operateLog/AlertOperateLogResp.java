package com.aspire.mirror.alert.api.dto.operateLog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 描述：
 *
 * @author
 * @date 2019-08-14 19:35:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertOperateLogResp {

    /**
     * ID
     */
    private Long id;
    /**
     * 关联ID
     */
    private String relationId;
    /**
     * 操作模块,如告警、屏蔽、筛选等
     */
    private String operateModel;
    private String operateModelDesc;
    /**
     * 操作类型,如新增、修改等
     */
    private String operateType;
    private String operateTypeDesc;
    /**
     * 操作人
     */
    private String operater;
    /**
     * 操作时间
     */
    private Date operateTime;
    /**
     * 操作内容
     */
    private String operateContent;
    /**
     * 备注
     */
    private String remark;
}
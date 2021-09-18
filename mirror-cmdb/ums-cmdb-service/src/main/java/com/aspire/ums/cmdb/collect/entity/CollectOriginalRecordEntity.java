package com.aspire.ums.cmdb.collect.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CollectOriginalRecordEntity
 * Author:   zhu.juwang
 * Date:     2019/3/12 15:35
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
public class CollectOriginalRecordEntity {
    /*
    ID
     */
    private String id;
    /*
    采集配置ID
     */
    private String collectId;
    /**
     * 实例ID
     */
    private String instanceId;
    /**
     采集任务ID
     */
    private String taskId;
    /*
    操作方式 自动采集/手工采集/导入
     */
    private String operateType;
    /*
    操作人
     */
    private String operator;
    /*
    采集时间
     */
    private Date collectTime;
    /*
    采集状态 待处理 已更新 已通知
     */
    private String collectStatus;
    /*
    采集内容
     */
    private String collectResult;
}

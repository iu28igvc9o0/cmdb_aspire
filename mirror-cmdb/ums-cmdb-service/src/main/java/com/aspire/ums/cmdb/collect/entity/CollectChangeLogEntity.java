package com.aspire.ums.cmdb.collect.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CollectChangeLogEntity
 * Author:   zhu.juwang
 * Date:     2019/3/12 15:14
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
public class CollectChangeLogEntity {
    /*
    ID
     */
    private String id;
    /*
    模型ID
     */
    private String instanceId;
    /*
    字段ID
     */
    private String formId;
    /*
    批量处理号 同一批操作的模型，该值相同 如果是自动采集, 该值与collectId相同
     */
    private String batchId;
    /**
     * 原始值
     */
    private String oldValue;
    /**
     * 新采集值
     */
    private String currValue;
//    /*
//    改变内容
//     */
//    private String changeContent;
    /*
    操作方式 自动采集 手工更新 导入 等.
     */
    private String operaterType;
    /*
    操作人
     */
    private String operator;
    /*
    操作时间
     */
    private Date operatorTime;
}

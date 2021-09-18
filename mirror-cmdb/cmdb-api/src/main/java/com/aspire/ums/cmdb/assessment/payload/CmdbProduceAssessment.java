package com.aspire.ums.cmdb.assessment.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbProduceAssessment
 * Author:   hangfang
 * Date:     2019/7/10
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbProduceAssessment {

    private String id;
    /**
     * 设备量id
     */
    private String deviceCountId;

    /**
     * 指标name
     */
    private String metricName;

    /**
     * 得分
     */
    private Integer score;

    /**
     * 得分说明
     */
    private String scoreDescription;

}

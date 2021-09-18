package com.aspire.ums.cmdb.v2.assessment.service;

import com.aspire.ums.cmdb.assessment.payload.CmdbProduceAssessment;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICmdbProduceAssessmentService
 * Author:   hangfang
 * Date:     2019/7/10
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface ICmdbProduceAssessmentService {

    /**
     * 新增厂家设备评分
     */
    void save(List<CmdbProduceAssessment> produceAssessment);

    /**
     * 根据countid查询评分数据
     */
    List<CmdbProduceAssessment> listByCountIds(List<String> countIds);


    /**
     * 新增厂家设备评分
     */
    void approval(Integer status, String province, String quarter);

    /**
     * 根据实例获取数据
     * @param entity 实例信息
     * @return 返回
     */
    List<CmdbProduceAssessment> listByEntity(CmdbProduceAssessment entity);
}

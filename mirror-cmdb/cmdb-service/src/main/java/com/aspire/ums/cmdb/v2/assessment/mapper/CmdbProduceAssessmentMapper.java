package com.aspire.ums.cmdb.v2.assessment.mapper;

import com.aspire.ums.cmdb.assessment.payload.CmdbItDeviceCount;
import com.aspire.ums.cmdb.assessment.payload.CmdbProduceAssessment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbProduceAssessmentMapper
 * Author:   hangfang
 * Date:     2019/7/10
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Mapper
public interface CmdbProduceAssessmentMapper {

    /**
     * 根据实例获取数据
     * @param entity 实例信息
     * @return 返回
     */
    List<CmdbProduceAssessment> listByEntity(CmdbProduceAssessment entity);
    /**
     * 根据countIds查询数据
     * @param countIds
     * @return 返回
     */
    List<CmdbProduceAssessment> listByCountIds(List<String> countIds);
    /**
     * 批量新增评分
     * @param entities 实例信息
     * @return 返回
     */
    void insertByBatch(List<CmdbProduceAssessment> entities);

    /**
     * 删除评分
     * @param produce 实例信息
     * @return 返回
     */
    void delete(@Param("produce") String produce);

    /**
     * 批量删除评分
     * @param deviceCouts 实例信息
     * @return 返回
     */
    void deleteByBatch(List<String> deviceCouts);
}

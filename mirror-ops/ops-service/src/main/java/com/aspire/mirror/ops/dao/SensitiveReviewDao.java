package com.aspire.mirror.ops.dao;

import com.aspire.mirror.ops.api.domain.SensitiveReview;
import com.aspire.mirror.ops.api.domain.SensitiveReviewPageResponse;
import com.aspire.mirror.ops.api.domain.SensitiveReviewQueryModel;
import com.aspire.mirror.ops.api.domain.SensitiveReviewRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.dao
 * 类名称:    SensitiveReviewDao.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/1/20 17:16
 * 版本:      v1.0
 */
@Mapper
public interface SensitiveReviewDao {
    void saveSensitiveReview(SensitiveReview sensitiveReview);

    SensitiveReview querySensitiveReviewById(Long reviewId);

    List<SensitiveReview> querySensitiveReviewByPipelineInstanceId(Long pipelineInstanceId);

    void modifySensitiveReviewStatus(SensitiveReview sensitiveReview);

    void removeSensitiveReview(Long reviewId);

    void removeSensitiveReviewByInstanceId(Long pipelineInstanceId);

    List<SensitiveReviewPageResponse> querySensitiveReviewList(SensitiveReviewQueryModel queryParam);

    Integer querySensitiveViewListTotalSize(SensitiveReviewQueryModel queryParam);

    void saveBatchSensitiveReview(List<SensitiveReview> reviewList);

    List<Long> queryCompleteAllReviewedInstance(@Param("reviewIdList") List<Long> reviewIdList);

    void saveBatchSensitiveReviewRole(List<SensitiveReviewRole> reviewRoleList);

    List<SensitiveReview> querySensitiveReviewListByIds(@Param("reviewIds") String reviewIds);

    void modifyBySensitiveReviewApply(@Param("pipelineInstanceId") Long pipelineInstanceId);
}

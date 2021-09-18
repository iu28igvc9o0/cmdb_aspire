package com.aspire.mirror.ops.dao;

import com.aspire.mirror.ops.api.domain.SensitiveRuleWhite;
import com.aspire.mirror.ops.api.domain.SensitiveRuleWhiteQueryModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.dao
 * 类名称:    SensitiveRuleWhiteDao.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/3/9 10:38
 * 版本:      v1.0
 */
@Mapper
public interface SensitiveRuleWhiteDao {
    void updateStatusByObjectIdAndType(SensitiveRuleWhite sensitiveRuleWhite) ;

    SensitiveRuleWhite queryByRuleIdAndObjectId(SensitiveRuleWhite sensitiveRuleWhite);

    SensitiveRuleWhite queryValidWhiteByRuleIdAndObjectId(SensitiveRuleWhite sensitiveRuleWhite);

    void deleteRuleWhiteByRuleIdArray(List<Long> deleteRuleIdList);

    void insertSensitiveRuleWhite(SensitiveRuleWhite sensitiveRuleWhite);

    void updateSensitiveRuleWhite(SensitiveRuleWhite sensitiveRuleWhite);

    List<SensitiveRuleWhite> queryRuleWhiteByRuleId(Long sensitiveRuleId);

    void updateStatusByWhiteId(SensitiveRuleWhite sensitiveRule);

    int querySensitiveRuleWhiteListTotalSize(SensitiveRuleWhiteQueryModel queryParam);

    List<SensitiveRuleWhite> querySensitiveRuleWhiteList(SensitiveRuleWhiteQueryModel queryParam);

    void deleteRuleWhiteByObjectIdAndType(@Param("objectId") Long objectId, @Param("objectType") String objectType);
}

package com.aspire.mirror.ops.dao;

import com.aspire.mirror.ops.api.domain.SensitiveConfig;
import com.aspire.mirror.ops.api.domain.SensitiveConfigQueryModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 敏感指令DAO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.dao
 * 类名称:    SensitiveConfigDao.java
 * 类描述:    敏感指令DAO
 * 创建人:    JinSu
 * 创建时间:  2020/1/20 14:17
 * 版本:      v1.0
 */
@Mapper
public interface SensitiveConfigDao {
    List<SensitiveConfig> querySensitiveConfigList(SensitiveConfigQueryModel queryParam);

    Integer querySensitiveConfigListTotalSize(SensitiveConfigQueryModel queryParam);

    void insertSensitiveConfig(SensitiveConfig sensitiveConfig);

    void updateSensitiveConfig(SensitiveConfig sensitiveConfig);

    SensitiveConfig querySensitiveConfigById(Long sensitiveConfigId);

    void removeSensitiveConfig(Long sensitiveConfigId);
}

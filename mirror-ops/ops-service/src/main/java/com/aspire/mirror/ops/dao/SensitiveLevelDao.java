package com.aspire.mirror.ops.dao;

import com.aspire.mirror.ops.api.domain.SensitiveLevel;
import com.aspire.mirror.ops.api.domain.SensitiveLevelQueryModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 敏感指令级别DAO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.dao
 * 类名称:    SensitiveLevelDao
 * 类描述:    敏感指令级别DAO
 * 创建人:    JinSu
 * 创建时间:  2020/9/17 17:05
 * 版本:      v1.0
 */
@Mapper
public interface SensitiveLevelDao {
    Integer querySensitiveLevelListTotalSize(SensitiveLevelQueryModel queryParam);

    List<SensitiveLevel> querySensitiveLevelList(SensitiveLevelQueryModel queryParam);

    int updateSensitiveLevelById(SensitiveLevel sensitiveLevel);
}

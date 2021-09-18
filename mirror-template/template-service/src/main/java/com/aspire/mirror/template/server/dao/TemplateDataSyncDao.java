package com.aspire.mirror.template.server.dao;

import com.aspire.mirror.template.server.dao.po.TemplateDataSync;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 模板同步数据Dao
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.template.server.dao
 * 类名称:    TemplateDataSyncDao.java
 * 类描述:    模板同步数据Dao
 * 创建人:    JinSu
 * 创建时间:
 * 版本:      v1.0 2018/9/11 11:00
 */
@Mapper
public interface TemplateDataSyncDao {
    void insert(TemplateDataSync templateDataSync);

    List<TemplateDataSync> selectByStartSyncIdAndSyncType(@Param("startSyncId") int startSyncId, @Param("syncType") String syncType);

    void insertBatch(List<TemplateDataSync> templateDataSyncList);
}

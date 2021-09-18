package com.aspire.ums.cmdb.sync.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aspire.ums.cmdb.sync.entity.CmdbSyncReceiveMessageLog;

/**
 * @since 2020年05月13日 10:30:41
 * @author jiangxuwen
 * @version v1.0
 */
@Mapper
public interface CmdbSyncReceiveMessageLogMapper {

    /**
     * 根据主键查询对象
     * 
     * @param id
     * @return
     * @since 2020年05月13日 10:30:41
     * @author jiangxuwen
     * @version v1.0
     */
    CmdbSyncReceiveMessageLog findById(Long id);

    /**
     * 分页查询
     * 
     * @param cmdbSyncReceiveMessageLog
     * @return
     * @since 2020年05月13日 10:30:41
     * @author jiangxuwen
     * @version v1.0
     */
    List<CmdbSyncReceiveMessageLog> findPage(CmdbSyncReceiveMessageLog cmdbSyncReceiveMessageLog);

    /**
     * 新增
     * 
     * @param cmdbSyncReceiveMessageLog
     * @since 2020年05月13日 10:30:41
     * @author jiangxuwen
     * @version v1.0
     */
    int insert(CmdbSyncReceiveMessageLog cmdbSyncReceiveMessageLog);

    /**
     * 批量插入
     * 
     * @param entityList
     *            待插入的实体列表
     * @since 2020年05月13日 10:30:41
     * @author jiangxuwen
     * @version v1.0
     */
    void batchInsert(List<CmdbSyncReceiveMessageLog> entityList);

    /**
     * 更新
     * 
     * @param cmdbSyncReceiveMessageLog
     * @since 2020年05月13日 10:30:41
     * @author jiangxuwen
     * @version v1.0
     */
    int update(CmdbSyncReceiveMessageLog cmdbSyncReceiveMessageLog);

    /**
     * 删除
     * 
     * @param id
     * @since 2020年05月13日 10:30:41
     * @author jiangxuwen
     * @version v1.0
     */
    int delete(Long id);

    /**
     * 批量删除
     * 
     * @param idList
     *            id列表
     * @since 2020年05月13日 10:30:41
     * @author jiangxuwen
     * @version v1.0
     */
    void batchDelete(List<Long> idList);
}

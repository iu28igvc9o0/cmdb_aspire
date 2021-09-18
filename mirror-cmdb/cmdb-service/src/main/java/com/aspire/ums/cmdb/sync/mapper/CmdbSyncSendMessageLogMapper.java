package com.aspire.ums.cmdb.sync.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aspire.ums.cmdb.sync.entity.CmdbSyncSendMessageLog;

/**
 * @since 2020年05月13日 10:30:42
 * @author jiangxuwen
 * @version v1.0
 */
@Mapper
public interface CmdbSyncSendMessageLogMapper {

    /**
     * 根据主键查询对象
     * 
     * @param id
     * @return
     * @since 2020年05月13日 10:30:42
     * @author jiangxuwen
     * @version v1.0
     */
    CmdbSyncSendMessageLog findById(Long id);

    /**
     * 分页查询
     * 
     * @param cmdbSyncSendMessageLog
     * @return
     * @since 2020年05月13日 10:30:42
     * @author jiangxuwen
     * @version v1.0
     */
    List<CmdbSyncSendMessageLog> findPage(CmdbSyncSendMessageLog cmdbSyncSendMessageLog);

    /**
     * 新增
     * 
     * @param cmdbSyncSendMessageLog
     * @since 2020年05月13日 10:30:42
     * @author jiangxuwen
     * @version v1.0
     */
    int insert(CmdbSyncSendMessageLog cmdbSyncSendMessageLog);

    /**
     * 批量插入
     * 
     * @param entityList
     *            待插入的实体列表
     * @since 2020年05月13日 10:30:42
     * @author jiangxuwen
     * @version v1.0
     */
    void batchInsert(List<CmdbSyncSendMessageLog> entityList);

    /**
     * 更新
     * 
     * @param cmdbSyncSendMessageLog
     * @since 2020年05月13日 10:30:42
     * @author jiangxuwen
     * @version v1.0
     */
    int update(CmdbSyncSendMessageLog cmdbSyncSendMessageLog);

    /**
     * 删除
     * 
     * @param id
     * @since 2020年05月13日 10:30:42
     * @author jiangxuwen
     * @version v1.0
     */
    int delete(Long id);

    /**
     * 批量删除
     * 
     * @param idList
     *            id列表
     * @since 2020年05月13日 10:30:42
     * @author jiangxuwen
     * @version v1.0
     */
    void batchDelete(List<Long> idList);
    
    List<CmdbSyncSendMessageLog> findRetryMsgList();
    
    void updateStatusAndRetryCount(CmdbSyncSendMessageLog messageLog);
}

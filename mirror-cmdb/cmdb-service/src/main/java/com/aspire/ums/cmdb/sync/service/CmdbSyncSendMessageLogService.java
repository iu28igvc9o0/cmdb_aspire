package com.aspire.ums.cmdb.sync.service;

import java.util.List;

import com.aspire.ums.cmdb.sync.entity.CmdbSyncSendMessageLog;

/**
 * @since 2020年05月13日 10:30:42
 * @author jiangxuwen
 * @version v1.0
 */
public interface CmdbSyncSendMessageLogService {

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
    void add(CmdbSyncSendMessageLog cmdbSyncSendMessageLog);

    /**
     * 更新
     * 
     * @param cmdbSyncSendMessageLog
     * @since 2020年05月13日 10:30:42
     * @author jiangxuwen
     * @version v1.0
     */
    void modify(CmdbSyncSendMessageLog cmdbSyncSendMessageLog);

    /**
     * 删除
     * 
     * @param id
     * @since 2020年05月13日 10:30:42
     * @author jiangxuwen
     * @version v1.0
     */
    void delete(Long id);

    List<CmdbSyncSendMessageLog> findRetryMsgList();

    void updateStatusAndRetryCount(CmdbSyncSendMessageLog messageLog);
}

package com.aspire.ums.cmdb.sync.service;

import java.util.List;

import com.aspire.ums.cmdb.sync.entity.CmdbSyncReceiveMessageLog;
import com.aspire.ums.cmdb.sync.entity.CmdbSyncSendMessageLog;

/**
 * @since 2020年05月13日 10:30:41
 * @author jiangxuwen
 * @version v1.0
 */
public interface CmdbSyncReceiveMessageLogService {

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
    void add(CmdbSyncReceiveMessageLog cmdbSyncReceiveMessageLog);

    /**
     * 保存接收的消息
     * 
     * @param
     * @return
     */
    void saveReceiveLog(CmdbSyncSendMessageLog sendMessageLog);

    /**
     * 更新
     * 
     * @param cmdbSyncReceiveMessageLog
     * @since 2020年05月13日 10:30:41
     * @author jiangxuwen
     * @version v1.0
     */
    void modify(CmdbSyncReceiveMessageLog cmdbSyncReceiveMessageLog);

    /**
     * 删除
     * 
     * @param id
     * @since 2020年05月13日 10:30:41
     * @author jiangxuwen
     * @version v1.0
     */
    void delete(Long id);

}

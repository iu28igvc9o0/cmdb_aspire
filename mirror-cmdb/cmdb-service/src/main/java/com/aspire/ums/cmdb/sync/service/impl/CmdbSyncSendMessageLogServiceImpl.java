package com.aspire.ums.cmdb.sync.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.sync.entity.CmdbSyncSendMessageLog;
import com.aspire.ums.cmdb.sync.mapper.CmdbSyncSendMessageLogMapper;
import com.aspire.ums.cmdb.sync.service.CmdbSyncSendMessageLogService;

/**
 * @since 2020年05月13日 10:30:42
 * @author jiangxuwen
 * @version v1.0
 */
@Transactional
@Service("cmdbSyncSendMessageLogService")
public class CmdbSyncSendMessageLogServiceImpl implements CmdbSyncSendMessageLogService {

    @Autowired
    private CmdbSyncSendMessageLogMapper cmdbSyncSendMessageLogMapper;

    /**
     * 根据主键查询对象
     * 
     * @param id
     * @return
     * @since 2020年05月13日 10:30:42
     * @author jiangxuwen
     * @version v1.0
     */
    @Transactional(readOnly = true)
    @Override
    public CmdbSyncSendMessageLog findById(Long id) {
        return cmdbSyncSendMessageLogMapper.findById(id);
    }

    /**
     * 分页查询
     * 
     * @param cmdbSyncSendMessageLog
     * @return
     * @since 2020年05月13日 10:30:42
     * @author jiangxuwen
     * @version v1.0
     */
    @Transactional(readOnly = true)
    @Override
    public List<CmdbSyncSendMessageLog> findPage(CmdbSyncSendMessageLog cmdbSyncSendMessageLog) {
        return cmdbSyncSendMessageLogMapper.findPage(cmdbSyncSendMessageLog);
    }

    /**
     * 新增
     * 
     * @param cmdbSyncSendMessageLog
     * @since 2020年05月13日 10:30:42
     * @author jiangxuwen
     * @version v1.0
     */
    @Override
    public void add(CmdbSyncSendMessageLog cmdbSyncSendMessageLog) {
        cmdbSyncSendMessageLogMapper.insert(cmdbSyncSendMessageLog);

    }

    /**
     * 更新
     * 
     * @param cmdbSyncSendMessageLog
     * @since 2020年05月13日 10:30:42
     * @author jiangxuwen
     * @version v1.0
     */
    @Override
    public void modify(CmdbSyncSendMessageLog cmdbSyncSendMessageLog) {
        cmdbSyncSendMessageLogMapper.update(cmdbSyncSendMessageLog);
    }

    /**
     * 删除
     * 
     * @param id
     * @since 2020年05月13日 10:30:42
     * @author jiangxuwen
     * @version v1.0
     */
    @Override
    public void delete(Long id) {
        cmdbSyncSendMessageLogMapper.delete(id);
    }

    @Override
    public List<CmdbSyncSendMessageLog> findRetryMsgList() {
        return cmdbSyncSendMessageLogMapper.findRetryMsgList();
    }

    @Override
    public void updateStatusAndRetryCount(CmdbSyncSendMessageLog messageLog) {
        cmdbSyncSendMessageLogMapper.updateStatusAndRetryCount(messageLog);
    }
}

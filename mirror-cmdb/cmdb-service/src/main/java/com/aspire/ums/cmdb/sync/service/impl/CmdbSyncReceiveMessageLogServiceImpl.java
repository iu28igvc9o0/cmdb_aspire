package com.aspire.ums.cmdb.sync.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.sync.entity.CmdbSyncReceiveMessageLog;
import com.aspire.ums.cmdb.sync.entity.CmdbSyncSendMessageLog;
import com.aspire.ums.cmdb.sync.mapper.CmdbSyncReceiveMessageLogMapper;
import com.aspire.ums.cmdb.sync.service.CmdbSyncReceiveMessageLogService;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * @since 2020年05月13日 10:30:41
 * @author jiangxuwen
 * @version v1.0
 */
@Transactional
@Service("cmdbSyncReceiveMessageLogService")
@Slf4j
public class CmdbSyncReceiveMessageLogServiceImpl implements CmdbSyncReceiveMessageLogService {

    @Autowired
    private CmdbSyncReceiveMessageLogMapper cmdbSyncReceiveMessageLogMapper;

    /**
     * 根据主键查询对象
     * 
     * @param id
     * @return
     * @since 2020年05月13日 10:30:41
     * @author jiangxuwen
     * @version v1.0
     */
    @Transactional(readOnly = true)
    @Override
    public CmdbSyncReceiveMessageLog findById(Long id) {
        return cmdbSyncReceiveMessageLogMapper.findById(id);
    }

    /**
     * 分页查询
     * 
     * @param cmdbSyncReceiveMessageLog
     * @return
     * @since 2020年05月13日 10:30:41
     * @author jiangxuwen
     * @version v1.0
     */
    @Transactional(readOnly = true)
    @Override
    public List<CmdbSyncReceiveMessageLog> findPage(CmdbSyncReceiveMessageLog cmdbSyncReceiveMessageLog) {
        return cmdbSyncReceiveMessageLogMapper.findPage(cmdbSyncReceiveMessageLog);
    }

    /**
     * 新增
     * 
     * @param cmdbSyncReceiveMessageLog
     * @since 2020年05月13日 10:30:41
     * @author jiangxuwen
     * @version v1.0
     */
    @Override
    public void add(CmdbSyncReceiveMessageLog cmdbSyncReceiveMessageLog) {
        cmdbSyncReceiveMessageLogMapper.insert(cmdbSyncReceiveMessageLog);

    }

    @Override
    public void saveReceiveLog(CmdbSyncSendMessageLog sendMessageLog) {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(CmdbSyncSendMessageLog.class, CmdbSyncReceiveMessageLog.class).field("id", "revId")
                .exclude("msgStatus").exclude("tryCount").exclude("createTime").exclude("updateTime").byDefault().register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        CmdbSyncReceiveMessageLog receiveMessageLog = mapper.map(sendMessageLog, CmdbSyncReceiveMessageLog.class);
        receiveMessageLog.setMsgHandlerStatus("0");
        cmdbSyncReceiveMessageLogMapper.insert(receiveMessageLog);
        log.info("保存接收到的消息成功!");
    }

    /**
     * 更新
     * 
     * @param cmdbSyncReceiveMessageLog
     * @since 2020年05月13日 10:30:41
     * @author jiangxuwen
     * @version v1.0
     */
    @Override
    public void modify(CmdbSyncReceiveMessageLog cmdbSyncReceiveMessageLog) {
        cmdbSyncReceiveMessageLogMapper.update(cmdbSyncReceiveMessageLog);
    }

    /**
     * 删除
     * 
     * @param id
     * @since 2020年05月13日 10:30:41
     * @author jiangxuwen
     * @version v1.0
     */
    @Override
    public void delete(Long id) {
        cmdbSyncReceiveMessageLogMapper.delete(id);
    }

}

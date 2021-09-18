package com.aspire.ums.cmdb.sync.service.producer;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.aspire.ums.cmdb.config.KafkaConfigConstant;
import com.aspire.ums.cmdb.config.SyncOsaConfig;
import com.aspire.ums.cmdb.sync.entity.CmdbSyncFieldMapping;
import com.aspire.ums.cmdb.sync.entity.CmdbSyncSendMessageLog;
import com.aspire.ums.cmdb.sync.payload.CmdbOptType;
import com.aspire.ums.cmdb.sync.service.CmdbSyncSendMessageLogService;
import com.aspire.ums.cmdb.sync.service.ICmdbSyncFieldMappingService;
import com.aspire.ums.cmdb.util.JsonMapper;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.google.common.collect.Maps;

/**
 * kafka生产者抽象类.
 *
 * @author jiangxuwen
 * @date 2020/5/13 20:53
 */
public abstract class AbstractKafkaProducerService<T> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private KafkaProducerServer kafkaProducerServer;

    private ThreadPoolTaskExecutor taskExecutor;

    private ICmdbSyncFieldMappingService cmdbSyncFieldMappingService;

    private CmdbSyncSendMessageLogService cmdbSyncSendMessageLogService;

    private SyncOsaConfig syncOsaConfig;


    public KafkaProducerServer getKafkaProducerServer() {

        if (this.kafkaProducerServer == null) {
            this.kafkaProducerServer = SpringUtils.getBean("kafkaProducerServer", KafkaProducerServer.class);
        }
        return this.kafkaProducerServer;
    }

    public void setKafkaProducerServer(KafkaProducerServer kafkaProducerServer) {
        this.kafkaProducerServer = kafkaProducerServer;
    }

    public ThreadPoolTaskExecutor getTaskExecutor() {
        if (this.taskExecutor == null) {
            this.taskExecutor = SpringUtils.getBean("taskExecutor", ThreadPoolTaskExecutor.class);
        }
        return this.taskExecutor;
    }

    public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public ICmdbSyncFieldMappingService getCmdbSyncFieldMappingService() {
        if (this.cmdbSyncFieldMappingService == null) {
            this.cmdbSyncFieldMappingService = SpringUtils.getBean("cmdbSyncFieldMappingService",
                    ICmdbSyncFieldMappingService.class);
        }
        return this.cmdbSyncFieldMappingService;
    }

    public void setCmdbSyncFieldMappingService(ICmdbSyncFieldMappingService cmdbSyncFieldMappingService) {
        this.cmdbSyncFieldMappingService = cmdbSyncFieldMappingService;
    }

    public CmdbSyncSendMessageLogService getCmdbSyncSendMessageLogService() {
        if (this.cmdbSyncSendMessageLogService == null) {
            this.cmdbSyncSendMessageLogService = SpringUtils.getBean("cmdbSyncSendMessageLogService",
                    CmdbSyncSendMessageLogService.class);
        }
        return this.cmdbSyncSendMessageLogService;
    }

    public void setCmdbSyncSendMessageLogService(CmdbSyncSendMessageLogService cmdbSyncSendMessageLogService) {
        this.cmdbSyncSendMessageLogService = cmdbSyncSendMessageLogService;
    }

    public SyncOsaConfig getSyncOsaConfig() {
        if (this.syncOsaConfig == null) {
            this.syncOsaConfig = SpringUtils.getBean(SyncOsaConfig.class);
        }
        return this.syncOsaConfig;
    }

    public void setSyncOsaConfig(SyncOsaConfig syncOsaConfig) {
        this.syncOsaConfig = syncOsaConfig;
    }

    /**
     * 保存发送的消息日志.
     *
     * @param eventType
     *            操作类型
     * @param topic
     *            kafka Topic
     * @param objectId
     *            操作表id
     * @param partitionId
     *            kafka分区id
     * @param objectName
     *            操作的表名
     * @return
     */
    public CmdbSyncSendMessageLog saveLog(CmdbOptType eventType, String topic, String moduleId, String objectId,
            String partitionId, String objectName) {
        CmdbSyncSendMessageLog messageLog = new CmdbSyncSendMessageLog();
        messageLog.setEventType(eventType.getValue());
        messageLog.setModuleId(moduleId);
        messageLog.setInstanceId("new_cmdb");
        messageLog.setMsgId(UUIDUtil.getUUID());
        messageLog.setTopic(topic);
        messageLog.setMsgStatus("0");
        messageLog.setObjectId(objectId);
        // if (!"delete".equals(eventType)) {
        // Map<String, Object> resultMap = sqlSessionTemplate.selectOne(
        // "com.aspire.birp.modules.osa.dao.config.dict.getDictConfig", objectId);
        // messageLog.setData(resultMap);
        // messageLog.setMsgBody(JSON.toJSONString(resultMap));
        // }
        T data = null;
        if (CmdbOptType.OPT_ADD == eventType || CmdbOptType.OPT_MODIFY == eventType) {
            data = getObjectData(moduleId, objectId);
        } else {
            Map<String, String> map = Maps.newHashMap();
            map.put("id", objectId);
            data = (T) map;
        }
        if (data == null) {
            logger.error("同步的data为空!!!!");
            throw new IllegalArgumentException("同步数据不能为空!!!");
        }
        messageLog.setData(data);
        // logger.info("data==={}", data);
        messageLog.setMsgBody(JsonMapper.getInstance().toJson(data));
        messageLog.setObjectName(objectName);
        messageLog.setObjectVersion(1);
        messageLog.setOperator("system");
        messageLog.setOptTime(new Date());
        messageLog.setOptDesc(eventType + "了" + objectName + "表的值");
        messageLog.setObjectKey(partitionId);
        getCmdbSyncSendMessageLogService().add(messageLog);
        return messageLog;
    }

    /**
     * 发送kafka消息.
     *
     * @param eventType
     *            操作类型
     * @param moduleId
     *            模型ID
     * @param objectId
     *            操作表id
     * @return
     */
    public void saveEventLogAndSendMsg(CmdbOptType eventType, String moduleId, String objectId) {
        boolean syncOsaFlag = getSyncOsaConfig().isSyncOsaFlag();
        // TODO:从网管模型同步数据表里面获取模型后，同步，其他模型 不同步。如业务系统模型 IP模型等不需要同步
        if (syncOsaFlag) {
            getTaskExecutor().execute(() -> sendMsg(eventType, moduleId, objectId));
        }
    }

    private void sendMsg(CmdbOptType eventType, String moduleId, String objectId) {
        boolean syncFlag = true;
        logger.info(">>>>>>> 进入发送同步消息到kafka逻辑 >>>>>>>>>>");
        if (StringUtils.isNotBlank(moduleId)) {
            List<CmdbSyncFieldMapping> moduleList = getSyncOsaModuleList();
            List<String> moduleIdList = moduleList.stream().map(e -> e.getMappingValue()).collect(Collectors.toList());
            if (moduleIdList.contains(moduleId)) {
                syncFlag = true;
            } else {
                syncFlag = false;
                logger.info("未找到该模型ID:[" + moduleId + "]对应的同步配置数据,请在[cmdb_sync_field_mapping]表内配置!!!");
            }
        }
        if (syncFlag) {
            // getTaskExecutor().execute(() -> {
            CmdbSyncSendMessageLog log = populate(moduleId);
            CmdbSyncSendMessageLog messageLog = saveLog(eventType, log.getTopic(), moduleId, objectId, objectId,
                    log.getObjectName());
            getKafkaProducerServer().sendMsgForTemplate(messageLog);
            // });
        }
    }

    public CmdbSyncSendMessageLog populate(String moduleId) {
        CmdbSyncSendMessageLog log = new CmdbSyncSendMessageLog();

        return log;
    }

    /**
     * 发送kafka消息.
     *
     * @param eventType
     *            操作类型
     * @param topic
     *            kafka Topic
     * @param objectId
     *            操作表id
     * @param partitionId
     *            kafka分区id
     * @param objectName
     *            操作的表名
     * @return
     */
    public void saveEventLogAndSendMsg(CmdbOptType eventType, String topic, String moduleId, String objectId, String partitionId,
            String objectName) {
        boolean syncOsaFlag = getSyncOsaConfig().isSyncOsaFlag();
        // TODO:从网管模型同步数据表里面获取模型后，同步，其他模型 不同步。如业务系统模型 IP模型等不需要同步
        if (syncOsaFlag) {
            getTaskExecutor().execute(() -> sendMsg(eventType, topic, moduleId, objectId, partitionId, objectName));
        }
    }

    private void sendMsg(CmdbOptType eventType, String topic, String moduleId, String objectId, String partitionId,
            String objectName) {
        boolean syncFlag = true;
        logger.info(">>>>>>> 进入发送同步消息到kafka逻辑 >>>>>>>>>>");
        if (StringUtils.isNotBlank(moduleId)) {
            List<CmdbSyncFieldMapping> moduleList = getSyncOsaModuleList();
            List<String> moduleIdList = moduleList.stream().map(e -> e.getMappingValue()).collect(Collectors.toList());
            if (moduleIdList.contains(moduleId)) {
                syncFlag = true;
            } else {
                syncFlag = false;
                logger.info("未找到该模型ID:[" + moduleId + "]对应的同步配置数据,请在[cmdb_sync_field_mapping]表内配置!!!");
            }
        }
        if (syncFlag) {
            // getTaskExecutor().execute(() -> {
                CmdbSyncSendMessageLog messageLog = saveLog(eventType, topic, moduleId, objectId, partitionId, objectName);
                getKafkaProducerServer().sendMsgForTemplate(messageLog);
            // });
        }
    }

    private List<CmdbSyncFieldMapping> getSyncOsaModuleList() {
        CmdbSyncFieldMapping query = new CmdbSyncFieldMapping();
        query.setMappingType(KafkaConfigConstant.MAPPING_MODULE);
        query.setSyncOsaFlag("1");
        return getCmdbSyncFieldMappingService().list();
    }

    /**
     * 获取模型实例的数据.
     *
     * @param
     * @return
     */
    protected abstract T getObjectData(String moduleId, String objectId);
}

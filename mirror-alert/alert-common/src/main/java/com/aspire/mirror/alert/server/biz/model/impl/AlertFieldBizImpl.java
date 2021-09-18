package com.aspire.mirror.alert.server.biz.model.impl;

import com.aspire.mirror.alert.server.dao.operateLog.AlertOperateLogMapper;
import com.aspire.mirror.alert.server.dao.operateLog.po.AlertOperateLog;
import com.aspire.mirror.alert.server.util.StringUtils;
import com.aspire.mirror.alert.server.biz.model.AlertFieldBiz;
import com.aspire.mirror.alert.server.dao.model.AlertFieldDao;
import com.aspire.mirror.alert.server.dao.model.AlertModelDao;
import com.aspire.mirror.alert.server.dao.model.po.AlertField;
import com.aspire.mirror.alert.server.vo.model.AlertFieldVo;
import com.aspire.mirror.alert.server.dao.model.po.AlertModel;
import com.aspire.mirror.common.entity.PageResponse;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Service
@Slf4j
public class AlertFieldBizImpl implements AlertFieldBiz {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private AlertFieldDao alertFieldDao;
    @Autowired
    private AlertModelDao alertModelDao;
    @Autowired
    private AlertOperateLogMapper alertOperateLogMapper;

    private final ConcurrentHashMap<String, List<AlertFieldVo>> alertModelCache = new ConcurrentHashMap<>();

    /**
    *
    * @auther baiwenping
    * @Description
    * @Date 15:47 2020/2/21
    * @Param [tableName]
    * @return java.util.List<com.aspire.mirror.alert.server.vo.model.AlertFieldRequestDTO>
    **/
    public List<AlertFieldVo> getModelFromRedis (String tableName, String sort) {
        //判断sort格式，防止sql恶意注入
        if (!StringUtils.isEmpty(sort) && !sort.matches("[a-zA-Z0-9\\.\\_\\ ,]+")) {
            sort = null;
        }
        // 经测试，由于数据量小，直接请求效率高于redis
//        List<AlertFieldRequestDTO> list = Lists.newArrayList();
//        if (redisTemplate.hasKey(tableName)) {
//            List<Object> values = redisTemplate.opsForHash().values(tableName);
//            list.addAll(PayloadParseUtil.jacksonBaseParse(AlertFieldRequestDTO.class, values));
//        } else {
            // 如果redis没有数据，则从数据库查询数据并更新redis
            List<AlertFieldVo> alertFieldList = alertFieldDao.getAlertFieldListByTableName(tableName, sort);
//            for (AlertFieldRequestDTO alertFieldDetailDTO:alertFieldList) {
//                redisTemplate.opsForHash().put(tableName, alertFieldDetailDTO.getId(), alertFieldDetailDTO);
//                redisTemplate.expire(tableName, 24, TimeUnit.HOURS);
//            }
//            list.addAll(alertFieldList);
//        }

        return  alertFieldList;
    }

    @Override
    @Transactional(rollbackFor= Exception.class)
    public void insertAlertModel(AlertFieldVo requestDTO) {

        // 设置id
        requestDTO.setId( UUID.randomUUID().toString());
        requestDTO.setCreateTime(new Date());
        try {
            // 写入数据库
            alertFieldDao.insertAlertModel(requestDTO);
            AlertModel alertModelDetail = alertModelDao.getAlertModelDetail(requestDTO.getModelId());
            Map<String, Object> map = Maps.newHashMap();
            map.put("tableName", alertModelDetail.getTableName());
            map.put("fieldCode", requestDTO.getFieldCode());
            map.put("jdbcType", requestDTO.getDataType());
            map.put("jdbcLength", StringUtils.isEmpty(requestDTO.getDataLength()) ? 0 : Integer.valueOf(requestDTO.getDataLength()));
            map.put("jdbcTip", requestDTO.getDataTip());
            alertFieldDao.addFieldColumn(map);
            // 更新redis
//            AlertModelRequestDTO alertModelDetail = alertModelDao.getAlertModelDetail(requestDTO.getModelId());
//            redisTemplate.opsForHash().put(alertModelDetail.getTableName(), requestDTO.getId(), requestDTO);
            // 添加操作日志
            AlertOperateLog alertOperateLog = new AlertOperateLog();
            alertOperateLog.setOperateContent("新增模型字段");
            alertOperateLog.setOperateModel("alert_field");
            alertOperateLog.setOperateModelDesc("告警模型字段");
            alertOperateLog.setOperater(requestDTO.getCreator());
            alertOperateLog.setOperateTime(new Date());
            alertOperateLog.setOperateType("insert");
            alertOperateLog.setOperateTypeDesc("新增模型字段");
            alertOperateLog.setRelationId(requestDTO.getId());
            alertOperateLogMapper.insert(alertOperateLog);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new RuntimeException(e);
        }

    }

    @Override
    public AlertField getAlertFieldDetailById(String id) {
        return alertFieldDao.getAlertFieldDetailById(id);
    }

    @Override
    public void deleteAlertFieldDetailById(String id, String modelId, String userName) {

        AlertModel alertModelDetail = alertModelDao.getAlertModelDetail(modelId);
        AlertField alertFieldById = alertFieldDao.getAlertFieldDetailById(id);
        // 删除数据库
        alertFieldDao.deleteAlertFieldDetailById(id,modelId);
        alertFieldDao.deleteFieldColumn(alertModelDetail.getTableName(), alertFieldById.getFieldCode());
        // 添加操作日志
        AlertOperateLog alertOperateLog = new AlertOperateLog();
        alertOperateLog.setOperateContent("删除模型字段");
        alertOperateLog.setOperateModel("alert_field");
        alertOperateLog.setOperateModelDesc("告警模型字段");
        alertOperateLog.setOperater(userName);
        alertOperateLog.setOperateTime(new Date());
        alertOperateLog.setOperateType("delete");
        alertOperateLog.setOperateTypeDesc("删除模型字段");
        alertOperateLog.setRelationId(id);
        alertOperateLogMapper.insert(alertOperateLog);
        // 删除redis
//        AlertModelRequestDTO alertModelDetail = alertModelDao.getAlertModelDetail(modelId);
//        redisTemplate.opsForHash().delete(alertModelDetail.getTableName(), id);
    }

    @Override
    @Transactional(rollbackFor= Exception.class)
    public void updateAlertField(AlertFieldVo requestDTO) {
        requestDTO.setUpdateTime(new Date());
        AlertModel alertModelDetail = alertModelDao.getAlertModelDetail(requestDTO.getModelId());
        AlertField alertFieldById = alertFieldDao.getAlertFieldDetailById(requestDTO.getId());
        try {
            // 修改数据库
            alertFieldDao.updateAlertField(requestDTO);
            if (!requestDTO.getDataLength().equals(alertFieldById.getDataLength()) ||
                    !requestDTO.getDataType().equals(alertFieldById.getDataType()) ||
                    !requestDTO.getDataTip().equals(alertFieldById.getDataTip())) {
                alertFieldDao.deleteFieldColumn(alertModelDetail.getTableName(),requestDTO.getFieldCode());
                Map<String, Object> map = Maps.newHashMap();
                map.put("tableName", alertModelDetail.getTableName());
                map.put("fieldCode", requestDTO.getFieldCode());
                map.put("jdbcType", requestDTO.getDataType());
                map.put("jdbcLength", StringUtils.isEmpty(requestDTO.getDataLength()) ? 0 : Integer.valueOf(requestDTO.getDataLength()));
                map.put("jdbcTip", requestDTO.getDataTip());
                alertFieldDao.addFieldColumn(map);
            }
            // 添加操作日志
            AlertOperateLog alertOperateLog = new AlertOperateLog();
            alertOperateLog.setOperateContent("修改模型字段");
            alertOperateLog.setOperateModel("alert_field");
            alertOperateLog.setOperateModelDesc("告警模型字段");
            alertOperateLog.setOperater(requestDTO.getUpdater());
            alertOperateLog.setOperateTime(new Date());
            alertOperateLog.setOperateType("update");
            alertOperateLog.setOperateTypeDesc("修改模型字段");
            alertOperateLog.setRelationId(requestDTO.getId());
            alertOperateLogMapper.insert(alertOperateLog);
            // 修改redis
//            AlertModelRequestDTO alertModelDetail = alertModelDao.getAlertModelDetail(requestDTO.getModelId());
//            redisTemplate.opsForHash().put(alertModelDetail.getTableName(), requestDTO.getId(), requestDTO);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResponse<AlertField> getAlertFieldListByModelId(Map<String,Object> request) {
//        Map<String, Object> param = Maps.newHashMap();
//        param.put("modelId", modelId);
//        param.put("searchText", searchText);
//        param.put("pageNum", pageNo);
//        param.put("pageSize", pageSize);
        PageResponse<AlertField> response = new PageResponse<AlertField>();
        response.setCount(alertFieldDao.getAlertFieldListCountByModelId(request));
        response.setResult(alertFieldDao.getAlertFieldListByModelId(request));
        return response;
    }

    @Override
    @Transactional(rollbackFor= Exception.class)
    public void updateLockStatus(String id, String modelId,String isLock, String userName) {
        try {
            // 更新数据库
            alertFieldDao.updateLockStatus(id,isLock);
            // 添加操作日志
            AlertOperateLog alertOperateLog = new AlertOperateLog();
            alertOperateLog.setOperateContent("更新模型字段锁定状态");
            alertOperateLog.setOperateModel("alert_field");
            alertOperateLog.setOperateModelDesc("告警模型字段");
            alertOperateLog.setOperater(userName);
            alertOperateLog.setOperateTime(new Date());
            alertOperateLog.setOperateType("update");
            alertOperateLog.setOperateTypeDesc("更新模型字段锁定状态");
            alertOperateLog.setRelationId(id);
            alertOperateLogMapper.insert(alertOperateLog);
            // 更新redis
//            AlertModelRequestDTO alertModelDetail = alertModelDao.getAlertModelDetail(modelId);
            // 更新锁定状态可以不更新redis
            // redisTemplate.opsForHash().put(alertModelDetail.getTableName(), id, );

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(rollbackFor= Exception.class)
    public void synchronizeField(String modelId, String userName) {
        try {
            // 获取 redis 数据
            AlertModel alertModelDetail = alertModelDao.getAlertModelDetail(modelId);
//            List<Object> values = redisTemplate.opsForHash().values(alertModelDetail.getTableName());
//            List<AlertFieldRequestDTO> alertFieldRequestDTOS = PayloadParseUtil.jacksonBaseParse(AlertFieldRequestDTO.class, values);
            List<AlertFieldVo> alertFieldVos = getModelFromRedis(alertModelDetail.getTableName(), null);
            for (AlertFieldVo alertFieldVo : alertFieldVos) {
                if (alertFieldVo.getFieldType().equals("2")) {
                    alertFieldDao.deleteAlertFieldDetailById(alertFieldVo.getId(), alertFieldVo.getModelId());
                    alertFieldDao.deleteFieldColumn(alertModelDetail.getTableName(), alertFieldVo.getFieldCode());
//                    redisTemplate.opsForHash().delete(alertModelDetail.getTableName(),alertFieldRequestDTO.getId());
                }
            }
            List<AlertFieldVo> alert_alerts = getModelFromRedis("alert_alerts", null);
            for (AlertFieldVo alertFieldVo : alert_alerts) {
                if (alertFieldVo.getFieldType().equals("1")) continue;
                alertFieldVo.setId(UUID.randomUUID().toString());
                alertFieldVo.setModelId(modelId);
                // 写入数据库
                alertFieldDao.insertAlertModel(alertFieldVo);
                Map<String, Object> map = Maps.newHashMap();
                map.put("tableName", alertModelDetail.getTableName());
                map.put("fieldCode", alertFieldVo.getFieldCode());
                map.put("jdbcType", alertFieldVo.getDataType());
                map.put("jdbcLength", StringUtils.isEmpty(alertFieldVo.getDataLength()) ? 0 : Integer.valueOf(alertFieldVo.getDataLength()));
                map.put("jdbcTip", alertFieldVo.getDataTip());
                alertFieldDao.addFieldColumn(map);
                // 更新redis
//                redisTemplate.opsForHash().put(alertModelDetail.getTableName(), alertFieldRequestDTO.getId(), alertFieldRequestDTO);
            }
            // 添加操作日志
            AlertOperateLog alertOperateLog = new AlertOperateLog();
            alertOperateLog.setOperateContent("同步模型字段");
            alertOperateLog.setOperateModel("alert_field");
            alertOperateLog.setOperateModelDesc("告警模型字段");
            alertOperateLog.setOperater(userName);
            alertOperateLog.setOperateTime(new Date());
            alertOperateLog.setOperateType("insert");
            alertOperateLog.setOperateTypeDesc("同步模型字段");
            alertOperateLog.setRelationId(modelId);
            alertOperateLogMapper.insert(alertOperateLog);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new RuntimeException(e);
        }

    }

    /**
     * 告警模型字段缓存
     * @param tableName
     * @return
     */
    public List<AlertFieldVo> getModelField (String tableName) {
        List<AlertFieldVo> alertFieldList = alertModelCache.get(tableName);
        if (CollectionUtils.isEmpty(alertFieldList)) {
            alertFieldList = getModelFromRedis(tableName, null);
            alertModelCache.put(tableName, alertFieldList);
        }
        return alertFieldList;
    }

    @Scheduled(cron = "0 0 */1 * * ?")
    public void flushAlertModelCache () {
        alertModelCache.clear();
    }
}

package com.aspire.ums.cmdb.dict.service.imp;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aspire.ums.cmdb.Application;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.config.KafkaConfigConstant;
import com.aspire.ums.cmdb.dict.mapper.ConfigDictMapper;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.dict.payload.Dict;
import com.aspire.ums.cmdb.dict.payload.DictListReq;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.sync.payload.CmdbOptType;
import com.aspire.ums.cmdb.sync.service.producer.ConfigDictProducerServiceImpl;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v3.redis.service.IRedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司 FileName: CollectController Author: ws Date: 2019/4/1 Description: ${DESCRIPTION} History:
 * <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
@Slf4j
@Service
@Transactional
public class ConfigDictServiceImpl implements ConfigDictService {

    @Autowired
    private ConfigDictMapper configDictMapper;

    @Autowired
    private IRedisService redisService;

    @Autowired(required = false)
    private ConfigDictProducerServiceImpl configDictProducerService;

    @Override
    public Result<Dict> getAllConfigDictData(int pageNum, int startPageNum, int pageSize, String pcode, String dictCode,
            String dictNote, String colName) {
        DictListReq request = new DictListReq();
        request.setPageNum(pageNum);
        request.setPageSize(pageSize);
        request.setStartPageNum();
        request.setPcode(pcode == null ? "" : pcode.trim());
        request.setDictCode(dictCode == null ? "" : dictCode.trim());
        request.setDictNote(dictNote == null ? "" : dictNote.trim());
        request.setColName(colName == null ? "" : colName.trim());

        Result<Dict> response = new Result<Dict>();
        int dataCount = configDictMapper.getConfigDictDataCount(request);
        response.setCount(dataCount);
        if (dataCount > 0) {
            response.setData(configDictMapper.getConfigDictData(request));
        }
        log.info("response is {}", response);
        return response;
    }

    @Override
    public List<Dict> getDictAll(String dictId) {
        List<Dict> resultList = null;
        try {
            if (StringUtils.isEmpty(dictId)) {
                Object object = redisService.get(Constants.REDIS_DICT_PREFIX);
                if (object == null) {
                    resultList = configDictMapper.getDictAll(dictId);
                    redisService.set(Constants.REDIS_DICT_PREFIX, resultList);
                    return resultList;
                }
                return (new ObjectMapper().convertValue(object, new TypeReference<List<Dict>>() {}));
            }
            resultList = configDictMapper.getDictAll(dictId);
        } catch (Exception e) {
            log.error("[ERROR] >>> " + e.toString());
        }
        return resultList;
    }

    @Override
    public String insert(Dict dict) {
        log.info("request is {}", dict);
        try {
            int r = configDictMapper.getByDict(dict);
            if (r > 0) {
                return "数据已存在";
            }
            // 网管同步的id不为空,删除更新根据id操作.
            String id = dict.getDictId();
            if (org.apache.commons.lang3.StringUtils.isBlank(id)) {
                id = UUIDUtil.getUUID();
            }
            dict.setDictId(id);
            int rs = configDictMapper.insert(dict);
            configDictMapper.insertAddLog(String.valueOf(dict.getDictId()));

            if (dict.isSyncFlag()) {
                // 发送同步数据到kafka
                configDictProducerService.saveEventLogAndSendMsg(CmdbOptType.OPT_ADD, KafkaConfigConstant.TOPIC_CONFIG_DICT, null,
                        id, id, KafkaConfigConstant.TABLE_CONFIG_DICT);
            }
            if (rs > 0) {
                return "success";
            }
        } catch (Exception e) {
            log.error("[ERROR] >>> " + e.toString());
            return "error";
        }
        return null;
    }

    @Override
    public String update(Dict dict) {
        log.info("request is {}", dict);
        int rs = 0;
        try {
            Dict entity = configDictMapper.getDictById(dict.getDictId());
            if (entity == null) {
                return insert(dict);
            } else {
                rs = configDictMapper.updateByPrimaryKey(dict);
                configDictMapper.insertEditLog(String.valueOf(dict.getDictId()));
            }

            if (dict.isSyncFlag()) {
                // 发送同步数据到kafka
                configDictProducerService.saveEventLogAndSendMsg(CmdbOptType.OPT_MODIFY, KafkaConfigConstant.TOPIC_CONFIG_DICT,
                        null, dict.getDictId(), dict.getDictId(), KafkaConfigConstant.TABLE_CONFIG_DICT);
            }
            if (rs > 0) {
                return "success";
            }
        } catch (Exception e) {
            log.error("[ERROR] >>> " + e.toString());
            return "error";
        }
        return null;
    }

    @Override
    public Dict getDictById(String dictId) {
        Dict dict = null;
        try {
            dict = configDictMapper.getDictById(dictId);
        } catch (Exception e) {
            log.error("[ERROR] >>> " + e.toString());
        }
        return dict;
    }

    @Override
    public String getValueById(String dictId) {
        return configDictMapper.getValueById(dictId);
    }

    @Override
    public String getIdByNoteAndCol(String dictNote, String colName) {
        return configDictMapper.getIdByNoteAndCol(dictNote, colName);
    }

    @Override
    public List<Map<String, Object>> getDictExportData(String pcode, String dictCode, String dictNote, String colName) {
        if (StringUtils.isNotEmpty(pcode)) {
            pcode = pcode.trim();
        }
        if (StringUtils.isNotEmpty(dictCode)) {
            dictCode = dictCode.trim();
        }
        if (StringUtils.isNotEmpty(dictNote)) {
            dictNote = dictNote.trim();
        }
        if (StringUtils.isNotEmpty(colName)) {
            colName = colName.trim();
        }
        return configDictMapper.getDictExportData(pcode, dictCode, dictNote, colName);
    }

    @Override
    public String delete(String dictId) {
        log.info("request is {}", dictId);
        try {
            if (!dictId.startsWith("(") && !dictId.endsWith(")")) {
                dictId = "(\"" + dictId + "\")";
            }
            int rs = configDictMapper.deleteByPrimaryKey(dictId);
            configDictMapper.insertDeleteLog(dictId);

            // 发送同步数据到kafka
            configDictProducerService.saveEventLogAndSendMsg(CmdbOptType.OPT_DEL, KafkaConfigConstant.TOPIC_CONFIG_DICT, null,
                    dictId, dictId, KafkaConfigConstant.TABLE_CONFIG_DICT);

            if (rs > 0) {
                return "success";
            }
        } catch (Exception e) {
            log.error("[ERROR] >>> " + e.toString());
            return "error";
        }
        return null;
    }

    @Override
    public String delete(Dict dict) {
        String dictId = dict.getDictId();
        log.info("request is {}", dictId);
        try {
            if (!dictId.startsWith("(") && !dictId.endsWith(")")) {
                dictId = "(\"" + dictId + "\")";
            }
            int rs = configDictMapper.deleteByPrimaryKey(dictId);
            configDictMapper.insertDeleteLog(dictId);

            if (rs > 0) {
                return "success";
            }
        } catch (Exception e) {
            log.error("[ERROR] >>> " + e.toString());
            return "error";
        }
        return null;
    }

    @Override
    public List<ConfigDict> selectDicts() {
        List<ConfigDict> resultList = null;
        try {
            resultList = configDictMapper.selectDicts();
        } catch (Exception e) {
            log.error("[ERROR] >>> " + e.toString());
        }
        return resultList;
    }

    @Override
    public List<ConfigDict> selectDictsByType(String type, String pid, String pValue, String pType) {
        List<ConfigDict> resultList = null;
        if (StringUtils.isEmpty(type)) {
            return resultList;
        }
        try {
            switch (type.toLowerCase(Locale.ENGLISH)) {
                case "department1": // 一级部门
                    resultList = configDictMapper.getDepartment1();
                    break;
                case "department2": // 二级部门
                    resultList = configDictMapper.getDepartment2(pid);
                    break;
                case "bizsystem": // 应用系统
                    throw new RuntimeException("Sorry method is out of date. Please use /cmdb/module/module/data api instead.");
                case "idctype": // 资源池
                    resultList = configDictMapper.getIdcTypeList();
                    break;
                case "project_name": // 项目名称
                    resultList = configDictMapper.getProjectNameList(pid);
                    break;
                case "pod_name": // pod名称
                    resultList = configDictMapper.getPodNameList(pid);
                    break;
                case "roomid": // 机房位置
                    resultList = configDictMapper.getRoomList(pid);
                    break;
                case "mainten_factory": // 维保厂商
                    throw new RuntimeException("Sorry method is out of date. Please use /cmdb/module/module/data api instead.");
                case "device_mfrs": // 制造厂商
                    throw new RuntimeException("Sorry method is out of date. Please use /cmdb/module/module/data api instead.");
                default: // 默认
                    resultList = configDictMapper.selectDictsByType(type, pid, pValue, pType);
                    break;
            }
        } catch (Exception e) {
            log.error("[ERROR] >>> " + e.toString());
        }
        return resultList;
    }

    @Override
    public List<String> getDictType() {
        return configDictMapper.getDictType();
    }

    /**
     * 树方式获取字典
     * 
     * @param colName
     * @return
     */
    @Override
    public List<ConfigDict> getDictTree(String colName) {
        List<ConfigDict> resultList = configDictMapper.selectDictsByType(colName, null, null, null);
        if (resultList != null) {
            getTree(resultList);
        }
        return resultList == null ? Lists.newArrayList() : resultList;
    }

    @Override
    public List<Map> getDistinctDictType() {
        return configDictMapper.getDistinctDictType();
    }

    @Override
    public Map<String, String> getIdcTypeByName(String idcName) {
        return configDictMapper.getIdcTypeByName(idcName);
    }

    @Override
    public ConfigDict getDictByColNameAndDictCode(String colName, String dictCode) {
        return configDictMapper.getDictByColNameAndDictCode(colName, dictCode);
    }

    @Override
    public List<Dict> getDictByIds(List<String> dictIds) {
        return configDictMapper.getDictByIds(dictIds);
    }

    private void getTree(List<ConfigDict> reuqest) {
        for (ConfigDict req : reuqest) {
            List<ConfigDict> childListRoles = configDictMapper.selectDictsByUpDict(req.getId());
            if (childListRoles != null) {
                req.setSubList(childListRoles);
                getTree(childListRoles);
            }
        }
    }

}

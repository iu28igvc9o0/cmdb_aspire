package com.aspire.ums.cmdb.dict.service.imp;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.allocate.entity.Result;
import com.aspire.ums.cmdb.dict.entity.ConfigDict;
import com.aspire.ums.cmdb.dict.entity.Dict;
import com.aspire.ums.cmdb.dict.entity.DictListReq;
import com.aspire.ums.cmdb.dict.mapper.ConfigDictMapper;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.util.StringUtils;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CollectController
 * Author:   ws
 * Date:     2019/4/1
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
@Service
@Transactional
public class ConfigDictServiceImpl implements ConfigDictService {
    
    @Autowired
    private ConfigDictMapper configDictMapper;
    
    @Override
    public Result<Dict> getAllConfigDictData(int pageNum, int startPageNum, int pageSize, String pcode, String dictCode, String dictNote, String colName) {
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
        log.info("response is {}",response);
        return response;
    }
    
    @Override
    public List<Dict> getDictAll(String dictId) {
        List<Dict> resultList = null;
        try{
            resultList = configDictMapper.getDictAll(dictId);
        }catch (Exception e) {
            log.error("[ERROR] >>> ", e);
        }
        return resultList;
    }
    
    @Override
    public String insert(Dict dict) {
        log.info("request is {}",dict);
        try{
            int rs = configDictMapper.insert(dict);
            configDictMapper.insertAddLog(String.valueOf(dict.getDictId()));
            if (rs > 0) {
                return "success";
            }
        }catch (Exception e){
            log.error("[ERROR] >>> ", e);
            return "error";
        }
        return null;
    }
    
    @Override
    public String update(Dict dict) {
        log.info("request is {}",dict);
        try{
            int rs = configDictMapper.updateByPrimaryKey(dict);
            configDictMapper.insertEditLog(String.valueOf(dict.getDictId()));
            if (rs > 0) {
                return "success";
            }
        }catch (Exception e){
            log.error("[ERROR] >>> ", e);
            return "error";
        }
        return null;
    }
    
    @Override
    public Dict getDictById(String dictId) {
        Dict dict = null;
        try{
            dict = configDictMapper.getDictById(dictId);
        }catch (Exception e) {
            log.error("[ERROR] >>> ", e);
        }
        return dict;
    }
    
    @Override
    public List<Map<String, Object>> getDictExportData(String pcode,String dictCode,String dictNote,String colName) {
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
        return configDictMapper.getDictExportData(pcode,dictCode,dictNote,colName);
    }
    
    @Override
    public String delete(String dictId) {
        log.info("request is {}",dictId);
        try{
            if (!dictId.startsWith("(") && !dictId.endsWith(")")) {
                dictId = "("  + dictId + ")";
            }
            int rs = configDictMapper.deleteByPrimaryKey(dictId);
            configDictMapper.insertDeleteLog(dictId);
            if (rs > 0) {
                return "success";
            }
        }catch (Exception e){
            log.error("[ERROR] >>> ", e);
            return "error";
        }
        return null;
    }
    
    @Override
    public List<ConfigDict> selectDicts() {
        List<ConfigDict> resultList = null;
        try{
            resultList = configDictMapper.selectDicts();
        }catch (Exception e) {
            log.error("[ERROR] >>> ", e);
        }
        return resultList;
    }
    
    @Override
    public List<ConfigDict> selectDictsByType(String colName,String pid,String pValue,String pType) {
        List<ConfigDict> resultList = null;
        if (StringUtils.isEmpty(colName)) {
            return resultList;
        }
        try{
            if (StringUtils.isEmpty(pValue)) {
                resultList = configDictMapper.selectDictsByType(colName,pid);
            } else {
                resultList = configDictMapper.selectDictDataByValue(pValue,pType,colName);
            }
        }catch (Exception e) {
            log.error("[ERROR] >>> ", e);
        }
        return resultList;
    }


    
    @Override
    public List<String> getDictType() {
        return configDictMapper.getDictType();
    }

    /**
     * 树方式获取字典
     * @param colName
     * @return
     */
    @Override
    public List<ConfigDict> getDictTree(String colName, String ids) {
        String [] idArray = null;
        if (!StringUtils.isEmpty(ids)) {
            idArray = ids.split(",");
        }
        List<ConfigDict> resultList =configDictMapper.selectDictsByTypeAndIds(colName, idArray);
        if (resultList != null) {
            getTree(resultList, idArray);
        }
        return resultList == null ? Lists.newArrayList() : resultList;
    }

    @Override
    public List<Dict> getDictByIds(List<String> dictIds) {
        return configDictMapper.getDictByIds(dictIds);
    }

    private void getTree(List<ConfigDict> reuqest, String [] ids){
        for(ConfigDict req:reuqest) {
            List<ConfigDict> childListRoles = configDictMapper.selectDictsByUpDictAndIds(req.getId(), ids);
            if (childListRoles != null) {
                req.setSubList(childListRoles);
                getTree(childListRoles, ids);
            }
        }
    }
}

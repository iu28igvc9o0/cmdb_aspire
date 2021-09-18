package com.aspire.ums.cmdb.v3.module.service.impl;

import com.alibaba.fastjson.JSON;
import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.dict.payload.Dict;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.helper.JDBCHelper;
import com.aspire.ums.cmdb.schema.service.SchemaService;
import com.aspire.ums.cmdb.sqlManage.CmdbSqlManage;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.module.mapper.CmdbV3ModuleCiRelationMapper;
import com.aspire.ums.cmdb.v3.module.payload.*;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCiCodeRelationService;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCiRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbV3ModuleCiRelationServiceImpl
 * Author:   hangfang
 * Date:     2020/4/26
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
@Slf4j
public class CmdbV3ModuleCiRelationServiceImpl implements ICmdbV3ModuleCiRelationService {

    @Autowired
    private CmdbV3ModuleCiRelationMapper ciRelationMapper;

    @Autowired
    private ICmdbV3ModuleCiCodeRelationService ciCodeRelationService;

    @Autowired
    private ConfigDictService configDictService;

    @Autowired
    private ICmdbInstanceService instanceService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ICmdbCodeService codeService;

    @Autowired
    private SchemaService schemaService;

    @Autowired
    private JDBCHelper jdbcHelper;


    @Override
    public List<CmdbV3ModuleCiRelationDetail> listRDetailByModuleId(String moduleId) {
        return ciRelationMapper.listRDetailByModuleId(moduleId);
    }

    @Override
    public List<CmdbV3ModuleCiRelation> listByModuleId(String moduleId) {
        return ciRelationMapper.listByModuleId(moduleId);
    }


    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void save(String userName, CmdbV3ModuleCiRelation relation) {
        validRelation(relation);
        if (relation.getCodeRelationList().size() == 0) {
            return;
        }
        CmdbV3ModuleCiRelation existRelation = ciRelationMapper.getExist(relation.getModuleId(),relation.getCodeRelationList().get(0).getCurrCodeId());
        // 存储字段关联关系
        if (existRelation != null) {
            throw new RuntimeException("当前字段已存在关系");
        }
        relation.setId(UUIDUtil.getUUID());
        relation.setCreatePerson(userName);
        relation.setCreateTime(new Date());
        // 创建和更新人在封装层给出
        relation.setUpdatePerson(userName);
        relation.setUpdateTime(new Date());
        deleteAndSaveRelationInfo(relation);
    }

    private void deleteAndSaveRelationInfo(CmdbV3ModuleCiRelation relation) {
        // 删除原本的字段关联关系
        ciCodeRelationService.deleteByRelationId(relation.getId());
        int sortIndex = 1;
        for (CmdbV3ModuleCiCodeRelation codeRelation : relation.getCodeRelationList()) {
            validCodeRelation(codeRelation);
            codeRelation.setId(UUIDUtil.getUUID());
            codeRelation.setSortIndex(sortIndex++);
            codeRelation.setRelationId(relation.getId());
        }
        ciCodeRelationService.save(relation.getCodeRelationList());
        // 存储关系
        ciRelationMapper.save(relation);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void update(String userName, CmdbV3ModuleCiRelation relation) {
        if (StringUtils.isEmpty(relation.getId())) {
            throw new RuntimeException("未传参[关联关系id]");
        }
        validRelation(relation);
        CmdbV3ModuleCiRelation existRelation = ciRelationMapper.getExist(relation.getModuleId(),relation.getCodeRelationList().get(0).getCurrCodeId());
        // 存储字段关联关系
        if (existRelation != null && !existRelation.getId().equals(relation.getId())) {
            throw new RuntimeException("当前字段已存在关系");
        }
        // 创建和更新人在封装层给出
        relation.setUpdatePerson(userName);
        relation.setUpdateTime(new Date());
        // 删除原本的字段关联关系
        ciCodeRelationService.deleteByRelationId(relation.getId());
        deleteAndSaveRelationInfo(relation);
    }

    private void validCodeRelation(CmdbV3ModuleCiCodeRelation codeRelation) {
        if (StringUtils.isEmpty(codeRelation.getCurrCodeId())) {
            throw new RuntimeException("未传参数[当前模型字段]");
        }
    }

    private void validRelation(CmdbV3ModuleCiRelation relation) {
        if (StringUtils.isEmpty(relation.getModuleId())) {
            throw new RuntimeException("未传参数[当前模型]");
        }
        if (StringUtils.isEmpty(relation.getRelation())) {
            throw new RuntimeException("未传参数[关联关系]");
        }
        if (StringUtils.isEmpty(relation.getCreatePerson())) {
            throw new RuntimeException("未传参数[当前用户]");
        }

        if (StringUtils.isEmpty(relation.getResourceName())) {
            throw new RuntimeException("未传参数[关联资源名称]");
        }
        if (StringUtils.isEmpty(relation.getRelationType())) {
            throw new RuntimeException("未传参数[关联类型]");
        }
        Dict relationType= configDictService.getDictById(relation.getRelationType());
        if (relationType == null) {
            throw new RuntimeException("当关系类型id[" + relation.getRelationType() + "]不存在");
        }
        if ("模型".equals(relationType.getDictNote()) && StringUtils.isEmpty(relation.getRelationModuleId())) {
            throw new RuntimeException("当关系类型为[模型]时未传参数[关联模型]");
        }
        if ("数据表".equals(relationType.getDictNote()) && StringUtils.isEmpty(relation.getRelationSql())) {
            throw new RuntimeException("当关系类型为[数据表]时未传参数[关联查询sql]");
        }
        if ("数据表".equals(relationType.getDictNote())) {
            validSql(relation);
        }
    }

    private void validSql(CmdbV3ModuleCiRelation relation) {
        // 做一次防注入过滤
        String temSql = relation.getRelationSql().toLowerCase();
        String badKeyWords = "'|exec|insert|delete|update|*|%|chr|mid|master|truncate|char|declare|;|-|+";
        String[] badKeys = badKeyWords.split("\\|");
        String[] sqlWords = temSql.split(" ");
        for (String badKey : badKeys) {
            for (String sqlWord : sqlWords) {
                if (badKey.equals(sqlWord)) {
                    throw new RuntimeException("table relation SQL is bad. SQL -> " + temSql);
                }
            }
        }
        // 检查是否还含关联字段
        List<CmdbV3ModuleCiCodeRelation> codeRelations = relation.getCodeRelationList();
        if (null == codeRelations || codeRelations.size() == 0) {
            throw new RuntimeException("未添加字段关联关系");
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void deleteById(String id) {
        // 删除字段关联关系
        ciCodeRelationService.deleteByRelationId(id);
        // 删除关联关系
        ciRelationMapper.delete(id);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void deleteByModuleId(String moduleId) {
        // 删除字段关联关系
        ciCodeRelationService.deleteByModuleId(moduleId);
        // 删除关联关系
        ciRelationMapper.deleteByModuleId(moduleId);
    }

    @Override
    public CmdbV3ModuleCiRelationDetailResponse getCiRelationDetail(String relationId, String instanceId) {
        long now = new Date().getTime();
        CmdbV3ModuleCiRelationDetail relationDetail = ciRelationMapper.getDetailById(relationId);
        if (relationDetail == null) {
            throw new RuntimeException("当前关联关系不存在,关联关系id：" + relationId);
        }
        log.info("-------[ELATION DETAIL] 查询关联关系详情耗时 {} ms", new Date().getTime() - now);
        now  = new Date().getTime();
        Map<String, Object> instanceDetail  = instanceService.getInstanceDetail(relationDetail.getCurModule().getId(), instanceId);

        log.info("-------[ELATION DETAIL] 查询CI详情耗时 {} ms", new Date().getTime() - now);
        now  = new Date().getTime();
        CmdbV3ModuleCiRelationDetailResponse response = new CmdbV3ModuleCiRelationDetailResponse();
        response.setId(relationId);
        response.setRelation(relationDetail.getRelationDict().getDictNote());
        response.setResourceName(relationDetail.getResourceName());
        response.setRelationType(relationDetail.getRelationTypeDict().getDictNote());
        List<CmdbV3ModuleCiCodeRelationDetail> codeRelationList = relationDetail.getCodeRelationList();
        List<Map<String, Object>> instanceList = new LinkedList<>();
        List<Object> headers = new ArrayList<>();
        if ("模型".equals(relationDetail.getRelationTypeDict().getDictNote())) {
            // 设置关联模型名称
            Map<String, Object> params = new HashMap<>();
            params.put("module_id", relationDetail.getRelationModule().getId());
            StringBuilder whereSql = new StringBuilder();
            for (CmdbV3ModuleCiCodeRelationDetail codeRelation : codeRelationList) {
                Object curValue = instanceDetail.get(codeRelation.getCurrCode().getFiledCode());
                params.put(codeRelation.getRelationCode().getFiledCode(), curValue);
                whereSql.append(" and ");
                whereSql.append(codeRelation.getRelationCode().getFiledCode()).append("=")
                        .append("#{").append(codeRelation.getRelationCode().getFiledCode()).append("}");

            }
            String sql = moduleService.getModuleQuerySQL(relationDetail.getRelationModule().getId());
            CmdbSqlManage cmdbSqlManage = new CmdbSqlManage(sql, relationDetail.getRelationModule().getId(), Constants.NEED_AUTH);
            instanceList = jdbcHelper.getQueryList(cmdbSqlManage, whereSql.toString(), null, null, params);
            log.info("-------[ELATION DETAIL] 查询关联条件下数据耗时 {} ms", new Date().getTime() - now);
            now  = new Date().getTime();
            List<CmdbSimpleCode> headerCodes = codeService.getSimpleCodeListByModuleId(relationDetail.getRelationModule().getId());
            log.info("-------[ELATION DETAIL] 查询表头数据耗时 {} ms", new Date().getTime() - now);
          if (headerCodes.size() > 0 ) {
             headers = JSON.parseArray(JSON.toJSONString(headerCodes), Object.class);
          }
          response.setRelationCiList(instanceList);
          response.setHeaderList(headers);
        } else if ("数据表".equals(relationDetail.getRelationTypeDict().getDictNote())) {
            String executeSql = relationDetail.getRelationSql();
            for (CmdbV3ModuleCiCodeRelationDetail codeRelation : codeRelationList) {
                CmdbSimpleCode currCode = codeRelation.getCurrCode();
                String replaceTemp = "["+ currCode.getFiledCode() +"]";
                String replaceValue = StringUtils.isNotEmpty(instanceDetail.get(currCode.getFiledCode()))  ?  instanceDetail.get(currCode.getFiledCode()).toString() : "";
                executeSql = executeSql.replace(replaceTemp, "'" + replaceValue + "'");
            }
            CmdbSqlManage cmdbSqlManage = new CmdbSqlManage(executeSql, relationDetail.getRelationModule().getId(),null, Constants.NEED_AUTH);
            instanceList = jdbcHelper.getQueryList(cmdbSqlManage, null, null, null, null);
            log.info("-------[ELATION DETAIL] 执行sql查询数据耗时 {} ms", new Date().getTime() - now);
            if (instanceList != null && instanceList.size() > 0) {
                headers = new ArrayList<>(instanceList.get(0).keySet());
            }
            response.setRelationCiList(instanceList);
            response.setHeaderList(headers);
        }
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return response;
    }
}

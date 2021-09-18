package com.aspire.ums.cmdb.view.service.impl;

import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.helper.JDBCHelper;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.sqlManage.CmdbSqlManage;
import com.aspire.ums.cmdb.util.CheckSqlUtil;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.redis.client.IRedisClient;
import com.aspire.ums.cmdb.v3.redis.service.IRedisService;
import com.aspire.ums.cmdb.view.mapper.CmdbViewMapper;
import com.aspire.ums.cmdb.view.payload.*;
import com.aspire.ums.cmdb.view.service.ICmdbViewNodeService;
import com.aspire.ums.cmdb.view.service.ICmdbViewService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbViewServiceImpl
 * Author:   hangfang
 * Date:     2020/5/19
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
@Slf4j
public class CmdbViewServiceImpl implements ICmdbViewService {

    @Autowired
    private CmdbViewMapper cmdbViewMapper;
    @Autowired
    private ICmdbViewNodeService nodeService;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private JDBCHelper jdbcHelper;
    @Autowired
    private IRedisService redisService;


    @Override
    public List<CmdbView> listByQuery(CmdbViewQuery query) {
        return cmdbViewMapper.list(query);
    }

    @Override
    public Integer listCount(CmdbViewQuery query) {
        return cmdbViewMapper.count(query);
    }

    @Override
    public CmdbViewData getDataStructure(String id) {
        CmdbViewData viewData = new CmdbViewData();
        CmdbView view = cmdbViewMapper.getOne(id);
        String moduleId = view.getModuleId();
        //顶级节点
        List<CmdbViewNode> nodeList = view.getViewNodeList();
        List<CmdbViewNodeData> treeDataList = new ArrayList<>();
        treeDataList = getSubNodeList("all",null, null, moduleId, nodeList);
        System.out.println(treeDataList);
        viewData.setNodeList(treeDataList);
        viewData.setViewCode(view.getViewCode());
        viewData.setId(view.getId());
        viewData.setViewName(view.getViewName());
        return viewData;
    }

    @Override
    public CmdbViewData getNextNodeData(CmdbViewData viewData) {
        validViewData(viewData);
        CmdbViewData resultViewData = new CmdbViewData();
        CmdbView view = new CmdbView();
        LinkedHashMap<String, String> queryInfo = new LinkedHashMap<>();
        LinkedHashMap<String, String> queryToData = new LinkedHashMap<>();
        List<CmdbViewNode> nodeList = new ArrayList<>();
        //如果是初始状态
        if (viewData.getNodeList() == null || viewData.getNodeList().size() == 0) {
            Object object = redisService.get(String.format(Constants.REDIS_VIEW_TREE, viewData.getViewCode()));
            if ( object != null) {
                return (new ObjectMapper().convertValue(object, new TypeReference<CmdbViewData>(){}));
            }
            view = cmdbViewMapper.getOneByCode(viewData.getViewCode());
            if (view == null) {
              return null;
            }
            nodeList = view.getViewNodeList();
        } else {
            //如果是点击状态
            view = cmdbViewMapper.getSimpleOneByCode(viewData.getViewCode());
            if (view == null) {
                return null;
            }
            // 获取被点击的节点详情
            List<CmdbViewNodeData> clickList = viewData.getNodeList();
            if (clickList.size() > 1) {
                throw new RuntimeException("点击状态下所传节点参数不能大于1");
            }
            CmdbViewNode queryNode = new CmdbViewNode();
            queryNode.setId(clickList.get(0).getNodeId());
            CmdbViewNode node = nodeService.getOne(queryNode);
            nodeList.addAll(node.getSubNodeList());
            queryInfo = clickList.get(0).getQueryInfo();
            queryToData = clickList.get(0).getQueryToData();
        }
//         //如果是静态节点查2级
//        if (node.getEnableQuery() == 0) {
//            queryLevel = 2;
//        } else {
//            queryLevel = 1;
//        }
        List<CmdbViewNodeData> treeDataList = getSubNodeList(null, queryInfo, queryToData, view.getModuleId(), nodeList);
        resultViewData.setViewName(view.getViewName());
        resultViewData.setId(view.getId());
        resultViewData.setViewCode(view.getViewCode());
        resultViewData.setModuleId(view.getModuleId());
        resultViewData.setNodeList(treeDataList);
        return resultViewData;
    }

    private void validViewData(CmdbViewData viewData) {
        if (StringUtils.isEmpty(viewData.getViewCode())) {
            throw new RuntimeException("未传参数[viewCode]");
        }
//        if (StringUtils.isEmpty(viewData.get())) {
//            throw new RuntimeException("未传参数[nodeId]");
//        }
    }

    private List<CmdbViewNodeData> getSubNodeList(String type, LinkedHashMap<String, String> queryMap, LinkedHashMap<String, String> queryToDataMap, String moduleId, List<CmdbViewNode> nodeList) {
        long startTime = new Date().getTime();
        List<CmdbViewNodeData> subNodeList = new ArrayList<>();
        for (CmdbViewNode node :  nodeList) {
            log.info("------开始查询 {}-------",node.getToQueryFiled());
            List<Map<String, Object>> resultList = new ArrayList<>();
            String filedCode = node.getToQueryFiled();
            // 纯文本节点
            if (node.getEnableQuery() == 0) {
                CmdbViewNodeData treeData = new CmdbViewNodeData();
                treeData.setNodeId(node.getId());
                treeData.setIcon(node.getIcon());
                treeData.setNodeName(node.getNodeName());
                if ((node.getSubNodeList() != null && node.getSubNodeList().size() > 0) || type != null) {
                    treeData.setSubNodeList(getSubNodeList(type, queryMap, queryToDataMap, moduleId, node.getSubNodeList()));
                }
                subNodeList.add(treeData);
            } else {
                // 非纯文本节点
                if (node.getEnableQuery() == 1 && node.getEnableUseCode() == 0) {
                    String sql = node.getConfigSql();
                    resultList = toHandleSql(sql, moduleId, filedCode, queryToDataMap);
                } else {
                    resultList = toHandleSql(null, moduleId, filedCode, queryToDataMap);
                }
                for (Map<String, Object> res : resultList) {
                    String nodeFiledName = res.get(filedCode).toString();
                    String nodeFiledId = null;
                    if (res.containsKey("id")) {
                        nodeFiledId = res.get("id").toString();
                    }
                    CmdbViewNodeData treeData = new CmdbViewNodeData();
                    treeData.setNodeId(node.getId());
                    treeData.setIcon(node.getIcon());
                    treeData.setNodeName(nodeFiledName);
                    treeData.setMetaData(res);
                    LinkedHashMap<String, String> queryInfo = new LinkedHashMap<>();
                    queryInfo.putAll(queryMap == null ? new LinkedHashMap<>() : queryMap);
                    queryInfo.put(node.getToQueryFiled(), nodeFiledName);
                    treeData.setQueryInfo(queryInfo);
                    LinkedHashMap<String, String> queryToData= new LinkedHashMap<>();
                    queryToData.putAll(queryMap == null ? new LinkedHashMap<>() : queryToDataMap);
                    queryToData.put(node.getToQueryFiled(), nodeFiledId == null ? nodeFiledName : nodeFiledId);
                    treeData.setQueryToData(queryToData);
                    if (node.getEnableShowCount() == 1) {
                        StringBuilder showInfo = new StringBuilder();
                        if (node.getNodeShowList() == null || node.getNodeShowList().size() == 0) {
                            Integer count = 0;
                            count = toHandleCountSql(null, moduleId,queryToData);
                            showInfo.append(count);
                        } else {
                            for (CmdbViewNodeShow show : node.getNodeShowList()) {
                                Integer count = 0;
                                count = toHandleCountSql(show.getShowSql(), moduleId,queryToData);
                                if (StringUtils.isNotEmpty(show.getShowPrefix())) {
                                    showInfo.append(show.getShowPrefix());
                                }
                                showInfo.append(count);
                                if (StringUtils.isNotEmpty(node.getShowSep())) {
                                    showInfo.append(node.getShowSep());
                                }
                            }
                            if (StringUtils.isNotEmpty(node.getShowSep())) {
                                showInfo.deleteCharAt(showInfo.length() -1);
                            }
                        }
                        treeData.setShowInfo(showInfo.toString());
                    }
                    if ((node.getEnableQuery() == 0 && node.getSubNodeList().size() > 0) || type != null) {
                        treeData.setSubNodeList(getSubNodeList(type, queryInfo, queryToData, moduleId, node.getSubNodeList()));
                    }
                    subNodeList.add(treeData);
                }
            }

        }
        log.info("---------获取数据结束，耗时{}", new Date().getTime() - startTime);
        return subNodeList;
    }

    private List<Map<String, Object>> toHandleSql(String configSql, String moduleId, String filedCode, Map<String, String> queryMap) {
        String baseSql = "";
        StringBuilder executeSql = new StringBuilder();
        if (StringUtils.isEmpty(configSql)) {
            baseSql = moduleService.getModuleQuerySQL(moduleId);
            executeSql.append("select distinct id,").append(filedCode).append(" from (").append(baseSql).append(")res where 1=1 ");
        } else {
            if (!configSql.contains(filedCode)) {
                throw new RuntimeException("sql有误，sql配置中查询项必须有[对应实例属性]");
            }
            CheckSqlUtil.checkSql(configSql);
            executeSql.append("select * from (").append(configSql).append(") res where 1=1 ");
        }
        List<Map<String, Object>> resultList = new ArrayList<>();
        executeSql.append(" and IFNULL(").append(filedCode).append(", '') != '' ");
        Map<String, Object> params = new HashMap<>();
        if (queryMap != null) {
            for (String key : queryMap.keySet()) {
                params.put(key, queryMap.get(key));
                executeSql.append("and ").append(key).append(" = #{").append(key).append("}");
            }
        }
//        List<Map<String, Object>> executeResList;
        try {
            CmdbSqlManage cmdbSqlManage = new CmdbSqlManage(executeSql.toString(), moduleId,null, Constants.NEED_AUTH);
            resultList = jdbcHelper.getQueryList(cmdbSqlManage, null, null, null, params);
//            for (Map<String, Object> res : executeResList) {
//                resultList.add(res.get(filedCode).toString());
//            }
        } catch (Exception e) {
            throw new RuntimeException("sql有误，请检查sql : " +  executeSql + "! error : " + e.getMessage());
        }
        return resultList;
    }

    private Integer toHandleCountSql(String showSql, String moduleId, Map<String, String> queryMap) {
        String baseSql;
        StringBuilder executeSql = new StringBuilder();
        if (StringUtils.isEmpty(showSql)) {
            baseSql = moduleService.getModuleQuerySQL(moduleId);
            executeSql.append("select id from (").append(baseSql).append(") res where 1=1 ");
        } else {
            CheckSqlUtil.checkSql(showSql);
            executeSql.append("select  * from (").append(showSql).append(") res where 1=1 ");
        }
        Map<String, Object> params = new HashMap<>();
        if (queryMap != null) {
            for (String key : queryMap.keySet()) {
                params.put(key, queryMap.get(key));
                executeSql.append("and ").append(key).append(" = #{").append(key).append("}");
            }
        }
        try {
            CmdbSqlManage cmdbSqlManage = new CmdbSqlManage(executeSql.toString(), moduleId, null, Constants.NEED_AUTH);
            return jdbcHelper.getInt(cmdbSqlManage, null, params);
        } catch (Exception e) {
            throw new RuntimeException("sql有误，请检查sql : " +  executeSql + "! error : " + e.getMessage());
        }
    }


    @Override
    public CmdbView getStructure(String id) {
        return cmdbViewMapper.getOne(id);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, SQLException.class})
    public String save(String userName, CmdbView view) {
        validView(view);
        if (StringUtils.isEmpty(view.getId())) {
            view.setId(UUIDUtil.getUUID());
            view.setCreateTime(new Date());
            view.setCreatePerson(userName);
        }
        view.setUpdatePerson(userName);
        view.setUpdateTime(new Date());
        // 添加节点
        List<CmdbViewNode> nodeList = view.getViewNodeList();
        for (CmdbViewNode node : nodeList) {
            node.setViewId(view.getId());
            nodeService.save(node);
        }
        cmdbViewMapper.save(view);
        return view.getId();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, SQLException.class})
    public void delete(String id) {
        cmdbViewMapper.deleteById(id);
        nodeService.deleteByViewId(id);
    }

    /**
     * 根据viewCode所有节点数据
     *
     * @param viewData
     */
    @Override
    public CmdbViewData getAllNodeData(CmdbViewData viewData) {
        CmdbViewData cmdbViewData = this.getNextNodeData(viewData);
        if (cmdbViewData != null && cmdbViewData.getNodeList() != null) {

        }
        return cmdbViewData;
    }


    private void validCheckData(Map<String, Object> checkData) {
        if (!checkData.containsKey("type") || StringUtils.isEmpty(checkData.get("type").toString())) {
            throw new RuntimeException("未传参数sql类型[type]");
        }
        if (!checkData.containsKey("sql")) {
            throw new RuntimeException("未传sql数据");
        }
        if (StringUtils.isEmpty(checkData.get("sql").toString())) {
            throw new RuntimeException("sql数据为空");
        }
        String type = checkData.get("type").toString();
        if (type.equals("node") && checkData.containsKey("queryInfo")) {

        }
    }


    private void validView(CmdbView view) {
        if (StringUtils.isEmpty(view.getViewCode())) {
            throw new RuntimeException("视图编码不能为空");
        }
        if (StringUtils.isEmpty(view.getViewName())) {
            throw new RuntimeException("视图名称不能为空");
        }
        CmdbView queryView = cmdbViewMapper.getSimpleOneByCode(view.getViewCode());
        // 如是新增
        if (queryView != null && StringUtils.isEmpty(view.getId())) {
            throw new RuntimeException("视图编码[" + view.getViewCode() + "]已存在");
        }
        // 如是更新
        if (queryView != null && StringUtils.isNotEmpty(view.getId())) {
            if (!queryView.getId().equals(view.getId())) {
                throw new RuntimeException("视图编码[" + view.getViewCode() + "]已存在");
            }
        }
        if (StringUtils.isEmpty(view.getCatalogId())) {
            throw new RuntimeException("模型分组不能为空");
        }
        if (StringUtils.isEmpty(view.getModuleId())) {
            throw new RuntimeException("模型不能为空");
        }
    }
}

package com.aspire.ums.cmdb.view.service.impl;

import com.aspire.ums.cmdb.schema.service.SchemaService;
import com.aspire.ums.cmdb.util.CheckSqlUtil;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.view.mapper.CmdbViewNodeMapper;
import com.aspire.ums.cmdb.view.payload.CmdbViewNode;
import com.aspire.ums.cmdb.view.payload.CmdbViewNodeShow;
import com.aspire.ums.cmdb.view.service.ICmdbViewNodeService;
import com.aspire.ums.cmdb.view.service.ICmdbViewNodeShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbViewNodeServiceImpl
 * Author:   hangfang
 * Date:     2020/5/19
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
public class CmdbViewNodeServiceImpl implements ICmdbViewNodeService {

    @Autowired
    private CmdbViewNodeMapper nodeMapper;
    @Autowired
    private ICmdbViewNodeShowService nodeShowService;
    @Autowired
    private SchemaService schemaService;

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, SQLException.class})
    public void save(CmdbViewNode viewNode) {
        validNode(viewNode);
        if (StringUtils.isEmpty(viewNode.getId())) {
            viewNode.setId(UUIDUtil.getUUID());
        }
        // 删除节点下展示信息
        nodeShowService.deleteByNodeId(viewNode.getId());
        // 如果需要展示数量信息
        if (viewNode.getEnableQuery() == 1 && viewNode.getEnableShowCount() == 1) {
            // 新增展示信息
            List<CmdbViewNodeShow> nodeShowList = viewNode.getNodeShowList();
            List<CmdbViewNodeShow> addShowList = new ArrayList<>();
            for (CmdbViewNodeShow nodeShow : nodeShowList) {
                //展示都为空的
                if (StringUtils.isEmpty(nodeShow.getShowPrefix()) && StringUtils.isEmpty(nodeShow.getShowSql())) {
                    continue;
                }
                if (StringUtils.isNotEmpty(nodeShow.getShowSql())) {
                    CheckSqlUtil.checkSql(nodeShow.getShowSql());
                }
                nodeShow.setNodeId(viewNode.getId());
                addShowList.add(nodeShow);
            }
            if (addShowList.size() > 0) {
                nodeShowService.saveByBatch(addShowList);
            }
        }
        nodeMapper.save(viewNode);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, SQLException.class})
    public void deleteByViewId(String viewId) {
        nodeMapper.deleteByViewId(viewId);
        nodeShowService.deleteByViewId(viewId);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, SQLException.class})
    public void deleteById(String id) {
        nodeMapper.deleteById(id);
        nodeShowService.deleteByNodeId(id);
    }

    @Override
    public CmdbViewNode getOne(CmdbViewNode node) {
        return nodeMapper.getOne(node);
    }

    private void validNode(CmdbViewNode node) {
        if (StringUtils.isEmpty(node.getNodeName())){
            throw new RuntimeException("节点名称不能为空");
        }
        CmdbViewNode query = new CmdbViewNode();
        query.setNodeName(node.getNodeName());
        query.setParentNodeId(node.getParentNodeId());
        query.setViewId(node.getViewId());
        query = nodeMapper.getOne(query);
        if (StringUtils.isEmpty(node.getId())) {
            if (query != null) {
                throw new RuntimeException("当前父级下有相同节点名称");
            }
        } else {
            if (query != null && !query.getId().equals(node.getId())) {
                throw new RuntimeException("当前父级下有相同节点名称");
            }
        }
        if (node.getEnableQuery() != 1) {
            return;
        }

        if (node.getEnableUseCode() == 1 && StringUtils.isEmpty(node.getUseCodeId())){
            throw new RuntimeException("当使用配置项时[配置项名称]不能为空");
        }
        if (node.getEnableUseCode() == 0 && StringUtils.isEmpty(node.getConfigSql())){
            throw new RuntimeException("当不使用配置项时[SQL配]不能为空");
        }
        if (node.getEnableUseCode() == 0 && StringUtils.isNotEmpty(node.getConfigSql())){
            String sql = node.getConfigSql();
            if (!sql.contains(node.getToQueryFiled())) {
                throw new RuntimeException("sql有误，sql配置中查询项必须有[对应实例属性]");
            }
            CheckSqlUtil.checkSql(sql);
            try {
                schemaService.executeSql(sql);
            } catch (Exception e) {
                throw new RuntimeException("sql有误，请检查sql : " +  sql + "! error : " + e.getMessage());
            }
        }
    }
}

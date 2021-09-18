package com.aspire.ums.cmdb.view.service;

import com.aspire.ums.cmdb.view.payload.CmdbView;
import com.aspire.ums.cmdb.view.payload.CmdbViewData;
import com.aspire.ums.cmdb.view.payload.CmdbViewNodeData;
import com.aspire.ums.cmdb.view.payload.CmdbViewQuery;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICmdbViewService
 * Author:   hangfang
 * Date:     2020/5/19
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface ICmdbViewService {

    /**
     * 根据模型分组获取视图列表
     */
    List<CmdbView>  listByQuery(CmdbViewQuery query);
    /**
     * 根据模型分组获取视图列表
     */
    Integer listCount(CmdbViewQuery query);


    /**
     * 根据id获取数据结构
     */
    CmdbViewData getDataStructure(String id);

    /**
     * 根据id获取数据结构
     */
    CmdbViewData getNextNodeData(CmdbViewData viewData);

    /**
     * 根据id获取视图结构
     */
    CmdbView getStructure(String id);
    /**
     * 新增自定义视图
     */
    String save(String userName, CmdbView cmdbView);
    /**
     * 新增自定义视图
     */
    void delete(String id);

    /**
     * 根据viewCode所有节点数据
     */
    CmdbViewData getAllNodeData(CmdbViewData viewData);
}

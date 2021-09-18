package com.aspire.ums.cmdb.view.web;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.view.ICmdbViewAPI;
import com.aspire.ums.cmdb.view.payload.CmdbView;
import com.aspire.ums.cmdb.view.payload.CmdbViewData;
import com.aspire.ums.cmdb.view.payload.CmdbViewQuery;
import com.aspire.ums.cmdb.view.service.ICmdbViewNodeService;
import com.aspire.ums.cmdb.view.service.ICmdbViewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbViewController
 * Author:   hangfang
 * Date:     2020/5/19
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbViewController implements ICmdbViewAPI {

    @Autowired
    private ICmdbViewService viewService;

    @Autowired
    private ICmdbViewNodeService nodeService;
    /**
     * 根据模型分组获取视图列表
     */
    @Override
    public Result<CmdbView> listByQuery(@RequestBody CmdbViewQuery query) {
        Result<CmdbView> result = new Result<>();
        List<CmdbView> resultList = viewService.listByQuery(query);
        Integer totalSize = viewService.listCount(query);
        result.setTotalSize(totalSize);
        result.setData(resultList);
        return result;
    }

    @Override
    public CmdbViewData getDataStructure(@RequestParam("id") String id) {
        return viewService.getDataStructure(id);
    }

    @Override
    public CmdbViewData getNextNodeData(@RequestBody CmdbViewData viewData) {
        return viewService.getNextNodeData(viewData);
    }

    @Override
    public CmdbViewData getAllNodeData(@RequestBody CmdbViewData viewData) {
        return viewService.getAllNodeData(viewData);
    }

    @Override
    public CmdbView getStructure(@RequestParam("id") String id) {
        return viewService.getStructure(id);
    }

    /**
     * 新增自定义视图
     */
    @Override
    public Map<String, Object> save(@RequestParam("userName")String userName, @RequestBody CmdbView cmdbView) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            String message;
            if (StringUtils.isEmpty(cmdbView.getId())) {
               message = "新增视图成功！";
            } else {
                message = "更新视图成功！";
            }
            String viewId = viewService.save(userName, cmdbView);
            resultMap.put("success", true);
            resultMap.put("message", message);
            resultMap.put("id", viewId);
        } catch (Exception e) {
            log.error("新增视图失败！error: {}",  e);
            resultMap.put("success", false);
            if (StringUtils.isEmpty(cmdbView.getId())) {
                resultMap.put("message", "新增视图失败！error: " + e.toString());
            } else {
                resultMap.put("message", "更新视图失败！error: " + e.toString());
            }
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> deleteView(@RequestParam("id") String id) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            viewService.delete(id);
            resultMap.put("success", true);
            resultMap.put("message", "删除视图成功！");
        } catch (Exception e) {
            log.error("删除视图失败！error: {}",  e);
            resultMap.put("success", false);
            resultMap.put("message", "删除视图失败！error: " + e.toString());
        }
        return resultMap;

    }

    @Override
    public Map<String, Object> deleteNode(@RequestParam("id") String id) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            nodeService.deleteById(id);
            resultMap.put("success", true);
            resultMap.put("message", "删除视图节点成功！");
        } catch (Exception e) {
            log.error("删除视图节点失败！error: {}",  e);
            resultMap.put("success", false);
            resultMap.put("message", "删除视图节点失败！error: " + e.toString());
        }
        return resultMap;
    }
}

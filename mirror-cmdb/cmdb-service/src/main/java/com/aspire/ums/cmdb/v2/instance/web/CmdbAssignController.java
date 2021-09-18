package com.aspire.ums.cmdb.v2.instance.web;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.instance.ICmdbAssignAPI;
import com.aspire.ums.cmdb.instance.payload.CmdbAssign;
import com.aspire.ums.cmdb.instance.payload.CmdbAssignQuery;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbAssignService;
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
 * FileName: CmdbAssignController
 * Author:   hangfang
 * Date:     2019/11/13
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbAssignController implements ICmdbAssignAPI {

    @Autowired
    private ICmdbAssignService assignService;

    @Override
    public Result<CmdbAssign> list(@RequestBody CmdbAssignQuery query) {
        Result<CmdbAssign> result = new Result<>();
        List<CmdbAssign> assignList = assignService.list(query);
        Integer count = assignService.listCount(query);
        result.setData(assignList);
        result.setCount(count);
        return result;
    }

    @Override
    public Map<String, Object> save(@RequestBody CmdbAssign assign) {
        Map<String, Object> resultMap = new HashMap<>();
        log.info("CmdbAssignController  save requestBody: {}", assign);
        try {
            assignService.save(assign);
            resultMap.put("success", true);
            resultMap.put("message", "新增成功");
        } catch (Exception e) {
            log.error("新增失败 error：" + e.getMessage());
            resultMap.put("success", false);
            resultMap.put("message", "新增失败");
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> delete(@RequestParam("id") String id) {
        Map<String, Object> resultMap = new HashMap<>();
        log.info("CmdbAssignController  delete id: {}", id);
        try {
            assignService.delete(id);
            resultMap.put("success", true);
            resultMap.put("message", "删除成功");
        } catch (Exception e) {
            log.error("删除失败 error：" + e.getMessage());
            resultMap.put("success", false);
            resultMap.put("message", "删除失败");
        }
        return resultMap;
    }
}

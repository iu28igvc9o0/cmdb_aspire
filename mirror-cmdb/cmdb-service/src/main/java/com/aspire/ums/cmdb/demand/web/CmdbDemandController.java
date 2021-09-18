package com.aspire.ums.cmdb.demand.web;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.demand.entity.InsertDemandEntity;
import com.aspire.ums.cmdb.demand.service.ICmdbDemandManagerService;
import com.aspire.ums.cmdb.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbDemandController
 * Author:   zhu.juwang
 * Date:     2019/5/9 16:30
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@RequestMapping("/cmdb/demandManager")
@Slf4j
public class CmdbDemandController {

    @Autowired
    private ICmdbDemandManagerService managerService;

    /**
     * 查询需求
     * @param queryMap 查询条件
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public JSONObject list(@RequestBody Map<String, Object> queryMap) {
        try {
            log.info("Request -> /cmdb/demandManager/list  data -> {}", JsonUtil.toJacksonJson(queryMap));
            JSONObject returnJson = managerService.queryForMap(queryMap);
            return returnJson;
        } catch (Exception e) {
            log.error("Query cmdb demand error. {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 查询需求
     * @param demandId 查询条件
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public InsertDemandEntity get(@RequestParam("demandId") String demandId) {
        try {
            log.info("Request -> /get/{demandId}  demandId -> {}", demandId);
            return managerService.getDemandInfoId(demandId);
        } catch (Exception e) {
            log.error("Get cmdb demand info error. {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 查询需求
     */
    @RequestMapping(value = "/list/header", method = RequestMethod.GET)
    public List<Map<String, Object>> getTableHeader() {
        try {
            log.info("Request -> /cmdb/demandManager/list/header");
//            List<Map<String, Object>> headerList = new LinkedList<>();
//            headerList = managerService.getTableHeader();
            return managerService.getTableHeader();
        } catch (Exception e) {
            log.error("Query cmdb demand header error. {}", e.getMessage(), e);
        }
        return null;
    }
    
    @RequestMapping(value = "/demandTypeList", method = RequestMethod.GET)
    public List<Map<String, Object>> getTypeAndValue() {
        try {
            log.info("Request -> /cmdb/demandManager/demandTypeList");
//            List<Map<String, Object>> headerList = new LinkedList<>();
//            headerList = managerService.getTypeAndValue();
            return managerService.getTypeAndValue();
        } catch (Exception e) {
            log.error("Query cmdb demand TypeList error. {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 新增需求
     * @param insertDemandEntity
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@RequestBody InsertDemandEntity insertDemandEntity) {
        try {
            log.info("Request -> /cmdb/demandManager/save  data -> {}", JsonUtil.toJacksonJson(insertDemandEntity));
            managerService.insert(insertDemandEntity);
            //response.setStatus(HttpStatus.NO_CONTENT.value());
            return "success";
        } catch (Exception e) {
            log.error("Save cmdb demand error. {}", e.getMessage(), e);
            //response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return "error";
        }
    }

    /**
     * 修改需求
     * @param insertDemandEntity
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public String update(HttpServletResponse response, @RequestBody InsertDemandEntity insertDemandEntity) {
        try {
            log.info("Request -> /cmdb/demandManager/update  data -> {}", JsonUtil.toJacksonJson(insertDemandEntity));
            managerService.update(insertDemandEntity);
            return "success";
        } catch (Exception e) {
            log.error("Update cmdb demand error. {}", e.getMessage(), e);
            return "error";
        }
    }
    
    @RequestMapping(value = "/exportDemand", method = RequestMethod.POST)
    public JSONObject exportDemand (@RequestBody Map<String, Object> sendRequest) {
        try {
            log.info("Request -> /cmdb/demandManager/exprotDemand  data -> {}", JsonUtil.toJacksonJson(sendRequest));
//            JSONObject returnJson = managerService.queryExportData(sendRequest);
            return managerService.queryExportData(sendRequest);
        } catch (Exception e) {
            log.error("Query cmdb demand error. {}", e.getMessage(), e);
        }
        return null;
    }
    
}

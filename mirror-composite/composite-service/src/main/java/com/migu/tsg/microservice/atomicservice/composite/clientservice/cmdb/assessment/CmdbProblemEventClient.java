package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.assessment;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.assessment.payload.CmdbProblemEvent;

/**
 * 项目名称:
 * 包: com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb
 * 类名称:
 * 类描述: 故障事件信息
 * 创建人: PJX
 * 创建时间: 2019/6/25 21:50
 * 版本: v1.0
 */
@FeignClient(value = "CMDB")
public interface CmdbProblemEventClient {
    
    /**
     * 查询所有故障事件信息
     * @param pageNum
     * @param pageSize
     * @param province
     * @param createUsername
     * @return
     */
    @GetMapping("/cmdb/device/problemEvent/list")
    Result<CmdbProblemEvent> getAllData(@RequestParam(value = "pageNum", required = false) int pageNum,
                                        @RequestParam(value = "pageSize", required = false) int pageSize,
                                        @RequestParam(value = "province",required = false) String province,
                                        @RequestParam(value = "quarter", required = false) String quarter,
                                        @RequestParam(value = "createUsername",required = false) String createUsername);
    
    /**
     * 新增更新故障事件信息
     * @param data
     * @return
     */
    @PostMapping("/cmdb/device/problemEvent/insertOrUpdate")
    Map<String, Object> insertOrUpdate(JSONObject data);
    
    @DeleteMapping("/cmdb/device/problemEvent/delete/{id}")
    Map<String, Object> delete(@PathVariable("id") String id);
}


package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.composite.service.inspection.payload.BizSystem;
import com.aspire.mirror.composite.service.inspection.payload.Concat;
import com.aspire.mirror.composite.service.inspection.payload.Result;

/**
 * 项目名称:
 * 包: com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/5/23 17:35
 * 版本: v1.0
 */
@FeignClient(value = "CMDB")
public interface BizSystemClient {
    @RequestMapping(value = "/cmdb/bizSystem/list", method = RequestMethod.GET)
    Result<BizSystem> getAllBizSystemData(@RequestParam(value = "pageNum",required = false) int pageNum,
                                          @RequestParam(value = "pageSize",required = false) int pageSize,
                                          @RequestParam(value = "pid",required = false) String pid,
                                          @RequestParam(value = "department1",required = false) String department1,
                                          @RequestParam(value = "department2",required = false) String department2,
                                          @RequestParam(value = "bizName",required = false) String bizName,
                                          @RequestParam(value = "isdisable",required = false) String isdisable);
    
    @PostMapping("/cmdb/bizSystem/add")
    String insert(@RequestBody BizSystem bizSystem);
    
    @GetMapping("/cmdb/bizSystem/getById")
    BizSystem getById (@RequestParam("id") String id);
    
    @PostMapping("/cmdb/bizSystem/update")
    String update(@RequestBody BizSystem bizSystem);
    
    @GetMapping("/cmdb/bizSystem/loadTree")
    List<BizSystem> loadTree();
    
    @DeleteMapping("/cmdb/bizSystem/deleteBiz")
    String deleteBiz(@RequestParam("id") String id);
    
    @GetMapping("/cmdb/bizSystem/getConcatsById")
    List<Concat> getConcatsById(@RequestParam("id") String id);

    /**
     * 根据多个业务系统ID获取业务系统列表
     * @param ids
     * @return
     */
    @RequestMapping(value = "/cmdb/bizSystem/getBizSystemByIds", method = RequestMethod.GET)
    List<Map<String, String>> getBizSystemByIds(@RequestParam("ids") String ids);
}

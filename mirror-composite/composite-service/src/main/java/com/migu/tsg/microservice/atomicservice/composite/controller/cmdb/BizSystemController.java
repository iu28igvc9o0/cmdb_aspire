package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb;

import com.aspire.mirror.composite.service.cmdb.IBizSystemService;
import com.aspire.mirror.composite.service.inspection.payload.BizSystem;
import com.aspire.mirror.composite.service.inspection.payload.Concat;
import com.aspire.mirror.composite.service.inspection.payload.Result;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.BizSystemClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.migu.tsg.microservice.atomicservice.composite.controller.cmdb
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/5/23 17:38
 * 版本: v1.0
 */
@RestController
@Slf4j
public class BizSystemController implements IBizSystemService {
    
    @Autowired
    private BizSystemClient bizSystemClient;
    
    @Override
    public Result<BizSystem> getAllBizSystemData(@RequestParam(value = "pageNum",required = false) int pageNum,
                                                 @RequestParam(value = "pageSize",required = false) int pageSize,
                                                 @RequestParam(value = "pid",required = false) String pid,
                                                 @RequestParam(value = "department1",required = false) String department1,
                                                 @RequestParam(value = "department2",required = false) String department2,
                                                 @RequestParam(value = "bizName",required = false) String bizName,
                                                 @RequestParam(value = "isdisable",required = false) String isdisable) {
        return bizSystemClient.getAllBizSystemData(pageNum,pageSize,pid,department1,department2,bizName,isdisable);
    }
    
    @Override
    public String insert(@RequestBody BizSystem bizSystem) {
        return bizSystemClient.insert(bizSystem);
    }
    
    @Override
    public BizSystem getById(@RequestParam("id") String id) {
        return bizSystemClient.getById(id);
    }
    
    @Override
    public String update(@RequestBody BizSystem bizSystem) {
        return bizSystemClient.update(bizSystem);
    }
    
    @Override
    public String deleteBiz(@RequestParam("id") String id) {
        return bizSystemClient.deleteBiz(id);
    }
    
    @Override
    public List<BizSystem> loadTree() {
        return bizSystemClient.loadTree();
    }
    
    @Override
    public List<Concat> getConcatsById(@RequestParam("id") String id) {
        return bizSystemClient.getConcatsById(id);
    }

    @Override
    public List<Map<String, String>> getBizSystemByIds(@RequestParam("ids") String ids) {
        return bizSystemClient.getBizSystemByIds(ids);
    }
}

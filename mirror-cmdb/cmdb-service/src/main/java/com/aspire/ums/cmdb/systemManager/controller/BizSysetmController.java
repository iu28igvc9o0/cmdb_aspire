package com.aspire.ums.cmdb.systemManager.controller;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.systemManager.IBizSysetmAPI;
import com.aspire.ums.cmdb.systemManager.payload.BizSystem;
import com.aspire.ums.cmdb.systemManager.payload.Concat;
import com.aspire.ums.cmdb.systemManager.service.BizSystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.systemManager.controller
 * 类名称: BizSysetmController
 * 类描述: 业务系统管理Controller
 * 创建人: PJX
 * 创建时间: 2019/5/21 15:23
 * 版本: v1.0
 */
@RefreshScope
@RestController
@Slf4j
public class BizSysetmController implements IBizSysetmAPI {
    
    @Autowired
    private BizSystemService bizSystemService;
    
    public Result<BizSystem> getAllBizSystemData(@RequestParam(value = "pageNum",required = false) int pageNum,
                                                 @RequestParam(value = "pageSize",required = false) int pageSize,
                                                 @RequestParam(value = "pid",required = false) String pid,
                                                 @RequestParam(value = "department1",required = false) String department1,
                                                 @RequestParam(value = "department2",required = false) String department2,
                                                 @RequestParam(value = "bizName",required = false) String bizName,
                                                 @RequestParam(value = "isdisable",required = false) String isdisable){
        return bizSystemService.getAllBizSystemData(pageNum,pageSize,pid,department1,department2,bizName,isdisable);
    }
    
    public List<BizSystem> loadTree() {
        return bizSystemService.loadTree();
    }
    
    public String insert(@RequestBody BizSystem bizSystem){
        return bizSystemService.insert(bizSystem);
    }
    
    public BizSystem getById (@RequestParam("id") String id){
        return bizSystemService.getBizSystemById(id);
    }
    
    public List<Concat> getConcatsById(@RequestParam("id") String id){
        return bizSystemService.getConcatsById(id);
    }
    
    public String update(@RequestBody BizSystem bizSystem){
        return bizSystemService.update(bizSystem);
    }
    
    public String deleteBiz(@RequestParam("id") String id){
        return bizSystemService.delete(id);
    }

    @Override
    public List<Map<String, String>> getBizSystemByIds(@RequestParam("ids") String ids) {
        return bizSystemService.getBizSystemByIds(ids);
    }

}

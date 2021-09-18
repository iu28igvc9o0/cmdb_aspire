package com.aspire.ums.cmdb.systemManager.controller;

import com.aspire.ums.cmdb.allocate.entity.Result;
import com.aspire.ums.cmdb.systemManager.entity.BizSystem;
import com.aspire.ums.cmdb.systemManager.entity.Concat;
import com.aspire.ums.cmdb.systemManager.service.BizSystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
@RequestMapping("/cmdb/bizSystem")
public class BizSysetmController {
    
    @Autowired
    private BizSystemService bizSystemService;
    
    @GetMapping("/list")
    public Result<BizSystem> getAllBizSystemData(@RequestParam(value = "pageNum",required = false) int pageNum,
                                                 @RequestParam(value = "pageSize",required = false) int pageSize,
                                                 @RequestParam(value = "pid",required = false) String pid,
                                                 @RequestParam(value = "bizName",required = false) String bizName,
                                                 @RequestParam(value = "isdisable",required = false) String isdisable){
        return bizSystemService.getAllBizSystemData(pageNum,pageSize,pid,bizName,isdisable);
    }
    
    @GetMapping("/loadTree")
    public List<BizSystem> loadTree() {
        return bizSystemService.loadTree();
    }
    
    @PostMapping("/add")
    public String insert(@RequestBody BizSystem bizSystem){
        return bizSystemService.insert(bizSystem);
    }
    
    @GetMapping("/getById")
    public BizSystem getById (@RequestParam("id") String id){
        return bizSystemService.getBizSystemById(id);
    }
    
    @GetMapping("/getConcatsById")
    public List<Concat> getConcatsById(@RequestParam("id") String id){
        return bizSystemService.getConcatsById(id);
    }
    
    @PostMapping("/update")
    public String update(@RequestBody BizSystem bizSystem){
        return bizSystemService.update(bizSystem);
    }
    
    @DeleteMapping("/deleteBiz")
    public String deleteBiz(@RequestParam("id") String id){
        return bizSystemService.delete(id);
    }
    
}

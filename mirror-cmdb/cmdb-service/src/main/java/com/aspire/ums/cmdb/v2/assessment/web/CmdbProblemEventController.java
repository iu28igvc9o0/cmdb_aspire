package com.aspire.ums.cmdb.v2.assessment.web;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.assessment.IProblemEventAPI;
import com.aspire.ums.cmdb.assessment.payload.CmdbProblemEvent;
import com.aspire.ums.cmdb.v2.assessment.service.ICmdbProblemEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.problemEvent.web
 * 类名称:
 * 类描述: 故障事件信息
 * 创建人: PJX
 * 创建时间: 2019/6/25 20:33
 * 版本: v1.0
 */
@RefreshScope
@RestController
@Slf4j
public class CmdbProblemEventController implements IProblemEventAPI {
    @Autowired
    private ICmdbProblemEventService problemEventService;
    
    /**
     * 查询所有故障事件信息
     * @param pageNum
     * @param pageSize
     * @param province
     * @param createUsername
     * @return
     */
    @Override
    public Result<CmdbProblemEvent> getAllData(@RequestParam(value = "pageNum",required = false) int pageNum,
                                               @RequestParam(value = "pageSize",required = false) int pageSize,
                                               @RequestParam(value = "province",required = false) String province,
                                               @RequestParam(value = "quarter", required = false) String quarter,
                                               @RequestParam(value = "createUsername",required = false) String createUsername) {
        return problemEventService.getAllData(pageNum,pageSize,province,quarter,createUsername);
    }
    
    /**
     * 新增更新故障事件信息
     * @param data
     * @return
     */
    @Override
    public Map<String, Object> insertOrUpdate(@RequestBody JSONObject data) {
        return problemEventService.insertOrUpdate(data);
    }
    
    /**
     * 删除故障事件信息
     */
    @Override
    public Map<String, Object> delete(@PathVariable("id") String id) {
        return problemEventService.delete(id);
    }
    
}

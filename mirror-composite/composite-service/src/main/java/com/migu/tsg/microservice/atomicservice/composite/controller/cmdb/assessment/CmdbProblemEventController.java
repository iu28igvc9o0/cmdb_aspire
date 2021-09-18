package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.assessment;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.assessment.ICmdbProblemEventService;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.assessment.payload.CmdbProblemEvent;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.assessment.CmdbProblemEventClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.migu.tsg.microservice.atomicservice.composite.controller.cmdb
 * 类名称:
 * 类描述: 故障事件信息
 * 创建人: PJX
 * 创建时间: 2019/6/25 21:54
 * 版本: v1.0
 */
@RestController
@Slf4j
public class CmdbProblemEventController implements ICmdbProblemEventService {
    
    @Autowired
    private CmdbProblemEventClient problemEventClient;
    
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
                                               @RequestParam(value = "page",required = false) String page,
                                               @RequestParam(value = "createUsername",required = false) String createUsername) {
        Result<CmdbProblemEvent> rs = problemEventClient.getAllData(pageNum,pageSize,province,quarter,createUsername);
        if (rs.getCount() > 0 && StringUtils.isNotEmpty(page) && "approve".equals(page)){
            // 审核状态 -1待审核 0拒绝  1通过 2保存中
            int status = rs.getData().get(0).getStatus();
            if (status == -1 || status == 1) {
                return rs;
            } else {
                return new Result<>();
            }
        }
        return rs;
    }
    
    /**
     * 新增更新故障事件信息
     * @param data
     * @return
     */
    @Override
    public Map<String, Object> insertOrUpdate(@RequestBody JSONObject data) {
        return problemEventClient.insertOrUpdate(data);
    }
    
    @Override
    public Map<String, Object> delete(@PathVariable("id") String id) {
        return problemEventClient.delete(id);
    }
}

package com.aspire.ums.cmdb.common.web;

import com.aspire.ums.cmdb.common.entity.QueryEntity;
import com.aspire.ums.cmdb.common.service.QueryService;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbQueryController
 * Author:   zhu.juwang
 * Date:     2019/3/25 15:31
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RefreshScope
@RestController
@Slf4j
@RequestMapping("/cmdb")
public class QueryController {

    @Autowired
    private QueryService queryService;

    /**
     * 获取自定义查询条件集合
     * @return 自定义查询条件集合
     */
    @RequestMapping(value = "/query/condition/", method = RequestMethod.GET)
    public List<QueryEntity> getQueryList(@RequestParam(value = "moduleId", required = false) String moduleId,
                                           @RequestParam(value = "user", required = false) String user,
                                           @RequestParam(value = "menuType", required = false) String menuType) {
        return queryService.getQueryConditionByModuleIdAndUserAndMenuType(moduleId, user, menuType);
    }

    /**
     * 新增自定义条件
     */
    @RequestMapping(value = "/query/condition/update", method = RequestMethod.PUT)
    public Map<String, String> insertOrUpdateQuery(@RequestBody QueryEntity entity) {
        Map<String, String> returnMap = new HashMap<>();
        try {
            if (StringUtils.isNotEmpty(entity.getId())) {
                queryService.updateVO(entity);
            } else {
                entity.setId(UUIDUtil.getUUID());
                queryService.insertVO(entity);
            }
            returnMap.put("code", "success");
        } catch (Exception e) {
            log.error("Insert|Update query condition error -> {}", e.getMessage());
            returnMap.put("code", "error");
            returnMap.put("message", e.getMessage());
        }
        return returnMap;
    }


    /**
     * 删除自定义条件
     */
    @RequestMapping(value = "/query/condition/delete/{queryId}", method = RequestMethod.DELETE)
    public Map<String, String> deleteQuery(@PathVariable("queryId") String queryId) {
        Map<String, String> returnMap = new HashMap<>();
        try {
            QueryEntity queryEntity = new QueryEntity();
            queryEntity.setId(queryId);
            queryService.deleteVO(queryEntity);
            returnMap.put("code", "success");
        } catch (Exception e) {
            log.error("Delete query condition error -> {}", e.getMessage());
            returnMap.put("code", "error");
            returnMap.put("message", e.getMessage());
        }
        return returnMap;
    }
}

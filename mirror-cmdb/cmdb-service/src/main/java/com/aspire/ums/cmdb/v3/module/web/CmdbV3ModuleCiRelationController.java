package com.aspire.ums.cmdb.v3.module.web;

import com.aspire.ums.cmdb.v3.module.ICmdbV3ModuleCiRelationAPI;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCiRelation;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCiRelationDetail;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCiRelationDetailResponse;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCiRelationService;
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
 * FileName: CmdbV3ModuleCiRelationController
 * Author:   hangfang
 * Date:     2020/4/26
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbV3ModuleCiRelationController implements ICmdbV3ModuleCiRelationAPI {

    @Autowired
    private ICmdbV3ModuleCiRelationService ciRelationService;

    @Override
    public List<CmdbV3ModuleCiRelation> listByModuleId(@RequestParam("moduleId") String moduleId) {
        return ciRelationService.listByModuleId(moduleId);
    }

    @Override
    public List<CmdbV3ModuleCiRelationDetail> listDetailByModuleId(@RequestParam("moduleId")String moduleId) {
        return ciRelationService.listRDetailByModuleId(moduleId);
    }

    @Override
    public Map<String, Object> save(@RequestParam("userName") String userName, @RequestBody CmdbV3ModuleCiRelation relation) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            ciRelationService.save(userName, relation);
            resultMap.put("success", true);
            resultMap.put("message", "保存成功");
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", "保存失败, error:" + e.toString());
        }
        return resultMap;
    }
    @Override
    public Map<String, Object> update(@RequestParam("userName") String userName, @RequestBody CmdbV3ModuleCiRelation relation) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            ciRelationService.update(userName, relation);
            resultMap.put("success", true);
            resultMap.put("message", "更新成功");
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", "更新失败, error:" + e.toString());
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> deleteById(@RequestParam("id") String id) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            ciRelationService.deleteById(id);
            resultMap.put("success", true);
            resultMap.put("message", "删除成功");
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", "删除失败, error:" + e.toString());
        }
        return resultMap;
    }

    @Override
    public CmdbV3ModuleCiRelationDetailResponse getCiRelationDetail(@RequestParam("relationId") String relationId,
                                                                    @RequestParam("instanceId") String instanceId) {
        return ciRelationService.getCiRelationDetail(relationId, instanceId);
    }
}

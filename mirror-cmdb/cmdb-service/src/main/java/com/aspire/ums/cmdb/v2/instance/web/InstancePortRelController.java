package com.aspire.ums.cmdb.v2.instance.web;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.instance.IInstancePortRelAPI;
import com.aspire.ums.cmdb.instance.payload.CmdbInstancePortQuery;
import com.aspire.ums.cmdb.instance.payload.CmdbInstancePortRelation;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstancePortRelationService;
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
 * FileName: RepairEventImportFactory
 * Author:   hangfang
 * Date:     2019/9/5
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class InstancePortRelController implements IInstancePortRelAPI {

    @Autowired
    private ICmdbInstancePortRelationService instancePortRelationService;

    /**
     * 获取当前资源池下所有ip
     * @param pool 资源池
     */
    @Override
    public List<Map<String, String>> getInstanceIpByPool(@RequestParam("pool") String pool) {
        return instancePortRelationService.getInstanceIpByPool(pool);
    }

    @Override
    public Boolean selectByPortAndId(@RequestBody CmdbInstancePortRelation portRelation) {
        List<CmdbInstancePortRelation> list = instancePortRelationService.selectByPortAndId(portRelation);
        if (null == list || list.size() == 0) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 获取端口关联设备列表
     * @param instancePortQuery 查询参数
     */
    @Override
    public Result<CmdbInstancePortRelation> list(@RequestBody CmdbInstancePortQuery instancePortQuery) {
        return instancePortRelationService.list(instancePortQuery);
    }

    /**
     * 获取端口关联设备列表
     * @param id 当前关联id
     */
    @Override
    public Map<String, Object> delete(@RequestParam String id) {
        Map<String,Object> map = new HashMap<>();
        try {
            CmdbInstancePortRelation portRelation = new CmdbInstancePortRelation();
            portRelation.setId(id);
            instancePortRelationService.delete(portRelation);
            map.put("success", true);
            map.put("message", "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除失败："+e);
            map.put("success", false);
            map.put("message", "删除失败");
        }
        return map;
    }

    /**
     * 获取端口关联设备列表
     * @param instancePortRelation 新增端口关联设备数据
     */
    @Override
    public Map<String, Object> insert(@RequestBody CmdbInstancePortRelation instancePortRelation) {
        Map<String,Object> map = new HashMap<>();
        try {
            instancePortRelationService.insert(instancePortRelation);
            map.put("success", true);
            map.put("message", "新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("新增失败："+e);
            map.put("success", false);
            map.put("message", "新增失败");
        }
        return map;
    }
}

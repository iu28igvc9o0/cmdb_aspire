package com.aspire.ums.cmdb.v2.process.instance;

import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.instance.payload.CmdbInstance;
import com.aspire.ums.cmdb.instance.payload.CmdbInstancePortRelation;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstancePortRelationService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.process.ImportFactory;
import com.aspire.ums.cmdb.v2.process.validate.DictValidator;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: InstancePortRalImportFactory
 * Author:   hangfang
 * Date:     2019/9/8
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
@NoArgsConstructor
public class InstancePortRalImportFactory extends ImportFactory {
    private ICmdbInstanceService instanceService;
    private ConfigDictService configDictService;
    private List<Map<String, String>> poolList;
    private List<Map<String, String>> connectTypeList;
    private ICmdbInstancePortRelationService instancePortRelationService;

    @Override
    public void initBasic() {
        super.initBasic();
        poolList = getDictList("idcType", null, null, null);
        connectTypeList = getDictList("resource_relation_type", null, null, null);
    }

    @Override
    public void formatRowDataMap(Map<String, String> dataMap) {
        super.formatRowDataMap(dataMap);
        dataMap.put("instanceId", super.getProcessParams().get("instanceId"));
    }

    @Override
    public void initSpringBean() {
        if (instancePortRelationService == null) {
            instancePortRelationService = SpringUtils.getBean(ICmdbInstancePortRelationService.class);
        }
        if (configDictService == null) {
            configDictService = SpringUtils.getBean(ConfigDictService.class);
        }
        if (instanceService == null) {
            instanceService = SpringUtils.getBean(ICmdbInstanceService.class);
        }
    }

    @Override
    public void execute(Map<String, String> dataMap) {
        CmdbInstancePortRelation portRelation = new CmdbInstancePortRelation();
        if (StringUtils.isEmpty(dataMap.get("instanceId"))) {
            throw new RuntimeException("请在设备详情下添加相关数据");
        }
        portRelation.setAInstanceId(dataMap.get("instanceId"));
        for (String key : dataMap.keySet()) {
            if (key.contains("必填") && StringUtils.isEmpty(dataMap.get(key))) {
                throw new RuntimeException("列[" + key + "] 不能为空");
            }
            if (dataMap.get(key).length() > 40) {
                throw new RuntimeException("列[" + key + "]长度超过范围, 请控制长度在40以内");
            }
        }
        CmdbInstance instance = new CmdbInstance();
        String pool = dataMap.get("对端资源池[必填]");
        DictValidator.validator("对端资源池[必填]", pool, this.poolList);
        instance.setIdcType(dataMap.get("对端资源池[必填]"));
        instance.setIp(dataMap.get("对端设备IP[必填]"));
        instance = this.instanceService.get(instance);
        if (null == instance || StringUtils.isEmpty(instance.getId())) {
            throw new RuntimeException("根据对端资源池和对端设备IP未获取到相应设备");
        }
        portRelation.setZInstanceId(instance.getId());
        portRelation.setAPortId(dataMap.get("本地端口[必填]"));
        portRelation.setZPortId(dataMap.get("对端设备端口[必填]"));
        portRelation.setZPortId(dataMap.get("对端设备端口[必填]"));
        portRelation.setRemark(dataMap.get("描述"));
        DictValidator.validator("连接属性[必填]", dataMap.get("连接属性[必填]"), this.connectTypeList);
        portRelation.setConnectType(dataMap.get("连接属性[必填]"));
        List<CmdbInstancePortRelation> list = this.instancePortRelationService.selectByPortAndId(portRelation);
        if (null != list && list.size() > 0) {
            throw new RuntimeException("当前关系已经存在");
        }
        this.instancePortRelationService.insert(portRelation);
    }
}


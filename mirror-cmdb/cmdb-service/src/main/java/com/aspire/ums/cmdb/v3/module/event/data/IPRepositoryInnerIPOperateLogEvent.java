package com.aspire.ums.cmdb.v3.module.event.data;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.aspire.ums.cmdb.cmic.util.EventThreadUtils;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v3.module.event.AbstractModuleEvent;
import com.aspire.ums.cmdb.v3.module.event.publicFunc.OperateLogHelper;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司 FileName: IPRepositoryInnerIPOperateLogEvent Author: hangfang Date: 2020/5/26 Description:
 * DESCRIPTION History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
@Slf4j
public class IPRepositoryInnerIPOperateLogEvent extends AbstractModuleEvent {

    @Autowired
    private OperateLogHelper logHelper;

    @Override
    public void initSpringBeans() {
        if (this.logHelper == null) {
            this.logHelper = SpringUtils.getBean(OperateLogHelper.class);
        }
    }

    @Override
    public Map<String, Object> event(String moduleId, String instanceId, Map<String, Object> handleData) {
        this.initSpringBeans();
        handleData.put("type", "ip");
        EventThreadUtils.FIXED_POOL.execute(() -> logHelper.toLog("inner_ip_need_fileds", moduleId, instanceId, handleData));
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("msg", "添加操作日志成功！");
        resultMap.put("flag", true);
        return resultMap;
    }

}

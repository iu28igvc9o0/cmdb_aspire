package com.aspire.ums.cmdb.v3.module.event.data;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.aspire.ums.cmdb.cmic.util.EventThreadUtils;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v3.module.event.AbstractModuleEvent;
import com.aspire.ums.cmdb.v3.module.event.publicFunc.OperateLogHelper;

/**
 * 机房机柜操作日志
 *
 * @author jiangxuwen
 * @date 2021/2/4 15:21
 */
public class ServerProjectCabinetOpereateLogEvent extends AbstractModuleEvent {

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
        handleData.put("type", "server_project_cabinet");
        EventThreadUtils.FIXED_POOL
                .execute(() -> logHelper.toLog("server_project_cabinet_fields", moduleId, instanceId, handleData));
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("msg", "添加操作日志成功！");
        resultMap.put("flag", true);
        return resultMap;
    }

}

package com.aspire.ums.cmdb.v3.module.event.code;

import java.util.HashMap;
import java.util.Map;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.aspire.ums.cmdb.cmic.service.ICmdbCmicInstanceService;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.schema.service.SchemaService;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceIpManagerService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import com.aspire.ums.cmdb.v3.module.event.AbstractModuleEvent;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCatalogService;

/**
 * 加电设备台数设置加电状态
 *
 * @author jiangxuwen
 * @date 2021/2/22 18:05
 */
public class ServerCabinetStatusEvent extends AbstractModuleEvent {

    protected ModuleService moduleService;

    protected ICmdbInstanceService instanceService;

    protected ICmdbInstanceIpManagerService ipManagerService;

    protected ICmdbV3ModuleCatalogService catalogService;

    protected SchemaService schemaService;

    protected ICmdbCmicInstanceService cmicInstanceService;

    protected ICmdbConfigService cmdbConfigService;

    protected ConfigDictService configDictService;

    protected ThreadPoolTaskExecutor taskExecutor;

    @Override
    public void initSpringBeans() {
        moduleService = moduleService == null ? SpringUtils.getBean(ModuleService.class) : moduleService;
        instanceService = instanceService == null ? SpringUtils.getBean(ICmdbInstanceService.class) : instanceService;
        ipManagerService = ipManagerService == null ? SpringUtils.getBean(ICmdbInstanceIpManagerService.class) : ipManagerService;
        catalogService = catalogService == null ? SpringUtils.getBean(ICmdbV3ModuleCatalogService.class) : catalogService;
        schemaService = schemaService == null ? SpringUtils.getBean(SchemaService.class) : schemaService;
        cmicInstanceService = cmicInstanceService == null ? SpringUtils.getBean(ICmdbCmicInstanceService.class)
                : cmicInstanceService;
        cmdbConfigService = cmdbConfigService == null ? SpringUtils.getBean(ICmdbConfigService.class) : cmdbConfigService;
        configDictService = configDictService == null ? SpringUtils.getBean(ConfigDictService.class) : configDictService;
        taskExecutor = taskExecutor == null ? SpringUtils.getBean("taskExecutor", ThreadPoolTaskExecutor.class) : taskExecutor;
    }

    @Override
    public Map<String, Object> event(String moduleId, String instanceId, Map<String, Object> handleData) {
        Map<String, Object> returnMap = new HashMap<>();
        // String id1 = configDictService.getIdByNoteAndCol("是", "whether");
        // String id2 = configDictService.getIdByNoteAndCol("否", "whether");
        // if (handleData.containsKey("online_count") && handleData.get("online_count").toString() != null) {
        // int onlineCount = Integer.parseInt(handleData.get("online_count").toString());
        // if (onlineCount > 0) {
        // handleData.put("server_online_status", id1);
        // }
        // if (onlineCount <= 0) {
        // handleData.put("server_online_status", id2);
        // }
        //
        // }
        returnMap.put("flag", true);
        returnMap.put("msg", "操作成功");
        return returnMap;
    }
}

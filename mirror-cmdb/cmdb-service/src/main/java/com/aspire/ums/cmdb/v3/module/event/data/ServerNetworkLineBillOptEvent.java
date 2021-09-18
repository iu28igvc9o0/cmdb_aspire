package com.aspire.ums.cmdb.v3.module.event.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.aspire.ums.cmdb.cmic.service.ICmdbCmicInstanceService;
import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.schema.service.SchemaService;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceIpManagerService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import com.aspire.ums.cmdb.v3.module.event.AbstractModuleEvent;
import com.aspire.ums.cmdb.v3.module.event.EventConst;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCatalogService;
import com.google.common.collect.Maps;

/**
 * 网络线路结算导入时，要按照网络线路管理填充数据
 *
 * @author jiangxuwen
 * @date 2021/2/22 18:33
 */
public class ServerNetworkLineBillOptEvent extends AbstractModuleEvent {

    protected ModuleService moduleService;

    protected ICmdbInstanceService instanceService;

    protected ICmdbInstanceIpManagerService ipManagerService;

    protected ICmdbV3ModuleCatalogService catalogService;

    protected SchemaService schemaService;

    protected ICmdbCmicInstanceService cmicInstanceService;

    protected ICmdbConfigService cmdbConfigService;

    protected ConfigDictService configDictService;

    protected ICmdbCodeService cmdbCodeService;

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
        cmdbCodeService = cmdbCodeService == null ? SpringUtils.getBean(ICmdbCodeService.class) : cmdbCodeService;
        taskExecutor = taskExecutor == null ? SpringUtils.getBean("taskExecutor", ThreadPoolTaskExecutor.class) : taskExecutor;
    }

    @Override
    public Map<String, Object> event(String moduleId, String instanceId, Map<String, Object> handleData) {
        Map<String, Object> returnMap = Maps.newHashMap();
        String optType = handleData.get("changeType").toString();
        if (!EventConst.EVENT_TYPE_DATA_UPDATE.equals(optType)) {
            return returnMap;
        }
        Map<String, Object> updateData = instanceService.getInstanceDetail(moduleId, instanceId);
        String networkLineModuleId = "a15eea62bf0c4872afe1c9630c3f5191";
        Module module = moduleService.getModuleDetail(networkLineModuleId);
        List<String> primaryKeys = moduleService.getModulePrimaryKeys(networkLineModuleId, updateData);
        List<CmdbCode> codeList = cmdbCodeService.getCodeListByModuleId(networkLineModuleId);
        // 主键列的值
        List<Map<String, Object>> primaryQueryList = new ArrayList<>();
        for (String primaryKey : primaryKeys) {
            CmdbCode cmdbCode = null;
            for (CmdbCode code : codeList) {
                if (code.getFiledCode().equals(primaryKey)) {
                    cmdbCode = code;
                    break;
                }
            }
            if (cmdbCode != null) {
                // 主键值为空
                if (!StringUtils.isNotEmpty(updateData.get(primaryKey).toString())) {
                    throw new RuntimeException("主键[" + cmdbCode.getFiledCode() + "]字段值不能为空.");
                }
                // moduleKeysMap.put(cmdbCode.getFiledCode(), instanceData.get(cmdbCode.getFiledCode()));
                Map<String, Object> moduleKeysMap = new HashMap<>();
                moduleKeysMap.put("filed", cmdbCode.getFiledCode());
                moduleKeysMap.put("require", true);
                moduleKeysMap.put("value", updateData.get(cmdbCode.getFiledCode()));
                moduleKeysMap.put("operator", "=");
                moduleKeysMap.put("filed_type", cmdbCode.getControlType().getControlCode());
                primaryQueryList.add(moduleKeysMap);
            }
        }
        // 拼装查询条件
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("query_module_id", module.getId());
        queryParams.put("index", module.getModuleCatalog().getCatalogCode());
        queryParams.put("type", module.getCode());
        queryParams.put("params", primaryQueryList);
        Map<String, Object> networkLineData = instanceService.getInstanceByPrimaryKey(queryParams);

        updateData.putAll(networkLineData);
        updateData.put("instance_id", instanceId);
        updateData.put("id", instanceId);
        updateData.put("module_id", moduleId);
        instanceService.updateInstance(updateData);
        returnMap.put("flag", true);
        returnMap.put("msg", "操作成功");
        return returnMap;
    }

}

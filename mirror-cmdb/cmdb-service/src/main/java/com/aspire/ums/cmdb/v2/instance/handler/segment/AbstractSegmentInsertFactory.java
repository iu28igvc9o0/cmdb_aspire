package com.aspire.ums.cmdb.v2.instance.handler.segment;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.aspire.ums.cmdb.cmic.service.ICmdbCmicInstanceService;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.instance.payload.CmdbInstanceIpManager;
import com.aspire.ums.cmdb.schema.service.SchemaService;
import com.aspire.ums.cmdb.util.DateUtils;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.instance.handler.AbstractInstanceInsertFactory;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceIpManagerService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCatalog;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCatalogService;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司 FileName: AbstarctInstanceInsertFactory Author: zhu.juwang Date: 2020/7/6 10:58 Description:
 * ${DESCRIPTION} History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
@Slf4j
public abstract class AbstractSegmentInsertFactory extends AbstractInstanceInsertFactory {

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
    public void initSpring() {
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

    /**
     * 网段新增逻辑
     * 
     * @param userName
     *            新增用户
     * @param instanceData
     *            实例数据
     * @param operateType
     *            操作方式
     * @return 返回状态信息
     */
    @Override
    public Map<String, Object> handler(String userName, Map<String, Object> instanceData, String operateType) {
        this.initSpring();
        Map<String, Object> returnMap = new HashMap<>();
        try {
            if (!instanceData.containsKey("module_id")) {
                returnMap.put("flag", "error");
                returnMap.put("msg", "Params[module_id] is require.");
                return returnMap;
            }
            String moduleId = instanceData.get("module_id").toString();
            if (moduleService.getModuleDataByPrimarys(moduleId, instanceData) != null) {
                returnMap.put("flag", "error");
                returnMap.put("msg", "当前设备已存在.");
                return returnMap;
            }
            String instanceId = UUIDUtil.getUUID();
            Map<String, Object> handelResult = instanceService.handleModuleData(instanceId, moduleId, instanceData);
            Map<String, Map<String, Object>> moduleData = (Map<String, Map<String, Object>>) handelResult.get("moduleData");
            if (!moduleData.containsKey(moduleId)) {
                moduleData.put(moduleId, new HashMap<>());
            }
            List<CmdbInstanceIpManager> ipManagerList = (List<CmdbInstanceIpManager>) handelResult.get("ipManagerList");
            // 创建IP地址信息
            if (ipManagerList.size() > 0) {
                ipManagerService.insertByBatch(instanceId, ipManagerList);
            }
            // 创建其他模型
            for (String refModuleId : moduleData.keySet()) {
                // 每个模型都要存ci的id
                moduleData.get(refModuleId).put("id", instanceId);
                CmdbV3ModuleCatalog catalog = catalogService.getByModuleId(refModuleId);
                if (catalog == null) {
                    throw new RuntimeException("未查到引用模型Id[" + refModuleId + "]分组信息");
                }
                // 数据库插入数据
                if (moduleData.get(refModuleId).keySet().size() > 0) {
                    Map<String, Object> temp = moduleData.get(refModuleId);
                    String catalogCode = catalog.getCatalogCode();
                    // 新增网段时，更新人员录入信息 modify fanwenhui 20200722
                    if ("cmdb_ip_repository".equals(catalogCode)) {
                        temp.put("insert_person", userName);
                        temp.put("insert_time", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                        temp.put("update_person", userName);
                        temp.put("update_time", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    schemaService.insertCi(catalog.getCatalogCode(), temp);
                }
            }
            // 创建IP信息
            this.createIp(userName, instanceData, operateType);
            returnMap.put("flag", "success");
            returnMap.put("msg", "已成功录入配置审核，请联系管理里进行审核");
            return returnMap;
        } catch (Exception e) {
            log.error("", e);
            returnMap.put("flag", "error");
            returnMap.put("msg", e.getMessage());
            return returnMap;
        }
    }

    /**
     * 网段新增逻辑
     * 
     * @param userName
     *            新增用户
     * @param instanceData
     *            实例数据
     * @param operateType
     *            操作方式
     * @return 返回状态信息
     */
    @Override
    public Map<String, Object> execute(String userName, Map<String, Object> instanceData, String operateType) {
        return handler(userName, instanceData, operateType);
    }

    /**
     * 网段新增逻辑
     * 
     * @param userName
     *            新增用户
     * @param instanceData
     *            实例数据
     * @param operateType
     *            操作方式
     * @return 返回状态信息
     */
    public abstract void createIp(String userName, Map<String, Object> instanceData, String operateType);
}

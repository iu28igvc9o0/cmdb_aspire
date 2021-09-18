package com.aspire.ums.cmdb.v3.es.service.impl;

import com.aspire.mirror.elasticsearch.api.dto.cmdb.Result;
import com.aspire.ums.cmdb.client.ICmdbESClient;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.helper.JDBCHelper;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.schema.service.SchemaService;
import com.aspire.ums.cmdb.sqlManage.CmdbSqlManage;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceIpManagerService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.es.service.ICmdbESService;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCodeSetting;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCodeSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: CmdbESServiceImpl
 * Author:   zhu.juwang
 * Date:     2020/2/17 9:45
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
@Slf4j
public class CmdbESServiceImpl implements ICmdbESService {

    @Autowired
    private ICmdbESClient cmdbESClient;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private SchemaService schemaService;
    @Autowired
    private ICmdbInstanceIpManagerService ipManagerService;
    @Autowired
    private ICmdbV3ModuleCodeSettingService codeSettingService;
    @Autowired
    private JDBCHelper jdbcHelper;
    @Value("${cmdb.query.db:mysql}")
    private String queryDbType;

    private void validQueryDB() {
        if (queryDbType != null && !queryDbType.toLowerCase(Locale.ENGLISH).equals("es")) {
            throw new RuntimeException("Current use db isn't ES. Please change db to es.");
        }
    }

    @Override
    public Map<String, String> insert(List<Map<String, Object>> data, String index, String type) {
        this.validQueryDB();
        return cmdbESClient.insert(data, index, type);
    }

    @Override
    public Map<String, Object> deleteIndex(String index) {
        this.validQueryDB();
        return cmdbESClient.deleteIndex(index);
    }

    @Override
    public Result<Map<String, Object>> list(Map<String, Object> querySetting) {
        this.validQueryDB();
        try {
            return cmdbESClient.list(querySetting);
        } catch (Exception e) {
            log.error("Query es data error. {}", e.getMessage());
            return new Result<>(0, new ArrayList<>());
        }
    }

    @Override
    public Map<String, Object> queryDetail(Map<String, Object> querySetting) {
        this.validQueryDB();
        try {
            return cmdbESClient.queryDetail(querySetting);
        } catch (Exception e) {
            log.error("Query es data error. {}", e.getMessage());
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, Object> getById(String id, String index, String type) {
        this.validQueryDB();
        try {
            return cmdbESClient.getById(id, index, type);
        } catch (Exception e) {
            log.error("Query es data error. {}", e.getMessage());
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, String> writeModuleData(String moduleId) {
        this.validQueryDB();
        final List<Module> moduleList = new ArrayList<>();
        if (StringUtils.isNotEmpty(moduleId) && ("ip_manager").equals(moduleId)) {
            // 跳过处理
        } else if (StringUtils.isNotEmpty(moduleId)) {
            Module module = moduleService.getModuleDetail(moduleId);
            moduleList.add(module);
        } else {
            moduleList.addAll(moduleService.getModuleList());
        }
        Map<String, String> returnMap = new HashMap<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (Module module : moduleList) {
                    try {
                        log.info("Begin sync module -> {} data.", module.getId());
                        String sql = moduleService.getModuleQuerySQL(module.getId());
                        if (StringUtils.isEmpty(sql)) {
                            returnMap.put("flag", "error");
                            String msg = returnMap.get("msg") == null ? "" : returnMap.get("msg");
                            msg = String.format(msg + "[%s-错误:未获取到模型对应的SQL配置, 无法获取模型数据.]", module.getCode());
                            log.error(msg);
                            continue;
                        }
                        CmdbSqlManage sqlManage = new CmdbSqlManage(sql, null, Constants.UN_NEED_AUTH);
                        List<Map<String, Object>> list = jdbcHelper.getQueryList(sqlManage,  null, null, null,null);
                        log.info("Module [{}] find {} record need to sync es.", module.getId(), list.size());
                        Module detailModule = moduleService.getModuleDetail(module.getId());
                        writeToES(list, detailModule.getModuleCatalog().getCatalogCode(), detailModule.getCode());
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
                if (StringUtils.isNotEmpty(moduleId) && ("ip_manager").equals(moduleId)) {
                    // IP地址信息
                    List<Map<String, Object>> ipList = ipManagerService.getAllIpManagerList(null);
                    writeToES(ipList, "cmdb_instance_ip_manager", "ip_manager");
                }
            }
        }).start();
        returnMap.put("flag", "success");
        return returnMap;
    }

    @Override
    public void asyncRefresh(String moduleId, String instanceId) {
        this.validQueryDB();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取模型依赖的所有模型列表
                CmdbV3ModuleCodeSetting querySetting = new CmdbV3ModuleCodeSetting();
                querySetting.setModuleId(moduleId);
                List<CmdbV3ModuleCodeSetting> codeSettingList = codeSettingService.listByEntity(querySetting);
                List<String> handlerModuleIds = new ArrayList<>();
                if (codeSettingList != null && codeSettingList.size() > 0) {
                    for (CmdbV3ModuleCodeSetting codeSetting : codeSettingList) {
                        String handlerModuleId = codeSetting.getOwnerModuleId();
                        if (!handlerModuleIds.contains(handlerModuleId)) {
                            handlerModuleIds.add(handlerModuleId);
                            writeModuleDataToES(handlerModuleId, instanceId);
                        }
                    }
                }
                // 处理本身的模型数据
                if (!handlerModuleIds.contains(moduleId)) {
                    writeModuleDataToES(moduleId, instanceId);
                }
                // 处理IP地址信息
                List<Map<String, Object>> ipList = ipManagerService.getAllIpManagerList(instanceId);
                writeToES(ipList, "cmdb_instance_ip_manager", "ip_manager");
            }
        }).start();
    }

    @Override
    public List<Map<String, Object>> queryDeviceByIpAndIdcType(Map<String, Object> params) {
        this.validQueryDB();
        try {
            return cmdbESClient.queryDeviceByIpAndIdcType(params);
        } catch (Exception e) {
            log.error("Query es data error. {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Map<String, Object>> stats(Map<String, Object> params, String index, String type) {
        return cmdbESClient.stats(params, index, type);
    }

    /**
     *
     * @param handlerModuleId 需要处理的模型ID
     * @param instanceId 需要处理的实例ID
     */
    private void writeModuleDataToES(String handlerModuleId, String instanceId) {
        this.validQueryDB();
        String sql = moduleService.getModuleQuerySQL(handlerModuleId);
        if (StringUtils.isEmpty(sql)) {
            log.error("错误:未获取到模型[{}]对应的SQL配置, 无法获取模型数据.", handlerModuleId);
            return;
        }
        sql += " and id=#{id}";
        Map<String, Object> params = new HashMap<>();
        params.put("id", instanceId);
        CmdbSqlManage sqlManage = new CmdbSqlManage(sql, null, Constants.UN_NEED_AUTH);
        List<Map<String, Object>> list = jdbcHelper.getQueryList(sqlManage, null, null, null, params);
        log.info("查询到需要写入的数据 --> {}", list);
        Module detailModule = moduleService.getModuleDetail(handlerModuleId);
        writeToES(list, detailModule.getModuleCatalog().getCatalogCode(), detailModule.getCode());
        log.info("模型[{}] 实例[{}]数据写入完成.", handlerModuleId, instanceId);
    }

    private void writeToES(List<Map<String, Object>> dataList, String index, String type) {
        Integer totalCount = dataList.size();
        List<Map<String, Object>> pageList = new LinkedList<>();
        for (int i = 0; i < totalCount; i++) {
            pageList.add(dataList.get(i));
            if (i % 1000 == 0 && i > 0) {
                // 1000条提交一次
                Map<String, String> esResult = insert(pageList, index, type);
                if (esResult.get("flag").equals("false")) {
                    log.error(String.format("[%s-错误:%s.]", index, esResult.get("msg")));
                }
                pageList = new LinkedList<>();
                try {
                    // 留3秒给ES数据处理
                    Thread.sleep(3 * 1000);
                } catch (InterruptedException e) {
                    log.error("添加线程睡眠失败.", e);
                }
                log.info(index + "数据[{}-{}]入库成功.", (i - 1000), i);
            }
            // 最后一条也时 也需要提交
            if (i == totalCount - 1) {
                // 提交
                Map<String, String> esResult = insert(pageList, index, type);
                if (esResult.get("flag").equals("false")) {
                    log.error(String.format("[%s-错误:%s.]", index, esResult.get("msg")));
                }
                pageList = new LinkedList<>();
                log.info(index + "数据[{}-{}]入库成功.", (i - 1000), i);
            }
        }
    }
}

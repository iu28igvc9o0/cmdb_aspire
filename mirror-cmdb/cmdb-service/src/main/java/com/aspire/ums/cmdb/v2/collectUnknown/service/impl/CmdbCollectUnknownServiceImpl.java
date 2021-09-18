package com.aspire.ums.cmdb.v2.collectUnknown.service.impl;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknown;
import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknownQuery;
import com.aspire.ums.cmdb.dict.mapper.ConfigDictMapper;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.instance.payload.CmdbInstance;
import com.aspire.ums.cmdb.instance.payload.CmdbInstanceIpManager;
import com.aspire.ums.cmdb.instance.payload.CmdbUpdateInstance;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.cache.CacheConst;
import com.aspire.ums.cmdb.v2.cache.CodeCache;
import com.aspire.ums.cmdb.v2.code.mapper.CmdbCodeMapper;
import com.aspire.ums.cmdb.v2.collectUnknown.mapper.CmdbCollectUnknownMapper;
import com.aspire.ums.cmdb.v2.collectUnknown.service.CmdbCollectUnknownService;
import com.aspire.ums.cmdb.v2.instance.mapper.CmdbInstanceMapper;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.CmdbConst;
import com.aspire.ums.cmdb.v2.module.mapper.ModuleMapper;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCatalog;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCatalogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbCollectUnknownServiceImpl
 * Author:   hangfang
 * Date:     2019/10/10
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
@Slf4j
public class CmdbCollectUnknownServiceImpl implements CmdbCollectUnknownService {

    @Autowired
    private CmdbCollectUnknownMapper collectUnknownMapper;

    @Autowired
    private CmdbInstanceMapper instanceMapper;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private ICmdbV3ModuleCatalogService catalogService;
    @Autowired
    private ConfigDictMapper configDictMapper;
    @Autowired
    private ConfigDictService dictService;


    @Autowired
    private ICmdbInstanceService cmdbInstanceService;

    private static final Integer UNHANDLE = 0;

    private static final Integer HANDLE = 1;

    private static final Integer SHIELD = 2;

    private static final List STATUS = Arrays.asList(new Integer[]{0, 1, 2});

    @Override
    public Result<CmdbCollectUnknown> list(CmdbCollectUnknownQuery collectUnknownQuery) {
        Result<CmdbCollectUnknown> res = new Result<>();
        List<CmdbCollectUnknown> list = collectUnknownMapper.list(collectUnknownQuery);
        Integer total = collectUnknownMapper.listCount(collectUnknownQuery);
        res.setCount(total);
        res.setData(list);
        return res;
    }

    @Override
    public void insert(CmdbCollectUnknown collectUnknown) {
        validInsertData(collectUnknown);
        String ip = collectUnknown.getIp();
//        Map<String, String> idcMap=dictService.getIdcTypeByName(collectUnknown.getIdcType());
//        collectUnknown.setIdcType(idcMap.get("id"));
        String pool = collectUnknown.getIdcType();

        Integer count = collectUnknownMapper.countShieldAndUnhand(ip, pool);
        if (count > 0) {
            log.info("数据来源: {},当前ip: {} 资源池: {} 存在未处理数据或已被屏蔽", collectUnknown.getDataFrom(), ip, pool);
        } else {
            if (StringUtils.isNotEmpty(collectUnknown.getDeviceType())) {
                ConfigDict dict = dictService.getDictByColNameAndDictCode("device_type",collectUnknown.getDeviceType());
                collectUnknown.setDeviceType(dict.getId());
            }
            collectUnknown.setId(UUIDUtil.getUUID());
            collectUnknown.setCommitTime(new Date());
            collectUnknown.setHandleStatus(UNHANDLE);
            collectUnknownMapper.insert(collectUnknown);
        }
    }

    @Override
    public List<Map<String, Object>> insertByBatch(List<CmdbCollectUnknown> collectUnknowns) {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> resultMap = new HashMap<>();
        for (CmdbCollectUnknown collectUnknown : collectUnknowns) {
           try {
               insert(collectUnknown);
               resultMap.put("success", true);
               resultMap.put("ip", collectUnknown.getIp());
               resultMap.put("pool", collectUnknown.getIdcType());
               resultMap.put("msg", "添加成功");
           } catch (Exception e) {
               resultMap.put("success", false);
               resultMap.put("ip", collectUnknown.getIp());
               resultMap.put("pool", collectUnknown.getIdcType());
               resultMap.put("msg", e.getMessage());
           }
            result.add(resultMap);
        }
        return result;

    }

    private void validInsertData(CmdbCollectUnknown collectUnknown) {
        if (StringUtils.isEmpty(collectUnknown.getIp())) {
            throw new RuntimeException("ip不能为空");
        }
        if (StringUtils.isEmpty(collectUnknown.getIdcType())) {
            throw new RuntimeException("资源池不能为空");
        }
        if (StringUtils.isEmpty(collectUnknown.getCommitUser())) {
            throw new RuntimeException("提交人不能为空");
        }
        if (StringUtils.isEmpty(collectUnknown.getDataFrom())) {
            throw new RuntimeException("数据来源不能为空");
        }
    }

    @Override
    public void update(CmdbCollectUnknown collectUnknown) {
        validUpdateData(collectUnknown);
        CmdbCollectUnknown sourceUnknown = collectUnknownMapper.get(collectUnknown.getId());
//        if (HANDLE.intValue() == collectUnknown.getHandleStatus().intValue()) {
//            CmdbInstance instance = new CmdbInstance();
//            instance.setIp(sourceUnknown.getIp());
//            instance.setIdcType(sourceUnknown.getIdcType());
//            instance = instanceMapper.get(instance);
//            if (null != instance) {
//                sourceUnknown.setInstanceId(instance.getId());
//                sourceUnknown.setDeviceName(instance.getDeviceName());
//            } else {
//                throw new RuntimeException("当前设备未查询到已维护数据，请先维护");
//            }
//        }
        sourceUnknown.setHandleStatus(collectUnknown.getHandleStatus());
        sourceUnknown.setHandleUser(collectUnknown.getHandleUser());
        sourceUnknown.setHandleTime(new Date());
        collectUnknownMapper.update(sourceUnknown);
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class, SQLException.class})
    public void maintain(CmdbCollectUnknown collectUnknown) {
        Module module = new Module();
        // 获取设备类型名称
        List<ConfigDict> dicts = configDictMapper.getIdcTypeList();
        if (null == dicts) {
            throw new RuntimeException("资源池字典值查询为空");
        }
        boolean flag = false;
        for (ConfigDict d : dicts) {
            if (collectUnknown.getIdcType().equals(d.getValue())) {
                flag = true;
            }
        }
        if (!flag) {
            throw new RuntimeException("资源池：" + collectUnknown.getIdcType() + "不存在");
        }
        module = moduleService.getDefaultModule(null);
        CmdbV3ModuleCatalog catalog = new CmdbV3ModuleCatalog();
        catalog.setCatalogName(collectUnknown.getDeviceType());
        catalog = catalogService.get(catalog);
        catalog = catalogService.getById(catalog.getParentCatalogId());
        Map<String, Object> params = new HashMap<>();
        params.put("ip",collectUnknown.getIp());
        params.put("idcType",collectUnknown.getIdcType());
        params.put("device_name",collectUnknown.getIp());
        params.put("device_class", catalog.getCatalogName());
        params.put("device_type",collectUnknown.getDeviceType());
        params.put("insert_person",collectUnknown.getHandleUser());
        params.put("module_id",module.getId());
        cmdbInstanceService.addInstance(collectUnknown.getHandleUser(),params  , "设备维护");
//        module.setName(collectUnknown.getDeviceType());
//        List<Module> modules = moduleMapper.getModuleSelective(module);
//        if (modules == null || modules.size() == 0) {
//            throw new RuntimeException("模型：" + module.getName() + " 不存在");
//        }
//        module = modules.get(0);
//        // 获取设备分类名称
//        Module parentModule = moduleMapper.getModuleDetail(module.getParentId());
//        List<CmdbUpdateInstance.CmdbInstancePrimaryTable> primaryTables = new ArrayList<>();
//        // 解析主表数据
//        CmdbUpdateInstance updateInstance = new CmdbUpdateInstance();
//        primaryTables.add(new CmdbUpdateInstance.CmdbInstancePrimaryTable("ip", collectUnknown.getIp(), null));
//        primaryTables.add(new CmdbUpdateInstance.CmdbInstancePrimaryTable("idcType", collectUnknown.getIdcType(), null));
//        primaryTables.add(new CmdbUpdateInstance.CmdbInstancePrimaryTable("device_name", collectUnknown.getDeviceName(), null));
//        primaryTables.add(new CmdbUpdateInstance.CmdbInstancePrimaryTable("device_class", parentModule.getName(), null));
//        primaryTables.add(new CmdbUpdateInstance.CmdbInstancePrimaryTable("device_type", module.getName(), null));
//        primaryTables.add(new CmdbUpdateInstance.CmdbInstancePrimaryTable("insert_person", collectUnknown.getHandleUser(), null));
//        updateInstance.setInstanceTableList(primaryTables);
//        updateInstance.setOtherTableList(new ArrayList<>());

        // 解析ip表数据
        // todo 以下代码需要改造
//        CmdbCode ipCode = new CmdbCode();
//        ipCode.setFiledCode("ip");
//        ipCode = codeMapper.get(ipCode);
//        CmdbInstanceIpManager ipManager = new CmdbInstanceIpManager();
//        ipManager.setCodeId(ipCode.getCodeId());
//        ipManager.setIp(collectUnknown.getIp());
//        List<CmdbInstanceIpManager> ipManagerList = new ArrayList<>();
//        ipManagerList.add(ipManager);
//        updateInstance.setIpManagerList(ipManagerList);
//        cmdbInstanceService.insert(modules.get(0), updateInstance);
        // 新增成功后更新未知设备状态
        collectUnknown.setHandleUser(collectUnknown.getHandleUser());
        collectUnknown.setHandleStatus(HANDLE);
        collectUnknown.setHandleTime(new Date());
        update(collectUnknown);
    }

    private void validUpdateData(CmdbCollectUnknown collectUnknown) {
        if (StringUtils.isEmpty(collectUnknown.getId())) {
            throw new RuntimeException("更新id不能为空");
        }
        if (StringUtils.isEmpty(collectUnknown.getHandleUser())) {
            throw new RuntimeException("处理人不能为空");
        }
        if (null == collectUnknown.getHandleStatus() || !STATUS.contains(collectUnknown.getHandleStatus())) {
            throw new RuntimeException("处理状态不能为空");
        }
    }
}

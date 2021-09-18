package com.aspire.ums.cmdb.IpResource.service.impl;

import com.aspire.ums.cmdb.IpResource.mapper.CmdbConfigDictMapper;
import com.aspire.ums.cmdb.IpResource.mapper.CmdbFinancialBusinessMapper;
import com.aspire.ums.cmdb.IpResource.service.ComboboxService;
import com.aspire.ums.cmdb.dict.mapper.ConfigDictMapper;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.sync.entity.CmdbBusinessLine;
import com.aspire.ums.cmdb.sync.mapper.CmdbBusinessLineMapper;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/6/16 20:08
 */
@Slf4j
@Service("ComboboxService")
public class ComboboxServiceImpl implements ComboboxService {

    @Autowired
    private CmdbFinancialBusinessMapper financialBusinessMapper;// 一级业务线
    @Autowired
    private CmdbBusinessLineMapper businessLineMapper;// 独立业务线
    @Autowired
    private ConfigDictMapper configDictMapper; //字典表
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private ICmdbConfigService cmdbConfigService;

    @Override
    public List<Map<String, Object>> getIdcVal() {
        List<Map<String, Object>> dataList = Lists.newArrayList();
        List<ConfigDict> dictList = configDictMapper.selectDictsByType("idcType2", null, null, null);
        dictList.stream().forEach(item -> {
            dataList.add(new HashMap() {{
                put("value", item.getId());
                put("label", item.getValue());
            }});
        });
        return dataList;
    }

    @Override
    public List<Map<String, Object>> getFirstBusiness(String financialId) {
        List<Map<String, Object>> resultList;
        List<Map<String, Object>> dataList = Lists.newArrayList();
        String valueName;
        if (StringUtils.isEmpty(financialId)) {
            resultList = financialBusinessMapper.getFinanciaList("a47956a854ff4f63928a5a32181bc2c6");
//            resultList = moduleService.getRefModuleDict("b86b3460ef8649ecbc647d59a9a5eab9");
            if (resultList.isEmpty()) {
                CmdbConfig cmdbConfig = cmdbConfigService.getConfigByCode("first_business_line");
                String configValue = cmdbConfig.getConfigValue();
                resultList = moduleService.getRefModuleDict(configValue);
            }
        } else {
            resultList = financialBusinessMapper.getBusinessByFinanciaId(financialId);
        }
        resultList.stream().forEach(item -> {
            dataList.add(new HashMap() {{
                put("value", item.get("id"));
                put("label", item.get("value"));
            }});
        });
        return dataList;
    }

    @Override
    public List<Map<String, Object>> getAloneBusiness(String aloneId) {
        List<Map<String, Object>> dataList = Lists.newArrayList();
        List<CmdbBusinessLine> resultList;
        if (StringUtils.isEmpty(aloneId)) {
            resultList = businessLineMapper.aloneList();
        } else {
            CmdbBusinessLine businessLine = new CmdbBusinessLine();
            businessLine.setParentId(aloneId);
            resultList = businessLineMapper.listByEntity(businessLine);
        }
        resultList.stream().forEach(item -> {
            dataList.add(new HashMap() {{
                put("value", item.getId());
                put("label", item.getBusinessName());
            }});
        });
        return dataList;
    }

    @Override
    public List<Map<String, Object>> getSegmentType(String pid) {
        String pType = "inner_segment_type";
        String colName = "inner_sub_segment_type";
        List<ConfigDict> dictList;
        List<Map<String, Object>> dataList = Lists.newArrayList();
        if (StringUtils.isEmpty(pid)) {
            dictList = configDictMapper.selectDictsByType(pType, null, null, null);
        } else {
            dictList = configDictMapper.selectDictsByTypeOrPid(colName, pid);
        }
        dictList.stream().forEach(item -> {
            dataList.add(new HashMap() {{
                put("value", item.getId());
                put("label", item.getName());
            }});
        });
        return dataList;
    }

    @Override
    public List<Map<String, Object>> getDevicesClassOrType(String pid) {
        List<ConfigDict> dictList;
        String pType = "device_class";
        String colName = "device_type";
        List<Map<String, Object>> dataList = Lists.newArrayList();
        if (StringUtils.isEmpty(pid)) {
            dictList = configDictMapper.selectDictsByType(pType, null, null, null);
        } else {
            dictList = configDictMapper.selectDictsByTypeOrPid(colName, pid);
        }
        // 用于去重
        List<String> tempList = new ArrayList<>();
        dictList.forEach(item -> {
            String idKey = item.getId();
            if(!tempList.contains(idKey)) {
                tempList.add(idKey);
                dataList.add(new HashMap() {{
                    put("value", item.getId());
                    put("label", item.getName());
                }});
            }
        });
        return dataList;
    }

    @Override
    public List<Map<String, Object>> getIdcLocationType() {
        List<ConfigDict> dictList = configDictMapper.selectDictsByType("idcLocationType", null, null, null);
        List<Map<String, Object>> dataList = Lists.newArrayList();
        dictList.stream().forEach(item -> {
            dataList.add(new HashMap() {{
                put("value", item.getId());
                put("label", item.getName());
            }});
        });
        return dataList;
    }

    @Override
    public List<Map<String, Object>> getInnerSegmentUse(String pid) {
        pidIsEmpty(pid);
        List<ConfigDict> dictList;
        List<Map<String, Object>> dataList = Lists.newArrayList();
        dictList = configDictMapper.selectDictsByTypeOrPid("inner_segment_use", pid);
        dictList.stream().forEach(item -> {
            dataList.add(new HashMap() {{
                put("value", item.getId());
                put("label", item.getName());
            }});
        });
        return dataList;
    }

    @Override
    public List<Map<String, Object>> getInnerSegmentSubUse(String pid) {
        pidIsEmpty(pid);
        List<ConfigDict> dictList;
        List<Map<String, Object>> dataList = Lists.newArrayList();
        dictList = configDictMapper.selectDictsByTypeOrPid("inner_segment_sub_use", pid);
        dictList.stream().forEach(item -> {
            dataList.add(new HashMap() {{
                put("value", item.getId());
                put("label", item.getName());
            }});
        });
        return dataList;
    }

    @Override
    public List<Map<String, Object>> getPublicSegmentUse(String pid) {
        String pType = "public_segment_use";
        String colName = "public_segment_sub_use";
        List<ConfigDict> dictList;
        List<Map<String, Object>> dataList = Lists.newArrayList();
        if (StringUtils.isEmpty(pid)) {
            dictList = configDictMapper.selectDictsByType(pType, null, null, null);
        } else {
            dictList = configDictMapper.selectDictsByTypeOrPid(colName, pid);
        }
        dictList.stream().forEach(item -> {
            dataList.add(new HashMap() {{
                put("value", item.getId());
                put("label", item.getName());
            }});
        });
        return dataList;
    }

    @Override
    public List<Map<String, Object>> getIpv6SegmentUse(String pid) {
        String pType = "ipv6_segment_use";
        String colName = "ipv6_segment_sub_use";
        List<ConfigDict> dictList;
        List<Map<String, Object>> dataList = Lists.newArrayList();
        if (StringUtils.isEmpty(pid)) {
            dictList = configDictMapper.selectDictsByType(pType, null, null, null);
        } else {
            dictList = configDictMapper.selectDictsByTypeOrPid(colName, pid);
        }
        dictList.stream().forEach(item -> {
            dataList.add(new HashMap() {{
                put("value", item.getId());
                put("label", item.getName());
            }});
        });
        return dataList;
    }

    @Override
    public List<Map<String, Object>> getIpTypeForAsset(String pid) {
        String pType = "ipTypeForAsset";
        List<ConfigDict> dictList;
        List<Map<String, Object>> dataList = Lists.newArrayList();
        dictList = configDictMapper.selectDictsByType(pType, null, null, null);
        dictList.stream().forEach(item -> {
            if (StringUtils.isEmpty(pid)) {//为空时，过滤无0和1的 全部
                if (item.getValue().startsWith("2,")) {
                    return;
                }
            } else if (pid.equals("2")) {//为2时，过滤 全部
                if (item.getValue().contains(",")) {
                    return;
                }
            } else {//为2时，过滤包含0和1的选项
                if (item.getValue().contains("0") || item.getValue().contains("1")) {
                    return;
                }
            }
            dataList.add(new HashMap() {{
                put("value", item.getValue());
                put("label", item.getName());
            }});
        });
        return dataList;
    }

    private void pidIsEmpty(String pid) {
        if (StringUtils.isEmpty(pid)) {
            throw new RuntimeException("pid不能为空！");
        }
    }
}

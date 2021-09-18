package com.migu.tsg.microservice.atomicservice.composite.helper;

import com.aspire.mirror.common.constant.SystemConstant;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.biz.ConfigDictBiz;
import com.migu.tsg.microservice.atomicservice.composite.biz.InstanceBiz;
import com.migu.tsg.microservice.atomicservice.composite.dao.po.ConfigDict;
import com.migu.tsg.microservice.atomicservice.composite.vo.cmdb.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author baiwenping
 * @Title: CmdbV2Helper
 * @Package com.migu.tsg.microservice.atomicservice.composite.helper
 * @Description: TODO
 * @date 2021/3/16 10:55
 */
@Component
@Slf4j
public class CmdbV2Helper {
    @Autowired
    private List<InstanceBiz> instanceBizList;
    @Autowired
    private List<ConfigDictBiz> configDictBizList;
    @Value("${systemType:simple}")
    private String systemType;

    private static final String CMDB_BIZSYSTEM_QUERY_BY_IDS = "alert_query_bizSystem_info_by_ids";

    private static final String CMDB_DEPARTMENT_QUERY_BY_IDS = "alert_query_department_info_by_ids";

    private static final ConcurrentHashMap<String, List<Map<String, Object>>> DICTMAP = new ConcurrentHashMap();

    /*****************************字典部分 start********************************/

    /**
     * 根据code获取字典值
     *
     * @param code code
     * @return String 机房名称
     */
    public String getCodeName(String code, String codeValue) {
        List<Map<String, Object>> listCode = listOption(code);
        return getOptionName(codeValue, listCode);
    }
    /**
     * 获取公共可选项列表
     *
     * @param code 选项编码
     * @return
     */
    public List<Map<String, Object>> listOption(String code) {
        List<Map<String, Object>> listOption = Lists.newArrayList();
        if (StringUtils.isEmpty(code)) {
            return listOption;
        }

        if (DICTMAP.size() > 0 && DICTMAP.get(code) != null) {
            return DICTMAP.get(code);
        }
        List<ConfigDict> rs = searchDictByType(code);
        if (!CollectionUtils.isEmpty(rs)) {
            for (ConfigDict cfd : rs) {
                Map map = new HashMap();
                map.put("name", cfd.getName());
                map.put("value", cfd.getValue());
                map.put("id", cfd.getId());
                listOption.add(map);
            }
            DICTMAP.put(code, listOption);
        }
        return listOption;
    }

    private List<ConfigDict> searchDictByType(String type) {
        if (SystemConstant.BIZ_SYSTEM_BDC.equals(systemType)) {
            for (ConfigDictBiz configDictBiz: configDictBizList) {
                if (configDictBiz.isBasic()) {
                    return configDictBiz.selectDictsByType(type, null, null, null);
                }
            }
        } else {
            for (ConfigDictBiz configDictBiz: configDictBizList) {
                if (!configDictBiz.isBasic()) {
                    return configDictBiz.selectDictsByType(type, null, null, null);
                }
            }
        }
        return configDictBizList.get(0).selectDictsByType(type, null, null, null);
    }

    private List<String> searchDictType() {
        if (SystemConstant.BIZ_SYSTEM_BDC.equals(systemType)) {
            for (ConfigDictBiz configDictBiz: configDictBizList) {
                if (configDictBiz.isBasic()) {
                    return configDictBiz.getDictType();
                }
            }
        } else {
            for (ConfigDictBiz configDictBiz: configDictBizList) {
                if (!configDictBiz.isBasic()) {
                    return configDictBiz.getDictType();
                }
            }
        }
        return configDictBizList.get(0).getDictType();
    }
    /**
     * 定时刷新内存
     */
    @Scheduled(cron = "0 0 1 * * ? ")
    public void flushDict() {
        try {
            List<String> typeList = searchDictType();
            for (String type : typeList) {
                List<ConfigDict> rs  = searchDictByType(type);
                if (!CollectionUtils.isEmpty(rs)) {
                    List<Map<String, Object>> listOption = Lists.newArrayList();
                    for (ConfigDict cfd : rs) {
                        Map map = new HashMap();
                        map.put("name", cfd.getName());
                        map.put("value", cfd.getValue());
                        map.put("id", cfd.getId());
                        listOption.add(map);
                    }
                    DICTMAP.put(type, listOption);
                }
            }
        } catch (Exception e) {
            log.error("flush dict error", e);
        }
    }

    /**
     * 根据字段可选项的value值取name
     *
     * @param optionCode 选项的value值
     * @param listBizSys 选项列表
     * @return String 选项的name
     */
    private String getOptionName(String optionCode, List<Map<String, Object>> listBizSys) {
        if (StringUtils.isEmpty(optionCode)) {
            return "";
        }
        for (Map<String, Object> map : listBizSys) {
            String value = (String) map.get("value");
            if (value.equals(optionCode)) {
                return (String) map.get("name");
            }
        }
        return null;
    }

    /*****************************字典部分 end ********************************/

    /*****************************实例部分 start********************************/
    /**
     * 根据条件查询实例详情
     * @param params
     * @return
     */
    public Map<String, Object> getInstanceDetail(Map<String, Object> params) {
        for (InstanceBiz instanceBiz: instanceBizList) {
            if (!instanceBiz.isBasic()) {
                return instanceBiz.getInstanceDetail(params);
            }
        }
        return instanceBizList.get(0).getInstanceDetail(params);
    }

    /**
     * 查询cmdb统计数据
     * @param params
     * @param name
     * @param responseType
     * @return
     */
    public Object getCmdbData(Map<String, Object> params, String name, String responseType) {
        for (InstanceBiz instanceBiz: instanceBizList) {
            if (!instanceBiz.isBasic()) {
                return instanceBiz.getCmdbData(params, name, responseType);
            }
        }
        return instanceBizList.get(0).getCmdbData(params, name, responseType);
    }

    /**
     * 查询listV3接口实例列表
     * @param params
     * @param moduleType
     * @return
     */
    public ResultVo<Map<String, Object>> getInstanceListV3(Map<String, Object> params, String moduleType) {
        for (InstanceBiz instanceBiz: instanceBizList) {
            if (!instanceBiz.isBasic()) {
                return instanceBiz.getInstanceListV3(params, moduleType);
            }
        }
        return instanceBizList.get(0).getInstanceListV3(params, moduleType);
    }

    /**
     * 设置对应cmdb字段对应的中文数据
     * @param esData
     * @param bizType
     */
    public void setBizDepartmentData(Map<String, Map<String, Long>> esData,String bizType) {
        if (null != esData && esData.size() > 0) {
            Set<String> set = esData.keySet();
            List<String> list = new ArrayList<>(set);
            List<Map<String, Object>> dataList = getDepartmentDataListByType(list, bizType);
            for (Map<String, Object> m : dataList) {
                String key = MapUtils.getString(m, "id");
                String value = MapUtils.getString(m, bizType);
                if (esData.containsKey(key)) {
                    esData.put(value, esData.remove(key));
                }
            }
        }
    }

    /**
     * 查询业务系统和租户信息，私有
     * @param idList
     * @param type
     * @return
     */
    private List<Map<String, Object>> getDepartmentDataListByType(List<String> idList, String type) {
        String responseType = "list";
        String name = "";
        Map<String, Object> params = Maps.newHashMap();
        if (type.equals("bizSystem")) {
            name = CMDB_BIZSYSTEM_QUERY_BY_IDS;
            params.put("bizSystem", idList);
        }
        if (type.equals("department")) {
            name = CMDB_DEPARTMENT_QUERY_BY_IDS;
            params.put("department", idList);
        }

        List<Map<String, Object>> dataList = (List<Map<String, Object>>) getCmdbData(params, name, responseType);
        return dataList;
    }
    /*****************************实例部分 end ********************************/

    /******************************机房、业务系统、资源池等start********************************************/
    /**
     * 根据id集合查询机房
     * @param ids
     * @return
     */
    public List<Map<String, String>> getRoomByIds(String ids) {
        for (InstanceBiz instanceBiz: instanceBizList) {
            if (!instanceBiz.isBasic()) {
                return instanceBiz.getRoomByIds(ids);
            }
        }
        return instanceBizList.get(0).getRoomByIds(ids);
    }

    /**
     * 根据id集合查询业务系统
     * @param ids
     * @return
     */
    public List<Map<String, String>> getBizSystemByIds(String ids) {
        for (InstanceBiz instanceBiz: instanceBizList) {
            if (!instanceBiz.isBasic()) {
                return instanceBiz.getBizSystemByIds(ids);
            }
        }
        return instanceBizList.get(0).getBizSystemByIds(ids);
    }

    /******************************机房、业务系统、资源池等end********************************************/

}

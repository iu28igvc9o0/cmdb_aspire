package com.migu.tsg.microservice.atomicservice.composite.helper;

import com.aspire.mirror.common.constant.SystemConstant;
import com.aspire.mirror.composite.service.inspection.payload.ConfigDict;
import com.aspire.ums.cmdb.instance.payload.CmdbInstance;
import com.aspire.ums.cmdb.restful.payload.StatisticRequestEntity;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.biz.ConfigDictService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.alert.CmdbInstanceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.CmdbServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.ConfigDictClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.restful.common.ICommonRestfulClient;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * cmdb帮助类
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.helper
 * 类名称:    CmdbHelper.java
 * 类描述:    cmdb帮助类
 * 创建人:    JinSu
 * 创建时间:  2018/9/25 10:25
 * 版本:      v1.0
 */
@Component
@Slf4j
public class CmdbHelper {
    @Autowired
    private CmdbServiceClient cmdbService;

    @Autowired
    private ConfigDictClient configDictService;

    @Autowired
    private ConfigDictService dictServiceClient;
    @Autowired
    private CmdbInstanceClient cmdbInstanceClient;

    @Value("${systemType:simple}")
    private String systemType;
    @Autowired
    private ICommonRestfulClient commonRestfulClient;
    

    private final ConcurrentHashMap<String, List<Map>> dictMap = new ConcurrentHashMap();
    
    private static final String CMDB_BIZSYSTEM_QUERY_BY_IDS = "alert_query_bizSystem_info_by_ids";

	private static final String CMDB_DEPARTMENT_QUERY_BY_IDS = "alert_query_department_info_by_ids";
	
	@Value("${cmdbQueryType.department:alert_query_department_by_bizSystem_id}")
	private String cmdbQueryName;

    /**
     * 定时刷新内存
     */
    @Scheduled(cron = "0 0 1 * * ? ")
    public void flushDict() {
        try {
            /*Map request = Maps.newHashMap();
            //设置查询条件为公共模型
            request.put("id", "0");
            Map<String, Object> form = cmdbService.getForms(request);
            List<Map> formList = (List<Map>) form.get("formData");
            if (!CollectionUtils.isEmpty(formList)) {
                for (Map map : formList) {
                    dictMap.put((String)map.get("code"), (List<Map>) map.get("formOptions"));
                }
            }*/
            List<String> typeList = configDictService.getDictType();
            for (String type : typeList) {
                List<ConfigDict> rs;
                if (SystemConstant.BIZ_SYSTEM_BDC.equals(systemType)) {
                    rs = dictServiceClient.selectDictsByType(type,null, null, null);
                } else {
                    rs = configDictService.getDictsByType(type, null, null, null);
                }
                if (!CollectionUtils.isEmpty(rs)) {
                    List<Map> listOption = Lists.newArrayList();
                    for (ConfigDict cfd : rs) {
                        Map map = new HashMap();
                        map.put("name", cfd.getName());
                        map.put("value", cfd.getValue());
                        listOption.add(map);
                    }
                    dictMap.put(type, listOption);
                }
            }
        } catch (Exception e) {
            log.error("flush dict error", e);
        }
    }

    /**
     * 获取公共可选项列表
     *
     * @param code 选项编码
     * @return
     */
    public Object listOption(String code) {
        List<Map> listOption = Lists.newArrayList();
        if (StringUtils.isEmpty(code)) {
            return listOption;
        }

        if (dictMap.size() > 0 && dictMap.get(code) != null) {
            return dictMap.get(code);
        }
        /*Map request = Maps.newHashMap();
        //设置查询条件为公共模型
        request.put("id", "0");
        Map<String, Object> form = cmdbService.getForms(request);
        List<Map> formList = (List<Map>) form.get("formData");
        if (!CollectionUtils.isEmpty(formList)) {
            for (Map map : formList) {
                if (map.get("code").equals(code)) {
                    listOption = (List<Map>) map.get("formOptions");
                }
                dictMap.put((String)map.get("code"), (List<Map>) map.get("formOptions"));
            }
        }*/
        List<ConfigDict> rs;
        if (SystemConstant.BIZ_SYSTEM_BDC.equals(systemType)) {
            rs = dictServiceClient.selectDictsByType(code, null, null, null);
        } else {
            rs = configDictService.getDictsByType(code, null, null, null);
        }
        if (!CollectionUtils.isEmpty(rs)) {
            for (ConfigDict cfd : rs) {
                Map map = new HashMap();
                map.put("name", cfd.getName());
                map.put("value", cfd.getValue());
                listOption.add(map);
            }
            dictMap.put(code, listOption);
        }
        return listOption;
    }


    /**
     * 获取机房列表
     *
     * @return
     */
    public Object listRoom() {
        return listOption("roomId");
    }

    /**
     * 获取业务系统列表
     *
     * @return
     */
    public Object listBizSys() {
        return listOption("bizSystem");
    }

    /**
     * @param bizSys 业务系统value
     * @return String
     */
    public String getBizSysName(String bizSys) {
        List<Map> listBizSys = (List<Map>) listBizSys();
        return getOptionName(bizSys, listBizSys);
    }

    /**
     * 根据字段可选项的value值取name
     *
     * @param optionCode 选项的value值
     * @param listBizSys 选项列表
     * @return String 选项的name
     */
    private String getOptionName(String optionCode, List<Map> listBizSys) {
        if (StringUtils.isEmpty(optionCode)) {
            return "";
        }
        for (Map map : listBizSys) {
            String value = (String) map.get("value");
            if (value.equals(optionCode)) {
                return (String) map.get("name");
            }
        }
        return null;
    }

    /**
     * 根据code获取值
     *
     * @param room 机房value
     * @return String 机房名称
     */
    public String getRoomName(String room) {
        List<Map> listRoom = (List<Map>) listRoom();
        return getOptionName(room, listRoom);
    }

    /**
     * 根据code获取值
     *
     * @param code code
     * @return String 机房名称
     */
    public String getCodeName(String code, String codeValue) {
        List<Map> listCode = (List<Map>) listOption(code);
        return getOptionName(codeValue, listCode);
    }

    /**
     * 根据机房和设备ip查找设备信息. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param idc      机房名称
     * @param deviceIp 设备IP
     * @return
     */
    public CmdbInstance queryDeviceByRoomIdAndIP(String idc, String deviceIp) {
        if (SystemConstant.BIZ_SYSTEM_BDC.equals(systemType)) {
            return null;
        }
//        return cmdbInstanceClient.queryDeviceByRoomIdAndIP(idc, deviceIp);
        return null;
    }
    
 // cmdb通用接口调用
 	public Object getCmdbData(Map<String, Object> params, String name, String responseType) {
 		StatisticRequestEntity entity = new StatisticRequestEntity();
 		entity.setName(name);
 		entity.setParams(params);
 		entity.setResponseType(responseType);
 		Object value = commonRestfulClient.getInstanceStatistics(entity);
 		return value;
 	}
 	
 // 返回业务系统或租户的id、namelist
 	public List<Map<String, Object>> getDepartmentDataListByType(List<String> idList, String type) {
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

 	// 返回业务系统或租户的id、name映射
 	public Map<String, String> getDepartmentDataMapByTye(List<String> idList, String type) {
 		List<Map<String, Object>> dataList = getDepartmentDataListByType(idList, type);
 		if (null == dataList || dataList.size() == 0) {
 			return null;
 		}
 		Map<String, String> map = Maps.newHashMap();
 		for (Map<String, Object> m : dataList) {
 			if (type.equals("bizSystem")) {
 				map.put( MapUtils.getString(m, "id"), MapUtils.getString(m, "bizSystem"));
 			}
 			if (type.equals("department")) {
 				map.put( MapUtils.getString(m, "id"), MapUtils.getString(m, "department"));
 			}

 		}
 		return map;
 	}
 	
 	public void formListBizData(List<Map<String, Object>> dataList, List<String> bizList) {
		Map<String, Map<String, Object>> bizMap = Maps.newHashMap();
		if (bizList == null || bizList.size() == 0) {
			bizList = Lists.newArrayList();
			for (Map<String, Object> entry : dataList) {
				// 获取租户1，2
				String bizSystem = entry.get("bizSystem").toString();
				bizList.add(bizSystem);

			}
		}
		Map<String, Object> params = Maps.newHashMap();
		params.put("id", bizList);
		Object value = getCmdbData(params, this.cmdbQueryName, "list");
		if (null != value) {
			List<Map<String, Object>> departList = (List<Map<String, Object>>) value;
			for (Map<String, Object> m : departList) {
				String bizSystem = m.get("id").toString();
				bizMap.put(bizSystem, m);
			}
			for (Map<String, Object> entry : dataList) {
				String bizSystem = entry.get("bizSystem").toString();
				Map<String, Object> bizObj = bizMap.get(bizSystem);
				
				if (null != bizObj) {
					entry.put("department1", bizObj.get("department1_name"));
					entry.put("department2", bizObj.get("department2_name"));
					entry.put("bizSystem",bizObj.get("bizSystem"));
				}
			}
		}
		
	}
 	
 // 根据业务系统id查询一二级租户信息
 	public void formMapBizData(Map<String, Map<String, Object>> dataMap, List<String> bizList) {
 		Map<String, Map<String, Object>> bizMap = Maps.newHashMap();
 		if (bizList == null || bizList.size() == 0) {
 			bizList = Lists.newArrayList();
 			for (Map.Entry<String, Map<String, Object>> entry : dataMap.entrySet()) {
 				// 获取租户1，2
 				String bizSystem = entry.getValue().get("bizSystem").toString();
 				bizList.add(bizSystem);

 			}
 		}
 		Map<String, Object> params = Maps.newHashMap();
 		params.put("id", bizList);
 		Object value = getCmdbData(params, this.cmdbQueryName, "list");
 		if (null != value) {
 			List<Map<String, Object>> departList = (List<Map<String, Object>>) value;
 			for (Map<String, Object> m : departList) {
 				String bizSystem = m.get("id").toString();
 				bizMap.put(bizSystem, m);
 			}
 			for (Map.Entry<String, Map<String, Object>> entry : dataMap.entrySet()) {
 				Map<String, Object> v = entry.getValue();
 				String bizSystem = v.get("bizSystem").toString();
 				Map<String, Object> bizObj = bizMap.get(bizSystem);
 				if (null != bizObj) {
 					v.put("bizSystem_id",bizObj.get("id"));
 					v.put("department1_id",bizObj.get("department1"));
 					v.put("department2_id",bizObj.get("department2"));
 					v.put("department1", bizObj.get("department1_name"));
 					v.put("department2", bizObj.get("department2_name"));
 					v.put("bizSystem",bizObj.get("bizSystem"));
 				}
 			}
 		}
 		
 	}
 	
	public Map<String, Map<String, Object>> getBizDepartmentDatas( List<String> bizList) {
 		Map<String, Map<String, Object>> bizMap = Maps.newHashMap();
 		if (bizList == null || bizList.size() == 0) {
 			return bizMap;
 		}
 		Map<String, Object> params = Maps.newHashMap();
 		params.put("id", bizList);
 		Object value = getCmdbData(params, this.cmdbQueryName, "list");
 		if (null != value) {
 			List<Map<String, Object>> departList = (List<Map<String, Object>>) value;
 			for (Map<String, Object> m : departList) {
 				String bizSystem = m.get("id").toString();
 				bizMap.put(bizSystem, m);
 			}
 		}
 		return bizMap;
	}
 	
 	public void setBizDepartmentData(Map<String, Map<String, Long>> esData,String bizType){
		if(null != esData && esData.size() > 0) {
			Set<String> set = esData.keySet();
			List<String> list = new ArrayList<>(set);
			List<Map<String, Object>> dataList = getDepartmentDataListByType(list, bizType);
			for(Map<String, Object> m:dataList) {
				String key = MapUtils.getString(m, "id");
				String value =MapUtils.getString(m, bizType);
				if(esData.containsKey(key)) {
					esData.put(value, esData.remove(key));
				}
			}
		}
	}
}

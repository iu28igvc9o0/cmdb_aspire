package com.aspire.ums.cmdb.v2.index.serivce.impl;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.index.payload.*;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.index.mapper.ItCloudScreenMapper;
import com.aspire.ums.cmdb.v2.index.serivce.ItCloudScreenBizSystemDeviceDetailService;
import com.aspire.ums.cmdb.v2.index.serivce.ItCloudScreenCheckScoreService;
import com.aspire.ums.cmdb.v2.index.serivce.ItCloudScreenService;
import com.aspire.ums.cmdb.v2.index.util.ScreenDataUtil;
import com.aspire.ums.cmdb.v2.index.util.SuppleUtil;
import com.aspire.ums.cmdb.v3.config.mapper.CmdbConfigMapper;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName ItCloudScreenServiceImpl
 * @Description IT云租户大屏——服务类接口实现类
 * @Author luowenbo
 * @Date 2020/2/26 21:44
 * @Version 1.0
 */
@Service
public class ItCloudScreenServiceImpl implements ItCloudScreenService {
    @Autowired
    private ItCloudScreenMapper itCloudScreenMapper;
    @Autowired
    private ItCloudScreenCheckScoreService checkScoreService;
    @Autowired
    private ItCloudScreenBizSystemDeviceDetailService deviceDetailService;
    @Autowired
    private CmdbConfigMapper cmdbConfigMapper;

    @Override
    @Transactional(rollbackFor = {SQLException.class, RuntimeException.class, Exception.class})
    public void insertResourceAllocate(ScreenResourceAllocation obj) {
        if(obj != null) {
            //obj.setSystemTitleId(findIdByTitle(obj.getSystemTitleId())); //将标题替换成ID
            obj.setId(UUIDUtil.getUUID());
            obj.setInsertTime(new Date());
            obj.setIsDelete("0");
        }
        itCloudScreenMapper.insertResourceAllocate(obj);
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class, RuntimeException.class, Exception.class})
    public void insertBatchResourceAllocate(List<ScreenResourceAllocation> objList) {
        if(null != objList && !objList.isEmpty()) {
            for(ScreenResourceAllocation item : objList) {
                //item.setSystemTitleId(findIdByTitle(item.getSystemTitleId())); //将标题替换成ID
                item.setId(UUIDUtil.getUUID());
                item.setInsertTime(new Date());
                item.setIsDelete("0");
            }
            itCloudScreenMapper.insertBatchResourceAllocate(objList);
        }
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class, RuntimeException.class, Exception.class})
    public void insertBatchMaxUtilization(List<ScreenMaxUtilization> objList) {
        if(null != objList && !objList.isEmpty()) {
            for(ScreenMaxUtilization item : objList) {
                //item.setSystemTitleId(findIdByTitle(item.getSystemTitleId())); //将标题替换成ID
                item.setId(UUIDUtil.getUUID());
                item.setInsertTime(new Date());
                item.setIsDelete("0");
            }
            itCloudScreenMapper.insertBatchMaxUtilization(objList);
        }
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class, RuntimeException.class, Exception.class})
    public void insertBatchAvgUtilization(List<ScreenAvgUtilization> objList) {
        if(null != objList && !objList.isEmpty()) {
            for(ScreenAvgUtilization item : objList) {
                //item.setSystemTitleId(findIdByTitle(item.getSystemTitleId())); //将标题替换成ID
                item.setId(UUIDUtil.getUUID());
                item.setInsertTime(new Date());
                item.setIsDelete("0");
            }
            itCloudScreenMapper.insertBatchAvgUtilization(objList);
        }
    }

    @Override
    public List<Map<String, String>> getSystemTitleList() {
        return itCloudScreenMapper.getSystemTitleList();
    }

    @Override
    public Map<String, Object> getResourceAllocateList(ItCloudScreenRequest req) {
        List<Map<String, Object>> tmpList = itCloudScreenMapper.getResourceAllocateList(req);
        // 计算资源 对象列表
        List<Map<String, Object>> calList = new ArrayList<>();
        // 存储资源 对象列表
        List<Map<String, Object>> storeList = new LinkedList<>();
        for(Map<String, Object> item : tmpList) {
            String moduleType = item.get("moduleType").toString();
            if("计算资源".equals(moduleType)) {
                calList.add(item);
            } else {
                storeList.add(item);
            }
        }
        // 封装两个对象
        Map<String, Object> result = new HashMap<>();
        result.put("calculate",calList);
        result.put("store",storeList);
        return result;
    }

    @Override
    public List<Map<String, Object>> getMaxUtilizationByMonth(ItCloudScreenRequest req) {
        return itCloudScreenMapper.getMaxUtilizationByMonth(req);
    }

    @Override
    public List<Map<String, Object>> getMaxUtilizationByBizSystem(ItCloudScreenRequest req) {
        List<Map<String, Object>> result = itCloudScreenMapper.getMaxUtilizationByBizSystem(req);
        return judgeIfNeedReason(result);
    }

    @Override
    public List<Map<String, Object>> getAvgUtilizationByMonth(ItCloudScreenRequest req) {
        return itCloudScreenMapper.getAvgUtilizationByMonth(req);
    }

    @Override
    public List<Map<String, Object>> getAvgUtilizationByBizSystem(ItCloudScreenRequest req) {
        List<Map<String, Object>> result = itCloudScreenMapper.getAvgUtilizationByBizSystem(req);
        return judgeIfNeedReason(result);
    }

    @Override
    public Map<String, Object> getMonthMaxUtilization(ItCloudScreenRequest req) {
        // 获取当前月份的月份均峰值利用率
        Map<String, Object> curMax = itCloudScreenMapper.getMonthMaxUtilization(req);
        // 获取上个月的月份均峰值利用率
        String monthlyTime = req.getMonthlyTime();
        int curYear = Integer.parseInt(monthlyTime.split("-")[0]);
        int curMonth = Integer.parseInt(monthlyTime.split("-")[1]);
        // 一月份，上个月是去年的12月
        if(curMonth == 1) {
            req.setMonthlyTime(curYear + "-" + 12);
        } else {
            String suffix = (curMonth-1) > 9 ? ""+(curMonth-1) : "0"+(curMonth-1);
            req.setMonthlyTime(curYear + "-" + suffix);
        }
        Map<String, Object> preMax = itCloudScreenMapper.getMonthMaxUtilization(req);
        Map<String, Object> result = new HashMap<>();
        result.put("current",curMax);
        result.put("previous",preMax);
        return result;
    }

    @Override
    public Map<String, Object> getMonthAvgUtilization(ItCloudScreenRequest req) {
        // 获取当前月份的月份平均利用率
        Map<String, Object> curAvg = itCloudScreenMapper.getMonthAvgUtilization(req);
        // 获取上个月的月份平均利用率
        String monthlyTime = req.getMonthlyTime();
        int curYear = Integer.parseInt(monthlyTime.split("-")[0]);
        int curMonth = Integer.parseInt(monthlyTime.split("-")[1]);
        // 一月份，上个月是去年的12月
        if(curMonth == 1) {
            req.setMonthlyTime(curYear + "-" + 12);
        } else {
            String suffix = (curMonth-1) > 9 ? ""+(curMonth-1) : "0"+(curMonth-1);
            req.setMonthlyTime(curYear + "-" + suffix);
        }
        Map<String, Object> preAvg = itCloudScreenMapper.getMonthAvgUtilization(req);
        Map<String, Object> result = new HashMap<>();
        result.put("current",curAvg);
        result.put("previous",preAvg);
        return result;
    }

    @Override
    public Map<String,String> getBizSystemCount(ItCloudScreenRequest req) {
        Map<String,String> result = new HashMap<>();
        Integer i = itCloudScreenMapper.getBizSystemCount(req);
        result.put("count",i+"");
        return result;
    }

    @Override
    public Map<String, String> getPmBizSystemCount(ItCloudScreenRequest req) {
        req.setDeviceType("物理机");
        // 获取不区分资源池符合的数量
        Integer noRp = itCloudScreenMapper.getPmBizSystemCountNotIdcType(req);
        // 获取区分资源池符合的数量
        // Integer hasRp = itCloudScreenMapper.getPmBizSystemCountWithIdcType(req);
        Map<String, String> result = new HashMap<>();
        result.put("account",noRp.toString());
        result.put("listCount","0");
        return result;
    }

    @Override
    public List<Map<String, Object>> getBizSystemListWithIdcType(ItCloudScreenRequest req) {
        return itCloudScreenMapper.getBizSystemListWithIdcType(req);
    }

    @Override
    public Map<String, String> getVmBizSystemCount(ItCloudScreenRequest req) {
        req.setDeviceType("虚拟机");
        // 获取不区分资源池符合的数量
        Integer noRp = itCloudScreenMapper.getVmBizSystemCountNotIdcType(req);
        // 获取区分资源池符合的数量
        // Integer hasRp = itCloudScreenMapper.getVmBizSystemCountWithIdcType(req);
        Map<String, String> result = new HashMap<>();
        result.put("account",noRp.toString());
        result.put("listCount","0");
        return result;
    }

    @Override
    public Map<String, String> getStoreBizSystemCount(ItCloudScreenRequest req) {
        // 获取不区分资源池符合的数量
        Integer noRp = itCloudScreenMapper.getStoreBizSystemCountNotIdcType(req);
        // 获取区分资源池符合的数量
        Integer hasRp = itCloudScreenMapper.getStoreBizSystemCountWithIdcType(req);
        Map<String, String> result = new HashMap<>();
        result.put("account",noRp.toString());
        result.put("listCount",hasRp.toString());
        return result;
    }

    @Override
    public List<Map<String, Object>> getStoreBizSystemListWithIdcType(ItCloudScreenRequest req) {
        return itCloudScreenMapper.getStoreBizSystemListWithIdcType(req);
    }

    @Override
    public Map<String,Object> judgeStatus(ItCloudScreenRequest req) {
        Map<String, Object> result = new HashMap<>();
        // 判断租户是否分配资源
        Integer resourceCount = itCloudScreenMapper.getResourceAllocationCount(req);
        if(resourceCount != null && resourceCount > 0) {
            int count;
            if("max".equals(req.getTableType())) {
                count = itCloudScreenMapper.getMaxCpuAndStoreCount(req);
            } else {
                count = itCloudScreenMapper.getAvgCpuAndStoreCount(req);
            }
            if(count > 0) {
                result.put("flag",true);
                result.put("msg","Agent采集数据正常");
            } else {
                result.put("flag",false);
                result.put("msg","性能数据正在计算中");
            }
        } else {
            result.put("flag",false);
            result.put("msg","租户未分配相关资源");
        }
        return result;
    }

    /*
    *  判断列表中是否存在需要显示原因的行，有则添加原因
    * */
    private List<Map<String, Object>> judgeIfNeedReason(List<Map<String, Object>> list) {
        List<Map<String, Object>> result = new ArrayList<>();
        for(Map<String, Object> item : list) {
            Object cpu = null;
            Object store = null;
            if(item.containsKey("cpuMax")) {
                // 峰值利用率
                cpu = item.get("cpuMax");
                store = item.get("storeMax");
            } else {
                // 均值利用率
                cpu = item.get("cpuAvg");
                store = item.get("storeAvg");
            }
            // cpu和内存值都为空，表示数据未采集到
            if(cpu == null && store == null) {
                item.put("msg","性能数据正在计算中");
                result.add(item);
            }else if(cpu != null && store != null){
                item.put("msg","success");
                result.add(item);
            }else {
                // 只有CPU或者内存有值，确定为脏数据，去除,不加到结果集中
                continue;
            }
        }
        return result;
    }

    @Override
    public void deleteOldData(ItCloudScreenRequest req) {
        itCloudScreenMapper.deleteOldData(req);
    }

    @Override
    public void updateCpuMaxList(String monthlyTime) {
        // IT公司和非IT公司分开处理
        List<Map<String,Object>> data = new ArrayList<>();
        List<Map<String,Object>> itData = itCloudScreenMapper.getCpuMaxListWithIT(monthlyTime);
        List<Map<String,Object>> notItData = itCloudScreenMapper.getCpuMaxListWithNotIT(monthlyTime);
        if (itData != null) {
            data.addAll(itData);
        }
        if (notItData != null) {
            data.addAll(notItData);
        }
        List<ScreenCheckScore> scsList = new ArrayList<>();
        // 逐行比较
        for(Map<String,Object> item : data ) {
            String accessValue;
            String score;
            if(null == item.get("mxNumber")) {
                accessValue = "0";
                score = "0";
            } else {
                accessValue = item.get("mxNumber").toString();
                score = ScreenDataUtil.calCpuMaxScore(item.get("mxNumber").toString());
            }

            ScreenCheckScore scs = new ScreenCheckScore();
            // 标题暂时写死
            scs.setSystemTitleId("1");
            scs.setDepartment1(item.get("department1").toString());
            scs.setDepartment2(item.get("department2") == null ? null : item.get("department2").toString());

			// hangfang 2020.07.30 null引用
//            scs.setBizSystem(null);
            // 以30%为标准值
            scs.setStandardValue("30");
            scs.setAssessedValue(accessValue);
            scs.setScore(score);
            scs.setScoreType("cpuMax");
            scs.setDescription("租户资源CPU利用率均峰值");
            scs.setMonthlyTime(monthlyTime);
            // 加入列表中
            scsList.add(scs);
        }
        // 删除源数据
        ItCloudScreenRequest checkScoreReq = new ItCloudScreenRequest();
        checkScoreReq.setMonthlyTime(monthlyTime);
        checkScoreReq.setScoreType("cpuMax");
        checkScoreService.delete(checkScoreReq);
        // 先删除，在做新增
        checkScoreService.batchInsert(scsList);
    }

    @Override
    public LinkedHashMap<String, Object> getSpecificDeviceByBz(ItCloudScreenRequest req) {
        LinkedHashMap<String, Object> returnJson = new LinkedHashMap<>();
        LinkedList<Map<String, Object>> rs = new LinkedList<>();
        if(null != req.getPageNo() && null != req.getPageSize()) {
            req.setPageNo((req.getPageNo() - 1)*req.getPageSize());
        }
        // 从列表拿要显示的资源池
        CmdbConfig cmdbConfig = new CmdbConfig();
        cmdbConfig.setConfigCode("business_display_");
        List<CmdbConfig> cmdbConfigs = cmdbConfigMapper.listByEntity(cmdbConfig);
        List<String> idcLists = cmdbConfigs.stream().map(item -> item.getConfigValue()).collect(Collectors.toList());
        // 获取CI的基础信息(CI的ID、管理IP、业务IP、device_name、资源池)
        List<LinkedHashMap<String, Object>> baseInfo = itCloudScreenMapper.getSpecificDeviceByBz(req.getBizSystem(),req.getDeviceType(),idcLists,req.getPageNo(),req.getPageSize());
        int totalCnt = itCloudScreenMapper.getSpecificDeviceCountByBz(req.getBizSystem(),req.getDeviceType(),idcLists);
        if(baseInfo.isEmpty()) {
            returnJson.put("total",totalCnt);
            returnJson.put("data",baseInfo);
            return returnJson;
        }
        // 从中间表中拉取具体的性能数据(CI的ID、具体的性能数据)
        ItCloudScreenBizSystemDeviceDetailRequest bsddReq = new ItCloudScreenBizSystemDeviceDetailRequest();
        bsddReq.setMonthlyTime(req.getMonthlyTime());
        bsddReq.setBizSystem(req.getBizSystem());
        bsddReq.setDeviceType(req.getDeviceType());
        List<ScreenBizSystemDeviceDetail> bsddList = deviceDetailService.list(bsddReq);
        // 将性能指标数据封装，以InstanceId为key值
        Map<String,Object> indexDataMp = new HashMap<>();
        for(ScreenBizSystemDeviceDetail obj : bsddList) {
            String key = obj.getInstanceId();
            // 根据具体的参数来获取均峰值还是均值
            String cpu = "MAX".equals(req.getTableType()) ? obj.getCpuMax() : obj.getCpuAvg();
            String memory = "MAX".equals(req.getTableType()) ? obj.getMemoryMax() : obj.getMemoryAvg();
            // 每天一个单元实体
            Map<String,Object> tmp = new HashMap<>();
            tmp.put("dateDisplay",obj.getDateDisplay());
            tmp.put("memory",memory);
            tmp.put("cpu",cpu);
            // 响应实体的一个节点
            Map<String,Object> srcTmp = new HashMap<>();
            srcTmp.put(obj.getStatistDate(),tmp);
            if(indexDataMp.containsKey(key)) {
                // 将每天的数据，汇集到一起
                Map<String,Object> crtTmp = (Map<String,Object>)indexDataMp.get(key);
                crtTmp.putAll(srcTmp);
            } else {
                indexDataMp.put(key,srcTmp);
            }
        }
        // 某些设备是不存在性能数据的，要用‘-’显示
        List<LinkedHashMap<String, Object>> notMatch = new ArrayList<>();
        String crtMonthlyTime = req.getMonthlyTime();
        int totalDay = SuppleUtil.getDayByMonthAndYear(crtMonthlyTime);
        // 遍历基础CI中的信息，与中间表保存的指标数据做合并
        for(LinkedHashMap<String, Object> item : baseInfo) {
            String key = item.get("resourceId").toString();
            // 对照的中间表中存在指标数据
            if(indexDataMp.containsKey(key)) {
                LinkedHashMap<String, Object> tmpSource = JSONObject.parseObject(JSONObject.toJSONString(indexDataMp.get(key)),LinkedHashMap.class);
                LinkedHashMap<String, Object> tmp = new LinkedHashMap<>();
                // 验证是否这个月中每天都有数据，缺少的用“-”补充
                for(int i=1;i<=totalDay;i++) {
                    String unitKey = i < 10 ? crtMonthlyTime + "-" + "0" + i : crtMonthlyTime + "-" + i;
                    if(!tmpSource.containsKey(unitKey)) {
                        Map<String, Object> unitMp = new HashMap<>();
                        unitMp.put("dateDisplay",i + "号");
                        unitMp.put("cpu","-");
                        unitMp.put("memory","-");
                        tmp.put(unitKey,unitMp);
                    } else {
                        tmp.put(unitKey, tmpSource.get(unitKey));
                    }
                }
                tmp.put("bizSystem",req.getBizSystem());
                tmp.put("deviceName",item.get("deviceName"));
                tmp.put("idcType",item.get("idcType"));
                tmp.put("serviceIp",item.get("serviceIp"));
                tmp.put("ip",item.get("ip"));
                tmp.put("resourceId",item.get("resourceId"));
                rs.add(tmp);
            } else {
                item.put("bizSystem",req.getBizSystem());
                notMatch.add(item);
            }
        }
        for (LinkedHashMap<String, Object> item : notMatch) {
            for(int i=1;i<=totalDay;i++) {
                LinkedHashMap<String, Object> unitMp = new LinkedHashMap<>();
                unitMp.put("dateDisplay",i + "号");
                unitMp.put("cpu","-");
                unitMp.put("memory","-");
                String unitKey = i < 10 ? crtMonthlyTime + "-" + "0" + i : crtMonthlyTime + "-" + i;
                item.put(unitKey,unitMp);
            }
            rs.add(item);
        }
        returnJson.put("total",totalCnt);
        returnJson.put("data",rs);
        return returnJson;
    }
}

package com.aspire.ums.cmdb.v2.index.serivce.impl;

import com.aspire.ums.cmdb.index.payload.ItCloudScreenOverviewRequest;
import com.aspire.ums.cmdb.v2.index.mapper.ItCloudScreenCheckScoreMapper;
import com.aspire.ums.cmdb.v2.index.mapper.ItCloudScreenOverviewMapper;
import com.aspire.ums.cmdb.v2.index.serivce.ItCloudScreenOverviewService;
import com.aspire.ums.cmdb.v2.index.util.MathCalculateUtil;
import com.aspire.ums.cmdb.v2.index.util.ScreenDataUtil;
import com.aspire.ums.cmdb.v2.index.util.SuppleUtil;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @ClassName ItCloudScreenOverviewServiceImpl
 * @Description 服务层实现类
 * @Author luowenbo
 * @Date 2020/4/15 10:03
 * @Version 1.0
 */
@Service
public class ItCloudScreenOverviewServiceImpl implements ItCloudScreenOverviewService {

    @Autowired
    private ItCloudScreenOverviewMapper mapper;
    @Autowired
    private ItCloudScreenCheckScoreMapper checkScoreMapper;

    @Override
    public Map<String,Object> getDeviceTypeList(String moduleType,String exclude) {
        List<String> all = mapper.getDeviceTypeList(moduleType);
        if(exclude != null) {
            List<String> part = Arrays.asList(exclude.split(","));
            all.removeAll(part);
        }
        Map<String,Object> rs = new HashMap<>();
        // 循环封装设备类型
        List<Map<String,Object>> dataList = new ArrayList<>();
        for(String item : all){
            Map<String,Object> dataMp = new HashMap<>();
            String name;
            switch (item){
                case "物理机": name = "裸金属";break;
                case "虚拟机": name = "云主机";break;
                default: name = item;
            }
            dataMp.put("code",item);
            dataMp.put("name",name);
            dataList.add(dataMp);
        }
        if (null !=all && !all.isEmpty()) {
            rs.put("flag",true);
            rs.put("data",dataList);
        } else {
            rs.put("flag",false);
            rs.put("errorMsg",null);
        }
        return rs;
    }

    @Override
    public Map<String,Object> getTenantAndBzNumByDep(ItCloudScreenOverviewRequest req) {
        Map<String, Object> data = mapper.getTenantAndBzNumByDep(req);
        if(null != data && !data.isEmpty()) {
            String key = "内部租户".equals(req.getDepType()) ? "dep2Count" : "dep1Count";
            Map<String, Object> rs = new HashMap<>();
            rs.put("depType",req.getDepType());
            rs.put("depCount",data.get(key));
            rs.put("bzCount",data.get("bzCount"));
            return ScreenDataUtil.transferDataByMap("CHART",rs);
        }
        return ScreenDataUtil.transferDataByMap("CHART",data);
    }

    @Override
    public Map<String, Object> getTenantAndBzNumByRp(ItCloudScreenOverviewRequest req) {
        List<Map<String, Object>> data = mapper.getTenantAndBzNumByRp(req);
        if (null != data && !data.isEmpty()) {
            String key = "内部租户".equals(req.getDepType()) ? "dep2Count" : "dep1Count";
            List<Map<String, Object>> rs = new ArrayList<>();
            for(Map<String, Object> item: data) {
                Map<String, Object> tmp = new HashMap<>();
                tmp.put("resourcePool",item.get("resourcePool"));
                tmp.put("bzCount",item.get("bzCount"));
                tmp.put("depCount",item.get(key));
                rs.add(tmp);
            }
            return ScreenDataUtil.transferDataByList("CHART",rs);
        }
        return ScreenDataUtil.transferDataByList("CHART",data);
    }

    @Override
    public Map<String, Object> getHasAndUseAltNumByDt(ItCloudScreenOverviewRequest req) {
        List<Map<String, Object>> data = mapper.getHasAndUseAltNumByDt(req);
        if (null != data && !data.isEmpty()) {
            String moduleType = req.getModuleType();
            Map<String, Object> rs = new HashMap<>();
            BigDecimal totalHasAlt = new BigDecimal("0");
            BigDecimal totalUseAlt = new BigDecimal("0");
            for(Map<String, Object> item : data){
                // 整数和浮点数都有可能
                totalHasAlt = MathCalculateUtil.bigDecimalAdd(totalHasAlt,item.get("hasAlt").toString());
                totalUseAlt = MathCalculateUtil.bigDecimalAdd(totalUseAlt,item.get("useAlt").toString());
                // 存储资源特殊处理，小数点后保留两位，不够则补零
                if("存储资源".equals(moduleType)){
                    item.put("useAlt",String.format("%.2f",new BigDecimal(item.get("useAlt").toString()).doubleValue()));
                    item.put("hasAlt",String.format("%.2f",new BigDecimal(item.get("hasAlt").toString()).doubleValue()));
                }
                if("内部租户".equals(item.get("depType"))) {
                    rs.put("inside",item);
                } else {
                    rs.put("outside",item);
                }
            }
            Map<String, Object> tmp = new HashMap<>();
            tmp.put("depType","总和");
            if("计算资源".equals(moduleType)) {
                tmp.put("hasAlt",totalHasAlt.intValue());
                tmp.put("useAlt",totalUseAlt.intValue());
            } else {
                tmp.put("hasAlt",String.format("%.2f",totalHasAlt.doubleValue()));
                tmp.put("useAlt",String.format("%.2f",totalUseAlt.doubleValue()));
            }
            rs.put("total",tmp);
            return ScreenDataUtil.transferDataByMap("CHART",rs);
        }
        return ScreenDataUtil.transferDataByList("CHART",data);
    }

    @Override
    public Map<String, Object> getHasAndUseAltNumByDevtAndDept(ItCloudScreenOverviewRequest req) {
        List<Map<String, Object>> data = mapper.getHasAndUseAltNumByDevtAndDept(req);
        String moduleType = req.getModuleType();
        // 存储资源特殊处理，保留两位小数，不够位补零
        if("存储资源".equals(moduleType)) {
            for(Map<String, Object> item : data) {
                item.put("useAlt",String.format("%.2f",new BigDecimal(item.get("useAlt").toString()).doubleValue()));
                item.put("hasAlt",String.format("%.2f",new BigDecimal(item.get("hasAlt").toString()).doubleValue()));
            }
        }
        return ScreenDataUtil.transferDataByList("TABLE",data);
    }

    @Override
    public Map<String,Object> getAgentNum(ItCloudScreenOverviewRequest req) {
        List<Map<String, Object>> data = mapper.getAgentNum(req);
        if(null != data && !data.isEmpty()) {
            Map<String,Object> rs = new HashMap<>();
            // 初始化，部署总量和部署点为0
            BigDecimal pmTotal = new BigDecimal("0");
            BigDecimal pmPart = new BigDecimal("0");
            BigDecimal vmTotal = new BigDecimal("0");
            BigDecimal vmPart = new BigDecimal("0");
            // 求和
            for(Map<String, Object> item : data) {
                pmTotal = MathCalculateUtil.bigDecimalAdd(pmTotal,item.get("pmTotalCount").toString());
                pmPart = MathCalculateUtil.bigDecimalAdd(pmPart,item.get("pmCount").toString());
                vmTotal = MathCalculateUtil.bigDecimalAdd(vmTotal,item.get("vmTotalCount").toString());
                vmPart = MathCalculateUtil.bigDecimalAdd(vmPart,item.get("vmCount").toString());
                String depTypeKey = "内部租户".equals(item.get("depType").toString())?"inside":"outside";
                rs.put(depTypeKey,item);
            }
            Map<String,Object> tmp = new HashMap<>();
            tmp.put("depType","总和");
            tmp.put("pmTotalCount",pmTotal);
            tmp.put("pmCount",pmPart);
            tmp.put("vmTotalCount",vmTotal);
            tmp.put("vmCount",vmPart);
            rs.put("total",tmp);
            return ScreenDataUtil.transferDataByMap("CHART",rs);
        }
        return ScreenDataUtil.transferDataByList("CHART",data);
    }

    @Override
    public Map<String, Object> getAgentNumWithDept(ItCloudScreenOverviewRequest req) {
        List<Map<String, Object>> data = mapper.getAgentNumWithDept(req);
        return ScreenDataUtil.transferDataByList("TABLE",data);
    }

    @Override
    public Map<String, Object> getStoreUtzByDt(ItCloudScreenOverviewRequest req) {
        Map<String, Object> rs = new HashMap<>();
        // 参数的当前月数据
        List<Map<String, Object>> crtData = mapper.getStoreUtzByDt(req);
        if (null != crtData && !crtData.isEmpty()) {
            Map<String, Object> rsp = ScreenDataUtil.transferStoreData(crtData);
            rs.put("crtMonth",rsp);
            // 获取上个月的月份数据
            String monthlyTime = req.getMonthlyTime();
            req.setMonthlyTime(SuppleUtil.getPreMonth(monthlyTime));
            List<Map<String, Object>> preData = mapper.getStoreUtzByDt(req);
            rsp = ScreenDataUtil.transferStoreData(preData);
            rs.put("preMonth",rsp);
            return ScreenDataUtil.transferDataByMap("CHART",rs);
        }
        return ScreenDataUtil.transferDataByList("CHART",crtData);
    }

    @Override
    public Map<String, Object> getStoreUtzByDtAndDept(ItCloudScreenOverviewRequest req) {
        List<Map<String, Object>> crtData = mapper.getStoreUtzByDtAndDept(req);
        if (null != crtData && !crtData.isEmpty()) {
            // 获取上个月的月份数据
            String monthlyTime = req.getMonthlyTime();
            req.setMonthlyTime(SuppleUtil.getPreMonth(monthlyTime));
            List<Map<String, Object>> preData = mapper.getStoreUtzByDtAndDept(req);
            // 将list封装成map
            Map<String,Object> preMp = new HashMap<>();
            for(Map<String, Object> item : preData) {
                preMp.put(item.get("department").toString(),item);
            }
            for(Map<String, Object> item : crtData) {
                String department = item.get("department").toString();
                if(preMp.containsKey(department)) {
                    Map<String, Object> preItem = (Map<String,Object>)preMp.get(department);
                    item.put("preHasAlt",preItem.get("hasAlt"));
                    item.put("preUseAlt",preItem.get("useAlt"));
                    item.put("preAlt",preItem.get("alt"));
                } else {
                    item.put("preHasAlt",0);
                    item.put("preUseAlt",0);
                    item.put("preAlt",0);
                }
            }
            Collections.sort(crtData, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    BigDecimal a1 = new BigDecimal(o1.get("alt").toString());
                    BigDecimal a2 = new BigDecimal(o2.get("alt").toString());
                    return a2.compareTo(a1);
                }
            });
            return ScreenDataUtil.transferDataByList("TABLE",crtData);
        }
        return ScreenDataUtil.transferDataByList("TABLE",crtData);
    }

    @Override
    public Map<String, Object> getRecycleNum(ItCloudScreenOverviewRequest req) {
        // 获取物理机和GPU的资源回收数量
        List<Map<String, Object>> data = mapper.getRecycleNumWithPmAndGpu(req);
        if (null != data && !data.isEmpty()) {
            Map<String, Object> rs = new HashMap<>();
            // 初始化
            BigDecimal pmReCntTotal = new BigDecimal("0");
            BigDecimal gpuReCntTotal = new BigDecimal("0");
            BigDecimal vmCpuReTotal = new BigDecimal("0");
            BigDecimal vmMsReTotal = new BigDecimal("0");
            // 求和
            for(Map<String, Object> item : data) {
                pmReCntTotal = MathCalculateUtil.bigDecimalAdd(pmReCntTotal,item.get("pmReCnt").toString());
                gpuReCntTotal = MathCalculateUtil.bigDecimalAdd(gpuReCntTotal,item.get("gpuReCnt").toString());
                String depTypeKey = "内部租户".equals(item.get("depType").toString())?"inside":"outside";
                rs.put(depTypeKey,item);
            }
            // 获取虚拟机CPU核数和内存的资源回收数量
            List<Map<String, Object>> vmData = mapper.getRecycleNumWithVm(req);
            for(Map<String, Object> item : vmData) {
                vmCpuReTotal = MathCalculateUtil.bigDecimalAdd(vmCpuReTotal,item.get("cpuNum").toString());
                vmMsReTotal = MathCalculateUtil.bigDecimalAdd(vmMsReTotal,item.get("memorySize").toString());
                String depTypeKey = "内部租户".equals(item.get("depType").toString())?"inside":"outside";
                // 覆盖租户下的数据
                Map<String, Object> tmp = (Map<String, Object>)rs.get(depTypeKey);
                tmp.put("vmReCnt",item.get("cpuNum"));
                rs.put(depTypeKey,tmp);
            }
            Map<String, Object> totalMp = new HashMap<>();
            totalMp.put("depType","总和");
            totalMp.put("vmReCnt",vmCpuReTotal);
            totalMp.put("vmMsCnt",vmMsReTotal);
            totalMp.put("pmReCnt",pmReCntTotal);
            totalMp.put("gpuReCnt",gpuReCntTotal);
            rs.put("total",totalMp);
            return ScreenDataUtil.transferDataByMap("CHART",rs);
        }
        return ScreenDataUtil.transferDataByList("CHART",data);
    }

    @Override
    public Map<String, Object> getRecycleNumWithDevt(ItCloudScreenOverviewRequest req) {
        if("虚拟机".equals(req.getDeviceType())) {
            return ScreenDataUtil.transferDataByList("TABLE",mapper.getVmRecycleNumWithDevt(req));
        } else {
            return ScreenDataUtil.transferDataByList("TABLE",mapper.getPmAndGpuRecycleNumWithDevt(req));
        }
    }

    @Override
    public Map<String,Object> getBizSystemTotalCount(ItCloudScreenOverviewRequest req) {
        List<Map<String, Object>> data = mapper.getBizSystemTotalCount(req);
        if (null != data && !data.isEmpty()) {
            Map<String,Object> rs = new HashMap<>();
            BigDecimal total = new BigDecimal("0");
            for(Map<String, Object> item : data ){
                total = MathCalculateUtil.bigDecimalAdd(total,item.get("cnt").toString());
                String depTypeKey = "内部租户".equals(item.get("depType").toString())?"inside":"outside";
                rs.put(depTypeKey,item.get("cnt"));
            }
            rs.put("total",total);
            return ScreenDataUtil.transferDataByMap("CHART",rs);
        }
        return ScreenDataUtil.transferDataByList("OTHER",data);
    }

    @Override
    public Map<String, Object> getLowUtlCount(ItCloudScreenOverviewRequest req) {
        List<Map<String, Object>> data = mapper.getLowUtlCount(req);
        if (null != data && !data.isEmpty()) {
            Map<String,Object> rs = new HashMap<>();
            BigDecimal total = new BigDecimal("0");
            for(Map<String, Object> item : data ){
                total = MathCalculateUtil.bigDecimalAdd(total,item.get("bzCnt").toString());
                String depTypeKey = "内部租户".equals(item.get("depType").toString())?"inside":"outside";
                rs.put(depTypeKey,item.get("bzCnt"));
            }
            rs.put("total",total);
            return ScreenDataUtil.transferDataByMap("CHART",rs);
        }
        return ScreenDataUtil.transferDataByList("CHART",data);
    }

    @Override
    public Map<String, Object> getLowUltListWithDept(ItCloudScreenOverviewRequest req) {
        List<Map<String,Object>> data = mapper.getLowUltListWithDept(req);
        for(Map<String,Object> item : data) {
            item.put("percent",String.format("%.2f",new BigDecimal(item.get("percent").toString()).doubleValue()));
        }
        return ScreenDataUtil.transferDataByList("TABLE",data);
    }

    @Override
    public Map<String, Object> getCpuAndStoreMaxWithDept(ItCloudScreenOverviewRequest req) {
        List<Map<String, Object>> crtData = mapper.getCpuAndStoreMaxWithDept(req);
        if (null != crtData && !crtData.isEmpty()) {
            // 获取上个月的月份数据
            String monthlyTime = req.getMonthlyTime();
            req.setMonthlyTime(SuppleUtil.getPreMonth(monthlyTime));
            List<Map<String, Object>> preData = mapper.getCpuAndStoreMaxWithDept(req);
            // 将list封装成map
            Map<String,Object> preMp = new HashMap<>();
            for(Map<String, Object> item : preData) {
                preMp.put(item.get("department").toString(),item);
            }
            for(Map<String, Object> item : crtData) {
                String department = item.get("department").toString();
                if(preMp.containsKey(department)) {
                    Map<String, Object> preItem = (Map<String,Object>)preMp.get(department);
                    item.put("preCpu",preItem.get("cpu"));
                    item.put("preStore",preItem.get("store"));
                } else {
                    item.put("preCpu",0);
                    item.put("preStore",0);
                }
            }
            return ScreenDataUtil.transferDataByList("TABLE",crtData);
        }
        return ScreenDataUtil.transferDataByList("TABLE",crtData);
    }

    @Override
    public Map<String, Object> getCpuAndStoreMax(ItCloudScreenOverviewRequest req) {
        Map<String, Object> rs = new HashMap<>();
        String MonthlyTime = req.getMonthlyTime();
        // 内部租户
        req.setDepType("内部租户");
        Map<String, Object> inCrtData = mapper.getCpuAndStoreMax(req);
        // 特殊处理，求的是最大值，mybatis返回的对象不可能为NULL
        if (null != inCrtData && !inCrtData.isEmpty() && null == inCrtData.get("cpu") && null == inCrtData.get("store")) {
            rs.put("flag",false);
            rs.put("errorMsg","性能数据正在计算中");
            return rs;
        }
        if (null != inCrtData && !inCrtData.isEmpty()) {
            req.setMonthlyTime(SuppleUtil.getPreMonth(MonthlyTime));
            Map<String, Object> inPreData = mapper.getCpuAndStoreMax(req);
            Map<String, Object> in = ScreenDataUtil.transferCpuAndStoreData(inCrtData,inPreData);
            rs.put("inside",in);
            // 外部租户
            req.setDepType("外部租户");
            req.setMonthlyTime(MonthlyTime);
            Map<String, Object> outCrtData = mapper.getCpuAndStoreMax(req);
            req.setMonthlyTime(SuppleUtil.getPreMonth(MonthlyTime));
            Map<String, Object> outPreData = mapper.getCpuAndStoreMax(req);
            Map<String, Object> out = ScreenDataUtil.transferCpuAndStoreData(outCrtData,outPreData);
            rs.put("outside",out);
            // 所有租户
			// hangfang 2020.07.30 null引用
            req.setDepType("");
            req.setMonthlyTime(MonthlyTime);
            Map<String, Object> allCrtData = mapper.getCpuAndStoreMax(req);
            req.setMonthlyTime(SuppleUtil.getPreMonth(MonthlyTime));
            Map<String, Object> allPreData = mapper.getCpuAndStoreMax(req);
            Map<String, Object> all = ScreenDataUtil.transferCpuAndStoreData(allCrtData,allPreData);
            rs.put("total",all);
            return ScreenDataUtil.transferDataByMap("CHART",rs);
        }
        return ScreenDataUtil.transferDataByMap("CHART",inCrtData);
    }

    @Override
    public Map<String,Object> getCpuAndStoreAvgWithDept(ItCloudScreenOverviewRequest req) {
        List<Map<String, Object>> crtData = mapper.getCpuAndStoreAvgWithDept(req);
        if (null != crtData && !crtData.isEmpty()) {
            // 获取上个月的月份数据
            String monthlyTime = req.getMonthlyTime();
            req.setMonthlyTime(SuppleUtil.getPreMonth(monthlyTime));
            List<Map<String, Object>> preData = mapper.getCpuAndStoreAvgWithDept(req);
            // 将list封装成map
            Map<String,Object> preMp = new HashMap<>();
            for(Map<String, Object> item : preData) {
                preMp.put(item.get("department").toString(),item);
            }
            for(Map<String, Object> item : crtData) {
                String department = item.get("department").toString();
                if(preMp.containsKey(department)) {
                    Map<String, Object> preItem = (Map<String,Object>)preMp.get(department);
                    item.put("preCpu",preItem.get("cpu"));
                    item.put("preStore",preItem.get("store"));
                } else {
                    item.put("preCpu",0);
                    item.put("preStore",0);
                }
            }
            return ScreenDataUtil.transferDataByList("TABLE",crtData);
        }
        return ScreenDataUtil.transferDataByList("TABLE",crtData);
    }

    @Override
    public Map<String, Object> getCpuAndStoreAvg(ItCloudScreenOverviewRequest req) {
        Map<String, Object> rs = new HashMap<>();
        String MonthlyTime = req.getMonthlyTime();
        // 内部租户
        req.setDepType("内部租户");
        Map<String, Object> inCrtData = mapper.getCpuAndStoreAvg(req);
        // 特殊处理，求的是最大值，mybatis返回的对象不可能为NULL
        if (null != inCrtData && !inCrtData.isEmpty() && null == inCrtData.get("cpu") && null == inCrtData.get("store")) {
            rs.put("flag",false);
            rs.put("errorMsg","性能数据正在计算中");
            return rs;
        }
        if (null != inCrtData && !inCrtData.isEmpty()) {
            req.setMonthlyTime(SuppleUtil.getPreMonth(MonthlyTime));
            Map<String, Object> inPreData = mapper.getCpuAndStoreAvg(req);
            Map<String, Object> in = ScreenDataUtil.transferCpuAndStoreData(inCrtData,inPreData);
            rs.put("inside",in);
            // 内部租户
            req.setDepType("外部租户");
            req.setMonthlyTime(MonthlyTime);
            Map<String, Object> outCrtData = mapper.getCpuAndStoreAvg(req);
            req.setMonthlyTime(SuppleUtil.getPreMonth(MonthlyTime));
            Map<String, Object> outPreData = mapper.getCpuAndStoreAvg(req);
            Map<String, Object> out = ScreenDataUtil.transferCpuAndStoreData(outCrtData,outPreData);
            rs.put("outside",out);
            // 所有租户
            req.setDepType("");
            req.setMonthlyTime(MonthlyTime);
            Map<String, Object> allCrtData = mapper.getCpuAndStoreAvg(req);
            req.setMonthlyTime(SuppleUtil.getPreMonth(MonthlyTime));
            Map<String, Object> allPreData = mapper.getCpuAndStoreAvg(req);
            Map<String, Object> all = ScreenDataUtil.transferCpuAndStoreData(allCrtData,allPreData);
            rs.put("total",all);
            return ScreenDataUtil.transferDataByMap("CHART",rs);
        }
        return ScreenDataUtil.transferDataByMap("CHART",inCrtData);
    }

    @Override
    public Map<String, Object> getScoreWithDept(ItCloudScreenOverviewRequest req) {
        List<Map<String, Object>> data = mapper.getScoreWithDept(req);
        if (null != data && !data.isEmpty()) {
            Map<String, Object> depMp = new HashMap<>();
            String total = null;
            for (Map<String, Object> item : data ) {
                if(depMp.containsKey(item.get("department").toString())) {
                    String scoreType = item.get("scoreType") == null ? null : item.get("scoreType").toString();
                    if(null == scoreType) {
                        continue;
                    }
                    Map<String, String> crtMp = (Map<String, String>)depMp.get(item.get("department"));
                    if("cpuMax".equals(item.get("scoreType").toString())) {
                        BigDecimal av = new BigDecimal(item.get("assessValue").toString());
                        crtMp.put("cpuMax",String.format("%.2f",av.doubleValue()));
                    } else {
                        String type = item.get("scoreType").toString();
                        String partScore = ScreenDataUtil.scoreAdd(crtMp.get(type),item.get("score").toString());
                        crtMp.put(type,partScore);
                        total = ScreenDataUtil.scoreAdd(crtMp.get("total"),partScore,"","ALL");
                        crtMp.put("total",total);
                    }
                    depMp.put(item.get("department").toString(),crtMp);
                } else {
                    Map<String, String> srcMp = new HashMap<>();
                    srcMp.put("total","0");srcMp.put("inform","0");srcMp.put("deploy","0");srcMp.put("cpuMax","0");
                    srcMp.put("department",item.get("department").toString());
                    // 初始化，如果没有扣分项的默认为0
                    String scoreType = item.get("scoreType") == null ? null : item.get("scoreType").toString();
                    if(null == scoreType) {
                        depMp.put(item.get("department").toString(),srcMp);
                        continue;
                    }
                    if("cpuMax".equals(scoreType)) {
                        BigDecimal av = new BigDecimal(item.get("assessValue").toString());
                        srcMp.put("cpuMax",String.format("%.2f",av.doubleValue()));
                    } else {
                        String partScore = ScreenDataUtil.scoreAdd("0",item.get("score").toString());
                        srcMp.put(item.get("scoreType").toString(),partScore);
                        total = ScreenDataUtil.scoreAdd("0",partScore,"","ALL");
                        srcMp.put("total",total);
                    }
                    depMp.put(item.get("department").toString(),srcMp);
                }
            }
            List<Map<String, Object>> dataList = new ArrayList<>();
            for(Object value : depMp.values()) {
                Map<String, Object> v = (Map<String, Object>)value;
                dataList.add(v);
            }
            // 排序按照cpu均峰值降序排序
            Collections.sort(dataList, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    BigDecimal av1 = new BigDecimal(o1.get("cpuMax").toString());
                    BigDecimal av2 = new BigDecimal(o2.get("cpuMax").toString());
                    return av2.compareTo(av1);
                }
            });
            return ScreenDataUtil.transferDataByList("TABLE",dataList);
        }
        return ScreenDataUtil.transferDataByList("TABLE",data);
    }

    @Override
    public Map<String, Object> getTotalMonthCheckScoreList(ItCloudScreenOverviewRequest req) {
        String monthTime = req.getMonthlyTime();
        String[] array = ScreenDataUtil.calMonthArray(monthTime);
        req.setMonthArray(array);
        List<Map<String, Object>> data = mapper.getTotalMonthCheckScoreList(req);
        // 分割年、月份
        int year = Integer.parseInt(monthTime.split("-")[0]);
        int month = Integer.parseInt(monthTime.split("-")[1]);
        String department1 = null;
        if("内部租户".equals(req.getDepType())) {
            department1 = "信息技术中心";
        }
        // 不同部门有不同的考核开始时间
        if (null != data && !data.isEmpty()) {
            Map<String, Object> depMp = new HashMap<>();
            String total = "0";
            String max = "0";
            String assessST = "";  // 考核开始时间
            String assessMonth = "";
            for (Map<String, Object> item : data ) {
                if(depMp.containsKey(item.get("department").toString())) {
                    String scoreType = item.get("scoreType") == null ? null : item.get("scoreType").toString();
                    if(null == scoreType) {
                        continue;
                    }
                    Map<String, String> crtMp = (Map<String, String>)depMp.get(item.get("department"));
                    if("cpuMax".equals(item.get("scoreType").toString())) {
                        String monthPattern = item.get("monthlyTime").toString();
                        max = monthPattern.compareTo(assessST) >= 0 ? ScreenDataUtil.scoreAdd(max,item.get("assessValue").toString()) : "0";
                        // 求均值时的月份的数量
                        int size = month <= Integer.parseInt(assessMonth) ? 0 : month - Integer.parseInt(assessMonth) + 1;
                        crtMp.put("cpuMax",ScreenDataUtil.dev(size+"",max));
                        total = "0";
                    } else {
                        // inform、deploy类型
                        String type = item.get("scoreType").toString();
                        String partScore = ScreenDataUtil.scoreAdd(crtMp.get(type),item.get("score").toString());
                        crtMp.put(type,partScore);
                        total = ScreenDataUtil.scoreAdd(total,partScore);
                        // inform是最后一个指标，进行总结
                        if("inform".equals(type)) {
                            // 累计的总扣分上限为3
                            crtMp.put("total",ScreenDataUtil.scoreAdd(crtMp.get("total"),ScreenDataUtil.scoreAdd("0",total,"","ALL")));
                        }
                    }
                    depMp.put(item.get("department").toString(),crtMp);
                } else {
                    Map<String,Object> assessTime = checkScoreMapper.getAssessStartMonth(department1,item.get("department").toString());
                    assessMonth = (assessTime == null || assessTime.get("assessMonth") == null) ? "1" : assessTime.get("assessMonth").toString();
                    assessST = (Integer.parseInt(assessMonth) < 10 ? year + "-0" + Integer.parseInt(assessMonth) : year + "-" + Integer.parseInt(assessMonth));
                    Map<String, String> srcMp = new HashMap<>();
                    srcMp.put("total","0");srcMp.put("inform","0");srcMp.put("deploy","0");srcMp.put("cpuMax","0");
                    srcMp.put("department",item.get("department").toString());
                    max = "0";
                    // 初始化，如果没有扣分项的默认为0
                    String scoreType = item.get("scoreType") == null ? null : item.get("scoreType").toString();
                    if(null == scoreType) {
                        depMp.put(item.get("department").toString(),srcMp);
                        continue;
                    }
                    if("cpuMax".equals(item.get("scoreType").toString())) {
                        String monthPattern = item.get("monthlyTime").toString();
                        max = monthPattern.compareTo(assessST) >= 0 ? ScreenDataUtil.scoreAdd(max,item.get("assessValue").toString()) : "0";
                        // 从考核开始时间进行计算均值
                        int size = month <= Integer.parseInt(assessMonth) ? 0 : month - Integer.parseInt(assessMonth) + 1;
                        srcMp.put("cpuMax",ScreenDataUtil.dev(size+"",max));
                        total = "0";
                    }
                    depMp.put(item.get("department").toString(),srcMp);
                }
            }
            // 求总累计和
            List<Map<String, Object>> dataList = new ArrayList<>();
            for(Object value : depMp.values()) {
                Map<String, Object> v = (Map<String, Object>)value;
                String finalScore = "0";
                if(month == 12) {
                    finalScore = ScreenDataUtil.calCpuMaxScore(v.get("cpuMax").toString());
                }
                v.put("total",ScreenDataUtil.scoreAdd(v.get("total").toString(),finalScore));
                v.put("finalScore",finalScore);
                dataList.add(v);
            }
            // 排序按照cpu均峰值降序排序
            Collections.sort(dataList, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    BigDecimal av1 = new BigDecimal(o1.get("cpuMax").toString());
                    BigDecimal av2 = new BigDecimal(o2.get("cpuMax").toString());
                    return av2.compareTo(av1);
                }
            });
            return ScreenDataUtil.transferDataByList("TABLE",dataList);
        }
        return ScreenDataUtil.transferDataByList("TABLE",data);
    }
}

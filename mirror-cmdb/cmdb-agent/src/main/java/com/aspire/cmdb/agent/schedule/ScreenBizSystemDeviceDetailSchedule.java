package com.aspire.cmdb.agent.schedule;

import com.aspire.cmdb.agent.client.CmdbServiceClient;
import com.aspire.ums.cmdb.client.ICmdbToAlterESClient;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.index.payload.ItCloudScreenBizSystemDeviceDetailRequest;
import com.aspire.ums.cmdb.index.payload.ScreenBizSystemDeviceDetail;
import com.aspire.ums.cmdb.v2.index.serivce.ItCloudScreenBizSystemDeviceDetailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @ClassName ScreenBizSystemDeviceDetailSchedule
 * @Description 租户大屏定时取具体设备类型的cpu和内存的数据
 * @Author luowenbo
 * @Date 2020/5/13 14:29
 * @Version 1.0
 */
@Component
@EnableScheduling
@Slf4j
public class ScreenBizSystemDeviceDetailSchedule {

    @Autowired
    private ICmdbToAlterESClient esClient;
    @Autowired
    private ItCloudScreenBizSystemDeviceDetailService service;
    @Autowired
    private CmdbServiceClient cmdbServiceClient;

    /*
    *  每个月1号执行一次，查询出上个月的所有具体设备数据
    * */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void startSync(){
        syncDeviceDetailData("");
    }

    public void syncDeviceDetailData(String monthParam) {
        log.info("开始-获取业务系统具体设备的cpu和内存的数据...");
        // 获取当前月份
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        String tmpMonthlyTime;
        if(month == 1) {
            tmpMonthlyTime = (year -1) + "-" + 12;
        } else {
            month -= 1;
            tmpMonthlyTime = year + "-" + (month < 10 ? "0" + month : "" + month);
        }
        String monthlyTime = StringUtils.isEmpty(monthParam) ? tmpMonthlyTime : monthParam;
        // 从大屏资源分配表中查出唯一性的 设备类型和业务系统
        List<Map<String,String>> dtAndBz = service.getDevTypeAndBizSystemList(monthlyTime);
        // 依据业务系统和设备类型以及日期去ES中查询数据
        for(Map<String,String> item : dtAndBz) {
            String deviceType = item.get("deviceType");
            String bizSystem = item.get("bizSystem");
            //将业务系统的名称转为ID作为参数
            String bizIdByName = getBizIdByName(bizSystem);
            if(StringUtils.isEmpty(bizIdByName)) {
                log.info("数据异常——————业务系统:" + bizSystem + "基础数据有误!!!");
                continue;
            }
            int day = getDayByMonthAndYear(monthlyTime);
            // 循环日期去ES中查询数据
            for(int i=1;i<=day;i++) {
                List<ScreenBizSystemDeviceDetail> insertList = new ArrayList<>();
                String paramDay = i < 10 ? "0" + i : "" + i;
                // 封装请求参数
                Map<String,Object> paramMp = new HashMap<>();
                List<Map<String, Object>> tmp;
                // ES传递参数，先通过业务系统Name去查询，如果为空再通过业务系统ID去查询
                paramMp.put("deviceType",deviceType);
                paramMp.put("bizSystem",bizSystem);
                paramMp.put("day",monthlyTime + "-" + paramDay);
                log.info("处理中-从ES中获取["+ monthlyTime + "-" + paramDay+ "]" + deviceType + "-" + bizSystem + "的设备cpu和内存的数据...");
                tmp = esClient.getBizSystemDetailInfo(paramMp);
                if(null == tmp || tmp.isEmpty()) {
                    log.info("处理进行中,通过业务系统名称查询数据为[]列表");
                    paramMp.put("bizSystem",bizIdByName);
                    log.info("处理中-从ES中获取["+ monthlyTime + "-" + paramDay+ "]" + deviceType + "-" + bizIdByName + "的设备cpu和内存的数据...");
                    tmp = esClient.getBizSystemDetailInfo(paramMp);
                }
                // 如果通过业务系统名称和ID都查询不到数据，就跳过处理
                if(null == tmp || tmp.isEmpty()) {
                    log.info("处理完成(当天无数据)-从ES中获取["+ monthlyTime + "-" + paramDay+ "]" + deviceType + "-" + bizSystem + "的设备cpu和内存的数据...");
                    continue;
                }
                for(Map<String, Object> it : tmp) {
                    ScreenBizSystemDeviceDetail bsdd = new ScreenBizSystemDeviceDetail();
                    bsdd.setDeviceType(deviceType);
                    bsdd.setBizSystem(bizSystem);
                    bsdd.setIp(it.get("ip") == null ? null : it.get("ip").toString());
                    bsdd.setResourcePool(it.get("idcType") == null ? null : it.get("idcType").toString());
                    bsdd.setDateDisplay(it.get("dateDisplsy") == null ? null : it.get("dateDisplsy").toString());
                    bsdd.setStatistDate(monthlyTime + "-" + paramDay);
                    bsdd.setCpuMax(it.get("cpuMax") == null ? "0" : it.get("cpuMax").toString());
                    bsdd.setMemoryMax(it.get("memoryMax") == null ? "0" : it.get("memoryMax").toString());
                    bsdd.setCpuAvg(it.get("cpuAvg") == null ? "0" : it.get("cpuAvg").toString());
                    bsdd.setMemoryAvg(it.get("memoryAvg") == null ? "0" : it.get("memoryAvg").toString());
                    insertList.add(bsdd);
                }
                // 防止重复，数据先删除再新增
                ItCloudScreenBizSystemDeviceDetailRequest req = new ItCloudScreenBizSystemDeviceDetailRequest();
                req.setDeviceType(deviceType);
                req.setBizSystem(bizSystem);
                req.setMonthlyTime(monthlyTime + "-" + paramDay);
                service.delete(req);
                // 查询的数据入CMDB库
                service.batchInsert(insertList);
                log.info("处理完成-从ES中获取["+ monthlyTime + "-" + paramDay+ "]" + deviceType + "-" + bizSystem + "的设备cpu和内存的数据...");
            }
        }
        log.info("结束-获取业务系统具体设备的cpu和内存的数据...");
    }

    /*
     *  根据年份和月份返回月份的天数
     *  @param monthlyTime : 例如 2020-03
     * */
    private int getDayByMonthAndYear(String monthlyTime) {
        int[] monthDay = {0,31,28,31,30,31,30,31,31,30,31,30,31};
        int year = Integer.parseInt(monthlyTime.split("-")[0]);
        int month = Integer.parseInt(monthlyTime.split("-")[1]);
        // 有月份计算月份的日期，且判断闰年
        if(year%400==0 || (year%4==0 && year%100!=0)) {
            if(month == 2) {
                monthDay[month] = 29;
            }
        }
        return monthDay[month];
    }

    /**
     *  依据业务系统名称获取其ID
     */
    private String getBizIdByName(String bizName){
        Map<String,String> biz = new HashMap<>();
        biz.put("bizSystem",bizName);
        Map<String,Object> params = new HashMap<>();
        params.put("name","query_bizSystem_info");
        params.put("params",biz);
        params.put("responseType","map");
        Map<String, Object> instanceStatistics = cmdbServiceClient.getInstanceStatistics(params);
        if(!instanceStatistics.isEmpty()) {
            return instanceStatistics.get("id").toString();
        }
        return null;
    }
}

package com.aspire.mirror.alert.server.biz.monthReport.impl;

import com.aspire.mirror.alert.server.biz.monthReport.AlertsScheduleBiz;
import com.aspire.mirror.alert.server.clientservice.CmdbDictClient;
import com.aspire.mirror.alert.server.clientservice.CmdbInstanceClient;
import com.aspire.mirror.alert.server.clientservice.CmdbResfulClient;
import com.aspire.mirror.alert.server.constant.AlertCommonConstant;
import com.aspire.mirror.alert.server.dao.inspectionDaily.AlertInspectionDao;
import com.aspire.mirror.alert.server.dao.monthReport.AlertsMonReportDao;
import com.aspire.mirror.alert.server.vo.monthReport.AlertsMonReportAlertVo;
import com.aspire.mirror.alert.server.vo.monthReport.AlertsMonReportIdcTypeVo;
import com.aspire.mirror.alert.server.util.MapUtils;
import com.aspire.mirror.alert.server.util.StringUtils;
import com.aspire.mirror.alert.server.biz.model.AlertFieldBiz;
import com.aspire.mirror.alert.server.constant.AlertConfigConstants;
import com.aspire.mirror.alert.server.dao.cmdbInstance.CmdbInstanceMapper;
import com.aspire.mirror.alert.server.vo.model.AlertFieldVo;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AlertScheduleBizImpl implements AlertsScheduleBiz {

    @Autowired
    private CmdbDictClient cmdbDictClient;
    @Autowired
    private AlertsMonReportDao alertsMonReportDao;


    private Map<String,Object> getTimeRange() {
        Map<String,Object> map = Maps.newHashMap();
        //获取前一个月第一天
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.MONTH, -1);
        calendar1.set(Calendar.DAY_OF_MONTH,1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String firstDay = sdf.format(calendar1.getTime());
        //获取当前月第一天
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.MONTH, 0);
        calendar2.set(Calendar.DAY_OF_MONTH, 1);
        String nowFirstDay = sdf.format(calendar2.getTime());

        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.MONTH, -1);
        SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");
        String month = sdfMonth.format(calendar3.getTime());
        map.put("startTime",firstDay);
        map.put("endTime",nowFirstDay);
        map.put("month",month);
        return map;
    }

    @Override
    public void alert(String startTime,String endTime,String month) {
        try {
            //获取时间范围-前一个月范围
            Map<String, Object> alertParam = getTimeRange();
            if (StringUtils.isNotEmpty(startTime)) alertParam.put("startTime",startTime);
            if (StringUtils.isNotEmpty(endTime)) alertParam.put("endTime",endTime);
            if (StringUtils.isNotEmpty(month)) alertParam.put("month",month);
            //获取资源池
            List<ConfigDict> idcType = cmdbDictClient.getDictsByType("idcType",null,null,null);
            List<AlertsMonReportIdcTypeVo> res = Lists.newArrayList();
            for (ConfigDict configDict : idcType) {
                alertParam.put("idcType",configDict.getValue());
                //严重
                AlertsMonReportIdcTypeVo alertIdcS = getAlertIdc( alertParam, "5" );
                if (null != alertIdcS) res.add(alertIdcS);
                //高
                AlertsMonReportIdcTypeVo alertIdcH = getAlertIdc( alertParam, "4" );
                if (null != alertIdcH) res.add(alertIdcH);
                //中
                AlertsMonReportIdcTypeVo alertIdcM = getAlertIdc( alertParam, "3" );
                if (null != alertIdcM) res.add(alertIdcM);
                //低
                AlertsMonReportIdcTypeVo alertIdcL = getAlertIdc( alertParam, "2" );
                if (null != alertIdcL) res.add(alertIdcL);
            }
            //写入数据库
            for (AlertsMonReportIdcTypeVo alertsMonReportIdcTypeVo : res) {
                alertsMonReportDao.insertAlertMonReportIdc(alertsMonReportIdcTypeVo);
            }
        } catch (Exception e) {
            log.error("[运营月报告警分布定时任务] error is {}",e);
        }

    }
    private AlertsMonReportIdcTypeVo getAlertIdc(Map<String, Object> alertParam, String alertLevel) {
        alertParam.put("alertLevel",alertLevel);
        AlertsMonReportIdcTypeVo alertIdcData = alertsMonReportDao.getAlertIdcData(alertParam);
        if (null != alertIdcData) {
            alertIdcData.setMon(String.valueOf(alertParam.get("month")));
        }
        return alertIdcData;
    }

    @Override
    public void device(String startTime,String endTime,String month) {
        try {
            //获取时间范围-前一个月范围
            Map<String, Object> param = getTimeRange();
            if (StringUtils.isNotEmpty(startTime)) param.put("startTime",startTime);
            if (StringUtils.isNotEmpty(endTime)) param.put("endTime",endTime);
            if (StringUtils.isNotEmpty(month)) param.put("month",month);
            // 获取资源池
            List<ConfigDict> idcType = cmdbDictClient.getDictsByType("idcType",null,null,null);
            List<AlertsMonReportAlertVo> list = Lists.newArrayList();
            for (ConfigDict configDict : idcType) {
                param.put("idcType",configDict.getValue());
                for (String str : AlertCommonConstant.DEVICE_TYPE_CONFIGS) {
                    param.put("deviceType", str);
                    // 严重
                    List<AlertsMonReportAlertVo> deviceSData = getDeviceData( param, "5" );
                    list.addAll(deviceSData);
                    //高
                    List<AlertsMonReportAlertVo> deviceHData = getDeviceData( param, "4" );
                    list.addAll(deviceHData);
                    //中
                    List<AlertsMonReportAlertVo> deviceMData = getDeviceData( param, "3" );
                    list.addAll(deviceMData);
                    //低
                    List<AlertsMonReportAlertVo> deviceLData = getDeviceData( param, "2" );
                    list.addAll(deviceLData);
                }
            }
            //写入数据库
            if (CollectionUtils.isNotEmpty(list)) {
                for (AlertsMonReportAlertVo alertsMonReportAlertVo : list) {
                    alertsMonReportDao.insertMonReportAlertDevice(alertsMonReportAlertVo);
                }
            }
        } catch (Exception e) {
            log.error("[运营月报告警设备定时任务] error is {}",e);
        }
    }
    private List<AlertsMonReportAlertVo> getDeviceData (Map<String, Object> param, String alertLevel) {
        param.put("alertLevel",alertLevel);
        List<AlertsMonReportAlertVo> deviceData = alertsMonReportDao.getDeviceData(param);
        if (CollectionUtils.isNotEmpty(deviceData)) {
            int i=1;
            for (AlertsMonReportAlertVo alertsMonReportAlertVo : deviceData) {
                alertsMonReportAlertVo.setRank(i++);
                alertsMonReportAlertVo.setMonth(String.valueOf(param.get("month")));
                StringBuffer mrfsModelOption = new StringBuffer();
                if (StringUtils.isNotEmpty(alertsMonReportAlertVo.getDeviceMrfs()))
                    mrfsModelOption.append(alertsMonReportAlertVo.getDeviceMrfs());
                if (StringUtils.isNotEmpty(alertsMonReportAlertVo.getDeviceModel()))
                    mrfsModelOption.append(alertsMonReportAlertVo.getDeviceModel());
                mrfsModelOption.append(alertsMonReportAlertVo.getIdcType());
                alertsMonReportAlertVo.setMrfsModelOption(mrfsModelOption.toString());
                StringBuffer idcPod = new StringBuffer();
                idcPod.append(alertsMonReportAlertVo.getIdcType());
                if (StringUtils.isNotEmpty(alertsMonReportAlertVo.getPod()))
                    idcPod.append(alertsMonReportAlertVo.getPod());
                alertsMonReportAlertVo.setIdcPod(idcPod.toString());
            }
        }

        return deviceData;
    }

    @Override
    public void alertIndex(String startTime,String endTime,String month) {
        //获取时间范围-前一个月范围
        Map<String, Object> indexParam = getTimeRange();
        if (StringUtils.isNotEmpty(startTime)) indexParam.put("startTime",startTime);
        if (StringUtils.isNotEmpty(endTime)) indexParam.put("endTime",endTime);
        if (StringUtils.isNotEmpty(month)) indexParam.put("month",month);
        // 获取资源池
        List<ConfigDict> idcType = cmdbDictClient.getDictsByType("idcType",null,null,null);
        // 上月告警数量总和
        float alertIndexSum = alertsMonReportDao.getAlertIndexSum(indexParam);
        List<AlertsMonReportAlertVo> list = Lists.newArrayList();
        for (ConfigDict configDict : idcType) {
            indexParam.put("idcType",configDict.getValue());
            // 严重
            List<AlertsMonReportAlertVo> deviceSData = getAlertIndexData( indexParam, "5", alertIndexSum );
            list.addAll(deviceSData);
            //高
            List<AlertsMonReportAlertVo> deviceHData = getAlertIndexData( indexParam, "4", alertIndexSum );
            list.addAll(deviceHData);
            //中
            List<AlertsMonReportAlertVo> deviceMData = getAlertIndexData( indexParam, "3", alertIndexSum );
            list.addAll(deviceMData);
            //低
            List<AlertsMonReportAlertVo> deviceLData = getAlertIndexData( indexParam, "2", alertIndexSum );
            list.addAll(deviceLData);
        }
        //写入数据库
        if (CollectionUtils.isNotEmpty(list)) {
            for (AlertsMonReportAlertVo alertsMonReportAlertVo : list) {
                alertsMonReportDao.insertMonReportAlertIndex(alertsMonReportAlertVo);
            }
        }
    }
    private List<AlertsMonReportAlertVo> getAlertIndexData (Map<String, Object> param, String alertLevel, float alertIndexSum) {
        param.put("alertLevel",alertLevel);
        List<AlertsMonReportAlertVo> getAlertIndexData = alertsMonReportDao.getAlertIndexData(param);

        if (CollectionUtils.isNotEmpty(getAlertIndexData)) {
            int n = 1;
            for (AlertsMonReportAlertVo alertsMonReportAlertVo : getAlertIndexData) {
                alertsMonReportAlertVo.setRank(n++);
                alertsMonReportAlertVo.setMonth(String.valueOf(param.get("month")));
                if (alertIndexSum > 0) {
                    float v = alertsMonReportAlertVo.getSCount() / alertIndexSum;
                    BigDecimal bd = new BigDecimal(v);
                    double  retValue = bd.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue() * 100;
                    int i = (new Double(retValue)).intValue();
                    alertsMonReportAlertVo.setRate(String.valueOf(i));
                }
            }
        }

        return getAlertIndexData;
    }

    @Override
    public void alertSum(String startTime,String endTime,String month) {
        //获取时间范围-前一个月范围
        Map<String, Object> indexParam = getTimeRange();
        if (StringUtils.isNotEmpty(startTime)) indexParam.put("startTime",startTime);
        if (StringUtils.isNotEmpty(endTime)) indexParam.put("endTime",endTime);
        if (StringUtils.isNotEmpty(month)) indexParam.put("month",month);
        // 查询数据库
        List<Map<String,Object>> alertIdcData = alertsMonReportDao.getIdcTypeAlertCount(indexParam);
        for (Map<String,Object> map : alertIdcData) {
            map.put("mon", indexParam.get("month"));
            alertsMonReportDao.insertAlertMonReportSum(map);
        }
    }

    @Autowired
    private CmdbResfulClient iCommonRestfulClient;
    @Autowired
    private AlertFieldBiz alertFieldBiz;
    @Autowired
    private CmdbInstanceMapper cmdbInstanceMapper;
    @Value("${cmdb.token:5245ed1b-6345-11e}")
    private String cmdbToken;
    @Value("${cmdb.condicationCode:alert_device_model_instance}")
    private String condicationCode;
    @Value("${cmdb.size:500000}")
    private Integer pageSize;

    @Override
    @Transactional
    public void synchronize() {
        long l = System.currentTimeMillis();
        int page = 1;
        // 获取主机资源总量
        Map<String, Object> params = Maps.newHashMap();
//        params.put("token",cmdbToken);
//        params.put("condicationCode",condicationCode);
        params.put("currentPage", page);
        params.put("pageSize",pageSize);
//        Result<Map<String, Object>> instanceList = iCommonRestfulClient.getInstanceList(params);
        Result<Map<String, Object>> instanceList = cmdbInstanceClient.getAllIPInstance(params);
        long l1 = System.currentTimeMillis();
        log.info("query cmdb instance list use {} ms",(l1 -l));
        if (instanceList == null || instanceList.getData() == null || instanceList.getData().isEmpty()) {
            return ;
        }

        // 获取模型字段
        List<AlertFieldVo> modelFromRedis =
                alertFieldBiz.getModelFromRedis(AlertConfigConstants.REDIS_MODEL_DEVICE_INSTANCE, null);
        if (modelFromRedis.isEmpty()) {
            log.error("cmdb instance model is not exist!");
            return;
        }
        List<String> keys = modelFromRedis.stream().map(AlertFieldVo::getFieldCode).collect(Collectors.toList());
        Map<String, Object> ciMap = Maps.newHashMap();
        ciMap.put("fieldList", keys);
        List<Map<String, Object>> list = instanceList.getData();

        // 删除数据
        cmdbInstanceMapper.deleteAll();
        insertCmdb(ciMap, list, modelFromRedis);

        int totalSize = instanceList.getTotalSize();
        int size = totalSize / pageSize;
        if (totalSize % pageSize != 0) {
            size++;
        }
        for (int i=2; i<=size; i++) {
            params.put("currentPage", i);
            instanceList = cmdbInstanceClient.getAllIPInstance(params);
            if (instanceList == null) {
                break;
            }
            list = instanceList.getData();
            if (CollectionUtils.isEmpty(list)) {
                break;
            }
            insertCmdb(ciMap, list, modelFromRedis);
        }

        log.info("insert cmdb instance list use {} ms",(System.currentTimeMillis() -l1));
    }

    /**
     * 插入数据
     * @param ciMap
     * @param list
     * @param modelFromRedis
     */
    public synchronized void insertCmdb (Map<String, Object> ciMap, List<Map<String, Object>> list, List<AlertFieldVo> modelFromRedis) {
        List<Map<String, Object>> values = Lists.newArrayList();
        int size = list.size();
        for (int i = 0; i < size; i++) {

            // 批量保存
            if (i != 0 && i % 200 == 0 && !values.isEmpty()) {
                ciMap.put("dateList", values);
                cmdbInstanceMapper.insert(ciMap);
                values.clear();
            }
            Map<String, Object> ci = list.get(i);
            Integer changeType = org.apache.commons.collections.MapUtils.getInteger(ci,"changeType");
            String id = MapUtils.getString(ci, "id");
            if (changeType != null && changeType == 3) {

                if (!StringUtils.isEmpty(id)) {
                    cmdbInstanceMapper.deleteById(id);
                }
            } else {
                if(StringUtils.isEmpty(id)) {
                    continue;
                }
                for (AlertFieldVo alertFieldVo : modelFromRedis) {
                    if (!StringUtils.isEmpty(alertFieldVo.getCiCode())) {
                        String ciValue = MapUtils.getString(ci, alertFieldVo.getCiCode());
                        if (!StringUtils.isEmpty(ciValue)) {
                            ci.put(alertFieldVo.getFieldCode(), ciValue);
                        }
                    }
                }
                values.add(ci);
            }
        }

        if (!values.isEmpty()) {
            ciMap.put("dateList", values);
            cmdbInstanceMapper.insert(ciMap);
            values.clear();
        }
    }

    @Autowired
    private CmdbInstanceClient cmdbInstanceClient;
    @Autowired
    private AlertInspectionDao alertInspectionDao;
    @Override
    public void deviceInspectionByDay() {
        // 获取cmdb主机实例数据
        try {
            Map<String, Object> vmList = cmdbInstanceClient.deviceCountByDeviceType("服务器", "虚拟机");
            Map<String, Object> phyList = cmdbInstanceClient.deviceCountByDeviceType("服务器", "X86服务器");
            Map<String, Object> hostList = cmdbInstanceClient.deviceCountByDeviceType("服务器", "宿主机");
            int vmCount = vmList != null && null != vmList.get("type_count") ? Integer.valueOf(String.valueOf(vmList.get("type_count"))) : 0;
            int phyCount = phyList != null && null != phyList.get("type_count") ? Integer.valueOf(String.valueOf(phyList.get("type_count"))) : 0;
            int hostCount = hostList != null && null != hostList.get("type_count") ? Integer.valueOf(String.valueOf(hostList.get("type_count"))) : 0;
            // CMDB 网络设备
            Map<String, Object> netList = cmdbInstanceClient.deviceCountByDeviceType("网络设备", null);
            int netCount = netList != null && null != netList.get("type_count") ? Integer.valueOf(String.valueOf(netList.get("type_count"))) : 0;

            Map<String, Object> p = Maps.newHashMap();
            double deviceSum = vmCount + phyCount + hostCount + netCount;
            p.put("deviceSum",(int)deviceSum);
            String dateTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            List<Map<String, Object>> alertDataByDeviceType = alertInspectionDao.getAlertDataByDeviceType(dateTime);
            int vmSum = 0;
            int phySum = 0;
            int hostSum = 0;
            for (Map<String, Object> item : alertDataByDeviceType) {
                if (item.get("device_type").equals("虚拟机")) {
                    vmSum = item.get("sCount") == null ? 0 : Integer.valueOf(String.valueOf(item.get("sCount")));
                }
                if (item.get("device_type").equals("X86服务器")) {
                    phySum = item.get("sCount") == null ? 0 : Integer.valueOf(String.valueOf(item.get("sCount")));
                }
                if (item.get("device_type").equals("宿主机")) {
                    hostSum = item.get("sCount") == null ? 0 : Integer.valueOf(String.valueOf(item.get("sCount")));
                }
            }
            // 告警 网络设备
            Map<String, Object> netAlert = alertInspectionDao.getAlertDataByDeviceClass(dateTime, "网络设备");
            int netSum = null != netAlert && null != netAlert.get("sCount")? Integer.valueOf(String.valueOf(netAlert.get("sCount"))) : 0;

            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            // 网络设备
            p.put("netSum",netSum);
            double netRate = deviceSum != 0 && netSum != 0
                    ? Double.valueOf(decimalFormat.format(netSum / deviceSum)) * 100 : 0;
            p.put("netRate",(int)netRate);
            p.put("vmSum",vmSum);
            double vmRate = deviceSum != 0 && vmSum != 0
                    ? Double.valueOf(decimalFormat.format(vmSum / deviceSum)) * 100 : 0;
            p.put("vmRate",(int)vmRate);
            p.put("phySum",phySum);
            double phyRate = deviceSum != 0 && phySum != 0
                    ? Double.valueOf(decimalFormat.format(phySum / deviceSum)) * 100  : 0;
            p.put("phyRate",(int)phyRate);
            p.put("hostSum",hostSum);
            double hostRate = deviceSum != 0 && hostSum != 0
                    ? Double.valueOf(decimalFormat.format(hostSum / deviceSum)) * 100  : 0;
            p.put("hostRate",(int)hostRate);
            double exceptionRate = deviceSum != 0 && (vmSum + phySum + hostSum + netSum) != 0
                    ? Double.valueOf(decimalFormat.format((vmSum + phySum + hostSum + netSum) / deviceSum))  : 0 ;
            p.put("exceptionRate",exceptionRate);
            p.put("normalSum",deviceSum != 0 ? (int)(deviceSum - vmSum - phySum - hostSum - netSum) : 0);
            p.put("normalRate",Double.valueOf(decimalFormat.format(1-exceptionRate)));
            p.put("mon",dateTime);


            alertInspectionDao.deleteDeviceInspection(dateTime);
            alertInspectionDao.insertDeviceInspection(p);
        } catch (Exception e) {
            log.error("Device Inspection Error is {}",e);
        }
    }

    @Override
    public void bizSystemInspectionByDay() {

        String dateTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //登陆
        List<String> normalLoginData = alertInspectionDao.getBizSystemData("登陆");
        int exceptionLoginCount = alertInspectionDao.getAlertDataByBizSystem(dateTime, Joiner.on(",").join(normalLoginData), "登陆");
        //访问
        List<String> normalVisitData = alertInspectionDao.getBizSystemData("访问");
        int exceptionVisitCount = alertInspectionDao.getAlertDataByBizSystem(dateTime, Joiner.on(",").join(normalVisitData), "访问");

        // 总量
        double bizsystemCount = normalLoginData.size() + normalVisitData.size();
        // 异常数量
        int exceptionCount = exceptionLoginCount + exceptionVisitCount;
        // 正常数量
        int normalCount = (int)bizsystemCount - exceptionCount;
        int normalLoginCount = normalLoginData.size() - exceptionLoginCount;
        int normalVisitCount = normalVisitData.size() - exceptionVisitCount;
        // 占比
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        double exceptionRate = exceptionCount == 0 ? 0 : Double.valueOf(decimalFormat.format(exceptionCount / bizsystemCount));
        double normalRate = 1 - exceptionRate;

        Map<String, Object> param = Maps.newHashMap();
        param.put("bizsystemCount", (int)bizsystemCount);
        param.put("normalCount", normalCount);
        param.put("normalLoginCount", normalLoginCount);
        param.put("normalVisitCount", normalVisitCount);
        param.put("normalRate", normalRate);
        param.put("exceptionCount", exceptionCount);
        param.put("exceptionLoginCount", exceptionLoginCount);
        param.put("exceptionVisitCount", exceptionVisitCount);
        param.put("exceptionRate", exceptionRate);
        param.put("mon", dateTime);

        alertInspectionDao.deleteBizSystemInspection(dateTime);
        alertInspectionDao.insertBizSystemInspection(param);

    }
}

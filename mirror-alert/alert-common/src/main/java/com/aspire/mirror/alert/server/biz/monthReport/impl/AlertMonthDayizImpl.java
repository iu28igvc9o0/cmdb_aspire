package com.aspire.mirror.alert.server.biz.monthReport.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.aspire.mirror.alert.server.vo.monthReport.AlertMonthReportVo;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.aspire.mirror.alert.server.biz.monthReport.AlertMonthDayBiz;
import com.aspire.mirror.alert.server.biz.ftp.FtpService;
import com.aspire.mirror.alert.server.biz.helper.CmdbHelper;
import com.aspire.mirror.alert.server.clientservice.CmdbAlertRestfulClient;
import com.aspire.mirror.alert.server.clientservice.MonthDayReportNewServiceClient;
import com.aspire.mirror.alert.server.clientservice.MonthDayReportServiceClient;
import com.aspire.mirror.alert.server.clientservice.MonthReportServiceClient;
import com.aspire.mirror.alert.server.clientservice.elasticsearch.HistoryServiceClient;
import com.aspire.mirror.alert.server.dao.monthReport.AlertMonthReportSyncDao;
import com.aspire.mirror.alert.server.dao.alert.AlertRestfulDao;
import com.aspire.mirror.alert.server.dao.monthReport.po.AlertMonthReportIdcType;
import com.aspire.mirror.alert.server.dao.monthReport.po.alertNetMonitorConfig;
import com.aspire.mirror.alert.server.helper.UtilsHelper;
import com.aspire.mirror.alert.server.util.ExportExcelUtil;
import com.aspire.mirror.alert.server.util.Utils;
import com.aspire.mirror.alert.server.biz.alert.AlertsBizV2;
import com.aspire.mirror.alert.server.biz.helper.AlertsHandleV2Helper;
import com.aspire.mirror.alert.server.vo.model.AlertFieldVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsV2Vo;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.elasticsearch.api.dto.HistorySearchRequest;
import com.aspire.mirror.elasticsearch.api.dto.MonthBizSystemDayRequest;
import com.aspire.mirror.elasticsearch.api.dto.MonthReportNetRequest;
import com.aspire.mirror.elasticsearch.api.dto.MonthReportRequest;
import com.aspire.mirror.elasticsearch.api.dto.NetConfig;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AlertMonthDayizImpl implements AlertMonthDayBiz {
    @Autowired
    private AlertMonthReportSyncDao alertMonthReportSyncDao;

    @Autowired
    private AlertRestfulDao alertRestfulDao;

    @Autowired
    private MonthDayReportServiceClient MonthDayReportServiceClient;

    @Autowired
    private MonthDayReportNewServiceClient MonthDayReportNewServiceClient;

    @Autowired
    private MonthDayReportNewServiceClient monthDayReportNewServiceClient;

    @Autowired
    private MonthReportServiceClient monthReportServiceClient;

    @Value("${cmdbQueryType.department:alert_query_department_by_bizSystem_id}")
    private String cmdbQueryName;


    @Autowired
    private FtpService ftpService;

    private final String CMNET = "CMNET路由器";
    private final String IP = "IP承载网路由器";
    private final String IFHIGHSPEED = "ifHighSpeed";
    private final String ifOutOctets = "ifOutOctets.Pused";
    private final String ifInOctets = "ifInOctets.pused";
    private final String ifHCInOctets = "ifHCInOctets.Total";
    private final String ifHCOutOctets = "ifHCOutOctets.Total";
    @Autowired
    private CmdbAlertRestfulClient cmdbAlertRestfulClient;

    @Autowired
    private AlertsHandleV2Helper alertHandleHelper;
    @Autowired
    private CmdbHelper cmdbHelper;


    @Autowired
    private HistoryServiceClient historyServiceClient;

    @Autowired
    private AlertsBizV2 alertsBizV2;
    @Value("${AlertMonitorScanDayTask.pageSize:4000}")
    private int pageSize;

    @Value("${AlertMonthReportNewDayTask.ftpFilePath:monthly_operation_report}")
    private String ftpFilePath;

    @Value("${AlertMonitorScanDayTask.idcType:信息港资源池,呼和浩特资源池,南基资源池,哈尔滨资源池,深圳池外}")
    private String idcTypeDefault;

    @Autowired
    private UtilsHelper utilsHelper;


    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Override
    public void syncBizSytemDay(AlertMonthReportVo monthReportRequest) throws ParseException {
        String idcType = monthReportRequest.getIdcType();
        String days = monthReportRequest.getDays();
        log.info("*AlertMonthController-syncBizSytemDay--begin:{},{}", idcType, days);
        List<String> dateList = Lists.newArrayList();
        // initList1(hList, kList, startTime, endTime, dateList);
        // days = "normal";

        String[] daysArr = days.split(",");
        dateList = Arrays.asList(daysArr);

        Map<String, Map<String, Object>> dataMap = Maps.newHashMap();
        List<Map<String, Object>> dataList = Lists.newArrayList();
        for (int i = 0; i < dateList.size(); i++) {
            MonthReportRequest request = new MonthReportRequest();
            request.setDataMap(dataMap);
            request.setDeviceType("X86服务器");
            request.setPod(monthReportRequest.getPod());
            String date = "";

            date = dateList.get(i);

            if (date.isEmpty()) {
                continue;
            }
            String month = date.substring(0, date.lastIndexOf("-"));
            // String dataEnd = dateList.get(i+4);
            request.setEndTime(date + " 23:59:59");
            request.setStartTime(date + " 00:00:00");

            request.setIdcType(idcType);
            request.setItem("cpu");

            dataMap = MonthDayReportServiceClient.queryByDay(request);
            request.setItem("memory");
            request.setDataMap(dataMap);
            /*
             * if(date.contains("2020-01-15")) { int a=9; }
             */

            dataMap = MonthDayReportServiceClient.queryByDay(request);
            request.setDeviceType("云主机");
            request.setItem("cpu");
            request.setDataMap(dataMap);
            dataMap = MonthDayReportServiceClient.queryByDay(request);
            request.setItem("memory");
            request.setDataMap(dataMap);
            dataMap = MonthDayReportServiceClient.queryByDay(request);
            if (dataMap.size() > 0) {
                if (dataMap.size() > 0) {
                    List<String> bizList = Lists.newArrayList();
                    for (Map.Entry<String, Map<String, Object>> entry : dataMap.entrySet()) {
                        entry.getValue().put("day", date);
                        entry.getValue().put("month", month);
                        entry.getValue().put("history_min_flag", 0);
                        dataList.add(entry.getValue());
                        String key = MapUtils.getString(entry.getValue(), "bizSystem");
                        if (!bizList.contains(key)) {
                            bizList.add(key);
                        }
                    }
                    utilsHelper.formListBizIdData(dataList, bizList);//设置租户的id值
                }

                dataMap.clear();
            }
        }

//	       

        if (dataList.size() > 0) {
            alertMonthReportSyncDao.insertIdcTypeBizSystemDay(dataList);
        }
        log.info("*AlertMonthController-syncBizSytemDay--end*");
    }

    @Override
    public void syncIdcTypeIpDay(AlertMonthReportVo alertMonthReportVo) throws Exception {
        String idcType = alertMonthReportVo.getIdcType();
        String days = alertMonthReportVo.getDays();
        log.info("*AlertMonthController-syncIdcTypeIpDay-begin:{}*", alertMonthReportVo);
        List<String> daysList = Lists.newArrayList();
        if (days.equals("normal")) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            Date startTime = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            daysList.add(sdf.format(startTime));

        } else {
            String[] daysArr = days.split(",");
            daysList = Arrays.asList(daysArr);

        }
        List<Map<String, Object>> dataList = Lists.newArrayList();
        for (String day : daysList) {

            MonthReportRequest monthReportRequest = new MonthReportRequest();
            monthReportRequest.setStartTime(day + " 00:00:00");
            monthReportRequest.setIdcType(idcType);
            monthReportRequest.setEndTime(day + " 23:59:59");
            monthReportRequest.setPod(alertMonthReportVo.getPod());
            Map<String, Map<String, Object>> map = Maps.newHashMap();
            // map =
            // MonthDayReportServiceClient.idcTypeDeviceUsedRateByDay(monthReportRequest);

            monthReportRequest.setDataMap(map);
            monthReportRequest.setDeviceType("X86服务器");
            monthReportRequest.setItem("cpu");
            map = MonthDayReportServiceClient.idcTypeDeviceUsedRateByDay(monthReportRequest);
            monthReportRequest.setDataMap(map);

            monthReportRequest.setItem("memory");
            map = MonthDayReportServiceClient.idcTypeDeviceUsedRateByDay(monthReportRequest);
            monthReportRequest.setDataMap(map);

            monthReportRequest.setDeviceType("云主机");
            monthReportRequest.setItem("cpu");
            map = MonthDayReportServiceClient.idcTypeDeviceUsedRateByDay(monthReportRequest);
            monthReportRequest.setDataMap(map);

            monthReportRequest.setItem("memory");
            map = MonthDayReportServiceClient.idcTypeDeviceUsedRateByDay(monthReportRequest);
            monthReportRequest.setDataMap(map);

            if (null != map && map.size() > 0) {

                for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
                    entry.getValue().put("day", day);
                    dataList.add(entry.getValue());
                }

                // alertMonthReportSyncDao.insertIdcType(list);
                alertMonthReportSyncDao.insertIdcTypeIpDay(new ArrayList(map.values()));
            }
        }
        log.info("*AlertMonthController-syncIdcTypeIpDay-end*");

    }

    @Override
    public List<Map<String, Object>> syncBizSytemDayNew(AlertMonthReportVo monthReportRequest)
            throws ParseException {
        String idcType = monthReportRequest.getIdcType();
        String days = monthReportRequest.getDays();
        log.info("*AlertMonthController-syncBizSytemDay--begin:{},{}", idcType, days);
        List<String> dateList = Lists.newArrayList();
        // initList1(hList, kList, startTime, endTime, dateList);
        // days = "normal";

        String[] daysArr = days.split(",");
        dateList = Arrays.asList(daysArr);

        Map<String, Map<String, Object>> dataMap = Maps.newHashMap();
        List<Map<String, Object>> dataList = Lists.newArrayList();
        for (int i = 0; i < dateList.size(); i++) {
            String date = "";

            date = dateList.get(i);

            if (date.isEmpty()) {
                continue;
            }
            String month = date.substring(0, date.lastIndexOf("-"));
            int history_min_flag = 0;
            if (monthReportRequest.isDeviceTypeNull()) {
                history_min_flag = 2;
                MonthReportRequest request = new MonthReportRequest();
                if (StringUtils.isNotEmpty(monthReportRequest.getDepartment1())) {
                    request.setCol("department1");
                    request.setColValue(monthReportRequest.getDepartment1());
                }
                request.setDataMap(dataMap);
                // request.setDeviceType("X86服务器");
                request.setPod(monthReportRequest.getPod());

                // String dataEnd = dateList.get(i+4);
                request.setEndTime(date + " 23:59:59");
                request.setStartTime(date + " 00:00:00");

                request.setIdcType(idcType);
                request.setItem("cpu");
                dataMap = MonthDayReportNewServiceClient.queryByDay(request);

                request.setItem("memory");
                request.setDataMap(dataMap);
                dataMap = MonthDayReportNewServiceClient.queryByDay(request);
            } else {
                MonthReportRequest request = new MonthReportRequest();
                request.setDataMap(dataMap);
                if (StringUtils.isNotBlank(monthReportRequest.getDeviceTypePhy())) {
                    request.setDeviceType(monthReportRequest.getDeviceTypePhy());
                } else {
                    request.setDeviceType("X86服务器");
                }

                request.setPod(monthReportRequest.getPod());

                // String dataEnd = dateList.get(i+4);
                request.setEndTime(date + " 23:59:59");
                request.setStartTime(date + " 00:00:00");

                request.setIdcType(idcType);
                request.setItem("cpu");

                dataMap = MonthDayReportNewServiceClient.queryByDay(request);
                request.setItem("memory");
                request.setDataMap(dataMap);
                /*
                 * if(date.contains("2020-01-15")) { int a=9; }
                 */

                dataMap = MonthDayReportNewServiceClient.queryByDay(request);
                //request.setDeviceType("云主机");
                if (StringUtils.isNotBlank(monthReportRequest.getDeviceTypeVm())) {
                    request.setDeviceType(monthReportRequest.getDeviceTypeVm());
                } else {
                    request.setDeviceType("云主机");
                }
                request.setItem("cpu");
                request.setDataMap(dataMap);
                dataMap = MonthDayReportNewServiceClient.queryByDay(request);
                request.setItem("memory");
                request.setDataMap(dataMap);
                dataMap = MonthDayReportNewServiceClient.queryByDay(request);
            }

            if (dataMap.size() > 0) {
                List<String> bizList = Lists.newArrayList();
                for (Map.Entry<String, Map<String, Object>> entry : dataMap.entrySet()) {
                    Map<String, Object> m = entry.getValue();
                    m.put("day", date);
                    m.put("month", month);
                    m.put("history_min_flag", history_min_flag);
                    // if(history_min_flag ==2) {
                    // 获取租户1，2
                    String key = MapUtils.getString(entry.getValue(), "bizSystem");
                    if (!bizList.contains(key)) {
                        bizList.add(key);
                    }

                    /*
                     * String bizSystem = entry.getValue().get("bizSystem").toString(); //
                     * bizList.add(bizSystem); try { Map<String, String> biz =
                     * cmdbAlertRestfulClient.getDepartmentInfoByBizSystem(bizSystem); if (null !=
                     * biz) { m.put("department1", StringUtils.isEmpty(biz.get("department1")) ? ""
                     * : biz.get("department1")); m.put("department2",
                     * StringUtils.isEmpty(biz.get("department2")) ? "" : biz.get("department2")); }
                     * } catch (Exception e) { log.error("查询业务系统数据报错：bizSystem:{}", bizSystem); }
                     */

                    dataList.add(entry.getValue());

                }
                utilsHelper.formListBizIdData(dataList, bizList);//设置租户的id值
                dataMap.clear();
            }
        }

//	       

        if (dataList.size() > 0) {
            alertMonthReportSyncDao.insertIdcTypeBizSystemDay(dataList);
        }
        log.info("*AlertMonthController-syncBizSytemDay--end*");
        return dataList;
    }

    @Override
    public void syncIdcTypeDayNew(AlertMonthReportVo monthReportRequest) throws ParseException {
        String days = monthReportRequest.getDays();
        log.info("*syncIdcTypeDayNew--begin:{},{}", days);
        List<String> dateList = Lists.newArrayList();
        // initList1(hList, kList, startTime, endTime, dateList);
        // days = "normal";

        String[] daysArr = days.split(",");
        dateList = Arrays.asList(daysArr);

        Map<String, Map<String, Object>> dataMap = Maps.newHashMap();
        List<Map<String, Object>> dataList = Lists.newArrayList();
        for (int i = 0; i < dateList.size(); i++) {
            MonthReportRequest request = new MonthReportRequest();
            request.setDataMap(dataMap);
            request.setDeviceType("X86服务器");
            request.setPod(monthReportRequest.getPod());
            String date = "";

            date = dateList.get(i);

            if (date.isEmpty()) {
                continue;
            }
            String month = date.substring(0, date.lastIndexOf("-"));
            // String dataEnd = dateList.get(i+4);
            request.setEndTime(date + " 23:59:59");
            request.setStartTime(date + " 00:00:00");

            request.setItem("cpu");

            dataMap = MonthDayReportNewServiceClient.queryIdcTypeByDay(request);
            request.setItem("memory");
            request.setDataMap(dataMap);
            /*
             * if(date.contains("2020-01-15")) { int a=9; }
             */

            dataMap = MonthDayReportNewServiceClient.queryIdcTypeByDay(request);
            request.setDeviceType("云主机");
            request.setItem("cpu");
            request.setDataMap(dataMap);
            dataMap = MonthDayReportNewServiceClient.queryIdcTypeByDay(request);
            request.setItem("memory");
            request.setDataMap(dataMap);
            dataMap = MonthDayReportNewServiceClient.queryIdcTypeByDay(request);
            /*
             * Map<String, Object> dataMap1 = Maps.newHashMap(); dataMap1.put("idcType",
             * "南方基地1"); dataMap.put("南方基地1", dataMap1);
             */
            if (dataMap.size() > 0) {
                for (Map.Entry<String, Map<String, Object>> entry : dataMap.entrySet()) {
                    entry.getValue().put("day", date);
                    entry.getValue().put("month", month);
                    dataList.add(entry.getValue());
                }
                dataMap.clear();
            }
        }

//	       

        if (dataList.size() > 0) {
            alertMonthReportSyncDao.insertIdcTypeDay(dataList);
        }
        log.info("*AlertMonthController-syncIdcTypeDayNew--end*");
    }

    @Override
    public void syncIdcTypeIpDayNew(AlertMonthReportVo alertMonthReportVo) throws Exception {
        String idcType = alertMonthReportVo.getIdcType();
        String days = alertMonthReportVo.getDays();
        log.info("*AlertMonthController-syncIdcTypeIpDay-begin:{},{}*", idcType, days);
        List<String> daysList = Lists.newArrayList();
        if (days.equals("normal")) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            Date startTime = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            daysList.add(sdf.format(startTime));

        } else {
            String[] daysArr = days.split(",");
            daysList = Arrays.asList(daysArr);

        }
        List<Map<String, Object>> dataList = Lists.newArrayList();
        for (String day : daysList) {

            MonthReportRequest monthReportRequest = new MonthReportRequest();
            monthReportRequest.setStartTime(day + " 00:00:00");
            monthReportRequest.setIdcType(idcType);
            monthReportRequest.setEndTime(day + " 23:59:59");
            monthReportRequest.setPod(alertMonthReportVo.getPod());
            Map<String, Map<String, Object>> map = Maps.newHashMap();
            // map =
            // MonthDayReportServiceClient.idcTypeDeviceUsedRateByDay(monthReportRequest);

            monthReportRequest.setDataMap(map);
            monthReportRequest.setDeviceType("X86服务器");
            monthReportRequest.setItem("cpu");
            map = MonthDayReportNewServiceClient.idcTypeDeviceUsedRateByDay(monthReportRequest);
            monthReportRequest.setDataMap(map);

            monthReportRequest.setItem("memory");
            map = MonthDayReportNewServiceClient.idcTypeDeviceUsedRateByDay(monthReportRequest);
            monthReportRequest.setDataMap(map);

            monthReportRequest.setDeviceType("云主机");
            monthReportRequest.setItem("cpu");
            map = MonthDayReportNewServiceClient.idcTypeDeviceUsedRateByDay(monthReportRequest);
            monthReportRequest.setDataMap(map);

            monthReportRequest.setItem("memory");
            map = MonthDayReportNewServiceClient.idcTypeDeviceUsedRateByDay(monthReportRequest);
            monthReportRequest.setDataMap(map);

            if (null != map && map.size() > 0) {

                for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
                    entry.getValue().put("day", day);
                    dataList.add(entry.getValue());
                }

                // alertMonthReportSyncDao.insertIdcType(list);
                alertMonthReportSyncDao.insertIdcTypeIpDay(new ArrayList(map.values()));
            }
        }
        log.info("*AlertMonthController-syncIdcTypeIpDay-end*");

    }

    public List<Date> getLastMonth(String month) throws ParseException {
        List<Date> list = Lists.newArrayList();
        Calendar calendar = Calendar.getInstance();
        if (StringUtils.isNotBlank(month)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            calendar.setTime(sdf.parse(month));
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            list.add(calendar.getTime());
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            list.add(calendar.getTime());
        } else {
            calendar.add(Calendar.MONTH, -1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            Date startTime = calendar.getTime();

            calendar.add(Calendar.MONTH, 1);
            calendar.add(Calendar.DATE, -1);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            Date endTime = calendar.getTime();

            list.add(startTime);
            list.add(endTime);
        }

        return list;
    }

    @Override
    public void IdcTypeMonthData(String month) throws Exception {
        List<Date> list = getLastMonth(month);
        Date startTime = list.get(0);
        Date endTime = list.get(1);

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
        month = sdf1.format(startTime);
        Map<String, Map<String, Object>> map = Maps.newHashMap();
        List<AlertMonthReportIdcType> idcTypeList = alertMonthReportSyncDao.queryIdcTypeBymonthDay(month, "云主机", null);
        for (AlertMonthReportIdcType idc : idcTypeList) {
            String idcType = idc.getIdcType();
            String deviceType = idc.getDeviceType();

            String key = idcType + deviceType;
            Map<String, Object> m = Maps.newHashMap();
            m.put("idcType", idc.getIdcType());
            m.put("cpu_max", idc.getCpu_max());
            m.put("cpu_avg", idc.getCpu_avg());
            m.put("memory_avg", idc.getMemory_avg());
            m.put("memory_max", idc.getMemory_max());
            m.put("deviceType", idc.getDeviceType());
            m.put("month", month);
            m.put("type", 0);
            map.put(key, m);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        MonthReportRequest monthReportRequest = new MonthReportRequest();
        monthReportRequest.setStartTime(sdf.format(startTime));
        monthReportRequest.setEndTime(sdf.format(endTime));
        monthReportRequest.setDataMap(map);
        /*
         * monthReportRequest.setDeviceType("X86服务器");
         * monthReportRequest.setItem("cpu");
         *
         * map =
         * monthDayReportNewServiceClient.idcTypeDeviceUsedRate(monthReportRequest);
         * monthReportRequest.setDataMap(map); monthReportRequest.setItem("memory");
         *
         * map =
         * monthDayReportNewServiceClient.idcTypeDeviceUsedRate(monthReportRequest);
         * monthReportRequest.setDataMap(map);
         */

        monthReportRequest.setDeviceType("云主机");
        monthReportRequest.setItem("cpu");
        map = monthDayReportNewServiceClient.idcTypeDeviceUsedRate(monthReportRequest);
        monthReportRequest.setDataMap(map);

        monthReportRequest.setItem("memory");
        map = monthDayReportNewServiceClient.idcTypeDeviceUsedRate(monthReportRequest);

        if (null != map && map.size() > 0) {
            alertMonthReportSyncDao.insertIdcType(new ArrayList(map.values()));
        }

    }

    @Override
    public void IdcTypeMonthData2(int type, String month) throws Exception {

        List<Date> list = getLastMonth(month);
        Date startTime = list.get(0);
        Date endTime = list.get(1);

        Map<String, Map<String, Object>> map = Maps.newHashMap();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        MonthReportRequest monthReportRequest = new MonthReportRequest();
        monthReportRequest.setStartTime(sdf.format(startTime));
        monthReportRequest.setEndTime(sdf.format(endTime));
        monthReportRequest.setDataMap(map);
        monthReportRequest.setDeviceType("X86服务器");
        monthReportRequest.setItem("cpu");
        monthReportRequest.setType(type);

        map = monthReportServiceClient.queryIdcTypeUserRate(monthReportRequest);
        monthReportRequest.setDataMap(map);

        map = monthDayReportNewServiceClient.idcTypeDeviceUsedRate(monthReportRequest);
        monthReportRequest.setDataMap(map);
        monthReportRequest.setItem("memory");

        map = monthReportServiceClient.queryIdcTypeUserRate(monthReportRequest);
        monthReportRequest.setDataMap(map);
        map = monthDayReportNewServiceClient.idcTypeDeviceUsedRate(monthReportRequest);
        monthReportRequest.setDataMap(map);

        if (null != map && map.size() > 0) {
            alertMonthReportSyncDao.insertIdcType(new ArrayList(map.values()));
        }

    }

    @Override
    public void phyMonthData(int type, String month) throws Exception {
        List<Date> list = getLastMonth(month);
        Date startTime = list.get(0);
        Date endTime = list.get(1);

        Map<String, Map<String, Object>> map = Maps.newHashMap();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        MonthReportRequest monthReportRequest = new MonthReportRequest();
        monthReportRequest.setStartTime(sdf.format(startTime));
        monthReportRequest.setEndTime(sdf.format(endTime));
        monthReportRequest.setDeviceType("X86服务器");
        monthReportRequest.setItem("cpu");
        monthReportRequest.setType(type);
        //monthReportRequest.setIdcType(noIdcTypes);

        map = monthReportServiceClient.queryPhyUserRate(monthReportRequest);

        if (null != map && map.size() > 0) {
            alertMonthReportSyncDao.insertIdcType(new ArrayList(map.values()));
        }

    }

    @Override
    public void bizSystemMonthData() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date startTime = calendar.getTime();

        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date endTime = calendar.getTime();

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
        String month = sdf1.format(startTime);

        Map<String, Map<String, Object>> map = Maps.newHashMap();
        List<AlertMonthReportIdcType> idcTypeList = alertMonthReportSyncDao.queryDepartmentBymonthDay(month, null,
                null);
        List<String> bizIdList = Lists.newArrayList();
        for (AlertMonthReportIdcType idc : idcTypeList) {
            String idcType = idc.getIdcType();
            String biz = idc.getBizSystem();
            if (!bizIdList.contains(biz)) {
                bizIdList.add(biz);
            }

            String deviceType = idc.getDeviceType();
            String key = idcType + "_" + biz + "_" + deviceType;
            Map<String, Object> m = Maps.newHashMap();
            m.put("idcType", idc.getIdcType());
            m.put("cpu_max", idc.getCpu_max());
            m.put("cpu_avg", idc.getCpu_avg());
            m.put("memory_avg", idc.getMemory_avg());
            m.put("memory_max", idc.getMemory_max());
            m.put("deviceType", idc.getDeviceType());
            m.put("bizSystem", idc.getBizSystem());
            m.put("month", month);
            map.put(key, m);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        MonthReportRequest monthReportRequest = new MonthReportRequest();
        monthReportRequest.setStartTime(sdf.format(startTime));
        monthReportRequest.setEndTime(sdf.format(endTime));
        monthReportRequest.setDataMap(map);
        monthReportRequest.setDeviceType("X86服务器");
        monthReportRequest.setItem("cpu");

        map = monthDayReportNewServiceClient.bizSystemDeviceUsedRate(monthReportRequest);
        monthReportRequest.setDataMap(map);
        monthReportRequest.setItem("memory");

        map = monthDayReportNewServiceClient.bizSystemDeviceUsedRate(monthReportRequest);
        monthReportRequest.setDataMap(map);

        monthReportRequest.setDeviceType("云主机");
        monthReportRequest.setItem("cpu");
        map = monthDayReportNewServiceClient.bizSystemDeviceUsedRate(monthReportRequest);
        monthReportRequest.setDataMap(map);

        monthReportRequest.setItem("memory");
        map = monthDayReportNewServiceClient.bizSystemDeviceUsedRate(monthReportRequest);


        if (null != map && map.size() > 0) {
            Map<String, String> bizMap = utilsHelper.getDepartmentDataMapByTye(bizIdList, "bizSystem");
            List<Map<String, Object>> dataList = new ArrayList(map.values());
            for (Map<String, Object> m : dataList) {
                String biztemp = MapUtils.getString(m, "bizSystem");
                if (bizMap.containsKey(biztemp)) {
                    String name = bizMap.get(biztemp);
                    m.put("bizSystem_id", biztemp);
                    m.put("bizSystem", name);
                }
            }
            alertMonthReportSyncDao.insertDepartment(new ArrayList(map.values()));
        }

    }

    @Override
    public void netMonthData(String month) throws ParseException {
        List<Date> list = getLastMonth(month);
        Date startTime = list.get(0);
        Date endTime = list.get(1);

        /*
         * Calendar calendar = Calendar.getInstance(); calendar.add(Calendar.MONTH, -1);
         * calendar.set(Calendar.DAY_OF_MONTH, 1); calendar.set(Calendar.HOUR_OF_DAY,
         * 0); calendar.set(Calendar.MINUTE, 0); calendar.set(Calendar.SECOND, 0);
         *
         * Date startTime = calendar.getTime();
         *
         * calendar.add(Calendar.MONTH, 1); calendar.add(Calendar.DATE, -1);
         * calendar.set(Calendar.HOUR_OF_DAY, 23); calendar.set(Calendar.MINUTE, 59);
         * calendar.set(Calendar.SECOND, 59); Date endTime = calendar.getTime();
         */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        DateFormat returnd = new SimpleDateFormat("yyyy-MM");
        month = returnd.format(startTime);
        List<alertNetMonitorConfig> netList = alertMonthReportSyncDao.queryNetMoniter();

        // 组装查询数据
        Map<String, NetConfig> cmnetMap = Maps.newHashMap();
        Map<String, NetConfig> ipMap = Maps.newHashMap();

        for (alertNetMonitorConfig net : netList) {
            String deviceType = net.getDevice_type();
            String idcType = net.getIdcType();
            String ip = net.getIp();
            String port = net.getPort();
            String key = deviceType + "_" + idcType;
            NetConfig netConfig = new NetConfig();
            if (deviceType.equals(CMNET)) {
                if (cmnetMap.containsKey(key)) {
                    netConfig = cmnetMap.get(key);
                    netConfig.setIps(netConfig.getIps() + "," + ip);
                } else {
                    netConfig.setIdcType(net.getIdcType());
                    netConfig.setIps(ip);
                    netConfig.setPort(port);
                    cmnetMap.put(key, netConfig);
                }

            } else {
                if (ipMap.containsKey(key)) {
                    netConfig = ipMap.get(key);
                    netConfig.setIps(netConfig.getIps() + "," + ip);
                } else {
                    netConfig.setIdcType(net.getIdcType());
                    netConfig.setIps(ip);
                    netConfig.setPort(port);
                    ipMap.put(key, netConfig);
                }
            }

        }

        MonthReportNetRequest monthReportRequest = new MonthReportNetRequest();
        monthReportRequest.setStartTime(sdf.format(startTime));
        monthReportRequest.setEndTime(sdf.format(endTime));

        Map<String, Map<String, Object>> map = Maps.newHashMap();
        // Map<String, Map<String, Object>> map =
        // monthReportServiceClient.idcTypeDeviceCount(monthReportRequest);
        // 同步CMNET数据
        List<NetConfig> netCmnetList = new ArrayList(cmnetMap.values());
        for (NetConfig net : netCmnetList) {
            List<NetConfig> netCmnets = Lists.newArrayList();
            monthReportRequest.setDataMap(map);
            monthReportRequest.setDeviceType(CMNET);
            monthReportRequest.setItem(this.IFHIGHSPEED);
            monthReportRequest.setIdcType(net.getIdcType());
            netCmnets.add(net);
            monthReportRequest.setNetConfigs(netCmnets);
            // 带宽
            map = monthReportServiceClient.queryNetBandwidth(monthReportRequest);
            // 利用率
            monthReportRequest.setDataMap(map);
            monthReportRequest.setItem(this.ifInOctets);
            monthReportRequest.setIn(true);
            map = monthReportServiceClient.queryNetUseRatio(monthReportRequest);
            monthReportRequest.setDataMap(map);
            monthReportRequest.setItem(this.ifOutOctets);
            monthReportRequest.setIn(false);
            map = monthReportServiceClient.queryNetUseRatio(monthReportRequest);
            // 总量
            monthReportRequest.setDataMap(map);
            monthReportRequest.setItem(this.ifHCInOctets);
            monthReportRequest.setIn(true);
            map = monthReportServiceClient.queryNetUseTotal(monthReportRequest);
            monthReportRequest.setDataMap(map);
            monthReportRequest.setItem(this.ifHCOutOctets);
            monthReportRequest.setIn(false);
            map = monthReportServiceClient.queryNetUseTotal(monthReportRequest);
        }

        List<NetConfig> ipCmnetList = new ArrayList(ipMap.values());
        for (NetConfig net : ipCmnetList) {
            List<NetConfig> netCmnets = Lists.newArrayList();
            // 同步IP承载网数据
            monthReportRequest.setDataMap(map);
            monthReportRequest.setDeviceType(this.IP);
            monthReportRequest.setItem(this.IFHIGHSPEED);
            netCmnets.add(net);
            monthReportRequest.setNetConfigs(netCmnets);
            // 带宽
            map = monthReportServiceClient.queryNetBandwidth(monthReportRequest);
            // 利用率
            monthReportRequest.setDataMap(map);
            monthReportRequest.setItem(this.ifInOctets);
            monthReportRequest.setIn(true);
            map = monthReportServiceClient.queryNetUseRatio(monthReportRequest);

            monthReportRequest.setDataMap(map);
            monthReportRequest.setItem(this.ifOutOctets);
            monthReportRequest.setIn(false);
            map = monthReportServiceClient.queryNetUseRatio(monthReportRequest);

            // 总量
            monthReportRequest.setDataMap(map);
            monthReportRequest.setItem(this.ifHCInOctets);
            monthReportRequest.setIn(true);
            map = monthReportServiceClient.queryNetUseTotal(monthReportRequest);
            monthReportRequest.setDataMap(map);
            monthReportRequest.setItem(this.ifHCOutOctets);
            monthReportRequest.setIn(false);
            map = monthReportServiceClient.queryNetUseTotal(monthReportRequest);
        }

        if (null != map && map.size() > 0) {
            for (Map<String, Object> val : map.values()) {
                val.put("month", month);
            }
            alertMonthReportSyncDao.insertNetMonitorData(new ArrayList(map.values()));
        }

    }

    @Override
    public void exportBizSytemDayExcel(String day, int hisFlag) throws Exception {

        log.info("***********exportReport--begin**************");

        List<String> hList = Lists.newArrayList();
        List<String> kList = Lists.newArrayList();
        hList.add("所属资源池名称");
        hList.add("归属部门（二级）");
        hList.add("所属租户（一级）");
        hList.add("业务系统名称");
        hList.add("联系人");
        hList.add("联系电话");
        kList.add("idcType");
        kList.add("department2");
        kList.add("department1");
        kList.add("bizSystem");
        kList.add("contact");
        kList.add("phone");

        /*
         * Calendar c = Calendar.getInstance();
         *
         * int year = c.get(Calendar.YEAR); int month = c.get(Calendar.MONTH)+1; int
         * date = c.get(Calendar.DATE); if(date==1) { c.add(Calendar.MONTH, -1); year =
         * c.get(c.YEAR); month = c.get(c.MONTH) + 1; } String monthStr = month+"";
         * if(month<10) { monthStr = "0"+month; }
         */
        List<String> days = Lists.newArrayList();
        days.add(day);
        List<Map<String, Object>> dataList = fromDate(day, hisFlag);

        if (days.size() == 0) {
            throw new Exception("没有数据");
        }
        Date startTime = DateUtils.parseDate(days.get(0), new String[]{"yyyy-MM-dd"});
        Date endTime = DateUtils.parseDate(days.get(days.size() - 1), new String[]{"yyyy-MM-dd"});
        List<String> dateList = Lists.newArrayList();

        initList1(hList, kList, startTime, endTime, dateList);
        String[] headerList = hList.toArray(new String[]{});
        String[] keyList = kList.toArray(new String[]{});

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = null;
        sheet = wb.createSheet("sheet1");

        String title = day + "租户利用率数据表";
        String fileName = title + ".xlsx";
        // 计算该报表的列数
        int number = hList.size();
        int num = dateList.size() + 6;

        for (int i = 0; i < number; i++) {
            sheet.setColumnWidth(i, 3000);
        }

        // 创建单元格样式
        XSSFCellStyle cellStyle = wb.createCellStyle();

        // 指定单元格居中对齐
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        // 指定单元格垂直居中对齐
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // 指定当单元格内容显示不下时自动换行
        cellStyle.setWrapText(true);

        // cellStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
        // cellStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
        // cellStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
        // cellStyle.setBorderTop(CellStyle.BORDER_MEDIUM);

        // 设置单元格字体
        XSSFFont font = wb.createFont();
        // font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("仿宋_GB2312");
        font.setFontHeight((short) 240);
        cellStyle.setFont(font);

        XSSFRow row1 = sheet.createRow(0);
        // 设置行高
        row1.setHeight((short) 1500);

        XSSFCell rowCell1 = null;
        // 创建不同的LIST的列标题
        for (int i = 0; i < num; i++) {

            if (i > 5) {
                String titleDay = dateList.get(i - 6);
                int m = (i - 6) * 8 + 6;
                rowCell1 = row1.createCell(m);
                rowCell1.setCellStyle(cellStyle);
                String name = getDate(titleDay);// month + "月" + (i - 5) + "日";
                rowCell1.setCellValue(name);
                for (int k = 0; k < 7; k++) {

                    rowCell1 = row1.createCell(m + 1 + k);
                    rowCell1.setCellValue("");
                }

            } else {
                rowCell1 = row1.createCell(i);
                rowCell1.setCellStyle(cellStyle);
                rowCell1.setCellValue(new XSSFRichTextString(hList.get(i).toString()));
            }

        }

        int dayNum = num - 6;
        // 合并单元格
        // sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 7));
        for (int i = 0; i < dayNum; i++) {
            int k = i * 8;
            sheet.addMergedRegion(new CellRangeAddress(0, 0, k + 6, k + 13));
            if (i == dayNum - 1) {
                k = i * 8 + 8;
                sheet.addMergedRegion(new CellRangeAddress(0, 0, k + 6, k + 13));
            }
        }

        XSSFCell row2Cell = null;
        XSSFRow row2 = sheet.createRow(1);
        row2.setHeight((short) 800);
        for (int i = 0; i < number; i++) {
            row2Cell = row2.createCell(i);
            row2Cell.setCellStyle(cellStyle);
            if (i > 5) {
                String name = "裸金属";
                row2Cell.setCellValue(name);
                for (int k = 0; k < 3; k++) {
                    row2Cell = row2.createCell(i + 1 + k);
                    row2Cell.setCellValue("");
                }
                String name1 = "云主机";
                row2Cell = row2.createCell(i + 4);
                row2Cell.setCellStyle(cellStyle);
                row2Cell.setCellValue(name1);
                for (int k = 0; k < 3; k++) {
                    row2Cell = row2.createCell(i + 5 + k);
                    row2Cell.setCellValue("");
                }
                i += 7;

            } else {
                row2Cell.setCellValue(new XSSFRichTextString(""));
            }

        }
        // 合并单元格
        for (int i = 0; i < dayNum; i++) {
            int k = i * 8;
            sheet.addMergedRegion(new CellRangeAddress(1, 1, k + 6, k + 9));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, k + 10, k + 13));
            if (i == dayNum - 1) {
                k = i * 8 + 8;
                sheet.addMergedRegion(new CellRangeAddress(1, 1, k + 6, k + 9));
                sheet.addMergedRegion(new CellRangeAddress(1, 1, k + 10, k + 13));
            }
        }

        XSSFCell row3Cell = null;
        XSSFRow row3 = sheet.createRow(2);
        row3.setHeight((short) 800);
        for (int i = 0; i < number; i++) {
            row3Cell = row3.createCell(i);
            row3Cell.setCellStyle(cellStyle);
            if (i > 5) {
                String name1 = "cpu日均均值";
                String name2 = "cpu日均峰值";
                String name3 = "内存日均均值";
                String name4 = "内存日均峰值";
                String name5 = "cpu日均均值";
                String name6 = "cpu日均峰值";
                String name7 = "内存日均均值";
                String name8 = "内存日均峰值";

                row3Cell.setCellValue(name1);
                row3Cell = row3.createCell(i + 1);
                row3Cell.setCellValue(name2);
                row3Cell = row3.createCell(i + 2);
                row3Cell.setCellValue(name3);
                row3Cell = row3.createCell(i + 3);
                row3Cell.setCellValue(name4);
                row3Cell = row3.createCell(i + 4);
                row3Cell.setCellValue(name5);
                row3Cell = row3.createCell(i + 5);
                row3Cell.setCellValue(name6);
                row3Cell = row3.createCell(i + 6);
                row3Cell.setCellValue(name7);
                row3Cell = row3.createCell(i + 7);
                row3Cell.setCellValue(name8);
                i += 7;

            } else {
                row3Cell.setCellValue(new XSSFRichTextString(""));
            }

        }
        // 合并单元格
        for (int i = 0; i < 6; i++) {
            sheet.addMergedRegion(new CellRangeAddress(0, 2, i, i));

        }
        List<String> bizIdList = dataList.stream().map(item -> MapUtils.getString(item, "bizSystem"))
                .collect(Collectors.toList());
        Map<String, String> bizMap = utilsHelper.getDepartmentDataMapByTye(bizIdList, "bizSystem");
        for (int i = 0; i < dataList.size(); i++) {
            int k = i + 3;
            Map<String, Object> map = dataList.get(i);
            XSSFRow row = sheet.createRow(k);
            row3.setHeight((short) 800);
            XSSFCell rowCell = null;
            String department1 = "";
            String department2 = "";
            String bizSystem = "";

            if (map.get("bizSystem") != null && map.get("bizSystem").toString() != "") {
                bizSystem = map.get("bizSystem").toString();
                if (bizMap.containsKey(bizSystem)) {
                    bizSystem = bizMap.get(bizSystem);
                    map.put("bizSystem", bizSystem);
                }
                /*
                 * Map<String, String> biz =
                 * cmdbAlertRestfulClient.getDepartmentInfoByBizSystem(bizSystem); if (null !=
                 * biz) { department1 = StringUtils.isEmpty(biz.get("department1")) ? "" :
                 * biz.get("department1"); department2 =
                 * StringUtils.isEmpty(biz.get("department2")) ? "" : biz.get("department2"); }
                 */

            }

            for (int j = 0; j < 6; j++) {
                rowCell = row.createCell(j);
                if (j == 0) {
                    rowCell.setCellValue(new XSSFRichTextString(map.get("idcType").toString()));
                } else if (j == 1) {
                    rowCell.setCellValue(new XSSFRichTextString(department2));
                } else if (j == 2) {
                    rowCell.setCellValue(new XSSFRichTextString(department1));
                } else if (j == 3) {
                    rowCell.setCellValue(new XSSFRichTextString(bizSystem));
                } else if (j == 4) {
                    rowCell.setCellValue(new XSSFRichTextString(""));
                } else if (j == 5) {
                    rowCell.setCellValue(new XSSFRichTextString(""));
                }
            }
            for (int j = 0; j < dateList.size(); j++) {
                int m = 8 * j;
                String timeStr = dateList.get(j);
                rowCell = row.createCell(m + 6);
                StringBuffer sb = new StringBuffer();
                sb.append(timeStr).append("_").append("X86服务器").append("_").append("cpu").append("_avg");
                String key = sb.toString();
                rowCell.setCellValue(new XSSFRichTextString(map.get(key) == null ? "" : map.get(key).toString()));

                rowCell = row.createCell(m + 7);
                sb.setLength(0);
                sb.append(timeStr).append("_").append("X86服务器").append("_").append("cpu").append("_max");
                key = sb.toString();
                rowCell.setCellValue(new XSSFRichTextString(map.get(key) == null ? "" : map.get(key).toString()));

                rowCell = row.createCell(m + 8);
                sb.setLength(0);
                sb.append(timeStr).append("_").append("X86服务器").append("_").append("memory").append("_avg");
                key = sb.toString();
                rowCell.setCellValue(new XSSFRichTextString(map.get(key) == null ? "" : map.get(key).toString()));

                rowCell = row.createCell(m + 9);
                sb.setLength(0);
                sb.append(timeStr).append("_").append("X86服务器").append("_").append("memory").append("_max");
                key = sb.toString();
                rowCell.setCellValue(new XSSFRichTextString(map.get(key) == null ? "" : map.get(key).toString()));

                rowCell = row.createCell(m + 10);
                sb.setLength(0);
                sb.append(timeStr).append("_").append("云主机").append("_").append("cpu").append("_avg");
                key = sb.toString();
                rowCell.setCellValue(new XSSFRichTextString(map.get(key) == null ? "" : map.get(key).toString()));

                rowCell = row.createCell(m + 11);
                sb.setLength(0);
                sb.append(timeStr).append("_").append("云主机").append("_").append("cpu").append("_max");
                key = sb.toString();
                rowCell.setCellValue(new XSSFRichTextString(map.get(key) == null ? "" : map.get(key).toString()));

                rowCell = row.createCell(m + 12);
                sb.setLength(0);
                sb.append(timeStr).append("_").append("云主机").append("_").append("memory").append("_avg");
                key = sb.toString();
                rowCell.setCellValue(new XSSFRichTextString(map.get(key) == null ? "" : map.get(key).toString()));

                rowCell = row.createCell(m + 13);
                sb.setLength(0);
                sb.append(timeStr).append("_").append("云主机").append("_").append("memory").append("_max");
                key = sb.toString();
                rowCell.setCellValue(new XSSFRichTextString(map.get(key) == null ? "" : map.get(key).toString()));

            }
        }

        // ExportExcelUtil eeu = new ExportExcelUtil();
        // eeu.exportExcel(wb, 0, fileName, headerList, dataList, keyList);
        ByteArrayOutputStream ops = null;
        ByteArrayInputStream in = null;
        try {
            ops = new ByteArrayOutputStream();
            wb.write(ops);
            byte[] b = ops.toByteArray();
            in = new ByteArrayInputStream(b);
            ftpService.uploadtoFTPNew(fileName, in, ftpFilePath);
            ops.flush();
        } catch (Exception e) {
            log.error("导出excel失败，失败原因：", e);
        } finally {
            IOUtils.closeQuietly(wb);
            IOUtils.closeQuietly(ops);
            IOUtils.closeQuietly(in);
        }
        log.info("***syncIdcTypeDayNew--end**************");

    }

    private String getDate(String titleDay) {
        String[] days = titleDay.split("-");
        int month = Integer.parseInt(days[1]);
        int day = Integer.parseInt(days[2]);
        return month + "月" + day + "日";
    }

    private List<Map<String, Object>> fromDate(String day, int hisFlag) {
        List<Map<String, Object>> daysData = alertMonthReportSyncDao.queryDayBizSystem(day, null, hisFlag);
        Map<String, Map<String, Object>> dataMap = Maps.newLinkedHashMap();
        formMapVal(daysData, dataMap, false);
        return new ArrayList(dataMap.values());
    }

    private void formMapVal(List<Map<String, Object>> daysData, Map<String, Map<String, Object>> dataMap,
                            boolean flag) {
        for (Map<String, Object> day : daysData) {
            if (null == day.get("bizSystem")) {
                continue;
            }
            String biz = day.get("bizSystem").toString();
            String idcTypeVal = day.get("idcType").toString();
            String deviceType = day.get("deviceType").toString();
            String cpu_avg = day.get("cpu_avg") == null ? "" : day.get("cpu_avg").toString();
            String cpu_max = day.get("cpu_max") == null ? "" : day.get("cpu_max").toString();
            String memory_avg = day.get("memory_avg") == null ? "" : day.get("memory_avg").toString();
            String memory_max = day.get("memory_max") == null ? "" : day.get("memory_max").toString();
            String dayVal = day.get("day") == null ? "" : day.get("day").toString();
            Map<String, Object> val = new HashMap<String, Object>();
            String key = biz + "_" + idcTypeVal;
            if (dataMap.containsKey(key)) {
                val = dataMap.get(key);
            } else {
                val.put("idcType", idcTypeVal);
                val.put("bizSystem", biz);
                dataMap.put(key, val);
            }
            val.put("deviceType", deviceType);
            StringBuffer sb = new StringBuffer();
            if (flag) {
                sb.append("日均均值").append("_").append(deviceType).append("_");
            } else {
                sb.append(dayVal).append("_").append(deviceType).append("_");
            }
            val.put(sb.toString() + "cpu_avg", cpu_avg);
            val.put(sb.toString() + "cpu_max", cpu_max);
            val.put(sb.toString() + "memory_avg", memory_avg);
            val.put(sb.toString() + "memory_max", memory_max);
        }

    }

    private void initList1(List<String> hList, List<String> kList, Date startTime, Date endTime,
                           List<String> dateList) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);

        DateFormat returndf = new SimpleDateFormat("yyyy-MM-dd");

        while (calendar.getTime().getTime() <= endTime.getTime()) {
            String timeStr = returndf.format(calendar.getTime());
            dateList.add(timeStr);
            StringBuffer sb = new StringBuffer();
            sb.append(timeStr).append("_").append("X86服务器").append("_").append("cpu");
            hList.add(sb.toString() + "_日均均值");
            hList.add(sb.toString() + "_日均峰值");
            kList.add(sb.toString() + "_avg");
            kList.add(sb.toString() + "_max");

            sb.setLength(0);
            sb.append(timeStr).append("_").append("X86服务器").append("_").append("内存");
            hList.add(sb.toString() + "_日均均值");
            hList.add(sb.toString() + "_日均峰值");
            kList.add(sb.toString() + "_avg");
            kList.add(sb.toString() + "_max");

            sb.setLength(0);
            sb.append(timeStr).append("_").append("云主机").append("_").append("cpu");
            hList.add(sb.toString() + "_日均均值");
            hList.add(sb.toString() + "_日均峰值");
            kList.add(sb.toString() + "_avg");
            kList.add(sb.toString() + "_max");

            sb.setLength(0);
            sb.append(timeStr).append("_").append("云主机").append("_").append("内存");
            hList.add(sb.toString() + "_日均均值");
            hList.add(sb.toString() + "_日均峰值");
            kList.add(sb.toString() + "_avg");
            kList.add(sb.toString() + "_max");

            calendar.add(Calendar.DATE, +1);

        }

    }

    @Override
    public void exportIdcTypeIpDayExcel(String day) throws Exception {

        MonthBizSystemDayRequest monthRequest = new MonthBizSystemDayRequest();
        monthRequest.setDay(day);
        List<Map<String, Object>> data = MonthDayReportNewServiceClient.getBizSytemDataByDayReturnMap(monthRequest);

        String[] headerList = {"资源池", "ip", "tag", "设备类型", "cpu日均均值", "cpu日均峰值", "内存日均均值", "内存日均峰值"};
        String[] keyList = {"idcType", "ip", "tag", "deviceType", "cpu_avg", "cpu_max", "memory_avg", "memory_max"};
        String fileName = day + "主机设备利用率日报.xlsx";
        ExportExcelUtil eeu = new ExportExcelUtil();
        Workbook book = new SXSSFWorkbook(128);
        // book.setCompressTempFiles(true);
        eeu.exportExcel(book, 0, fileName, headerList, data, keyList);
        ByteArrayOutputStream ops = null;
        ByteArrayInputStream in = null;
        try {
            ops = new ByteArrayOutputStream();
            book.write(ops);
            byte[] b = ops.toByteArray();
            in = new ByteArrayInputStream(b);
            ftpService.uploadtoFTPNew(fileName, in, ftpFilePath);
            ops.flush();
        } catch (Exception e) {
            log.error("导出excel失败，失败原因：", e);
        } finally {
            IOUtils.closeQuietly(book);
            IOUtils.closeQuietly(ops);
            IOUtils.closeQuietly(in);
        }
    }

    @Override
    public void syncBizSytemDayByMinite(AlertMonthReportVo monthReportRequest) throws Exception {

        String idcType = monthReportRequest.getIdcType();
        String days = monthReportRequest.getDays();
        log.info("*AlertMonthDayizImpl-syncBizSytemDayByMinite--begin:{},{}", idcType, days);
        List<String> dateList = Lists.newArrayList();
        // initList1(hList, kList, startTime, endTime, dateList);
        // days = "normal";

        String[] daysArr = days.split(",");
        dateList = Arrays.asList(daysArr);

        Map<String, Map<String, Object>> dataMap = Maps.newHashMap();
        for (int i = 0; i < dateList.size(); i++) {
            MonthReportRequest request = new MonthReportRequest();
            request.setDataMap(dataMap);
            request.setDeviceType("X86服务器");
            request.setPod(monthReportRequest.getPod());
            String date = "";

            date = dateList.get(i);

            if (date.isEmpty()) {
                continue;
            }
            // String dataEnd = dateList.get(i+4);
            request.setEndTime(date + " 23:59:59");
            request.setStartTime(date + " 00:00:00");

            request.setIdcType(idcType);

            dataMap = MonthDayReportServiceClient.bizSystemUsedRateByDay(request);
            if (dataMap.size() > 0) {
                List<Map<String, Object>> map = new ArrayList(dataMap.values());
                utilsHelper.formListBizIdData(map, null);
                alertMonthReportSyncDao.insertIdcTypeBizSystemDay(map);
                dataMap.clear();
            }

            request.setDeviceType("云主机");
            dataMap = MonthDayReportServiceClient.bizSystemUsedRateByDay(request);
            if (dataMap.size() > 0) {
                List<Map<String, Object>> map = new ArrayList(dataMap.values());
                utilsHelper.formListBizIdData(map, null);
                alertMonthReportSyncDao.insertIdcTypeBizSystemDay(map);
                dataMap.clear();
            }

        }

        log.info("*AlertMonthDayizImpl-syncBizSytemDayByMinite--end*");

    }

    @Override
    public void syncDepartment2Data(int type, String month) throws Exception {
        List<Date> list = getLastMonth(month);
        Date startTime = list.get(0);
        Date endTime = list.get(1);

        Map<String, Map<String, Object>> map = Maps.newHashMap();

        boolean department1Flag = false;
        if (type == 1) {
            department1Flag = true;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        MonthReportRequest monthReportRequest = new MonthReportRequest();
        monthReportRequest.setType(type);
        monthReportRequest.setStartTime(sdf.format(startTime));
        monthReportRequest.setEndTime(sdf.format(endTime));
        monthReportRequest.setDataMap(map);
        monthReportRequest.setItem("cpu");

        map = monthReportServiceClient.queryDepartment2UserRate(monthReportRequest, department1Flag);
        monthReportRequest.setDataMap(map);

        monthReportRequest.setItem("memory");

        map = monthReportServiceClient.queryDepartment2UserRate(monthReportRequest, department1Flag);

        if (null != map && map.size() > 0) {
            List<Map<String, Object>> list1 = new ArrayList(map.values());
            List<String> bizIdList = new ArrayList(map.keySet());
            Map<String, String> dataMap = utilsHelper.getDepartmentDataMapByTye(bizIdList, "department");
            for (Map<String, Object> m : list1) {
                String key = MapUtils.getString(m, "department2");
                if (dataMap.containsKey(key)) {
                    m.put("department2_id", key);
                    m.put("department2", dataMap.get(key));
                }
            }
            alertMonthReportSyncDao.insertDepartment2(list1);
        }

    }

    @Override
    public void syncBizSystem2Data(int type, String month) throws Exception {
        List<Date> list = getLastMonth(month);
        Date startTime = list.get(0);
        Date endTime = list.get(1);

        Map<String, Map<String, Object>> map = Maps.newHashMap();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        MonthReportRequest monthReportRequest = new MonthReportRequest();
        monthReportRequest.setType(type);
        monthReportRequest.setStartTime(sdf.format(startTime));
        monthReportRequest.setEndTime(sdf.format(endTime));
        monthReportRequest.setDataMap(map);
        monthReportRequest.setDeviceType("X86服务器");

        map = monthReportServiceClient.queryBizSystemUserRate(monthReportRequest);
        monthReportRequest.setDataMap(map);

        monthReportRequest.setDeviceType("云主机");

        map = monthReportServiceClient.queryBizSystemUserRate(monthReportRequest);
        monthReportRequest.setDataMap(map);

        if (null != map && map.size() > 0) {
            List<Map<String, Object>> list1 = new ArrayList(map.values());
            List<String> bizIdList = list1.stream().map(item -> MapUtils.getString(item, "bizSystem")).collect(Collectors.toList());
            utilsHelper.formListBizData(list1, bizIdList);
            /*
             * for (Map<String, Object> m : list1) { String bizSystem =
             * m.get("bizSystem").toString(); try { Map<String, String> biz =
             * cmdbAlertRestfulClient.getDepartmentInfoByBizSystem(bizSystem); if (null !=
             * biz) { m.put("department1", StringUtils.isEmpty(biz.get("department1")) ? ""
             * : biz.get("department1")); m.put("department2",
             * StringUtils.isEmpty(biz.get("department2")) ? "" : biz.get("department2")); }
             * } catch (Exception e) { log.error("查询业务系统数据报错：bizSystem:{}", bizSystem); }
             *
             * }
             */

            alertMonthReportSyncDao.insertDepartment2(list1);
        }

    }

    @Override
    public List<Map<String, Object>> getData(String month, String idcType, int hisFlag) throws Exception {
        List<Map<String, Object>> daysData = alertMonthReportSyncDao.queryDayBizSystem(month, null, hisFlag);
        List<Map<String, Object>> monthData = alertMonthReportSyncDao.queryDayBizSystemMonth(month, null, hisFlag);
        Map<String, Map<String, Object>> X86dataMap = Maps.newLinkedHashMap();
        Map<String, Map<String, Object>> vmdataMap = Maps.newLinkedHashMap();
        List<String> idcs = null;
        if (StringUtils.isNotBlank(idcType)) {
            idcs = Arrays.asList(idcType.split(","));
        }

        /*
         * Iterator<Map<String, Object>> it = daysData.iterator(); while(it.hasNext()){
         * Map<String, Object> x = it.next(); if(x.equals("del")){ it.remove(); } }
         */
        List<String> bizIdList = monthData.stream().map(item -> MapUtils.getString(item, "bizSystem"))
                .collect(Collectors.toList());
        Map<String, String> bizMap = utilsHelper.getDepartmentDataMapByTye(bizIdList, "bizSystem");
        for (Map<String, Object> map : daysData) {
            String idcType1 = map.get("idcType").toString();
            if (null != idcs && !(idcs.contains(idcType1))) {
                continue;
            }
            String bizSystem = map.get("bizSystem").toString();

            String deviceType = map.get("deviceType").toString();
            String key = idcType1 + bizSystem;
            if (deviceType.equals("云主机")) {
                if (!vmdataMap.containsKey(key)) {
                    Map<String, Object> s = Maps.newHashMap();
                    s.put("idcType", idcType1);
                    s.put("bizSystem", bizSystem);
                    vmdataMap.put(key, s);

                }
            } else {
                if (!X86dataMap.containsKey(key)) {
                    Map<String, Object> s = Maps.newHashMap();
                    s.put("idcType", idcType1);
                    s.put("bizSystem", bizSystem);
                    X86dataMap.put(key, s);

                }
            }

        }
        for (Map<String, Object> map : daysData) {
            String idcType1 = map.get("idcType").toString();
            if (null != idcs && !(idcs.contains(idcType1))) {
                continue;
            }

            String bizSystem = map.get("bizSystem").toString();
            String day = map.get("day").toString();
            String cpu_max = map.get("cpu_max") == null ? "" : map.get("cpu_max").toString();
            String cpu_avg = map.get("cpu_avg") == null ? "" : map.get("cpu_avg").toString();
            String memory_avg = map.get("memory_avg") == null ? "" : map.get("memory_avg").toString();
            String memory_max = map.get("memory_max") == null ? "" : map.get("memory_max").toString();
            String deviceType = map.get("deviceType").toString();
            String key = idcType1 + bizSystem;
            Map<String, Object> s = X86dataMap.get(key);
            if (deviceType.equals("云主机")) {
                s = vmdataMap.get(key);
            }
            if (null == s) {
                continue;
            }
            s.put(day + deviceType + "cpuAvg", cpu_avg);
            s.put(day + deviceType + "cpuMax", cpu_max);
            s.put(day + deviceType + "memoryAvg", memory_avg);
            s.put(day + deviceType + "memoryMax", memory_max);

            if (bizMap.containsKey(bizSystem)) {
                String bizName = bizMap.get(bizSystem);
                map.put("bizSystem", bizName);
                s.put("bizSystem", bizName);
            }

        }

        for (Map<String, Object> map : monthData) {
            String idcType1 = map.get("idcType").toString();
            if (null != idcs && !(idcs.contains(idcType1))) {
                continue;
            }
            String bizSystem = map.get("bizSystem").toString();

            String cpu_max = map.get("cpu_max") == null ? "" : map.get("cpu_max").toString();
            String cpu_avg = map.get("cpu_avg") == null ? "" : map.get("cpu_avg").toString();
            String memory_avg = map.get("memory_avg") == null ? "" : map.get("memory_avg").toString();
            String memory_max = map.get("memory_max") == null ? "" : map.get("memory_max").toString();
            String deviceType = map.get("deviceType").toString();
            String key = idcType1 + bizSystem;
            Map<String, Object> s = X86dataMap.get(key);
            if (deviceType.equals("云主机")) {
                s = vmdataMap.get(key);
            }
            if (null == s) {
                continue;
            }
            s.put(month + deviceType + "cpuAvg", cpu_avg);
            s.put(month + deviceType + "cpuMax", cpu_max);
            s.put(month + deviceType + "memoryAvg", memory_avg);
            s.put(month + deviceType + "memoryMax", memory_max);

            if (bizMap.containsKey(bizSystem)) {
                String bizName = bizMap.get(bizSystem);
                map.put("bizSystem", bizName);
                s.put("bizSystem", bizName);
            }

        }

        List<String> hList = com.google.common.collect.Lists.newArrayList();
        List<String> xcpuAhList = com.google.common.collect.Lists.newArrayList();
        List<String> xcpumhList = com.google.common.collect.Lists.newArrayList();
        List<String> xmahList = com.google.common.collect.Lists.newArrayList();
        List<String> xmmhList = com.google.common.collect.Lists.newArrayList();
        List<String> vmmhList = com.google.common.collect.Lists.newArrayList();
        List<String> vmahList = com.google.common.collect.Lists.newArrayList();
        List<String> vcpuahList = com.google.common.collect.Lists.newArrayList();
        List<String> vcpumhList = com.google.common.collect.Lists.newArrayList();
        List<String> kList = com.google.common.collect.Lists.newArrayList();
        hList.add("资源池");
        hList.add("业务系统");
        kList.add("idcType");
        kList.add("bizSystem");
        xcpuAhList.addAll(kList);
        xcpumhList.addAll(kList);
        xmahList.addAll(kList);
        xmmhList.addAll(kList);
        vmmhList.addAll(kList);
        vmahList.addAll(kList);
        vcpuahList.addAll(kList);
        vcpumhList.addAll(kList);

        int days = getMonthDays(month) + 1;
        for (int i = 1; i < days; i++) {
            String s = month + "-" + i;

            if (i < 10) {
                s = month + "-0" + i;
            }
            hList.add(s);
            xcpuAhList.add(s + "X86服务器cpuAvg");
            xcpumhList.add(s + "X86服务器cpuMax");
            xmahList.add(s + "X86服务器memoryAvg");
            xmmhList.add(s + "X86服务器memoryMax");
            vmmhList.add(s + "云主机memoryMax");
            vmahList.add(s + "云主机memoryAvg");
            vcpuahList.add(s + "云主机cpuAvg");
            vcpumhList.add(s + "云主机cpuMax");
            /*
             * kList.add(s+"X86服务器cpuMax"); kList.add(s+"X86服务器memoryAvg");
             * kList.add(s+"X86服务器memoryMax"); kList.add(s+"云主机cpuAvg");
             * kList.add(s+"云主机cpuMax"); kList.add(s+"云主机memoryAvg");
             * kList.add(s+"云主机memoryMax");
             */
        }
        hList.add("月均值");
        // kList.add(month+"X86服务器cpuAvg");
        xcpuAhList.add(month + "X86服务器cpuAvg");
        xcpumhList.add(month + "X86服务器cpuMax");
        xmahList.add(month + "X86服务器memoryAvg");
        xmmhList.add(month + "X86服务器memoryMax");
        vmmhList.add(month + "云主机memoryMax");
        vmahList.add(month + "云主机memoryAvg");
        vcpuahList.add(month + "云主机cpuAvg");
        vcpumhList.add(month + "云主机cpuMax");

        String[] headerList = hList.toArray(new String[]{});

        String title = "tenant_utilization_export_list.xlsx";

        List<Map<String, Object>> valuelist = new ArrayList(X86dataMap.values());

        List<Map<String, Object>> vmValuelist = new ArrayList(vmdataMap.values());

        // excel constuct
        ExportExcelUtil eeu = new ExportExcelUtil();
        Workbook book = new SXSSFWorkbook(128);
        eeu.exportExcel(book, 0, "裸金属cpu均值", headerList, valuelist, xcpuAhList.toArray(new String[]{}));
        eeu.exportExcel(book, 1, "裸金属cpu峰值", headerList, valuelist, xcpumhList.toArray(new String[]{}));
        eeu.exportExcel(book, 2, "裸金属内存均值", headerList, valuelist, xmahList.toArray(new String[]{}));
        eeu.exportExcel(book, 3, "裸金属内存峰值", headerList, valuelist, xmmhList.toArray(new String[]{}));
        eeu.exportExcel(book, 4, "云主机cpu均值", headerList, vmValuelist, vcpuahList.toArray(new String[]{}));
        eeu.exportExcel(book, 5, "云主机cpu峰值", headerList, vmValuelist, vcpumhList.toArray(new String[]{}));
        eeu.exportExcel(book, 6, "云主机内存均值", headerList, vmValuelist, vmahList.toArray(new String[]{}));
        eeu.exportExcel(book, 7, "云主机内存峰值", headerList, vmValuelist, vmmhList.toArray(new String[]{}));
        ByteArrayOutputStream ops = null;
        ByteArrayInputStream in = null;
        try {
            ops = new ByteArrayOutputStream();
            book.write(ops);
            byte[] b = ops.toByteArray();
            in = new ByteArrayInputStream(b);
            ftpService.uploadtoFTPNew(month + title, in, ftpFilePath);
            ops.flush();
        } catch (Exception e) {
            log.error("导出excel失败，失败原因：", e);
        } finally {
            // IOUtils.closeQuietly(book);
            IOUtils.closeQuietly(ops);
            IOUtils.closeQuietly(in);
        }

        return new ArrayList(X86dataMap.values());
    }

    public int getMonthDays(String month) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(month));
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    @Override
    //resourceId对于好几个ip
    public void scanDeviceMonitorData(String idcType, String idcTypeName, String date) {
        log.info("**scanDeviceMonitorData--begin**************");
        if (StringUtils.isNotBlank(this.idcTypeDefault)) {
            List<String> idcTypes = new ArrayList<>(Arrays.asList(this.idcTypeDefault.split(",")));
            if (!idcTypes.contains(idcTypeName)) {
                return;
            }
        }

        Map<String, Object> params = Maps.newHashMap();
        //查询设备
        int sizeNum = this.pageSize;
        params.put("pageNo", 1);
        params.put("pageSize", sizeNum);
        params.put("idcType", idcTypeName);
        params.put("monitorStatus", "是");
        Date from = new Date();
        PageResponse<Map<String, Object>> list = cmdbHelper.getMonitorDeviceList(params);
        Date to = new Date();
        log.info("**********耗时：{}", (to.getTime() - from.getTime()) / 1000);
        int count = list.getCount();
        if (count == 0) {
            return;
        }
        //count = 4;
        List<Map<String, Object>> listData = list.getResult();

        // 查询还存在的告警
        /*
         * AlertsV2 alertQuery = new AlertsV2();
         * alertQuery.setSource(Constants.NULL_MONITOR_SOURCE);
         * alertQuery.setIdcType(idcTypeName); List<AlertsDTOV2> alerts =
         * alertsBizV2.select(alertQuery);
         */
        if (StringUtils.isBlank(date)) {
            date = com.aspire.mirror.alert.server.util.DateUtils.getPastDate(new Date(), -1,
                    new SimpleDateFormat("yyyy-MM-dd"));
        }

        try {

            /*
             * List<AlertFieldRequestDTO> alertFieldList = alertHandleHelper
             * .getModelField(AlertConfigConstants.REDIS_MODEL_ALERT);
             */
            String dateFilnal = date;


            FactorialCalculator factorialCalculator = new FactorialCalculator(params, 1, idcTypeName
                    , dateFilnal, listData);
            taskExecutor.execute(factorialCalculator);
            if (count > sizeNum) {
                int size = count / sizeNum;
                int val = count % sizeNum;
                if (val != 0) {
                    size++;
                }
                for (int i = 2; i <= size; i++) {
                    final int num = i;
                    FactorialCalculator factorialCalculator1 = new FactorialCalculator(params, num, idcTypeName
                            , dateFilnal, null);
                    taskExecutor.execute(factorialCalculator1);
                }
            }

            // 消警
            //revokeAlerts(alerts, alertFieldList);
        } catch (Exception e) {
            log.error("**资源池性能扫描报错**************", e);
        }

        log.info("**scanDeviceMonitorData--end**************");
    }



    /*
     * private void handle(List<Future<Object>> resultList,ThreadPoolExecutor
     * service) { do { for (int i = 0; i < resultList.size(); i++) { Future<Object>
     * result = resultList.get(i); System.out.printf("Task %d : %s \n", i,
     * result.isDone()); } try { TimeUnit.MILLISECONDS.sleep(50); } catch
     * (InterruptedException e) { //e.printStackTrace(); LOGGER.log(Level.WARN,
     * "Interrupted!", e); } } while (service.getCompletedTaskCount() <
     * resultList.size()); }
     */


    public class FactorialCalculator implements Runnable {

        private Map<String, Object> params;
        private String dateFilnal;
        private int num;
        private String idcTypeName;
        private List<Map<String, Object>> listData;

        public FactorialCalculator(Map<String, Object> params, int num, String idcTypeName
                , String dateFilnal, List<Map<String, Object>> listData) {
            this.params = params;
            this.idcTypeName = idcTypeName;
            this.num = num;
            this.dateFilnal = dateFilnal;
            this.listData = listData;
        }

        public void run() {
            try {
                if (num == 1) {
                    handleData(listData, dateFilnal, null, null, idcTypeName);
                } else {
                    params.put("pageNo", num);
                    PageResponse<Map<String, Object>> list1 = cmdbHelper.getMonitorDeviceList(params);
                    handleData(list1.getResult(), dateFilnal, null, null, idcTypeName);
                }
            } catch (Exception e) {
                log.error("设备监控数据稽核失败", e);
            }

        }
    }

    /**
     * 处理数据
     *
     * @param listData
     * @param date
     * @param alerts
     * @param alertFieldList
     */
    public void handleData(List<Map<String, Object>> listData, String date,
                           List<AlertsV2Vo> alerts, List<AlertFieldVo> alertFieldList, String idcType) {
        if (!CollectionUtils.isEmpty(listData)) {
            Date curDate = new Date();
            List<String> idList = Lists.newArrayList();
            Map<String, Map<String, Object>> map = Maps.newHashMap();
            for (Map<String, Object> item : listData) {
                String ip = MapUtils.getString(item, "ip");
                if (StringUtils.isBlank(ip)) {
                    continue;
                }
                String id = MapUtils.getString(item, "instance_id");
                if (!idList.contains(id)) {
                    idList.add(id);
                }
                map.put(id, item);
            }
            if (CollectionUtils.isEmpty(idList)) {
                return;
            }
            // 获取消警数据
            //getRrevokeAlerts(alerts, idList);

            HistorySearchRequest request = new HistorySearchRequest();
            request.setIdList(idList);
            request.setStartTime(date + " 00:00:00");
            request.setEndTime(date + " 23:59:59");
            //request.setKpi("cpu");
            request.setIdcType(idcType);
            List<String> nullList = historyServiceClient.getNullValueDeviceList(request);

            if (!CollectionUtils.isEmpty(nullList)) {
                List<Map<String, Object>> mapList = Lists.newArrayList();
                for (String id : nullList) {
                    Map<String, Object> device = map.get(id);
                    if (null == device) {
                        continue;
                    }
                    String ip = MapUtils.getString(device, "ip");

                    Map<String, Object> dataMap = Maps.newHashMap();
                    dataMap.put("resourceId", id);
                    dataMap.put("idcType", device.get("idcType"));
                    dataMap.put("ip", ip);
                    dataMap.put("pod_name", device.get("pod_name"));
                    dataMap.put("roomId", device.get("roomId"));
                    dataMap.put("bizSystem", device.get("bizSystem"));
                    dataMap.put("suyanUuid", device.get("suyan_uuid"));
                    dataMap.put("create_time", new Date());
                    dataMap.put("date", date);
                    dataMap.put("device_type", device.get("device_type"));
                    mapList.add(dataMap);

                    /*
                     * AlertsDTOV2 dto = new AlertsDTOV2();
                     * dto.setAlertLevel(Constants.NULL_MONITOR_LEVEL);
                     * dto.setMoniIndex(String.format(Constants.NULL_MONITOR_MONI_INDEX, ip));
                     * dto.setCurMoniTime(curDate); dto.setAlertStartTime(curDate);
                     * dto.setDeviceIp(ip); dto.setAlertType(AlertsDTOV2.ALERT_ACTIVE);
                     * dto.setMoniObject(Constants.NULL_MONITOR_MONI_OBJECT);
                     * dto.setSource(Constants.NULL_MONITOR_SOURCE);
                     * dto.setIdcType(device.get("idcType")==null?"":device.get("idcType").toString(
                     * )); dto.setObjectType(AlertsDTOV2.OBJECT_TYPE_DEVICE);
                     *
                     * ObjectMapper objectMapper = new ObjectMapper(); String jsonString = "{}"; try
                     * { jsonString = objectMapper.writeValueAsString(dto); } catch
                     * (JsonProcessingException e) { } JSONObject alertJson =
                     * JSONObject.parseObject(jsonString);
                     *
                     * cmdbHelper.queryDeviceForAlertV2(alertJson, alertFieldList,
                     * alertHandleHelper.getModelField(AlertConfigConstants.
                     * REDIS_MODEL_DEVICE_INSTANCE)); try { //
                     * logger.info("alertHandleHelper.handleAlert:{}",JSON.toJSONString(dto)); //
                     * 发告警 alertHandleHelper.handleAlert(dto, alertJson, alertFieldList); } catch
                     * (Exception e) { log.error("error {}", e); }
                     */
                }

                if (mapList.size() > 0) {
                    alertRestfulDao.insertDeviceAlerts(mapList);
                }
            }

        }
    }

    /**
     * 去掉还存在的告警
     *
     * @param alerts
     * @param idList
     */
    void getRrevokeAlerts(List<AlertsV2Vo> alerts, List<String> idList) {
        Iterator<AlertsV2Vo> it = alerts.iterator();
        while (it.hasNext()) {
            AlertsV2Vo a = it.next();
            String deviceId = a.getDeviceId();
            if (idList.contains(deviceId)) {
                it.remove();
            }
        }
    }

    /**
     * 消警
     *
     * @param alerts
     * @param alertFieldList
     */
    void revokeAlerts(List<AlertsV2Vo> alerts, List<AlertFieldVo> alertFieldList) {
        for (AlertsV2Vo dto : alerts) {
            dto.setAlertType(AlertsV2Vo.ALERT_REVOKE);
//			ObjectMapper objectMapper = new ObjectMapper();
//			String jsonString = "{}";
//			try {
//				jsonString = objectMapper.writeValueAsString(dto);
//			} catch (JsonProcessingException e) {
//			}
//			JSONObject alertJson = JSONObject.parseObject(jsonString);
//
//			cmdbHelper.queryDeviceForAlertV2(alertJson, alertFieldList,
//					alertHandleHelper.getModelField(AlertConfigConstants.REDIS_MODEL_DEVICE_INSTANCE));
            try {
                // logger.info("alertHandleHelper.handleAlert:{}",JSON.toJSONString(dto));
                // 发告警
                alertHandleHelper.handleAlert(dto);
            } catch (Exception e) {
                log.error("error {}", e);
            }
        }

    }

    /*
     * 按天统计资源池利用率
     */
    @Override
    public void scanIdcTypePerformanceData(String idcType, String idcTypeName, String date) throws Exception {
        log.info("**scanIdcTypePerformanceData-impl-begin**************");
        if (StringUtils.isNotBlank(this.idcTypeDefault)) {
            List<String> idcTypes = new ArrayList<>(Arrays.asList(this.idcTypeDefault.split(",")));
            if (null != idcTypeName && !idcTypes.contains(idcTypeName)) {
                return;
            }
        }

        if (StringUtils.isBlank(date)) {
            date = com.aspire.mirror.alert.server.util.DateUtils.getPastDate(new Date(), -1,
                    new SimpleDateFormat("yyyy-MM-dd"));
        }
        if (idcType.equals("all")) {
            getAllIdcTypePerformanceData(date);
            return;
        }
        HistorySearchRequest request = new HistorySearchRequest();
        request.setStartTime(date + " 00:00:00");
        request.setEndTime(date + " 23:59:59");
        // request.setKpi("cpu");
        request.setIdcType(idcTypeName);
        Map<String, Map<String, Object>> map = historyServiceClient.getIdcTypePerformanceData(request);
        for (Map.Entry<String, Map<String, Object>> en : map.entrySet()) {
            Map<String, Object> m = en.getValue();
            m.put("day", date);
            m.put("idcType", idcTypeName);
        }
        if (null != map && map.size() > 0) {
            alertRestfulDao.batchInsertIdcTypePerformance(new ArrayList(map.values()));
        }
        log.info("**scanIdcTypePerformanceData-impl-end**************");
    }

    public void getAllIdcTypePerformanceData(String date) {
        List<Map<String, Object>> list = alertRestfulDao.getAllIdcTypePerformanceData(date);
        for (Map<String, Object> m : list) {
            m.put("idcType", "all");
            m.put("day", date);
            long sum = Long.parseLong(m.get("fifteen_count").toString()) + Long.parseLong(m.get("thirty_count").toString())
                    + Long.parseLong(m.get("sixty_count").toString())
                    + Long.parseLong(m.get("eighty_five_count").toString())
                    + Long.parseLong(m.get("hundred_count").toString());
            float sumRatio = 0f;
            sumRatio += setRatioValue(m, "hundred_count", sum, sumRatio);
            sumRatio += setRatioValue(m, "thirty_count", sum, sumRatio);
            sumRatio += setRatioValue(m, "sixty_count", sum, sumRatio);
            sumRatio += setRatioValue(m, "eighty_five_count", sum, sumRatio);
            m.put("fifteen_ratio", (float) Math.round((100 - sumRatio) * 100) / 100);
        }
        if (null != list && list.size() > 0) {
            alertRestfulDao.batchInsertIdcTypePerformance(list);
        }
    }

    public float setRatioValue(Map<String, Object> m, String key, long sum, float sumRatio) {
        sumRatio = Math.round(sumRatio * 100) / 100;
        long val = Long.parseLong(m.get(key).toString());
        float ratio = Utils.getOperValue(val, sum);
        if ((sumRatio + ratio) > 100) {
            ratio = 100 - sumRatio;
        }
        m.put(key.substring(0, key.lastIndexOf("_")) + "_ratio", ratio);
        return ratio;
    }

    @Override
    public void exportBizSystemMonthExcel(String month, String idcType, int hisFlag) throws Exception {


        log.info("***********exportReport--begin**************");

        List<String> hList = Lists.newArrayList();
        List<String> kList = Lists.newArrayList();
        hList.add("所属资源池名称");
        hList.add("归属部门（二级）");
        hList.add("所属租户（一级）");
        hList.add("业务系统名称");
        hList.add("联系人");
        hList.add("联系电话");
        kList.add("idcType");
        kList.add("department2");
        kList.add("department1");
        kList.add("bizSystem");
        kList.add("contact");
        kList.add("phone");

        /*
         * Calendar c = Calendar.getInstance();
         *
         * int year = c.get(Calendar.YEAR); int month = c.get(Calendar.MONTH)+1; int
         * date = c.get(Calendar.DATE); if(date==1) { c.add(Calendar.MONTH, -1); year =
         * c.get(c.YEAR); month = c.get(c.MONTH) + 1; } String monthStr = month+"";
         * if(month<10) { monthStr = "0"+month; }
         */
        List<String> days = alertMonthReportSyncDao.queryDays(month, idcType, hisFlag);
        List<Map<String, Object>> dataList = fromDate(month, idcType, hisFlag);


        if (days.size() == 0) {
            throw new Exception("没有数据");
        }
        Date startTime = DateUtils.parseDate(days.get(0), new String[]{"yyyy-MM-dd"});
        Date endTime = DateUtils.parseDate(days.get(days.size() - 1), new String[]{"yyyy-MM-dd"});
        List<String> dateList = Lists.newArrayList();

        initList1(hList, kList, startTime, endTime, dateList);
        String[] headerList = hList.toArray(new String[]{});
        String[] keyList = kList.toArray(new String[]{});

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = null;
        sheet = wb.createSheet("sheet1");
        String title = month + "月份租户利用率数据表";

        if (StringUtils.isNotBlank(idcType)) {
            title = idcType + month + "月份租户利用率数据表";
        }
        String fileName = title + ".xlsx";
        // 计算该报表的列数
        int number = hList.size();
        int num = dateList.size() + 6;

        for (int i = 0; i < number; i++) {
            sheet.setColumnWidth(i, 3000);
        }

        // 创建单元格样式
        XSSFCellStyle cellStyle = wb.createCellStyle();

        // 指定单元格居中对齐
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        // 指定单元格垂直居中对齐
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // 指定当单元格内容显示不下时自动换行
        cellStyle.setWrapText(true);

        //cellStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
        //cellStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
        //cellStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
        //cellStyle.setBorderTop(CellStyle.BORDER_MEDIUM);

        // 设置单元格字体
        XSSFFont font = wb.createFont();
        // font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("仿宋_GB2312");
        font.setFontHeight((short) 240);
        cellStyle.setFont(font);

        XSSFRow row1 = sheet.createRow(0);
        // 设置行高
        row1.setHeight((short) 1500);

        XSSFCell rowCell1 = null;
        // 创建不同的LIST的列标题
        for (int i = 0; i < num; i++) {

            if (i > 5) {
                String titleDay = dateList.get(i - 6);
                int m = (i - 6) * 8 + 6;
                rowCell1 = row1.createCell(m);
                rowCell1.setCellStyle(cellStyle);
                String name = getDate(titleDay);//month + "月" + (i - 5) + "日";
                rowCell1.setCellValue(name);
                for (int k = 0; k < 7; k++) {

                    rowCell1 = row1.createCell(m + 1 + k);
                    rowCell1.setCellValue("");
                }
                // i += 7;
                if (i == num - 1) {
                    m = (i - 6) * 8 + 6 + 8;
                    rowCell1 = row1.createCell(m);
                    rowCell1.setCellStyle(cellStyle);
                    rowCell1.setCellValue("平均值");
                    for (int k = 0; k < 7; k++) {
                        rowCell1 = row1.createCell(m + 1 + k);
                        rowCell1.setCellValue("");
                    }
                }
            } else {
                rowCell1 = row1.createCell(i);
                rowCell1.setCellStyle(cellStyle);
                rowCell1.setCellValue(new XSSFRichTextString(hList.get(i).toString()));
            }

        }

        int dayNum = num - 6;
        // 合并单元格
        // sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 7));
        for (int i = 0; i < dayNum; i++) {
            int k = i * 8;
            sheet.addMergedRegion(new CellRangeAddress(0, 0, k + 6, k + 13));
            if (i == dayNum - 1) {
                k = i * 8 + 8;
                sheet.addMergedRegion(new CellRangeAddress(0, 0, k + 6, k + 13));
            }
        }

        XSSFCell row2Cell = null;
        XSSFRow row2 = sheet.createRow(1);
        row2.setHeight((short) 800);
        for (int i = 0; i < number; i++) {
            row2Cell = row2.createCell(i);
            row2Cell.setCellStyle(cellStyle);
            if (i > 5) {
                String name = "裸金属";
                row2Cell.setCellValue(name);
                for (int k = 0; k < 3; k++) {
                    row2Cell = row2.createCell(i + 1 + k);
                    row2Cell.setCellValue("");
                }
                String name1 = "云主机";
                row2Cell = row2.createCell(i + 4);
                row2Cell.setCellStyle(cellStyle);
                row2Cell.setCellValue(name1);
                for (int k = 0; k < 3; k++) {
                    row2Cell = row2.createCell(i + 5 + k);
                    row2Cell.setCellValue("");
                }
                i += 7;

            } else {
                row2Cell.setCellValue(new XSSFRichTextString(""));
            }

        }
        // 合并单元格
        for (int i = 0; i < dayNum; i++) {
            int k = i * 8;
            sheet.addMergedRegion(new CellRangeAddress(1, 1, k + 6, k + 9));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, k + 10, k + 13));
            if (i == dayNum - 1) {
                k = i * 8 + 8;
                sheet.addMergedRegion(new CellRangeAddress(1, 1, k + 6, k + 9));
                sheet.addMergedRegion(new CellRangeAddress(1, 1, k + 10, k + 13));
            }
        }

        XSSFCell row3Cell = null;
        XSSFRow row3 = sheet.createRow(2);
        row3.setHeight((short) 800);
        for (int i = 0; i < number; i++) {
            row3Cell = row3.createCell(i);
            row3Cell.setCellStyle(cellStyle);
            if (i > 5) {
                String name1 = "cpu日均均值";
                String name2 = "cpu日均峰值";
                String name3 = "内存日均均值";
                String name4 = "内存日均峰值";
                String name5 = "cpu日均均值";
                String name6 = "cpu日均峰值";
                String name7 = "内存日均均值";
                String name8 = "内存日均峰值";

                if (i == number - 8) {
                    name1 = "cpu均值";
                    name2 = "cpu峰值";
                    name3 = "内存均值";
                    name4 = "内存峰值";
                    name5 = "cpu均值";
                    name6 = "cpu峰值";
                    name7 = "内存均值";
                    name8 = "内存峰值";
                }
                row3Cell.setCellValue(name1);
                row3Cell = row3.createCell(i + 1);
                row3Cell.setCellValue(name2);
                row3Cell = row3.createCell(i + 2);
                row3Cell.setCellValue(name3);
                row3Cell = row3.createCell(i + 3);
                row3Cell.setCellValue(name4);
                row3Cell = row3.createCell(i + 4);
                row3Cell.setCellValue(name5);
                row3Cell = row3.createCell(i + 5);
                row3Cell.setCellValue(name6);
                row3Cell = row3.createCell(i + 6);
                row3Cell.setCellValue(name7);
                row3Cell = row3.createCell(i + 7);
                row3Cell.setCellValue(name8);
                i += 7;

            } else {
                row3Cell.setCellValue(new XSSFRichTextString(""));
            }

        }
        // 合并单元格
        for (int i = 0; i < 6; i++) {
            sheet.addMergedRegion(new CellRangeAddress(0, 2, i, i));

        }

        for (int i = 0; i < dataList.size(); i++) {
            int k = i + 3;
            Map<String, Object> map = dataList.get(i);
            XSSFRow row = sheet.createRow(k);
            row3.setHeight((short) 800);
            XSSFCell rowCell = null;
            String department1 = "";
            String department2 = "";
            String bizSystem = "";

            if (map.get("bizSystem") != null && map.get("bizSystem").toString() != "") {
                bizSystem = map.get("bizSystem").toString();

                /*
                 * Map<String, String> biz =
                 * cmdbAlertRestfulClient.getDepartmentInfoByBizSystem(bizSystem); if (null !=
                 * biz) { department1 = StringUtils.isEmpty(biz.get("department1")) ? "" :
                 * biz.get("department1"); department2 =
                 * StringUtils.isEmpty(biz.get("department2")) ? "" : biz.get("department2"); }
                 */

            }

            for (int j = 0; j < 6; j++) {
                rowCell = row.createCell(j);
                if (j == 0) {
                    rowCell.setCellValue(new XSSFRichTextString(map.get("idcType").toString()));
                } else if (j == 1) {
                    rowCell.setCellValue(new XSSFRichTextString(department2));
                } else if (j == 2) {
                    rowCell.setCellValue(new XSSFRichTextString(department1));
                } else if (j == 3) {
                    rowCell.setCellValue(new XSSFRichTextString(bizSystem));
                } else if (j == 4) {
                    rowCell.setCellValue(new XSSFRichTextString(""));
                } else if (j == 5) {
                    rowCell.setCellValue(new XSSFRichTextString(""));
                }
            }
            for (int j = 0; j < dateList.size(); j++) {
                int m = 8 * j;
                String timeStr = dateList.get(j);
                rowCell = row.createCell(m + 6);
                StringBuffer sb = new StringBuffer();
                sb.append(timeStr).append("_").append("X86服务器").append("_").append("cpu").append("_avg");
                String key = sb.toString();
                rowCell.setCellValue(new XSSFRichTextString(map.get(key) == null ? "" : map.get(key).toString()));

                rowCell = row.createCell(m + 7);
                sb.setLength(0);
                sb.append(timeStr).append("_").append("X86服务器").append("_").append("cpu").append("_max");
                key = sb.toString();
                rowCell.setCellValue(new XSSFRichTextString(map.get(key) == null ? "" : map.get(key).toString()));

                rowCell = row.createCell(m + 8);
                sb.setLength(0);
                sb.append(timeStr).append("_").append("X86服务器").append("_").append("memory").append("_avg");
                key = sb.toString();
                rowCell.setCellValue(new XSSFRichTextString(map.get(key) == null ? "" : map.get(key).toString()));

                rowCell = row.createCell(m + 9);
                sb.setLength(0);
                sb.append(timeStr).append("_").append("X86服务器").append("_").append("memory").append("_max");
                key = sb.toString();
                rowCell.setCellValue(new XSSFRichTextString(map.get(key) == null ? "" : map.get(key).toString()));

                rowCell = row.createCell(m + 10);
                sb.setLength(0);
                sb.append(timeStr).append("_").append("云主机").append("_").append("cpu").append("_avg");
                key = sb.toString();
                rowCell.setCellValue(new XSSFRichTextString(map.get(key) == null ? "" : map.get(key).toString()));

                rowCell = row.createCell(m + 11);
                sb.setLength(0);
                sb.append(timeStr).append("_").append("云主机").append("_").append("cpu").append("_max");
                key = sb.toString();
                rowCell.setCellValue(new XSSFRichTextString(map.get(key) == null ? "" : map.get(key).toString()));

                rowCell = row.createCell(m + 12);
                sb.setLength(0);
                sb.append(timeStr).append("_").append("云主机").append("_").append("memory").append("_avg");
                key = sb.toString();
                rowCell.setCellValue(new XSSFRichTextString(map.get(key) == null ? "" : map.get(key).toString()));

                rowCell = row.createCell(m + 13);
                sb.setLength(0);
                sb.append(timeStr).append("_").append("云主机").append("_").append("memory").append("_max");
                key = sb.toString();
                rowCell.setCellValue(new XSSFRichTextString(map.get(key) == null ? "" : map.get(key).toString()));

                if (j == dateList.size() - 1) {
                    m = m + 8;
                    rowCell = row.createCell(m + 6);
                    sb.setLength(0);
                    timeStr = "日均均值";
                    sb.append(timeStr).append("_").append("X86服务器").append("_").append("cpu").append("_avg");
                    key = sb.toString();
                    rowCell.setCellValue(new XSSFRichTextString(map.get(key) == null ? "" : map.get(key).toString()));

                    rowCell = row.createCell(m + 7);
                    sb.setLength(0);
                    sb.append(timeStr).append("_").append("X86服务器").append("_").append("cpu").append("_max");
                    key = sb.toString();
                    rowCell.setCellValue(new XSSFRichTextString(map.get(key) == null ? "" : map.get(key).toString()));

                    rowCell = row.createCell(m + 8);
                    sb.setLength(0);
                    sb.append(timeStr).append("_").append("X86服务器").append("_").append("memory").append("_avg");
                    key = sb.toString();
                    rowCell.setCellValue(new XSSFRichTextString(map.get(key) == null ? "" : map.get(key).toString()));

                    rowCell = row.createCell(m + 9);
                    sb.setLength(0);
                    sb.append(timeStr).append("_").append("X86服务器").append("_").append("memory").append("_max");
                    key = sb.toString();
                    rowCell.setCellValue(new XSSFRichTextString(map.get(key) == null ? "" : map.get(key).toString()));

                    rowCell = row.createCell(m + 10);
                    sb.setLength(0);
                    sb.append(timeStr).append("_").append("云主机").append("_").append("cpu").append("_avg");
                    key = sb.toString();
                    rowCell.setCellValue(new XSSFRichTextString(map.get(key) == null ? "" : map.get(key).toString()));

                    rowCell = row.createCell(m + 11);
                    sb.setLength(0);
                    sb.append(timeStr).append("_").append("云主机").append("_").append("cpu").append("_max");
                    key = sb.toString();
                    rowCell.setCellValue(new XSSFRichTextString(map.get(key) == null ? "" : map.get(key).toString()));

                    rowCell = row.createCell(m + 12);
                    sb.setLength(0);
                    sb.append(timeStr).append("_").append("云主机").append("_").append("memory").append("_avg");
                    key = sb.toString();
                    rowCell.setCellValue(new XSSFRichTextString(map.get(key) == null ? "" : map.get(key).toString()));

                    rowCell = row.createCell(m + 13);
                    sb.setLength(0);
                    sb.append(timeStr).append("_").append("云主机").append("_").append("memory").append("_max");
                    key = sb.toString();
                    rowCell.setCellValue(new XSSFRichTextString(map.get(key) == null ? "" : map.get(key).toString()));
                }

            }
        }


        /*
         * ExportExcelUtil eeu = new ExportExcelUtil(); Workbook book = new
         * SXSSFWorkbook(128); eeu.exportExcel(book, 0, fileName, headerList, dataList,
         * keyList);
         */
        ByteArrayOutputStream ops = null;
        ByteArrayInputStream in = null;
        try {
            ops = new ByteArrayOutputStream();
            wb.write(ops);
            byte[] b = ops.toByteArray();
            in = new ByteArrayInputStream(b);
            ftpService.uploadtoFTPNew(fileName, in, ftpFilePath);
            ops.flush();
        } catch (Exception e) {
            log.error("导出excel失败，失败原因：", e);
        } finally {
            //IOUtils.closeQuietly(book);
            IOUtils.closeQuietly(ops);
            IOUtils.closeQuietly(in);
        }
        log.info("***********exportReport--end**************");


    }

	private List<Map<String, Object>> fromDate(String month,String idcType,int hisFlag) {
		List<Map<String, Object>> daysData = alertMonthReportSyncDao.queryDayBizSystem(month,idcType,hisFlag);
		List<Map<String, Object>> monthData = alertMonthReportSyncDao.queryDayBizSystemMonth(month,idcType,hisFlag);
		Map<String, Map<String, Object>> dataMap = Maps.newLinkedHashMap();
		formMapVal(daysData,dataMap,month,false);
		formMapVal(monthData,dataMap,month,true);
		return new ArrayList(dataMap.values());
	}

	private void formMapVal(List<Map<String, Object>> daysData
			, Map<String, Map<String, Object>> dataMap,String month,boolean flag) {
		for(Map<String, Object> day:daysData) {
			if(null==day.get("bizSystem")) {
				continue;
			}
			String biz = day.get("bizSystem").toString();
			String idcTypeVal = day.get("idcType").toString();
			String deviceType = day.get("deviceType").toString();
			String cpu_avg = day.get("cpu_avg") == null ? "" : day.get("cpu_avg").toString();
			String cpu_max = day.get("cpu_max") == null ? "" : day.get("cpu_max").toString();
			String memory_avg = day.get("memory_avg") == null ? "" : day.get("memory_avg").toString();
			String memory_max = day.get("memory_max") == null ? "" : day.get("memory_max").toString();
			String dayVal = day.get("day") == null ? "" : day.get("day").toString();
			Map<String, Object> val = new HashMap<String, Object>();
			String key = biz + "_"+idcTypeVal;
			if(dataMap.containsKey(key)) {
				val = dataMap.get(key);
			}else {
				val.put("idcType", idcTypeVal);
				val.put("bizSystem", biz);
				dataMap.put(key, val);
				val.put("month", month);
			}
			val.put("deviceType", deviceType);
			StringBuffer sb = new StringBuffer();
			if(flag) {
				sb.append("日均均值").append("_").append(deviceType).append("_");
			}else {
				sb.append(dayVal).append("_").append(deviceType).append("_");
			}
			val.put(sb.toString()+"cpu_avg", cpu_avg);
			val.put(sb.toString()+"cpu_max", cpu_max);
			val.put(sb.toString()+"memory_avg", memory_avg);
			val.put(sb.toString()+"memory_max", memory_max);
		}

	}
}

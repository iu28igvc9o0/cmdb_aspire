package com.aspire.mirror.alert.server.controller.alert;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.alert.api.dto.alert.AlertStatisticSummaryDTO;
import com.aspire.mirror.alert.api.dto.alert.AlertsStatisticClassifyDTO;
import com.aspire.mirror.alert.api.dto.alert.*;
import com.aspire.mirror.alert.api.service.alert.AlertStatisticService;
import com.aspire.mirror.alert.server.aspect.RequestAuthContext;
import com.aspire.mirror.alert.server.biz.alert.AlertsStatisticBiz;
import com.aspire.mirror.alert.server.biz.ftp.FtpService;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsExport;
import com.aspire.mirror.alert.server.util.PayloadParseUtil;
import com.aspire.mirror.alert.server.vo.alert.AlertTargetCountVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsStatisticSourceClassifyVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsHisVo;
import com.aspire.mirror.alert.server.util.DateUtils;
import com.aspire.mirror.alert.server.util.ExportExcelUtil;
import com.aspire.mirror.alert.server.util.TransformUtils;
import com.aspire.mirror.alert.server.constant.AlertConfigConstants;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.FieldUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.aspire.mirror.alert.server.util.PayloadParseUtil.jacksonBaseParse;

@Slf4j
@RestController
public class AlertStatisticController implements AlertStatisticService {
    @Autowired
    private AlertsStatisticBiz alertsStatisticBiz;
    @Autowired
    private FtpService ftpService;
    @Value("${alert.branch:}")
    private String branch;
    /**
     * 警报统计数据总览
     * @return
     */
    @Override
    public AlertStatisticSummaryResponse summary(@RequestParam(name = "idcType", required = false) String idcType) {
        if (AlertConfigConstants.BRANCH_IT_YUN.equalsIgnoreCase(branch)) {
            return summaryItYun(idcType);
        } else {
            return summaryCommon(idcType);
        }
    }

    private AlertStatisticSummaryResponse summaryCommon (String idcType) {
        // 获取数据权限
        Map<String, List<String>> resFilterMap = RequestAuthContext.getRequestHeadUser().getResFilterConfig();

        AlertStatisticSummaryResponse response = new AlertStatisticSummaryResponse();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String startTime = simpleDateFormat.format(date);
        String endTime = DateUtils.getSpecifiedDayAfter(startTime,1);
        Map<String, Object> param = Maps.newHashMap();
        param.put("code",0);
        param.put("startTime",startTime);
        param.put("endTime",endTime);
        AlertStatisticSummaryDTO toBeConfirmed = PayloadParseUtil.jacksonBaseParse(AlertStatisticSummaryDTO.class, alertsStatisticBiz.getSummaryByOperateType(param,resFilterMap));//待确认
        response.setToBeConfirmed(toBeConfirmed);
        param.put("code",1);
        AlertStatisticSummaryDTO confirmed = PayloadParseUtil.jacksonBaseParse(AlertStatisticSummaryDTO.class, alertsStatisticBiz.getSummaryByOperateType(param,resFilterMap));
        response.setConfirmed(confirmed);//已确认
        //已解除
        param.put("code",null);
        param.put("alertEndTime","alertEndTime");
        response.setResolved(PayloadParseUtil.jacksonBaseParse(AlertStatisticSummaryDTO.class, alertsStatisticBiz.getHisSummaryByOperateType(param,resFilterMap)));
        // 已清除
        param.put("alertEndTime",null);
        param.put("clearTime","clearTime");
        response.setCleared(PayloadParseUtil.jacksonBaseParse(AlertStatisticSummaryDTO.class, alertsStatisticBiz.getHisSummaryByOperateType(param,resFilterMap)));
        return response;
    }

    private AlertStatisticSummaryResponse summaryItYun (String idcType) {
        // 获取数据权限
        Map<String, List<String>> resFilterMap = RequestAuthContext.getRequestHeadUser().getResFilterConfig();

        AlertStatisticSummaryResponse response = new AlertStatisticSummaryResponse();

        Map<String, Object> param = Maps.newHashMap();
        param.put("code",0);
        if (StringUtils.isNotEmpty(idcType)) {
            param.put("idcType",idcType);
        }
        AlertStatisticSummaryDTO toBeConfirmed = PayloadParseUtil.jacksonBaseParse(AlertStatisticSummaryDTO.class, alertsStatisticBiz.getSummaryByOperateTypeOrder(param,resFilterMap));//待确认
        response.setToBeConfirmed(toBeConfirmed);
        param.put("code",1);
        AlertStatisticSummaryDTO confirmed = PayloadParseUtil.jacksonBaseParse(AlertStatisticSummaryDTO.class, alertsStatisticBiz.getSummaryByOperateTypeOrder(param,resFilterMap));
        response.setConfirmed(confirmed);//已确认
        //已解除
        param.put("code",null);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String startTime = simpleDateFormat.format(date);
        String endTime = DateUtils.getSpecifiedDayAfter(startTime,1);
        param.put("startTime",startTime);
        param.put("endTime",endTime);
        param.put("alertEndTime","alertEndTime");
        response.setResolved(PayloadParseUtil.jacksonBaseParse(AlertStatisticSummaryDTO.class, alertsStatisticBiz.getHisSummaryByOperateTypeOrder(param,resFilterMap)));
        // 已清除
        param.remove("alertEndTime");
        param.put("clearTime","clearTime");
        response.setCleared(PayloadParseUtil.jacksonBaseParse(AlertStatisticSummaryDTO.class, alertsStatisticBiz.getHisSummaryByOperateTypeOrder(param,resFilterMap)));
        return response;
    }

    @Override
    public AlertSummaryResponse getAlertSummary(@RequestBody Map<String, Object> authContentMap) {
        log.info("[ALERT-SERVICE]>>> getAlertSummary request is {}", authContentMap);
        AlertSummaryResponse response = new AlertSummaryResponse();
        Map<String, List<String>> resFilterMap = RequestAuthContext.getRequestHeadUser().getResFilterConfig();

        try {
            // 待确认
            authContentMap.put("code", 0);
            List<String> toConfirm = alertsStatisticBiz.getSummaryByAuthContentTest(authContentMap,resFilterMap);
            response.setToBeConfirm(CollectionUtils.isEmpty(toConfirm) ? 0 : toConfirm.size());
            //已确认
            authContentMap.put("code", 1);
            List<String> confirm = alertsStatisticBiz.getSummaryByAuthContentTest(authContentMap,resFilterMap);
            response.setConfirmed(CollectionUtils.isEmpty(confirm) ? 0 : confirm.size());
        } catch (Exception e) {
            log.error("[ALERT-SERVICE]>>> getAlertSummary error is {}", e);
            throw new RuntimeException("[ALERT-SERVICE]>>> getAlertSummary error is {}", e);
        }
        return response;
    }

    @Override
    public List<AlertMainSummaryResponse> getAlertSummaryByDeviceClass(@RequestParam("code") String code) {
        return PayloadParseUtil.jacksonBaseParse(AlertMainSummaryResponse.class, alertsStatisticBiz.getAlertSummaryByDeviceClass(code));
    }

    /**
     * 告警总览
     *
     * @return AlertsSummaryResponse 告警概览
     */
    @Override
    public AlertsOverViewResponse overview(@PathVariable("codes") String codes) throws ParseException {
//        List<Integer> codeList = Lists.newArrayList();
//        for (String code : codes.split(",")) {
//            if (StringUtils.isNotEmpty(code)) {
//                codeList.add(Integer.parseInt(code));
//            }
//        }
        AlertStatisticSummaryDTO overview = PayloadParseUtil.jacksonBaseParse(AlertStatisticSummaryDTO.class, alertsStatisticBiz.getOverview(codes));
        AlertsOverViewResponse response = new AlertsOverViewResponse();
        BeanUtils.copyProperties(overview, response);
        return response;
    }

    /**
     * 告警-监控对象列表
     *
     * @return AlertsSummaryResponse 告警概览
     */
    @Override
    public List<String> monitObjectList() {
        return alertsStatisticBiz.getAlertsMonitObjectList();
    }

    /**
     * 历史告警-监控对象列表
     *
     * @return AlertsSummaryResponse 监控对象列表
     */
    @Override
    public List<String> monitObjectHisList() {
        return alertsStatisticBiz.getHisAlertsMonitObjectList();
    }

    /**
     * 警报级别分类统计
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    @Override
    public AlertStatisticSummaryDTO alertLevelSummay(@RequestParam(value = "startDate") String startDate,
                                                     @RequestParam(value = "endDate") String endDate) throws ParseException {
        if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
            log.error("Alert Level Summary Request Params, startDate or endDate is empty!");
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return PayloadParseUtil.jacksonBaseParse(AlertStatisticSummaryDTO.class, alertsStatisticBiz.getSummaryByDateRange(sdf.parse(startDate), sdf.parse(endDate)));
    }

    /**
     * 警报级别分类统计
     * @param span
     * @return
     */
    @Override
    public AlertStatisticSummaryDTO alertLevelSummayByTimeSpan(@RequestParam(value = "span") String span) {
        if (StringUtils.isEmpty(span)) {
            return null;
        }
        Date startDate = DateUtils.getDateBySpan(span.toLowerCase());
        Date endDate = DateUtils.getTimesmorning();
        return PayloadParseUtil.jacksonBaseParse(AlertStatisticSummaryDTO.class, alertsStatisticBiz.getSummaryByDateRange(startDate, endDate));
    }

    /**
     * 警报级别时间趋势分类统计
     * @param span
     * @return
     */
    @Override
    public Map trend(@RequestParam(value = "span") String span) {
        return alertsStatisticBiz.getTrendSeries(span);
    }

    /**
     * 最新警报列表
     * @param limit
     * @return
     */
    @Override
    public List<AlertsDetailResponse> latest(@RequestParam(value = "limit") Integer limit) {
        // 获取数据权限
        Map<String, List<String>> resFilterMap = RequestAuthContext.getRequestHeadUser().getResFilterConfig();
        return TransformUtils.transform(AlertsDetailResponse.class,alertsStatisticBiz.getLatest(limit,resFilterMap));
    }

    /**
     * 警报类型数据分布
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    @Override
    public List<AlertsStatisticClassifyDTO> classify(@RequestParam(value = "startDate") String startDate,
                                                     @RequestParam(value = "endDate") String endDate) throws ParseException {
        if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
            log.error("Alert Level Summary Request Params, startDate or endDate is empty!");
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return PayloadParseUtil.jacksonBaseParse(AlertsStatisticClassifyDTO.class, alertsStatisticBiz.getClassifySeries(sdf.parse(startDate), sdf.parse(endDate)));
    }
    /**
     * 警报类型数据分布
     * @return
     * @throws ParseException
     */
    @Override
    public List<AlertsStatisticClassifyDTO> classifyByTimeSpan(@RequestParam(value = "span") String span) {
        if (StringUtils.isEmpty(span)) {
            return null;
        }
        Date startDate = DateUtils.getDateBySpan(span.toLowerCase());
        Date endDate = DateUtils.getTimesmorning();
        return PayloadParseUtil.jacksonBaseParse(AlertsStatisticClassifyDTO.class, alertsStatisticBiz.getClassifySeries(startDate, endDate));
    }

    private List<Map<String, Object>> transSourceClassifyResult(Map<String,
            List<AlertsStatisticSourceClassifyVo>> map) {
        List<Map<String, Object>> list = Lists.newArrayList();
        Iterator entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String source = (String) entry.getKey();
            List<AlertsStatisticSourceClassifyVo> innerList = (List<AlertsStatisticSourceClassifyVo>) entry.getValue();
            Map<String, Object> innerMap = new HashMap<>();
            innerMap.put("source", source);
            innerMap.put("alerts", innerList);
            list.add(innerMap);
        }
        return list;
    }

    /**
     * 资源池归属
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    @Override
    public List<Map<String, Object>> sourceClassify(@RequestParam(value = "startDate") String startDate,
                              @RequestParam(value = "endDate") String endDate) throws ParseException {
        if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
            log.error("Alert Level Summary Request Params, startDate or endDate is empty!");
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return transSourceClassifyResult(
                alertsStatisticBiz.getSourceClassifySeries(sdf.parse(startDate), sdf.parse(endDate))
        );
    }

    @Override
    public List<Map<String, Object>> sourceClassifyByTimeSpan(@RequestParam(value = "span") String span) {
        if (StringUtils.isEmpty(span)) {
            return null;
        }
        Date startDate = DateUtils.getDateBySpan(span.toLowerCase());
        Date endDate = DateUtils.getTimesmorning();
        return transSourceClassifyResult(
                alertsStatisticBiz.getSourceClassifySeries(startDate, endDate)
        );
    }

    private static final String CONTAIN_FLAG_INCLUDE = "include";
    private static final String CONTAIN_FLAG_EXCLUDE = "exclude";

    /**
     * 组装查询数据
     * @param queryRequest
     * @return
     * @throws ParseException
     */
    private PageRequest preparePageRequest(AlertsQueryRequest queryRequest) throws ParseException {
        PageRequest pageRequest = new PageRequest();
        BeanUtils.copyProperties(queryRequest, pageRequest);
        // 时间区间
        String span = queryRequest.getSpan();
        if (StringUtils.isNotEmpty(span)) {
            log.info("#=====> query span: " + span);
            Date startDate = DateUtils.getDateBySpan(span.toLowerCase());
            Date endDate = DateUtils.getTimesmorning();
            queryRequest.setAlertCreateStartTime(startDate);
            queryRequest.setAlertCreateEndTime(endDate);
        }
        // 业务系统
        if (StringUtils.isNotEmpty(queryRequest.getBizSys())) {
            queryRequest.setBizSysList(Arrays.asList(queryRequest.getBizSys().split(",")));
        }
        // 监控项
        if (StringUtils.isNotEmpty(queryRequest.getMonitItems())) {
            queryRequest.setMonitItemList(Arrays.asList(queryRequest.getMonitItems().split(",")));
        }
        // 告警来源
        if (StringUtils.isNotEmpty(queryRequest.getSource())) {
            queryRequest.setSourceList(Arrays.asList(queryRequest.getSource().split(",")));
        }
        SimpleDateFormat f_sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 告警生效时间区间
        if (StringUtils.isNotEmpty(queryRequest.getAlertCreateTimeRangeStart())) {
            queryRequest.setAlertCreateStartTime(f_sdf.parse(queryRequest.getAlertCreateTimeRangeStart()));
        }
        if (StringUtils.isNotEmpty(queryRequest.getAlertCreateTimeRangeEnd())) {
            queryRequest.setAlertCreateEndTime(f_sdf.parse(queryRequest.getAlertCreateTimeRangeEnd()));
        }
        // 当前告警时间区间
        if (StringUtils.isNotEmpty(queryRequest.getCurMoniTimeFrom())) {
            queryRequest.setCurMoniTimeStart(f_sdf.parse(queryRequest.getCurMoniTimeFrom()));
        }
        if (StringUtils.isNotEmpty(queryRequest.getCurMoniTimeTo())) {
            queryRequest.setCurMoniTimeEnd(f_sdf.parse(queryRequest.getCurMoniTimeTo()));
        }
        // 通知方式
        if (StringUtils.isNotEmpty(queryRequest.getNotifyType())) {
            queryRequest.setNotifyTypeList(Arrays.asList(queryRequest.getNotifyType().split(",")));
        }
        // 通知操作时间区间
        if (StringUtils.isNotEmpty(queryRequest.getNotifyTimeRangeStart())) {
            queryRequest.setNotifyStartTime((f_sdf.parse(queryRequest.getNotifyTimeRangeStart())));
        }
        if (StringUtils.isNotEmpty(queryRequest.getNotifyTimeRangeEnd())) {
            queryRequest.setNotifyEndTime((f_sdf.parse(queryRequest.getNotifyTimeRangeEnd())));
        }
        // 告警转派时间区间
        if (StringUtils.isNotEmpty(queryRequest.getTransferTimeRangeStart())) {
            queryRequest.setTransferStartTime(f_sdf.parse(queryRequest.getTransferTimeRangeStart()));
        }
        if (StringUtils.isNotEmpty(queryRequest.getTransferTimeRangeEnd())) {
            queryRequest.setTransferEndTime(f_sdf.parse(queryRequest.getTransferTimeRangeEnd()));
        }
//        // 告警确认操作时间区间
//        if (StringUtils.isNotEmpty(queryRequest.getConfirmTimeRangeStart())) {
//            queryRequest.setConfirmStartTime((f_sdf.parse(queryRequest.getConfirmTimeRangeStart())));
//        }
//        if (StringUtils.isNotEmpty(queryRequest.getConfirmTimeRangeEnd())) {
//            queryRequest.setConfirmEndTime(f_sdf.parse(queryRequest.getConfirmTimeRangeEnd()));
//        }
        // 派单操作时间区间
        if (StringUtils.isNotEmpty(queryRequest.getDeliverTimeRangeStart())) {
            queryRequest.setDeliverStartTime(f_sdf.parse(queryRequest.getDeliverTimeRangeStart()));
        }
        if (StringUtils.isNotEmpty(queryRequest.getDeliverTimeRangeEnd())) {
            queryRequest.setDeliverEndTime(f_sdf.parse(queryRequest.getDeliverTimeRangeEnd()));
        }
        // 清除操作时间区间
        if (StringUtils.isNotEmpty(queryRequest.getClearTimeRangeStart())) {
            queryRequest.setClearStartTime(f_sdf.parse(queryRequest.getClearTimeRangeStart()));
        }
        if (StringUtils.isNotEmpty(queryRequest.getClearTimeRangeEnd())) {
            queryRequest.setClearEndTime(f_sdf.parse(queryRequest.getClearTimeRangeEnd()));
        }
//        // 过滤操作时间区间
//        if (StringUtils.isNotEmpty(queryRequest.getFilterTimeRangeStart())) {
//            queryRequest.setFilterStartTime(f_sdf.parse(queryRequest.getFilterTimeRangeStart()));
//        }
//        if (StringUtils.isNotEmpty(queryRequest.getFilterTimeRangeEnd())) {
//            queryRequest.setFilterEndTime(f_sdf.parse(queryRequest.getFilterTimeRangeEnd()));
//        }
//        // 工程操作时间区间
//        if (StringUtils.isNotEmpty(queryRequest.getProjectTimeRangeStart())) {
//            queryRequest.setProjectStartTime(f_sdf.parse(queryRequest.getProjectTimeRangeStart()));
//        }
//        if (StringUtils.isNotEmpty(queryRequest.getMaintainTimeRangeStart())) {
//            queryRequest.setMaintainStartTime(f_sdf.parse(queryRequest.getMaintainTimeRangeStart()));
//        }
//        // 维护 操作时间区间
//        if (StringUtils.isNotEmpty(queryRequest.getMaintainTimeRangeEnd())) {
//            queryRequest.setMaintainEndTime(f_sdf.parse(queryRequest.getMaintainTimeRangeEnd()));
//        }
        Map<String, Object> map = FieldUtil.getFiledMap(queryRequest);
        for (String key : map.keySet()) {
            pageRequest.addFields(key, map.get(key));
        }
        log.info("#=====> query params: " + JSONObject.toJSONString(pageRequest));
        return pageRequest;
    }
    /**
     * 警报看板
     * 活动/确认 告警
     * @param queryRequest
     * @return
     */
    @Override
    public PageResponse<AlertsDetailResponse> query(@RequestBody AlertsDataRequest queryRequest) throws ParseException
    {
        if (queryRequest == null) {
            log.error("Alert query param pageRequset is null or query type is empty !");
            return null;
        }
        PageResponse<AlertsVo> pageResult =
                alertsStatisticBiz.select(preparePageRequest(jacksonBaseParse(AlertsQueryRequest.class,queryRequest)));
        PageResponse<AlertsDetailResponse> result = new PageResponse<>();
        result.setCount(pageResult.getCount());
        result.setResult(TransformUtils.transform(AlertsDetailResponse.class, pageResult.getResult()));
        return result;
    }

    /**
     * 导出警报列表数据
     */
    @Override
    public List<Map<String, Object>> export(@RequestBody AlertsDataRequest queryRequest) throws Exception {
        List<Map<String, Object>> dataLists = Lists.newArrayList();
        if (queryRequest == null) {
            log.error("Alert query param pageRequset is null or query type is empty !");
            return dataLists;
        }
        List<AlertsExport> pageResult =
                alertsStatisticBiz.export(preparePageRequest(jacksonBaseParse(AlertsQueryRequest.class,queryRequest)));
        for (AlertsExport alertsDTO : pageResult) {
//            AlertsExportDTO exportDTO = new AlertsExportDTO();
//            exportDTO.setAlertId(alertsDTO.getAlertId());
//            exportDTO.setAlertLevel(transAlertLevel(alertsDTO.getAlertLevel()));
//            exportDTO.setOrderStatus(transOrderStatus(alertsDTO.getOrderStatus()));
//            exportDTO.setObjectType(transAlertObjectType(alertsDTO.getObjectType()));
//            exportDTO.setDeviceIp(alertsDTO.getDeviceIp());
//            exportDTO.setDeviceId(alertsDTO.getDeviceId());
//            exportDTO.setMoniObject(alertsDTO.getMoniObject());
//            exportDTO.setCurMoniValue(alertsDTO.getCurMoniValue());
//            exportDTO.setMoniIndex(alertsDTO.getMoniIndex());
//            exportDTO.setAlertStartTime(alertsDTO.getAlertStartTime() == null ? "" : f_sdf.format(alertsDTO.getAlertStartTime()));
//            exportDTO.setCurMoniTime(alertsDTO.getCurMoniTime() == null ? "" : f_sdf.format(alertsDTO.getCurMoniTime()));
//            exportDTO.setIdcType(alertsDTO.getIdcType());
//            exportDTO.setSourceRoom(alertsDTO.getSourceRoom());
//            exportDTO.setSource(alertsDTO.getSource());
//            exportDTO.setReportType(transAlertReportType(alertsDTO.getReportType()));
//            exportDTO.setReportStatus(transOperateStatus(alertsDTO.getReportStatus()));
//            exportDTO.setReportTime(alertsDTO.getReportTime() == null ? "" : f_sdf.format(alertsDTO.getReportTime()));
//            exportDTO.setTransUser(alertsDTO.getTransUser());
//            exportDTO.setTransStatus(transOperateStatus(alertsDTO.getTransStatus()));
//            exportDTO.setTransTime(alertsDTO.getTransTime() == null ? "" : f_sdf.format(alertsDTO.getTransTime()));
//            exportDTO.setToConfirmUser(alertsDTO.getToConfirmUser());
//            exportDTO.setConfirmedUser(alertsDTO.getConfirmedUser());
//            exportDTO.setConfirmedTime(alertsDTO.getConfirmedTime() == null ? "" : f_sdf.format(alertsDTO.getConfirmedTime()));
//            exportDTO.setConfirmedContent(alertsDTO.getConfirmedContent());
//            exportDTO.setDeliverStatus(transOperateStatus(alertsDTO.getDeliverStatus()));
//            exportDTO.setDeliverTime(alertsDTO.getDeliverTime() == null ? "" : f_sdf.format(alertsDTO.getDeliverTime()));
//            exportDTO.setOrderType(transOrderType(alertsDTO.getOrderType()));
//            exportDTO.setAlertCount(alertsDTO.getAlertCount() == null ? 0 : alertsDTO.getAlertCount());
//            exportDTO.setDeviceClass(alertsDTO.getDeviceClass());
//            exportDTO.setDeviceType(alertsDTO.getDeviceType());
//            exportDTO.setDeviceMfrs(alertsDTO.getDeviceMfrs());
//            exportDTO.setDeviceModel(alertsDTO.getDeviceModel());
//            exportDTO.setHostName(alertsDTO.getHostName());
            dataLists.add(objectToMap(alertsDTO));
        }
        return dataLists;
    }

    /**
     * 警报看板
     * 活动/确认 告警历史
     * @param queryRequest
     * @return
     */
    @Override
    public PageResponse<AlertsHisDetailResponse> queryHis(@RequestBody AlertsHisDataRequest queryRequest) throws ParseException {
        if (queryRequest == null) {
            return null;
        }
        String isClear = null;
        if ("1".equals(queryRequest.getIsClear())) {
            isClear = "clearTime";
        } else if ("2".equals(queryRequest.getIsClear())) {
            isClear = "alertTime";
        }
        queryRequest.setIsClear(isClear);
        PageResponse<AlertsHisVo> pageResult =
                alertsStatisticBiz.selectHis(preparePageRequest(jacksonBaseParse(AlertsQueryRequest.class,queryRequest)));
        PageResponse<AlertsHisDetailResponse> result = new PageResponse<>();
        result.setCount(pageResult.getCount());
        result.setResult(TransformUtils.transform(AlertsHisDetailResponse.class, pageResult.getResult()));
        return result;
    }

    /**
     * 导出历史警报列表数据
     */
    @Override
    public Map<String, Object> exportHis(@RequestBody AlertsHisDataRequest queryRequest) throws Exception {
        Long time = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> pageResult =
                alertsStatisticBiz.exportHis(preparePageRequest(jacksonBaseParse(AlertsQueryRequest.class,queryRequest)));
        if (queryRequest == null) {
            log.error("Alert query param pageRequset is null or query type is empty !");
            map.put("code","0001");
            map.put("message", "query paramters are blank.");
            return map;
        }
        log.info("-----1.query his use-----: {} ms",(System.currentTimeMillis()-time));

        String[] headerList = {"级别","设备IP","监控对象","监控值","告警内容","开始时间","当前时间","资源池","工程期数","pod池","机房位置","业务线","设备分类","设备类型","设备厂商","设备型号","主机名称","告警来源","告警类型","状态","通知方式","通知状态","通知时间","转派人","转派状态","转派时间","待确认人","确认人","确认时间","确认内容","派单状态","派单时间","派单类型","自动解除时间","清除人","清除时间","清除内容"};
        String[] keyList = {"alertLevel","deviceIp","moniObject","curMoniValue","moniIndex","alertStartTime","curMoniTime","idcType","projectName","podName","sourceRoom","bizSystem","deviceClass","deviceType","deviceMfrs","deviceModel","hostName","source","objectType","orderStatus","reportType","reportStatus","reportTime","transUser","transStatus","transTime","toConfirmUser","confirmedUser","confirmedTime","confirmedContent","deliverStatus","deliverTime","orderType","alertEndTime","clearUser","clearTime","clearContent"};
        String title = "历史告警导出列表";
        String fileName = title+".xlsx";
        //excel constuct
        ExportExcelUtil eeu = new ExportExcelUtil();
        SXSSFWorkbook book = new SXSSFWorkbook(128);
        book.setCompressTempFiles(true);
        eeu.exportExcel(book, 0, title, headerList, pageResult, keyList);
        //主动释放资源
        pageResult = null;

        ByteArrayOutputStream ops = null;
        ByteArrayInputStream in = null;
        log.info("-----2.create excel use-----: {} ms",(System.currentTimeMillis()-time));
        try {
            ops = new ByteArrayOutputStream();
            book.write(ops);
            byte[] b = ops.toByteArray();
            in = new ByteArrayInputStream(b);
            map = ftpService.uploadtoFTP(fileName, in);
            ops.flush();
            log.info("-----3.upload excel to ftp use-----: {} ms",(System.currentTimeMillis()-time));
        } catch (Exception e) {
            log.error("导出excel失败，失败原因：", e);
            map.put("code","0005");
            map.put("message", e.getMessage());
        }finally {
            IOUtils.closeQuietly(book);
            IOUtils.closeQuietly(ops);
            IOUtils.closeQuietly(in);
            return map;
        }
    }

    private Map<String, Object> objectToMap(Object obj) throws Exception {
        if(obj == null){
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }

    private String transAlertLevel(String origin) {
        if (StringUtils.isEmpty(origin)) {
            return "";
        }
        String alertLevel;
        switch (StringUtils.trim(origin)) {
            case "1":
                alertLevel = "提示";
                break;
            case "2":
                alertLevel = "低";
                break;
            case "3":
                alertLevel = "中";
                break;
            case "4":
                alertLevel = "高";
                break;
            case "5":
                alertLevel = "严重";
                break;
            default:
                alertLevel = origin;
                break;
        }
        return alertLevel;
    }

    private String transOrderStatus(String origin) {
        if (StringUtils.isEmpty(origin)) {
            return "";
        }
        String orderStatus;
        switch (StringUtils.trim(origin)) {
            case "1":
                orderStatus = "未派单";
                break;
            case "2":
                orderStatus = "处理中";
                break;
            case "3":
                orderStatus = "已完成";
                break;
            default:
                orderStatus = origin;
                break;
        }
        return orderStatus;
    }

    private String transAlertObjectType(String origin) {
        if (StringUtils.isEmpty(origin)) {
            return "";
        }
        String objectType;
        switch (StringUtils.trim(origin)) {
            case "1":
                objectType = "设备";
                break;
            case "2":
                objectType = "业务系统";
                break;
            default:
                objectType = origin;
                break;
        }
        return objectType;
    }

    private String transAlertReportType(Integer reportType) {
        if (reportType == null) {
            return "";
        }
        String type;
        switch (reportType) {
            case 0:
                type = "短信";
                break;
            case 1:
                type = "邮件";
                break;
            default:
                type = String.valueOf(reportType);
                break;
        }
        return type;
    }

    private String transOperateStatus(Integer reportStaus) {
        if (reportStaus == null) {
            return "";
        }
        String status;
        switch (reportStaus) {
            case 0:
                status = "失败";
                break;
            case 1:
                status = "成功";
                break;
            default:
                status = String.valueOf(reportStaus);
                break;
        }
        return status;
    }

    private String transOrderType(String origin) {
        if (StringUtils.isEmpty(origin)) {
            return "";
        }
        String type;
        switch (origin) {
            case "1":
                type = "告警工单";
                break;
            case "2":
                type = "故障工单";
                break;
            default:
                type = origin;
                break;
        }
        return type;
    }

    @Override
    public List<AlertTargetCountResponse> getAlertTargetCount(@RequestParam(value = "alertLevel", required = false) String alertLevel,
                                                              @RequestParam("type") String type) {
        List<AlertTargetCountResponse> response = Lists.newArrayList();
        try {
            List<AlertTargetCountVo> alertTargetCountVos = alertsStatisticBiz.getAlertTargetCount(alertLevel, type);
            for (AlertTargetCountVo alertTargetCountVo : alertTargetCountVos) {
                response.add(jacksonBaseParse(AlertTargetCountResponse.class, alertTargetCountVo));
            }
        } catch (Exception e) {
            throw new RuntimeException("[ALERT-SERVICE]>>> getAlertTargetCount error is {}", e);
        }
        return response;
    }

    @Override
    public List<Map<String, Object>> getAlertCountByIpAndIdc(@RequestBody List<Map<String,Object>> request) {
        return alertsStatisticBiz.getAlertCountByIpAndIdc(request);
    }


}

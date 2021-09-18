package com.aspire.mirror.alert.server.biz.alert.impl;

import com.aspire.mirror.alert.server.biz.alert.AlertsStatisticBiz;
import com.aspire.mirror.alert.server.biz.helper.CmdbHelper;
import com.aspire.mirror.alert.server.dao.alert.AlertsDao;
import com.aspire.mirror.alert.server.dao.alert.AlertsRecordDao;
import com.aspire.mirror.alert.server.dao.alert.AlertsStatisticDao;
import com.aspire.mirror.alert.server.dao.alert.AlertsTransferDao;
import com.aspire.mirror.alert.server.dao.alert.po.*;
import com.aspire.mirror.alert.server.vo.alert.*;
import com.aspire.mirror.alert.server.util.TransformUtils;
import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.PageUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.axis.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Data
@Service
@Slf4j
public class AlertsStatisticBizImpl implements AlertsStatisticBiz {
    @Autowired
    private AlertsDao alertsDao;
    @Autowired
    private AlertsStatisticDao alertsStatisticDao;
    @Autowired
    private AlertsRecordDao alertsRecordDao;
    @Autowired
    private AlertsTransferDao alertsTransferDao;

    private static final String ALERT_LEVEL_TIP = "1";
    private static final String ALERT_LEVEL_LOW = "2";
    private static final String ALERT_LEVEL_MEDIUM = "3";
    private static final String ALERT_LEVEL_HIGH = "4";
    private static final String ALERT_LEVEL_SERIOUS = "5";

    private static final String TIME_RANGE_WEEK = "week";
    private static final String TIME_RANGE_MONTH = "month";
    private static final String TIME_RANGE_SEASON = "season";
    private static final String TIME_RANGE_YEAR = "year";

    private static final int ALERT_OPERATE_STATUS_CONFIRMED = 1;

    private AlertStatisticSummaryVo calcSummay(List<AlertsStatisticOverview> overviewList) {
        int tip = 0;
        int low = 0;
        int medium = 0;
        int high = 0;
        int serious = 0;
        int confirmed = 0;
        int summary = 0;
        for (AlertsStatisticOverview overview : overviewList) {
            switch (overview.getAlertLevel()) {
                case ALERT_LEVEL_TIP:
                    tip += overview.getCount();
                    break;
                case ALERT_LEVEL_LOW:
                    low += overview.getCount();
                    break;
                case ALERT_LEVEL_MEDIUM:
                    medium += overview.getCount();
                    break;
                case ALERT_LEVEL_HIGH:
                    high += overview.getCount();
                    break;
                case ALERT_LEVEL_SERIOUS:
                    serious += overview.getCount();
                    break;
                default:
                    break;
            }
            if (overview.getOperateStatus() != null && overview.getOperateStatus() == ALERT_OPERATE_STATUS_CONFIRMED) {
                confirmed += overview.getCount();
            }
            summary += overview.getCount();
        }
        AlertStatisticSummaryVo summaryDTO = new AlertStatisticSummaryVo();
        summaryDTO.setSummary(summary);
        summaryDTO.setConfirmed(confirmed);
        summaryDTO.setTip(tip);
        summaryDTO.setLow(low);
        summaryDTO.setMedium(medium);
        summaryDTO.setHigh(high);
        summaryDTO.setSerious(serious);
        return summaryDTO;
    }

    /**
     * 根据操作代码，获取警报统计数据
     * @param param
     * @return
     */
    @Override
    public AlertStatisticSummaryVo getSummaryByOperateType(Map<String, Object> param, Map<String, List<String>> resFilterMap) {
        Map<String, Object> map = alertsDao.selectSummeryByOperateCode(param,resFilterMap);
        AlertStatisticSummaryVo response = new AlertStatisticSummaryVo();
        if (null != map) {
            response.setSummary(map.get("codeCount") == null ? 0 : Integer.valueOf(String.valueOf(map.get("codeCount"))));
            response.setSerious(map.get("sCount") == null ? 0 : Integer.valueOf(String.valueOf(map.get("sCount"))));
            response.setHigh(map.get("hCount") == null ? 0 : Integer.valueOf(String.valueOf(map.get("hCount"))));
            response.setMedium(map.get("mCount") == null ? 0 : Integer.valueOf(String.valueOf(map.get("mCount"))));
            response.setLow(map.get("lCount") == null ? 0 : Integer.valueOf(String.valueOf(map.get("lCount"))));
        }
        return response;
    }

    public AlertStatisticSummaryVo getSummaryByOperateTypeOrder(Map<String, Object> param, Map<String, List<String>> resFilterMap) {
        Map<String, Object> map = alertsDao.selectSummeryByOperateCodeOrder(param,resFilterMap);
        AlertStatisticSummaryVo response = new AlertStatisticSummaryVo();
        if (null != map) {
            response.setSummary(map.get("codeCount") == null ? 0 : Integer.valueOf(String.valueOf(map.get("codeCount"))));
            response.setSerious(map.get("sCount") == null ? 0 : Integer.valueOf(String.valueOf(map.get("sCount"))));
            response.setHigh(map.get("hCount") == null ? 0 : Integer.valueOf(String.valueOf(map.get("hCount"))));
            response.setMedium(map.get("mCount") == null ? 0 : Integer.valueOf(String.valueOf(map.get("mCount"))));
            response.setLow(map.get("lCount") == null ? 0 : Integer.valueOf(String.valueOf(map.get("lCount"))));
        }
        return response;
    }

    @Override
    public AlertStatisticSummaryVo getHisSummaryByOperateType(Map<String, Object> param, Map<String, List<String>> resFilterMap) {
        AlertStatisticSummaryVo response = new AlertStatisticSummaryVo();
        Map<String, Object> map = alertsStatisticDao.selectHisSummary(param,resFilterMap);
        response.setSummary(0);
        response.setSerious(0);
        response.setMedium(0);
        response.setHigh(0);
        response.setLow(0);
        response.setConfirmed(0);
        response.setTip(0);
        if (null != map) {
            response.setSummary(map.get("codeCount") == null ? 0 : Integer.valueOf(String.valueOf(map.get("codeCount"))));
            response.setSerious(map.get("sCount") == null ? 0 : Integer.valueOf(String.valueOf(map.get("sCount"))));
            response.setHigh(map.get("hCount") == null ? 0 : Integer.valueOf(String.valueOf(map.get("hCount"))));
            response.setMedium(map.get("mCount") == null ? 0 : Integer.valueOf(String.valueOf(map.get("mCount"))));
            response.setLow(map.get("lCount") == null ? 0 : Integer.valueOf(String.valueOf(map.get("lCount"))));
        }
        return response;
    }

    public AlertStatisticSummaryVo getHisSummaryByOperateTypeOrder(Map<String, Object> param, Map<String, List<String>> resFilterMap) {
        AlertStatisticSummaryVo response = new AlertStatisticSummaryVo();
        Map<String, Object> map = alertsStatisticDao.selectHisSummaryOrder(param,resFilterMap);
        response.setSummary(0);
        response.setSerious(0);
        response.setMedium(0);
        response.setHigh(0);
        response.setLow(0);
        response.setConfirmed(0);
        response.setTip(0);
        if (null != map) {
            response.setSummary(map.get("codeCount") == null ? 0 : Integer.valueOf(String.valueOf(map.get("codeCount"))));
            response.setSerious(map.get("sCount") == null ? 0 : Integer.valueOf(String.valueOf(map.get("sCount"))));
            response.setHigh(map.get("hCount") == null ? 0 : Integer.valueOf(String.valueOf(map.get("hCount"))));
            response.setMedium(map.get("mCount") == null ? 0 : Integer.valueOf(String.valueOf(map.get("mCount"))));
            response.setLow(map.get("lCount") == null ? 0 : Integer.valueOf(String.valueOf(map.get("lCount"))));
        }
        return response;
    }

    @Override
    public AlertStatisticSummaryVo getSummaryByAuthContent(Map<String, Object> authContent) {
        return calcSummay(alertsStatisticDao.getSummaryByAuthContent(authContent));
    }

    @Override
    public List<String> getSummaryByAuthContentTest(Map<String, Object> authContent,
                                                    Map<String, List<String>> resFilterMap) {
        return "0".equals(String.valueOf(authContent.get("code"))) ?
                alertsStatisticDao.getSummaryActivityByAuthContent(authContent,resFilterMap) : alertsStatisticDao.getSummaryConfirmByAuthContent(authContent,resFilterMap);
    }

    /**
     * 根据时间区间获取警报数据
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public AlertStatisticSummaryVo getSummaryByDateRange(Date startDate, Date endDate) {
        return calcSummay(alertsDao.selectByDateRange(startDate, endDate));
    }

    @Override
    public AlertStatisticSummaryVo getOverview(String codes) {
        AlertStatisticSummaryVo response = new AlertStatisticSummaryVo();
        Map<String, Object> map = Maps.newHashMap();
        map.put("codes", codes);
        map.put("params", null);
        Map<String, Object> maps = alertsStatisticDao.selectOverview(map);
        if (null != maps) {
            response.setSummary(Integer.valueOf(String.valueOf(maps.get("codeCount"))));
            response.setSerious(Integer.valueOf(String.valueOf(maps.get("sCount"))));
            response.setHigh(Integer.valueOf(String.valueOf(maps.get("hCount"))));
            response.setMedium(Integer.valueOf(String.valueOf(maps.get("mCount"))));
            response.setLow(Integer.valueOf(String.valueOf(maps.get("lCount"))));
            if ("1".equals(codes)) response.setConfirmed(Integer.valueOf(String.valueOf(maps.get("codeCount"))));
            if ("4".equals(codes)) response.setObserved(Integer.valueOf(String.valueOf(maps.get("codeCount"))));
        }
        return response;
    }

    @Override
    public List<String> getAlertsMonitObjectList() {
        return alertsDao.selectMoniObj();
    }

    @Override
    public List<String> getHisAlertsMonitObjectList() {
        return alertsDao.selectHisMoniObj();
    }

    /**
     * 根据不同的时间跨度获取不周级别的警报数据
     * @param span
     * @return
     */
    @Override
    public Map<String, Object> getTrendSeries(String span) {
        Map<String, Object> map = new HashMap<>();
        Date startDate = null;
        Date endDate = new Date();
        String interval;
        Calendar c = Calendar.getInstance();
        c.setTime(endDate);
        List<AlertsStatisticTrend> trendList;
        Locale.setDefault(Locale.CHINA);
        switch (span.toLowerCase()) {
            case TIME_RANGE_WEEK: // 展示前7天数据
                c.add(Calendar.DATE, -7);
                startDate = c.getTime();
                interval = "day";
                break;
            case TIME_RANGE_MONTH:// 展示前30天数据
                c.add(Calendar.MONTH, -1);
                startDate = c.getTime();
                interval = "day";
                break;
            case TIME_RANGE_SEASON: // 展示前12周数据
                c.add(Calendar.MONTH, -3);
                startDate = c.getTime();
                interval = "week";
                break;
            case TIME_RANGE_YEAR: // 展示前12个月的数据
                c.add(Calendar.YEAR, -1);
                startDate = c.getTime();
                interval = "month";
                break;
            default:
                startDate = endDate;
                interval = "day";
                break;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("beginDate", startDate);
        params.put("endDate", endDate);
        params.put("interval", interval);
        trendList = alertsStatisticDao.selectTrendBySection(params);
        Set<String> sectionSet = new TreeSet<>();
        for (AlertsStatisticTrend trend : trendList) {
            sectionSet.add(trend.getSection());
        }
        Set<String> levelSet = new HashSet<>();
        Map<String, List<AlertsStatisticTrend>> levelMap = new HashMap<>();
        for (AlertsStatisticTrend trend : trendList) {
            String level = trend.getLevel();
            if (levelSet.contains(level)) {
                List<AlertsStatisticTrend> innerList = levelMap.get(level);
                innerList.add(trend);
            } else {
                List<AlertsStatisticTrend> innerList = Lists.newArrayList();
                innerList.add(trend);
                levelMap.put(level, innerList);
                levelSet.add(level);
            }
        }
        Map<String, List<Map<String, Integer>>> series = new HashMap<>();
        if (levelMap != null) {
            Iterator<Map.Entry<String, List<AlertsStatisticTrend>>> iterator = levelMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, List<AlertsStatisticTrend>> entry = iterator.next();
                String level = entry.getKey();
                List<AlertsStatisticTrend> trends = entry.getValue();
                series.put(level, dealInnerSeries(sectionSet, trends));
            }
        }
        map.put("xAxis", new ArrayList<>(sectionSet));
        map.put("series", series);
        return map;
    }

    private List<Map<String, Integer>> dealInnerSeries(Set<String> sectionSet, List<AlertsStatisticTrend> trends){
        List<Map<String, Integer>> list = Lists.newArrayList();
        for (String section : sectionSet) {
            Map<String, Integer> map = new HashMap<>();
            map.put(section, 0);
            for (AlertsStatisticTrend trend : trends) {
                if (section.equals(trend.getSection())) {
                    map.put(section, trend.getAlertNum());
                }
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 警报类型数据分布
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public List<AlertsStatisticClassifyVo> getClassifySeries(Date startDate, Date endDate) {
        List<AlertsStatisticClassifyVo> list = Lists.newArrayList();
        Map<String, Object> params = new HashMap<>();
        params.put("beginDate", startDate);
        params.put("endDate", endDate);
        List<AlertsStatisticClassify> classifyList = alertsStatisticDao.selectAlertsClassify(params);
        for (AlertsStatisticClassify classify : classifyList) {
            AlertsStatisticClassifyVo dto = new AlertsStatisticClassifyVo();
            BeanUtils.copyProperties(classify, dto);
            list.add(dto);
        }
        return list;
    }

    /**
     * 资源池归属
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public Map<String, List<AlertsStatisticSourceClassifyVo>> getSourceClassifySeries(Date startDate, Date endDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("beginDate", startDate);
        params.put("endDate", endDate);
        List<AlertsStatisticSourceClassify> sourceClassifyList = alertsStatisticDao.selectAlertsSourceClassify(params);
        Set<String> sourceSet = Sets.newHashSet();
        Map<String, List<AlertsStatisticSourceClassifyVo>> sourceMap = new HashMap<>();
        for (AlertsStatisticSourceClassify sourceClassify : sourceClassifyList) {
            String source  = StringUtils.isEmpty(sourceClassify.getIdcType()) ? "" : sourceClassify.getIdcType();
            AlertsStatisticSourceClassifyVo dto = new AlertsStatisticSourceClassifyVo();
            BeanUtils.copyProperties(sourceClassify, dto);
            if (sourceSet.contains(source)) {
                List<AlertsStatisticSourceClassifyVo> innerList = sourceMap.get(source);
                innerList.add(dto);
            } else {
                List<AlertsStatisticSourceClassifyVo> innerList = Lists.newArrayList();
                innerList.add(dto);
                sourceMap.put(source, innerList);
                sourceSet.add(source);
            }
        }
        return sourceMap;
    }

    /**
     * 获取最新的警报数据
     * @return
     */
    @Override
    public List<AlertsVo> getLatest(Integer limit, Map<String, List<String>> param) {
        List<Alerts> alertsList = alertsDao.selectLatestByCurMoniTime(limit,param);
        return TransformUtils.transform(AlertsVo.class, alertsList);
    }

    @Autowired
    private CmdbHelper cmdbHelper;
    /**
     * 警报看板
     * @param pageRequest
     * @return
     */
    @Override
    public PageResponse<AlertsVo> select(PageRequest pageRequest) {
        Page page = PageUtil.convert(pageRequest);
        int count = alertsStatisticDao.pageListCount(page);
        PageResponse<AlertsVo> pageResponse = new PageResponse<AlertsVo>();
        pageResponse.setCount(count);
        if (count > 0) {
            List<Alerts> listAlerts = alertsStatisticDao.pageList(page);
            List<AlertsVo> listDTO = TransformUtils.transform(AlertsVo.class, listAlerts);
            for (AlertsVo alertHis: listDTO) {
           	    String alertId = alertHis.getAlertId();
                List<AlertsRecord> recordList = alertsRecordDao.selectRecordByPrimaryKey(alertId);
                
                List<AlertReportOperateRecord> reportRecord = alertsRecordDao.selectReportOperationRecordByAlertKey(alertId);
                if (!CollectionUtils.isEmpty(reportRecord)) {
                	alertHis.setReportTime(reportRecord.get(0).getCreateTime());
                    alertHis.setReportStatus(reportRecord.get(0).getStatus());
                    alertHis.setReportType(reportRecord.get(0).getReportType());
                }
                
                if (!CollectionUtils.isEmpty(recordList)) {
                    for (AlertsRecord record: recordList) {
                        String operationType = record.getOperationType();
                        switch (operationType) {
                            case "0":
                                if (alertHis.getTransTime() == null || alertHis.getTransTime().before(record.getOperationTime())) {
                                    alertHis.setTransUser(record.getUserName());
                                    alertHis.setTransStatus(Integer.parseInt(record.getOperationStatus()));
                                    alertHis.setTransTime(record.getOperationTime());
                                }
                                break;
                            case "1":
                                if (alertHis.getConfirmedTime() == null || alertHis.getConfirmedTime().before(record.getOperationTime())) {
                                    alertHis.setConfirmedUser(record.getUserName());
                                    alertHis.setConfirmedTime(record.getOperationTime());
                                    alertHis.setConfirmedContent(record.getContent());
                                }
                                break;
                            case "2":
                                if (alertHis.getDeliverTime() == null || alertHis.getDeliverTime().before(record.getOperationTime())) {
                                    alertHis.setDeliverTime(record.getOperationTime());
                                    alertHis.setDeliverStatus(Integer.parseInt(record.getOperationStatus()));
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
                if (org.apache.commons.lang.StringUtils.isEmpty(alertHis.getConfirmedUser())) {
                    List<AlertsTransfer> transferList = alertsTransferDao.selectByAlertId(alertHis.getAlertId());
                    Set<String> nameSet = new HashSet<>();
                    for (AlertsTransfer transfer: transferList) {
                        nameSet.add(transfer.getConfirmUserName());
                    }
                    StringBuilder username = new StringBuilder("");
                    for (String name: nameSet) {
                        username.append(name).append(",");
                    }
                    String toConfirmUser = username.toString();
                    alertHis.setToConfirmUser(toConfirmUser.length() > 0 ? toConfirmUser.substring(0, toConfirmUser.length()-1) : "");
                }
           }
            pageResponse.setResult(listDTO);
        }
        return pageResponse;
    }

    @Override
    public PageResponse<AlertsHisVo> selectHis(PageRequest pageRequest) {
        Page page = PageUtil.convert(pageRequest);
        int count = alertsStatisticDao.pageHisListCount(page);
        PageResponse<AlertsHisVo> pageResponse = new PageResponse<>();
        pageResponse.setCount(count);
        if (count > 0) {
            List<AlertsHis> listHisAlerts = alertsStatisticDao.pageHisList(page);
            List<AlertsHisVo> listHisDTO = TransformUtils.transform(AlertsHisVo.class, listHisAlerts);
            for (AlertsHisVo alertHis: listHisDTO) {
                String alertId = alertHis.getAlertId();
                List<AlertsRecord> recordList = alertsRecordDao.selectRecordByPrimaryKey(alertId);
                List<AlertReportOperateRecord> reportRecord = alertsRecordDao.selectReportOperationRecordByAlertKey(alertId);
                if (!CollectionUtils.isEmpty(reportRecord)) {
                	alertHis.setReportTime(reportRecord.get(0).getCreateTime());
                    alertHis.setReportStatus(reportRecord.get(0).getStatus());
                    alertHis.setReportType(reportRecord.get(0).getReportType());
                }
                if (!CollectionUtils.isEmpty(recordList)) {
                    for (AlertsRecord record: recordList) {
                        String operationType = record.getOperationType();
                        switch (operationType) {
                            case "0":
                                if (alertHis.getTransTime() == null || alertHis.getTransTime().before(record.getOperationTime())) {
                                    alertHis.setTransUser(record.getUserName());
                                    alertHis.setTransStatus(Integer.parseInt(record.getOperationStatus()));
                                    alertHis.setTransTime(record.getOperationTime());
                                }
                                break;
                            case "1":
                                if (alertHis.getConfirmedTime() == null || alertHis.getConfirmedTime().before(record.getOperationTime())) {
                                    alertHis.setConfirmedUser(record.getUserName());
                                    alertHis.setConfirmedTime(record.getOperationTime());
                                    alertHis.setConfirmedContent(record.getContent());
                                }
                                break;
                            case "2":
                                if (alertHis.getDeliverTime() == null || alertHis.getDeliverTime().before(record.getOperationTime())) {
                                    alertHis.setDeliverTime(record.getOperationTime());
                                    alertHis.setDeliverStatus(Integer.parseInt(record.getOperationStatus()));
                                }
                                break;
//                            case "3":
//                                if (alertHis.getClearTime() == null || alertHis.getClearTime().before(record.getOperationTime())) {
//                                    alertHis.setClearTime(record.getOperationTime());
//                                    alertHis.setClearContent(record.getContent());
//                                    alertHis.setClearUser(record.getUserName());
//                                }
//                                break;
                            default:
                                break;
                        }
                    }
                }
                if (org.apache.commons.lang.StringUtils.isEmpty(alertHis.getConfirmedUser())) {
                    List<AlertsTransfer> transferList = alertsTransferDao.selectByAlertId(alertHis.getAlertId());
                    Set<String> nameSet = new HashSet<>();
                    for (AlertsTransfer transfer: transferList) {
                        nameSet.add(transfer.getConfirmUserName());
                    }
                    StringBuilder username = new StringBuilder("");
                    for (String name: nameSet) {
                        username.append(name).append(",");
                    }
                    String toConfirmUser = username.toString();
                    alertHis.setToConfirmUser(toConfirmUser.length() > 0 ? toConfirmUser.substring(0, toConfirmUser.length()-1) : "");
                }
            }
            pageResponse.setResult(listHisDTO);
        }
        return pageResponse;
    }

    @Override
    public List<AlertsExport> export(PageRequest pageRequest) {
        Page page = PageUtil.convert(pageRequest);
        List<AlertsExport> listAlerts = alertsStatisticDao.exportList(page);
        return listAlerts;
    }

    @Override
    public List<Map<String, Object>> exportHis(PageRequest pageRequest) {
        Page page = PageUtil.convert(pageRequest);
        List<Map<String, Object>> listHisAlerts = alertsStatisticDao.exportHisList(page);
        return listHisAlerts;
    }

    @Override
    public List<AlertMainSummaryVo> getAlertSummaryByDeviceClass(String code) {
        log.info("[ALERT-SERVICE]>>> getAlertSummaryByDeviceClass request is {}", code);
//        Map<String,AlertStatisticSummaryDTO> response = Maps.newHashMap();
        List<AlertMainSummaryVo> alertMainSummaryRespons = Lists.newArrayList();
        try {
            List<AlertsStatisticOverview> alertSummaryByDeviceClass = Lists.newArrayList();
            if ("3".equals(code)) {
                alertSummaryByDeviceClass.addAll(alertsStatisticDao.getAlertHisSummaryByDeviceClass());
            } else {
                List<String> codeList = Arrays.asList(code.split(","));
                alertSummaryByDeviceClass.addAll(alertsStatisticDao.getAlertSummaryByDeviceClass(codeList));
            }
            Map<String,List<AlertsStatisticOverview>> data = Maps.newHashMap();
            for (AlertsStatisticOverview overView : alertSummaryByDeviceClass) {
                Set<String> key = data.keySet();
                String deviceClass1 = overView.getDeviceClass();
                List<AlertsStatisticOverview> list = Lists.newArrayList();
                if (key.contains(deviceClass1)) {
                    list = data.get(deviceClass1);
                    list.add(overView);
                } else {
                    list.add(overView);
                }
                data.put(deviceClass1,list);
            }
            Set<String> key = data.keySet();
            for (String str : key) {
                AlertStatisticSummaryVo alertStatisticSummaryVo = this.calcSummay(data.get(str));
                AlertMainSummaryVo obj = new AlertMainSummaryVo();
                obj.setDeviceClass(str);
                obj.setAlertStatisticSummaryVo(alertStatisticSummaryVo);
                alertMainSummaryRespons.add(obj);
            }
            log.info("[ALERT-SERVICE]>>> getAlertSummaryByDeviceClass response is {}", alertMainSummaryRespons);
        } catch (Exception e) {
            log.error("[ALERT-SERVICE]>>> getAlertSummaryByDeviceClass error is {}", e);
        }
        return alertMainSummaryRespons;
    }

    @Override
    public List<AlertTargetCountVo> getAlertTargetCount(String alertLevel, String type) {
        List<AlertTargetCountVo> response = Lists.newArrayList();
        if ("alert".equals(type)) {
            response.addAll(alertsStatisticDao.getAlertCountByAlertTarget(alertLevel));
        } else if ("monitor".equals(type)) {
            response.addAll(alertsStatisticDao.getAlertCountByMoniTarget(alertLevel));
        }
        return response;
    }

    @Override
    public int selectAlert(PageRequest pageRequest) {
        Page page = PageUtil.convert(pageRequest);
        return alertsStatisticDao.selectAlert(page);
    }

    @Override
    public List<Map<String, Object>> getAlertCountByIpAndIdc(List<Map<String, Object>> request) {
        List<Map<String, Object>> response = Lists.newArrayList(  );
        for (Map<String, Object> map : request) {
            Map<String, Object> alertCountByIpAndIdc =
                    alertsStatisticDao.getAlertCountByIpAndIdc(String.valueOf(map.get("ip")),
                            String.valueOf(map.get("idcType")));
            map.put("count",alertCountByIpAndIdc == null ? 0 : alertCountByIpAndIdc.get("count"));
            response.add(map);
        }
        return response;
    }
}

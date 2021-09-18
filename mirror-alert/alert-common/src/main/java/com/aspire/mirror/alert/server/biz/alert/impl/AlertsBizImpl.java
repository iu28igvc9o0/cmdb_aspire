package com.aspire.mirror.alert.server.biz.alert.impl;

import com.aspire.mirror.alert.server.biz.alert.AlertsBiz;
import com.aspire.mirror.alert.server.dao.alert.*;
import com.aspire.mirror.alert.server.dao.alert.po.*;
import com.aspire.mirror.alert.server.constant.Constants;
import com.aspire.mirror.alert.server.dao.notify.AlertsNotifyDao;
import com.aspire.mirror.alert.server.dao.notify.po.AlertsNotify;
import com.aspire.mirror.alert.server.util.TransformUtils;
import com.aspire.mirror.alert.server.vo.alert.AlertDeviceInformationVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsVo;
import com.aspire.mirror.alert.server.vo.notify.NotifyPageVo;
import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.PageUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 告警实现类
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.server.biz.impl
 * 类名称:    AlertsBizImpl.java
 * 类描述:    告警实现类
 * 创建人:    JinSu
 * 创建时间:  2018/9/18 16:16
 * 版本:      v1.0
 */
@Slf4j
@Service
@Transactional
public class AlertsBizImpl implements AlertsBiz {

    @Autowired
    private AlertsDao alertsDao;

    @Autowired
    private AlertsRecordDao alertsRecordDao;

    @Autowired
    private AlertsNotifyDao alertsNotifyDao;

    @Autowired
    private AlertsHisDao alertHisDao;

    @Autowired
    private AlertsDetailDao alertsDetailDao;

    @Autowired
    private AlertsStatisticDao alertsStatisticDao;

    /**
     * 告警详情
     *
     * @param alertId 告警ID
     * @return
     */
    @Override
    public AlertsVo selectAlertByPrimaryKey(String alertId) {
        if (StringUtils.isEmpty(alertId)) {
            LOGGER.warn("method[selectByPrimaryKey] param[alertId] is null");
            return null;
        }
        Alerts alerts = alertsDao.selectByPrimaryKey(alertId);

        if (alerts == null) {
            return null;
        }
        AlertsVo alertsVo = TransformUtils.transform(AlertsVo.class, alerts);
        return alertsVo;
    }

    /**
     * 根据条件查询
     *
     * @param alertQuery
     * @return
     */
    public List<AlertsVo> select(Alerts alertQuery) {
        List<Alerts> list = alertsDao.select(alertQuery);
        return TransformUtils.transform(AlertsVo.class, list);
    }


    //分页查询告警上报记录
    @Override
    public PageResponse<AlertsDetail> alertGenerateListByPage(String alertId, String pageNo, String pageSize) {

        PageResponse<AlertsDetail> pageResponse = new PageResponse<AlertsDetail>();

        Map<String, Object> hashMap = new HashMap<String, Object>();
        int pageSize1 = Integer.valueOf(pageSize);
        int pageNo1 = Integer.valueOf(pageNo);
        hashMap.put("pageNo", (pageNo1 - 1) * pageSize1);
        hashMap.put("pageSize", pageSize1);
        hashMap.put("alertId", alertId);

        int count = alertsDetailDao.countByAlertId(hashMap);
        List<AlertsDetail> alertDetailList = alertsDetailDao.selectByAlertId(hashMap);

//  		List<AlertGenResp> alertGenRespList=new ArrayList<AlertGenResp>();
//        for ( AlertsDetail alertsDetail : alertDetailList ){
//
//        	AlertGenResp alertGenResp=new AlertGenResp();
//
//        	BeanUtils.copyProperties(alertsDetail, alertGenResp);
//
//        	alertGenRespList.add(alertGenResp);
//  		}
        pageResponse.setCount(count);
        pageResponse.setResult(alertDetailList);
        return pageResponse;
    }


    //分页查询告警操作记录
    @Override
    public PageResponse<AlertsRecord> alertRecordListByPage(String alertId, String pageNo, String pageSize) {

        PageResponse<AlertsRecord> pageResponse = new PageResponse<AlertsRecord>();

        Map<String, Object> hashMap = new HashMap<String, Object>();

        int pageSize1 = Integer.valueOf(pageSize);
        int pageNo1 = Integer.valueOf(pageNo);

        hashMap.put("pageNo", (pageNo1 - 1) * pageSize1);
        hashMap.put("pageSize", pageSize1);

        hashMap.put("alertId", alertId);

        int count = alertsRecordDao.getAlertRecordCount(hashMap);

        List<AlertsRecord> alertsRecordList = alertsRecordDao.getAlertRecordByPage(hashMap);

//  		List<AlertRecordResp> alertRecordRespList=new ArrayList<AlertRecordResp>();
//
//        for ( AlertsRecord alertsRecord : alertsRecordList ){
//
//        	AlertRecordResp alertRecordResp=new AlertRecordResp();
//
//        	BeanUtils.copyProperties(alertsRecord, alertRecordResp);
//
//        	alertRecordRespList.add(alertRecordResp);
//  		}
//
        pageResponse.setCount(count);

        pageResponse.setResult(alertsRecordList);

        return pageResponse;


    }


    //分页查询告警通知记录
    @Override
    public NotifyPageVo<AlertsNotify> alertNotifyListByPage(String alertId, String pageNo, String pageSize,
                                                            String reportType) {

        try {
            NotifyPageVo<AlertsNotify> pageResponse = new NotifyPageVo<AlertsNotify>();
            Map<String, Object> hashMap = new HashMap<String, Object>();
            int pageSize1 = Integer.valueOf(pageSize);
            int pageNo1 = Integer.valueOf(pageNo);
            hashMap.put("pageNo", (pageNo1 - 1) * pageSize1);
            hashMap.put("pageSize", pageSize1);
            hashMap.put("alertId", alertId);
            hashMap.put("reportType", reportType);

            if (StringUtils.isEmpty(reportType)) {
                List<String> all = Arrays.asList("1", "2", "3");
                hashMap.put("notifyTypeList", all);
            } else {
                List<String> mobile = Arrays.asList("1");
                List<String> email = Arrays.asList("2", "3");
                hashMap.put("notifyType", "0".equals(reportType) ? mobile : email);
            }
            int sumCount = 0;
            List<Integer> count = alertsNotifyDao.getAlertNotifyCount(hashMap);
            for (Integer integer : count) {
                sumCount += integer;
            }
            List<Integer> success = alertsNotifyDao.getSuccessAlertNotifyCount(hashMap);
            int successCount = 0;
            for (Integer integer : success) {
                successCount += integer;
            }
            List<AlertsNotify> alertsNotifyList = alertsNotifyDao.getAlertNotifyByPage(hashMap);
//            List<AlertNotifyResp> alertNotifyRespList=new ArrayList<AlertNotifyResp>();
//            for ( AlertsNotify alertsNotify : alertsNotifyList ){
//                AlertNotifyResp alertNotifyResp=new AlertNotifyResp();
//                BeanUtils.copyProperties(alertsNotify, alertNotifyResp);
//                alertNotifyRespList.add(alertNotifyResp);
//            }
            pageResponse.setCount(sumCount);
            pageResponse.setSuccessCount(successCount);
            pageResponse.setFallCount(sumCount - successCount);
            pageResponse.setResult(alertsNotifyList);
            return pageResponse;
        } catch (Exception e) {
            log.error("alertNotifyListByPage is error {}", e);
        }
        return null;

    }


    //告警上报记录Excel
    @Override
    public List<AlertsVo> selectAlertGenerateList(String alertId) {


        Alerts alerts = alertsDao.selectByPrimaryKey(alertId);

        String deviceIp = alerts.getDeviceIp();
        String moniObject = alerts.getMoniObject();
        String alertLevel = alerts.getAlertLevel();


        List<Alerts> alertList = alertsDao.selectAllAlertGenerateList(deviceIp, moniObject, alertLevel);

        if (alertList == null) {
            return null;
        }

        List<AlertsVo> alertDtolist = new ArrayList<AlertsVo>();

        for (Alerts alerts1 : alertList) {
            AlertsVo alertsVo = TransformUtils.transform(AlertsVo.class, alerts1);
            alertDtolist.add(alertsVo);

        }

        return alertDtolist;
    }

    //告警操作记录Excel
    @Override
    public List<AlertsRecord> selectAlertRecordByPrimaryKey(String alertId) {

        if (StringUtils.isEmpty(alertId)) {
            LOGGER.warn("method[selectByPrimaryKey] param[alertId] is null");
            return null;
        }

        List<AlertsRecord> alertsRecords = alertsRecordDao.selectRecordByPrimaryKey(alertId);


        return alertsRecords;

    }

    //告警通知记录Excel
    @Override
    public List<AlertsNotify> selectalertNotifyByPrimaryKey(String alertId) {

        if (StringUtils.isEmpty(alertId)) {
            LOGGER.warn("method[selectByPrimaryKey] param[alertId] is null");
            return null;
        }

        List<AlertsNotify> alertsNotifys = alertsNotifyDao.selectNotifyByPrimaryKey(alertId);

        return alertsNotifys;
    }


    //根据主键修改备注
    @Override
    public void updateNote(String alertId, String note) {

        Alerts alerts = alertsDao.selectByPrimaryKey(alertId);

        alerts.setRemark(note);

        int index = alertsDao.updateByPrimaryKey(alerts);

    }


    /**
     * 根据工单ID修改工单状态
     *
     * @param orderId     工单ID
     * @param orderStatus 工单状态
     */
    @Override
    public void modOrderStatusByOrderId(String orderId, String orderStatus) {
        alertsDao.modOrderStatusByOrderId(orderId, orderStatus);
    }

    /**
     * @param alertsVo 告警修改对象
     * @return
     */
    @Override
    public int updateByPrimaryKey(AlertsVo alertsVo) {
        if (null == alertsVo) {
            LOGGER.error("method[updateByPrimaryKey] param[alertsDTO] is null");
            throw new RuntimeException("param[alertsDTO] is null");
        }
        if (StringUtils.isEmpty(alertsVo.getAlertId())) {
            LOGGER.warn("method[updateByPrimaryKey] param[alertId] is null");
            throw new RuntimeException("param[alertId] is null");
        }
        Alerts alerts = TransformUtils.transform(Alerts.class, alertsVo);
        int result = alertsDao.updateByPrimaryKey(alerts);
        return result;
    }


    /**
     * @param pageRequest 告警查询page对象
     * @return
     */
    @Override
    public PageResponse<AlertsVo> pageList(PageRequest pageRequest) {
        Page page = PageUtil.convert(pageRequest);
        int count = alertsDao.pageListCount(page);
        PageResponse<AlertsVo> pageResponse = new PageResponse<AlertsVo>();
        pageResponse.setCount(count);
        if (count > 0) {
            List<Alerts> listAlerts = alertsDao.pageList(page);
            List<AlertsVo> listDTO = TransformUtils.transform(AlertsVo.class, listAlerts);
            pageResponse.setResult(listDTO);
        }
        return pageResponse;
    }

    @Override
    public List<AlertsVo> selectByPrimaryKeyArrays(String[] alertIdArrays) {
        if (ArrayUtils.isEmpty(alertIdArrays)) {
            LOGGER.warn("method[selectByPrimaryKeyArrays] param[alertIdArrays] is null");
            return Collections.emptyList();
        }
        List<Alerts> alertsList = alertsDao.selectByPrimaryKeyArrays(alertIdArrays);
        return TransformUtils.transform(AlertsVo.class, alertsList);
    }

    private Logger LOGGER = LoggerFactory.getLogger(AlertsBizImpl.class);

    @Override
    public List<Map<String, Object>> getAlertConfig() {
        return alertsDao.getAlertConfig();
    }

    @Override
    public List<Map<String, String>> getFirstBusiness(Map<String, Object> param) {
        return alertsDao.getFirstBusiness(param);
    }

    /**
     * 根据告警id删除上报记录
     *
     * @param alertId
     */
    public void deleteAlertsDetail(String alertId) {
        alertsDetailDao.deleteByAlertId(alertId);
    }

    /**
     * 新增告警上报记录
     *
     * @param alertDetail
     */
    public void insertAlertsDetail(AlertsDetail alertDetail) {
        alertsDetailDao.insert(alertDetail);
    }

    @Override
    public String upgrade(String oldOrderId, String orderId, String type,String orderStatus, String userName) {
        Alerts alertQuery = new Alerts();
        alertQuery.setOrderId(oldOrderId);
        List<Alerts> alertList = alertsDao.select(alertQuery);
        for (Alerts alert : alertList) {
            alert.setOrderId(orderId);
            alert.setOrderType(type);
            alert.setOrderStatus(orderStatus);
            alertsDao.updateByPrimaryKey(alert);
            //如果是工单升级，同时往告警操作记录表插入一条数据
            if (orderStatus.equals(Constants.ORDER_DEALING)){
                String content = "";
                if (type.equals("2")){
                    content ="bpm侧升级到故障工单";
                }else if (type.equals("3")){
                    content ="bpm侧升级到维保工单";
                }
                AlertsRecord alertsRecord = new AlertsRecord();
                alertsRecord.setAlertId(alert.getAlertId());
                alertsRecord.setUserName(userName);
                alertsRecord.setOperationType("100");
                alertsRecord.setOperationTime(new Date());
                alertsRecord.setOperationStatus("1");
                alertsRecord.setContent(content);
                alertsRecordDao.insert(alertsRecord);
            }
        }

        AlertsHis alertsHisQuery = new AlertsHis();
        alertsHisQuery.setOrderId(oldOrderId);
        List<AlertsHis> alertsHisList = alertHisDao.select(alertsHisQuery);
        for (AlertsHis alertsHis : alertsHisList) {
            alertsHis.setOrderId(orderId);
            alertsHis.setOrderType(type);
            alertsHis.setOrderStatus(orderStatus);
            alertHisDao.updateByPrimaryKey(alertsHis);
        }
        return "OK";
    }

    /**
     * 根据id更新告警监控时间
     * @param alertId
     * @param curMoniTime
     */
    public void updateCurMoniTime(String alertId, Date curMoniTime) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("alertId", alertId);
        map.put("curMoniTime", curMoniTime);
        alertsDao.updateCurMoniTime(map);
    }

    @Override
    public int selectAlert(PageRequest pageRequest) {
        Page page = PageUtil.convert(pageRequest);
        return alertsStatisticDao.selectAlert(page);
    }

    @Override
    public int getAlertValue(Map<String, List<String>> ipMap, List<String> alertLevel, List<String> itemIdList) {
        return alertsDao.getAlertValue(ipMap, alertLevel, itemIdList);
    }

    /**
     *  列头柜相关设备数量查询
     * @param map
     * @return
     */
    @Override
    public AlertDeviceInformationVo queryAlertHList(Map<String, Object> map ) {
        return alertsDao.queryAlertHList(map);
    }

    /**
     * 机柜相关设备数量查询
     * @param map
     * @return
     */
    @Override
    public AlertDeviceInformationVo queryAlertMList(Map<String, Object> map) {
        return alertsDao.queryAlertMList(map);
    }

    @Override
    public Map<String,Object> selectAlertByAlertId(String alertId) {
        return alertsDao.selectAlertByAlertId(alertId);
    }
}

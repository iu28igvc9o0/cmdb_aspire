package com.aspire.mirror.alert.server.biz.helper;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.aspire.mirror.alert.server.biz.alert.AlertsBizV2;
import com.aspire.mirror.alert.server.biz.alert.AlertsHisBizV2;
import com.aspire.mirror.alert.server.biz.derive.IAlertDeriveAlertsBizV2;
import com.aspire.mirror.alert.server.biz.isolate.IAlertIsolateAlertsHisBizV2;
import com.aspire.mirror.alert.server.biz.model.AlertFieldBiz;
import com.aspire.mirror.alert.server.vo.alert.AlertsVo;
import com.aspire.mirror.alert.server.vo.alert.ZabbixAlertV2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.alert.server.biz.bpm.IBpmTaskService;
import com.aspire.mirror.alert.server.constant.AlertCommonConstant;
import com.aspire.mirror.alert.server.constant.Constants;
import com.aspire.mirror.alert.server.dao.derive.AlertDeriveMapper;
import com.aspire.mirror.alert.server.dao.isolate.AlertIsolateMapper;
import com.aspire.mirror.alert.server.dao.primarySecondary.AlertPrimarySecondaryMapper;
import com.aspire.mirror.alert.server.dao.common.AlertScheduleIndexDao;
import com.aspire.mirror.alert.server.dao.alertStandard.AlertStandardDao;
import com.aspire.mirror.alert.server.dao.derive.po.AlertDerive;
import com.aspire.mirror.alert.server.dao.isolate.po.AlertIsolate;
import com.aspire.mirror.alert.server.dao.primarySecondary.po.AlertPrimarySecondary;
import com.aspire.mirror.alert.server.dao.alertStandard.po.AlertStandard;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsDetail;
import com.aspire.mirror.alert.server.vo.derive.AlertDeriveAlertsVo;
import com.aspire.mirror.alert.server.vo.primarySecondary.AlertPrimarySecondaryAlertsVo;
import com.aspire.mirror.alert.server.constant.AlertConfigConstants;
import com.aspire.mirror.alert.server.constant.OperateStatusEnum;
import com.aspire.mirror.alert.server.constant.OrderStatusEnum;
import com.aspire.mirror.alert.server.dao.businessAlert.AlertConfigBusinessMapper;
import com.aspire.mirror.alert.server.dao.exceptionAlert.AlertConfigExceptionMapper;
import com.aspire.mirror.alert.server.dao.derive.AlertDeriveAlertsV2Mapper;
import com.aspire.mirror.alert.server.dao.isolate.AlertIsolateAlertsV2Mapper;
import com.aspire.mirror.alert.server.dao.primarySecondary.AlertPrimarySecondaryAlertsV2Mapper;
import com.aspire.mirror.alert.server.dao.businessAlert.po.AlertConfigBusiness;
import com.aspire.mirror.alert.server.dao.exceptionAlert.po.AlertConfigException;
import com.aspire.mirror.alert.server.dao.derive.po.AlertDeriveAlertsV2;
import com.aspire.mirror.alert.server.vo.model.AlertFieldVo;
import com.aspire.mirror.alert.server.dao.primarySecondary.po.AlertPrimarySecondaryAlertsV2;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsV2;
import com.aspire.mirror.alert.server.vo.alert.AlertsV2Vo;
import com.aspire.mirror.alert.server.vo.alert.AlertsHisV2Vo;
import com.aspire.mirror.alert.server.util.AlertModelCommonUtil;
import com.aspire.mirror.alert.server.util.Criteria;
import com.aspire.mirror.common.constant.SystemConstant;
import com.aspire.mirror.common.util.DateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;
import sun.net.util.IPAddressUtil;

/**
 * 告警处理帮助类, 告警、自动消警、手工消警  都封装在此类中    <br/>
 * Project Name:alert-metrics
 * File Name:AlertsHandleHelper.java
 * Package Name:com.aspire.mirror.alert.server.biz
 * ClassName: AlertsHandleHelper <br/>
 * date: 2018年10月12日 上午10:40:07 <br/>
 *
 * @author pengguihua
 * @since JDK 1.6
 */
@Slf4j
@Service
public class AlertsHandleV2Helper {


    public static final String ALERT_TYPE = "alarmType";

    @Autowired
    protected AlertsBizV2 alertsBiz;
    @Autowired
    protected AlertsHisBizV2 alertsHisBiz;
    @Autowired
    private AlertIsolateMapper alertIsolateMapper;
    @Autowired
    private AlertIsolateAlertsV2Mapper alertIsolateAlertsMapper;
    @Autowired
    private IAlertIsolateAlertsHisBizV2 alertIsolateAlertsHisBizV2;
    @Autowired
    private AlertDeriveMapper alertDeriveMapper;
    @Autowired
    private AlertDeriveAlertsV2Mapper alertDeriveAlertsMapper;
    @Autowired
    private IAlertDeriveAlertsBizV2 alertDeriveAlertsBizV2;
    @Autowired
    private AlertPrimarySecondaryMapper alertPrimarySecondaryMapper;
    @Autowired
    private AlertPrimarySecondaryAlertsV2Mapper alertPrimarySecondaryAlertsMapper;
    @Autowired
    private AlertFieldBiz alertFieldBiz;
    @Autowired
    private IBpmTaskService iBpmTaskService;
    @Autowired
    private AlertConfigBusinessMapper alertConfigBusinessMapper;
    @Autowired
    private AlertConfigExceptionMapper alertConfigExceptionMapper;
    @Autowired
    private AlertStandardDao alertStandardDao;
    
    @Autowired
    private AlertScheduleIndexDao alertScheduleIndexDao;
    @Autowired
    private CabinetColumnAlertHandler cabinetColumnAlertHandler;
    
    @Autowired
    private CmdbHelper cmdbHelper;

    @Value("${alert.check.level:2,3,4,5}")
    private String checkLevel;

    @Value("${systemType:normal}")
    private String systemType;

    @Value("${bdc_notify_url:http://localhost:8888/bmserver/api/alert/v1.0/notify.ajax}")
    private String bdcNotifyUrl;
    
    @Value("${AlertCabinetColumnTask.flag:fnormal}")
    private String cabinetColumnFalg;

    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Autowired
	private ThreadPoolTaskExecutor taskExecutor;
    
    @Value("${AlertCabinetColumnTask.itemInfo:}")
	private String itemInfo;


//    @Transactional
    public String handleAlert(AlertsV2Vo alert) {
        String alertId = null;
        Date alertStartTime = alert.getAlertStartTime();
        Date curMoniTime = alert.getCurMoniTime();
        if (alertStartTime == null) {
            alert.setAlertStartTime(curMoniTime);
        }
        if (curMoniTime == null) {
            alert.setCurMoniTime(alertStartTime);
        }
        List<AlertFieldVo> alertFieldList = alertFieldBiz.getModelField(AlertConfigConstants.REDIS_MODEL_ALERT);

        if (alert.getObjectType().equals(AlertsV2Vo.OBJECT_TYPE_BIZ) && com.aspire.mirror.alert.server.util.StringUtils.isEmpty(alert.getMoniObject())) {
            alert.setMoniObject("Application");
//            alert.setObjectId(alert.getBizSys());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = "{}";
        try {
            jsonString = objectMapper.writeValueAsString(alert);
        } catch (JsonProcessingException e) {
        }
        JSONObject alertJson = JSONObject.parseObject(jsonString);
        Map<String, Object> ext = alert.getExt();
        cmdbHelper.doExt (alertJson, alertFieldList, ext);
        //处理cmdb数据
        if (com.aspire.mirror.alert.server.util.StringUtils.isNotEmpty(alert.getDeviceIp()) && org.springframework.util.CollectionUtils.isEmpty(ext)){
            // 根据  机房 + IP, 查找设备
            cmdbHelper.queryDeviceForAlertV2(alertJson, alertFieldList, alertFieldBiz.getModelField(AlertConfigConstants.REDIS_MODEL_DEVICE_INSTANCE));
        }
        alertId = handleAlert(alert, alertJson, alertFieldList);
        return alertId;
    }

    /**
     * 处理告警. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param alert
     * @throws Exception
     */
    @Transactional
    public String handleAlert(AlertsV2Vo alert, JSONObject alertJson, List<AlertFieldVo> alertFieldList) {
        String alertId = null;
        if (!check(alert)) {
            return alertId;
        }

        //设置告警名称
        setKeyComment(alert, alertJson);

        // 判断连通性状态告警给cmdb
        alertsBiz.putPingStatusToKafka(alert, alertJson.getString("device_type"));

        //屏蔽告警逻辑
        if (isolateAlert(alert, alertJson)) {
        	 checkCabinetColumnAlert(alert, alertJson);
            return alertId;
        }

        //衍生告警逻辑
        if (deriveAlert(alert, alertJson, alertFieldList)) {
        	 checkCabinetColumnAlert(alert, alertJson);
            return alertId;
        }

        //主次告警逻辑
        if (primarySecondaryAlert(alert, alertJson)) {
        	 checkCabinetColumnAlert(alert, alertJson);
            return alertId;
        }

        // 判断异常信息
        if (!checkExceptionAlert(alert, alertJson)) {
            // 配置业务告警
            checkBusinessAlert(alert, alertJson);
        }

        if (AlertsV2Vo.ALERT_ACTIVE.equals(alert.getAlertType())) {
            alertId = this.handleAlertActive(alert, alertJson, alertFieldList);
        } else if (AlertsV2Vo.ALERT_REVOKE.equals(alert.getAlertType())) {
            this.handleAlertRevoke(alert, alertJson, alertFieldList);
        } else {
            log.error("The alert has no alertType to indicate whether it is alert-active or alert-revoke, "
                    + "detail: {}", alert);
        }
      //判断是否开启列头柜
       
        checkCabinetColumnAlert(alert, alertJson);
        
        
        return alertId;
    }
    
  //判断电源告警和机柜告警
    private void checkCabinetColumnAlert(AlertsV2Vo alert, JSONObject alertJson) {
    	 if(!cabinetColumnFalg.equals("normal")) {
    		 return;
    	 }
    	log.info("判断是否机柜或者电源告警或者消警_开始");
    	String idcCabinet = alertJson.getString("idc_cabinet");
		if(StringUtils.isEmpty(idcCabinet)) {
			return;
		}
		String deviceClass = alertJson.getString("device_class");//TODO
		String keyComment = alert.getKeyComment();
		String source = alert.getSource();
		String key = String.format("%s_%s_%s", deviceClass,keyComment,source); 
		if(StringUtils.isEmpty(keyComment)) {
			keyComment = "";
		}
		if(StringUtils.isEmpty(source)) {
			source = "";
		}
		//判断是否机电告警
		if(source.equals(AlertCommonConstant.CABINET_COLUMN_SOURCE) && keyComment.equals(AlertCommonConstant.CABINET_ALERT_TITLE)) {
			CabinetColumnThread thread = new CabinetColumnThread(alert,alertJson,null,1);
			taskExecutor.execute(thread);
			return;
		}
	
		//判断是否电源告警
		boolean flag = false;
			JSONArray array = JSONArray.parseArray(itemInfo);
	        for(int i=0;i<array.size();i++) {
	        	JSONObject a = array.getJSONObject(i);
	        	String key2 =  String.format("%s_%s_%s", a.getString("deviceClass"),a.getString("keyComment")
	        	,a.getString("source")); 
	        	if(key.equalsIgnoreCase(key2)) {
	        		flag = true;
	        		break;
	        	}
	        }
			
		if(flag) {
			
			CabinetColumnThread thread = new CabinetColumnThread(alert,alertJson,array,2);
			taskExecutor.execute(thread);
		}
		log.info("判断是否机柜或者电源告警或者消警_结束");
    }
    
public class CabinetColumnThread implements  Runnable{
    	
    	
        private AlertsV2Vo alert;
        private JSONObject alertJson;
        private JSONArray array;//电源配置项
        private int type;///1机柜告警2电源告警
    	    public  CabinetColumnThread(AlertsV2Vo alert, JSONObject alertJson, JSONArray array, int type)
    	    {
    	        this.alert = alert;
    	        this.alertJson = alertJson;
    	        this.array = array;
    	        this.type = type;
    	    }
    	    public  void  run()
    	    {
    	    	log.info("处理机柜或者电源告警或者消警_开始：alert:{},json:{},type:{}",alert,alertJson,type);
    			try {
    				String alertType = alert.getAlertType();
    				if(AlertsV2Vo.ALERT_ACTIVE.equals(alertType)) {
    					if(type==2) {//电源告警
    						cabinetColumnAlertHandler.setIdcCabinetMap(alertJson, 2);
    						cabinetColumnAlertHandler.handlePowerAlert( alertJson, array);
    					}else {//机柜告警
    						cabinetColumnAlertHandler.setIdcCabinetMap(alertJson, 1);
    						cabinetColumnAlertHandler.handleCabinetAlert(alertJson,array);
    					}
    				} else if (AlertsV2Vo.ALERT_REVOKE.equals(alertType)) {
    					if(type==2) {//电源消警
    						cabinetColumnAlertHandler.setIdcCabinetMap(alertJson, 2);
    						cabinetColumnAlertHandler.handleCabinetAlertHis(alertJson,array);
    					}else {//机柜消警
    						cabinetColumnAlertHandler.setIdcCabinetMap(alertJson, 1);
    						cabinetColumnAlertHandler.handleCabinetColumnAlertHis(alertJson,array);
    					}
    		        } else {
    		        	log.error("The alert has no alertType to indicate whether it is alert-active or alert-revoke, "
    		                    + "detail: {}", alert);
    		        }
    			} catch (Exception e) {
    				log.error("处理列头柜告警数据报错", e);
    				log.error("处理列头柜告警数据报错：type:{},json:{},alert:{}",type, alertJson,alert);
    			}
    			log.info("处理机柜或者电源告警或者消警_开始");
    	    }
    		
    }

    /**
     * 处理告警激活. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param alert
     * @throws Exception
     */
    @Transactional
    String handleAlertActive(AlertsV2Vo alert, JSONObject alertJson, List<AlertFieldVo> alertFieldList) {
        log.debug("Begin to handle alert active for alert with rawId {}", alert.getRAlertId());

        String alertId = null;
        //查询告警是否存在，zabbix告警ralert可以确认是否存在；其他告警场景需要通过ip+itemid+level确认相同告警
        AlertsV2 alertQuery = new AlertsV2();
        if (AlertsV2Vo.OBJECT_TYPE_BIZ.equals(alert.getObjectType())) {
            alertQuery.setBizSys(alert.getBizSys());
        } else {
            alertQuery.setDeviceIp(alert.getDeviceIp());
        }
        alertQuery.setItemId(alert.getItemId());
        alertQuery.setAlertLevel(alert.getAlertLevel());
        alertQuery.setSource(alert.getSource());
        List<AlertsV2Vo> queryList = alertsBiz.select(alertQuery);
        if (CollectionUtils.isNotEmpty(queryList)) {
            AlertsV2Vo alertN = queryList.get(0);
            alertId = alertN.getAlertId();
            //更新告警基本信息
            alertN.setMoniIndex(alert.getMoniIndex());
            alertN.setMoniObject(alert.getMoniObject());
            alertN.setCurMoniValue(alert.getCurMoniValue());
            Date curMoniTime = alertN.getCurMoniTime();
            Date curMoniTime1 = alert.getCurMoniTime();
            if (curMoniTime == null || (curMoniTime1 != null && curMoniTime1.after(curMoniTime))) {
                alertN.setCurMoniTime(alert.getCurMoniTime());
            }
            if (StringUtils.isNotEmpty(alert.getRAlertId()) && alert.getRAlertId().startsWith(Constants.PREFIX_PRIMARY)) {
                alertN.setRAlertId(alert.getRAlertId());
            }
            if (StringUtils.isEmpty(alertN.getRemark())) {
                alertN.setRemark(alert.getRemark());
            }
            if (StringUtils.isNotEmpty(alert.getBizSys())) {
                alertN.setBizSys(alert.getBizSys());
            }
            if (StringUtils.isNotEmpty(alert.getIdcType())) {
                alertN.setIdcType(alert.getIdcType());
            }
            if (StringUtils.isNotEmpty(alert.getItemKey())) {
                alertN.setItemKey(alert.getItemKey());
            }
            if (StringUtils.isNotEmpty(alert.getKeyComment())) {
                alertN.setKeyComment(alert.getKeyComment());
            }
            if (alert.getOperateStatus() != null &&
                    !(alert.getOperateStatus() == OperateStatusEnum.TO_DO.getCode() && alertN.getOperateStatus() == OperateStatusEnum.DOING.getCode())) {
                alertN.setOperateStatus(alert.getOperateStatus());
            }
            //数据库更新时候自增数量，减少时间开销，预防数量上对不上
            if (alertN.getAlertCount() == null) {
                alertN.setAlertCount(1);
            }
            alertN.setAlertCount(alertN.getAlertCount() + 1);
            String jsonString = "{}";
            try {
                jsonString = objectMapper.writeValueAsString(alertN);
            } catch (JsonProcessingException e) {
            }
            JSONObject alertNJson = JSONObject.parseObject(jsonString);
            alertJson.putAll(alertNJson);
            alertsBiz.updateByPrimaryKey(AlertModelCommonUtil.generateAlerts(alertJson, alertFieldList));
        } else {

            // 如果为simple类型系统则调别人的通知接口直接通知
            try {
                if (systemType.equals(SystemConstant.BIZ_SYSTEM_BDC) && alert.getObjectType().equals("2")) {
                    log.info("===begin send  bdc notify url " + bdcNotifyUrl);
                    RestTemplate restTemplate = new RestTemplate();
                    alert.setAlertId(alertId);
                    JSONArray jsonArray = JSON.parseArray(alert.getRemark());
                    String alertType = "";
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        if (jsonObj.get("dimCode").equals(ALERT_TYPE)) {
                            alertType = jsonObj.getString("value");
                        }
                    }
                    alert.setAlertType(alertType);
                    // 如果没有数据情况默认服务异常告警类型
                    if (StringUtils.isEmpty(alertType)) {
                        alert.setAlertType("01");
                    }
                    ResponseEntity<Map> responseEntity = restTemplate.postForEntity(bdcNotifyUrl, alert, Map
                            .class);
                    log.info("===bdc send notify result is : {}===", JSON.toJSONString(responseEntity.getBody()));
                    AlertsV2Vo updateAlert = new AlertsV2Vo();
                    updateAlert.setAlertId(alertId);
                    Map<String, Object> bodyMap = responseEntity.getBody();
                    if (bodyMap != null && bodyMap.get("data") != null) {
                        Map<String, Object> data = (Map<String, Object>) bodyMap.get("data");
                        log.info("===mailSend:{}, mailOpen:{}, messageOpen:{}, messageSend:{}", data.get("mailSend").toString(), data.get("mailOpen").toString(), data.get("messageOpen").toString());
                        alertJson.put("mail_send", data.get("mailSend").toString());
                        alertJson.put("mail_open", data.get("mailOpen").toString());
                        alertJson.put("message_open", data.get("messageOpen").toString());
                        alertJson.put("message_send", data.get("messageSend").toString());
                        log.info("===update alert is {}", updateAlert);
//                        alertsBiz.updateByPrimaryKey(updateAlert);
                    }
                }
            } catch (Exception e) {
                log.info("bdc通知失败");
            }
            if (alertJson.get("create_time") == null) {
                alertJson.put("create_time", DateUtil.formatDate(DateUtil.DATE_TIME_CH_FORMAT));
            }
            alertId = alertsBiz.insert(AlertModelCommonUtil.generateAlerts(alertJson, alertFieldList));
        }

        //添加上报记录
        insertAlertsDetail(alertId, alert);
        return alertId;
    }

    public boolean check(AlertsV2Vo alert) {
        if (StringUtils.isBlank(alert.getRAlertId()) && ZabbixAlertV2.SOURCE_ZABBIX.equalsIgnoreCase(alert.getSource())) {
            log.error("The alertDTO is invalid as the alertId is absent.");
            return false;
        }
        String alertLevel = alert.getAlertLevel();
        if (StringUtils.isBlank(alertLevel) || !StringUtils.isNumeric(alertLevel)) {
            log.error("The alertDTO is invalid as the alertLevel is absent.");
            return false;
        }
        if (StringUtils.isNotEmpty(checkLevel) && !checkLevel.contains(alertLevel)) {
            log.error("The alertDTO is invalid as the alertLevel is not legal.");
            return false;
        }
//        if (StringUtils.isBlank(alert.getDeviceIp()) && StringUtils.isBlank(alert.getBizSys())) {
//            log.error("The alertDTO is invalid as the deviceIp and bizSys are absent.");
//            return false;
//        }
        if (AlertsV2Vo.OBJECT_TYPE_DEVICE.equals(alert.getObjectType())
                && !StringUtils.isBlank(alert.getDeviceIp())
                && !IPAddressUtil.isIPv4LiteralAddress(alert.getDeviceIp())
                && !IPAddressUtil.isIPv6LiteralAddress(alert.getDeviceIp())) {
            log.error("The alertDTO is invalid as the deviceIp and bizSys are legal.");
            return false;
        }
        if (StringUtils.isBlank(alert.getMoniIndex())) {
            log.error("The alertDTO is invalid as the moniIndex is absent.");
            return false;
        }
        return true;
    }

    /**
     * 处理消警. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param alert
     * @throws Exception
     */
    private void handleAlertRevoke(AlertsV2Vo alert, JSONObject alertJson, List<AlertFieldVo> alertFieldList) {
        log.debug("Begin to handle alert revoke for alert with rawId {}", alert.getAlertId());
        String alertId = null;

        //查询告警是否存在，zabbix告警ralert可以确认是否存在；其他告警场景需要通过ip+itemid+level确认相同告警
        AlertsV2 alertQuery = new AlertsV2();
        if (AlertsV2Vo.OBJECT_TYPE_BIZ.equals(alert.getObjectType())) {
            alertQuery.setBizSys(alert.getBizSys());
        } else {
            alertQuery.setDeviceIp(alert.getDeviceIp());
        }
        alertQuery.setItemId(alert.getItemId());
        alertQuery.setAlertLevel(alert.getAlertLevel());
        alertQuery.setSource(alert.getSource());
        List<AlertsV2Vo> queryList = alertsBiz.select(alertQuery);
        if (CollectionUtils.isNotEmpty(queryList)) {
            for (int i = 0; i < queryList.size(); i++) {
                AlertsV2Vo alertDto = queryList.get(i);
                AlertsHisV2Vo hisDto = buildAlertHisDto(alertDto);
                if (i == 0) {
                    alertId = hisDto.getAlertId();
                    hisDto.setAlertCount(hisDto.getAlertCount() + 1);
                }
                hisDto.setAlertEndTime(alert.getCurMoniTime());
                if (StringUtils.isNotEmpty(hisDto.getOrderId()) && "2".equals(hisDto.getOrderStatus())) {
                    hisDto.setOrderStatus(OrderStatusEnum.FINISH.getCode());
                }
                String jsonString = "{}";
                try {
                    jsonString = objectMapper.writeValueAsString(hisDto);
                } catch (JsonProcessingException e) {
                    log.error("ObjectMapper 解析失败 ------ {}",e);
                }
                JSONObject alertNJson = JSONObject.parseObject(jsonString);
                alertJson.putAll(alertNJson);
                // 消警
                alertsHisBiz.insert(AlertModelCommonUtil.generateAlerts(alertJson, alertFieldBiz.getModelField(AlertConfigConstants.REDIS_MODEL_ALERT_HIS)));
                alertsBiz.deleteByPrimaryKeyArrays(new String[]{alertDto.getAlertId()});
                // 关闭工单
                if (StringUtils.isNotEmpty(hisDto.getOrderId())) {
                    iBpmTaskService.closeBpm(hisDto.getOrderId(), "alert");
                }
            }
        } else {
            //没有可消除的告警，忽略此条消警
            log.warn("Received alert revoke data with the raw alertId {}, but there is no alert record "
                    + "with logicAlertId {} in DB.", alertId, alertId);
            return;
        }

        //添加上报记录
        insertAlertsDetail(alertId, alert);
    }

    /**
     * 插入告警记录
     * @param alertId
     * @param alert
     */
    private void insertAlertsDetail(String alertId, AlertsV2Vo alert) {
        if (StringUtils.isEmpty(alertId)) {
            return;
        }
        AlertsDetail alertsDetail = new AlertsDetail();
//        alertsDetail.setActionId(alert.getActionId());
        alertsDetail.setAlertId(alertId);
        alertsDetail.setAlertLevel(alert.getAlertLevel());
        alertsDetail.setCurMoniTime(alert.getCurMoniTime());
        alertsDetail.setCurMoniValue(alert.getCurMoniValue());
//        alertsDetail.setEventId(alert.getEventId());
        alertsDetail.setMoniIndex(alert.getMoniIndex());
        alertsDetail.setMoniObject(alert.getMoniObject());
        alertsDetail.setItemId(alert.getItemId());
        alertsBiz.insertAlertsDetail(alertsDetail);
    }

    private AlertsHisV2Vo buildAlertHisDto(AlertsV2Vo alertDto) {
        AlertsHisV2Vo hisDto = new AlertsHisV2Vo();
        BeanUtils.copyProperties(alertDto, hisDto);
        return hisDto;
    }

    /**
     * 根据规则过滤告警
     * @param alertJson
     * @return
     */
    private boolean isolateAlert (AlertsV2Vo alert, JSONObject alertJson) {
        if (alertJson == null) {
            return true;
        }
        //解析告警实体为符合告警规则的json格式
        //查询过滤规则，用于过滤告警
        List<AlertIsolate> list = alertIsolateMapper.listEffective();
        Long id = null;
        for (AlertIsolate alertIsolate: list) {
            String optionCondition = alertIsolate.getOptionCondition().toLowerCase();
            //条件为空的忽略
            if (StringUtils.isEmpty(optionCondition)) {
                continue;
            }
            if (judgeAlert (alertJson, optionCondition)) {
                id = alertIsolate.getId();
                break;
            }
        }
        //屏蔽成功，插入屏蔽记录
        if (id != null) {
            alertJson.put("isolate_id", id);
            if (AlertsV2Vo.ALERT_ACTIVE.equals(alert.getAlertType())) {
                this.handleIsolateAlertActiveLog(alert, alertJson);
            } else if (AlertsV2Vo.ALERT_REVOKE.equals(alert.getAlertType())) {
                this.handleIsolateAlertRevokeLog(alert, alertJson);
            }
            return true;
        }
        return false;
    }

    /**
     * 根据规则判断过滤告警
     * @param alertJson
     * @param optionCondition
     * @return
     */
    private boolean judgeAlert(JSONObject alertJson, String optionCondition) {
        JSONObject jsonObject = JSONObject.parseObject(optionCondition);
        JSONArray objects = jsonObject.getJSONArray("data");
        //条件为空的忽略
        if (objects == null || objects.isEmpty()) {
            return false;
        }
        //逐条解析条件
        for (int i = 0; i < objects.size(); i++) {
            JSONObject andJson = objects.getJSONObject(i);
            JSONArray andlist = andJson.getJSONArray("data");
            if (andlist == null || andlist.isEmpty()) {
                continue;
            }
            // 判断条件,单条规则所有条件都符合才能屏蔽
            boolean isolateFlag = true;
            boolean updateFlag = false;
            for (int j = 0; j < andlist.size(); j++) {
                JSONObject val = andlist.getJSONObject(j);
                String filterItemName = val.getString("filteritemname");
                String operate = val.getString("operate").toLowerCase();
                String value = val.getString("value");
//                    String jdbcType = val.getString("jdbcType");
                String alertValue = alertJson.getString(filterItemName);
                // 字段为空直接判断失败
                if (StringUtils.isEmpty(alertValue) || StringUtils.isEmpty(value) ) {
                    isolateFlag = false;
                    break;
                }
                alertValue = alertValue.toLowerCase();
                updateFlag = true;
                switch (operate) {
                    case "like":
                        if (!alertValue.contains(value)) {
                            isolateFlag = false;
                        }
                        break;
                    case "=":
                        if (!alertValue.equals(value)) {
                            isolateFlag = false;
                        }
                        break;
                    case "!=":
                        if (alertValue.equals(value)) {
                            isolateFlag = false;
                        }
                        break;
                    case ">":
                        if (StringUtils.isNumeric(alertValue) && StringUtils.isNumeric(value)) {
                            if (Long.valueOf(alertValue) <= Long.valueOf(value)) {
                                isolateFlag = false;
                            }
                        } else {
                            if (alertValue.compareTo(value) <= 0) {
                                isolateFlag = false;
                            }
                        }
                        break;
                    case ">=":
                        if (StringUtils.isNumeric(alertValue) && StringUtils.isNumeric(value)) {
                            if (Long.valueOf(alertValue) < Long.valueOf(value)) {
                                isolateFlag = false;
                            }
                        } else {
                            if (alertValue.compareTo(value) < 0) {
                                isolateFlag = false;
                            }
                        }
                        break;
                    case "<":
                        if (StringUtils.isNumeric(alertValue) && StringUtils.isNumeric(value)) {
                            if (Long.valueOf(alertValue) >= Long.valueOf(value)) {
                                isolateFlag = false;
                            }
                        } else {
                            if (alertValue.compareTo(value) >= 0) {
                                isolateFlag = false;
                            }
                        }
                        break;
                    case "<=":
                        if (StringUtils.isNumeric(alertValue) && StringUtils.isNumeric(value)) {
                            if (Long.valueOf(alertValue) > Long.valueOf(value)) {
                                isolateFlag = false;
                            }
                        } else {
                            if (alertValue.compareTo(value) > 0) {
                                isolateFlag = false;
                            }
                        }
                        break;
                    case "in":
                        try {
                            JSONArray inValJson = JSON.parseArray(value);
                            if (inValJson == null || !inValJson.contains(alertValue)) {
                                isolateFlag = false;
                            }
                        } catch (JSONException e) {
                            List<String> inList = Arrays.asList(value.split(","));
                            if (inList == null || !inList.contains(alertValue)) {
                                isolateFlag = false;
                            }
                        }
                        break;
                    case "not in":
                        try {
                            JSONArray notValJson = JSON.parseArray(value);
                            if (notValJson != null && notValJson.contains(alertValue)) {
                                isolateFlag = false;
                            }
                        } catch (JSONException e) {
                            List<String> notList = Arrays.asList(value.split(","));
                            if (notList != null && notList.contains(alertValue)) {
                                isolateFlag = false;
                            }
                        }
                        break;
                    default:
                        // 包含规则之外的配置，一律判定失败
                        isolateFlag = false;
                        break;
                }
                // 过滤失败，跳出循环
                if (!isolateFlag) {
                    break;
                }
            }
            //所有条件符合，返回屏蔽状态
            if (isolateFlag && updateFlag) {
                log.info("judge alert success, alert is : {}, optionCondition is : {}", alertJson.toJSONString(), optionCondition);
                return true;
            }
        }
        return false;
    }

    /**
     * 根据规则过滤衍生告警
     * @param alert
     * @return
     */
    private boolean deriveAlert (AlertsV2Vo alert, JSONObject alertJson, List<AlertFieldVo> alertFieldList) {
        if (alert == null) {
            return true;
        }

        //查询过滤规则，用于过滤告警
        List<AlertDerive> list = alertDeriveMapper.listEffective();
        AlertDerive alertDeriveSelected = null;
        for (AlertDerive alertDerive: list) {
            String optionCondition = alertDerive.getOptionCondition().toLowerCase();
            //条件为空的忽略
            if (StringUtils.isEmpty(optionCondition)) {
                continue;
            }
            if (judgeAlert (alertJson, optionCondition)) {
                alertDeriveSelected = alertDerive;
                break;
            }
        }
        //屏蔽成功，插入衍生记录
        if (alertDeriveSelected != null) {
            if (AlertsV2Vo.ALERT_REVOKE.equals(alert.getAlertType())) {
                handleDeriveAlertRevokeLog(alert, alertJson, alertDeriveSelected);
            } else {
                handleDeriveAlertActiveLog(alert, alertDeriveSelected, alertJson, alertFieldList);
            }
            return true;
        }
        return false;
    }

    /**
     * 创建衍生告警
     * @param alert
     * @param alertDeriveSelected
     * @return
     */
    private String createDeriveAlert (AlertsV2Vo alert, AlertDerive alertDeriveSelected, JSONObject alertJson, List<AlertFieldVo> alertFieldList) {
        String alertSetting = alertDeriveSelected.getAlertSetting();
        Integer alertThreshold = alertDeriveSelected.getAlertThreshold();
        Integer deriveActiveTimeout = alertDeriveSelected.getDeriveActiveTimeout();
        //默认五分钟
        if (deriveActiveTimeout == null) {
            deriveActiveTimeout = 5;
        }
        Map<String, Object> queryMap = Maps.newHashMap();
        queryMap.put("deriveId", alertDeriveSelected.getId());
        if (Constants.DEVICE_RELATION_TYPE_SAME.equals(alertDeriveSelected.getDeviceRelationType())
                && StringUtils.isNotEmpty(alert.getDeviceIp())) {
            queryMap.put("deviceIp", alert.getDeviceIp());
            queryMap.put("idcType", alert.getIdcType());
        }
        Long mills = System.currentTimeMillis() - deriveActiveTimeout * 60 * 1000;
        String date = DateUtil.format(new Date(mills), "yyyy-MM-dd HH:mm:ss");
        queryMap.put("date", date);
        List<Map<String, Object>> mapList = alertDeriveAlertsMapper.queryDeriveAlertIdByDeriveId(queryMap);
        //如果还没有衍生记录，直接插入一条
        if (CollectionUtils.isEmpty(mapList)) {
            return insertDeriveAlert(alert, alertSetting, alertDeriveSelected.getDeviceRelationType(),alertJson, alertFieldList);
        }
        Map<String, Object> map = mapList.get(0);
        String deriveAlertId = String.valueOf(map.get("derive_alert_id"));
        queryMap.put("deriveAlertId", deriveAlertId);
        mapList = alertDeriveAlertsMapper.queryCountByDeriveId(queryMap);
        int count = 0;
        if (CollectionUtils.isEmpty(mapList)) {
            count = Integer.parseInt(String.valueOf(mapList.get(0).get("count")));
        }

        //如果衍生记录超过阈值，新增一条衍生告警
        if (alertThreshold != null && alertThreshold <= count) {
            return insertDeriveAlert(alert, alertSetting, alertDeriveSelected.getDeviceRelationType(),alertJson, alertFieldList);
        }
        AlertsV2Vo deriveAlertExsit = alertsBiz.selectAlertByPrimaryKey(deriveAlertId);
        //如果还没有衍生记录，直接插入一条
        if (deriveAlertExsit == null) {
            return insertDeriveAlert(alert, alertSetting, alertDeriveSelected.getDeviceRelationType(),alertJson, alertFieldList);
        }
        //如果上报时间超出关联时间窗，新增一条衍生告警
        Long alertMills = deriveAlertExsit.getCreateTime() == null
                ? (deriveAlertExsit.getAlertStartTime() == null ? System.currentTimeMillis() : deriveAlertExsit.getAlertStartTime().getTime())
                : deriveAlertExsit.getCreateTime().getTime();
        if (mills >= alertMills) {
            return insertDeriveAlert(alert, alertSetting, alertDeriveSelected.getDeviceRelationType(), alertJson, alertFieldList);
        }
        //不满足条件，返回当前衍生告警ID
        return deriveAlertId;
    }

    /**
     * 插入衍生告警，衍生告警不走通用告警，需要单独插入
     * @param alert
     * @param alertSetting
     * @return
     */
    private String insertDeriveAlert (AlertsV2Vo alert, String alertSetting, String deviceRelationType, JSONObject alertJson, List<AlertFieldVo> alertFieldList) {
        //根据告警生成设置，转换衍生告警
        AlertsV2Vo deriveAlertDTO = JSONObject.parseObject(alertSetting, AlertsV2Vo.class);
        Date now = new Date();
        deriveAlertDTO.setCurMoniTime(now);
        deriveAlertDTO.setAlertStartTime(now);
        deriveAlertDTO.setAlertType(alert.getAlertType());
        deriveAlertDTO.setAlertCount(1);
        if (Constants.DEVICE_RELATION_TYPE_SAME.equals(deviceRelationType)) {
            deriveAlertDTO.setDeviceIp(alert.getDeviceIp());
            deriveAlertDTO.setBizSys(alert.getBizSys());
            deriveAlertDTO.setIdcType(alert.getIdcType());
        }
        if (StringUtils.isEmpty(deriveAlertDTO.getMoniIndex())) {
            deriveAlertDTO.setMoniIndex(alert.getMoniIndex());
        }
        //插入数据

        String jsonString = "{}";
        try {
            jsonString = objectMapper.writeValueAsString(deriveAlertDTO);
        } catch (JsonProcessingException e) {
        }
        JSONObject alertNJson = JSONObject.parseObject(jsonString);
        JSONObject alertJsonNN = JSONObject.parseObject(alertSetting);
        alertJson.putAll(alertJsonNN);
        alertJson.putAll(alertNJson);
        // 判断异常信息
        if (!checkExceptionAlert(alert, alertJson)) {
            // 配置业务告警
            checkBusinessAlert(alert, alertJson);
        }
        String alertId = alertsBiz.insert(AlertModelCommonUtil.generateAlerts(alertJson, alertFieldList));

        //添加上报记录
        insertAlertsDetail(alertId, deriveAlertDTO);
        return alertId;
    }

    /**
     * getUUID
     * @return
     */
    private String getUUID () {
        return UUID.randomUUID().toString();
    }

    /**
     * 消除衍生记录及告警
     * @param alert
     */
    private void handleDeriveAlertRevokeLog (AlertsV2Vo alert, JSONObject alertJson, AlertDerive alertDeriveSelected) {
        long l = System.currentTimeMillis();
        String alertId = null;

        //查询告警是否存在，zabbix告警ralert可以确认是否存在；其他告警场景需要通过ip+itemid+level确认相同告警
        AlertDeriveAlertsVo alertQuery = new AlertDeriveAlertsVo();
        if (AlertsV2Vo.OBJECT_TYPE_BIZ.equals(alert.getObjectType())) {
            alertQuery.setBizSys(alert.getBizSys());
        } else {
            alertQuery.setDeviceIp(alert.getDeviceIp());
        }
        alertQuery.setItemId(alert.getItemId());
        alertQuery.setAlertLevel(alert.getAlertLevel());
        alertQuery.setSource(alert.getSource());
        alertQuery.setAlertType(AlertsV2Vo.ALERT_ACTIVE);
        List<AlertDeriveAlertsV2> queryList = alertDeriveAlertsMapper.select(alertQuery);
        log.info("---%%%%%%%%%------1.handleDeriveAlertRevokeLog query derive alert log use {} ms------------", (System.currentTimeMillis() - l));
        if (CollectionUtils.isNotEmpty(queryList)) {
            List<String> exsitIdList = Lists.newArrayList();
            List<String> exsitDeriveAlertIdList = Lists.newArrayList();
            for (int i = 0; i < queryList.size(); i++) {
                AlertDeriveAlertsV2 alertDeriveAlerts = queryList.get(i);
                alertDeriveAlerts.setCurMoniTime(alert.getCurMoniTime());
                alertDeriveAlerts.setAlertType(AlertsV2Vo.ALERT_REVOKE);
                alertDeriveAlertsMapper.updateByEntity(alertDeriveAlerts);
                String deriveAlertId = alertDeriveAlerts.getDeriveAlertId();
                exsitIdList.add(alertDeriveAlerts.getAlertId());
                exsitDeriveAlertIdList.add(deriveAlertId);
                log.info("---%%%%%%%%%------2.handleDeriveAlertRevokeLog update derive alert log use {} ms------------", (System.currentTimeMillis() - l));
            }
            for (int i = 0; i < exsitDeriveAlertIdList.size(); i++) {
                String deriveAlertId = exsitDeriveAlertIdList.get(i);
                AlertDeriveAlertsVo alertExsitQuery = new AlertDeriveAlertsVo();
                alertExsitQuery.setDeriveAlertId(deriveAlertId);
//                alertExsitQuery.setAlertType(AlertsDTO.ALERT_ACTIVE);
                List<AlertDeriveAlertsV2> queryExsitList = alertDeriveAlertsMapper.select(alertExsitQuery);
                log.info("---%%%%%%%%%------3.handleDeriveAlertRevokeLog query exist derive alert log use {} ms------------", (System.currentTimeMillis() - l));
                boolean deriveAlertFlag = true;

//                Integer alertThreshold = alertDeriveSelected.getAlertThreshold();
//                Integer deriveActiveTimeout = alertDeriveSelected.getDeriveActiveTimeout();
//                //默认五分钟
//                if (deriveActiveTimeout == null) {
//                    deriveActiveTimeout = 5;
//                }
//                if (alertThreshold == null) {
//                    alertThreshold = 100;
//                }
//                Map<String, Object> queryMap = Maps.newHashMap();
//                queryMap.put("deriveId", alertDeriveSelected.getId());
//                if (Constants.DEVICE_RELATION_TYPE_SAME.equals(alertDeriveSelected.getDeviceRelationType())
//                        && StringUtils.isNotEmpty(alert.getDeviceIp())) {
//                    queryMap.put("deviceIp", alert.getDeviceIp());
//                    queryMap.put("idcType", alert.getIdcType());
//                }
//                Long mills = System.currentTimeMillis() - deriveActiveTimeout * 60 * 1000;
//                Date date = new Date(mills);

                if (CollectionUtils.isNotEmpty(queryExsitList)) {
//                    Date dateOld = null;
                    for (AlertDeriveAlertsV2 queryExsit: queryExsitList) {
//                        Date createTime = queryExsit.getCreateTime();
//                        if (date.after(createTime)) {
//                            dateOld = createTime;
//                        }
                        if (exsitIdList.contains(queryExsit.getAlertId())) {
                            continue;
                        }
                        if (AlertsV2Vo.ALERT_ACTIVE.equalsIgnoreCase(queryExsit.getAlertType())) {
                            deriveAlertFlag = false;
                        }
                    }
//                    if (alertThreshold > queryExsitList.size() && dateOld == null) {
//                        deriveAlertFlag = false;
//                    }
                }
                //如果衍生记录全部消除，消除衍生告警
                if (deriveAlertFlag) {
                    AlertsV2Vo alertDeleting = alertsBiz.selectAlertByPrimaryKey(deriveAlertId);
                    if (alertDeleting != null) {
                        AlertsHisV2Vo hisDto = buildAlertHisDto(alertDeleting);
                        hisDto.setAlertEndTime(alert.getCurMoniTime());
                        String jsonString = "{}";
                        try {
                            jsonString = objectMapper.writeValueAsString(hisDto);
                        } catch (JsonProcessingException e) {
                        }
                        JSONObject alertNJson = JSONObject.parseObject(jsonString);
                        alertJson.putAll(alertNJson);
                        alertsHisBiz.insert(AlertModelCommonUtil.generateAlerts(alertJson, alertFieldBiz.getModelField(AlertConfigConstants.REDIS_MODEL_ALERT_HIS)));

                        alertsBiz.deleteByPrimaryKeyArrays(new String[]{deriveAlertId});
                        // 关闭工单
                        if (StringUtils.isNotEmpty(hisDto.getOrderId())) {
                            iBpmTaskService.closeBpm(hisDto.getOrderId(), "alert");
                        }
                        // 删除衍生日志
                        alertDeriveAlertsBizV2.deleteByAlertId(alertDeleting.getAlertId(), alertFieldBiz.getModelField(AlertConfigConstants.REDIS_MODEL_DERIVE_ALERT_HIS));
                        log.info("---%%%%%%%%%------4.handleDeriveAlertRevokeLog delete  derive alert use {} ms------------", (System.currentTimeMillis() - l));
                    }
                }
            }

        } else {
            //没有可消除的告警，忽略此条消警
            log.warn("Received alert revoke data with the raw alertId {}, but there is no alert record "
                    + "with logicAlertId {} in DB.", alertId, alertId);
            return;
        }
    }

    /**
     * 处理衍生告警及记录
     * @param alert
     * @param alertDeriveSelected
     */
    private void handleDeriveAlertActiveLog (AlertsV2Vo alert, AlertDerive alertDeriveSelected, JSONObject alertJson, List<AlertFieldVo> alertFieldList) {
        long l = System.currentTimeMillis();
        //查询告警是否存在，zabbix告警ralert可以确认是否存在；其他告警场景需要通过ip+itemid+level确认相同告警
        AlertDeriveAlertsVo alertQuery = new AlertDeriveAlertsVo();
        if (AlertsV2Vo.OBJECT_TYPE_BIZ.equals(alert.getObjectType())) {
            alertQuery.setBizSys(alert.getBizSys());
        } else {
            alertQuery.setDeviceIp(alert.getDeviceIp());
        }
        alertQuery.setItemId(alert.getItemId());
        alertQuery.setAlertLevel(alert.getAlertLevel());
        alertQuery.setSource(alert.getSource());
        alertQuery.setAlertType(AlertsV2Vo.ALERT_ACTIVE);
        alertQuery.setDeriveId(alertDeriveSelected.getId());
        List<AlertDeriveAlertsV2> queryList = alertDeriveAlertsMapper.select(alertQuery);
        log.info("---$$$$$$$$------1.handleDeriveAlertActiveLog query derive alert use {} ms------------", (System.currentTimeMillis() - l));
        if (CollectionUtils.isNotEmpty(queryList)) {
            AlertDeriveAlertsV2 alertN = queryList.get(0);
            //更新告警基本信息
            alertN.setMoniIndex(alert.getMoniIndex());
            alertN.setMoniObject(alert.getMoniObject());
            alertN.setCurMoniValue(alert.getCurMoniValue());
            Date curMoniTime = alertN.getCurMoniTime();
            Date curMoniTime1 = alert.getCurMoniTime();
            if (curMoniTime == null || (curMoniTime1 != null && curMoniTime1.after(curMoniTime))) {
                alertN.setCurMoniTime(alert.getCurMoniTime());
                alertsBiz.updateCurMoniTime(alertN.getDeriveAlertId(), alertN.getCurMoniTime());
            }
            if (StringUtils.isEmpty(alertN.getRemark())) {
                alertN.setRemark(alert.getRemark());
            }
            if (StringUtils.isNotEmpty(alert.getBizSys())) {
                alertN.setBizSys(alert.getBizSys());
            }
            if (StringUtils.isNotEmpty(alert.getIdcType())) {
                alertN.setIdcType(alert.getIdcType());
            }

            String jsonString = "{}";
            try {
                jsonString = objectMapper.writeValueAsString(alertN);
            } catch (JsonProcessingException e) {
            }
            JSONObject alertNJson = JSONObject.parseObject(jsonString);
            alertJson.putAll(alertNJson);
            alertDeriveAlertsMapper.update(AlertModelCommonUtil.generateAlerts(alertJson,
                    alertFieldBiz.getModelField(AlertConfigConstants.REDIS_MODEL_DERIVE_ALERT)));
            log.info("---$$$$$$$$------2.handleDeriveAlertActiveLog update derive alert log use {} ms------------", (System.currentTimeMillis() - l));
        } else {
            //创建新的衍生告警
            String deriveAlertId = createDeriveAlert(alert, alertDeriveSelected, alertJson,alertFieldList);
            log.info("---$$$$$$$$------3.handleDeriveAlertActiveLog create derive alert use {} ms------------", (System.currentTimeMillis() - l));
            AlertDeriveAlertsV2 alertDeriveAlerts = new AlertDeriveAlertsV2();
            BeanUtils.copyProperties(alert, alertDeriveAlerts);
            alertDeriveAlerts.setDeriveAlertId(deriveAlertId);
            alertDeriveAlerts.setDeriveId(alertDeriveSelected.getId());
            if (StringUtils.isEmpty(alertDeriveAlerts.getAlertId())) {
                alertDeriveAlerts.setAlertId(getUUID());
            }
            if (alertDeriveAlerts.getCreateTime() == null) {
                alertDeriveAlerts.setCreateTime(new Date());
            }

            String jsonString = "{}";
            try {
                jsonString = objectMapper.writeValueAsString(alertDeriveAlerts);
            } catch (JsonProcessingException e) {
            }
            JSONObject alertNJson = JSONObject.parseObject(jsonString);
            alertJson.putAll(alertNJson);
            if (alertJson.get("create_time") == null) {
                alertJson.put("create_time", DateUtil.formatDate(DateUtil.DATE_TIME_CH_FORMAT));
            }
            alertDeriveAlertsMapper.insert(AlertModelCommonUtil.generateAlerts(alertJson, alertFieldBiz.getModelField(AlertConfigConstants.REDIS_MODEL_DERIVE_ALERT)));
            log.info("---$$$$$$$$------4.handleDeriveAlertActiveLog insert derive alert use {} ms------------", (System.currentTimeMillis() - l));
        }
    }

    /**
     * 消除衍生记录及告警
     * @param alert
     */
    private void handleIsolateAlertRevokeLog (AlertsV2Vo alert, JSONObject alertJson) {

        //查询告警是否存在，zabbix告警ralert可以确认是否存在；其他告警场景需要通过ip+itemid+level确认相同告警
        Criteria example = new Criteria();
        Criteria.Condition condition = example.createConditon();
        if (AlertsV2Vo.OBJECT_TYPE_BIZ.equals(alert.getObjectType())) {
            condition.andEqualTo("biz_sys", alert.getBizSys());
        } else {
            condition.andEqualTo("device_ip", alert.getDeviceIp());
        }
        condition.andEqualTo("item_id", alert.getItemId());
        condition.andEqualTo("alert_level", alert.getAlertLevel());
        condition.andEqualTo("source", alert.getSource());
        List<Map<String, Object>> queryList = alertIsolateAlertsMapper.findPageWithResult(example);
        if (CollectionUtils.isNotEmpty(queryList)) {
            for (int i = 0; i < queryList.size(); i++) {
                Map<String, Object> alertDto = queryList.get(i);
                if (i == 0) {
                    alertDto.put("alert_count", MapUtils.getIntValue(alertDto, "alert_count") + 1);
                }
                if (alert.getCurMoniTime() != null) {
                    alertDto.put("alert_end_time", DateUtil.format(alert.getCurMoniTime(), DateUtil.DATE_TIME_CH_FORMAT));
                } else {
                    alertDto.put("alert_end_time", DateUtil.formatDate(DateUtil.DATE_TIME_CH_FORMAT));
                }

                for (Map.Entry<String, Object> entry: alertJson.entrySet()) {
                    alertDto.putIfAbsent(entry.getKey(), entry.getValue());
                }

                for (Map.Entry<String, Object> entry: alertDto.entrySet()) {
                    Object value = entry.getValue();
                    if (value == null) {
                        continue;
                    }
                    if (value instanceof Timestamp) {
                        alertDto.put(entry.getKey(), DateUtil.format(new Date(((Timestamp) value).getTime()), "yyyy-MM-dd HH:mm:ss"));
                    }
                }

                // 消警
                alertIsolateAlertsHisBizV2.insert(AlertModelCommonUtil.generateAlerts(alertDto, alertFieldBiz.getModelField(AlertConfigConstants.REDIS_MODEL_ISOLATE_ALERT_HIS)));
                String alertId = MapUtils.getString(alertDto, "alert_id");
                if (StringUtils.isNotEmpty(alertId)) {
                    alertIsolateAlertsMapper.deleteById(alertId);
                }
                // 关闭工单
                String orderId = MapUtils.getString(alertDto, "order_id");
                String orderStatus = MapUtils.getString(alertDto, "order_status");
                if (StringUtils.isNotEmpty(orderId)) {
                    iBpmTaskService.closeBpm(orderId, "isolate");
                }
            }

        } else {
            //没有可消除的告警，忽略此条消警

            return;
        }
    }

    /**
     * 处理屏蔽告警及记录
     * @param alert
     */
    private void handleIsolateAlertActiveLog (AlertsV2Vo alert, JSONObject alertJson) {
        //查询告警是否存在，zabbix告警ralert可以确认是否存在；其他告警场景需要通过ip+itemid+level确认相同告警
        Criteria example = new Criteria();
        Criteria.Condition condition = example.createConditon();
        if (AlertsV2Vo.OBJECT_TYPE_BIZ.equals(alert.getObjectType())) {
            condition.andEqualTo("biz_sys", alert.getBizSys());
        } else {
            condition.andEqualTo("device_ip", alert.getDeviceIp());
        }
        condition.andEqualTo("item_id", alert.getItemId());
        condition.andEqualTo("alert_level", alert.getAlertLevel());
        condition.andEqualTo("source", alert.getSource());
        List<Map<String, Object>> queryList = alertIsolateAlertsMapper.findPageWithResult(example);
        if (CollectionUtils.isNotEmpty(queryList)) {
            Map<String, Object> alertN = queryList.get(0);
            //更新告警基本信息
            alertN.put("moni_index", alert.getMoniIndex());
            alertN.put("moni_object", alert.getMoniObject());
            alertN.put("cur_moni_value", alert.getCurMoniValue());
            if (alert.getCurMoniTime() != null) {
                alertN.put("cur_moni_time", alert.getCurMoniTime());
            }
            alertN.put("alert_count", MapUtils.getIntValue(alertN, "alert_count") + 1);
            for (Map.Entry<String, Object> entry: alertJson.entrySet()) {
                alertN.putIfAbsent(entry.getKey(), entry.getValue());
            }

            for (Map.Entry<String, Object> entry: alertN.entrySet()) {
                Object value = entry.getValue();
                if (value == null) {
                    continue;
                }
                if (value instanceof Timestamp) {
                    alertN.put(entry.getKey(), DateUtil.format(new Date(((Timestamp) value).getTime()), "yyyy-MM-dd HH:mm:ss"));
                } else if (value instanceof Date) {
                    alertN.put(entry.getKey(), DateUtil.format((Date) value, "yyyy-MM-dd HH:mm:ss"));
                }
            }
            alertIsolateAlertsMapper.update(AlertModelCommonUtil.generateAlerts(alertN,
                    alertFieldBiz.getModelField(AlertConfigConstants.REDIS_MODEL_ISOLATE_ALERT)));
        } else {
            if (StringUtils.isEmpty(alertJson.getString("alert_id"))) {
                alertJson.put("alert_id", getUUID());
            }
            alertIsolateAlertsMapper.insert(AlertModelCommonUtil.generateAlerts(alertJson, alertFieldBiz.getModelField(AlertConfigConstants.REDIS_MODEL_ISOLATE_ALERT)));
        }
    }

    /**
     * 根据规则过滤主次告警
     * @param alert
     * @return
     */
    private boolean primarySecondaryAlert (AlertsV2Vo alert, JSONObject alertJson) {
        if (alert == null) {
            return true;
        }
        //不符合主要告警规则
        boolean primaryFlag = true;
        if (StringUtils.isEmpty(alert.getAlertLevel())) {
            primaryFlag = false;
        }
        if (StringUtils.isEmpty(alert.getMoniIndex())) {
            primaryFlag = false;
        }

        String idcType = alertJson.getString("idc_type");

        //查询过滤规则，用于过滤告警
        List<AlertPrimarySecondary> list = alertPrimarySecondaryMapper.listEffective();
        AlertPrimarySecondary alertPrimarySelected = null;
        //先判断是否符合主要告警规则
        if (primaryFlag && AlertsV2Vo.ALERT_ACTIVE.equals(alert.getAlertType())) {
            for (AlertPrimarySecondary alertPrimarySecondary: list) {
                //先判断监控项数据，不正确直接跳过本次
                String primaryAlertLevel = alertPrimarySecondary.getPrimaryAlertLevel();
                //判断告警等级
                if (!alert.getAlertLevel().equals(primaryAlertLevel)) {
                    continue;
                }
                String primaryMoniIndex = alertPrimarySecondary.getPrimaryMoniIndex();
                //判断告警内容
                if (alert.getMoniIndex().indexOf(primaryMoniIndex) < 0) {
                    continue;
                }

                String deviceRelationType = alertPrimarySecondary.getDeviceRelationType();
                //相同设备处理逻辑
                if (Constants.DEVICE_RELATION_TYPE_SAME.equals(deviceRelationType)) {
                    String primaryOptionCondition = alertPrimarySecondary.getPrimaryOptionCondition().toLowerCase();
                    //条件为空的忽略
                    if (StringUtils.isEmpty(primaryOptionCondition)) {
                        continue;
                    }
                    if (judgeAlert (alertJson, primaryOptionCondition)) {
                        alertPrimarySelected = alertPrimarySecondary;
                        break;
                    }
                }
                //不同设备处理逻辑
                else if (Constants.DEVICE_RELATION_TYPE_DIFFERENT.equals(deviceRelationType)) {
                    String primaryIdc = alertPrimarySecondary.getPrimaryIdc();
                    String primaryIp = alertPrimarySecondary.getPrimaryIp();
                    if (StringUtils.isNotEmpty(primaryIdc) && primaryIdc.equals(idcType)
                    && StringUtils.isNotEmpty(primaryIp) && primaryIp.equals(alert.getDeviceIp())) {
                        alertPrimarySelected = alertPrimarySecondary;
                        break;
                    }
                }

            }
        }

        //匹配主要告警成功，更新id后直接入库
        if (alertPrimarySelected != null) {
            log.info("filter primary alert success! alert is :{}", alert);
            alert.setRAlertId(Constants.PREFIX_PRIMARY + alertPrimarySelected.getId());
            alert.setExtId(alert.getRAlertId());
            alertJson.put("r_alert_id", alert.getRAlertId());
            alertJson.put("ext_id", alert.getRAlertId());
            return false;
        }

        //匹配次要告警
        for (AlertPrimarySecondary alertPrimarySecondary: list) {
            String optionCondition = alertPrimarySecondary.getSecondaryOptionCondition().toLowerCase();
            //条件为空的忽略
            if (StringUtils.isEmpty(optionCondition)) {
                continue;
            }
            if (judgeAlert (alertJson, optionCondition)) {
                if (Constants.DEVICE_RELATION_TYPE_SAME.equals(alertPrimarySecondary.getDeviceRelationType())) {
                    String primaryOptionCondition = alertPrimarySecondary.getPrimaryOptionCondition().toLowerCase();
                    //条件为空的忽略
                    if (StringUtils.isEmpty(primaryOptionCondition)) {
                        continue;
                    }
                    if (!judgeAlert (alertJson, primaryOptionCondition)) {
                        continue;
                    }
                }
                //屏蔽成功，插入次要告警记录
                if (AlertsV2Vo.ALERT_REVOKE.equals(alert.getAlertType())) {
                    handleSecondaryAlertRevokeLog(alert, alertJson);
                    return true;
                } else {
                    if (handleSecondaryAlertActiveLog(alert, alertPrimarySecondary, alertJson)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * 消除次要记录
     * @param alert
     */
    private void handleSecondaryAlertRevokeLog (AlertsV2Vo alert, JSONObject alertJson) {
        String alertId = null;

        //查询告警是否存在，zabbix告警ralert可以确认是否存在；其他告警场景需要通过ip+itemid+level确认相同告警
        AlertPrimarySecondaryAlertsVo alertQuery = new AlertPrimarySecondaryAlertsVo();
        if (AlertsV2Vo.OBJECT_TYPE_BIZ.equals(alert.getObjectType())) {
            alertQuery.setBizSys(alert.getBizSys());
        } else {
            alertQuery.setDeviceIp(alert.getDeviceIp());
        }
        alertQuery.setItemId(alert.getItemId());
        alertQuery.setAlertLevel(alert.getAlertLevel());
        alertQuery.setSource(alert.getSource());
        alertQuery.setAlertType(AlertsV2Vo.ALERT_ACTIVE);
        List<AlertPrimarySecondaryAlertsV2> queryList = alertPrimarySecondaryAlertsMapper.select(alertQuery);
        if (CollectionUtils.isNotEmpty(queryList)) {
            for (int i = 0; i < queryList.size(); i++) {
                AlertPrimarySecondaryAlertsV2 alertPrimarySecondaryAlerts = queryList.get(i);
                alertPrimarySecondaryAlerts.setCurMoniTime(alert.getCurMoniTime());
                alertPrimarySecondaryAlerts.setAlertType(AlertsV2Vo.ALERT_REVOKE);
                alertPrimarySecondaryAlertsMapper.updateByEntity(alertPrimarySecondaryAlerts);
            }
        } else {
            //没有可消除的告警，忽略此条消警
            log.warn("Received alert revoke data with the raw alertId {}, but there is no alert record "
                    + "with logicAlertId {} in DB.", alertId, alertId);
            return;
        }
    }

    /**
     * 处理衍生告警及记录
     * @param alert
     * @param alertDeriveSelected
     */
    private boolean handleSecondaryAlertActiveLog (AlertsV2Vo alert, AlertPrimarySecondary alertDeriveSelected, JSONObject alertJson) {

        //查询告警是否存在，zabbix告警ralert可以确认是否存在；其他告警场景需要通过ip+itemid+level确认相同告警
        AlertPrimarySecondaryAlertsVo alertQuery = new AlertPrimarySecondaryAlertsVo();
        if (AlertsV2Vo.OBJECT_TYPE_BIZ.equals(alert.getObjectType())) {
            alertQuery.setBizSys(alert.getBizSys());
        } else {
            alertQuery.setDeviceIp(alert.getDeviceIp());
        }
        alertQuery.setItemId(alert.getItemId());
        alertQuery.setAlertLevel(alert.getAlertLevel());
        alertQuery.setSource(alert.getSource());
        alertQuery.setAlertType(AlertsV2Vo.ALERT_ACTIVE);
        alertQuery.setPrimarySecondaryId(alertDeriveSelected.getId());
        List<AlertPrimarySecondaryAlertsV2> queryList = alertPrimarySecondaryAlertsMapper.select(alertQuery);
        if (CollectionUtils.isNotEmpty(queryList)) {
            AlertPrimarySecondaryAlertsV2 alertN = queryList.get(0);
            //更新告警基本信息
            alertN.setMoniIndex(alert.getMoniIndex());
            alertN.setMoniObject(alert.getMoniObject());
            alertN.setCurMoniValue(alert.getCurMoniValue());
            alertN.setCurMoniTime(alert.getCurMoniTime());
            if (StringUtils.isEmpty(alertN.getRemark())) {
                alertN.setRemark(alert.getRemark());
            }
            if (StringUtils.isNotEmpty(alert.getBizSys())) {
                alertN.setBizSys(alert.getBizSys());
            }
            if (StringUtils.isNotEmpty(alert.getIdcType())) {
                alertN.setIdcType(alert.getIdcType());
            }
            String jsonString = "{}";
            try {
                jsonString = objectMapper.writeValueAsString(alertN);
            } catch (JsonProcessingException e) {
            }
            JSONObject alertNJson = JSONObject.parseObject(jsonString);
            alertJson.putAll(alertNJson);
            alertPrimarySecondaryAlertsMapper.update(AlertModelCommonUtil.generateAlerts(alertJson, alertFieldBiz.getModelField(AlertConfigConstants.REDIS_MODEL_PRIMARY_SECONDARY_ALERT)));
        } else {
            //创建新的次要告警

            AlertsV2 alertsQuery = new AlertsV2();
            if (Constants.DEVICE_RELATION_TYPE_SAME.equals(alertDeriveSelected.getDeviceRelationType())) {
                if (AlertsV2Vo.OBJECT_TYPE_BIZ.equals(alert.getObjectType())) {
                    alertsQuery.setBizSys(alert.getBizSys());
                } else {
                    alertsQuery.setDeviceIp(alert.getDeviceIp());
                }
                alertsQuery.setIdcType(alert.getIdcType());
            }

            alertsQuery.setRAlertId(Constants.PREFIX_PRIMARY+alertDeriveSelected.getId());
            List<AlertsV2Vo> queryExsitList = alertsBiz.select(alertsQuery);
            //次要告警没有匹配的主要告警，则不收敛
            if (CollectionUtils.isEmpty(queryExsitList)) {
                return false;
            }
            AlertsV2Vo alertN = queryExsitList.get(0);
            alertJson.put("primary_secondary_alert_id", alertN.getAlertId());
            alertJson.put("primary_secondary_id", alertDeriveSelected.getId());
            alertJson.put("alert_id", getUUID());
            alertPrimarySecondaryAlertsMapper.insert(AlertModelCommonUtil.generateAlerts(alertJson, alertFieldBiz.getModelField(AlertConfigConstants.REDIS_MODEL_PRIMARY_SECONDARY_ALERT)));
        }
        return true;
    }

    /**
     * 判断业务告警
     * @param alertJson
     * @return
     */
    private boolean checkBusinessAlert (AlertsV2Vo alert, JSONObject alertJson) {
        if (alertJson == null) {
            return true;
        }
        //解析告警实体为符合告警规则的json格式
        //查询过滤规则，用于过滤告警
        List<AlertConfigBusiness> list = alertConfigBusinessMapper.listEffective();
        for (AlertConfigBusiness alertConfigBusiness: list) {
            String optionCondition = alertConfigBusiness.getOptionCondition().toLowerCase();
            //条件为空的忽略
            if (StringUtils.isEmpty(optionCondition)) {
                continue;
            }
            if (judgeAlert (alertJson, optionCondition)) {
                String id = alertConfigBusiness.getId();
                alert.setOperateStatus(OperateStatusEnum.BUSINESS_ALERT.getCode());
                alertJson.put("operate_status", OperateStatusEnum.BUSINESS_ALERT.getCode());
                alert.setExtId(AlertConfigConstants.BUSINESS_ALERT_PREFIX + id);
                alertJson.put("ext_id", AlertConfigConstants.BUSINESS_ALERT_PREFIX + id);
                return true;
            }
        }
        return false;
    }

    /**
     * 判断异常信息
     * @param alertJson
     * @return
     */
    private boolean checkExceptionAlert (AlertsV2Vo alert, JSONObject alertJson) {
        if (alertJson == null) {
            return true;
        }
        //解析告警实体为符合告警规则的json格式
        //查询过滤规则，用于过滤告警
        List<AlertConfigException> list = alertConfigExceptionMapper.listEffective();
        for (AlertConfigException alertConfigException: list) {
            String optionCondition = alertConfigException.getOptionCondition().toLowerCase();
            //条件为空的忽略
            if (StringUtils.isEmpty(optionCondition)) {
                continue;
            }
            if (judgeAlert (alertJson, optionCondition)) {
                String id = alertConfigException.getId();
                alert.setOperateStatus(OperateStatusEnum.EXCEPTION_ALERT.getCode());
                alertJson.put("operate_status", OperateStatusEnum.EXCEPTION_ALERT.getCode());
                alert.setExtId(AlertConfigConstants.EXCEPTION_ALERT_PREFIX + id);
                alertJson.put("ext_id", AlertConfigConstants.EXCEPTION_ALERT_PREFIX + id);
                return true;
            }
        }
        return false;
    }

    /**
     * 设置告警名称
     * @param alert
     * @param alertJson
     */
    private void setKeyComment (AlertsV2Vo alert, JSONObject alertJson) {
        if (StringUtils.isEmpty(alert.getAlertLevel()) || StringUtils.isEmpty(alert.getItemKey())) {
            return;
        }
        List<AlertStandard> list = alertStandardDao.getAlertStandardWithEnable();
        for (AlertStandard alertStandard: list) {
            String standardAlertLevel = alertStandard.getAlertLevel();
            String standardKey = alertStandard.getMonitorKey();
            if (StringUtils.isEmpty(standardAlertLevel) || StringUtils.isEmpty(standardKey)) {
                continue;
            }
            if (alert.getItemKey().toLowerCase().indexOf(standardKey.toLowerCase()) == 0 &&
                    standardAlertLevel.indexOf(alert.getAlertLevel()) > -1 &&
                    !StringUtils.isEmpty(alertStandard.getStandardName())) {
                alert.setKeyComment(alertStandard.getStandardName());
                alertJson.put("key_comment", alertStandard.getStandardName());
                break;
            }
        }
    }

    public AlertsV2Vo parseZabbix(final ZabbixAlertV2 zabbixAlertV2) throws Exception {
        AlertsV2Vo dto = new AlertsV2Vo();
//		dto.setAlertId(this.getAlertId());
        dto.setRAlertId(zabbixAlertV2.getAlertId());
        dto.setAlertLevel(zabbixAlertV2.getAlertLevel());

        DateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        dto.setCurMoniTime(format.parse(zabbixAlertV2.getCurMoniTime()));
        dto.setCurMoniValue(zabbixAlertV2.getCurMoniValue());
        if (com.aspire.mirror.alert.server.util.StringUtils.isNotEmpty(zabbixAlertV2.getAlertStartTime())) {
            dto.setAlertStartTime(format.parse(zabbixAlertV2.getAlertStartTime()));
        } else {
            dto.setAlertStartTime(format.parse(zabbixAlertV2.getCurMoniTime()));
        }
        dto.setDeviceIp(zabbixAlertV2.getDeviceIP());
        dto.setBizSys(zabbixAlertV2.getServSystem());
        dto.setAlertType(zabbixAlertV2.getMoniResult());
        dto.setItemId(zabbixAlertV2.getZbxItemId()); 		// 后续均为保存微服务中定义的itemid
        dto.setMoniIndex(zabbixAlertV2.getMonitorIndex());
        dto.setMoniObject(zabbixAlertV2.getMonitorObject());
        dto.setSource(zabbixAlertV2.getSource());
        dto.setItemKey(zabbixAlertV2.getItemKey());
        dto.setKeyComment(zabbixAlertV2.getKeyComment());
        dto.setRemark(zabbixAlertV2.getAlertDesc());
        String idcType = cmdbHelper.getIdc(zabbixAlertV2.getBusinessSystem());
        dto.setIdcType(idcType);
        dto.setAlertCount(1);
        dto.setExt(zabbixAlertV2.getExt());
        if (com.aspire.mirror.alert.server.util.StringUtils.isNotEmpty(zabbixAlertV2.getObjectType())) {
            dto.setObjectType(zabbixAlertV2.getObjectType());
        } else {
            dto.setObjectType(AlertsVo.OBJECT_TYPE_DEVICE);
        }
        return dto;
    }
}

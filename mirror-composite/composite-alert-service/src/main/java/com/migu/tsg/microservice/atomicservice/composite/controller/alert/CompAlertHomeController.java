package com.migu.tsg.microservice.atomicservice.composite.controller.alert;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;
import com.aspire.mirror.alert.api.dto.alert.AlertsDetailResponse;
import com.aspire.mirror.alert.api.dto.alert.AlertsHisDetailResponse;
import com.aspire.mirror.alert.api.dto.alert.AlertsOverViewResponse;
import com.aspire.mirror.alert.api.dto.alert.AlertsQueryRequest;
import com.aspire.mirror.composite.payload.alert.*;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceV2Controller;
import com.migu.tsg.microservice.atomicservice.composite.helper.UserHelper;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.CompUserVo;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.service.alert.ICompAlertHomeService;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.alert.AlertHomeServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CompAlertHomeController extends CommonResourceV2Controller implements ICompAlertHomeService {

    private static final String CMDB_HELPER_KEY_IDCTYPE = "idcType";

    @Autowired
    private AlertHomeServiceClient alertHomeServiceClient;
    @Autowired
    private UserHelper userHelper;

    @Override
    @ResAction(action = "view", resType = "alert", loadResFilter=true)
    public PageResponse<CompAlertsDetailResp> query(@RequestBody CompAlertsQueryRequest queryRequest,
                                                    @RequestParam("alertType") String alertType) throws ParseException {
        PageResponse<CompAlertsDetailResp> compResponse = new PageResponse<>();
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        
		resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        String username = authCtx.getUser().getUsername();
        try {
            if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser()) {
                
                if ("activity".equals(alertType)) {
                    CompUserVo user = userHelper.findByLdapId(username);
                    queryRequest.setUserName(user.getCode());
                } else {
                    queryRequest.setUserName(username);
                }
            } else {
                String toConfirmStaff = queryRequest.getToConfirmStaff();
                if ("activity".equals(alertType)) {
                    queryRequest.setUserName(StringUtils.isNotBlank(toConfirmStaff) ? toConfirmStaff : "");
                } else {
                    String confirmStaff = queryRequest.getConfirmStaff();
                    queryRequest.setUserName(StringUtils.isNotBlank(confirmStaff) ? confirmStaff : "");
                }
            }
            queryRequest.setQueryType("activity".equals(alertType) ? "0" : "1");
            AlertsQueryRequest alertsQueryRequest = jacksonBaseParse(AlertsQueryRequest.class, queryRequest);
            log.info("#=====> query alert data entity: {}", JSONObject.toJSONString(alertsQueryRequest));
            PageResponse<AlertsDetailResponse> pageResponse = alertHomeServiceClient.getAlertData(alertsQueryRequest,alertType);
            compResponse.setCount(pageResponse.getCount());
            List<CompAlertsDetailResp> alertsRespList = Lists.newArrayList();
            List<AlertsDetailResponse> alertsList = pageResponse.getResult();
            if (!CollectionUtils.isEmpty(alertsList)) {
                alertsRespList = jacksonBaseParse(CompAlertsDetailResp.class, alertsList);
            }
            compResponse.setResult(alertsRespList);
        } catch (Exception e) {
            throw new RuntimeException("[Composite]>>> Get Home Alert Response Error is {}", e);
        }
        return compResponse;
    }

    // 业务系统
    private void getAuthBizSystem(CompAlertsQueryRequest queryRequest) {
        String bizSystem = queryRequest.getBizSys();
        // 有权限的业务系统
        List<String> authBizSystemIdList = queryRequest.getAuthBizSystemIdList();
        // 符合条件的业务系统
        List<String> newAuthBizSystemIdList = Lists.newArrayList();
        if (StringUtils.isNotBlank(bizSystem) && !CollectionUtils.isEmpty(authBizSystemIdList)) {
            final String[] bizSystemArray = bizSystem.split(",");
            for (String str : bizSystemArray) {
                if (authBizSystemIdList.contains(str)) newAuthBizSystemIdList.add(str);
            }
            if (CollectionUtils.isEmpty(newAuthBizSystemIdList)) {
                newAuthBizSystemIdList.add("bizSystem");
            }
        } else {
            if (CollectionUtils.isEmpty(authBizSystemIdList)) {
//                newAuthBizSystemIdList.add("bizSystem");
            } else {
                newAuthBizSystemIdList.addAll(authBizSystemIdList);
            }
        }
        queryRequest.setAuthBizSystemIdList(newAuthBizSystemIdList);
    }
    // 设备类型
    private void getAuthDeviceType(CompAlertsQueryRequest queryRequest) {
        String deviceType = queryRequest.getDeviceType();
        // 有权限的设备类型
        List<String> authBizSystemIdList = queryRequest.getAuthDeviceTypeList();
        // 符合条件的设备类型
        List<String> authBizSystem = Lists.newArrayList();
        if (StringUtils.isNotBlank(deviceType) && !CollectionUtils.isEmpty(authBizSystemIdList)) {
            authBizSystem.add(authBizSystemIdList.contains(deviceType) ? deviceType : "deviceType");
        }
        else {
            if (CollectionUtils.isEmpty(authBizSystemIdList)) {
//                authBizSystem.add("deviceType");
            } else {
                authBizSystem.addAll(authBizSystemIdList);
            }
        }
        queryRequest.setAuthDeviceTypeList(authBizSystem);
    }
    // 设备IP
    private void getAuthDeviceIp(CompAlertsQueryRequest queryRequest) {
        String deviceIp = queryRequest.getDeviceIp();
        // 有权限的设备ip
        List<String> authDeviceIdListList = queryRequest.getAuthDeviceIdList();
        // 符合条件的设备ip
        List<String> authDeviceIp = Lists.newArrayList();
        if (StringUtils.isNotBlank(deviceIp) && !CollectionUtils.isEmpty(authDeviceIdListList)) {
            for (String str : authDeviceIdListList) {
                if (str.contains(deviceIp)) authDeviceIp.add(str);
            }
            if (CollectionUtils.isEmpty(authDeviceIp)) authDeviceIp.add("deviceIp");
        } else {
            if (CollectionUtils.isEmpty(authDeviceIdListList)) {
//                authDeviceIp.add("deviceType");
            } else {
                authDeviceIp.addAll(authDeviceIdListList);
            }
        }
        queryRequest.setAuthDeviceIdList(authDeviceIp);
    }
    // 机房
    private void getAuthSourceRoom(CompAlertsQueryRequest queryRequest) {
        String sourceRoom = queryRequest.getSourceRoom();
        //有权限的机房
        List<String> authRoomList = queryRequest.getAuthPrecinctList();
        //符合条件的机房
        List<String> authRoom = Lists.newArrayList();
        if (StringUtils.isNotBlank(sourceRoom) && !CollectionUtils.isEmpty(authRoomList)) {
            authRoom.add(authRoomList.contains(sourceRoom) ? sourceRoom : "sourceRoom");
        } else {
            if (CollectionUtils.isEmpty(authRoomList)) {
            } else {
                authRoom.addAll(authRoomList);
            }
        }
        queryRequest.setAuthPrecinctList(authRoom);
    }
    // 资源池
    private void getAuthIdcType(CompAlertsQueryRequest queryRequest) {
        String idcType = queryRequest.getIdcType();
        //有权限的资源池
        List<String> authIdcTypeList = queryRequest.getAuthIdcIdList();
        if (StringUtils.isNotBlank(idcType) && !CollectionUtils.isEmpty(authIdcTypeList)) {
            //符合条件的资源池
            queryRequest.setAuthIdcType(authIdcTypeList.contains(idcType) ? idcType : "idcType");
            queryRequest.setAuthIdcIdList(null);
        } else {
            if (CollectionUtils.isEmpty(authIdcTypeList)) {
                queryRequest.setAuthIdcType(null);
//                queryRequest.setAuthIdcIdList(Arrays.asList("idcType","idcTypeOtherWise"));
            } else {
                queryRequest.setIdcTypeOtherWise(authIdcTypeList.contains("其他") ? "其他" : "idcTypeOtherWise");
            }
        }
    }

    @Override
    @ResAction(resType="alert", action="view", loadResFilter=true)
    public CompAlertsOverViewResponse overview(@RequestBody CompAlertsQueryRequest queryRequest,
                                               @RequestParam("alertType") String alertType) throws ParseException {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        String username = authCtx.getUser().getUsername();
        if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser()) {
			/*
			 * Map<String, Set<String>> totalConstraints =
			 * resAuthHelper.verifyActionAndGetResource( authCtx.getUser(),
			 * authCtx.getResAction(), authCtx.getFlattenConstraints());
			 * 
			 * if (!super.applyGeneralAuthConstraints(totalConstraints, queryRequest)) {
			 * return new CompAlertsOverViewResponse(); }
			 * this.getAuthBizSystem(queryRequest); this.getAuthDeviceType(queryRequest);
			 * this.getAuthDeviceIp(queryRequest); this.getAuthSourceRoom(queryRequest);
			 * this.getAuthIdcType(queryRequest);
			 */
            if ("activity".equals(alertType)) {
                CompUserVo user = userHelper.findByLdapId(username);
                queryRequest.setUserName(user.getCode());
            } else {
                queryRequest.setUserName(username);
            }
        } else {
            String toConfirmStaff = queryRequest.getToConfirmStaff();
            if ("activity".equals(alertType)) {
                queryRequest.setUserName(StringUtils.isNotBlank(toConfirmStaff) ? toConfirmStaff : "");
            } else {
                String confirmStaff = queryRequest.getConfirmStaff();
                queryRequest.setUserName(StringUtils.isNotBlank(confirmStaff) ? confirmStaff : "");
            }
        }
        queryRequest.setQueryType("activity".equals(alertType) ? "0" : "1");
        AlertsQueryRequest alertsQueryRequest = jacksonBaseParse(AlertsQueryRequest.class, queryRequest);
        log.info("#=====> get alert overview entity: {}", JSONObject.toJSONString(alertsQueryRequest));
        AlertsOverViewResponse overview = alertHomeServiceClient.overview(alertsQueryRequest,alertType);
        return jacksonBaseParse(CompAlertsOverViewResponse.class, overview);
    }

    @Override
    @ResAction(resType="alert", action="view", loadResFilter=true)
    public PageResponse<CompAlertsHisDetailResp> queryHis(@RequestBody CompAlertsQueryRequest queryRequest) throws ParseException {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        String username = authCtx.getUser().getUsername();
        if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser()) {
			
            queryRequest.setUserName(username);
        }
        AlertsQueryRequest alertsQueryRequest = jacksonBaseParse(AlertsQueryRequest.class, queryRequest);
        log.info("#=====>  query his alert entity: {}", JSONObject.toJSONString(alertsQueryRequest));
        PageResponse<AlertsHisDetailResponse> hisPageList = alertHomeServiceClient.queryHis(alertsQueryRequest);
        PageResponse<CompAlertsHisDetailResp> hisCompResponse = new PageResponse<>();
        hisCompResponse.setCount(hisPageList.getCount());
        List<CompAlertsHisDetailResp> hisAlertsRespList = Lists.newArrayList();
        List<AlertsHisDetailResponse> hisAlertsList = hisPageList.getResult();
        if (!CollectionUtils.isEmpty(hisAlertsList)) {
            hisAlertsRespList = jacksonBaseParse(CompAlertsHisDetailResp.class, hisAlertsList);
        }
        hisCompResponse.setResult(hisAlertsRespList);
        return hisCompResponse;
    }

//    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    @Override
    @ResAction(resType="alert", action="view", loadResFilter=true)
    public void export(@RequestBody CompAlertsQueryRequest queryRequest,
                       @RequestParam ("alertType") String alertType,
                       HttpServletResponse response) throws Exception {
        boolean flag = false;
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        String username = authCtx.getUser().getUsername();
        if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser()) {
          
            if ("activity".equals(alertType)) {
                CompUserVo user = userHelper.findByLdapId(username);
                queryRequest.setUserName(user.getCode());
            } else {
                queryRequest.setUserName(username);
            }
			
            
        } else {
            flag = true;
            String toConfirmStaff = queryRequest.getToConfirmStaff();
            if ("activity".equals(alertType)) {
                queryRequest.setUserName(StringUtils.isNotBlank(toConfirmStaff) ? toConfirmStaff : "");
            } else {
                String confirmStaff = queryRequest.getConfirmStaff();
                queryRequest.setUserName(StringUtils.isNotBlank(confirmStaff) ? confirmStaff : "");
            }
        }
        queryRequest.setQueryType("activity".equals(alertType) ? "0" : "1");
        AlertsQueryRequest alertsQueryRequest = jacksonBaseParse(AlertsQueryRequest.class, queryRequest);
        log.info("#=====> export alert entity: {}", JSONObject.toJSONString(alertsQueryRequest));
        List<Map<String, Object>> dataList = Lists.newArrayList();
       /* if (flag)*/ dataList = alertHomeServiceClient.export(alertsQueryRequest,alertType);
       log.info("我的告警中的数据"+dataList);
        String[] headerList = {"级别","设备IP","监控对象","监控值","告警内容","开始时间","当前时间","资源池","工程期数","pod池","所属位置","机房位置","业务线","告警类型","状态","通知方式","通知状态","通知时间","转派人","转派状态","转派时间","待确认人","确认人","确认时间","确认内容","派单状态","派单时间","派单类型","告警次数","设备厂商","设备型号"};
        String[] keyList = {"alertLevel","deviceIp","moniObject","curMoniValue","moniIndex","alertStartTime","curMoniTime","idcType","projectName","podName","source","sourceRoom","idcType","objectType","orderStatus","reportType","reportStatus","reportTime","transUser","transStatus","transTime","toConfirmUser","confirmedUser","confirmedTime","confirmedContent","deliverStatus","deliverTime","orderType","alertCount","deviceMfrs","deviceModel"};
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String title = "告警导出列表_" + sDateFormat.format(new Date());
        String fileName = title+".xlsx";
        OutputStream os = response.getOutputStream();// 取得输出流
        response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf( URLEncoder.encode(fileName, "UTF-8"))));
        response.setHeader("Connection", "close");
        response.setHeader("Content-Type", "application/vnd.ms-excel");
        //excel constuct
        ExportExcelUtil eeu = new ExportExcelUtil();
        Workbook book = new SXSSFWorkbook(128);
        eeu.exportExcel(book, 0, title, headerList, dataList, keyList);
        book.write(os);

    }

    @Override
    @ResAction(resType="alert", action="view", loadResFilter=true)
    public void exportHis(@RequestBody CompAlertsQueryRequest queryRequest, HttpServletResponse response) throws Exception {
        if (queryRequest == null) {
            log.error("Alert query param pageRequset is null or query type is empty !");
            return;
        }
        boolean flag = false;
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        String username = authCtx.getUser().getUsername();
        //Map<String, Set<String>> totalConstraints = Maps.newHashMap();
        if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser()) {
			/*
			 * totalConstraints = resAuthHelper.verifyActionAndGetResource(
			 * authCtx.getUser(), authCtx.getResAction(), authCtx.getFlattenConstraints());
			 * if (super.applyGeneralAuthConstraints(totalConstraints, queryRequest)) { flag
			 * = true; this.getAuthBizSystem(queryRequest);
			 * this.getAuthDeviceType(queryRequest); this.getAuthDeviceIp(queryRequest);
			 * this.getAuthSourceRoom(queryRequest); this.getAuthIdcType(queryRequest);
			 * queryRequest.setUserName(username); }
			 */
            queryRequest.setUserName(username);
        } else {
            flag = true;
        }
        AlertsQueryRequest alertsQueryRequest = jacksonBaseParse(AlertsQueryRequest.class, queryRequest);
        log.info("#=====> export alert his entity: {}", JSONObject.toJSONString(alertsQueryRequest));
        List<Map<String, Object>> dataList = Lists.newArrayList();
        if (flag) dataList = alertHomeServiceClient.exportHis(alertsQueryRequest);
        String[] headerList = {"级别","设备IP","监控对象","监控值","告警内容","开始时间","当前时间","资源池","工程期数","pod池","所属位置","机房位置","业务线","告警类型","状态","通知方式","通知状态","通知时间","转派人","转派状态","转派时间","待确认人","确认人","确认时间","确认内容","派单状态","派单时间","派单类型","清除人","清除时间","清除内容","设备厂商","设备型号"};
        String[] keyList = {"alertLevel","deviceIp","moniObject","curMoniValue","moniIndex","alertStartTime","curMoniTime","idcType","projectName","podName","source","sourceRoom","idcType","objectType","orderStatus","reportType","reportStatus","reportTime","transUser","transStatus","transTime","toConfirmUser","confirmedUser","confirmedTime","confirmedContent","deliverStatus","deliverTime","orderType","clearUser","clearTime","clearContent","deviceMfrs","deviceModel"};
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String title = "历史告警导出列表_" + sDateFormat.format(new Date());
        String fileName = title+".xlsx";
        OutputStream os = response.getOutputStream();// 取得输出流
        response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
        response.setHeader("Connection", "close");
        response.setHeader("Content-Type", "application/vnd.ms-excel");
        //excel constuct
        ExportExcelUtil eeu = new ExportExcelUtil();
        Workbook book = new SXSSFWorkbook(128);
        eeu.exportExcel(book, 0, title, headerList, dataList, keyList);
        book.write(os);
    }

    @Override
    @ResAction(action = "view", resType = "alert", loadResFilter=true)
    public ResponseEntity<String> getHomeAlertVoiceContent(@RequestParam ("alertType") String alertType) {
        try {
            RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
            String username = authCtx.getUser().getUsername();
            CompAlertsQueryRequest queryRequest = new CompAlertsQueryRequest();
            Map<String, Set<String>> totalConstraints = Maps.newHashMap();
            boolean isUandS = true;
            if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser()) {
                totalConstraints = resAuthHelper.verifyActionAndGetResource(
                        authCtx.getUser(), authCtx.getResAction(), authCtx.getFlattenConstraints() );
                if (!super.applyGeneralAuthConstraints(totalConstraints, queryRequest)) {
                    return new ResponseEntity<String>("", HttpStatus.OK);
                }
                isUandS = false;
                this.getAuthBizSystem(queryRequest);
                this.getAuthDeviceType(queryRequest);
                this.getAuthDeviceIp(queryRequest);
                this.getAuthSourceRoom(queryRequest);
                this.getAuthIdcType(queryRequest);
            }
            queryRequest.setQueryType("activity".equals( alertType ) ? "0" : "1");
            queryRequest.setUserName(username);
            AlertsQueryRequest alertsQueryRequest = jacksonBaseParse(AlertsQueryRequest.class, queryRequest);
            return alertHomeServiceClient.getHomeAlertVoiceContent(isUandS,alertsQueryRequest);
        } catch (Exception e) {
            log.error("[COMPOSITE]>>>Get Home Alert Voice Content Error is {}", e);
        }
        return null;
    }

    @Override
    @ResAction(resType="alert", action="view", loadResFilter=true)
    public CompAlertsOverViewResponse hisOverview(@RequestBody CompAlertsQueryRequest queryRequest) throws ParseException {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        String username = authCtx.getUser().getUsername();
        if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser()) {
            
            queryRequest.setUserName(username);
        }
        AlertsQueryRequest alertsQueryRequest = jacksonBaseParse(AlertsQueryRequest.class, queryRequest);
        log.info("#=====>  query his alert entity: {}", JSONObject.toJSONString(alertsQueryRequest));
        AlertsOverViewResponse hisOverview = alertHomeServiceClient.hisOverview(alertsQueryRequest);
        return jacksonBaseParse(CompAlertsOverViewResponse.class, hisOverview);
    }

    @Override
    public PageResponse<CompAlertsDetailResp> mobileQuery(@RequestBody CompAlertsQueryRequest queryRequest,
                                                          @RequestParam("alertType") String alertType) throws ParseException {
        return null;
    }

    @Override
    @ResAction(resType="alert", action="view", loadResFilter=true)
    public CompAlertStatisticSummaryResp mobileOverView(@RequestParam Map<String, String> params) {
        CompAlertStatisticSummaryResp response = new CompAlertStatisticSummaryResp();
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        String username = authCtx.getUser().getUsername();
        CompAlertsQueryRequest queryParam = new CompAlertsQueryRequest();
		/*
		 * if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser()) {
		 * Map<String, Set<String>> totalConstraints =
		 * resAuthHelper.verifyActionAndGetResource( authCtx.getUser(),
		 * authCtx.getResAction(), authCtx.getFlattenConstraints());
		 * 
		 * if (!super.applyGeneralAuthConstraints(totalConstraints, queryParam)) {
		 * return new com.aspire.mirror.composite.payload.alert.CompAlertStatisticSummaryResp(); }
		 * this.getAuthBizSystem(queryParam); this.getAuthDeviceType(queryParam);
		 * this.getAuthDeviceIp(queryParam); this.getAuthSourceRoom(queryParam);
		 * this.getAuthIdcType(queryParam); }
		 */
        AlertsQueryRequest alertsQueryRequest = jacksonBaseParse(AlertsQueryRequest.class, queryParam);
        popupMobileOverViewParams(alertsQueryRequest, params);
        
        try {
            // 活动告警
            if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser()) {
                CompUserVo user = userHelper.findByLdapId(username);
                alertsQueryRequest.setUserName(user.getCode());
            }
            alertsQueryRequest.setQueryType("0");
            AlertsOverViewResponse activityAlertCount = alertHomeServiceClient.overview(alertsQueryRequest,"activity");
            response.setToBeConfirmed(PayloadParseUtil.jacksonBaseParse(AlertStatisticSummaryDTO.class,activityAlertCount));
            // 活动告警
            if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser()) {
                alertsQueryRequest.setUserName(username);
            }
            alertsQueryRequest.setQueryType("1");
            AlertsOverViewResponse confirmAlertCount = alertHomeServiceClient.overview(alertsQueryRequest,"confirm");
            response.setConfirmed(PayloadParseUtil.jacksonBaseParse(AlertStatisticSummaryDTO.class,confirmAlertCount));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return response;
    }
    
    private void popupMobileOverViewParams(final AlertsQueryRequest alertsQueryRequest, final Map<String, String> params) {
    	String source = MapUtils.getString(params, "source");
    	if (StringUtils.isNotBlank(source)) {
    		alertsQueryRequest.setSource(source);
    	}
    }
}

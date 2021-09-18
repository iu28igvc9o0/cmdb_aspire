package com.aspire.mirror.alert.server.biz.helper;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.alert.server.clientservice.CmdbClient;
import com.aspire.mirror.alert.server.clientservice.CmdbInstanceClient;
import com.aspire.mirror.alert.server.clientservice.payload.InnerCmdbDeviceDetail;
import com.aspire.mirror.alert.server.dao.common.AlertProxyIdcDao;
import com.aspire.mirror.alert.server.dao.common.po.AlertProxyIdc;
import com.aspire.mirror.alert.server.vo.alert.AlertsVo;
import com.aspire.mirror.alert.server.util.MapUtils;
import com.aspire.mirror.alert.server.constant.AlertConfigConstants;
import com.aspire.mirror.alert.server.dao.cmdbInstance.CmdbInstanceMapper;
import com.aspire.mirror.alert.server.vo.model.AlertFieldVo;
import com.aspire.mirror.common.constant.SystemConstant;
import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageResponse;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * cmdb帮助类
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.helper
 * 类名称:    CmdbHelper.java
 * 类描述:    cmdb帮助类
 * 创建人:    JinSu
 * 创建时间:  2018/9/25 10:25
 * 版本:      v1.0
 */
@Slf4j
@Component
public class CmdbHelper {

    @Autowired
    private CmdbClient cmdbService;
    @Autowired
    private CmdbInstanceClient cmdbInstanceClient;

    @Autowired
    private AlertProxyIdcDao alertProxyIdcDao;

    @Autowired
    private CmdbInstanceMapper cmdbInstanceMapper;

    @Value("${systemType:simple}")
    private String systemType;

    @Value("${cmdb.token:5245ed1b-6345-11e}")
    private String cmdbToken;
    @Value("${cmdb.model:base}")
    private String cmdbModel;

    private final ConcurrentHashMap<String, String> proxyIdcMap = new ConcurrentHashMap<>();
    /**
     * 根据机房和设备ip查找设备信息. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param idc      机房名称
     * @param deviceIp 设备IP
     * @return
     */
    public Map<String, Object> queryDeviceByRoomIdAndIP(String idc, String deviceIp) {
        if (SystemConstant.BIZ_SYSTEM_BDC.equals(systemType)) {
            return null;
        }
        if (AlertConfigConstants.CMDB_MODEL_BASE.equalsIgnoreCase(cmdbModel)) {
            List<Map<String, Object>> cmdbList = cmdbInstanceMapper.selectByIPAndIdcType(deviceIp, idc);
            if (!CollectionUtils.isEmpty(cmdbList)) {
                Map<String, Object> cmdbMap = cmdbList.get(0);
                return cmdbMap;
            }
            return null;
        }
        Map<String, Object> map = Maps.newHashMap();
        map.put("idcType", idc);
        map.put("ip", deviceIp);
        map.put("token", cmdbToken);
//        return cmdbInstanceClient.queryDeviceByRoomIdAndIP(idc, deviceIp);
        return cmdbInstanceClient.queryDeviceByIdcTypeAndIP(map);
    }

    public void queryDeviceForAlert(AlertsVo alert) {
        if (alert == null) {
            return;
        }
        if (SystemConstant.BIZ_SYSTEM_BDC.equals(systemType)) {
            return;
        }
        Map<String, Object> deviceInfo
                = queryDeviceByRoomIdAndIP(alert.getIdcType(), alert.getDeviceIp());
        if (deviceInfo != null) {
//            alert.setDeviceId(deviceInfo.getId());
//            alert.setObjectId(deviceInfo.getId()); 	// 设备id
//            alert.setSourceRoom(deviceInfo.getRoomId());
//            if (!StringUtils.isEmpty(deviceInfo.getBizSystem())) alert.setBizSys(deviceInfo.getBizSystem());
//            if (!StringUtils.isEmpty(deviceInfo.getBizSystem())) alert.setBizSysName(deviceInfo.getBizSystem());
//            if (!StringUtils.isEmpty(deviceInfo.getIdcType())) alert.setIdcType(deviceInfo.getIdcType());
//            if (!StringUtils.isEmpty(deviceInfo.getDeviceClass())) alert.setDeviceClass(deviceInfo.getDeviceClass());
//            if (!StringUtils.isEmpty(deviceInfo.getDeviceType())) alert.setDeviceType(deviceInfo.getDeviceType());
//            if (!StringUtils.isEmpty(deviceInfo.getDeviceMfrs())) alert.setDeviceMfrs(deviceInfo.getDeviceMfrs());
//            if (!StringUtils.isEmpty(deviceInfo.getDeviceModel())) alert.setDeviceModel(deviceInfo.getDeviceModel());
//            if (!StringUtils.isEmpty(deviceInfo.getHostName())) alert.setHostName(deviceInfo.getHostName());
//            if (!StringUtils.isEmpty(deviceInfo.getPodName())) alert.setPodName(deviceInfo.getPodName());
//            if (!StringUtils.isEmpty(deviceInfo.getProjectName())) alert.setProjectName(deviceInfo.getProjectName());
        }
    }

    public void queryDeviceForAlertV2(JSONObject alertJson, List<AlertFieldVo> alertFieldList, List<AlertFieldVo> cmdbFieldList) {
        if (CollectionUtils.isEmpty(alertFieldList)) {
            return ;
        }
        if (SystemConstant.BIZ_SYSTEM_BDC.equals(systemType)) {
            return ;
        }

        if (AlertConfigConstants.CMDB_MODEL_BASE.equalsIgnoreCase(cmdbModel)) {
            List<Map<String, Object>> cmdbList = cmdbInstanceMapper.selectByIPAndIdcType(alertJson.getString("device_ip"), alertJson.getString("idc_type"));
            if (!CollectionUtils.isEmpty(cmdbList)) {
                Map<String, Object> cmdbMap = cmdbList.get(0);
                for (AlertFieldVo alertFieldVo : alertFieldList) {
                    if (!StringUtils.isEmpty(alertFieldVo.getCiCode())) {
                        String code = getCode(alertFieldVo.getCiCode(), cmdbFieldList);
                        if (!StringUtils.isEmpty(code)) {
                            String ciValue = MapUtils.getString(cmdbMap, code);
                            if (!StringUtils.isEmpty(ciValue)) {
                                alertJson.put(alertFieldVo.getFieldCode(), ciValue);
                            }
                        }
                    }
                }
            }
        } else {
            Map<String, Object> queryCiMap = Maps.newHashMap();
            for (AlertFieldVo alertFieldVo : alertFieldList) {
                if (AlertConfigConstants.YES.equals(alertFieldVo.getIsCiParams())) {
                    String value = alertJson.getString(alertFieldVo.getFieldCode());
                    if (!StringUtils.isEmpty(value)) {
                        queryCiMap.put(alertFieldVo.getParamsName(), value);
                    }
                }
            }
            queryCiMap.put("token", cmdbToken);
            try {
                Long time1 = System.currentTimeMillis();
                Map<String, Object> ciMap = cmdbInstanceClient.queryDeviceByIdcTypeAndIP(queryCiMap);
                log.info("------------------call queryDeviceByIdcTypeAndIP to get cmdb instance data use : {} ms", (System.currentTimeMillis()-time1));
                for (AlertFieldVo alertFieldVo : alertFieldList) {
                    if (!StringUtils.isEmpty(alertFieldVo.getCiCode())) {
                        String ciValue = MapUtils.getString(ciMap, alertFieldVo.getCiCode());
                        if (!StringUtils.isEmpty(ciValue)) {
                            alertJson.put(alertFieldVo.getFieldCode(), ciValue);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("get cmdb instance data failed,{}", e.getMessage());
            }
        }
    }

    private String getCode(String ciCode, List<AlertFieldVo> cmdbFieldList) {
        if (StringUtils.isEmpty(ciCode)) {
            return null;
        }
        for (AlertFieldVo cmdbField: cmdbFieldList) {
            if (ciCode.equalsIgnoreCase(cmdbField.getCiCode())) {
                return cmdbField.getFieldCode();
            }
        }
        return null;
    }

    /**
     * 处理ext
     * @param alertJson
     * @param alertFieldList
     * @param ext
     */
    public void doExt (JSONObject alertJson, List<AlertFieldVo> alertFieldList, Map<String, Object> ext) {
        if (!CollectionUtils.isEmpty(ext)) {
            for (AlertFieldVo alertFieldVo : alertFieldList) {
                if (!org.springframework.util.StringUtils.isEmpty(alertFieldVo.getCiCode())) {
                    String ciValue = MapUtils.getString(ext, alertFieldVo.getFieldCode());
                    if (!org.springframework.util.StringUtils.isEmpty(ciValue)) {
                        alertJson.put(alertFieldVo.getFieldCode(), ciValue);
                    }
                }
            }
        }
    }


    /**
     *
     */
    public String getDeviceIp(String deviceId) {
        List<InnerCmdbDeviceDetail> list;
        if (SystemConstant.BIZ_SYSTEM_BDC.equals(systemType)) {
            return null;
        } else {
            list = cmdbService.listDeviceDetailsByIdArr(deviceId);
        }
        if (CollectionUtils.isEmpty(list)) {
            return null;
        } else {
            return list.get(0).getIp();
        }
    }

    /**
     * 根据proxy名称查询资源池
     *
     * @param proxyName
     * @return
     */
    public String getIdc(String proxyName) {
        if (org.apache.commons.lang.StringUtils.isEmpty(proxyName)) {
            return null;
        }

        if (proxyIdcMap.isEmpty()) {
            List<AlertProxyIdc> list = alertProxyIdcDao.selectAllProxyIdc();
            //            log.info("query alert_proxy_idc success,result size is {}", list.size());
            for (AlertProxyIdc proxyIdc : list) {
                proxyIdcMap.put(proxyIdc.getProxyName(), proxyIdc.getIdc());
            }
        }
        return proxyIdcMap.get(proxyName);
    }

    public PageResponse<Map<String,Object>> getMonitorDeviceList(@RequestBody Map<String,Object> params)  {
        int pageNo = params.get("pageNo")==null?1:Integer.parseInt(params.get("pageNo").toString());
        int pageSize = params.get("pageSize")==null?50:Integer.parseInt(params.get("pageSize").toString());
        Page page = new Page();
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        page.setParams(params);
        int count = cmdbInstanceMapper.getMonitorDeviceCount(page);
        PageResponse<Map<String,Object>> pageResponse = new PageResponse< Map<String,Object>>();
        pageResponse.setCount(count);
        if (count > 0) {
            List<Map<String,Object>> listAlertFilter = cmdbInstanceMapper.getMonitorDeviceList(page);
            pageResponse.setResult(listAlertFilter);
        }
        return pageResponse;
    }

    @Scheduled(cron = "0 0 */1 * * ?")
    public void flushIdc () {
        List<AlertProxyIdc> list = alertProxyIdcDao.selectAllProxyIdc();
        //            log.info("query alert_proxy_idc success,result size is {}", list.size());
        if (!CollectionUtils.isEmpty(list)) {
            proxyIdcMap.clear();
            for (AlertProxyIdc proxyIdc : list) {
                proxyIdcMap.put(proxyIdc.getProxyName(), proxyIdc.getIdc());
            }
        }
    }
}

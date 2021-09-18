package com.aspire.mirror.alert.server.controller.inspectionDaily;

import java.util.List;
import java.util.Map;

import com.aspire.mirror.alert.api.dto.inspectionDaily.AlertInspectionDailyReq;
import com.aspire.mirror.alert.api.dto.inspectionDaily.AlertInspectionDailyResponse;
import com.aspire.mirror.alert.api.dto.inspectionDaily.AlertRourcePoolReq;
import com.aspire.mirror.alert.api.service.inspectionDaily.AlertResourcePoolService;
import com.aspire.mirror.alert.server.biz.inspectionDaily.AlertResourcePoolBiz;
import com.aspire.mirror.alert.server.util.PayloadParseUtil;
import com.aspire.mirror.alert.server.vo.inspectionDaily.AlertRourcePoolVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.aspire.mirror.alert.server.clientservice.CmdbClient;
import com.aspire.mirror.alert.server.clientservice.CommonServiceClient;
import com.aspire.mirror.alert.server.clientservice.payload.ConfigDict;
import com.aspire.mirror.alert.server.constant.AlertCommonConstant;
import com.aspire.mirror.alert.server.dao.inspectionDaily.po.AlertInspectionDaily;
import com.aspire.mirror.alert.server.util.Utils;
import com.aspire.mirror.common.constant.SystemConstant;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.FieldUtil;
import com.google.common.collect.Lists;

/**
 * 告警控制层
 * <p>
 * 项目名称: mirror平台 包: com.aspire.mirror.alert.server.controller 类名称:
 * AlertsHisController.java 类描述: 告警控制层 创建人: JinSu 创建时间: 2018/9/14 17:51 版本: v1.0
 */
@RestController
public class AlertResourcePoolController implements AlertResourcePoolService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlertResourcePoolController.class);
    @Autowired
    private AlertResourcePoolBiz alertResourcePoolBiz;

    @Autowired
    private CmdbClient cmdbClient;

    @Autowired
    private CommonServiceClient commonServiceClient;

    @Value("${systemType:simple}")
    private String systemType;





    /*
     * @Override public ResourcePoolAlertResponse
     * resourcePoolAlert(AlertRourcePoolReq pageRequset) { if (pageRequset == null)
     * { LOGGER.warn("resourcePoolAlert param pageRequset is null"); return null; }
     * PageRequest page = new PageRequest(); BeanUtils.copyProperties(pageRequset,
     * page,Utils.getNullPropertyNames(pageRequset)); Map<String, Object> map =
     * FieldUtil.getFiledMap(pageRequset); for (String key : map.keySet()) {
     * page.addFields(key, map.get(key)); } ResourcePoolAlertResponse pageResult =
     * alertResourcePoolBiz.getResourcePoolAlert(page); return pageResult; }
     */

    /**
     * 同步告警总数
     *
     * @param pageRequset
     * @return
     */
    @Override
    public ResponseEntity<String> synResourcePoolAlert(@RequestBody AlertRourcePoolReq pageRequset) {
        if (pageRequset == null) {
            LOGGER.warn("synResourcePoolAlert param pageRequset is null");
            return null;
        }
        /*
         * //获取上个月 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM"); Calendar c
         * = Calendar.getInstance(); c.setTime(new Date()); c.add(Calendar.MONTH, -1);
         * Date m = c.getTime(); String mon = format.format(m);
         * pageRequset.setMonth(mon);
         */

        pageRequset.setIdcType(alertResourcePoolBiz.getDefaultIdcType());

        setDeviceType(pageRequset);
        alertResourcePoolBiz.deleteCountByMonth(pageRequset.getMonth());
        alertResourcePoolBiz.syncAlertTotal(PayloadParseUtil.jacksonBaseParse(AlertRourcePoolVo.class, pageRequset));
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    void setDeviceType(AlertRourcePoolReq pageRequset) {
        pageRequset.setPhysicServer(AlertCommonConstant.PHYSICSERVER_DEVICETYPE);
        pageRequset.setPhysicserverPrometheus(AlertCommonConstant.PHYSICSERVER_PROMETHEUS);
        pageRequset.setPhysicserverZabbix(AlertCommonConstant.PHYSICSERVER_ZABBIX);
        List<ConfigDict> prometheus;
        if (SystemConstant.BIZ_SYSTEM_BDC.equals(systemType)) {
            prometheus = commonServiceClient.getDictsByType(AlertCommonConstant.PHYSICSERVER_SOURCE_PROMETHEUS, null, null, null);
        } else {
            prometheus = cmdbClient.getDictsByType(AlertCommonConstant.PHYSICSERVER_SOURCE_PROMETHEUS, null, null, null);
        }
        if (null != prometheus && prometheus.size() > 0) {
            pageRequset.setSourcePrometheus(prometheus.get(0).getValue());
        }

        List<ConfigDict> zabbix;
        if (SystemConstant.BIZ_SYSTEM_BDC.equals(systemType)) {
            zabbix = commonServiceClient.getDictsByType(AlertCommonConstant.PHYSICSERVER_SOURCE_PROMETHEUS, null, null, null);
        } else {
            zabbix = cmdbClient.getDictsByType(AlertCommonConstant.PHYSICSERVER_SOURCE_ZABBIX, null, null, null);
        }
        if (null != prometheus && prometheus.size() > 0) {
            pageRequset.setSourceZabbix(zabbix.get(0).getValue());
        }


        pageRequset.setNetworkType(AlertCommonConstant.NETWORK_DEVICETYPE);
        pageRequset.setOther(AlertCommonConstant.OTHER_DEVICETYPE);
    }

    /**
     * 同步告警总数
     *
     * @param pageRequset
     * @return
     */
    @Override
    public ResponseEntity<String> syncDeviceTop10Alert(@RequestBody AlertRourcePoolReq pageRequset) {
        if (pageRequset == null) {
            LOGGER.warn("syncDeviceTop10Alert param pageRequset is null");
            return null;
        }
        //获取上个月
        /*
         * SimpleDateFormat format = new SimpleDateFormat("yyyy-MM"); Calendar c =
         * Calendar.getInstance(); c.setTime(new Date()); c.add(Calendar.MONTH, -1);
         * Date m = c.getTime(); String mon = format.format(m);
         * pageRequset.setMonth(mon);
         */
        setDeviceType(pageRequset);

        pageRequset.setIdcType(alertResourcePoolBiz.getDefaultIdcType());
        //pageRequset.setDeviceDictType(AlertCommonConstant.DEVICE_DICT_TYPE);
        alertResourcePoolBiz.deleteDeviceByMonth(pageRequset.getMonth());
        alertResourcePoolBiz.syncDeviceTop10Alert(PayloadParseUtil.jacksonBaseParse(AlertRourcePoolVo.class, pageRequset));
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> syncMoniterTop10Alert(@RequestBody AlertRourcePoolReq pageRequset) {
        if (pageRequset == null) {
            LOGGER.warn("syncMoniterTop10Alert param pageRequset is null");
            return null;
        }
        //获取上个月
        /*
         * SimpleDateFormat format = new SimpleDateFormat("yyyy-MM"); Calendar c =
         * Calendar.getInstance(); c.setTime(new Date()); c.add(Calendar.MONTH, -1);
         * Date m = c.getTime(); String mon = format.format(m);
         * pageRequset.setMonth(mon);
         */
        setDeviceType(pageRequset);

        //pageRequset.setDeviceDictType(AlertCommonConstant.DEVICE_DICT_TYPE);
        pageRequset.setIdcType(alertResourcePoolBiz.getDefaultIdcType());
        alertResourcePoolBiz.deleteMoniterByMonth(pageRequset.getMonth());
        alertResourcePoolBiz.syncMoniterTop10Alert(PayloadParseUtil.jacksonBaseParse(AlertRourcePoolVo.class, pageRequset));
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> syncDistributionAlert(@RequestBody AlertRourcePoolReq pageRequset) {
        if (pageRequset == null) {
            LOGGER.warn("syncDistributionAlert param pageRequset is null");
            return null;
        }
        setDeviceType(pageRequset);

        pageRequset.setIdcType(alertResourcePoolBiz.getDefaultIdcType());
        alertResourcePoolBiz.deleteRecordByMonth(pageRequset.getMonth());
        alertResourcePoolBiz.syncDistributionAlert(PayloadParseUtil.jacksonBaseParse(AlertRourcePoolVo.class, pageRequset));
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @Override
    public PageResponse<AlertInspectionDailyResponse> inspectionDaily(@RequestBody AlertInspectionDailyReq pageRequset) {
        if (pageRequset == null) {
            LOGGER.warn("inspectionDaily param pageRequset is null");
            return null;
        }
        PageRequest page = new PageRequest();
        BeanUtils.copyProperties(pageRequset, page, Utils.getNullPropertyNames(pageRequset));
        Map<String, Object> map = FieldUtil.getFiledMap(pageRequset);
        for (String key : map.keySet()) {
            page.addFields(key, map.get(key));
        }
        PageResponse<AlertInspectionDaily> pageResult = alertResourcePoolBiz.getInspectionDaily(page);

        List<AlertInspectionDailyResponse> listAlert = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(pageResult.getResult())) {
            for (AlertInspectionDaily filterScene : pageResult.getResult()) {
                AlertInspectionDailyResponse filterSceneDTO = new AlertInspectionDailyResponse();
                BeanUtils.copyProperties(filterScene, filterSceneDTO);
                listAlert.add(filterSceneDTO);
            }
        }

        PageResponse<AlertInspectionDailyResponse> result = new PageResponse<AlertInspectionDailyResponse>();
        result.setCount(pageResult.getCount());
        result.setResult(listAlert);
        return result;
    }

    @Override
    public List<Map<String, Object>> exportDaily(@RequestBody AlertInspectionDailyReq pageRequest) {
        if (pageRequest == null) {
            LOGGER.warn("exportDaily param pageRequset is null");
            return null;
        }
        PageRequest page = new PageRequest();
        BeanUtils.copyProperties(pageRequest, page, Utils.getNullPropertyNames(pageRequest));
        Map<String, Object> map = FieldUtil.getFiledMap(pageRequest);
        for (String key : map.keySet()) {
            page.addFields(key, map.get(key));
        }
        List<Map<String, Object>> pageResult = alertResourcePoolBiz.queryExportData(page);


        return pageResult;
    }

}

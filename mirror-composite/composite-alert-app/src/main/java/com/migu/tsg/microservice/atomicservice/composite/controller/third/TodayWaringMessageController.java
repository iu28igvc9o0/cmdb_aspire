package com.migu.tsg.microservice.atomicservice.composite.controller.third;

import com.aspire.mirror.alert.api.dto.third.TodayWarningMessageDTO;
import com.aspire.mirror.composite.payload.third.TodayWarningMessageResponse;
import com.aspire.mirror.composite.service.third.AlertTodayWaringMessageService;
import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.third.AlertTodayWaringMessageServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceV2Controller;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;


@Slf4j
@RestController
public class TodayWaringMessageController extends CommonResourceV2Controller implements AlertTodayWaringMessageService {
    private Logger logger = LoggerFactory.getLogger(TodayWaringMessageController.class);
    @Autowired
    private AlertTodayWaringMessageServiceClient alertTodayWaringMessageServiceClient;

    /**
     * @return根据信息港资源池去查询出当天的告警信息
     */
    @Override
    public List<TodayWarningMessageResponse> getTodayWarningMessae(@RequestParam(value = "idc_type", required = false) String idc_type,
                                                                   @RequestParam(value = "source_room", required = false) String source_room) {
        List<TodayWarningMessageDTO> todayWarningMessae = alertTodayWaringMessageServiceClient.getTodayWarningMessae(idc_type, source_room);
        List<TodayWarningMessageResponse> alertList = Lists.newArrayList();
        if (null == todayWarningMessae) {
            return alertList;
        }
        alertList = jacksonBaseParse(TodayWarningMessageResponse.class, todayWarningMessae);
        return alertList;

    }

    /**
     * @param idc_type    查询出信息港资源和池设备名称的告警信息
     * @param source_room
     * @return
     */


    @Override
    public List<TodayWarningMessageResponse> getTodayWarningMessaeByDeviceMfrs(@RequestParam(value = "idc_type", required = false) String idc_type,
                                                                               @RequestParam(value = "source_room", required = false) String source_room) {
        List<TodayWarningMessageDTO> todayWarningMessaeByDeviceMfrs = alertTodayWaringMessageServiceClient.getTodayWarningMessaeByDeviceMfrs(idc_type, source_room);
        List<TodayWarningMessageResponse> todayWarningMessaeByDeviceMfrsList = Lists.newArrayList();
        if (null == todayWarningMessaeByDeviceMfrs) {
            return todayWarningMessaeByDeviceMfrsList;
        }
        todayWarningMessaeByDeviceMfrsList = jacksonBaseParse(TodayWarningMessageResponse.class, todayWarningMessaeByDeviceMfrs);
        return todayWarningMessaeByDeviceMfrsList;
    }


    /**
     * 查询出机房和池设备类型的告警信息
     */


    @Override
    public List<TodayWarningMessageResponse> getSummaryByDeviceTypeAndSoyrceRoomAndDeviceClass(@RequestParam(value = "idc_type", required = false) String idc_type,
                                                                                               @RequestParam(value = "source_room", required = false) String source_room) {
        List<TodayWarningMessageDTO> summaryByDeviceTypeAndSoyrceRoomAndDeviceClass = alertTodayWaringMessageServiceClient.getSummaryByDeviceTypeAndSoyrceRoomAndDeviceClass(idc_type, source_room);
        List<TodayWarningMessageResponse> summaryByDeviceTypeAndSoyrceRoomAndDeviceClassList = Lists.newArrayList();
        if (null == summaryByDeviceTypeAndSoyrceRoomAndDeviceClass) {
            return summaryByDeviceTypeAndSoyrceRoomAndDeviceClassList;
        }
        summaryByDeviceTypeAndSoyrceRoomAndDeviceClassList = jacksonBaseParse(TodayWarningMessageResponse.class, summaryByDeviceTypeAndSoyrceRoomAndDeviceClass);
        return summaryByDeviceTypeAndSoyrceRoomAndDeviceClassList;
    }


}

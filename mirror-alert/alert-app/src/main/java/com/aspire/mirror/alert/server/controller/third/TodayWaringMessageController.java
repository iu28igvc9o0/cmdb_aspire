package com.aspire.mirror.alert.server.controller.third;
import com.aspire.mirror.alert.api.dto.third.TodayWarningMessageDTO;
import com.aspire.mirror.alert.api.service.third.AlertTodayWaringMessageService;
import com.aspire.mirror.alert.server.biz.third.TodayWaringMessageService;
import com.aspire.mirror.alert.server.util.DateUtils;
import com.aspire.mirror.alert.server.util.PayloadParseUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RestController
public class TodayWaringMessageController implements AlertTodayWaringMessageService {
    @Autowired
    private TodayWaringMessageService todayWaringMessageService;

    /**
     * @return根据信息港资源池去查询出当天的告警信息
     */
    @Override
    public List<TodayWarningMessageDTO> getTodayWarningMessae(@RequestParam(value = "idc_type", required = false) String idc_type,
                                                              @RequestParam(value = "source_room", required = false) String source_room) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String startTime = simpleDateFormat.format(date);
        String endTime = DateUtils.getSpecifiedDayAfter(startTime, 1);
        Map<String, Object> param = Maps.newHashMap();
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        param.put("idc_type", idc_type);
        param.put("source_room", source_room);
        return PayloadParseUtil.jacksonBaseParse(TodayWarningMessageDTO.class, todayWaringMessageService.getSummaryByIdctype(param));
    }

    /**
     * @return查询出信息港资源和池设备名称的告警信息
     */
    @Override
    public List<TodayWarningMessageDTO> getTodayWarningMessaeByDeviceMfrs(@RequestParam(value = "idc_type", required = false) String idc_type,
                                                                          @RequestParam(value = "source_room", required = false) String source_room) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String startTime = simpleDateFormat.format(date);
        String endTime = DateUtils.getSpecifiedDayAfter(startTime, 1);
        Map<String, Object> param = Maps.newHashMap();
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        param.put("idc_type", idc_type);
        param.put("source_room", source_room);
        return PayloadParseUtil.jacksonBaseParse(TodayWarningMessageDTO.class, todayWaringMessageService.getSummaryByDeviceMfrs(param));
    }

    /**
     * 查询出机房和池设备类型的告警信息
     *
     * @return
     */
    @Override
    public List<TodayWarningMessageDTO> getSummaryByDeviceTypeAndSoyrceRoomAndDeviceClass(@RequestParam(value = "idc_type", required = false) String idc_type,
                                                                                          @RequestParam(value = "source_room", required = false) String source_room) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String startTime = simpleDateFormat.format(date);
        String endTime = DateUtils.getSpecifiedDayAfter(startTime, 1);
        Map<String, Object> param = Maps.newHashMap();
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        param.put("idc_type", idc_type);
        param.put("source_room", source_room);
        return PayloadParseUtil.jacksonBaseParse(TodayWarningMessageDTO.class, todayWaringMessageService.getSummaryByDeviceTypeAndSoyrceRoomAndDeviceClass(param));
    }
}

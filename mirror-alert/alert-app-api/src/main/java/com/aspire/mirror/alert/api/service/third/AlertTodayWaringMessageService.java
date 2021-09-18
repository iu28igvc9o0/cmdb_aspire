package com.aspire.mirror.alert.api.service.third;
import com.aspire.mirror.alert.api.dto.third.TodayWarningMessageDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Api(value = "告警统计")
@RequestMapping(value = "/v1/alerts/TodayWaringMessage/")
public interface AlertTodayWaringMessageService {
    /**
     * 资源池当天告警总览
     *
     * @return AlertStatisticSummaryResponse 告警总览
     */
    @PostMapping(value = "/getTodayWarningMessae", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "资源池当天告警总览", notes = "告警总览", response = TodayWarningMessageDTO.class, tags = {"AlertTodayWaringMessage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = TodayWarningMessageDTO.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = TodayWarningMessageDTO.class)})
    List<TodayWarningMessageDTO> getTodayWarningMessae(@RequestParam(value = "idc_type", required = false) String idc_type,
                                                       @RequestParam(value = "source_room") String source_room);
    /**
     * 信息港资源池依据品牌总览
     *
     * @return AlertStatisticSummaryResponse 告警总览
     */
    @PostMapping(value = "/getTodayWarningMessaeByDeviceMfrs", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "信息港资源池依据品牌总览", notes = "告警总览", response = TodayWarningMessageDTO.class, tags = {"AlertTodayWaringMessage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = TodayWarningMessageDTO.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = TodayWarningMessageDTO.class)})
    List<TodayWarningMessageDTO> getTodayWarningMessaeByDeviceMfrs(@RequestParam(value = "idc_type", required = false) String idc_type,
                                                                   @RequestParam(value = "source_room", required = false) String source_room);


    /**
     * 3050机房依据设备类型总览
     *
     * @return AlertStatisticSummaryResponse 告警总览
     */
    @PostMapping(value = "/getSummaryByDeviceTypeAndSoyrceRoomAndDeviceClass", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "3050机房依据设备类型总览", notes = "告警总览", response = TodayWarningMessageDTO.class, tags = {"AlertTodayWaringMessage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = TodayWarningMessageDTO.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = TodayWarningMessageDTO.class)})
    List<TodayWarningMessageDTO> getSummaryByDeviceTypeAndSoyrceRoomAndDeviceClass(@RequestParam(value = "idc_type", required = false) String idc_type,
                                                                                   @RequestParam(value = "source_room", required = false) String source_room);

}

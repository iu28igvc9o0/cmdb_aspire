package com.aspire.mirror.alert.server.clientservice;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.alert.server.clientservice.payload.ConfigDict;
import com.aspire.mirror.alert.server.clientservice.payload.InnerCmdbDeviceDetail;

@FeignClient(value = "COMMON-SERVICE",url = "http://10.1.203.100:1111")
public interface CommonServiceClient {

    /**
     *
     */
    @PostMapping(value = "/cmdb/form/getForms")
    Map<String, Object> getForms(@RequestBody Map request);


    /**
     * 根据机房和设备ip查找设备信息. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param deviceIp
     * @return
     */
    @GetMapping(value = "/cmdb/instance/queryDeviceByRoomAndIP",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    InnerCmdbDeviceDetail queryDeviceByRoomIdAndIP(
            @RequestParam(value = "idc", required = false) String idc, @RequestParam("deviceIp") String deviceIp);

    @GetMapping(value = "/cmdb/repertryInstance/listInstanceBaseInfo/{device_ids}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    List<InnerCmdbDeviceDetail> listDeviceDetailsByIdArr(@PathVariable("device_ids") String joinedIds);

    @GetMapping(value = "/cmdb/configDict/getDictsByType", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    List<ConfigDict> getDictsByType(@RequestParam("type") String type,
                                    @RequestParam(value = "pid", required = false) String pid,
                                    @RequestParam(value = "pValue", required = false) String pValue,
                                    @RequestParam(value = "pType", required = false) String pType);

}

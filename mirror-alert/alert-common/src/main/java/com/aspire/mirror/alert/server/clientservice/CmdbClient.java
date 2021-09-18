package com.aspire.mirror.alert.server.clientservice;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.alert.server.clientservice.payload.ConfigDict;
import com.aspire.mirror.alert.server.clientservice.payload.InnerCmdbDeviceDetail;
import com.aspire.mirror.alert.server.domain.BizSysRequestBody;
import com.aspire.ums.cmdb.dict.payload.Dict;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@FeignClient(value = "CMDB", url = "http://10.1.203.100:2222")
public interface CmdbClient {

    /**
     *
     */
    @PostMapping(value = "/cmdb/form/getForms")
    Map<String, Object> getForms(@RequestBody Map request);

//    @GetMapping(value = "/cmdb/repertryInstance/getInstanceByIds/{instanceIds}")
//    List<Map> getInstanceByIds(@PathVariable("instanceIds") String instanceIds);


	/**
	* 根据机房和设备ip查找设备信息. <br/>
	*
	* 作者： pengguihua
	* @param pid
	* @return
	*/  
//	@GetMapping(value = "/cmdb/instance/queryDeviceByRoomAndIP",
//				produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	InnerCmdbDeviceDetail queryDeviceByRoomIdAndIP(
//			@RequestParam(value = "idc", required = false) String idc, @RequestParam("deviceIp") String deviceIp);

	@GetMapping(value = "/cmdb/repertryInstance/listInstanceBaseInfo/{device_ids}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	List<InnerCmdbDeviceDetail> listDeviceDetailsByIdArr(@PathVariable("device_ids") String joinedIds);
	
	@GetMapping(value = "/cmdb/configDict/getDictsByType", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	List<ConfigDict> getDictsByType(@RequestParam("type") String type,
                                    @RequestParam(value = "pid", required = false) String pid,
                                    @RequestParam(value = "pValue", required = false) String pValue,
                                    @RequestParam(value = "pType", required = false) String pType);

	@PostMapping(value = "/v1/cmdb/instanceSearch/selectDepartBizSystemInfo")
	@ApiOperation(value = "部门业务系统查询接口", notes = "部门业务系统查询接口", tags = {"CMDB DeviceStatistic API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
			@ApiResponse(code = 500, message = "内部错误")})
	Map<String, Object> selectDepartBizSystemInfo(@RequestBody BizSysRequestBody requestBody);

	@RequestMapping(value = "/cmdb/configDict/update", method = RequestMethod.POST)
	String updateCfgDict(@RequestBody Dict dict);
}

/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  InspectionServiceClient.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年3月5日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2020 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.clientservice;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.aspire.mirror.ops.clientservice.model.CmdbInstanceQueryParam;
import com.aspire.mirror.ops.clientservice.model.CmdbListResponse;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: CmdbServiceClient
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年3月5日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@FeignClient(value = "CMDB", url = "http://10.1.203.100:2222")
public interface CmdbServiceClient {
	
	/** 
	 * 功能描述: 查询cmdb
	 * <p>
	 * @return
	 */
	@PostMapping(value = "/cmdb/instance/listV3", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public CmdbListResponse queryCmdbInstanceList(CmdbInstanceQueryParam param);

	@PostMapping(value = "/cmdb/instance/queryDeviceByIdcTypeAndIP")
	CmdbListResponse.CmdbInstance queryDeviceByRoomIdAndIP(@RequestBody Map<String, Object> params);
}

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

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.aspire.mirror.ops.clientservice.model.InspectionCountQueryModel;
import com.aspire.mirror.ops.clientservice.model.InspectionCountResp;
import com.aspire.mirror.ops.domain.OpsApItemType;
import com.aspire.mirror.ops.domain.OpsTimeConsumeStatisticBase;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: InspectionServiceClient
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
@FeignClient("inspection-service")
public interface InspectionServiceClient {
	
	/** 
	 * 功能描述: 巡检自愈指标同步  
	 * <p>
	 * @return
	 */
	@PostMapping(value = "/v1/autoRepair/fetchWholeApItemList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<OpsApItemType> fetchWholeApItemList();
	
	
	/** 
	 * 功能描述: TODO  
	 * <p>
	 * @param queryParam
	 * @return
	 */
	@PostMapping(value = "v1/report_data/getInspectionCount", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	InspectionCountResp getInspectionTaskStatistics(@RequestBody InspectionCountQueryModel queryParam);

	@PostMapping(value = "v1/report_data/getInspectionTimeStatistic", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    OpsTimeConsumeStatisticBase getInspectionTimeStatistic(Map<String,Object> param);
}

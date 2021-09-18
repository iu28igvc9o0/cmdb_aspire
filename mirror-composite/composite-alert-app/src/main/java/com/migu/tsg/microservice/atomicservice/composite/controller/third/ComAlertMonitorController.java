package com.migu.tsg.microservice.atomicservice.composite.controller.third;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

import java.util.List;
import java.util.Map;

import com.aspire.mirror.composite.payload.third.ComAlertsCountDTO;
import com.aspire.mirror.composite.payload.third.ComAlertsDeviceCountsDTO;
import com.aspire.mirror.composite.payload.third.ComAlertsLevelCountsResponse;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.third.AlertThirdMonitorServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.aspire.mirror.composite.service.third.IComAlertMonitorService;

/**
 * 历史告警服务
 * <p>
 * 项目名称: mirror平台 包: com.aspire.mirror.alert.api.metrics 类名称:
 * AlertsHisService.java 类描述: 历史告警服务 创建人: JinSu 创建时间: 2018/9/19 11:19 版本: v1.0
 */
@RestController
public class ComAlertMonitorController  implements IComAlertMonitorService{

	 private Logger logger = LoggerFactory.getLogger(ComAlertMonitorController.class);

	 	@Autowired
		private AlertThirdMonitorServiceClient thirdAlertMonitorServiceClient;
	 
	 	
	 	
		@Override
		public ComAlertsCountDTO idcTypeAlertCount(@RequestParam(value="idc_type", required = false)String idcType) {
			logger.info("展示信息港资源池待解决和已解决告警总数  idcTypeAlertCount,idc_type:{}",idcType);
			return jacksonBaseParse(ComAlertsCountDTO.class,  thirdAlertMonitorServiceClient.idcTypeAlertCount(idcType));
		}

		@Override
		public ComAlertsLevelCountsResponse toBeResolvedLevalCounts(@RequestParam(value="idc_type", required = false)String idcType) {
			logger.info("待解决告警分类统计toBeResolvedLevalCounts,idc_type:{}",idcType);
			return jacksonBaseParse(ComAlertsLevelCountsResponse.class, thirdAlertMonitorServiceClient.toBeResolvedLevalCounts(idcType));
		}

		@Override
		public ComAlertsDeviceCountsDTO deviceLevelCount() throws Exception {
			logger.info("设备告警分类统计deviceLevelCount begin");
			return jacksonBaseParse(ComAlertsDeviceCountsDTO.class, thirdAlertMonitorServiceClient.deviceLevelCount());
		}

		@Override
		public List<Map<String, String>> department1Rate() throws Exception {
		
		logger.info("TOP10租户资源利用率department1Rate begin");
		List<Map<String, String>> list = thirdAlertMonitorServiceClient.department1Rate();
		logger.info("TOP10租户资源利用率department1Rate end");
		return list;

		}

	
}

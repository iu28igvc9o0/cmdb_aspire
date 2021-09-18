package com.aspire.mirror.alert.server.controller.third;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.aspire.mirror.alert.api.service.third.PrometheusAlertsService;
import com.aspire.mirror.alert.server.biz.alert.PrometheusAlertsBiz;

/**
 * prometheus告警控制层
 * <p>
 * 项目名称：mirror平台
 * 包：com.aspire.mirror.alert.server.controller
 * 类名称：PrometheusAlertsController.java
 * 类描述：prometheus告警控制层
 * 创建人：zhujiahao
 * 创建时间：2019/1/11 14:00
 * 版本：v1.0
 *
 */
@RestController
public class PrometheusAlertsController implements PrometheusAlertsService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PrometheusAlertsController.class);
	
	@Autowired
	private PrometheusAlertsBiz prometheusAlertsBiz;

	@Override
	public Object createdAlerts(@RequestBody String message) {
		/*
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		StringBuilder stringBuilder = new StringBuilder();
		InputStream is;
		try {
			is = request.getInputStream();
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			String str;
			while((str = streamReader.readLine()) != null) {
				stringBuilder.append(str);
			}
		} catch (IOException e) {
			LOGGER.error("prometheus告警获取异常: "+e.getMessage());
			return "prometheus告警获取异常: "+e.getMessage();
		}
		*/
		if(StringUtils.isEmpty(message)) {
			LOGGER.warn("PrometheusAlertsController.createdAlerts param json is null");
			return "PrometheusAlertsController.createdAlerts param json is null";
		}
		LOGGER.debug("prometheus alert message is {}", message);
		String result = prometheusAlertsBiz.insert(message);
		
		return result;
	}

}

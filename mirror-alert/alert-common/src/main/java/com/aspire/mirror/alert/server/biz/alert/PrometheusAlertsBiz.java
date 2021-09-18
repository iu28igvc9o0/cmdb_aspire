package com.aspire.mirror.alert.server.biz.alert;

/**
 * prometheus告警业务层接口
 * <p>
 * 项目名称：mirror平台
 * 包：com.aspire.mirror.alert.server.biz
 * 类名称：PrometheusAlertsBiz.java
 * 类描述：prometheus告警业务层接口
 * 创建人：zhujiahao
 * 创建时间：2019/1/11 14:00
 * 版本：v1.0
 *
 */
public interface PrometheusAlertsBiz {
	
	/**
	 * @param message 告警信息
	 * @return
	 */
	String insert(String message);

}

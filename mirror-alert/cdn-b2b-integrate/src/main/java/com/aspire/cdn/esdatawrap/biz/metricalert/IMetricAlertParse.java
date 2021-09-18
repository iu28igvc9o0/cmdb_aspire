package com.aspire.cdn.esdatawrap.biz.metricalert;

/** 
 *
 * 项目名称: esdatawrap 
 * <p/>
 * 
 * 类名: IMetricAlertParse
 * <p/>
 *
 * 类功能描述: 指标告警转换接口, 如果业务需要存储指标告警，业务对象需要实现此接口
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年6月23日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
public interface IMetricAlertParse {
	MetricAlert parse2MetricAlert(int moniResult, int alertLevel);
}

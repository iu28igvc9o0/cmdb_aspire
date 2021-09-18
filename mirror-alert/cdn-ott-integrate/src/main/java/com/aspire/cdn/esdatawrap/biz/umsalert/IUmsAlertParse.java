package com.aspire.cdn.esdatawrap.biz.umsalert;

/** 
 *
 * 项目名称: cdn-integrate 
 * <p/>
 * 
 * 类名: IUmsAlertParse
 * <p/>
 *
 * 类功能描述: ums告警转换接口
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年6月22日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
public interface IUmsAlertParse {
	 public UmsAlertMessage parse2UmsAlert();
}

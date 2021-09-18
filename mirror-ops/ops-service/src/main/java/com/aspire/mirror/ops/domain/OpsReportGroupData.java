/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  OpsReportGroupData.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年4月7日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2020 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.domain;

import lombok.Data;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsReportGroupData
 * <p/>
 *
 * 类功能描述: 报表分组数量统计数据对象
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年4月7日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
public class OpsReportGroupData {
	private String	groupCode;
	private Integer	groupCount;
}

/**
 *
 * 项目名： ops-api 
 * <p/> 
 *
 * 文件名:  OpsLabel.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年12月3日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.api.domain;

import lombok.Data;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: OpsLabel
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年12月3日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Data
public class OpsLabel {
	private String	code;
	private String	label;
	private String	description;
	
	@Data
	public static class OpsLabelQueryModel {
		private String code;
		private String labelLike;
		private String descriptionLike;
	}
}

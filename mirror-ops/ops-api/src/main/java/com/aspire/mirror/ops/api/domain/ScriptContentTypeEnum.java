/**
 *
 * 项目名： ops-api 
 * <p/> 
 *
 * 文件名:  ScriptContentTypeEnum.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月28日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.api.domain;

import lombok.Getter;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: ScriptContentTypeEnum
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月28日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Getter
public enum ScriptContentTypeEnum {
	SHELL(1, "shell"), BAT(2, "bat"), PYTHON(3, "python");

	private ScriptContentTypeEnum(Integer code, String label) {
		this.code = code;
		this.label = label;
	}

	private Integer	code;
	private String	label;
}

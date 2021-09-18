package com.aspire.mirror.template.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Result通用返回结果类
 * <p>
 * 项目名称:  咪咕微服务运营平台-CICD
 * 包:       com.migu.tsg.msp.microservice.atomicservice.cicd.commom.entity
 * 类名称:    Result.java
 * 类描述:    返回结果信息封装
 * 创建人:    WuFan
 * 创建时间:  2017/07/27 00:49
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
public class Result implements Serializable {
    /**
    * serialVersionUID:TODO(用一句话描述这个变量表示什么).
    * @since JDK 1.6
    */

    private static final long serialVersionUID = 9085085005467879389L;
    private String resultCode;
	private String resultDes;

	private static final String SUCCESS_CODE = "0";
	private static final String SUCCESS_DESC = "success";

	private static final Result SUCCESSES = new Result(SUCCESS_CODE, SUCCESS_DESC);

	public Result(String resultCode, String resultDes) {
		this.resultCode = resultCode;
		this.resultDes = resultDes;
	}
	/**
	 * 成功
	 * @return Result成功实体
	 */
	public static Result success() {
		return SUCCESSES;
	}
}

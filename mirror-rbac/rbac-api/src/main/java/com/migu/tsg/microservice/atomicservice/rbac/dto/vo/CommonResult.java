package com.migu.tsg.microservice.atomicservice.rbac.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class CommonResult<E> {
	
	@ApiModelProperty(name="state",notes="状态 true：操作成功  false：操作失败")
	Boolean state = true;

	@ApiModelProperty(name="message",notes="执行结果消息提示信息")
	String message;
	
	@ApiModelProperty(name="value",notes="返回参数")
	E value;
	
	public CommonResult(String message) {
		this(true, message, null);
	}
	
	public CommonResult(boolean success, String message) {
		this(success, message, null);
	}

	public CommonResult(Boolean state, E value) {
		this.state = state;
		this.value = value;
	}

	public CommonResult(boolean success, String message, E value) {
		this.state = success;
		this.message = message;
		this.value = value;
	}

	public CommonResult() {
	}
	
	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public E getValue() {
		return value;
	}

	public void setValue(E value) {
		this.value = value;
	}
}

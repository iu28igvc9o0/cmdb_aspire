package com.aspire.ums.cmdb.vo;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class AjaxResponseObject implements Serializable {
	private static final long	serialVersionUID	= 1L;
	private String status;//页面返回状态
	private String message;//提示信息
	private Object data;//数据内容
	private Boolean success;//是否成功

	public AjaxResponseObject() {

	}
	
	public AjaxResponseObject(boolean success,Object data){
		this.success=success;
		this.data=data;
	}
	
	public AjaxResponseObject (boolean success,String status,String message){
		this.success=success;
		this.status = status;
		this.message=message;
	}
	
	public AjaxResponseObject(boolean success,String message,Object data){
		this.success=success;
		this.message=message;
		this.data=data;
	}
	
	public AjaxResponseObject(boolean success,String status,String message,Object data){
		this.success=success;
		this.status = status;
		this.message=message;
		this.data=data;
	}
	
	public AjaxResponseObject(String status,String message,Object data){
		this.success = true;
		this.status=status;
		this.message=message;
		this.data=data;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
}

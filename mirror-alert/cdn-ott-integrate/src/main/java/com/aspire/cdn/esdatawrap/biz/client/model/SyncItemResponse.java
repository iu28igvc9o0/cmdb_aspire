package com.aspire.cdn.esdatawrap.biz.client.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SyncItemResponse {
	private String code;		// 0000:成功        0001:时间格式错误      0002：必填参数为空    9999：系统异常
	private String message;
	
	public static final SyncItemResponse SUCCESS 			= new SyncItemResponse("0000", "success");
	public static final SyncItemResponse WRONG_TIME_FORMAT 	= new SyncItemResponse("0001", "incorrect time format");
	public static final SyncItemResponse ABSENT_PARAMS 		= new SyncItemResponse("0002", "the required parameter device_level is blank.");
	public static final SyncItemResponse GENERAL_ERROR 		= new SyncItemResponse("9999", "exception");
	
//	SyncItemResponse
	public SyncItemResponse(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public static SyncItemResponse build(SyncItemResponse rawResp, String message) {
		message = message == null ? rawResp.getMessage() : message;
		return new SyncItemResponse(rawResp.getCode(), message);
	}
}

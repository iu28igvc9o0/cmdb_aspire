package com.aspire.mirror.inspection.server.clientservice.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OsaCmdbDetail {
	@JsonProperty("ID")
	private String deviceId;
	
	@JsonProperty("DEVICE_LOG_NAME")
	private String deviceName;
	
	@JsonProperty("DEVICE_IP")
	private String deviceIp;
	
	@JsonProperty("IDC")
	private String roomId;
	
	@JsonProperty("DEVICE_TYPE")
	private String deviceType;
}

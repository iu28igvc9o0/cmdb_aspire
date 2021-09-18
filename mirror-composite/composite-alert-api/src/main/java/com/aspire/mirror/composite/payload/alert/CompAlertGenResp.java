package com.aspire.mirror.composite.payload.alert;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CompAlertGenResp {
	
	/**
     * 告警ID
     */
    @JsonProperty("alert_id")
    private String alertId;
    /**
     * 监控时间
     */
	@JsonProperty("cur_moni_time")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date curMoniTime;
     
	
	/** 监控指标/内容，关联触发器name */
    @JsonProperty("moni_index")
    private String moniIndex;
	
    /**
     * 当前监控值
     */
	@JsonProperty("cur_moni_value")
    private String curMoniValue;


    /**
     * 监控内容
     */
    @JsonProperty("moni_object")
    private String moniObject;
    

}

package com.aspire.mirror.alert.api.dto.notify;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

 


@NoArgsConstructor
@Data
public class NotifyPageResponse<T> {

	
	/**
     * 总数
     */
    private int count;
	
    
    /**
     * 通知成功数
     */
    @JsonProperty("success_count")
    private int successCount;
    
    /**
     * 通知失败数
     */
    @JsonProperty("fall_count")
    private int  fallCount;
    
    
    /**
     * 列表对象
     */
    private List<T> result;
}
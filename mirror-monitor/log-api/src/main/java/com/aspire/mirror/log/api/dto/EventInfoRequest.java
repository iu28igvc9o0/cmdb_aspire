package com.aspire.mirror.log.api.dto;

import java.util.List;

import com.aspire.mirror.log.api.dto.model.EventInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 事件请求类
* Project Name:log-api
* File Name:EventInfoRequest.java
* Package Name:com.migu.tsg.microservice.monitor.log.dto
* ClassName: EventInfoRequest <br/>
* date: 2017年8月8日 下午3:30:10 <br/>
* @author jiangfuyi
* @version 1.0
* @since JDK 1.8
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventInfoRequest {
    // 事件基础信息DTO
    private List<EventInfoDTO> eventDTOs;
}

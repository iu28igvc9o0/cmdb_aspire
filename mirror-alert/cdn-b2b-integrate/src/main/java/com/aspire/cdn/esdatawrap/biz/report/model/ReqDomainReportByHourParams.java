package com.aspire.cdn.esdatawrap.biz.report.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class ReqDomainReportByHourParams {
	public static final DateTimeFormatter	TIME_FORMAT		= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private List<String>	reqDomainList;
	private String			startTime;			// yyyy-MM-dd HH:mm
	private String			endTime;			// yyyy-MM-dd HH:mm		
	private String			reportKey;			
	
	@JsonIgnore
	public Long getStartTimeMills() {
		if (!StringUtils.isBlank(startTime)) {
			return LocalDateTime.parse(startTime, TIME_FORMAT).toInstant(ZoneOffset.of("+8")).toEpochMilli();
		}
		return null;
	}
	
	@JsonIgnore
	public Long getEndTimeMills() {
		if (!StringUtils.isBlank(endTime)) {
			return LocalDateTime.parse(endTime, TIME_FORMAT).toInstant(ZoneOffset.of("+8")).toEpochMilli();
		}
		return null;
	}
	
	@JsonIgnore
	public String getJoinReqdomainArrText() {
		if (CollectionUtils.isEmpty(reqDomainList)) {
			return "";
		}
		return StringUtils.join(reqDomainList.stream().map(item -> {
					return "\"" + item + "\"";
				}).collect(Collectors.toList()), ",");
	}
}

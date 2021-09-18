package com.aspire.cdn.esdatawrap.biz.generaloperate.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.Setter;


public class HostPingRequestBody {
	@Setter
	private String joinHostList;
	
	public List<String> resolveHostList() {
		return StringUtils.isBlank(joinHostList) ? new ArrayList<String>() : Arrays.asList(joinHostList.split(","));
	}
	
	@Override
	public String toString() {
		return joinHostList;
	}
}

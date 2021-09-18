package com.aspire.mirror.alert.server.vo.bpm;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SmsBpmCallParam {
	private List<String> mobileList;
	private String content;
	private Map<String, String> mobileIdMap;
}

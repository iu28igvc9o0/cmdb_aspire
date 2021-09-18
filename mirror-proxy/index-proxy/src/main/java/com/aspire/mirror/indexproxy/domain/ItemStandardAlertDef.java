package com.aspire.mirror.indexproxy.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import io.krakens.grok.api.Grok;
import io.krakens.grok.api.GrokCompiler;
import io.krakens.grok.api.Match;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 
 *
 * 项目名称: index-proxy 
 * <p/>
 * 
 * 类名: StandardItemAlertDef
 * <p/>
 *
 * 类功能描述: 监控项标准告警定义
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年12月25日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
@EqualsAndHashCode(of= {"itemKey"})
public class ItemStandardAlertDef {
	private static final List<String> ESCAPE_ARR 	=  Arrays.asList("$","(",")","*","+",".","[","]","?","\\","^","{","}","|");
	private static final Pattern PARAM_PATTERN 		=  Pattern.compile("\\{#([^\\}]+)\\}|\\{([^#\\}]+)\\}");
	private String itemKey;
	private String alertTitle;
	private String alertContentTemplate;
	private String itemKeyGrokPattern;
	private String itemKeyPrefix;
	
	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
		StringBuffer sb = new StringBuffer();
		Matcher matcher = PARAM_PATTERN.matcher(itemKey);
		boolean grokFlag = false;
		Integer firstMark = -1;
		Integer lastIdx = 0;
		while (matcher.find()) {
			grokFlag = true;
			Integer start = matcher.start();
			if (firstMark < 0) {
				firstMark = start;
			}
			String normalText = itemKey.substring(lastIdx, start);
			sb.append(handleRegexEscapeChars(normalText));
			String paramName = matcher.group(1);
			paramName = paramName != null ? paramName : matcher.group(2); 
			sb.append("%{GREEDYDATA:" + paramName + "}");
			lastIdx = matcher.end();
		}
		if (grokFlag) {
			sb.append(handleRegexEscapeChars(itemKey.substring(lastIdx)));
		}
		
		if (!grokFlag) {
			itemKeyPrefix = itemKey;
			itemKeyGrokPattern = null;
		} 
		else {
			itemKeyPrefix = itemKey.substring(0, firstMark);
			itemKeyGrokPattern = sb.toString();
		}
	}
	
	private String handleRegexEscapeChars(String source) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < source.length(); i++) {
			String c = String.valueOf(source.charAt(i));
			int escapeIdx = ESCAPE_ARR.indexOf(c);
			if (escapeIdx >= 0) {
				sb.append("\\").append(c);
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	/** 
	 * 功能描述: 根据deviceItemKey, 解析itemKey中的参数键值对  
	 * <p>
	 * @param deviceItemKey
	 * @return
	 */
	public Map<String, Object> resolveItemKeyParamKeyValuePair(String deviceItemKey) {
		if (StringUtils.isBlank(itemKeyGrokPattern)) {
			return new HashMap<>();
		}
		GrokCompiler grokCompiler = GrokCompiler.newInstance();
        grokCompiler.registerDefaultPatterns();
        Grok grok = grokCompiler.compile(itemKeyGrokPattern);
	    Match grokMatch = grok.match(deviceItemKey);
	    return grokMatch.capture();
	}
	
	public static void main(String[] args) {
		ItemStandardAlertDef def = new ItemStandardAlertDef();
		def.setItemKey("vmware.vm.cpu.usage[{SNMPVALUE},{#HOST.IP},{HOST.HOST}]");
		System.out.println(def.resolveItemKeyParamKeyValuePair("vmware.vm.cpu.usage[Vsi-interface20,10.13.34.123,test36]]"));
	}
}

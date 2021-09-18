package com.aspire.cdn.esdatawrap.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/** 
 *
 * 项目名称: esdatawrap 
 * <p/>
 * 
 * 类名: LabelContentHolder
 * <p/>
 *
 * 类功能描述: 标签文本内容持有器
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年5月11日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Component
public class LabelContentHolder {
	private static final String					KEY_PREFIX					= "<<<<<";
	private static final String					KEY_SUFFIX					= ">>>>>";
	private static final String					LOAD_FILE					= "labelTextConfig.json";
	private static final String					VAR_PLACE_HOLDER_PREFIX		= "${";
	private static final String					VAR_PLACE_HOLDER_SUFFIX		= "}";
	private static final Map<String, String>	KEY_CONTENT_CACHE			= new HashMap<>();

	public static final String					KEY_SCROLL_LOAD_IDX_DATA	= "scroll_load_whole_idx_data";
	public static final String					KEY_SCROLL_FETCH_DATA		= "scroll_fetch_data";
	
	@PostConstruct
	private void init() throws Exception {
		ClassPathResource labelFile = new ClassPathResource(LOAD_FILE);
		try (InputStream input = labelFile.getInputStream()) {
			List<String> lines = IOUtils.readLines(input);
			parse(lines);
		}
	}
	
	public String getContentByLabelKey(String labelKey) {
		return getContentByLabelKey(labelKey, null);
	}
	
	public String getContentByLabelKey(String labelKey, Map<String, ?> varibles) {
		String content = KEY_CONTENT_CACHE.get(labelKey);
		if (varibles == null) {
			return content;
		}
		for (Map.Entry<String, ?> entry : varibles.entrySet()) {
			String findKey = VAR_PLACE_HOLDER_PREFIX + entry.getKey() + VAR_PLACE_HOLDER_SUFFIX;
			String replaceVal = String.valueOf(entry.getValue());
			content = content.replace(findKey, replaceVal);
		}
		return content;
	}
	
	private void parse(List<String> lines) {
		String label = null;
		List<String> content = new ArrayList<>();
		
		for (String line : lines) {
			if (label != null) {
				if (line.trim().startsWith(KEY_PREFIX) && line.trim().endsWith(KEY_SUFFIX)) {
					KEY_CONTENT_CACHE.put(label, StringUtils.join(content, "\n"));
					label = null;
					content.clear();
					continue;
				}	
				content.add(line);
				continue;	
			}	
			
			if (line.trim().startsWith(KEY_PREFIX) && line.trim().endsWith(KEY_SUFFIX)) {
				label = line.trim().substring(KEY_PREFIX.length(), line.trim().length() - KEY_SUFFIX.length());
			}	
		}
		
		if (label != null) {
			throw new RuntimeException("The config in the " + LOAD_FILE + " file is wrong, please check.");
		}
	}
}

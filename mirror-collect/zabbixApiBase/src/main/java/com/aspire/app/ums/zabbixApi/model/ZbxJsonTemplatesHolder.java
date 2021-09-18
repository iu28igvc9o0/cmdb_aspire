package com.aspire.app.ums.zabbixApi.model;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aspire.app.ums.util.XstreamUtils;
import com.aspire.app.ums.zabbixApi.ZabbixApiHelper;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

/** 
 *
 * 项目名称: zabbixApiBase 
 * <p/>
 * 
 * 类名: ZbxJsonTemplatesHolder
 * <p/>
 *
 * 类功能描述: ZABBIX API 请求JSON模版 持有器
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2016年5月19日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2016 卓望公司-版权所有 
 *
 */
@XStreamAlias("zabbixApiJson")
public class ZbxJsonTemplatesHolder {
	private static final Logger logger = LoggerFactory.getLogger(ZbxJsonTemplatesHolder.class);
	
	@XStreamImplicit(itemFieldName="template")
	private List<Template> templateList;
	
	// 缓存分组名 与  模版列表映射 
	private static Map<String, List<Template>> GROUP_MAP_TEMPLATES 	= new ConcurrentHashMap<String, List<Template>>();
	
	// 核心组名，提供登录、登录模版
	public static final String 			CORE_GROUP					= "core";
	private static final String			CORE_TEMPLATE_FILE			= "/zabbixApi_json_core.xml";
	
	// 初始化装载  core 模版
	static {
		init();
	}
	
	/** 
	 * 功能描述: 初始化加载core分组模版  
	 * <p>
	 */
	private static void init() {
		try {
			InputStream is = ZbxJsonTemplatesHolder.class.getResourceAsStream(CORE_TEMPLATE_FILE);
			ZbxJsonTemplatesHolder.loadJsonTemplates(CORE_GROUP, is, true);
		} catch (Exception e) {
			logger.error("Failed to load core templates for zabbixApiBase!", e);
		}
	}
	
	/** 
	 * 功能描述: 从配置文件加载  zabbixApi请求 JSON 模版,  放入 groupName 指定的分组中
	 * <p>
	 * @param groupName
	 * @param filePath
	 */
	public static void loadJsonTemplates(String groupName, String filePath) throws Exception {
		InputStream is = new FileInputStream(filePath);
		loadJsonTemplates(groupName, is, true);
	}
	
	/** 
	 * 功能描述: 从流读入 JSON 模版,  放入 groupName 指定的分组中,  默认关闭流
	 * <p>
	 * @param groupName	配置归类的组名
	 * @param is		XML配置输入流
	 */
	public static void loadJsonTemplates(String groupName, InputStream is) throws Exception {
		loadJsonTemplates(groupName, is, true);
	}
	
	/** 
	 * 功能描述: 从流读入 JSON 模版,  放入 groupName 指定的分组中
	 * <p>
	 * @param groupName	配置归类的组名
	 * @param is		XML配置输入流
	 * @param autoClose	是否自动关闭输入流is
	 */
	public static void loadJsonTemplates(String groupName, InputStream is, boolean autoClose) throws Exception {
		ZbxJsonTemplatesHolder loadConfig = loadFromConfig(is, autoClose);
		
		if (loadConfig == null) {
			return;
		}
		List<Template> templateList = loadConfig.templateList;
		if (templateList == null || templateList.isEmpty()) {
			return;
		}
		
		List<Template> cacheList = GROUP_MAP_TEMPLATES.get(groupName);
		if (cacheList == null) {
			cacheList = new ArrayList<Template>();
		}
		
		cacheList.addAll(templateList);
		GROUP_MAP_TEMPLATES.put(groupName, cacheList);
	}
	
	/** 
	 * 功能描述: 根据分组名，及模版id，查找模版  
	 * <p>
	 * @param groupName
	 * @param tempId
	 * @return
	 */
	public static Template getTemplateById(String groupName, String tempId) {
		if (StringUtils.isBlank(tempId)) {
			return null;
		}
		
		List<Template> groupList = getGroupTempList(groupName);
		if (groupList == null || groupList.isEmpty()) {
			return null;
		}
		
		for (Template temp : groupList) {
			if (tempId.equals(temp.getId())) {
				return temp;
			}
		}
		return null;
	}
	
	/** 
	 * 功能描述: 返回指定分组下的所有模版  
	 * <p>
	 * @param groupName
	 * @return
	 */
	public static List<Template> getGroupTempList(String groupName) {
		List<Template> groupList = GROUP_MAP_TEMPLATES.get(groupName);
		if (groupList == null) {
			groupList = new ArrayList<Template>();
		}
		return groupList;
	}
	
	/** 
	 * 功能描述: 读取JSON模版配置  
	 * <p>
	 * @param is		XML输入流
	 * @param autoClose	是否自动关闭is流
	 */
	private static ZbxJsonTemplatesHolder loadFromConfig(InputStream is, boolean autoClose) {
		ZbxJsonTemplatesHolder readObj = null;
		try {
			readObj = XstreamUtils.xml2Object(is, ZbxJsonTemplatesHolder.class);
		} catch (Exception e) {
			logger.error(null, e);
		} finally {
			if (autoClose) {
				IOUtils.closeQuietly(is);
			}
		}
		return readObj;
	}
	
	@XStreamConverter(value=ToAttributedValueConverter.class, strings={"content"})
	public class Template {
		@XStreamAsAttribute
		private String id;
		private String content;
		
		public String getId() {
			return id;
		}
		
		public String getContent() {
			return content;
		}
	}
	
	public static void main(String[] args) throws Exception {
		Template temp = ZbxJsonTemplatesHolder.getTemplateById(CORE_GROUP, "user.login");
		String rawJson = temp.getContent();
		String json = String.format(rawJson, ZabbixApiHelper.getRandomSeq(), "admin", "password");
		System.out.println(json);
	}
}

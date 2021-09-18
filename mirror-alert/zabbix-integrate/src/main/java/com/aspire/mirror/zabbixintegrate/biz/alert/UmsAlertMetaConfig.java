package com.aspire.mirror.zabbixintegrate.biz.alert;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aspire.mirror.zabbixintegrate.util.XstreamUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 *
 * 项目名称: com.aspire.zabbix
 * <p/>
 * 
 * 类名: UmsAlertMetaConfig
 * <p/>
 *
 * 类功能描述: UMS中告警配置元信息
 * <p/>
 *
 * @author pengguihua
 *
 * @date 2016年3月23日
 *
 * @version V1.0 <br/>
 *
 *          <b>Copyright(c)</b> 2016 卓望公司-版权所有
 *
 */
@XStreamAlias("umsAlertMetas")
class UmsAlertMetaConfig {
	private static final Logger						logger					= LoggerFactory.getLogger(UmsAlertMetaConfig.class);
	private static final String						WILDCARD_ALERT_LEVEL	= "*";
	private static final String						XML_FILE_NAME			= "UmsAlertMetaConfig.xml";
	private static final String						ITEM_SPLIT_SIGN			= ",";
	
	@XStreamImplicit(itemFieldName="alertMetaItem")
	private List<AlertMetaItem>						metaItemList				= new ArrayList<AlertMetaItem>();

	// 告警级别映射： 键为ZABBIX告警级别, 值为  配置Item
	private static final Map<String, AlertMetaItem>	zbxLevelMapMetaItem		= new HashMap<String, AlertMetaItem>();

	private static final UmsAlertMetaConfig			INSTANCE				= loadFromXml();

	
	public void setMetaList(List<AlertMetaItem> metaList) {
		this.metaItemList = metaList;
	}
	
	public List<AlertMetaItem> getMetaList() {
		return metaItemList;
	}
	
	// 解析
	static {
		resolveAlertLevelMatch();
	}

	/**
	 * 功能描述: 处理 ZABBIX告警级别与 UMS告警级别的匹配关系
	 * <p>
	 */
	private static void resolveAlertLevelMatch() {
		if (INSTANCE == null || INSTANCE.metaItemList == null) {
			logger.error(null, new NullPointerException("There is no alert meta info configed! "));
			return;
		}

		zbxLevelMapMetaItem.clear();
		for (AlertMetaItem umsItem : INSTANCE.metaItemList) {
			String zbxLevels = umsItem.getZbxAlertLevels();
			if (StringUtils.isBlank(zbxLevels)) {
				logger.warn("There is no config value with the zabbixLevels attribute in the item: " + umsItem);
				continue;
			}

			String[] sysLevelArr = zbxLevels.split(ITEM_SPLIT_SIGN);
			for (String sysLevel : sysLevelArr) {
				if (StringUtils.isBlank(sysLevel)) {
					continue;
				}
				zbxLevelMapMetaItem.put(sysLevel.trim(), umsItem);
			}
		}
	}

	/**
	 * 功能描述: 根据指定的ZABBIX告警级别，获取对应的UMS告警级别 code
	 * <p>
	 * 
	 * @param zbxAlertLevel
	 * @return
	 */
	public static String getMatchedUmsLevel(String zbxAlertLevel) {
		AlertMetaItem metaItem = getMatchedUmsAlertMeta(zbxAlertLevel);
		if (metaItem == null) {
			return null;
		}
		return metaItem.getUmsAlertLevel();
	}

	/**
	 * 功能描述: 根据指定的ZABBIX告警级别，获取对应的UMS告警级别 name
	 * <p>
	 * 
	 * @param zbxAlertLevel
	 * @return
	 */
	public static String getMatchedUmsLevelName(String zbxAlertLevel) {
		AlertMetaItem metaItem = getMatchedUmsAlertMeta(zbxAlertLevel);
		if (metaItem == null) {
			return null;
		}
		return metaItem.getUmsAlertDesc();
	}

	private static AlertMetaItem getMatchedUmsAlertMeta(String zbxAlertLevel) {
		AlertMetaItem umsItem = zbxLevelMapMetaItem.get(zbxAlertLevel);
		if (umsItem == null) {
			umsItem = zbxLevelMapMetaItem.get(WILDCARD_ALERT_LEVEL);
		}
		return umsItem;
	}
	
	/**
	 * 功能描述: 根据指定的UMS告警级别code，获取对应的UMS告警级别 name
	 * <p>
	 * 
	 * @param umsLevel
	 * @return
	 */
	public static String getUmsAlertNameByLevel(String umsLevel) {
		if (StringUtils.isBlank(umsLevel)) {
			logger.error("The input umsLevel is wrong: " + umsLevel);
			return null;
		}
		
		umsLevel = umsLevel.trim();
		for (AlertMetaItem umsItem : INSTANCE.metaItemList) {
			if (umsLevel.equals(umsItem.getUmsAlertLevel())) {
				return umsItem.getUmsAlertDesc();
			}
		}
		return null;
	}

	/**
	 * 功能描述: 读取配置文件并返回配置对象
	 * <p>
	 * 
	 * @return
	 */
	private static UmsAlertMetaConfig loadFromXml() {
		UmsAlertMetaConfig parseObj = null;
		InputStream is = null;

		try {
			File cfgFile = new File(XML_FILE_NAME);
			if (cfgFile.isFile()) {
				is = new FileInputStream(cfgFile);
			} else {
				is = UmsAlertMetaConfig.class.getResourceAsStream("/" + XML_FILE_NAME);
			}
			parseObj = XstreamUtils.xml2Object(is, UmsAlertMetaConfig.class);
		} catch (Exception e) {
			logger.error(null, e);
		} finally {
			IOUtils.closeQuietly(is);
		}
		
		return parseObj;
	}
	
	private static class AlertMetaItem {
		@XStreamAsAttribute
		private String	umsAlertLevel;
		@XStreamAsAttribute
		private String	umsAlertDesc;
		@XStreamAsAttribute
		private String	zbxAlertLevels;

		public String getUmsAlertLevel() {
			return umsAlertLevel;
		}

		public String getUmsAlertDesc() {
			return umsAlertDesc;
		}
		
		public String getZbxAlertLevels() {
			return zbxAlertLevels;
		}

		@Override
		public String toString() {
			return "Ums AlertMetaItem detail: umsAlertLevel=" + umsAlertLevel + "|umsAlertDesc=" + umsAlertDesc 
				 + "|zbxAlertLevels=" + zbxAlertLevels;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(UmsAlertMetaConfig.getMatchedUmsLevel("0"));
		System.out.println(UmsAlertMetaConfig.getMatchedUmsLevel("1"));
		System.out.println(UmsAlertMetaConfig.getMatchedUmsLevel("2"));
		System.out.println(UmsAlertMetaConfig.getMatchedUmsLevel("3"));
		System.out.println(UmsAlertMetaConfig.getMatchedUmsLevel("4"));
		System.out.println(UmsAlertMetaConfig.getMatchedUmsLevel("5"));
		System.out.println(UmsAlertMetaConfig.getMatchedUmsLevel("8"));
		System.out.println(UmsAlertMetaConfig.getMatchedUmsLevel("*"));
		System.out.println("---------------------------------------------");
		
		System.out.println(UmsAlertMetaConfig.getUmsAlertNameByLevel("1"));
		System.out.println(UmsAlertMetaConfig.getUmsAlertNameByLevel("2"));
		System.out.println(UmsAlertMetaConfig.getUmsAlertNameByLevel("3"));
		System.out.println("---------------------------------------------");
		
		System.out.println(UmsAlertMetaConfig.getMatchedUmsLevelName("0"));
		System.out.println(UmsAlertMetaConfig.getMatchedUmsLevelName("1"));
		System.out.println(UmsAlertMetaConfig.getMatchedUmsLevelName("2"));
		System.out.println(UmsAlertMetaConfig.getMatchedUmsLevelName("3"));
		System.out.println(UmsAlertMetaConfig.getMatchedUmsLevelName("4"));
		System.out.println(UmsAlertMetaConfig.getMatchedUmsLevelName("5"));
		System.out.println(UmsAlertMetaConfig.getMatchedUmsLevelName("8"));
		System.out.println(UmsAlertMetaConfig.getMatchedUmsLevelName("*"));
	}
}

package com.aspire.app.ums.zabbixApi;

import java.io.InputStream;
import java.security.SecureRandom;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aspire.app.ums.zabbixApi.model.ZbxApiSvrInfo;
import com.aspire.app.ums.zabbixApi.model.ZbxApiSvrInfoHolder;
import com.aspire.app.ums.zabbixApi.model.ZbxJsonTemplatesHolder;
import com.aspire.app.ums.zabbixApi.model.ZbxJsonTemplatesHolder.Template;

import net.sf.json.JSONObject;

/** 
 *
 * 项目名称: pentaho-zabbix 
 * <p/>
 * 
 * 类名: ZabbixApiHelper
 * <p/>
 *
 * 类功能描述: ZabbixApi调用帮助类
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2016年4月21日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2016 卓望公司-版权所有 
 *
 */
public final class ZabbixApiHelper {
	private static final Logger logger = LoggerFactory.getLogger(ZabbixApiHelper.class);
	
	private static final ThreadLocal<UserSession> USER_AUTH = new ThreadLocal<UserSession>();
	private static final String LOGIN_METHOND				= "user.login";
	private static final String LOGOUT_METHOND				= "user.logout";
	private static final String ERROR_KEY					= "error";
	
	private static final IZabbixApiSendResp apiFacade 		= new ZabbixApiSendRespImpl();
	
	/** 
	 * 功能描述: 通过zabbixAPI登录，并返回登录token，如果登录失败，返回 null
	 * <p>
	 * @param zbxApiUrl
	 * @param userName
	 * @param passWord
	 * @return
	 * @throws Exception 
	 */
	public static String login(String zbxApiUrl, String userName, String passWord) throws Exception {
		IZabbixApiSendResp apiFacade = getDefaultZabbixApiSendResp();
		String reqJson = getJsonTemplateById(ZbxJsonTemplatesHolder.CORE_GROUP, LOGIN_METHOND, getRandomSeq(), userName, passWord);
		
		JSONObject respJson = apiFacade.request(zbxApiUrl, reqJson);
		if (respJson == null || respJson.isEmpty()) {
			throw new RuntimeException("Failed to login zabbixApi with params: zbxApiUrl=" + zbxApiUrl + "|userName=" + userName);
		}
		
		Object error = respJson.get(ERROR_KEY);
		if (error != null) {
			String errorTip = " Failed to login the zabbix api server(" + zbxApiUrl + "),"
							+ " the error detail : " + error.toString();
			logger.error(errorTip);
			throw new RuntimeException(errorTip);
		}
		
		String authToken = respJson.getString("result");
		USER_AUTH.set(new UserSession(zbxApiUrl, authToken));
		return authToken;
	}
	
	/** 
	 * 功能描述: 当用户通过  ZabbixApiHelper#login(url, userName, pasword)登录成功后， 在调用线程中获取AUTH码  <BR/>
	 * 注意：	      需要先调用   ZabbixApiHelper#login(url, userName, pasword) 成功登录 Zabbix API，否则此方法返回null
	 * <p>
	 * @return
	 */
	public static String getLoginedAuthToken() {
		UserSession sess = USER_AUTH.get();
		if (sess == null) {
			return null;
		}
		return sess.getAuthToken();
	}
	
	/** 
	 * 功能描述: 当用户通过  ZabbixApiHelper#login(url, userName, pasword)登录成功后， 在调用线程中获取ApiURL  <BR/>
	 * 注意：	      需要先调用   ZabbixApiHelper#login(url, userName, pasword) 成功登录 Zabbix API，否则此方法返回null
	 * <p>
	 * @return
	 */
	public static String getLoginedZabbixApiUrl() {
		UserSession sess = USER_AUTH.get();
		if (sess == null) {
			return null;
		}
		return sess.getApiUrl();
	}
	
	/** 
	 * 功能描述: 获取随机 整数  
	 * <p>
	 * @return
	 */
	public static int getRandomSeq() {
		return new SecureRandom().nextInt();
	}
	
	/** 
	 * 功能描述: 登出  
	 * <p>
	 */
	public static void logout() {
		logout(getLoginedZabbixApiUrl(), getLoginedAuthToken());
	}
	
	/** 
	 * 功能描述: 登出  
	 * <p>
	 * @param zbxApiUrl
	 * @param authToken
	 */
	public static void logout(String zbxApiUrl, String authToken) {
		if (StringUtils.isBlank(zbxApiUrl) || StringUtils.isBlank(authToken)) {
			return;
		}
		
		IZabbixApiSendResp apiFacade = getDefaultZabbixApiSendResp();
		String reqJson = getJsonTemplateById(ZbxJsonTemplatesHolder.CORE_GROUP, LOGOUT_METHOND, authToken, getRandomSeq());
		
		try {
			USER_AUTH.remove();
			apiFacade.request(zbxApiUrl, reqJson);
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	/** 
	 * 功能描述: 根据模版ID及指定的参数，从指定的分组，获取格式化后的JSON  
	 * <p>
	 * @param groupName
	 * @param tempId
	 * @param formatParams
	 * @return
	 */
	public static String getJsonTemplateById(String groupName, String tempId, Object ... formatParams) {
		Template temp = ZbxJsonTemplatesHolder.getTemplateById(groupName, tempId);
		if (temp == null) {
			logger.info("There is no template configed in the zabbix api config file with templateId: " + tempId);
			return null;
		}
		if (formatParams == null || formatParams.length == 0) {
			return temp.getContent();
		}
		return String.format(temp.getContent(), formatParams);
	}
	
	/** 
	 * 功能描述: 从filePath指定的XML配置文件，加载JSON模版，并归入groupName的分组
	 * <p>
	 * @param groupName
	 * @param filePath
	 * @throws Exception
	 */
	public static void loadJsonTemplates(String groupName, String filePath) throws Exception {
		ZbxJsonTemplatesHolder.loadJsonTemplates(groupName, filePath);
	}
	
	/** 
	 * 功能描述: 从is指定的输入流，加载JSON模版，并归入groupName的分组，加载完成后直接关闭输入流
	 * <p>
	 * @param groupName
	 * @param filePath
	 * @throws Exception
	 */
	public static void loadJsonTemplates(String groupName, InputStream is) throws Exception {
		ZbxJsonTemplatesHolder.loadJsonTemplates(groupName, is);
	}
	
	/** 
	 * 功能描述: 从is指定的输入流，加载JSON模版，并归入groupName的分组，根据autoClose参数，决定加载完成后是否关闭输入流
	 * <p>
	 * @param groupName
	 * @param filePath
	 * @throws Exception
	 */
	public static void loadJsonTemplates(String groupName, InputStream is, boolean autoClose) throws Exception {
		ZbxJsonTemplatesHolder.loadJsonTemplates(groupName, is, autoClose);
	}
	
	/** 
	 * 功能描述: 获取指定分组下的所有主机信息  
	 * <p>
	 * @param groupName
	 * @return
	 */
	public static List<ZbxApiSvrInfo> getGroupSvrInfoList(String groupName) {
		return ZbxApiSvrInfoHolder.getGroupSvrInfoList(groupName);
	}
	
	/** 
	 * 功能描述: 获取指定分组下的所有主机信息, 使用  clazz指定的  ZbxApiSvrInfo 对象子类
	 * <p>
	 * @param groupName
	 * @return
	 */
	public static <T extends ZbxApiSvrInfo> List<T> getGroupSvrInfoList(String groupName, Class<T> clazz) {
		return ZbxApiSvrInfoHolder.getGroupSvrInfoList(groupName, clazz);
	}
	
	/** 
	 * 功能描述: 添加  groupList 指定的主机信息   到   groupName 分组  
	 * <p>
	 * @param groupName
	 * @param svrList
	 * @throws Exception
	 */
	public static <T extends ZbxApiSvrInfo> void loadZbxServerInfos(String groupName, List<T> svrList) throws Exception {
		ZbxApiSvrInfoHolder.loadZbxServerInfos(groupName, svrList);
	}
	
	/** 
	 * 功能描述: 从配置文件加载   ZABBIX Server 配置信息,  放入 groupName 指定的分组中
	 * <p>
	 * @param groupName
	 * @param filePath
	 */
	public static void loadZbxServerInfos(String groupName, String filePath) throws Exception {
		ZbxApiSvrInfoHolder.loadZbxServerInfos(groupName, filePath);
	}
	
	/** 
	 * 功能描述: 从流读入 ZABBIX Server 配置信息,  放入 groupName 指定的分组中,  默认关闭流
	 * <p>
	 * @param groupName	配置归类的组名
	 * @param is		XML配置输入流
	 */
	public static void loadZbxServerInfos(String groupName, InputStream is) throws Exception {
		ZbxApiSvrInfoHolder.loadZbxServerInfos(groupName, is, true);
	}
	
	/** 
	 * 功能描述: 从流读入 ZABBIX Server 配置信息,  放入 groupName 指定的分组中,  根据autoClose决定是否关闭流  
	 * <p>
	 * @param groupName
	 * @param is
	 * @param autoClose
	 * @throws Exception
	 */
	public static void loadZbxServerInfos(String groupName, InputStream is, boolean autoClose) throws Exception {
		ZbxApiSvrInfoHolder.loadZbxServerInfos(groupName, is, autoClose);
	}
	
	/** 
	 * 功能描述: 获取  ZABBIX API 调用 网络请求实现  
	 * <p>
	 * @return
	 */
	public static IZabbixApiSendResp getDefaultZabbixApiSendResp() {
		return apiFacade;
	}
	
	/** 
	 *
	 * 项目名称: zabbixApiBase 
	 * <p/>
	 * 
	 * 类名: UserSession
	 * <p/>
	 *
	 * 类功能描述: 保留用户认证信息
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
	private static class UserSession {
		private String apiUrl;
		private String authToken;
		
		public UserSession(String apiUrl, String authToken) {
			this.apiUrl = apiUrl;
			this.authToken = authToken;
		}
		
		public String getApiUrl() {
			return apiUrl;
		}

		public String getAuthToken() {
			return authToken;
		}
	}
}

package com.aspire.app.ums.zabbixApi;

import net.sf.json.JSONObject;

/** 
*
* 项目名称: pentaho-zabbix 
* <p/>
* 
* 类名: IZabbixApiSendResp
* <p/>
*
* 类功能描述: zabbixApi请求发送接收接口
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
public interface IZabbixApiSendResp {
	
	/** 
	 * 功能描述: 使用   queryJson 发起 zabbixAPI请求，并返回响应结果
	 * <p>
	 * @param targetURL
	 * @param queryJson
	 * @return
	 */
	public JSONObject request(String targetURL, JSONObject queryJson) throws Exception;
	
	/** 
	 * 功能描述: TODO  
	 * <p>
	 * @param targetURL
	 * @param jsonText
	 * @return
	 * @throws Exception
	 */
	public JSONObject request(String targetURL, String jsonText) throws Exception;
	
	
	/** 
	 * 功能描述: 使用   queryJson 发起 zabbixAPI请求，在响应JSON中查找指定key的属性值返回
	 * <p>
	 * @param targetURL
	 * @param queryJson
	 * @param key
	 * @return
	 */
	public String request(String targetURL, JSONObject queryJson, String key) throws Exception;
	
	/** 
	 * 功能描述: TODO  
	 * <p>
	 * @param targetURL
	 * @param jsonText
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public String request(String targetURL, String jsonText, String key) throws Exception;
}

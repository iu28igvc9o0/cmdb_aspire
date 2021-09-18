package com.aspire.cdn.esdatawrap.biz;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aspire.cdn.esdatawrap.biz.client.IWeixinCorpClient;
import com.aspire.cdn.esdatawrap.biz.client.model.WeixinMessageSendRequest;
import com.aspire.cdn.esdatawrap.biz.client.model.WeixinCorpAccessTokenRepsonse;
import com.aspire.cdn.esdatawrap.biz.client.model.WeixinGeneralResponse;
import com.aspire.cdn.esdatawrap.client.ClientServiceBuilder;
import com.aspire.cdn.esdatawrap.config.LabelContentHolder;
import com.aspire.cdn.esdatawrap.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: esdatawrap6 
 * <p/>
 * 
 * 类名: WeixinAcessHelper
 * <p/>
 *
 * 类功能描述: 微信访问业务类
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年5月18日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Slf4j
@Component
final class WeixinAcessHelper {
	private static final String									JOIN_CHAR				= "|";
	private static final String									WEIXIN_BASE_URL			= "https://qyapi.weixin.qq.com/";
	public static final String									WEIXIN_CORP_ID			= "wxd744910e5efaf8bb";
	@Autowired
	private LabelContentHolder									labelHodler;
	private Map<String, Map<String, String>>					proAgentSecretCache;
	private final ConcurrentMap<String, WeixinCorpAccessTokenRepsonse>	corpAgentMapAccessToken	= new ConcurrentHashMap<>();
	
	@PostConstruct
	private void init() {
		String json = labelHodler.getContentByLabelKey("province_map_agentidSecret");
		TypeReference<Map<String, Map<String, String>>> typeRef = new TypeReference<Map<String,Map<String,String>>>() {};
		proAgentSecretCache = JsonUtil.jacksonConvert(json, typeRef);
	}
	
	public String getAgentIdByProvinceName(String provinceName) {
		Map<String, String> agentSecret = proAgentSecretCache.get(provinceName);
		if (agentSecret == null) {
			log.warn("There is not weixin agent and secret info for the province: {}.", provinceName);
			return null;
		}
		return agentSecret.get("agent_id");
	}
	
	/** 
	 * 功能描述: 获取公众号省份对应的accessToken  
	 * <p>
	 * @param provinceName
	 * @return
	 */
	public String getWeixinCorpAccessTokenByProvince(String provinceName) {
		Map<String, String> agentSecret = proAgentSecretCache.get(provinceName);
		if (agentSecret == null) {
			throw new RuntimeException("There is not weixin agent and secret info for the province: " + provinceName);
		}
		String agentId = agentSecret.get("agent_id");
		String secret = agentSecret.get("secret");
		String cacheKey = provinceName + JOIN_CHAR + agentId;
		
		WeixinCorpAccessTokenRepsonse cacheToken = corpAgentMapAccessToken.get(cacheKey);
		if (cacheToken != null && !cacheToken.need2RefreshToken()) {
			return cacheToken.getAccessToken();
		}
		cacheToken = loadAccessToken(secret);
		if (cacheToken.getErrcode() > 0) {
			String tip = String.format("Error to fetch the accessToken for province %s, agentId %s with errorCode %s and detail: %s.", 
					provinceName, agentId, cacheToken.getErrcode(), cacheToken.getErrmsg());
			throw new RuntimeException(tip);
		}
		corpAgentMapAccessToken.put(cacheKey, cacheToken);
		return cacheToken.getAccessToken();
	}
	
	private WeixinCorpAccessTokenRepsonse loadAccessToken(String secret) {
		IWeixinCorpClient client = ClientServiceBuilder.buildClientService(IWeixinCorpClient.class, WEIXIN_BASE_URL);
		WeixinCorpAccessTokenRepsonse fetchAccessToken = client.fetchAccessToken(WEIXIN_CORP_ID, secret);
		return fetchAccessToken;
	}
	
	public void notifyAlertByWeixinCorp(String accessToken, WeixinMessageSendRequest alert) {
		notifyAlertByWeixinCorp(accessToken, alert, true);
	}
	
	public void notifyAlertByWeixinCorp(String accessToken, WeixinMessageSendRequest alert, boolean errorThrow) {
		IWeixinCorpClient client = ClientServiceBuilder.buildClientService(IWeixinCorpClient.class, WEIXIN_BASE_URL);
		WeixinGeneralResponse response = client.sendWeixinCorpMessage(accessToken, alert);
		if (errorThrow && response.getErrcode() != null && response.getErrcode().intValue() > 0) {
			throw new RuntimeException(String.format("Error when invoke weixin alert notify with errorcode %s and detail: %s",
					response.getErrcode(), response.getErrmsg()));
		}
	}
}

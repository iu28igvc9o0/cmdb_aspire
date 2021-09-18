package com.aspire.cdn.esdatawrap.biz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import com.aspire.cdn.esdatawrap.biz.generaloperate.model.AppClientInfo;
import com.aspire.cdn.esdatawrap.util.JsonUtil;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.AbstractTemplate;
import com.gexin.rp.sdk.template.StartActivityTemplate;
import com.gexin.rp.sdk.template.style.AbstractNotifyStyle;
import com.gexin.rp.sdk.template.style.Style0;

import lombok.extern.slf4j.Slf4j;


/** 
 *
 * 项目名称: cdn-b2b-integrate 
 * <p/>
 * 
 * 类名: AppPushHelper
 * <p/>
 *
 * 类功能描述: App消息推送帮助类
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年11月16日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Slf4j
@Component
@ConditionalOnExpression("${local.config.mobile-notify.switch:false}")
public class AppPushHelper {
	// 如果需要使用HTTPS，直接修改url即可
//	public static final String	URL	= "http://api.getui.com/apiex.htm";
//	private static String URL    = "https://api.getui.com/apiex.htm";

	@Value("${local.config.mobile-notify.appId}")
	private String				appId;
	
	@Value("${local.config.mobile-notify.appKey}")
	private String				appKey;
	
	@Value("${local.config.mobile-notify.masterSecret}")
	private String				masterSecret;
	
	@Value("${local.config.mobile-notify.url:http://api.getui.com/apiex.htm}")
	private String				url;
	
	@Value("${local.config.mobile-notify.logo:push.png}")
	private String				logo;
	
	
	public static void main(String[] args) {
		AppPushHelper helper = new AppPushHelper();
		helper.appId = "ZpD30C3I1Y5h3Wdl7XGt19";
		helper.appKey = "wWUITBbWFj5NLXEhBrlIF8";
		helper.masterSecret = "K52GaC4tXZ9uL94Vajwh96";
		helper.url = "http://api.getui.com/apiex.htm";
		
		List<AppClientInfo> clientList = new ArrayList<>();
		AppClientInfo info = new AppClientInfo();
		info.setClientId("c23f7e6e55a8062e12aae45e2ecf27da");
		info.setAccountName("lupengjie");
		info.setClientPlatform("ios");
		clientList.add(info);
		
		info = new AppClientInfo();
		info.setClientId("aea3344f151ba111139f7aaf978f3bc2");
		info.setAccountName("pengguihua");
		info.setClientPlatform("android");
		clientList.add(info);
		
		Map<String, Object> params = new HashMap<>();
		params.put("actionType", "alert_push");
		params.put("rAlertId", "26a410a60b49da9b4e829965d016200e_3");
//		params.put("rAlertId", "dsafafdsadsfafd");
		
		Random random = new Random();
//		for (int i = 0; i < 100; i++) {
			helper.pushListMessage(clientList, "TestTitle1123-0020" + random.nextInt(10000), "Testcontent1123-0020" + random.nextInt(10000), params);
			try {
				TimeUnit.SECONDS.sleep(30);
			} catch (Exception e) {
				log.error(null, e);
			}
//		}
	}

	/** 
	 * 功能描述: 针对指定的客户端进行推送，每天推送的次数上限为200万  
	 * <p>
	 * @param appClientList
	 * @param title
	 * @param content
	 * @param params
	 * @return taskId
	 */
	public String pushListMessage(List<AppClientInfo> appClientList, String title, String content, Map<String, Object> params) {
		if (CollectionUtils.isEmpty(appClientList)) {
			return null;
		}
		List<Target> targets = appClientList.stream().map(clientInfo -> {
			Target target = new Target();
			target.setAppId(appId);
			target.setClientId(clientInfo.getClientId());
			return target;
		}).collect(Collectors.toList());
		
		IGtPush push = new IGtPush(url, appKey, masterSecret);
		ListMessage message = getListMessage(getStartActivityTemplate(title, content, params));
		String taskId = push.getContentId(message);
        IPushResult ret = push.pushMessageToList(taskId, targets);
        log.info(ret.getResponse().toString());
        return taskId;
	}
	
    /** 
     * 功能描述: 针对所有APP进行消息推送, 注意：针对所有APP用户推送时，一天最多只能推送100次
     * <p>
     * @param title
     * @param content
     * @param params
     * @return taskId
     */
    public void pushAppMessage(String title, String content, Map<String, Object> params) {
    	IGtPush push = new IGtPush(url, appKey, masterSecret);
        // 定义"AppMessage"类型消息对象,设置推送消息有效期等推送参数
        AppMessage message = new AppMessage();
        message.setAppIdList(Collections.singletonList(appId));
//        message.setData(getNotificationTemplate(title, content, params));
        message.setData(getStartActivityTemplate(title, content, params));
        message.setOffline(true);
        message.setOfflineExpireTime(TimeUnit.HOURS.toMillis(12));  // 时间单位为毫秒
        // 厂商下发策略；1: 个推通道优先，在线经个推通道下发，离线经厂商下发(默认);2: 在离线只经厂商下发;3: 在离线只经个推通道下发;4: 优先经厂商下发，失败后经个推通道下发;
        message.setStrategyJson("{\"default\":4,\"ios\":4,\"hw\":4}");

        // STEP6：执行推送
        IPushResult ret = push.pushMessageToApp(message);
        log.info(ret.getResponse().toString());
    }
    
    private ListMessage getListMessage(AbstractTemplate template) {
        ListMessage message = new ListMessage();
        message.setData(template);
        // 设置消息离线，并设置离线时间
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(TimeUnit.HOURS.toMillis(12));
        message.setPushNetWorkType(0);  //判断客户端是否wifi环境下推送。1为仅在wifi环境下推送，0为不限制网络环境，默认不限
        // 厂商下发策略；1: 个推通道优先，在线经个推通道下发，离线经厂商下发(默认);2: 在离线只经厂商下发;3: 在离线只经个推通道下发;4: 优先经厂商下发，失败后经个推通道下发;
        message.setStrategyJson("{\"default\":4,\"ios\":4,\"st\":4}");
        return message;
    }
    
    
	// 点击通知, 打开（自身）应用内任意页面
    private StartActivityTemplate getStartActivityTemplate(String title, String content, Map<String, Object> params) {
		StartActivityTemplate template = new StartActivityTemplate();
		// 设置APPID与APPKEY
		template.setAppId(appId);
		template.setAppkey(appKey);
		// 设置展示样式
		template.setStyle(getStyle0(title, content));

		String intentFormat = "intent:#Intent;action=android.intent.action.oppopush;launchFlags=0x14000000;"
				+ "component=uni.UNI96D0D24/io.dcloud.PandoraEntry;"
				+ "B.UP-OL-SU=true;S.title=%s;S.content=%s;S.payload=%s;end";
		params = params == null ? new HashMap<>() : params;
		String intent = String.format(intentFormat, title, content, JsonUtil.toJacksonJson(params));
		template.setIntent(intent); // 最大长度限制为1000
		// template.setNotifyid(123); // 在消息推送的时候设置notifyid。如果需要覆盖此条消息，则下次使用相同的notifyid发一条新的消息。客户端sdk会根据notifyid进行覆盖。
		// template.setSmsInfo(PushSmsInfo.getSmsInfo()); //短信补量推送
		// template.setDuration("2019-07-09 11:40:00", "2019-07-09 12:24:00");
		// // 设置定时展示时间，安卓机型可用
		template.setAPNInfo(getAPNPayload(title, content, params)); 
		return template;
	}
    
    /**
     * 点击通知打开应用模板, 在通知栏显示一条含图标、标题等的通知，用户点击后激活您的应用。
     * 通知模板(点击后续行为: 支持打开应用、发送透传内容、打开应用同时接收到透传 这三种行为)
     * @return
    private NotificationTemplate getNotificationTemplate(String title, String content, Map<String, Object> params) {
        NotificationTemplate template = new NotificationTemplate();
        // 设置APPID与APPKEY
        template.setAppId(appId);
        template.setAppkey(appKey);
        //设置展示样式
        template.setStyle(getStyle0(title, content));
        template.setTransmissionType(1);  // 透传消息设置，收到消息是否立即启动应用： 1为立即启动，2则广播等待客户端自启动  
        template.setTransmissionContent(JSONUtils.toJSONString(params));
//      template.setSmsInfo(PushSmsInfo.getSmsInfo()); //短信补量推送
//      template.setDuration("2019-07-09 11:40:00", "2019-07-09 12:24:00");  // 设置定时展示时间，安卓机型可用
//      template.setNotifyid(123); // 在消息推送的时候设置notifyid。如果需要覆盖此条消息，则下次使用相同的notifyid发一条新的消息。客户端sdk会根据notifyid进行覆盖。
        template.setAPNInfo(getAPNPayload(title, content, params));  // ios消息推送
        return template;
    }
     */
    
    private AbstractNotifyStyle getStyle0(String title, String content) {
        Style0 style = new Style0();
        // 设置通知栏标题与内容
        style.setTitle(title);
        style.setText(content);
//        // 配置通知栏图标
//        style.setLogo("push.png"); //配置通知栏图标，需要在客户端开发时嵌入，默认为push.png
//        // 配置通知栏网络图标
//        style.setLogoUrl("");
        
        // 设置通知是否响铃，震动，或者可清除
        style.setRing(true);
        style.setVibrate(true);
        style.setClearable(true);
        style.setChannel("cdn");
        style.setChannelName("cdn");
        style.setChannelLevel(3);  // 设置通知渠道重要性    3：有声音，有震动，锁屏和通知栏中都显示，通知唤醒屏幕。（推荐）
        return style;
    }
    
    private APNPayload getAPNPayload(String title, String content, Map<String, Object> params) {
        APNPayload payload = new APNPayload();
        //在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，显示指定数字
//        payload.setAutoBadge("+1");
//        payload.setContentAvailable(1);
        //ios 12.0 以上可以使用 Dictionary 类型的 sound
        payload.setSound("default");
//        payload.setCategory("$由客户端定义");
        for (Map.Entry<String, Object> entry : params.entrySet()) {
        	payload.addCustomMsg(entry.getKey(), JsonUtil.toJacksonJson(entry.getValue()));
        }
        //简单模式APNPayload.SimpleMsg
        payload.setAlertMsg(getDictionaryAlertMsg(title, content));  //字典模式使用APNPayload.DictionaryAlertMsg

        //设置语音播报类型，int类型，0.不可用 1.播放body 2.播放自定义文本
        payload.setVoicePlayType(0);
        // 设置语音播报内容，String类型，非必须参数，用户自定义播放内容，仅在voicePlayMessage=2时生效
        // 注：当"定义类型"=2, "定义内容"为空时则忽略不播放
        // payload.setVoicePlayMessage("定义内容");

        // 添加多媒体资源
//        payload.addMultiMedia(new MultiMedia()
//                .setResType(MultiMedia.MediaType.pic)
//                .setResUrl("资源文件地址")
//                .setOnlyWifi(true));

        return payload;
    }
    
    private APNPayload.DictionaryAlertMsg getDictionaryAlertMsg(String title, String content) {
        APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
        alertMsg.setBody(content);
//        alertMsg.setActionLocKey("显示关闭和查看两个按钮的消息");
//        alertMsg.setLocKey("loc-key");
//        alertMsg.addLocArg("loc-ary1");
//        alertMsg.addLocArg("loc-ary2");
//        alertMsg.setLaunchImage("调用已经在应用程序中绑定的图形文件名");
        // iOS8.2以上版本支持
        alertMsg.setTitle(title);
//        alertMsg.setTitleLocKey("自定义通知标题");
//        alertMsg.addTitleLocArg("自定义通知标题组1");
//        alertMsg.addTitleLocArg("自定义通知标题组2");
//
//        alertMsg.setSubtitle("sub-title");
//        alertMsg.setSubtitleLocKey("sub-loc-key1");
//        alertMsg.addSubtitleLocArgs("sub-loc-arg1");
//        alertMsg.addSubtitleLocArgs("sub-loc-arg2");
        return alertMsg;
    }
}


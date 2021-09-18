package com.migu.tsg.microservice.atomicservice.composite.controller.aspect;

import com.aspire.mirror.composite.service.desk.bpm.payload.DeskLogs;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.DeskLogsAnnotation;
import com.migu.tsg.microservice.atomicservice.composite.controller.desk.DeskStaffController;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.BeanUtils;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.JsonUtil2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 系统日志new切面
 *
 * @company 卓望技术有限公司
 * @author menglinjie
 * @date 2020年11月16日
 */
@Aspect
@Component
public class DeskLogsAspect {
	
	@Value("${spring.application.name}")
	public String moduleType;

	@Resource
	DeskStaffController deskStaffController;

	@Around("execution(* *..*Controller.*(..)) && @annotation(com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.DeskLogsAnnotation)")
	public Object sysLogs(ProceedingJoinPoint joinPoint) throws Throwable{
		Class<?> targetClass = joinPoint.getTarget().getClass();
		String methodName = joinPoint.getSignature().getName();
		
		Method[] methods = targetClass.getMethods();
		// 当前切中的方法
		Method method = null;
		for (int i = 0; i < methods.length; i++){
			if (methods[i].getName() == methodName){
				method = methods[i];
				break;
			}
		}
		DeskLogsAnnotation deskLogsAnnotation = null;
		if(method != null){
			deskLogsAnnotation = method.getAnnotation(DeskLogsAnnotation.class);
		}
		// 注解SysLogsAnnotation的OpType作为具体操作的状态,默认为View
		String OpText = null;
		String OpType = null;
		String AppType = null;
		if(deskLogsAnnotation != null){
			OpText = deskLogsAnnotation.value();
			OpType = deskLogsAnnotation.OpType();
			AppType = deskLogsAnnotation.AppType();
		}
		// 执行方法前
		Object proceed = joinPoint.proceed();
		// 执行方法后
	    //访问目标方法的参数：
        Object[] args = joinPoint.getArgs();
		
		// 当前切中的方法
//		HttpServletRequest request = this.getRequest();
//		if(request==null) {
//			return proceed;
//		}
//		String reqUrl = request.getRequestURI();
		try {

			StringBuffer sb = new StringBuffer();
			if( args !=null && args.length>0){
				for (Object object : args) {
					try {
						String json = this.toJson(object);
						sb.append(json);
					} catch (Exception e) {
						sb.append(object.toString());
					}
				}
			}
			DeskLogs req = new DeskLogs();
//			req.setSubUser(executor);
//			req.setSip(WebUtil.getIpAddr(request));
//			req.setAppModule(moduleType);
			req.setOpType(OpType);
			req.setOpText(OpText);
//			req.setAppType(AppType);
			deskStaffController.saveDeskLogs(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return proceed;
	}

	public HttpServletRequest getRequest() {
		RequestAttributes requestAttributes = null;
		try{
			requestAttributes = RequestContextHolder.currentRequestAttributes();
		}catch (IllegalStateException e){
			return null;
		}
		return ((ServletRequestAttributes) requestAttributes).getRequest();
	}

	/**
	 * 对象转字符串
	 * @param obj	对象
	 * @return		json格式字符串
	 * @throws IOException
	 */
	public String toJson(Object obj) throws IOException{
		 ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(obj);
	}

	/**
	 * 获取当前用户的名称
	 * @return
	 */
	public  String getCurrentUserFullname(){
		JsonNode jsonNode = getUserThreadLocal().get("fullname");
		if(BeanUtils.isEmpty(jsonNode) || !jsonNode.isTextual()){
			return null;
		}
		return jsonNode.asText();
	}

	public JsonNode getUserThreadLocal() {
		ThreadLocal<JsonNode> userThreadLocal = new ThreadLocal<JsonNode>();
		JsonNode jsonNode = userThreadLocal.get();
		if(BeanUtils.isEmpty(jsonNode)) {
			return JsonUtil2.getMapper().createObjectNode();
		}
		return jsonNode;
	}

	/**
	 * 判断字符串非空
	 *
	 * @param str
	 * @return
	 */
	public boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 判断字符串是否为空
	 *
	 * @param str
	 * @return
	 */
	public boolean isEmpty(String str) {
		if (str == null){
			return true;
		}
        return str.trim().equals("");
    }

	/**
	 * 获取当前用户的账号信息
	 * @return
	 */
	public  String getCurrentUsername(){
		JsonNode jsonNode = getUserThreadLocal().get("account");
		if(BeanUtils.isEmpty(jsonNode) || !jsonNode.isTextual()) {
			return null;
		}
		return jsonNode.asText();
	}
}

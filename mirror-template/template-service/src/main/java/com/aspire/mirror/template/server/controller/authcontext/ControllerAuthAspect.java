package com.aspire.mirror.template.server.controller.authcontext;

import com.aspire.mirror.template.server.anno.ResFilterKeysValid;
import com.aspire.mirror.template.server.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * Controller方法调用前置代理：  1. 检测用户信息是否完整        2. 为请求填充默认的resourceType和action
 * Project Name:composite-service2
 * File Name:ControllerAuthAspect.java
 * Package Name:com.migu.tsg.microservice.atomicservice.composite.controller.aspect
 * ClassName: ResActionAnnoAspect <br/>
 * date: 2017年8月30日 上午9:25:32 <br/>
 * Controller方法调用前置代理：1. 检测用户信息是否完整 2. 为请求填充默认的resourceType和action
 *
 * @author pengguihua
 * @version
 * @since JDK 1.6
 */
@Slf4j
@Aspect
@Component
@Order(100)
public class ControllerAuthAspect {

    /**
     * 切入点： 定位到 controller中的方法
     */
    @Pointcut("execution(public * com.aspire.mirror.template.server.controller..*Controller.*(..))")
    private void controllerMethod() {
    }

    /**
     * 1. 在Controller方法调用前，检查用户信息完整  <br/>
     * 2.
     * 如果HTTP请求中未携带action和resourceType，则读取controller方法上的ResAction注解，提取action和resourceType参数
     * <br/>
     *
     * 作者： pengguihua
     *
     * @param joinPoint
     * @throws Exception
     */
    @Before("controllerMethod()")
    public void doBefore(final JoinPoint joinPoint) throws Exception {
        ServletRequestAttributes reqAttrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        this.initCurrRequestUserAuth(joinPoint, reqAttrs.getRequest());
    }

    /**
    * 初始化当前请求userAuth .<br/>
    *
    * 作者： pengguihua
    * @param joinPoint
    * @param httpReq
    */
    private void initCurrRequestUserAuth(final JoinPoint joinPoint, final HttpServletRequest httpReq) {
        // 从当前请求解析出 UserAuth
        final RequestAuthContext authCtx = this.retrieveUserAuthInfo(joinPoint, httpReq);

        // 验证用户认证信息是否存在
        if (authCtx.getUser() == null || !authCtx.getUser().selfCheck()) {
            log.warn("There is no head user auth info.");
            return;
        }

        checkResFilterConfigKeys(joinPoint, authCtx);

        // 缓存AUTH上下文
        RequestAuthContext.setRequestAuthContext(authCtx);
    }

    /**
     * 功能描述: 验证数据过滤指定的键的值是否存在非空，如果所有键都为空，抛出异常
     * <p>
     * @param joinPoint
     */
    private void checkResFilterConfigKeys(final JoinPoint joinPoint, final RequestAuthContext authCtx) {
    	RequestAuthContext.RequestHeadUser reqUser = authCtx.getUser();
    	// 管理员不验证
    	if (reqUser.isAdmin() || reqUser.isSuperUser()) {
    		return;
    	}

    	// 从@ResFilterKeysValid注解中，提取验证的数据权限key, 验证相关的配置是否存在
    	final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Method method = signature.getMethod();
        final ResFilterKeysValid anno = method.getAnnotation(ResFilterKeysValid.class);
        if (anno == null) {
        	return;
        }
        String[] notAllEmptyKeys = anno.notAllEmpty();
        if (ArrayUtils.isEmpty(notAllEmptyKeys)) {
        	return;
        }

        Map<String, List<String>> resFilterCfgMap = authCtx.getUser().getResFilterConfig();
        String tip = "There are no res filter configs with keys: " + StringUtils.join(notAllEmptyKeys, ',');
        if (MapUtils.isEmpty(resFilterCfgMap)) {
        	throw new RuntimeException(tip);
        }
        boolean anyExists = Arrays.stream(notAllEmptyKeys).anyMatch(resFilterKey ->
        	resFilterCfgMap.containsKey(resFilterKey) && CollectionUtils.isNotEmpty(resFilterCfgMap.get(resFilterKey)));
        if (!anyExists) {
        	throw new RuntimeException(tip);
        }
    }

    /**
    *  从请求中解析UserAuth.<br/>
    *
    * 作者： pengguihua
    * @param httpReq
    * @return
    */
    private RequestAuthContext retrieveUserAuthInfo(final JoinPoint joinPoint, final HttpServletRequest httpReq) {
        final String namespace = httpReq.getHeader("head_orgAccount");
        final String username = httpReq.getHeader("head_userName");
        final String isAdmin = httpReq.getHeader("head_isAdmin");
        final String isSuperUser = httpReq.getHeader("head_isSuperUser");
        final String resFilterConfigBase64 = httpReq.getHeader("head_resFilter");

        boolean isAdminBool = isAdmin == null ? false : Boolean.valueOf(isAdmin);
        boolean isSuperUserBool = isSuperUser == null ? false : Boolean.valueOf(isSuperUser);
        Map<String, List<String>> resFilterConfig = null;

        if (StringUtils.isNotBlank(resFilterConfigBase64)) {
			try {
				String resFilterConfigJson = new String(Base64Utils.decodeFromUrlSafeString(resFilterConfigBase64), "UTF-8");
				TypeReference<Map<String, List<String>>> typeRef = new TypeReference<Map<String, List<String>>>() {};
				resFilterConfig = JsonUtil.jacksonConvert(resFilterConfigJson, typeRef);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
        }

        //根据报文头信息生成用户信息，不需要转义
        RequestAuthContext.RequestHeadUser user = new RequestAuthContext.RequestHeadUser(namespace, username, isAdminBool, isSuperUserBool, resFilterConfig);
        final RequestAuthContext authContext = new RequestAuthContext();
        authContext.setUser(user);
        return authContext;
    }
}

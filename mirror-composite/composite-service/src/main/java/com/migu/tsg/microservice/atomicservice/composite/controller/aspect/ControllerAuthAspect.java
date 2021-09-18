package com.migu.tsg.microservice.atomicservice.composite.controller.aspect;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.Authentication;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext.RequestHeadUser;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestConstraintEnum;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.exception.BadUserAuthException;

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
@Aspect
@Component
@Order(100)
public class ControllerAuthAspect {
//	@Autowired
//    private ResourceAuthHelper resAuthHelper;

    /**
     * 切入点： 定位到 controller中的方法
     */
    @Pointcut("execution(public * com.migu.tsg.microservice.atomicservice.composite.controller..*Controller.*(..))")
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
        Object firstFlag = reqAttrs.getAttribute(RequestAuthContext.REQUEST_FIRST_FLAG_KEY, ServletRequestAttributes.SCOPE_REQUEST);
        this.initCurrRequestUserAuth(joinPoint, reqAttrs.getRequest(), firstFlag);
        // 设置RequestAuthContext.REQUEST_FIRST_FLAG_KEY, 解决controller嵌套调用时， @ResAction解析覆盖的问题
        reqAttrs.setAttribute(RequestAuthContext.REQUEST_FIRST_FLAG_KEY, Boolean.TRUE, ServletRequestAttributes.SCOPE_REQUEST);
    }
    

    /**
    * 初始化当前请求userAuth .<br/>
    *
    * 作者： pengguihua
    * @param joinPoint
    * @param httpReq
    */
    private void initCurrRequestUserAuth(final JoinPoint joinPoint, final HttpServletRequest httpReq, final Object firstFlag) {
    	RequestAuthContext authCtx = null;
    	
    	if (firstFlag != null) {
    		authCtx = RequestAuthContext.currentRequestAuthContext();
    	} 
    	else {
    		authCtx = this.retrieveUserAuthInfo(joinPoint, httpReq);
    		// 缓存AUTH上下文
    		RequestAuthContext.setRequestAuthContext(authCtx);
    		
//    		// 如果当前auth上下文已经有 resourceType和action，则忽略解析方法的注解ResAction
//    		if (StringUtils.isNotBlank(authCtx.getResType()) && StringUtils.isNotBlank(authCtx.getRawAction())) {
//    			return;
//    		}
    	}

    	// 验证用户认证信息是否存在
        if (authCtx.getUser() == null || !authCtx.getUser().selfCheck()) {
            throw new BadUserAuthException();
        }

        // 解析方法的注解ResAction,填充 resourceType和action
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Method method = signature.getMethod();
        final ResAction anno = method.getAnnotation(ResAction.class);
        if (anno != null) {
        	authCtx.setResType(anno.resType());
        	authCtx.setRawAction(anno.action());
        	if (anno.loadResFilter()) {		// 在controller嵌套调用时，只要有@ResAction注解中配置了需要加载资源, 则该标记适用于所有controller
        		authCtx.setLoadResFilter(anno.loadResFilter());
        	}
        }
    }
    
    /** 
     * 功能描述: 动作鉴权  
     * <p>
     * @param authCtx
    private void checkResAction(final RequestAuthContext authCtx) {
    	if (StringUtils.isNotBlank(authCtx.getResAction())) {
    		resAuthHelper.resourceActionVerify(
    				authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
    	}
    }
     */

    
    /**
    *  从请求中解析UserAuth.<br/>
    *
    * 作者： pengguihua
    * @param httpReq
    * @return
    */
    private RequestAuthContext retrieveUserAuthInfo(final JoinPoint joinPoint, final HttpServletRequest httpReq) {
        final RequestAuthContext authContext = new RequestAuthContext();
        Signature signature = joinPoint.getSignature();    
        MethodSignature methodSignature = (MethodSignature)signature;    
        Method targetMethod = methodSignature.getMethod(); 
        Authentication authAnno = AnnotationUtils.findAnnotation(targetMethod, Authentication.class);
        //通过注解配置不需要验证报文头用户信息的匿名登录
        RequestHeadUser user = null;
        if (authAnno != null && authAnno.anonymous()) {
            final String anonymousName = authAnno.anonymousName();
            user = new RequestHeadUser(anonymousName, anonymousName, false, false);
        } else {
            final String namespace = httpReq.getHeader("head_orgAccount");
            final String username = httpReq.getHeader("head_userName");
            final String isAdmin = httpReq.getHeader("head_isAdmin");
            final String isSuperUser = httpReq.getHeader("head_isSuperUser");
            boolean isAdminBool = isAdmin == null ? false : Boolean.valueOf(isAdmin);
            boolean isSuperUserBool = isSuperUser == null ? false : Boolean.valueOf(isSuperUser);
            //根据报文头信息生成用户信息，不需要转义
            user = new RequestHeadUser(namespace, username, isAdminBool, isSuperUserBool);
        }
        authContext.setUser(user);

        // 提取请求中的约束
        for (RequestConstraintEnum item : RequestConstraintEnum.values()) {
            String constaintVal = httpReq.getParameter(item.name());
            if (StringUtils.isNotBlank(constaintVal)) {
                authContext.addConstraint(item.name(), constaintVal);
            }
        }

        // 提取请求中的action和resoureType
        final String resourceType = httpReq.getParameter("resource_type");
        final String action = httpReq.getParameter("action");
        if (StringUtils.isNotBlank(resourceType)) {
            authContext.setResType(resourceType);
        }
        if (StringUtils.isNotBlank(action)) {
            authContext.setRawAction(action);
        }

        // 把请求的参数都保存在Map中
        authContext.cacheRequestParameterMap(httpReq.getParameterMap());
        return authContext;
    }
}

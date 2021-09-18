package com.migu.tsg.microservice.atomicservice.composite.controller.aspect;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.IgnoreAopLog;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext.RequestHeadUser;
import com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LogCodeDefine;
import com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LogCodeHelper;
import com.migu.tsg.microservice.atomicservice.composite.exception.BadRequestUrlException;
import com.migu.tsg.microservice.atomicservice.composite.exception.ResultErrorEnum;

/**
 * Controller方法调用日志记录切面 Project Name:composite-service 
 * File Name: RequestLogAspect.java Package
 * Name:com.migu.tsg.microservice.atomicservice.composite.controller.aspect
 * ClassName: ControllerLogAspect <br/>
 * date: 2017年9月1日 下午11:16:45 <br/>
 * Controller方法调用日志记录切面
 *
 * @author pengguihua
 * @version
 * @since JDK 1.6
 */
//@Aspect
//@Component
//@Order(200)
public class ControllerLogAspect {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerLogAspect.class);

    /**
     * 编码解码字符集
     */
    private static final String CHARACTER_ENCODING = "UTF-8";
    
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * 切入点： 定位到 controller中的方法
     */
    @Pointcut("execution(public * com.migu.tsg.microservice.atomicservice.composite.controller..*Controller.*(..))")
    private void controllerMethod() {

    }
    
    /**
    * 解析controller类、接口方法上的注解的日志码值.<br/>
    *
    * 作者： pengguihua
    * @param point
    */
    private void resolveLogCodeDefine(final JoinPoint point) {
        // 先清空
        LogCodeHelper.clearCurrLogCode();
        
        Signature signature = point.getSignature();    
        MethodSignature methodSignature = (MethodSignature) signature;    
        Method method = methodSignature.getMethod(); 
        Class<?> clazz = method.getDeclaringClass();
        
        LogCodeDefine clazzLogCode = clazz.getAnnotation(LogCodeDefine.class);
        if (clazzLogCode != null) {
            LogCodeHelper.appendLogCode(clazzLogCode.value());
        }
        
        LogCodeDefine methodLogCode = method.getAnnotation(LogCodeDefine.class);
        if (methodLogCode != null) {
            LogCodeHelper.appendLogCode(methodLogCode.value());
        }
    }

    /**
     * AOP Before
     *
     * @param joinPoint
     * @throws Exception
     */
    @Before("controllerMethod()")
    public void doBefore(final JoinPoint joinPoint) throws Exception {
        // 处理日志合成编码注解
        resolveLogCodeDefine(joinPoint);
        
        if (checkIgnoreLog(joinPoint)) {
            return;
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        final String decode = URLDecoder.decode(request.getRequestURI(), CHARACTER_ENCODING);
        // 若URI中含有空格,则不予处理,直接抛请求地址无效异常
        if (Pattern.compile("\\s+").matcher(decode).find()) {
            throw new BadRequestUrlException(ResultErrorEnum.BAD_REQUEST_URL);
        }
        
        if (LOGGER.isInfoEnabled()) {
            String reqURL = request.getMethod() + " " + request.getRequestURI();
            String remoteIP = request.getRemoteAddr();
            String methodStack = joinPoint.getSignature().getDeclaringTypeName() 
                    + "." + joinPoint.getSignature().getName();
            Object[] args = joinPoint.getArgs();
            List<String> argStrList = new ArrayList<>();
            //截取请求中太长的参数，主要针对图片
            if (null != args && args.length > 0) {
                for (Object object : args) {
                    if (null == object) {
                        argStrList.add(null);
                        continue;
                    }
                    String str = object.toString();
                    int num = 1024;
                    if (str.length() > num) {
                        str = str.substring(0, 1024) + "...";
                    }
                    argStrList.add(str);
                }
            }
            RequestHeadUser currUser = RequestAuthContext.currentRequestAuthContext().getUser();
            LOGGER.info("Composite Controller invoke begin: method = {}, args = {}, reqURL = {},"
            		+ " userInfo = {} from {}", 
                    methodStack, argStrList.toString(), reqURL, currUser, remoteIP);
        }
    }

    /**
     * 返回响应值之前执行该方法
     *
     * @param methodReturn
     */
    @AfterReturning(returning = "methodReturn", pointcut = "controllerMethod()")
    public void doAfterReturning(JoinPoint joinPoint, final Object methodReturn) {
        if (!LOGGER.isInfoEnabled() || checkIgnoreLog(joinPoint)) {
            return;
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        
        String reqURL = request.getMethod() + " " + request.getRequestURL().toString();
        String remoteIP = request.getRemoteAddr();
        String methodStack = joinPoint.getSignature().getDeclaringTypeName() 
                           + "." + joinPoint.getSignature().getName();
        RequestHeadUser currUser = RequestAuthContext.currentRequestAuthContext().getUser();
        String response = "";
        if (methodReturn != null) {
            if (methodReturn instanceof CharSequence || methodReturn instanceof Number
                    || methodReturn instanceof Boolean) {
                response = String.valueOf(methodReturn);
            } else {
                try {
                    response = mapper.writeValueAsString(methodReturn);
                } catch (JsonProcessingException e) {
                    response = String.valueOf(methodReturn);
                }
            }
        }
        //如果返回太长，截取前512个字符
        if (StringUtils.isNotEmpty(response) && response.length() > 1024) {
            response = response.substring(0,1024) + "...";
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Composite Controller invoke return: response = {},"
                       + " method = {}, reqURL = {}, userInfo = {} from {}", 
                        response, methodStack, reqURL, currUser, remoteIP);
        } else {
            LOGGER.info("Composite Controller invoke return: "
                    + " method = {}, reqURL = {}, userInfo = {} from {}", 
                    methodStack, reqURL, currUser, remoteIP);
        }
    }
    
    /**
     * 
     * checkIgnoreLog:(通过注解配置不需要打印登陆日志). <br/>
     * 作者： baiwp
     * @param joinPoint
     * @return
     */
    boolean checkIgnoreLog (JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();    
        MethodSignature methodSignature = (MethodSignature)signature;    
        Method targetMethod = methodSignature.getMethod(); 
        IgnoreAopLog authAnno = AnnotationUtils.findAnnotation(targetMethod, IgnoreAopLog.class);
        //通过注解配置不需要打印登陆日志
        return authAnno != null && authAnno.isIgnore();
    }
}

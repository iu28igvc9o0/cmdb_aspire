package com.aspire.mirror.inspection.server.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 项目名称:  mirror平台-巡检
 * 包:       com.aspire.mirror.inspection.server.config
 * 类名称:    ControllerAspect.java
 * 类描述:
 * 创建人:    JinSu
 * 创建时间:  2018/07/27 16:03
 * 版本:      v1.0
 */
@Aspect
@Component
public class ControllerAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAspect.class);

    private ThreadLocal<Long> startTime = new ThreadLocal<Long>();

    static final String HR = System.getProperty("line.separator");

    /**
     * webLog
     */
    @Pointcut("execution(* com.aspire.mirror..*Controller.*(..))")
    public void webLog() {
    }

    /**
     * 请求controller前处理
     * @param joinPoint joinPoint
     * @throws Throwable Throwable
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        startTime.set(System.currentTimeMillis());
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        String url = request.getRequestURL().toString();
        LOGGER.info("{}URL : {} ---------request controller start----------   " + "{}HTTP_METHOD : {}  "
                + "{}CLASS_METHOD : {}  " + "{}IP : {}" + "{}ARGS : {}"
                + "{}URL : {} ---------request controller end---------- ", HR, url, HR, request.getMethod(), HR,
                joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(), HR,
                request.getRemoteAddr(), HR, Arrays.toString(joinPoint.getArgs()), HR, url);
    }

    /**
     * 请求返回前处理
     * @param ret ret
     * @throws Throwable
     */
    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();

        LOGGER.info("{}URL : {} ---------response controller start----------   " + "{}RESPONSE : {}  "
                + "{}URL : {} ---------response controller end---------- ", HR, url, HR, ret, HR, url);
        startTime.remove();
    }
}
package com.migu.tsg.microservice.atomicservice.ldap.aspect;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

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

import com.migu.tsg.microservice.atomicservice.ldap.annotation.ResultCode;
import com.migu.tsg.microservice.atomicservice.ldap.enums.ResultErrorEnum;
import com.migu.tsg.microservice.atomicservice.ldap.exception.BadRequestUrlException;

import static com.migu.tsg.microservice.atomicservice.ldap.handle.ExceptionHandle.RESULT_CODE;

/**
 * 项目名称: ldap-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.ldap.aspect <br>
 * 类名称: HttpRequestAspect.java <br>
 * 类描述: AOP处理请求路径有效性 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年7月28日下午3:06:09 <br>
 * 版本: v1.0
 */
@Aspect
@Component
public class HttpRequestAspect {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestAspect.class);

    /**
     * 编码解码字符集
     */
    private static final String CHARACTER_ENCODING = "UTF-8";

    private Class<ResultCode> clazz = ResultCode.class;

    /**
     * 切入点
     */
    @Pointcut("execution(public * com.migu.tsg.microservice.atomicservice.ldap.controller.*Controller.*(..))")
    public void point() {
    }

    /**
     * AOP Before
     * @param joinPoint 切入点对象
     * @throws Exception 异常
     */
    @Before("point()")
    public void doBefore(final JoinPoint joinPoint) throws Exception {
        LOGGER.debug("***** Welcome Access Ldap Server *****");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Method[] methods = joinPoint.getTarget().getClass().getMethods();
        String methodName = joinPoint.getSignature().getName();
        String resultCode = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                ResultCode annotation = method.getAnnotation(clazz);
                if (annotation != null) {
                    resultCode = annotation.value();
                }
                break;
            }
        }
        if (!resultCode.isEmpty()) {
            request.setAttribute(RESULT_CODE, resultCode);
        }
        String decode = URLDecoder.decode(request.getRequestURI(), CHARACTER_ENCODING);
        // 请求URL无效,不能含有任何不可见字符,包括空格、制表符、换页符等等
        // 若URI中含有空格,则不予处理,直接抛请求地址无效异常
        if (Pattern.compile("\\s+").matcher(decode).find()) {
            throw new BadRequestUrlException(ResultErrorEnum.BAD_REQUEST_URL);
        }

        LOGGER.debug("***** Call Ldap Service Start *****");
        // url
        LOGGER.debug("Url={}", request.getRequestURL());
        // method
        LOGGER.debug("Method={}", request.getMethod());
        // ip
        LOGGER.debug("Ip={}", request.getRemoteAddr());
        // 类方法
        LOGGER.debug("ClassMethod={}",
                joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        // 参数
        LOGGER.debug("Args={}", Arrays.asList(joinPoint.getArgs()));
    }

    /**
     * 返回响应值之前执行该方法
     * @param object 响应值
     */
    @AfterReturning(returning = "object", pointcut = "point()")
    public void doAfterReturning(final Object object) {
        if (object == null) {
            LOGGER.debug("response={}");
        } else {
            LOGGER.debug("response={}", object.toString());
        }
        LOGGER.debug("***** Call Ldap Service End *****");
    }
}

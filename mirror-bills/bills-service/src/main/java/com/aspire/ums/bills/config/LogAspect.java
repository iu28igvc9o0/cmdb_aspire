package com.aspire.ums.bills.config;

import com.aspire.ums.bills.log.payload.BillsLog;
import com.aspire.ums.bills.log.service.BillLogService;
import com.aspire.ums.bills.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: LogAspect
 * Author:   hangfang
 * Date:     2021/2/20
 * Description: 描述
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Aspect
@Slf4j
@Component
public class LogAspect {

    private SpelExpressionParser spelParser = new SpelExpressionParser();
    @Pointcut("execution(public * com.aspire.ums.bills.*.web.*Controller.*(..))" )
    private void controllerMethod() {
    }

    @Pointcut("execution(public * com.aspire.ums.bills.schedule.*Schedule.*(..))" )
    private void scheduleMethod() {
    }
    @Autowired
    private BillLogService billLogService;

    @AfterReturning("@annotation(com.aspire.ums.bills.config.BillLogAnnotation)")
    public void doAfter(JoinPoint joinPoint){
           BillsLog billsLog = new BillsLog();
        //将方法的参数名和参数值一一对应的放入上下文中
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        BillLogAnnotation operationAnnotation = method.getAnnotation(BillLogAnnotation.class);
        List<String> paramNameList = Arrays.asList(methodSignature.getParameterNames());
        List<Object> paramList = Arrays.asList(joinPoint.getArgs());
        EvaluationContext ctx = new StandardEvaluationContext();
        for (int i = 0; i < paramNameList.size(); i++) {
            ctx.setVariable(paramNameList.get(i), paramList.get(i));
        }
        String operateContent = operationAnnotation.content();
        billsLog.setOperateType(operationAnnotation.type());
        billsLog.setOperateOBJ( operationAnnotation.obj());
        billsLog.setOperateContent(spelParser.parseExpression(operateContent).getValue(ctx).toString());
        billLogService.saveBillLog(billsLog);
    }


    @Before("controllerMethod()")
    private RequestAuthContext getUserInfo() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes)ra;
        HttpServletRequest request = sra.getRequest();
        RequestAuthContext.RequestHeadUser headUser = new RequestAuthContext.RequestHeadUser();
        final RequestAuthContext authContext = new RequestAuthContext();
        headUser.setUsername(request.getHeader("head_userName"));
        headUser.setIp(request.getHeader("X-Real-IP"));
        if (StringUtils.isEmpty(headUser.getUsername())) {
            headUser.setUsername("系统管理员");
        }
        if (StringUtils.isEmpty(headUser.getIp())) {
            headUser.setIp("系统服务IP");
        }
        authContext.setUser(headUser);
        RequestAuthContext.setRequestAuthContext(authContext);
        return authContext;
    }

    @Before("scheduleMethod()")
    private RequestAuthContext getScheduleUserInfo() {
        RequestAuthContext.RequestHeadUser headUser = new RequestAuthContext.RequestHeadUser();
        final RequestAuthContext authContext = new RequestAuthContext();
        headUser.setUsername("系统管理员");
        headUser.setIp("系统服务IP");
        authContext.setUser(headUser);
        RequestAuthContext.setRequestAuthContext(authContext);
        return authContext;
    }
}

package com.aspire.mirror.alert.server.aspect;

import com.aspire.mirror.alert.server.annotation.ServiceLogAnnotation;
import com.aspire.mirror.alert.server.dao.operateLog.AlertOperateLogMapper;
import com.aspire.mirror.alert.server.dao.operateLog.po.AlertOperateLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Aspect
public class LogAspect {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private AlertOperateLogMapper alertOperateLogMapper;

    /*
    *  定义切入点
    * */
    @Pointcut("@annotation(com.aspire.mirror.alert.server.annotation.ServiceLogAnnotation) ")
    public void entryPoint(){}

    @Around("entryPoint()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = point.proceed();
        try {
            // 在连接点后面处理日志
            handleLogger(point);
        } catch (Exception e){
            log.error("日志记录异常", e);
        }
        return result;
    }

    public void handleLogger(ProceedingJoinPoint point) throws Exception {
        Signature sig = point.getSignature();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
           throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        // 获取Class
        Object target = point.getTarget();
        // 获取具体Method
        Method currentMethod = target.getClass().getMethod(msig.getName(),msig.getParameterTypes());
        // 获取注解中的参数
        ServiceLogAnnotation serviceLogAnnotation = currentMethod.getAnnotation(ServiceLogAnnotation.class);
        String id = serviceLogAnnotation.id();
        // 对Id进行逗号分割
        String[] ids = id.split(",");
        List<AlertOperateLog> alertOperateLogs = new ArrayList<>();
        for(String item: ids){
            AlertOperateLog alertOperateLog = new AlertOperateLog();
            alertOperateLog.setOperateContent(serviceLogAnnotation.content());
            alertOperateLog.setOperateModel(serviceLogAnnotation.operateModel());
            alertOperateLog.setOperateModelDesc(serviceLogAnnotation.operateModelDesc());
            alertOperateLog.setOperater(serviceLogAnnotation.operator());
            alertOperateLog.setOperateType(serviceLogAnnotation.operateType());
            alertOperateLog.setOperateTypeDesc(serviceLogAnnotation.operateTypeDesc());
            alertOperateLog.setRelationId(item);
            alertOperateLog.setOperateTime(new Date());
            alertOperateLog.setRemark(serviceLogAnnotation.remark());
            alertOperateLogs.add(alertOperateLog);
        }
        alertOperateLogMapper.insertBatch(alertOperateLogs);
    }
}

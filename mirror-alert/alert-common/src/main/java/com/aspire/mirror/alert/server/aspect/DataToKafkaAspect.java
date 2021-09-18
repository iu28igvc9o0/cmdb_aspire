package com.aspire.mirror.alert.server.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.alert.server.annotation.DataToKafka;
import com.aspire.mirror.alert.server.util.StringUtils;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

@Component
@Aspect
@Slf4j
public class DataToKafkaAspect {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private String TOPIC = "topic_alert_datas";

    /*
     *  定义切入点
     * */
    @Pointcut("@annotation(com.aspire.mirror.alert.server.annotation.DataToKafka) ")
    public void annotationPoint(){}

    @Async
    @AfterReturning("annotationPoint()")
    public void around(JoinPoint point){

        try {
            Signature sig = point.getSignature();
            if (!(sig instanceof MethodSignature)) {
                return;
            }
            MethodSignature msig = (MethodSignature) sig;
            // 获取Class
            Object target = point.getTarget();
            // 获取具体Method
            Method currentMethod = target.getClass().getMethod(msig.getName(),msig.getParameterTypes());
            // 获取注解中的参数
            DataToKafka dataToKafka = currentMethod.getAnnotation(DataToKafka.class);
            String index = dataToKafka.index();
            boolean allArgs = dataToKafka.allArgs();
            if (!StringUtils.isEmpty(index)) {
                Object[] args = point.getArgs();
                // 如果参数为空，则不处理
                if (args == null || args.length == 0) {
                    return;
                }

                // 发送参数到kafka
                if (allArgs) {

                    String argsStr = JSON.toJSONString(args);
                    Map<String, Object> map = Maps.newHashMap();
                    map.put("data", argsStr);
                    map.put("index", index);
                    kafkaTemplate.send(TOPIC, JSON.toJSONString(map));
                } else {
                    for (Object arg: args) {
                        if (arg == null || arg.getClass().isPrimitive() || CharSequence.class.isInstance(arg) || Number.class.isInstance(arg)
                                || Boolean.class.isInstance(arg)) {
                            continue;
                        }
                        JSONObject argStr = JSON.parseObject(JSON.toJSONString(arg));
                        argStr.put("index", index);
                        kafkaTemplate.send(TOPIC, argStr.toJSONString());
                        break;
                    }
                }
            }
        } catch (Exception e){
            log.error("发送数据到kafka异常,", e);
        }
    }
}

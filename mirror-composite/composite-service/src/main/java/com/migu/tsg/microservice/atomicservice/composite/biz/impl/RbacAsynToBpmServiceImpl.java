package com.migu.tsg.microservice.atomicservice.composite.biz.impl;

import com.migu.tsg.microservice.atomicservice.composite.helper.RbacHelper;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.UserPayload;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @projectName: RbacAsynToBpmServiceImpl
 * @description: 类
 * @author: luowenbo
 * @create: 2020-07-02 17:19
 **/
@EnableAsync
@Service
public class RbacAsynToBpmServiceImpl {

    @Autowired
    private RbacHelper rbacHelper;

    @Value("${syncToBpm:false}")
    private boolean syncToBpm;

    @Async
    public void syncUserToBpm(Map<String,String> user,String operateType){
        if(syncToBpm){
            System.out.println("sync's method will begin.");
            Map<String, List<Map<String,String>>> m = rbacHelper.packageParams(user,operateType);
            rbacHelper.syncPortalUserToBpm(m,true);
        }
    }
}

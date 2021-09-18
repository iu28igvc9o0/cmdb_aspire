package com.migu.tsg.microservice.atomicservice.composite.clientservice.project;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.migu.tsg.microservice.atomicservice.architect.service.ProjectTemplateService;

/**
 * 
 * 项目模板调用原子接口 Project Name:composite-service File
 * Name:TemplatesServiceClient.java Package
 * Name:com.migu.tsg.microservice.atomicservice.composite.clientservice.project
 * ClassName: TemplatesServiceClient <br/>
 * date: 2017年9月27日 下午4:00:06 <br/>
 * 项目模板调用原子接口
 * 
 * @author baiwp
 * @version
 * @since JDK 1.6
 */
@FeignClient(value = "rbac")
public interface TemplatesServiceClient extends ProjectTemplateService {

}

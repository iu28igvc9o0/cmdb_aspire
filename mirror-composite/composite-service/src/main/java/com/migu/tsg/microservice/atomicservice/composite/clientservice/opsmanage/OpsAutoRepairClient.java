/**
 *
 * 项目名： composite-service 
 * <p/> 
 *
 * 文件名:  OpsManageClient.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月26日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.migu.tsg.microservice.atomicservice.composite.clientservice.opsmanage;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.mirror.ops.api.service.IOpsAutoRepairService;

/** 
 *
 * 项目名称: composite-service 
 * <p/>
 * 
 * 类名: OpsManageClient
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月26日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@FeignClient(value = "ops-service", url = "http://10.1.203.100:30303")
public interface OpsAutoRepairClient extends IOpsAutoRepairService {
	
}

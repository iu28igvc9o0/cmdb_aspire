package com.migu.tsg.microservice.atomicservice.composite.clientservice.opsmanage;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.mirror.ops.api.service.IOpsWhitelistService;

/**
 * @projectName: WhiteListClient
 * @description: 接口
 * @author: xuxixuan
 * @create: 2020-03-011 09:00
 **/
@FeignClient("ops-service")
public interface OpsWhitelistManageClient extends IOpsWhitelistService {
//	@PostMapping(value = "/queryWhitelistHostList", produces = MediaType.APPLICATION_JSON_VALUE)
//	PageListQueryResult<OpsWhitelistHost> queryWhitelistHostList(@RequestBody OpsWhitelistHostQueryParam queryParam);
//	
//	@PostMapping(value = "/queryOpsWhitelistHostByKeys", produces = MediaType.APPLICATION_JSON_VALUE)
//	OpsWhitelistHost queryOpsWhitelistHostByKeys(@RequestBody OpsWhitelistHost queryParam);
//	
//	@PostMapping(value = "/saveWhitelistHost", produces = MediaType.APPLICATION_JSON_VALUE)
//	GeneralResponse saveWhitelistHost(@RequestBody OpsWhitelistHost whitelistHost);
//	
//	@DeleteMapping(value = "/removeWhitelistHostById/{whitelistHostId}", produces = MediaType.APPLICATION_JSON_VALUE)
//	void removeWhitelistHostById(@PathVariable("whitelistHostId") String whitelistHostId);
//	
//	
//	//巡检接口
//
//	@PostMapping(value = "/queryWhitelistCruiseCheckList", produces = MediaType.APPLICATION_JSON_VALUE)
//	PageListQueryResult<OpsWhitelistCruiseCheck> queryWhitelistCruiseCheckList(@RequestBody OpsWhitelistCruiseCheckQueryParam  queryParam);
//	
//	@PostMapping(value = "/queryOpsWhitelistCruiseCheckByKeys", produces = MediaType.APPLICATION_JSON_VALUE)
//	OpsWhitelistCruiseCheck queryOpsWhitelistCruiseCheckByKeys(@RequestBody OpsWhitelistCruiseCheck queryParam);
//	
//	@PostMapping(value = "/saveWhitelistCruiseCheck", produces = MediaType.APPLICATION_JSON_VALUE)
//	GeneralResponse 	saveWhitelistCruiseCheck(@RequestBody 	OpsWhitelistCruiseCheck whitelisCruiseCheck);
//	
//	@DeleteMapping(value = "/removeWhitelistCruiseCheckById/{whitelistCruiseCheckId}", produces = MediaType.APPLICATION_JSON_VALUE)
//	void removeWhitelistCruiseCheckById(@PathVariable("whitelistCruiseCheckId") String whitelistCruiseCheckId);
}

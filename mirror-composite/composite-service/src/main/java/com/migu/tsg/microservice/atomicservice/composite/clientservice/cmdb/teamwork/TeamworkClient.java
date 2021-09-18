package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.teamwork;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.ums.cmdb.teamwork.ITeamworkAPI;

@FeignClient(value = "CMDB")
public interface TeamworkClient extends ITeamworkAPI {
}

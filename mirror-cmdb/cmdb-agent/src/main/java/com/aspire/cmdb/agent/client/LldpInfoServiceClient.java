package com.aspire.cmdb.agent.client;

import com.aspire.mirror.elasticsearch.api.dto.LldpInfo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient("ELASTICSEARCH-SERVICE")
@Component
public interface LldpInfoServiceClient {

    @GetMapping("/v1/lldp/querylldpInfoByidcAndIp")
    List<LldpInfo> querylldpInfoByidcAndIp(@RequestParam(value = "idcType", required = false) String idcType,
                                           @RequestParam(value = "ips", required = false) String ips);
}

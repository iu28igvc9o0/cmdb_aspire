//package com.migu.tsg.microservice.atomicservice.composite.clientservice.common;
//
//import com.aspire.mirror.composite.service.inspection.payload.ConfigDict;
//import org.springframework.cloud.netflix.feign.FeignClient;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.List;
//
//@FeignClient(value = "${mirror.feign.common.value}", path = "${mirror.feign.common.path:}")
//public interface CommonServiceClient {
//
//    @GetMapping(value = "/cmdb/configDict/getDictsByType")
//    List<ConfigDict> getDictsByType(@RequestParam("type") String type,
//                                    @RequestParam(value = "pid", required = false) String pid,
//                                    @RequestParam(value = "pValue", required = false) String pValue,
//                                    @RequestParam(value = "pType", required = false) String pType);
//
//}

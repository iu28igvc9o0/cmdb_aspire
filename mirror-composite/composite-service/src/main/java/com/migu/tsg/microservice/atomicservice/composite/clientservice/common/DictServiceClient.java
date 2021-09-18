//package com.migu.tsg.microservice.atomicservice.composite.clientservice.common;
//
//import com.aspire.mirror.common.api.service.DictService;
//import com.aspire.mirror.composite.service.inspection.payload.ConfigDict;
//import org.springframework.cloud.netflix.feign.FeignClient;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.List;
//
///**
// * 字典客户端
// * <p>
// * 项目名称:  mirror平台
// * 包:        com.migu.tsg.microservice.atomicservice.composite.clientservice.common
// * 类名称:    DictServiceClient.java
// * 类描述:    字典客户端
// * 创建人:    JinSu
// * 创建时间:  2018/11/9 17:16
// * 版本:      v1.0
// */
//@FeignClient(value = "${mirror.feign.common.value}", path = "${mirror.feign.common.path:}")
//public interface DictServiceClient extends DictService {
////
//    @RequestMapping(value = "/cmdb/configDict/getDictsByType", method = RequestMethod.GET)
//    List<ConfigDict> getConfigDictsByType(@RequestParam("type") String type,
//                                          @RequestParam(value = "pid", required = false) String pid,
//                                          @RequestParam(value = "pValue", required = false) String pValue,
//                                          @RequestParam(value = "pType", required = false) String pType);
//
//}

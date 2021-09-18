package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.mainten;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.maintenance.payload.Concat;
import com.aspire.ums.cmdb.maintenance.payload.ProduceInfoRequest;
import com.aspire.ums.cmdb.maintenance.payload.ProduceInfoResq;

@FeignClient(value = "CMDB")
public interface MaintenProduceInfoClient {

    @PostMapping("/cmdb/maintenProduce/listProduceByPage")
    Result<ProduceInfoResq> selectProduceByPage(@RequestBody ProduceInfoRequest produceInfoRequest);

    @GetMapping(value = "/cmdb/maintenProduce/getProduceById" )
    ProduceInfoResq getProduceById(@RequestParam("id") String id);

    @PostMapping("/cmdb/maintenProduce/insertProduce")
    Map<String, Object> insertProduce(@RequestBody ProduceInfoResq produceInfoRequest);

    @PostMapping("/cmdb/maintenProduce/updateProduce")
    Map<String, Object> updateProduce(@RequestBody ProduceInfoResq produceInfoRequest);

    @DeleteMapping("/cmdb/maintenProduce/deleteProduce/")
    Map<String, Object> deleteProduce(@RequestParam("produceId") String produceId);

//    @GetMapping("/cmdb/maintenProduce/getConcatsByProduceId")
//    List<Concat> getConcatsByProduceId(@RequestParam("produceId") String produceId);

    @GetMapping("/cmdb/maintenProduce/queryConcat")
    List<Concat> queryConcat(@RequestParam("produceId") String produceId,
                             @RequestParam(value = "personType", required = false) String type);


    @RequestMapping(value = "/cmdb/maintenProduce/export", method = RequestMethod.POST)
    JSONObject export(@RequestBody Map<String,Object> reqParam);

    @GetMapping("/cmdb/maintenProduce/queryProduceInfoList")
    List<ProduceInfoResq> queryProduceInfoList();

    @PostMapping(value = "/cmdb/maintenProduce/addConcat" )
    Map<String, Object> addConcat(@RequestBody Concat concat);

    @PostMapping(value = "/cmdb/maintenProduce/updateConcat" )
    Map<String, Object> updateConcat(@RequestBody Concat concat);

    @DeleteMapping(value = "/cmdb/maintenProduce/deleteConcat" )
        Map<String, Object> deleteConcat(@RequestParam("id") String id);

//    @RequestMapping(value = "/cmdb/maintenProduce/queryConcatByProduceId", method = RequestMethod.GET)
//    List<Concat> queryConcatByProduceId(@RequestParam("produceId") String produceId);
}

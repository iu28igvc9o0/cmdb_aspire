package com.aspire.ums.cmdb.maintenance.web;


import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.maintenance.IProduceInfoAPI;
import com.aspire.ums.cmdb.maintenance.payload.Concat;
import com.aspire.ums.cmdb.maintenance.payload.ProduceInfoRequest;
import com.aspire.ums.cmdb.maintenance.payload.ProduceInfoResq;
import com.aspire.ums.cmdb.maintenance.service.IProduceInfoService;
import com.aspire.ums.cmdb.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Dscription: 维保厂商Controller
 * @Author: PJX
 * @Version: V1.0
 */
@RestController
@Slf4j
public class ProduceInfoController implements IProduceInfoAPI {

    @Autowired
    private IProduceInfoService produceInfoService;

    @Override
    public Result<ProduceInfoResq> selectProduceByPage(@RequestBody ProduceInfoRequest produceInfoRequest) {
        return produceInfoService.selectProduceByPage(produceInfoRequest);
    }

    @Override
    public ProduceInfoResq getProduceById(@RequestParam("id") String id) {
        return produceInfoService.getProduceById(id);
    }

    @Override
    public Map<String, Object> insertProduce(@RequestBody ProduceInfoResq produceInfoRequest) {
        log.info("ProduceInfo insertProduce() param is {} ",produceInfoRequest);
        return produceInfoService.insert(produceInfoRequest);
    }

    @Override
    public Map<String, Object> updateProduce(@RequestBody ProduceInfoResq produceInfoRequest) {
        log.info("ProduceInfo updateProduce() param is {} ",produceInfoRequest);
        return produceInfoService.update(produceInfoRequest);
    }

    @Override
    public Map<String, Object> deleteProduce(@RequestParam("produceId") String produceId) {
        log.info("ProduceInfo deleteProduce() param is {} ",produceId);
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            produceInfoService.deleteProduce(produceId);
            result.put("success", true);
            result.put("message", "删除厂商成功");
        }catch (Exception e) {
            log.error("ProduceInfoController deleteProduce() error {}" , e);
            result.put("success", false);
            result.put("message", "删除厂商失败");
        }
        return result;
    }

    @Override
    public JSONObject export (@RequestBody Map<String, Object> sendRequest) {
        try {
            log.info("Request -> /cmdb/maintenProduce/exprot data -> {}", JsonUtil.toJacksonJson(sendRequest));
            JSONObject returnJson = produceInfoService.queryExportData(sendRequest);
            return returnJson;
        } catch (Exception e) {
            log.error("Query cmdb maintenProduce exprot error. {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<ProduceInfoResq> queryProduceInfoList() {
        return produceInfoService.queryProduceInfoList();
    }


    @Override
    public List<Concat> queryConcat(@RequestParam("produceId")String produceId,
                                    @RequestParam(value = "personType", required = false) String type) {
        log.info("ProduceInfo queryConcat() param produceId is {} personType {} ",produceId, type);
        return produceInfoService.queryConcat(produceId, type);
    }

    /**
     * 添加联系人
     * @param concat
     * @return
     */
    @Override
    public Map<String, Object> addConcat(@RequestBody Concat concat){
        Map<String, Object> result = new HashMap<>();
        try {
            log.info("Request ProduceInfoController.addConcat");
            produceInfoService.addConcat(concat);
            result.put("success", true);
            result.put("message", "新增成功");
        }catch (Exception e) {
            log.info("新增失败, error: {}", e.getMessage() );
            result.put("success", false);
            result.put("message", "新增失败,error:" + e.getMessage());
        }
        return  result;
    }

    @Override
    public Map<String, Object> updateConcat(@RequestBody Concat concat) {
        Map<String, Object> result = new HashMap<>();
        try {
            log.info("Request ProduceInfoController.updateConcat");
            produceInfoService.updateConcat(concat);
            result.put("success", true);
            result.put("message", "更新成功");
        }catch (Exception e) {
            log.info("更新失败, error: {}", e.getMessage() );
            result.put("success", false);
            result.put("message", "更新失败,error:" + e.getMessage());
        }
        return  result;
    }

    /**
     * 删除联系人
     * @param id
     * @return
             */
    @Override
    public Map<String, Object> deleteConcat(@RequestParam("id") String id) {
        Map<String, Object> result = new HashMap<>();
        try {
            log.info("Request ProduceInfoController.deleteConcat");
            produceInfoService.deleteConcat(id);
            result.put("success", true);
            result.put("message", "删除成功");
        } catch (Exception e) {
            log.info("删除失败, error: {}", e.getMessage() );
            result.put("success", false);
            result.put("message", "删除失败,error:" + e.getMessage());
        }
        return result;
    }
}

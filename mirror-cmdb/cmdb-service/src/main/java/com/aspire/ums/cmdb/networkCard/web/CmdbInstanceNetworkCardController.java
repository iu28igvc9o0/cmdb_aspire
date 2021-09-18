package com.aspire.ums.cmdb.networkCard.web;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.networkCard.INetworkCardProgramAPI;
import com.aspire.ums.cmdb.networkCard.payload.CmdbInstanceNetworkCard;
import com.aspire.ums.cmdb.networkCard.service.ICmdbInstanceNetworkCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbInstanceNetworkCardController
 * Author:   luowenbo
 * Date:     2019/09/18 14:41
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbInstanceNetworkCardController implements INetworkCardProgramAPI {

    @Autowired
    private ICmdbInstanceNetworkCardService cmdbInstanceNetworkCardService;

    @Override
    public JSONObject insertNetwordCard(@RequestBody CmdbInstanceNetworkCard cmdbInstanceNetworkCard) {
        log.info("Request into CmdbInstanceNetworkCardController.insertNetwordCard()  params -> {}", cmdbInstanceNetworkCard);
        JSONObject jsonObject = new JSONObject();
        try {
            cmdbInstanceNetworkCardService.insert(cmdbInstanceNetworkCard);
            jsonObject.put("success", true);
            jsonObject.put("msg", "新增成功");
        } catch (Exception e) {
            jsonObject.put("msg", e.getMessage());
            log.error("Create cmdbInstanceNetworkCard project error.", e);
        }
        return jsonObject;
    }

    @Override
    public JSONObject deleteNetwordCardById(@RequestParam("id") String ids) {
        log.info("Request into CmdbInstanceNetworkCardController.deleteNetwordCardById()  params -> {}", ids);
        JSONObject jsonObject = new JSONObject();
        try {
            List idList = Arrays.asList(ids.split(","));
            cmdbInstanceNetworkCardService.deleteByLogic(idList);
            jsonObject.put("success", true);
            jsonObject.put("msg", "删除成功");
        } catch (Exception e) {
            jsonObject.put("msg", e.getMessage());
            log.error("delete cmdbInstanceNetworkCard project error.", e);
        }
        return jsonObject;
    }

    @Override
    public JSONObject updateNetwordCard(@RequestBody CmdbInstanceNetworkCard cmdbInstanceNetworkCard) {
        log.info("Request into CmdbInstanceNetworkCardController.updateNetwordCard()  params -> {}", cmdbInstanceNetworkCard);
        JSONObject jsonObject = new JSONObject();
        try {
            cmdbInstanceNetworkCard.setIsDelete("0");
            cmdbInstanceNetworkCardService.update(cmdbInstanceNetworkCard);
            jsonObject.put("success", true);
            jsonObject.put("msg", "修改成功");
        } catch (Exception e) {
            jsonObject.put("msg", e.getMessage());
            log.error("Update cmdbInstanceNetworkCard project error.", e);
        }
        return jsonObject;
    }

    @Override
    public List<CmdbInstanceNetworkCard> getNetwordCardByInstanceId(@RequestParam("instanceId") String instanceId) {
        log.info("Request into CmdbInstanceNetworkCardController.getNetwordCardByInstanceId()  params -> {}", instanceId);
        try {
            return cmdbInstanceNetworkCardService.listByInstanceId(instanceId);
        } catch (Exception e) {
            log.error("get cmdbInstanceNetworkCard project error.", e);
        }
        return null;
    }

    @Override
    public CmdbInstanceNetworkCard getNetwordCardByName(String name) {
        log.info("Request into CmdbInstanceNetworkCardController.getNetwordCardByName()  params -> {}", name);
        try {
            return cmdbInstanceNetworkCardService.get(name);
        } catch (Exception e) {
            log.error("Get cmdbInstanceNetworkCard project error.", e);
        }
        return null;
    }
}

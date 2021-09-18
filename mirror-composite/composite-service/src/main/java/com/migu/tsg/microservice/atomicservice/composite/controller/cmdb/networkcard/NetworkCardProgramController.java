package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.networkcard;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.networkCard.INetworkCardProgramAPI;
import com.aspire.ums.cmdb.networkCard.payload.CmdbInstanceNetworkCard;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.networkcard.NetworkCardProgramClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: NetworkCardProgramController
 * Author:   luowenbo
 * Date:     2019/09/20 10:49
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class NetworkCardProgramController implements INetworkCardProgramAPI {

    @Autowired
    private NetworkCardProgramClient projectClient;

    @Override
    public JSONObject insertNetwordCard(@RequestBody CmdbInstanceNetworkCard cmdbInstanceNetworkCard) {
        return projectClient.insertNetwordCard(cmdbInstanceNetworkCard);
    }

    @Override
    public JSONObject deleteNetwordCardById(@RequestParam("id") String id) {
        return projectClient.deleteNetwordCardById(id);
    }

    @Override
    public JSONObject updateNetwordCard(@RequestBody CmdbInstanceNetworkCard cmdbInstanceNetworkCard) {
        return projectClient.updateNetwordCard(cmdbInstanceNetworkCard);
    }

    @Override
    public List<CmdbInstanceNetworkCard> getNetwordCardByInstanceId(@RequestParam("instanceId") String instanceId) {
        return projectClient.getNetwordCardByInstanceId(instanceId);
    }

    @Override
    public CmdbInstanceNetworkCard getNetwordCardByName(@RequestParam("name") String name) {
        return projectClient.getNetwordCardByName(name);
    }
}

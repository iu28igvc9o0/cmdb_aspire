package com.migu.tsg.microservice.atomicservice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.JsonUtil;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.UserViewAction;

/**
 * xxxxxx
 *
 * @author jiangxuwen
 * @date 2020/12/23 16:29
 */
public class StreamTest {
    
    @Test
    public void test(){
        String json = "[\n" + "  {\n" + "    \"actions\": [],\n" + "    \"resource\": \"0a1490a5-37e1-4ea8-9d07-1764258c752d\"\n"
                + "  },\n" + "  {\n" + "    \"actions\": [\n" + "      \"cmdb:create\",\n" + "      \"cmdb:update\",\n"
                + "      \"cmdb:delete\",\n" + "      \"cmdb:import\",\n" + "      \"cmdb:export\",\n"
                + "      \"cmdb:batchUpdate\"\n" + "    ],\n" + "    \"resource\": \"51ec25f7-92ca-4d40-804d-4a0982160efb\"\n"
                + "  },\n" + "  {\n" + "    \"actions\": [\n" + "      \"cmdb:create\",\n" + "      \"cmdb:update\",\n"
                + "      \"cmdb:delete\",\n" + "      \"cmdb:import\",\n" + "      \"cmdb:export\",\n"
                + "      \"cmdb:batchUpdate\",\n" + "      \"cmdb:ip_register_update\"\n" + "    ],\n"
                + "    \"resource\": \"ee586a93-2e4c-464d-b358-bbe5b126937b\"\n" + "  }\n" + "]";
        List<UserViewAction> userActions = JsonUtil.jacksonConvert(json, new TypeReference<List<UserViewAction> >() {
        });
        Map<String,List<String>> map = userActions.stream().collect(Collectors.toMap(UserViewAction::getResource, UserViewAction::getActions));
        String str = JsonUtil.toJacksonJson(map);
        System.out.println(str);
    }
}

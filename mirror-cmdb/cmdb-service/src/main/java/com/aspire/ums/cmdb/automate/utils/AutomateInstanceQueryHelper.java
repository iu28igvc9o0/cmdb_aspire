package com.aspire.ums.cmdb.automate.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.automate.exception.AutomateException;
import com.aspire.ums.cmdb.automate.payload.AutomateHostDTO;
import com.aspire.ums.cmdb.automate.payload.AutomateHostEntity;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author fanwenhui
 * @date 2020-08-24 11:06
 * @description 查询自动化实例帮助类
 */
@Slf4j
public class AutomateInstanceQueryHelper {

    /**
     * 查询实例的详细信息
     * @param instanceIdList 实例ID集合
     * @param type 实例类型
     */
    public static String queryInstanceByType(List<String> instanceIdList,String type) {
        try {
            return getInstanceResponse(type, instanceIdList);
        } catch (Exception e) {
            log.error("", e);
            throw new AutomateException(e);
        }
    }

    private static String getInstanceResponse(String type,List<String> instanceIdList) {
        Map<String, String> data2 = new TreeMap<>();
        EasyOpsAPI easyOpsAPI = new EasyOpsAPI();
        return easyOpsAPI.doApi(String.format("/cmdb_resource/object/%s/instance/_search", type), data2,
                buildRequestBody(instanceIdList), "POST");
    }

    private static String buildRequestBody(List<String> instanceIdList) {
        return buildRequestBody(instanceIdList, 1, 200);
    }

    private static String buildRequestBody(List<String> instanceIdList, Integer pageNo, Integer pageSize) {
        pageNo = pageNo == null ? 1 : pageNo;
        pageSize = pageSize == null ? 200 : pageSize;
        String instanceIdStr = JSONArray.toJSONString(instanceIdList);
        String requestBody = "{\"page\":" + pageNo + ",\"page_size\":" + pageSize + ",\"query\":{\"instanceId\":{\"$in\":"
                + instanceIdStr + "}}}";
        return requestBody;
    }

    public static <T> List<T> array2List(Object value, Class<T> clazz) {
        List<T> dataList = Lists.newArrayList();
        String jsonString = JSON.toJSONString(value);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        Object targetObj = jsonObject.get("new");
        if (targetObj == null) {
            return dataList;
        }
        if (targetObj instanceof JSONArray) {
            List<T> list = JSONObject.parseArray(targetObj.toString(), clazz);
            dataList.addAll(list);
        } else if (targetObj instanceof JSONObject) {
            dataList.add(JSONObject.parseObject(JSON.toJSONString(targetObj), clazz));
        } else {
            dataList.add((T) targetObj.toString());
        }
        return dataList;
    }

}

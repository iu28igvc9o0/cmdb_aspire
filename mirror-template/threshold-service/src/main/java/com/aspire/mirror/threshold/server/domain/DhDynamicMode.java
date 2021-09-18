package com.aspire.mirror.threshold.server.domain;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    DhDynamicMode
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/11/4 16:36
 * 版本:      v1.0
 */
@Data
public class DhDynamicMode {
    private String itemId;
    private String item;
    private String resourceId;
    private String host;
    private String deviceClass;
    private String idcType;
    private String roomId;
    private String type;
    private String taskId;
    private String status;
    private String model;
    private String updateTime;
    private String lastUpdateTime;
    private String desc;
    private String templateId;
    private String errMsg;
    private String key_;

    public static DhDynamicMode parseByJSONObject(JSONObject jsonObject) {
        DhDynamicMode dhDynamicMode = new DhDynamicMode();
        dhDynamicMode.setTaskId(jsonObject.getString("taskId"));
        dhDynamicMode.setItem(jsonObject.getJSONObject("id").getString("item"));
        dhDynamicMode.setItemId(jsonObject.getJSONObject("id").getString("itemId"));
        dhDynamicMode.setResourceId(jsonObject.getJSONObject("id").getString("resourceId"));
        dhDynamicMode.setHost(jsonObject.getJSONObject("id").getString("host"));
        dhDynamicMode.setDeviceClass(jsonObject.getJSONObject("id").getString("deviceClass"));
        dhDynamicMode.setIdcType(jsonObject.getJSONObject("id").getString("idcType"));
        dhDynamicMode.setRoomId(jsonObject.getJSONObject("id").getString("roomId"));
        dhDynamicMode.setType(jsonObject.getString("type").equals("平稳") ? "2" : "1");
        dhDynamicMode.setModel(jsonObject.getString("model"));
        dhDynamicMode.setStatus(jsonObject.getString("status"));
        dhDynamicMode.setLastUpdateTime(jsonObject.getString("lastUpdateTime"));
        dhDynamicMode.setUpdateTime(jsonObject.getString("updateTime"));
        dhDynamicMode.setDesc(jsonObject.getString("desc"));
        dhDynamicMode.setErrMsg(jsonObject.getString("errMsg"));
        // 指标模板id
        dhDynamicMode.setTemplateId(jsonObject.getJSONObject("id").getString("templateId"));
        dhDynamicMode.setKey_(jsonObject.getJSONObject("id").getString("item"));
        return dhDynamicMode;
    }
}

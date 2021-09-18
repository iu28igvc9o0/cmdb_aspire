package com.migu.tsg.microservice.atomicservice.composite.service.sms.payload;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.migu.tsg.microservice.atomicservice.composite.service.sms.dto.SmsInfoDTO;

import lombok.Data;

/**
 * 项目名称: 咪咕微服务运营平台 包:
 * com.migu.tsg.microservice.atomicservice.composite.service.sms.payload 类名称:
 * SmsQueryResponse.java 类描述: 创建人: GaoYang 创建时间: 2017/12/18 17:13 版本: v1.0
 */
@Data
public class SmsQueryResponse {
    // 返回日志总记录数
    @SerializedName("total_items")
    private String totalItems;

    // 返回日志总页数
    @SerializedName("total_page")
    private String totalPage;

    private List<SmsInfoDTO> smsInfos;
}
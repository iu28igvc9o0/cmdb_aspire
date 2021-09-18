package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* role列表请求响应
* Project Name:composite-service
* File Name:RoleListResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.controller.rabc.serviceApi.payload.response
* ClassName: RoleListResponse <br/>
* date: 2017年8月28日 上午2:47:59 <br/>
* @author pengguihua
* @version
* @since JDK 1.6
*/
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
public class RoleListResponse {
    @JsonProperty("count")
    private Integer totalCount;

    @JsonProperty("page_size")
    private Integer pageSize;

    @JsonProperty("num_pages")
    private Integer pageCount;

    @JsonProperty("results")
    private List<RbacRoleListItem> roleDataList;
}

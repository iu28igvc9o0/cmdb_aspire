package com.migu.tsg.microservice.atomicservice.rbac.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 项目名称: rbac-api <br>
* 包: com.migu.tsg.microservice.atomicservice.rbac.dto <br>
* 类名称: ListActionsResponse.java <br>
* 类描述: 获取有权限的资源操作列表<br>
* 创建人: WangSheng <br>
* 创建时间: 2018年2月1日下午5:52:15 <br>
* 版本: v1.0
*/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListActionsResponse {

    /**
     * 资源操作
     * 例子：["service:*","service:create","service:update","service:view"]
     */
    private List<String> actions;

    /**
     * 资源类型
     * 例子：service
     */
    private String resource;

}

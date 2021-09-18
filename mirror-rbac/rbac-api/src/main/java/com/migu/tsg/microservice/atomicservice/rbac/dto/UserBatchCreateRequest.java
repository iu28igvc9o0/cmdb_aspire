package com.migu.tsg.microservice.atomicservice.rbac.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.rbac.dto
 * 类名称:    UserBatchCreateRequest.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/4/24 19:13
 * 版本:      v1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBatchCreateRequest {
    private List<UserCreateRequest> listUser;
}

package com.migu.tsg.microservice.atomicservice.rbac.dao.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.rbac.dao.po
 * 类名称:    DepartmentUser.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/5/21 17:07
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentUser {
    private String  deptId;

    private String userId;
}

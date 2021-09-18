package com.migu.tsg.microservice.atomicservice.rbac.dao.po;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * xxxxxx
 *
 * @author jiangxuwen
 * @date 2020/12/11 16:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentUserTree implements Serializable {

    private static final long serialVersionUID = -2963816975755971519L;

    /** 部门或用户的id */
    private String uuid;

    /** 父级Id */
    private String parentId;

    /** 部门或人员名称 */
    private String name;

    /** 用户名 */
    private String code;

    /** 类型: user用户、department部门 */
    private String type;
}

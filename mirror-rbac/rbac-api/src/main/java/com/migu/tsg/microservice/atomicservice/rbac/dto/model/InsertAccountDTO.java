package com.migu.tsg.microservice.atomicservice.rbac.dto.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto.model <br>
 * 类名称: InsertAccountDTO.java <br>
 * 类描述: 新增成员信息对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月16日上午10:04:48 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InsertAccountDTO {

    /**
     * 成员用户名
     */
    private String username;

    /**
     * 成员邮箱
     */
    private String email;
    

    //账号类型
    private String type;

    //描述
    private String description;

    /**
     * 项目
     * 例如：B1(包一),B2(包二)
     */
    private List<String> projects;

    private String mobile;

}

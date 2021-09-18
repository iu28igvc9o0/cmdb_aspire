package com.migu.tsg.microservice.atomicservice.rbac.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto <br>
 * 类名称: UpdateSubAccountResponse.java <br>
 * 类描述: 更新成员(账号)信息 的响应对象<br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月16日上午9:45:29 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateSubAccountResponse {
    
    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 所属项目
     */
    private List<String> projects;

}

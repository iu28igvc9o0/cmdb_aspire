package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateAccountResponse {
    
     // 电子邮箱
    private String email;

    // 所属项目
    private List<String> projects;
    
    // 所属项目名
    private List<String> projectNames;
}

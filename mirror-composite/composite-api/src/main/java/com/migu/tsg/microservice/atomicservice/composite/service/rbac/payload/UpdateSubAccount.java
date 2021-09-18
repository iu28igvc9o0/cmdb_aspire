package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
public class UpdateSubAccount {
    // 电子邮箱
    private String email;
 
    // 所属项目名
    private List<String> projectNames;
}

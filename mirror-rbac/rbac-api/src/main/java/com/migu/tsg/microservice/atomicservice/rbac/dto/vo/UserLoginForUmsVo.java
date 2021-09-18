package com.migu.tsg.microservice.atomicservice.rbac.dto.vo;

import lombok.Data;

@Data
public class UserLoginForUmsVo {
	 private String username;
	 private String password;
	 private String loginKey;
}

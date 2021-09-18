package com.migu.tsg.microservice.atomicservice.composite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;


/**
* ErrorsResponse类
* Project Name:composite-api
* File Name:ErrorsResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite
* ClassName: ErrorsResponse <br/>
* date: 2017年9月1日 下午12:40:18 <br/>
* 异常响应
* @author yangshilei
* @version
* @since JDK 1.6
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorsResponse {
    private List<Map<String, Object>> errors;
}

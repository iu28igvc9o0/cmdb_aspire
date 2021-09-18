package com.migu.tsg.microservice.atomicservice.composite.service.ping.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: msp-composite
 * @Description:
 * @author: YangShiLei
 * @create: 2018-04-21 16:08
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Detail {

    private String operator;

    private String operation;
}

package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.teamwork;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public enum TearworkStatusEnum {

	WORKING("1","协同中"),
	FINISHED("2","协同结束"),
	SUMMARIZED("3","已汇总");
	
    @Getter
    private String value;

    @Getter
    private String name;

}

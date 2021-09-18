package com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload;

import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 环境变量对象
 * @author longfeng
 *
 */
@Data
@NoArgsConstructor
public class EnvvarsDTOCopy {
	private String name;

    private String value;
}

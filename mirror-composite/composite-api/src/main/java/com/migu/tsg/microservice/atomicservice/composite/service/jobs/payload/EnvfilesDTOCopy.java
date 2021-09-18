package com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 环境变量文件对象
 * 
 * @author longfeng
 *
 */
@Data
@NoArgsConstructor
public class EnvfilesDTOCopy {
	private String uuid;

	private String name;
}

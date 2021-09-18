package com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload;

import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 
 * @author longfeng
 *
 */
@Data
@NoArgsConstructor
public class JobLogsDTO {
	 private int time;
     private String message;
     private int level;
}

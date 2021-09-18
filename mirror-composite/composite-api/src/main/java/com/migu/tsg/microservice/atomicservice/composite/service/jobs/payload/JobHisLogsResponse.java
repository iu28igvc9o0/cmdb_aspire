package com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JobHisLogsResponse {
	private String message;
	private String level;
	private Long time;
}

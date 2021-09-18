package com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateJobStatusRequest {
	private String status;

	@JsonProperty("region_id")
    private String regionId;
}

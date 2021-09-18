package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel
public class SyncHistoryUpdateRequest {
	private String status;
}

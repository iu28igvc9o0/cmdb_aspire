package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ListConfigResponse extends SyncConfigBaseVOCopy{
	//private static final long serialVersionUID = -3642697582501539981L;
	
	@JsonProperty("source")
	private SyncConfigSourceVOCopy source;
	
	@JsonProperty("dest")
	private List<SyncConfigDestVOCopy> dest;
	
	@JsonProperty("resource_actions")
	private List<String> resourceActions;
	
}

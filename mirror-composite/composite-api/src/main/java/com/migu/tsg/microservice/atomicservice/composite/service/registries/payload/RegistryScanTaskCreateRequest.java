package com.migu.tsg.microservice.atomicservice.composite.service.registries.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistryScanTaskCreateRequest {
	private String registry_name;
	private String repo_uuid;
	private String repo_path	;
	private String tag;
	private String scan_type;
	private String username;
	private String token;
	private String namespace;
}

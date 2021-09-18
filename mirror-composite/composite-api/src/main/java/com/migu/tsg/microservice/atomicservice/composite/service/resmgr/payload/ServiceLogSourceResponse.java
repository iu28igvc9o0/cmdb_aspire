package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor
@AllArgsConstructor
public class ServiceLogSourceResponse {
private List<String> sources;
}

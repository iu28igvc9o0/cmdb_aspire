package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Logs {
    private String instance_id;
    private String instance_id_full;
    private String message;
    private Double time;
}

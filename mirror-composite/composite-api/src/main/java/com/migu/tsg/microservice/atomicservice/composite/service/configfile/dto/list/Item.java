package com.migu.tsg.microservice.atomicservice.composite.service.configfile.dto.list;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Item {
    private String key;
    private String value;
}

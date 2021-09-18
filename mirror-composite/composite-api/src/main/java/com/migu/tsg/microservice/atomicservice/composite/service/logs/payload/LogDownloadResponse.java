package com.migu.tsg.microservice.atomicservice.composite.service.logs.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class LogDownloadResponse {

    private boolean exceed;
    private int count;
    private int limit;
}

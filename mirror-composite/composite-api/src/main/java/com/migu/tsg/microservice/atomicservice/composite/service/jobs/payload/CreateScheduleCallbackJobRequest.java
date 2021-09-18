package com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateScheduleCallbackJobRequest {
    
    private String namespace;
    private String token;
    private String time;
    private String created_by;
    private String user_token;
    private String config_uuid;
    private String is_auto_scheduled;

}

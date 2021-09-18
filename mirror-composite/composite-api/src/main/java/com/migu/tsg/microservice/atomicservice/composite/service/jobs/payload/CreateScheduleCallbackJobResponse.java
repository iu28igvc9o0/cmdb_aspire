package com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateScheduleCallbackJobResponse {
    
    private String job_uuid;

}

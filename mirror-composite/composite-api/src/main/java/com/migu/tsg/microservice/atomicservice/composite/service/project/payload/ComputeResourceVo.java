package com.migu.tsg.microservice.atomicservice.composite.service.project.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ComputeResourceVo {
    private CpuAndMemVo num;
    private CpuAndMemVo percent;
}

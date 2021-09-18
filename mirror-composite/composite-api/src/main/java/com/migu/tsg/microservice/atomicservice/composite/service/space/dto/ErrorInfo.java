package com.migu.tsg.microservice.atomicservice.composite.service.space.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ErrorInfo {
    private String source;
    private String message;
    private String code;
    private String fields;
}

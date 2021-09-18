package com.migu.tsg.microservice.atomicservice.composite.common.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConditionPiece{
    private String column;
    private String op;
    private Object value;
    
    private String prefix;
    private String suffix;
}
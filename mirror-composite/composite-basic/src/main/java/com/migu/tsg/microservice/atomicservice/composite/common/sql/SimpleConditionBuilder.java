package com.migu.tsg.microservice.atomicservice.composite.common.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SimpleConditionBuilder implements Condition {
    public static final String EQUALS = "=";
    public static final String NOT_EQUALS = "<>";
    public static final String GREATER_THAN = ">";
    public static final String LESS_THAN = "<";
    public static final String GREATER_EQUALS = ">=";
    public static final String LESS_EQUALS = "<=";
    public static final String CONTAINS = "contains";
    public static final String CONTAINS_IGNORE_CASE = "icontains";
    public static final String IN = "in";
    public static final String START_WITH = "startWith";
    public static final String END_WITH = "endWith";
    
    private String column;
    private String op;
    private Object value;
}

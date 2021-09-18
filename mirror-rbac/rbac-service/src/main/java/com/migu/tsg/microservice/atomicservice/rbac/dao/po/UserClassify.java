package com.migu.tsg.microservice.atomicservice.rbac.dao.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author menglinjie
 */
@NoArgsConstructor
@Data
public class UserClassify implements Serializable {
    private String uuid;

    private String userClassifyName;

    private String namespace;

    private String description;

    private String creator;

    private Date lastUpdateTime;

    private static final long serialVersionUID = 1L;

}
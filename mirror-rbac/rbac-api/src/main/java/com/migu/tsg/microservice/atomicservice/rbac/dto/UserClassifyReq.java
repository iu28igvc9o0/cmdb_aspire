package com.migu.tsg.microservice.atomicservice.rbac.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author menglinjie
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserClassifyReq {
    private String uuid;

    private String userClassifyName;

    private String namespace;

    private String description;

    private String creator;

    private Date lastUpdateTime;

    private String customizedviewId;

    private String systemId;

    private String pageCode;

    private String pageAlias;

    private String pageCustomConfig;

    private String[] userIds;

    private String customizedviewName;

    private String pageType;
}

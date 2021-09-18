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
public class UserClassifyPageConfig implements Serializable {
    private String uuid;

    private String userClassifyUuid;

    private String customizedviewId;

    private String systemId;

    private String pageCode;

    private String pageAlias;

    private String pageCustomConfig;

    private String namespace;

    private String creator;

    private Date lastUpdateTime;

    private static final long serialVersionUID = 1L;

}
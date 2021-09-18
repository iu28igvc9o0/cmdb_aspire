package com.migu.tsg.microservice.atomicservice.rbac.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class SysMenuResp {

    private String id;

    private String parentId;

    private String name;

    private String icon;

    private String menuType;

    private String base;

    private String path;

    private String component;

    private String url;

    private Integer sort;

    private String isShow;

    private String systemId;

    private String systemName;

    private String creater;

    private Date createTime;

    private String editer;

    private Date updateTime;

    private List<SysMenuResp> children;

}
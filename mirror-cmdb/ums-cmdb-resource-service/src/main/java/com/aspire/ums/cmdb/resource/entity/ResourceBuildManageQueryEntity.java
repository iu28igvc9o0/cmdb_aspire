package com.aspire.ums.cmdb.resource.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class ResourceBuildManageQueryEntity implements Serializable {

    private static final long serialVersionUID = -2796700420770121649L;

    private String appSystem;
    private Integer endIndex;
    private Integer pageNum;
    private Integer pageSize;
    private String primaryDepartment;
    private String secondaryDepartment;
    private Integer startIndex;
}

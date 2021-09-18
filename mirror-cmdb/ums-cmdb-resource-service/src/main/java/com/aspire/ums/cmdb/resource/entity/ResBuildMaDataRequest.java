package com.aspire.ums.cmdb.resource.entity;//package com.aspire.ums.cmdb.resource.entity;


import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class ResBuildMaDataRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String  name;

    private Integer startIndex;

    private Integer endIndex;

    private String rePool;

    private String reStatus;

    private String seType;

    private String seName;

    private Integer pageNum;

    private Integer pageSize;
}

package com.aspire.ums.cmdb.resource.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@ToString
public class UpdateResBuildRequest  implements Serializable {

    private static final long serialVersionUID = 1L;

    private String addUnit;

    private Integer buildId;

    private String buildName;

    private String configDetail;

    private String count;

    private String serverName;

    private String serverType;
}

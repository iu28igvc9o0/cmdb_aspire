package com.aspire.ums.cmdb.resource.entity;//package com.aspire.ums.cmdb.resource.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class ResBuildDetail implements Serializable {


    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer buildId;

    private String serverType;

    private String serverName;

    private String unit;

    private Integer count;

    private String configDetail;

    private Integer imports;


}

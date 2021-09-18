package com.aspire.ums.cmdb.resource.entity;//package com.aspire.ums.cmdb.resource.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class ResBuildMaDataResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean flag;

    private Integer sum;

    private List<ResBuildMaData> result;

}

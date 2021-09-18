package com.aspire.ums.cmdb.resource.entity;//package com.aspire.ums.cmdb.resource.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class AddOrUpdateResponse implements Serializable {

    private String msg;

    private Boolean flag;
}

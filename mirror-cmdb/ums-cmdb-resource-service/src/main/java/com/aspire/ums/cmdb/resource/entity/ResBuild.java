package com.aspire.ums.cmdb.resource.entity;//package com.aspire.ums.cmdb.resource.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class ResBuild implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String buildName;

    private Integer estimateId;

    private String resourcePool;

    private String status;

    private String createId;

    private Date createTime;

    private String updateId;

    private Date updateTime;

    private String remark;
}

package com.aspire.ums.cmdb.allocate.payload;

import lombok.Data;

import java.util.Date;

@Data
public class AllocateIpConfigOperationReq {

    private String id;

    private int type;

    private String allocateIpId;

    private String operator;

    private Date operationTime;
}

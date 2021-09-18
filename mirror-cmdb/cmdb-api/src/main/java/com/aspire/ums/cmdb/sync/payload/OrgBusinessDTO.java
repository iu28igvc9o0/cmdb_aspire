package com.aspire.ums.cmdb.sync.payload;

import java.io.Serializable;

import lombok.Data;

/**
 * 财务业务线(部门-业务线关联)
 *
 * @author jiangxuwen
 * @date 2020/5/13 11:01
 */
@Data
public class OrgBusinessDTO implements Serializable {

    private static final long serialVersionUID = 5123015786262616758L;

    private String id;

    /** 部门id,字典表的id. */
    private String departmentId;

    /** 财务业务线名称. */
    private String financialName;

    private String departmentName;

    private String cmdbBusinessName;

    private String cmdbBusinessCode;

    private String cmdbBusinessId;
}

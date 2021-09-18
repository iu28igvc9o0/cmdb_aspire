package com.aspire.ums.cmdb.sync.payload;

import java.io.Serializable;

import lombok.Data;

/**
 * 财务业务线和独立业务线关联DTO
 *
 * @author jiangxuwen
 * @date 2020/5/13 11:02
 */
@Data
public class FinBusinessRelDTO implements Serializable {

    private static final long serialVersionUID = 4273081272769373077L;

    /** cmdb业务线. */
    private String cmdbBusinessId;

    /** 财务业务线id. */
    private String financialBusinessId;
}

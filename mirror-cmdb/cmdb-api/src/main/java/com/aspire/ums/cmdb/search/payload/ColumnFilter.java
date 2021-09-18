package com.aspire.ums.cmdb.search.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnFilter implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
	private String menuType;
	private String moduleId;
    private String columnInfo;
    private Date insertTime;
    private Date updateTime;
    private Map columnMap;
    private String loginName;
}
package com.aspire.mirror.alert.server.clientservice.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.dict.entity
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/4/15 09:49
 * 版本: v1.0
 */
@Data
public class ConfigDict implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 4416835412977077798L;

	 /** 标签ID */
    private String id;
    
    /** 标签名称 */
    private String name;
    
    /** 标签类型 */
    private String type;
    
    /** 标签值 */
    private String value;
    
    /** 父标签ID */
    private String pid;
    
    /** 描述 */
    private String description;
    
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date create_date;
    
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date update_date;
    /** 父标签名 */
    private String pname;

    private List<ConfigDict> subList;
    
}

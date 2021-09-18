package com.aspire.ums.cmdb.allocate.entity;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Menu {
	
	/** 标签ID */
    private Integer dictId;
    
    /** 标签名称 */
    private String dictCode;
    
    /** 标签类型 */
    private String colName;
    
    /** 标签值 */
    private String dictNote;
    
    /** 父菜单 */
    private String upDict;
   
    //子菜单    
    private List<Menu> childMenuList;
	
     
}

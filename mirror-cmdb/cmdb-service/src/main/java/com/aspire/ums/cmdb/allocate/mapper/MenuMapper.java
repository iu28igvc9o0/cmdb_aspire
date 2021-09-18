package com.aspire.ums.cmdb.allocate.mapper;

 
import org.apache.ibatis.annotations.Mapper;

import com.aspire.ums.cmdb.allocate.payload.Menu;

import java.util.List;

 
/**
 * ip分配域名DAO  
 * <p>
 * 项目名称:   mirror平台
 * 包:        com.aspire.ums.cmdb.allocate.mapper
 * 类名称:    MenuMapper.java
 * 类描述:    ip分配域名DAO
 * 创建人:    JinSu
 * 创建时间:  2018/9/18 16:16
 * 版本:      v1.0
 */
@Mapper
public interface MenuMapper {
	
    /**
     * 查询Menu
     */
	List<Menu> selectMenu();
    
     
   

}

package com.aspire.ums.cmdb.allocate.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

 
import com.aspire.ums.cmdb.allocate.entity.AllocateDomain;

 
/**
 * ip分配域名DAO  
 * <p>
 * 项目名称:   mirror平台
 * 包:        com.aspire.ums.cmdb.allocate.mapper
 * 类名称:    AllocateDomainDao.java
 * 类描述:    ip分配域名DAO
 * 创建人:    JinSu
 * 创建时间:  2018/9/18 16:16
 * 版本:      v1.0
 */
@Mapper
public interface AllocateDomainMapper {
	
    /**
     * 新增ip分配域名
     * @param allocateDomain ip分配域名
     */
    int insertAllocateDomain(AllocateDomain allocateDomain);
    
    /**
     * 修改域名
     * @param allocateDomain ip分配域名
     */
    int updateAllocateDomain(AllocateDomain allocateDomain);
    
    
    /**
     * 查询域名根据id
     * @param id 域名id 
     */
     AllocateDomain  selectAllocateDomainById(String id);
    
    
    /**
     * 查询域名根据名称
     * @param domain 域名 
     */
     List<AllocateDomain>  selectAllocateDomainByName(Map<String, Object> hashmap);
    
     
      
    
    /**
     * 删除ip分配域名
     * @param alerts  
     */
    int deleteAllocateDomainById(String id); 
    
    
    
    /**
     * 查询分页数量
     * @param   
     */
    int getDomainCount(Map<String, Object> hashmap);
    
   
    /**
     * 查询分页数据
     * @param   
     */
    List<AllocateDomain> getDomainData(Map<String, Object> hashmap);
    
    
    
     

}

package com.aspire.ums.cmdb.allocate.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

 
import com.aspire.ums.cmdb.allocate.entity.AllocateDomain;
import com.aspire.ums.cmdb.allocate.entity.AllocateNetsegment;

 
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
public interface AllocateNetsegmentMapper {
	
    /**
     * 新增ip分配网段
     * @param allocateDomain ip分配域名
     */
    int insertAllocateNetsegment(AllocateNetsegment allocateNetsegment);
    
    /**
     * 查询ip分配网段
     * @param  
     */

    List<AllocateNetsegment> selectAllocateNetsegmentBydomainId(String domainId );
    
    
    /**
     * 删除ip分配网段
     * @param   
     */
    int deleteAllocateNetsegmentByDomainId(String domainId); 
    
     
    
    /**
     * 删除网段根据id
     * @param   
     */
    int deleteAllocateNetsegmentById(String id);
    
    
    
    /**
     * 查询网段下的ip数
     * @param   
     */
    int getIPAddressCountByNetsegId(String netSegid);
    
    
    
    /**
     * 查询网段根据名称
     * @param domain 域名 
     */
    List<AllocateNetsegment>  selectAllocateNetsegmentByName(Map<String, Object> hashmap);
    
    
    
    
     

}

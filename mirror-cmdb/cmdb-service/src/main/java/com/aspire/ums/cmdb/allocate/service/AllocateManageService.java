package com.aspire.ums.cmdb.allocate.service;

 
import java.util.List;

import com.aspire.ums.cmdb.allocate.entity.AllocateDomain;
import com.aspire.ums.cmdb.allocate.entity.AllocateNetsegment;
import com.aspire.ums.cmdb.allocate.payload.AllocateManagePageRequest;
import com.aspire.ums.cmdb.allocate.payload.AllocateManagePageResp;
import com.aspire.ums.cmdb.allocate.payload.Menu;
import com.aspire.ums.cmdb.common.PageBean;
 

/**
 * ip分配域名业务层接口
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.ums.cmdb.allocate.service
 * 类名称:    AllocateDomainService.java
 * 类描述:    ip分配域名业务层接口
 * 创建人:    JinSu
 * 创建时间:  2018/9/14 15:55
 * 版本:      v1.0
 */
public interface AllocateManageService {
	
	
    /**
     * 创建域名
     */
    int insertAllocateDomain(AllocateDomain allocateDomain, String networkSegment);
    
    
    /**
     * 修改域名
     */
    int updateAllocateDomain(AllocateDomain allocateDomain,String networkSegment);
    
    
    
    /**
     * 查询域名根据id
     *
     * @param id 域名id
     * @return AllocateDomain  域名对象
     */
    public  AllocateDomain  selectAllocateDomainById(String id);
    
    /**
     * 查询域名通过名称
     *
     * @param domain 域名
     * @return AllocateDomain  域名对象
     */
    public  AllocateDomain  selectAllocateDomainByName(String hostnet, String domain);
    
    /**
     * 查询网段通过名称
     */
    public AllocateNetsegment selectAllocateNetsegmentByName(String hostnet, String netseg);
    
    
  
    
    /**
     * 删除ip分配域名
     */
    int deleteAllocateDomainById(String id); 
    
    
    
    /**
     * 分页查询域名
     */
    PageBean<AllocateManagePageResp> selectAllocateManageByPage( AllocateManagePageRequest netSegmentPageRequest);
    
   
    /**
     * 获取菜单
     */
    public  List<Menu> selectMenu();
    
    
}

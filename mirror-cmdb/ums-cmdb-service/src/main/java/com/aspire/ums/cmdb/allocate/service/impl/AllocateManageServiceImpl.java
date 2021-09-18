package com.aspire.ums.cmdb.allocate.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.allocate.entity.AllocateDomain;
import com.aspire.ums.cmdb.allocate.entity.AllocateNetsegment;
import com.aspire.ums.cmdb.allocate.entity.Menu;
import com.aspire.ums.cmdb.allocate.mapper.AllocateDomainMapper;
import com.aspire.ums.cmdb.allocate.mapper.AllocateNetsegmentMapper;
import com.aspire.ums.cmdb.allocate.mapper.MenuMapper;
import com.aspire.ums.cmdb.allocate.service.AllocateManageService;
import com.aspire.ums.cmdb.allocate.util.AllocateManagePageRequest;
import com.aspire.ums.cmdb.allocate.util.AllocateManagePageResp;
import com.aspire.ums.cmdb.allocate.util.PageBean;
import com.aspire.ums.cmdb.module.entity.Module;

import lombok.extern.slf4j.Slf4j;

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
@Slf4j
@Service
@Transactional
public class AllocateManageServiceImpl implements AllocateManageService {
	
    @Autowired
    private AllocateDomainMapper allocateDomainMapper;
    
    @Autowired
    private AllocateNetsegmentMapper allocateNetsegmentMapper;
    
    @Autowired
    private MenuMapper menuMapper;
    

    /**
     * 插入域名
     *
     * @param alertIds 派单的告警ID列表
     */
    @Override
    @Transactional
	public int insertAllocateDomain(AllocateDomain allocateDomain,String networkSegment) {
		
    	log.info("allocateDomain is {} ",allocateDomain);
    	
    	log.info("networkSegment is {} ",networkSegment);
    	
        Map<String, Object> hashMap=new HashMap<String, Object>();
		
		hashMap.put("hostnet", allocateDomain.getHostnet());
		hashMap.put("domain", allocateDomain.getDomain());
		
		List<AllocateDomain> allocateDomainList=allocateDomainMapper.selectAllocateDomainByName(hashMap);
    	
    	if (allocateDomainList!=null&&allocateDomainList.size()>0 ) {
			
    		throw new RuntimeException("domain already exist");
    			 
		}
    	
    	int count= allocateDomainMapper.insertAllocateDomain(allocateDomain);
    	
    	String [] netSegArray=networkSegment.split(",");
    	
    	if (netSegArray!=null && netSegArray.length>0 ) {
			
	    	for (int i = 0; i < netSegArray.length; i++) {
				String netSeg = netSegArray[i];
				
				 Map<String, Object> hashMapNet=new HashMap<String, Object>();
					
				 hashMapNet.put("hostnet", allocateDomain.getHostnet());
				 hashMapNet.put("netsegment", netSeg);
				 
				 
				 List<AllocateNetsegment> AllocateNetsegmentList=allocateNetsegmentMapper.selectAllocateNetsegmentByName(hashMapNet);
					
				 if (AllocateNetsegmentList!=null&&AllocateNetsegmentList.size()>0 ) {
						
			    	throw new RuntimeException("Netsegment already exist");
			    			 
				 }   
					
				AllocateNetsegment allocateNetsegment=new AllocateNetsegment();
				allocateNetsegment.setHostnet(allocateDomain.getHostnet());
				allocateNetsegment.setDomainId(allocateDomain.getId());
				allocateNetsegment.setNetworkSegment(netSeg);
	 	 		
				allocateNetsegmentMapper.insertAllocateNetsegment(allocateNetsegment);
					 
			}
    	
    	}
    	
    	return  count;
    		 
	}
    
    /**
     * 修改域名
     *
     * @param alertIds 派单的告警ID列表
     */
    @Override
    @Transactional
	public int updateAllocateDomain(AllocateDomain allocateDomain, String networkSegment) {
    	
        log.info("allocateDomain is {} ",allocateDomain);
    	
    	log.info("networkSegment is {} ",networkSegment);
    	
        Map<String, Object> hashMapExist=new HashMap<String, Object>();
		
        hashMapExist.put("hostnet", allocateDomain.getHostnet());
        hashMapExist.put("domain", allocateDomain.getDomain());
		
	    
	    List<AllocateDomain> allocateDomainList=allocateDomainMapper.selectAllocateDomainByName(hashMapExist);
    	
	    if (allocateDomainList!=null && allocateDomainList.size()>0) {
	    	
	    	 for (AllocateDomain  AllocateDomainTemp : allocateDomainList) {
	    		 
	    		 if (  !AllocateDomainTemp.getId().equals(allocateDomain.getId())) {
	    			 
	    			 throw new RuntimeException("update domain already exist");
	    			 
	    		 } 
  
			 }
			
		}
	    
    	int count= allocateDomainMapper.updateAllocateDomain(allocateDomain);
    	
    	String [] netSegArray=networkSegment.split(",");
    	
    	List<String> newList= Arrays.asList(netSegArray);
    	
    	List<String> oldList= new ArrayList<String>();
    	
    	List<AllocateNetsegment> allocateNetsegments= allocateNetsegmentMapper.selectAllocateNetsegmentBydomainId(allocateDomain.getId() );
					 
		 for (AllocateNetsegment allocateNetsegment : allocateNetsegments) {
		 	
			 oldList.add(allocateNetsegment.getNetworkSegment());
		    	
		 }
		 
		log.info("oldList is {} ",oldList);
		
		log.info("newList is {} ",newList);
			
		for ( String oldNetwork : oldList  ){
			
			if ( !newList.contains(oldNetwork)) {
			
				 for (AllocateNetsegment allocateNetsegment : allocateNetsegments) {
					 	
					 if (allocateNetsegment.getNetworkSegment().equals(oldNetwork)) {
						 
						 int countip=allocateNetsegmentMapper.getIPAddressCountByNetsegId(allocateNetsegment.getId());
						 
						 if(countip >0){
						 
							 throw new RuntimeException("network have ipaddress");
						 }else{
							 
						  allocateNetsegmentMapper.deleteAllocateNetsegmentById(allocateNetsegment.getId());
						 
						 
						 }
						 
						 
					 } 
				   	
				 }
			 	
			}
			
		}
		
       for ( String newNetwork : newList  ){
			
			if ( !oldList.contains(newNetwork)) {
				
				Map<String, Object> hashMapNet=new HashMap<String, Object>();
				
				 
				hashMapNet.put("hostnet", allocateDomain.getHostnet());
				hashMapNet.put("netsegment", newNetwork);
				 
				List<AllocateNetsegment> AllocateNetsegmentList=allocateNetsegmentMapper.selectAllocateNetsegmentByName(hashMapNet);
					
				 if (AllocateNetsegmentList!=null && AllocateNetsegmentList.size()>0 ) {
						
			    	throw new RuntimeException("update Netsegment already exist");
			    			 
				 }   
				
				AllocateNetsegment allocateNetsegment=new AllocateNetsegment();
				allocateNetsegment.setHostnet(allocateDomain.getHostnet());
				allocateNetsegment.setDomainId(allocateDomain.getId());
				allocateNetsegment.setNetworkSegment(newNetwork);
				allocateNetsegmentMapper.insertAllocateNetsegment(allocateNetsegment);
				
			}
			
		}
    	
    	return count;
	}


  //查询单个域名根据id
  	@Override
  	public  AllocateDomain  selectAllocateDomainById(String id) {
  		
        log.info("id is {} ",id);
    	  
  		AllocateDomain  allocateDomain=allocateDomainMapper.selectAllocateDomainById(id);
  		
  		
  		return allocateDomain;
  		
  	}
    

    //查询网络下单个域名
	@Override
	public  AllocateDomain  selectAllocateDomainByName(String hostnet, String domain) {
		
		log.info("hostnet is {} ",hostnet);
		
		log.info("domain is {} ",domain);
		
		Map<String, Object> hashMap=new HashMap<String, Object>();
		
		hashMap.put("hostnet", hostnet);
		hashMap.put("domain", domain);
		
	   List<AllocateDomain> allocateDomainList=allocateDomainMapper.selectAllocateDomainByName(hashMap);
		
	   AllocateDomain allocateDomain=null;
	   
	   if (allocateDomainList!=null && allocateDomainList.size()>0) {
		   allocateDomain=allocateDomainList.get(0);
	   }
	   
	   
	   return allocateDomain;
		
	}
	
	//查询网络下单个网段
	@Override
	public AllocateNetsegment selectAllocateNetsegmentByName(String hostnet, String netseg) {
        
		log.info("hostnet is {} ",hostnet);
		
		log.info("netseg is {} ",netseg);
		
        Map<String, Object> hashMap=new HashMap<String, Object>();
		
		hashMap.put("hostnet", hostnet);
		hashMap.put("netsegment", netseg);
		
		List<AllocateNetsegment>  allocateNetsegmentList=allocateNetsegmentMapper.selectAllocateNetsegmentByName(hashMap);
		
		AllocateNetsegment allocateNetsegment=null;
		   
	   if (allocateNetsegmentList!=null && allocateNetsegmentList.size()>0) {
		   allocateNetsegment=allocateNetsegmentList.get(0);
		   
	   }
		   
		
		return allocateNetsegment;
	}

	

    //删除域名
	@Override
	@Transactional
	public int deleteAllocateDomainById(String id) {
		 
		log.info("id is {} ",id);
		
		int count= allocateDomainMapper.deleteAllocateDomainById(id);
		
		allocateNetsegmentMapper.deleteAllocateNetsegmentByDomainId(id);
		
		return count;
		
	}

	  
	
	//分页查询域名
	@Override
	public PageBean<AllocateManagePageResp> selectAllocateManageByPage(AllocateManagePageRequest netSegmentPageRequest) {
		 
		log.info("netSegmentPageRequest is {} ",netSegmentPageRequest);
		
		 PageBean<AllocateManagePageResp> pageBean=new PageBean<AllocateManagePageResp>();
		
		 Map<String, Object> hashMap=new HashMap<String, Object>();
		 
		 int pageSize=Integer.valueOf(netSegmentPageRequest.getPageSize());
		 int pageNo=Integer.valueOf(netSegmentPageRequest.getPageNo());
		 
		 hashMap.put("pageNo", (pageNo - 1) * pageSize);
		 hashMap.put("pageSize", pageSize);
		 
		 hashMap.put("systemId", netSegmentPageRequest.getSystemId());
		 hashMap.put("hostnet", netSegmentPageRequest.getHostnet());
				 
		 String ipaddress=netSegmentPageRequest.getIpadress();    
		 
		 String domain=netSegmentPageRequest.getDomain();
		 
		//判断条件是否存在
		if(ipaddress != null && !"".equals(ipaddress)){
			hashMap.put("ipadress", ipaddress);
		}
		
		//判断条件是否存在
		if(domain != null && !"".equals(domain)){
			hashMap.put("domain", domain);
		}
		
		int count=allocateDomainMapper.getDomainCount(hashMap);
		
		List<AllocateDomain> netSegmentList=allocateDomainMapper.getDomainData(hashMap);
		
		List<AllocateManagePageResp> netSegmentPageRespList=new ArrayList<AllocateManagePageResp>();
		
		for ( AllocateDomain allocateDomain : netSegmentList ){
			
			AllocateManagePageResp allocateManagePageResp=new AllocateManagePageResp();
			 
			
	    	BeanUtils.copyProperties(allocateDomain, allocateManagePageResp);
			 
	    	allocateManagePageResp.setNetworkSegment(allocateDomain.getNetSegment());
  
	    	allocateManagePageResp.setAllocateSum(allocateDomain.getSum());
			
			netSegmentPageRespList.add(allocateManagePageResp);
			
		}
	 
		
		pageBean.setCount (count);
		
		pageBean.setResult(netSegmentPageRespList);
		
		return pageBean;
	}

	//获取菜单
	@Override
	public List<Menu> selectMenu() {
		 
	   return menuMapper.selectMenu(); 
		
		
	}

	
 
	
	
}

package com.aspire.ums.cmdb.allocate.web;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.ums.cmdb.allocate.entity.AllocateDomain;
import com.aspire.ums.cmdb.allocate.entity.AllocateNetsegment;
import com.aspire.ums.cmdb.allocate.entity.Menu;
import com.aspire.ums.cmdb.allocate.service.AllocateManageService;
import com.aspire.ums.cmdb.allocate.util.AllocateDomainRequest;
import com.aspire.ums.cmdb.allocate.util.AllocateDomainResp;
import com.aspire.ums.cmdb.allocate.util.AllocateManagePageRequest;
import com.aspire.ums.cmdb.allocate.util.AllocateManagePageResp;
import com.aspire.ums.cmdb.allocate.util.AllocateNetSegmentResp;
import com.aspire.ums.cmdb.allocate.util.PageBean;
import com.aspire.ums.cmdb.module.entity.Module;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: DomainController
 * Date:     2019/3/12 15:43
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
 
@RestController
@Slf4j
@RequestMapping("/v1/cmdb/allocate")
public class AllocateManageController   {
	
	 
	
    @Autowired
    private AllocateManageService  allocateManageService;
   
    
    /**
     *  新增域名
     * @return 模型列表
     */
    @PostMapping(value = "/insertDomain" )
    public String insertDomain(@RequestBody AllocateDomainRequest allocateDomainRequest) {
         
    	log.info("allocateDomainRequest is {} ",allocateDomainRequest);
    	 
    	AllocateDomain allocateDomain=new AllocateDomain();
    	BeanUtils.copyProperties(allocateDomainRequest, allocateDomain);
    	
        String networkSegment=allocateDomainRequest.getNetworkSegment();
    	
    	allocateManageService.insertAllocateDomain(allocateDomain,networkSegment);
    	
    	return "success";
    	
    }
    
    /**
     *  查询域名通过id
     * @return 域名
     */
    @GetMapping(value = "/selectDomainById" )
    public AllocateDomainResp selectAllocateDomainById( @RequestParam("id") String id ) {
        
    	log.info("id is {} ",id);
    	
    	AllocateDomain allocateDomain=allocateManageService.selectAllocateDomainById(id);
    	AllocateDomainResp allocateDomainResp=new AllocateDomainResp();
    	BeanUtils.copyProperties(allocateDomain, allocateDomainResp);
    	allocateDomainResp.setNetworkSegment(allocateDomain.getNetSegment());
        return allocateDomainResp;
    }
     
    /**
     *  查询单个域名通过名称
     * @return 域名
     */
    @GetMapping(value = "/selectDomainByName" )
    public AllocateDomainResp selectAllocateDomainByName( @RequestParam("hostnet") String hostnet, @RequestParam("domain") String domain ) {
    	
    	log.info("hostnet is {} ",hostnet);
    	log.info("domain is {} ",domain);
    	
    	AllocateDomain allocateDomain=allocateManageService.selectAllocateDomainByName(hostnet,domain);
    	
    	AllocateDomainResp allocateDomainResp=new AllocateDomainResp();
    	
    	if (allocateDomain!=null) {
    		
    		BeanUtils.copyProperties(allocateDomain, allocateDomainResp);
		}
    	
    	
        return allocateDomainResp;
    }
    
    /**
     *  查询单个网段通过名称
     * @return 域名
     */
    @GetMapping(value = "/selectNetsegmentByName" )
    public AllocateNetSegmentResp selectAllocateNetsegmentByName( @RequestParam("hostnet") String hostnet, @RequestParam("netseg") String netseg ) {
    	
    	log.info("hostnet is {} ",hostnet);
    	log.info("netseg is {} ",netseg);
    	
    	AllocateNetsegment allocateNetsegment=allocateManageService.selectAllocateNetsegmentByName(hostnet, netseg);
    	
    	AllocateNetSegmentResp allocateNetSegmentResp=new AllocateNetSegmentResp();
    	
    	if (allocateNetsegment!=null) {
    			
    		BeanUtils.copyProperties(allocateNetsegment, allocateNetSegmentResp);
		}
    		
        return allocateNetSegmentResp;
    }
    
    
    
    /**
     *  修改域名
     * @return 模型列表
     */
    @PostMapping(value = "/updateDomain" )
    public String updateDomain(@RequestBody AllocateDomainRequest allocateDomainRequest) {
    	
    	log.info("allocateDomainRequest is {} ",allocateDomainRequest); 
    	
    	AllocateDomain allocateDomain=new AllocateDomain();
    	BeanUtils.copyProperties(allocateDomainRequest, allocateDomain);
    	
        String networkSegment=allocateDomainRequest.getNetworkSegment();
    	
    	allocateManageService.updateAllocateDomain (allocateDomain,networkSegment);
    	
    	return "success";
    	
    }
     
    
    /**
     *  删除域名
     * @return 模型列表
     */
    @DeleteMapping(value = "/deleteDomain" )
    public String deleteDomain( @RequestParam("id") String id ) {
    	
    	log.info("id is {} ",id);
    	
    	allocateManageService.deleteAllocateDomainById(id);
    	
    	return "success";  
    	
    }
    

   
    /**
     *  分页查询域名数据
     * @return 模型列表
     */
    @PostMapping(value = "/listDomaineByPage" )  
    public PageBean<AllocateManagePageResp> selectAllocateManageByPage( @RequestBody AllocateManagePageRequest allocateManagePageRequest ) {
        
    	log.info("allocateManagePageRequest is {} ",allocateManagePageRequest);
    	
        return allocateManageService.selectAllocateManageByPage(allocateManagePageRequest);
        
    }
    
    //获取菜单
    @GetMapping(value = "/selectAllocateMenu" )
    public  List<Menu> selectMenu(){
    	
    	return allocateManageService.selectMenu();
    	
    }
    
    
    
}

package com.aspire.mirror.composite.service.cmdb;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.service.cmdb.payload.CompAllocateDomainRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompAllocateDomainResp;
import com.aspire.mirror.composite.service.cmdb.payload.CompAllocateManagePageRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompAllocateManagePageResp;
import com.aspire.mirror.composite.service.cmdb.payload.CompAllocateNetSegmentResp;
import com.aspire.mirror.composite.service.cmdb.payload.Menu;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * TODO
 * <p>
 * 项目名称:   mirror平台
 * 包:        com.aspire.mirror.composite.service.cmdb
 * 类名称:    ICompAllocateManageService.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/9/20 10:14
 * 版本:      v1.0
 */
@Api(value = "Ip分配管理")
@RequestMapping("${version}/cmdb/allocate")
public interface ICompAllocateManageService {
	
	
	/**
     * 新增域名
     * @return String 返回  
     */
    @PostMapping(value = "/insertDomain")
    @ApiOperation(value = "新增域名", notes = "新增域名", tags = {"allocateManage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    public String insertDomain(@RequestBody CompAllocateDomainRequest compAllocateDomainRequest);
    
    
    /**
     * 修改域名
     * @return String 返回  
     */
    @PostMapping(value = "/updateDomain")
    @ApiOperation(value = "修改域名", notes = "修改域名", tags = {"allocateManage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    public String updateDomain(@RequestBody CompAllocateDomainRequest compAllocateDomainRequest);
    
    
    /**
     *  查询域名通过id
     * @return 域名
     */
    @GetMapping(value = "/selectDomainById" )
    @ApiOperation(value = "查询域名", notes = "查询域名通过id", tags = {"allocateManage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    public CompAllocateDomainResp selectAllocateDomainById( @RequestParam("id") String id );
    
    
    
    /**
     *  查询域名通过名称
     * @return 域名
     */
    @GetMapping(value = "/selectDomainByName" )
    @ApiOperation(value = "查询域名", notes = "查询域名", tags = {"allocateManage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    public CompAllocateDomainResp selectAllocateDomainByName( @RequestParam("hostnet") String hostnet,@RequestParam("domain") String domain );
    
    
    /**
     *  查询网段通过名称
     * @return 域名
     */
    @GetMapping(value = "/selectNetsegmentByName" )
    @ApiOperation(value = "查询网段", notes = "查询网段", tags = {"allocateManage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    public CompAllocateNetSegmentResp selectAllocateNetsegmentByName(@RequestParam("hostnet") String hostnet, @RequestParam("netseg") String netseg );
    
     
    
    /**
     *  删除域名
     * @return 域名
     */
    @DeleteMapping(value = "/deleteDomain" )
    @ApiOperation(value = "删除域名", notes = "删除域名", tags = {"allocateManage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    public String deleteDomain( @RequestParam("id") String id );
    
    
    
    /**
     *  分页查询网段数据
     * @return 域名
     */
    @PostMapping(value = "/listDomaineByPage" )  
    @ApiOperation(value = "查询分页数据", notes = "查询分页数据", tags = {"allocateManage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    public PageResponse<CompAllocateManagePageResp> selectAllocateManageByPage( @RequestBody  CompAllocateManagePageRequest compAllocateManagePageRequest ) ;
    
    
    
     //获取菜单
    @GetMapping(value = "/selectAllocateMenu" )
    @ApiOperation(value = "获取菜单", notes = "获取菜单", tags = {"allocateManage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    public  List<Menu> selectMenu();
    	
    	 
    
    
    
    
    

}

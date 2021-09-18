package com.aspire.cdn.esdatawrap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.cdn.esdatawrap.biz.domain.DomainMapCpname;
import com.aspire.cdn.esdatawrap.biz.domain.DomainMapCpname.DomainMapCpnameQueryModel;
import com.aspire.cdn.esdatawrap.common.GeneralResponse;
import com.aspire.cdn.esdatawrap.common.PageListQueryResult;
import com.aspire.cdn.esdatawrap.config.DomainMapCpNameHolder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/** 
 *
 * 项目名称: esdatawrap 
 * <p/>
 * 
 * 类名: BaseDataManageController
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年5月25日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@RestController
@Api(value = "基础数据维护")
@RequestMapping("/esdatawrap/basedata")
@ConditionalOnExpression("!'none'.equals('${spring.main.web-application-type}')")
public class BaseDataManageController {
	@Autowired
	private DomainMapCpNameHolder domainCpnameHolder;
	
	@PostMapping(value = "/queryDomainMapCpnameList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询域名cp_name映射列表", notes = "查询域名cp_name映射列表", response = DomainMapCpname.class, 
    			tags = {"Esdatawrap Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = DomainMapCpname.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public PageListQueryResult<DomainMapCpname> queryDomainMapCpnameList(@RequestBody DomainMapCpnameQueryModel queryParam) {
		return domainCpnameHolder.queryDomainMapCpnameList(queryParam);
	}
	
	@PostMapping(value = "/saveDomainMapCpname", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "保存域名和cp_name映射", notes = "保存域名和cp_name映射", response = DomainMapCpname.class, 
    				tags = {"Esdatawrap Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = DomainMapCpname.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public GeneralResponse saveDomainMapCpname(@RequestBody DomainMapCpname mapData) {
		return domainCpnameHolder.saveDomainMapCpname(mapData);
	}
	
	@DeleteMapping(value = "/deleteDomainMapCpnameById/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "删除域名和cp_name映射", notes = "删除域名和cp_name映射", response = DomainMapCpname.class, 
    		tags = {"Esdatawrap Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = DomainMapCpname.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public GeneralResponse removeDomainMapCpname(@PathVariable("id") String id) {
		return domainCpnameHolder.removeDomainMapCpname(id);
	}
}

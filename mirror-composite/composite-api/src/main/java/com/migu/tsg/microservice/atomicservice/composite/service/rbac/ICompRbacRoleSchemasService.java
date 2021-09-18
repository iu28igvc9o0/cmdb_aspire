package com.migu.tsg.microservice.atomicservice.composite.service.rbac;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.RbacRoleSchemasNameResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.RbacRoleSchemasResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
* Endpoint used to fetch role schema related data
* Project Name:composite-api
* File Name:ICompRbacRoleSchemasService.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.rbac
* ClassName: ICompRbacRoleSchemasService <br/>
* date: 2017年8月30日 上午12:19:52 <br/>
* Endpoint used to fetch role schema related data
* @author pengguihua
* @version
* @since JDK 1.6
*/
@Api(value = "Endpoint used to fetch role schema related data",
     description = "Endpoint used to fetch role schema related data")
public interface ICompRbacRoleSchemasService {
    /**
    * Get a full list of the whole schema.<br/>
    *
    * 作者： pengguihua
    * @return
    */
    @ApiOperation(value = "获取整个资源列表", notes = "获取整个资源列表",
                  response = RbacRoleSchemasResponse.class, tags = {"Composite Role schema service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "", response = RbacRoleSchemasResponse.class)})
    @GetMapping("${version}/role-schema")
    @ResponseStatus(HttpStatus.OK)
    List<RbacRoleSchemasResponse> getRoleSchemas();

    /**
     * Get a full list of the whole schema.<br/>
     *
     * 作者： pengguihua
     * @return
     */
     @ApiOperation(value = "获取操作权限列表", notes = "获取操作权限列表",
                   response = RbacRoleSchemasResponse.class, tags = {"Composite Role schema service API"})
     @ApiResponses(value = {@ApiResponse(code = 200, message = "", response = RbacRoleSchemasResponse.class)})
     @GetMapping("${version}/role-schema/{resource_type}")
     @ResponseStatus(HttpStatus.OK)
     RbacRoleSchemasResponse getRoleSchemas(@PathVariable("resource_type") String resType);
     

    @ApiOperation(value = "获取访问权限树", notes = "获取访问权限树",
              response = RbacRoleSchemasResponse.class, tags = {"Composite Role schema service API"})
 	@ApiResponses(value = {@ApiResponse(code = 200, message = "", response = RbacRoleSchemasResponse.class)})
 	@GetMapping("${version}/role-schema/tree")
 	@ResponseStatus(HttpStatus.OK)
 	List<RbacRoleSchemasNameResponse> getSchemasTree(@RequestParam(name="id", required=false, defaultValue="all") String id);

    @ApiOperation(value = "获取访问权限列表", notes = "获取访问权限列表",
             response = RbacRoleSchemasResponse.class, tags = {"Composite Role schema service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "", response = RbacRoleSchemasResponse.class)})
	@GetMapping("${version}/role-schema/child")
	@ResponseStatus(HttpStatus.OK)
	List<RbacRoleSchemasNameResponse> getRoleChildrenSchemas(@RequestParam(name = "id", required = false) String id);
}

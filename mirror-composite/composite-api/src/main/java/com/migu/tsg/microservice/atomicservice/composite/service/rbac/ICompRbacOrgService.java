package com.migu.tsg.microservice.atomicservice.composite.service.rbac;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.migu.tsg.microservice.atomicservice.composite.ErrorsResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.common.payload.BaseResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.AuthProfileResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.OrgsAssignRolesRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.OrgsAssignRolesResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.OrgsCompanyNameResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.OrgsCompanyRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.OrgsCreateSubAccountsRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.OrgsCreateSubAccountsResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.OrgsDetailResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.OrgsLogoFilesRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.OrgsPasswordRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.OrgsRolesPermissionResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.OrgsSubAccountDetailResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.OrgsSubAccountsListResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.OrgsUpdateAccountsRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.UpdateBykeycloakPayload;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RequestMapping(value = "${version}", produces="application/json")
public interface ICompRbacOrgService {
    
    //0.1 查询用户的所有信息；
    //API接口未传参数，具体哪些参数未知，先默认有username；
    @ApiOperation(value = "获取命名空间数据", notes = "获取命名空间数据",
            response =AuthProfileResponse.class, tags = {"Composite SubAccount service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Return the subAccount detail data",
            response = AuthProfileResponse.class)})
    @GetMapping(path = "/auth/{namespace}/profile",produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    AuthProfileResponse getAuthProfileDetail(
            @PathVariable("namespace") String namespace);
    
    //0.2 修改账号密码,暂时注释；
    //只限于根账号；
/*    @ApiOperation(value = "修改密码，仅限根账号", notes = "修改密码，仅限根账号",
            response = AuthProfileResponse.class, tags = {"Composite SubAccount service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Password was modified successfully",
    response = AuthProfileResponse.class)})
    @PutMapping(path = "/auth/{namespace}/profile", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    AuthProfileResponse updateAuthProfilePassword(
            @RequestBody OrgsPasswordRequest userPasswordrequest,
            @RequestParam("username") String username,
            @PathVariable("namespace") String namespace);*/
    
    //1.1
    @ApiOperation(value = "获取组织下的所有用户", notes = "获取组织下的所有用户",
            response = OrgsSubAccountsListResponse.class, tags = {"Composite SubAccount service API"})
    @ApiResponses(value = {@ApiResponse(code=200, message = "Return the sub accounts in orgs",
    response = OrgsSubAccountsListResponse.class)})
    @GetMapping(path = "/orgs/{org_name}/accounts",produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    OrgsSubAccountsListResponse getOrgsSubAccounts(
            @PathVariable("org_name") String orgName,
            @RequestParam(value = "username",required = false) String uuid,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "order_by", required = false) String orderBy,
            @RequestParam(value = "team_name_filter", required = false) String teamNameFilter,
            @RequestParam(value = "assign", required = false, defaultValue = "false") Boolean assign,
            @RequestParam(value = "page_size", required = false, defaultValue = "20") int pageSize,
            @RequestParam(value = "page", required = false, defaultValue = "1") int currentPage,
            @RequestParam(value = "project_name", required = false, defaultValue = "") String projectName);


    //1.2
    @ApiOperation(value = "在组织下添加用户",notes= "在组织下添加用户",
            response = OrgsCreateSubAccountsResponse.class,tags = {"Composite SubAccount service API"})
    @ApiResponses(value = {@ApiResponse(code = 201,message = "Returned when the creation was successful",
                                        response = OrgsCreateSubAccountsResponse.class),
                           @ApiResponse(code = 400, message = "Returned when one of the users could not be created",
                                        response = ErrorsResponse.class)})
    @PostMapping(path="/orgs/{org_name}/accounts/{user_id}", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.CREATED)
    List<OrgsCreateSubAccountsResponse> createOrgsSubAccounts(
            @PathVariable("org_name") String orgName,
            @PathVariable("user_id") String userId,
            @RequestBody OrgsCreateSubAccountsRequest req);

    @ApiOperation(value = "修改全量账户信息",notes= "修改全量账户信息",
            response = BaseResponse.class,tags = {"Composite SubAccount service API"})
    @ApiResponses(value = {@ApiResponse(code = 201,message = "Returned when the creation was successful",
                                        response = BaseResponse.class),
                           @ApiResponse(code = 400, message = "Returned when one of the users could not be created",
                                        response = ErrorsResponse.class)})
    @PostMapping(path="/orgs/{username}/accounts/{user_id}/update", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.CREATED)
    BaseResponse updateOrgsSubAccounts(
            @PathVariable("username") String username,
            @PathVariable("user_id") String userId,
            @RequestBody OrgsUpdateAccountsRequest req);

    //2.1
    @ApiOperation(value = "获取特定用户的详细信息", notes = "获取特定用户的详细信息",
            response =OrgsSubAccountDetailResponse.class, tags = {"Composite SubAccount service API"})
    @ApiResponses(value = {@ApiResponse(code = 200,message = "Return subaccount detail successful",
            response = OrgsSubAccountDetailResponse.class)})
    @GetMapping(path = "/orgs/{org_name}/accounts/{username}", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    OrgsSubAccountDetailResponse getSubAccountDetail(
            @PathVariable("org_name") String orgName,
            @PathVariable("username") String username);

    //2.2
    @ApiOperation(value = "修改子账号密码", notes = "修改子账号密码",tags = {"Composite SubAccount service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "Returned when the update was successful")})
    @PutMapping(path = "/orgs/{org_name}/accounts/{username}", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    BaseResponse updateSubAccountPassword(
            @PathVariable("org_name") String orgName,
            @PathVariable("username") String username,
            @RequestBody OrgsPasswordRequest passwordRequest);

    @ApiOperation(value = "通过keycloak修改(账号启用、禁用、解锁等)", notes = "通过keycloak修改(账号启用、禁用、解锁等)",tags = {"Composite SubAccount service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "Returned when the update was successful")})
    @PutMapping(path = "/orgs/{org_name}/keycloak/{username}", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    BaseResponse updateByKeycloak(@RequestBody UpdateBykeycloakPayload updateByKeycloakrequst);

    //2.3
    @ApiOperation(value = "删除组织中的用户", notes = "删除组织中的用户",tags = {"Composite SubAccount service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "Returned when the update was successful"),
                           @ApiResponse(code = 403,
                           message = "If not enought privilege or if it is synced via LDAP server")})
    @DeleteMapping(path = "/orgs/{org_name}/accounts/{username}", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    BaseResponse deleteSubAccount(
            @PathVariable("org_name") String orgName,
            @PathVariable("username") String username);


    //4.1
    @ApiOperation(value = "需要对角色的视图权限",notes = "需要对角色的视图权限",
            response = OrgsRolesPermissionResponse.class, tags = {"Composite SubAccount service API"})
    @ApiResponses(value = {@ApiResponse(code = 200,message = "View permission on roles successful",
            response = OrgsRolesPermissionResponse.class)})
    @GetMapping(path = "/orgs/{org_name}/accounts/{username}/roles", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    List<OrgsRolesPermissionResponse> getPermissionOnRoles(
            @PathVariable("org_name") String orgName,
            @PathVariable("username") String username);

    //4.2
    @ApiOperation(value = "分配角色给一个用户",notes = "分配角色给一个用户",
            response = OrgsAssignRolesResponse.class, tags = {"Composite SubAccount service API"})
    @ApiResponses(value = {@ApiResponse(code = 200,message = "Assign role to a user successful",
            response = OrgsAssignRolesResponse.class)})
    @PostMapping(path = "/orgs/{org_name}/accounts/{username}/roles", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    List<OrgsAssignRolesResponse> assignRoleToSubAccount(
            @PathVariable("org_name") String orgName,
            @PathVariable("username") String username,
            @RequestBody List<OrgsAssignRolesRequest> roleNames);

    @ApiOperation(value = "分配角色给一个用户(根据角色id分配)",notes = "分配角色给一个用户(根据角色id分配)",
            response = OrgsAssignRolesResponse.class, tags = {"Composite SubAccount service API"})
    @ApiResponses(value = {@ApiResponse(code = 200,message = "Assign role to a user successful",
            response = OrgsAssignRolesResponse.class)})
    @PostMapping(path = "/orgs/{org_name}/accounts/{username}/assign", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    List<OrgsAssignRolesResponse> assignRoleToAccount(
            @PathVariable("org_name") String orgName,
            @PathVariable("username") String username,
            @RequestBody List<OrgsAssignRolesRequest> roleIds);

    //5.1 /orgs/{org_name}:GET
    @ApiOperation(value = "获取一个组织详细信息",notes = "获取一个组织详细信息",
            response = OrgsDetailResponse.class, tags = {"Composite SubAccount service API"})
    @ApiResponses(value = {@ApiResponse(code = 200,message = "Get details regarding one organization successful",
            response = OrgsDetailResponse.class)})
    @GetMapping(path = "/orgs/{org_name}", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    OrgsDetailResponse retrieveOrgsDetail(@PathVariable("org_name") String orgName);

    //5.2 /orgs/{org_name}:PUT
    @ApiOperation(value = "修改公司名称或邮箱", notes = "修改公司名称",
            response = OrgsCompanyNameResponse.class, tags = {"Composite SubAccount service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "update companyName successful",
            response = OrgsCompanyNameResponse.class)})
    @PutMapping(path = "/orgs/{org_name}", produces = "application/json;charset=UTF-8")
    OrgsCompanyNameResponse companyName(
            @PathVariable("org_name") String orgName,
            @RequestBody OrgsCompanyRequest company);

    //5.3更新头像/users/{namespace}/logo:post
    @ApiOperation(value = "更新头像", notes = "更新头像",
            response = BaseResponse.class, tags = {"Composite SubAccount service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "update logo successful",
            response = BaseResponse.class)})
    @PostMapping(path = "/users/{namespace}/logo", produces = "application/json;charset=UTF-8")
    BaseResponse updateLogo(@PathVariable("namespace") String namespace,
                            @RequestBody OrgsLogoFilesRequest file);
        
    /**
    * updateSubAccount:(这里用一句话描述这个方法的作用). <br/>
    * TODO(更新用户的邮箱或者项目).<br/>
    *
    * 作者： yangshilei
    * @param namespace
    * @param username
    * @param request
    * @return
    */
//        
//    @ApiOperation(value = "修改用户邮箱或项目", notes = "修改用户邮箱或项目",
//            response = UpdateSubAccount.class, tags = {"Composite SubAccount service API"})
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "update subAccount email or project",
//            response = UpdateSubAccount.class)})
//    @PutMapping(path = "/orgs/{org_name}/accounts/{username}/detail", produces = "application/json;charset=UTF-8")
//    UpdateAccountResponse updateSubAccount(@PathVariable("org_name")String namespace,
//            @PathVariable("username")String username,
//            @RequestBody UpdateSubAccount request);

    @ApiOperation(value = "批量分配角色给用户",
            notes = "批量分配角色给用户",
            response = OrgsAssignRolesResponse.class,
            tags = { "Composite SubAccount service API" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Assign role to a user successful", response = OrgsAssignRolesResponse.class) })
    @PostMapping(path = "/orgs/user/roles/assign", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    List<OrgsAssignRolesResponse> assignUserRoles(@RequestBody OrgsUpdateAccountsRequest req);
    
    @ApiOperation(value = "批量取消分配角色给用户",
            notes = "批量取消分配角色给用户",
            response = OrgsAssignRolesResponse.class,
            tags = { "Composite SubAccount service API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "revoke role to a user successful", response = BaseResponse.class) })
    @PostMapping(path = "/orgs/user/roles/revoke", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    BaseResponse revokeUserRoles(@RequestBody OrgsUpdateAccountsRequest req);
    
}

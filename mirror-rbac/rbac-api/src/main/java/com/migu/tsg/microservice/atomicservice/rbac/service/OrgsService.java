package com.migu.tsg.microservice.atomicservice.rbac.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.migu.tsg.microservice.atomicservice.rbac.dto.*;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.FileUpload;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.service <br>
 * 类名称: OrgsService.java <br>
 * 类描述: 成员(账号)API接口 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月14日下午2:11:45 <br>
 * 版本: v1.0
 */
@Api(tags = "orgs", description = "成员管理")
public interface OrgsService {

    /**
     * 更新成员(账号)密码
     * @param request 请求对象
     * @param namespace 空间名(根账号username)
     * @param username 成员名称
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("更新成员密码")
    @PutMapping(value = "/v1/orgs/{org_name}/keycloak/{username}/password",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void updateSubAccountPassword(@RequestBody UpdateSubAccountPasswordRequest request,
            @PathVariable("org_name") String namespace, @PathVariable("username") String username);


    /**
     * 更新成员(账号)密码
     * @param request 请求对象
     * @param namespace 空间名(根账号username)
     * @param username 成员名称
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("通过keycloak修改(账号启用、禁用、解锁等)")
    @PutMapping(value = "/v1/orgs/accounts/password",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void updateByKeycloak(@RequestBody UpdateBykeycloakRequest request);
    
    /**
     * 删除成员
     * @param namespace 空间名(根账号username)
     * @param username 成员名称
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("删除成员")
    @DeleteMapping(value = "/v1/orgs/{org_name}/accounts/{username}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void removeOrgAccount(@PathVariable("org_name") String namespace,
            @PathVariable("username") String username);

    /**
     * 修改根账号信息
     * @param request 请求对象
     * @param namespace 空间名(根账号username)
     * @return 响应对象
     */
    @ApiOperation("修改根账号信息")
    @PutMapping(value = "/v1/orgs/{org_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UpdateOrgResponse updateOrg(@RequestBody UpdateOrgRequest request,
            @PathVariable("org_name") String namespace);

    /**
     * 查询根账号(组织/公司)详细信息
     * @param namespace 空间名(根账号username)
     * @return 响应对象
     */
    @ApiOperation("查询组织详细信息")
    @GetMapping(value = "/v1/orgs/{org_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public GetOrgDetailResponse getOrgDetail(@PathVariable("org_name") String namespace);

    /**
     * 查询根账号(组织/公司)中单个成员详细信息
     * @param namespace 空间名(根账号username)
     * @param username 成员名称
     * @return 响应对象
     */
    @ApiOperation("查询成员详细信息")
    @GetMapping(value = "/v1/orgs/{org_name}/accounts/{username}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public GetOrgUserDetailResponse getOrgUserDetail(@PathVariable("org_name") String namespace,
            @PathVariable("username") String username);
    
    /**
     * 查询根账号(组织/公司)中单个成员详细信息
     * @param namespace 空间名(根账号username)
     * @param username 成员名称
     * @return 响应对象
     */
    @ApiOperation("查询成员详细信息")
    @PostMapping(value = "/v1/orgs/{namespace}/hasAdminRole/{username}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public int hasAdminRole(@PathVariable("namespace") String namespace,
    		@PathVariable("username") String username);

    /**
     * 新增成员信息
     * @param request 请求对象
     * @param namespace 空间名(根账号)
     * @return 响应对象
     */
    @ApiOperation("新增成员")
    @PostMapping(value = "/v1/orgs/{org_name}/accounts", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<CreateOrgAccountResponse> createOrgAccount(@RequestBody CreateOrgAccountRequest request,
                                                           @PathVariable("org_name") String namespace);

    /**
     * 查询成员列表
     * @param namespace 空间名
     * @param teamNameFilter 按团队名称过滤用户列表
     * @param search 搜索：按名称使用“包含”算法搜索，可以使用逗号（例如test,repo）给出多个项。当前的行为不仅会被角色名称过滤，而且在所有项目之外也会被项目过滤。
     * @param orderBy 来改变默认的列表排序。选项："created_at"和"名字"做降序排序就加一个“-”符号的开始。例如-名字。
     * @param pageSize 在一个页面成功的项目数，默认为20,如果该值为非正数,则表示返回查询结果的全部数据
     * @param currentPage 选择所需页，默认为1
     * @return 响应对象
     */
    @ApiOperation("查询成员列表")
    @GetMapping(value = "/v1/orgs/{org_name}/accounts", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ListOrgAccountsResponse listOrgAccounts(@PathVariable("org_name") String namespace,
            @RequestParam(value = "team_name_filter", required = false) String teamNameFilter,
            @RequestParam(value = "username", required = false) String uuid,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "order_by", required = false, defaultValue = "-createTime") String orderBy,
            @RequestParam(value = "page_size", required = false, defaultValue = "20") int pageSize,
            @RequestParam(value = "page", required = false, defaultValue = "1") int currentPage);

    /**
     * 更新公司LOGO
     * @param request 请求对象
     * @param namespace 空间名(根账号)
     */
    @ApiOperation("上传头像")
    @PostMapping(value = { "/v1/user/update-logo/{namespace}" },
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void fileUpload(@RequestBody FileUpload request, @PathVariable("namespace") String namespace);

    /**
     * 更新成员(账号)信息
     * @param request 请求对象
     * @param namespace 空间名(根账号)
     * @param username 成员名称
     * @return 响应对象
     */
    @ApiOperation("更新成员信息")
    @PutMapping(value = "/v1/orgs/{org_name}/accounts/{username}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UpdateSubAccountResponse updateSubAccount(@RequestBody UpdateSubAccountRequest request,
            @PathVariable("org_name") String namespace, @PathVariable("username") String username);

}

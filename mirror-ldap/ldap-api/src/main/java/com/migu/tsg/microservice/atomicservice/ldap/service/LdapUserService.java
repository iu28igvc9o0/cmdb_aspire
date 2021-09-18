package com.migu.tsg.microservice.atomicservice.ldap.service;

import java.util.List;
import java.util.Map;

import com.migu.tsg.microservice.atomicservice.ldap.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.migu.tsg.microservice.atomicservice.ldap.dto.GetLdapUserResponse;
import com.migu.tsg.microservice.atomicservice.ldap.dto.InsertLdapMemberRequest;
import com.migu.tsg.microservice.atomicservice.ldap.dto.InsertLdapMemberResponse;
import com.migu.tsg.microservice.atomicservice.ldap.dto.ListPagenationResponse;
import com.migu.tsg.microservice.atomicservice.ldap.dto.UpdateLdapAdminRequest;
import com.migu.tsg.microservice.atomicservice.ldap.dto.UpdateLdapMemberRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

/**
* 项目名称: ldap-api <br>
* 包: com.migu.tsg.microservice.atomicservice.ldap.service <br>
* 类名称: LdapUserService.java <br>
* 类描述: LDAP用户接口<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年11月6日下午4:10:19 <br>
* 版本: v1.0
*/
@Api(tags = "ldap", description = "用户管理端点")
public interface LdapUserService {

    /**
     * 获取单个命名空间(根账号)信息
     * @param namespace 命名空间(根账号)
     * @return 响应对象
     */
    @ApiOperation("获取单个根账号信息")
    @GetMapping(value = "/v1/ldap/user/{namespace}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public GetLdapUserResponse getLdapAdmin(@PathVariable("namespace") String namespace);

    /**
     * 修改单个根账号信息(比如:公司名称,公司头像)
     * @param namespace 命名空间(根账号)
     * @param request 请求对象
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("修改根单个账号信息")
    @PutMapping(value = "/v1/ldap/user/{namespace}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void updateLdapAdmin(@PathVariable("namespace") String namespace,
            @RequestBody UpdateLdapAdminRequest request);

    /**
     * 获取命名空间(根账号)中的单个成员信息
     * @param namespace 命名空间(根账号)
     * @param username 用户名称
     * @return 响应对象
     */
    @ApiOperation("获取根账号中的单个成员信息")
    @GetMapping(value = "/v1/ldap/user/{namespace}/{username}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public GetLdapUserResponse getLdapMember(@PathVariable("namespace") String namespace,
            @PathVariable("username") String username);

    /**
     * 获取命名空间中(根账号)的成员信息列表
     * @param namespace 命名空间(根账号)
     * @param usernames 成员名称集合,按成员名称使用“包含”算法搜索
     * @param projects 所属项目集合,按成员名称使用“包含”算法搜索
     * @param orderBy 来改变默认的列表排序。选项："createTime"和"username"做降序排序就加一个“-”符号的开始。例如-username
     * @param pageSize 在一个页面成功的项目数,默认为20,如果该值为非正数,则表示返回查询结果的全部数据
     * @param currentPage 选择所需页，默认为1
     * @return 响应对象
     */
    @ApiOperation("获取根账号中的成员信息列表")
    @GetMapping(value = "/v1/ldap/users/{namespace}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ListPagenationResponse listLdapMember(@PathVariable("namespace") String namespace,
            @RequestParam(value = "uuids", required = false) List<String> uuids,
            @RequestParam(value = "usernames", required = false) List<String> usernames,
            @RequestParam(value = "names", required = false) List<String> names,
            @RequestParam(value = "projects", required = false) List<String> projects,
            @RequestParam(value = "order_by", required = false,
                    defaultValue = "createTime") List<String> orderBy,
            @RequestParam(value = "page_size", required = false, defaultValue = "20") int pageSize,
            @RequestParam(value = "page", required = false, defaultValue = "1") int currentPage);

    @ApiOperation("获取根账号中的成员信息列表")
    @PostMapping(value = "/v1/ldap/users-info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ListPagenationResponse listLdapMemberInfo(@RequestBody GetLdapMemberRequest memberRequest);
    /**
     * 新增命名空间(根账号)中的多个成员信息
     * @param namespace 命名空间
     * @param request 请求对象集合
     * @return 响应对象集合
     */
    @ApiOperation("新增根账号中的多个成员信息")
    @PostMapping(value = "/v1/ldap/users/{namespace}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<InsertLdapMemberResponse> insertLdapMembers(@PathVariable("namespace") String namespace,
            @RequestBody List<InsertLdapMemberRequest> request);

    /**
     * 修改命名空间(根账号)中的单个成员信息
     * @param namespace 命名空间
     * @param username 成员名称
     * @param request 请求对象
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("修改根账号中的单个成员信息")
    @PutMapping(value = "/v1/ldap/user/{namespace}/{username}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void updateLdapMember(@PathVariable("namespace") String namespace,
            @PathVariable("username") String username, @RequestBody UpdateLdapMemberRequest request);

    /**
     * 删除命名空间(根账号)中的单个成员信息
     * @param namespace 命名空间
     * @param username 成员名称
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("删除根账号中的单个成员信息")
    @DeleteMapping(value = "/v1/ldap/user/{namespace}/{username}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteLdapMember(@PathVariable("namespace") String namespace,
            @PathVariable("username") String username);

    /**
     * 获取验证码
     * @param callback 命名空间
     * @param response 成员名称
     */
    @ApiOperation("获取验证码图片")
    @GetMapping(value = "/v1/ldap/validCodeImage")
    public void getValidCodeImage(@RequestParam(value = "callback") String callback, HttpServletResponse response);

    /**
     * 校验验证码
     * @param inputCode 输入数据
     * @param validCodeKey
     * @param response response
     */
    @ApiOperation("校验验证码")
    @GetMapping(value="/v1/ldap/checkValidCode")
    public void checkValidCode(@RequestParam(value = "inputCode") String inputCode, @RequestParam(value = "validCodeKey") String validCodeKey, HttpServletResponse response);

    /**
     * 获取验证码
     */
    @ApiOperation("获取验证码")
    @GetMapping(value = "/v1/ldap/validCode")
    Map getValidCode();

    /**
     * 校验验证码
     * @param inputCode 输入数据
     * @param validCodeKey
     */
    @ApiOperation("校验验证码")
    @GetMapping(value="/v1/ldap/validCode/check")
    Map checkValidCode(@RequestParam(value = "inputCode") String inputCode, @RequestParam(value = "validCodeKey") String validCodeKey);
}

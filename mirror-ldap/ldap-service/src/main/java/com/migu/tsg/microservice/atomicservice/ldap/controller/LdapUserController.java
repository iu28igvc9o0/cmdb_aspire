package com.migu.tsg.microservice.atomicservice.ldap.controller;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.ldap.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.migu.tsg.microservice.atomicservice.ldap.annotation.ResultCode;
import com.migu.tsg.microservice.atomicservice.ldap.biz.LdapUserBiz;
import com.migu.tsg.microservice.atomicservice.ldap.service.LdapUserService;

import javax.servlet.http.HttpServletResponse;

/**
* 项目名称: ldap-service <br>
* 包: com.migu.tsg.microservice.atomicservice.ldap.controller <br>
* 类名称: LdapUserController.java <br>
* 类描述: LDAP用户控制层<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年11月6日下午4:10:59 <br>
* 版本: v1.0
*/
@RestController
public class LdapUserController implements LdapUserService {

    @Autowired
    private LdapUserBiz ldapUserBiz;

    /**
     * 获取命名空间(根账号)信息
     * @param namespace 命名空间(根账号)
     * @return 响应对象
     */
    @ResultCode("105030101")
    public GetLdapUserResponse getLdapAdmin(@PathVariable("namespace") final String namespace) {
        return ldapUserBiz.getLdapAdmin(namespace);
    }

    /**
     * 修改单个根账号信息(比如:公司名称,公司头像)
     * @param namespace 命名空间(根账号)
     * @param request 请求对象
     */
    @ResultCode("105030102")
    public void updateLdapAdmin(final @PathVariable("namespace") String namespace,
            final @RequestBody UpdateLdapAdminRequest request) {
        ldapUserBiz.updateLdapAdmin(namespace, request.getCompany(), request.getMail(),
                request.getJpegPhoto(), request.getNewPassword(), request.getOldPassword());
    }

    /**
     * 获取命名空间(根账号)中的成员信息
     * @param namespace 命名空间(根账号)
     * @param username 用户名称
     * @return 响应对象
     */
    @ResultCode("105030103")
    public GetLdapUserResponse getLdapMember(@PathVariable("namespace") final String namespace,
            @PathVariable("username") final String username) {
        return ldapUserBiz.getLdapMember(namespace, username);
    }

    /**
     * 获取命名空间中(根账号)的成员信息列表
     * @param namespace 命名空间(根账号)
     * @param usernames 成员名称集合,按成员名称使用“包含”算法搜索
     * @param projects 所属项目集合,按项目名称使用“包含”算法搜索
     * @param orderBy 来改变默认的列表排序。选项："createTime"和"username"做降序排序就加一个“-”符号的开始。例如-username
     * @param pageSize 在一个页面成功的项目数,默认为20,如果该值为非正数,则表示返回查询结果的全部数据
     * @param currentPage 选择所需页，默认为1
     * @return 响应对象
     */
    @ResultCode("105030104")
    public ListPagenationResponse listLdapMember(@PathVariable("namespace") final String namespace,
            @RequestParam(value = "uuids", required = false) List<String> uuids,
            @RequestParam(value = "usernames", required = false) final List<String> usernames,
            @RequestParam(value = "names", required = false) List<String> names,
            @RequestParam(value = "projects", required = false) List<String> projects,
            @RequestParam(value = "order_by", required = false,
                    defaultValue = "createTime,username") List<String> orderBy,
            @RequestParam(value = "page_size", required = false, defaultValue = "20") int pageSize,
            @RequestParam(value = "page", required = false, defaultValue = "1") int currentPage) {
        return ldapUserBiz.listLdapMember(namespace, uuids, usernames, names, projects, orderBy, pageSize, currentPage);
    }

    @Override
    public ListPagenationResponse listLdapMemberInfo(@RequestBody GetLdapMemberRequest memberRequest) {
        return listLdapMember
                (
                        memberRequest.getNamespace(),
                        memberRequest.getUuids(),
                        memberRequest.getUsernames(),
                        memberRequest.getNames(),
                        memberRequest.getProjects(),
                        memberRequest.getOrderBy(),
                        memberRequest.getPageSize(),
                        memberRequest.getCurrentPage()
                );
    }

    /**
     * 新增命名空间(根账号)中的多个成员信息
     * @param namespace 命名空间
     * @param request 请求对象集合
     * @return 响应对象集合
     */
    @ResultCode("105030105")
    public List<InsertLdapMemberResponse> insertLdapMembers(@PathVariable("namespace") final String namespace,
            @RequestBody final List<InsertLdapMemberRequest> request) {
        return ldapUserBiz.insertLdapMember(namespace, request);
    }

    /**
     * 修改命名空间(根账号)中的单个成员信息
     * @param namespace 命名空间
     * @param username 成员名称
     * @param request 请求对象
     */
    @ResultCode("105030106")
    public void updateLdapMember(@PathVariable("namespace") final String namespace,
            @PathVariable("username") String username, @RequestBody final UpdateLdapMemberRequest request) {
        ldapUserBiz.updateLdapMember(namespace, username, request.getMobile(), request.getMail(),
                request.getOldPassword(), request.getNewPassword(), request.getName(), request.getDept(),
                request.getProjects(),request.isUpdateTime(),request.getEmployeeType(),request.getDescription());
    }

    /**
     * 删除命名空间(根账号)中的单个成员信息
     * @param namespace 命名空间
     * @param username 成员名称
     */
    @ResultCode("105030107")
    public void deleteLdapMember(@PathVariable("namespace") final String namespace,
            @PathVariable("username") String username) {
        ldapUserBiz.deleteLdapMember(namespace, username);
    }

    /**
     * 获取验证码
     * @param callback ajax回调名称
     * @param response response
     */
    @ResultCode("105030108")
    public void getValidCodeImage(@RequestParam(value = "callback") String callback, HttpServletResponse response) {
        ldapUserBiz.getCodeImage(callback, response);
    }

    /**
     * 校验验证码
     * @param inputCode 输入数据
     * @param validCodeKey 验证码key
     * @param response response
     */
    @ResultCode("105030109")
    public void checkValidCode(@RequestParam(value = "inputCode") String inputCode, @RequestParam(value = "validCodeKey") String validCodeKey, HttpServletResponse response) {
        ldapUserBiz.checkValidCode(inputCode, validCodeKey, response);
    }

    /**
     * 获取验证码
     * @return
     */
    @ResultCode("105030110")
    public Map getValidCode() {
        return ldapUserBiz.getValidCode();
    }

    /**
     * 校验验证码
     * @param inputCode 输入数据
     * @param validCodeKey
     * @return
     */
    @ResultCode("105030111")
    public Map checkValidCode(@RequestParam(value = "inputCode") String inputCode, @RequestParam(value = "validCodeKey") String validCodeKey) {
        String message = ldapUserBiz.checkCode(inputCode, validCodeKey);
        Map<String, String> map = Maps.newHashMap();
        map.put("status", message);
        return map;
    }
}

package com.aspire.mirror.alert.server.clientservice;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.alert.server.clientservice.payload.ListPagenationResponse;
import com.aspire.mirror.alert.server.domain.GetLdapMemberRequest;

import io.swagger.annotations.ApiOperation;

@FeignClient(value = "LDAP", url = "http://10.1.203.100:28102")
public interface LdapClient {
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
    ListPagenationResponse listLdapMember(@PathVariable("namespace") String namespace,
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
}

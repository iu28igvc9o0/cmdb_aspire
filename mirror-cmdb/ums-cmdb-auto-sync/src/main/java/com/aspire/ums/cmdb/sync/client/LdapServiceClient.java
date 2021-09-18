package com.aspire.ums.cmdb.sync.client;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.common.configuration.FeignHttpClientConfiguration;
import com.migu.tsg.microservice.atomicservice.ldap.dto.InsertLdapMemberRequest;
import com.migu.tsg.microservice.atomicservice.ldap.dto.InsertLdapMemberResponse;
import com.migu.tsg.microservice.atomicservice.ldap.dto.ListPagenationResponse;
import com.migu.tsg.microservice.atomicservice.ldap.dto.UpdateLdapMemberRequest;

@FeignClient(value = "LDAP",url="http://10.1.203.100:28102" configuration = FeignHttpClientConfiguration.class)
public interface LdapServiceClient {
	
	
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
    @RequestMapping(value = "/v1/ldap/users/{namespace}", method = RequestMethod.GET)
    ListPagenationResponse listLdapMember(@PathVariable("namespace") String namespace,
            @RequestParam(value = "usernames", required = false) List<String> usernames,
            @RequestParam(value = "uuids", required = false) List<String> uuids,
            @RequestParam(value = "projects", required = false) List<String> projects,
            @RequestParam(value = "order_by", required = false,
                    defaultValue = "createTime") List<String> orderBy,
            @RequestParam(value = "page_size", required = false, defaultValue = "20") int pageSize,
            @RequestParam(value = "page", required = false, defaultValue = "1") int currentPage);
    
    
    
    
    
    /**
    * 新增命名空间(根账号)中的多个成员信息
    * @param namespace 命名空间
    * @param request 请求对象集合
    * @return 响应对象集合
    */

    @RequestMapping(value = "/v1/ldap/users/{namespace}", method = RequestMethod.POST)
   public List<InsertLdapMemberResponse> insertLdapMembers(@PathVariable("namespace") String namespace,
           @RequestBody List<InsertLdapMemberRequest> request);
    
    
    
   /**
    * 修改命名空间(根账号)中的单个成员信息
    * @param namespace 命名空间
    * @param username 成员名称
    * @param request 请求对象
    */
   
  
    @RequestMapping(value = "/v1/ldap/user/{namespace}/{username}",
		   method = RequestMethod.PUT)
   public void updateLdapMember(@PathVariable("namespace") String namespace,
           @PathVariable("username") String username, @RequestBody UpdateLdapMemberRequest request);
    
    
    
   /**
    * 删除命名空间(根账号)中的单个成员信息
    * @param namespace 命名空间
    * @param username 成员名称
    */
  
    @RequestMapping(value = "/v1/ldap/user/{namespace}/{username}",
    		method = RequestMethod.DELETE)
   public void deleteLdapMember(@PathVariable("namespace") String namespace,
           @PathVariable("username") String username);

}






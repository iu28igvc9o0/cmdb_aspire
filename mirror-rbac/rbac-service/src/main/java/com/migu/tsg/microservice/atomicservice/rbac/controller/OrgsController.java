package com.migu.tsg.microservice.atomicservice.rbac.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.migu.tsg.microservice.atomicservice.common.annotation.ResultCode;
import com.migu.tsg.microservice.atomicservice.common.config.KeycloakProperties;
import com.migu.tsg.microservice.atomicservice.ldap.dto.UpdateLdapMemberRequest;
import com.migu.tsg.microservice.atomicservice.rbac.biz.OrgsBiz;
import com.migu.tsg.microservice.atomicservice.rbac.biz.UserBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dto.CreateOrgAccountRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.CreateOrgAccountResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.GetOrgDetailResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.GetOrgUserDetailResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ListOrgAccountsResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UpdateBykeycloakRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UpdateOrgRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UpdateOrgResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UpdateSubAccountPasswordRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UpdateSubAccountRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UpdateSubAccountResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.CreateOrgAccountDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.FileUpload;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ListOrgAccountsDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.OrgDetailDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.UserDTO;
import com.migu.tsg.microservice.atomicservice.rbac.service.OrgsService;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.controller <br>
 * 类名称: OrgsController.java <br>
 * 类描述: 成员控制层 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月14日下午3:56:34 <br>
 * 版本: v1.0
 */
@RestController
public class OrgsController implements OrgsService {

    @Autowired
    private OrgsBiz orgsBiz;
    
    @Autowired
    private KeycloakProperties properties;
    

	/**
	 * service
	 */
	 @Autowired
    private UserBiz userBiz;

    /**
     * 更新成员(账号)密码
     * @param request 请求对象
     * @param namespace 空间名称
     * @param username 成员名称
     */
    @ResultCode("105010201")
    public void updateSubAccountPassword(@RequestBody final UpdateSubAccountPasswordRequest request,
            @PathVariable("org_name") final String namespace,
            @PathVariable("username") final String username) {
        orgsBiz.updateSubAccountPassword(request.getPassword(), request.getOldPassword(), namespace,
                username);
    }
    


	/* (non-Javadoc)
	 * @see com.migu.tsg.microservice.atomicservice.rbac.service.OrgsService#updateByKeycloak(com.migu.tsg.microservice.atomicservice.rbac.dto.UpdateBykeycloakRequest)
	 */
	@Override
	public void updateByKeycloak(@RequestBody UpdateBykeycloakRequest request) {
		Keycloak kcMaster = Keycloak.getInstance(properties.getUrl(), "master", properties.getUsername(), properties.getPassword(), "admin-cli");
		RealmResource realmResource = kcMaster.realm(properties.getRealm());
		 // 根据前台传入的用户名获取keycloak上的 User对象
        String username = request.getUsername();
		UserRepresentation updateUser = realmResource.users().search(username).get(0);
        Boolean enable = request.getEnable();
		updateUser.setEnabled(enable);
        UserResource userResource = realmResource.users().get(updateUser.getId());
		userResource.update(updateUser);
		UserDTO userDTO=new UserDTO();
		userDTO.setLdapId(username);
		List<UserDTO> userList=userBiz.select(userDTO);
		if(CollectionUtils.isNotEmpty(userList)) {
			UserDTO dt=userList.get(0);
			dt.setLdapStatus(enable?"1":"0");
			userBiz.updateByPrimaryKey(dt);
		}
	}

    /**
     * 删除成员
     * @param namespace 空间名称
     * @param username 成员名称
     */
    @ResultCode("105010202")
    public void removeOrgAccount(@PathVariable("org_name") final String namespace,
            @PathVariable("username") final String username) {
        orgsBiz.removeOrgAccount(namespace, username);
    }

    /**
     * 修改组织信息(公司名称,公司邮箱)
     * @param request 请求对象
     * @param namespace 空间名称
     * @return 响应对象
     */
    @ResultCode("105010203")
    public UpdateOrgResponse updateOrg(@RequestBody final UpdateOrgRequest request,
            @PathVariable("org_name") final String namespace) {
        OrgDetailDTO updateOrg = orgsBiz.updateOrg(request.getCompany(), request.getEmail(),
                request.getNewPassword(), request.getOldPassword(), namespace);
        UpdateOrgResponse response = new UpdateOrgResponse();
        BeanUtils.copyProperties(updateOrg, response);
        return response;
    }

    /**
     * 查询根账号(组织/公司)详细信息
     * @param namespace 空间名称
     * @return 响应对象
     */
    @ResultCode("105010204")
    public GetOrgDetailResponse getOrgDetail(@PathVariable("org_name") final String namespace) {
        OrgDetailDTO orgDetail = orgsBiz.getOrgDetail(namespace);
        GetOrgDetailResponse response = new GetOrgDetailResponse();
        BeanUtils.copyProperties(orgDetail, response);
        return response;
    }

    /**
     * 查询根账号(组织/公司)中单个成员详细信息
     * @param namespace 空间名(根账号)
     * @param username 成员名称
     * @return 响应对象
     */
    @ResultCode("105010205")
    public GetOrgUserDetailResponse getOrgUserDetail(@PathVariable("org_name") String namespace,
            @PathVariable("username") String username) {
        OrgDetailDTO orgDetail = orgsBiz.getOrgUserDetail(namespace, username);
        GetOrgUserDetailResponse response = new GetOrgUserDetailResponse();
        BeanUtils.copyProperties(orgDetail, response);
        return response;
    }

    /**
     * 新增成员信息
     * @param request 请求对象
     * @param namespace 空间名称
     * @return 响应对象
     */
    @ResultCode("105010206")
    public List<CreateOrgAccountResponse> createOrgAccount(@RequestBody final CreateOrgAccountRequest request,
            @PathVariable("org_name") final String namespace) {
        List<CreateOrgAccountDTO> result = orgsBiz.createOrgAccount(namespace, request.getAccounts(),
                request.getRoles(), request.getPassword());
        UserDTO userDTO =new UserDTO(); 
        userDTO.setUuid(request.getUserId());
        userDTO.setLdapId(request.getAccounts().get(0).getUsername());
        userDTO.setLdapStatus("1");
        userDTO.setNamespace(namespace);
        userDTO.setLdapPasswordUpdatetime(new Timestamp(System.currentTimeMillis()));
        userBiz.updateByPrimaryKey(userDTO);
        List<CreateOrgAccountResponse> response = result.stream().map(dto -> {
            CreateOrgAccountResponse resposne = new CreateOrgAccountResponse();
            BeanUtils.copyProperties(dto, resposne);
            return resposne;
        }).collect(Collectors.toList());
        return response;
    }

    /**
     * 查询成员列表
     * @param namespace 空间名称
     * @param teamNameFilter 按团队名称过滤用户列表(暂时没用)
     * @param search 搜索：按名称使用“包含”算法搜索，可以使用逗号（例如test,repo）给出多个项。
     * @param orderBy 来改变默认的列表排序,选项："createTime"和"username"做降序排序就加一个“-”符号的开始,可以使用逗号（例如createTime,-username）给出多个项。
     * @param pageSize 在一个页面成功的项目数，默认为20,如果该值为非正数,则表示返回查询结果的全部数据
     * @param currentPage 选择所需页，默认为1
     * @return 响应对象
     */
    @ResultCode("105010207")
    public ListOrgAccountsResponse listOrgAccounts(@PathVariable("org_name") String namespace,
            @RequestParam(value = "team_name_filter", required = false) final String teamNameFilter,
            @RequestParam(value = "username", required = false) String uuid,
            @RequestParam(value = "search", required = false) final String search,
            @RequestParam(value = "order_by", required = false,
                    defaultValue = "-createTime") final String orderBy,
            @RequestParam(value = "page_size", required = false, defaultValue = "20") final int pageSize,
            @RequestParam(value = "page", required = false, defaultValue = "1") final int currentPage) {
        ListOrgAccountsDTO dto = orgsBiz.listOrgAccounts(namespace, teamNameFilter, uuid, search, orderBy, pageSize,
                currentPage);
        ListOrgAccountsResponse response = new ListOrgAccountsResponse();
        BeanUtils.copyProperties(dto, response);
        return response;
    }

    /**
     * 更新公司LOGO
     * @param namespace 空间名(根账号)
     * @param request 请求对象
     */
    @ResultCode("105010208")
    public void fileUpload(@RequestBody final FileUpload request,
            @PathVariable("namespace") final String namespace) {
        orgsBiz.fileUpload(request.getLogoFile(), namespace);
    }

    /**
     * 更新成员(账号)信息
     * @param request 请求对象
     * @param namespace 空间名(根账号)
     * @param username 成员名称
     */
    @ResultCode("105010209")
    public UpdateSubAccountResponse updateSubAccount(@RequestBody final UpdateSubAccountRequest request,
            @PathVariable("org_name") final String namespace,
            @PathVariable("username") final String username) {
        UserDTO userDTO =new UserDTO();
        userDTO.setUuid(request.getUserId());
        userDTO.setLdapId(username);
        userDTO.setNamespace(namespace);
        userDTO.setLdapStatus("1");
        if (StringUtils.isNotEmpty(request.getNewPassword())) {
            userDTO.setLdapPasswordUpdatetime(new Timestamp(System.currentTimeMillis()));
        }
        userBiz.updateByPrimaryKey(userDTO);
    	UpdateLdapMemberRequest req=new UpdateLdapMemberRequest();
    	BeanUtils.copyProperties(request, req);
        return orgsBiz.updateSubAccount(namespace, username, req);
    }



	/**
	 * 判断是否有管理员角色
	 */
	@Override
	public int hasAdminRole(@PathVariable("namespace") String namespace,@PathVariable("username")  String username) {
		return orgsBiz.hasAdminRole(username, namespace);
	}

}

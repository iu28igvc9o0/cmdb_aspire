package com.migu.tsg.microservice.atomicservice.rbac.biz;

import com.aspire.mirror.common.util.MD5Util;
import com.migu.tsg.microservice.atomicservice.common.client.LdapServiceClient;
import com.migu.tsg.microservice.atomicservice.common.enums.BadRequestFieldMessageEnum;
import com.migu.tsg.microservice.atomicservice.common.enums.ResultErrorEnum;
import com.migu.tsg.microservice.atomicservice.common.exception.BadRequestFieldException;
import com.migu.tsg.microservice.atomicservice.common.util.RegexUtils;
import com.migu.tsg.microservice.atomicservice.ldap.dto.GetLdapUserResponse;
import com.migu.tsg.microservice.atomicservice.ldap.dto.InsertLdapMemberRequest;
import com.migu.tsg.microservice.atomicservice.ldap.dto.InsertLdapMemberResponse;
import com.migu.tsg.microservice.atomicservice.ldap.dto.ListPagenationResponse;
import com.migu.tsg.microservice.atomicservice.ldap.dto.UpdateLdapAdminRequest;
import com.migu.tsg.microservice.atomicservice.ldap.dto.UpdateLdapMemberRequest;
import com.migu.tsg.microservice.atomicservice.ldap.enums.LdapUserTypeEnum;
import com.migu.tsg.microservice.atomicservice.rbac.dao.RoleDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.RoleUsersDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.UserDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.Role;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.RoleUsers;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.User;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UpdateSubAccountResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.AccountDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.AccountRoleDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.CreateOrgAccountDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.InsertAccountDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ListOrgAccountsDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.OrgDetailDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.biz <br>
 * 类名称: OrgsBiz.java <br>
 * 类描述: 成员业务层 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月14日下午2:48:32 <br>
 * 版本: v1.0
 */
@Service
@Transactional
public class OrgsBiz {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrgsBiz.class);

    @Autowired
    private RoleUsersDao roleUsersDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private LdapServiceClient ldapServiceClient;

    @Value("${password.policy:md5}")
    private String pwdPolicy;
	@Autowired
	private UserDao userDao;

    /**
     * ","分隔符
     */
    public static final String SPLIT = ",";

    /**
     * 更新成员密码 <br>
     * 若oldPassword为null,则表示根账号修改其他成员密码,不需要验证旧密码是否一致
     * 若oldPassword不为null,则表示当前成员修改自己密码,需要验证旧密码是否一致
     * @param password 新密码
     * @param oldPassword 旧密码
     * @param namespace 空间名称/根账号
     * @param username 成员名称
     */
    public void updateSubAccountPassword(final String password, final String oldPassword,
            final String namespace, final String username) {
        UpdateLdapMemberRequest request = new UpdateLdapMemberRequest();
        request.setNewPassword(password);
        request.setOldPassword(oldPassword);
        request.setUpdateTime(true);
        ldapServiceClient.updateLdapMember(namespace, username, request);
        LOGGER.info("method[updateSubAccountPassword] Update ldap user {} password successfully", username);
    }
    
    /**
     * <p>
     * 判断是否有管理员角色
     * </p>
     * @author 曾祥华
     * @version 0.1 2019年4月12日
     * @param username
     * @param namespace
     * @return
     * int
     */
    public int hasAdminRole(String username, String namespace) {
    	return roleUsersDao.hasAdminRole(username, namespace);
    }

    /**
     * 删除成员
     * @param namespace 空间名称
     * @param username 成员名称
     */
    public void removeOrgAccount(final String namespace, final String username) {
        ldapServiceClient.deleteLdapMember(namespace, username);
        // 删除角色成员中间表关联关系
        roleUsersDao.deleteRoleUsers(null, namespace, username);
        User u=new User();
        u.setLdapId(username);
        List<User> ulist=userDao.queryList(u);
        for(User user:ulist) {
        	User uu=new User();
        	uu.setUuid(user.getUuid());
        	uu.setLdapId("");
        	uu.setLdapStatus("-1");
        	userDao.updateByPrimaryKey(uu);
        }
        LOGGER.info("method[removeOrgAccount] Delete ldap user {} successfully", username);
    }

    /**
     * 修改公司信息(公司名称,公司邮箱)
     * @param company 公司/组织名称
     * @param mail 公司邮箱
     * @param newPassword 新密码
     * @param oldPassword 旧密码
     * @param namespace 空间名/根账号
     * @return DTO对象
     */
    public OrgDetailDTO updateOrg(final String company, final String mail, final String newPassword,
                                  final String oldPassword, final String namespace) {
        //        if (StringUtils.isBlank(company)) {
        //            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "company",
        //                    new String[] { BadRequestFieldMessageEnum.COMPANY_NAME_CANNOT_BE_EMPTY.getMessage() });
        //        }
        UpdateLdapAdminRequest request = new UpdateLdapAdminRequest();
        request.setCompany(company);
        request.setMail(mail);
        request.setNewPassword(newPassword);
        request.setOldPassword(oldPassword);
        ldapServiceClient.updateLdapAdmin(namespace, request);
        LOGGER.info("method[updateOrg] Update ldap user {} success", namespace);
        OrgDetailDTO dto = getOrgDetail(namespace);
        return dto;
    }

    /**
     * 更新成员(账号)信息
     * @param namespace 空间名(根账号)
     * @param username 成员名称
     * @param request 请求参数
     * @return 响应对象
     */
    public UpdateSubAccountResponse updateSubAccount(final String namespace, final String username,
                                                     UpdateLdapMemberRequest request) {
        if (!StringUtils.isEmpty(request.getNewPassword())) {
            if("md5".equalsIgnoreCase(pwdPolicy)) {
                request.setNewPassword(MD5Util.MD5Encode(request.getNewPassword(), null));
            }
        }
        ldapServiceClient.updateLdapMember(namespace, username, request);
        GetLdapUserResponse ldapUser = ldapServiceClient.getLdapMember(namespace, username);
        UpdateSubAccountResponse usResponse = new UpdateSubAccountResponse();
        if (ldapUser != null) {
            usResponse.setEmail(ldapUser.getMail());
            usResponse.setProjects(ldapUser.getProjects());
        }
        LOGGER.info("method[updateSubAccount] Update ldap user {} success", username);
        return usResponse;

    }

    /**
     * 查询组织详细信息
     * @param namespace 空间名/根账号
     * @return DTO对象
     */
    public OrgDetailDTO getOrgDetail(final String namespace) {
        GetLdapUserResponse ldapOrg = ldapServiceClient.getLdapAdmin(namespace);
        OrgDetailDTO dto = packageNamespaceInfo(ldapOrg);
        LOGGER.info("method[getOrgDetail] Query ldap org {} success", namespace);
        return dto;
    }

    /**
     * 查询组织中成员详细信息
     * @param namespace 空间名/根账号
     * @param username 成员名称
     * @return DTO对象
     */
    public OrgDetailDTO getOrgUserDetail(final String namespace, final String username) {
        GetLdapUserResponse ldapUser = ldapServiceClient.getLdapMember(namespace, username);
        OrgDetailDTO dto = packageNamespaceInfo(ldapUser);
        LOGGER.info("method[getOrgUserDetail] Query ldap user {} success", username);
        return dto;
    }

    /**
     * 新增成员信息
     * @param namespace 空间名称/根账号
     * @param accounts 成员集合
     * @param roles 角色集合
     * @param password 密码
     * @return DTO对象
     */
    public List<CreateOrgAccountDTO> createOrgAccount(final String namespace,
                                                      final List<InsertAccountDTO> accounts, final List<AccountRoleDTO> roles, final String password) {
        validateParameterRoles(roles);
        boolean isAdmin = validateParameterRolesAccessDB(roles);
        List<InsertLdapMemberRequest> users = new ArrayList<>();
        for (InsertAccountDTO dto : accounts) {
            InsertLdapMemberRequest request = new InsertLdapMemberRequest();
            if("md5".equalsIgnoreCase(pwdPolicy)) {
                request.setPassword(MD5Util.MD5Encode(password, null)); // 必填
            } else {
                request.setPassword(password);
            }

            request.setUsername(dto.getUsername());// 必填
            request.setMail(dto.getEmail());
            request.setMobile(dto.getMobile());
            request.setProjects(dto.getProjects());
            if (StringUtils.isNotEmpty(dto.getDescription())) {
                request.setDescription(dto.getDescription());
            }
            
            if (isAdmin) {
                request.setType(LdapUserTypeEnum.admin.toString());
            } else {
                request.setType(dto.getType());
            }
            users.add(request);
        }
        List<InsertLdapMemberResponse> insertLdapUsers = ldapServiceClient.insertLdapMembers(namespace,
                users);
        List<CreateOrgAccountDTO> result = insertLdapUsers.stream().map(input -> {
            return new CreateOrgAccountDTO(input.getUsername(), input.getPassword(), input.getMail(),
                    input.getProjects());
        }).collect(Collectors.toList());
        List<String> usernames = insertLdapUsers.stream().map(InsertLdapMemberResponse::getUsername)
                .collect(Collectors.toList());
        // 新增成员时,允许不关联角色
        if (CollectionUtils.isNotEmpty(roles)) {
            List<RoleUsers> roleUsersList = new ArrayList<>();
            for (String username : usernames) {
                for (AccountRoleDTO accountRoleDTO : roles) {
                    roleUsersList
                            .add(new RoleUsers(accountRoleDTO.getUuid(), null, namespace, username, null));
                }
            }
            if (CollectionUtils.isNotEmpty(roleUsersList)) {
                // 新增成员和角色的关联关系
                roleUsersDao.createRoleUsersBatch(roleUsersList);
            }
        }
        LOGGER.info("method[createOrgAccount] Insert ldap users {} success", usernames);
        return result;
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
    public ListOrgAccountsDTO listOrgAccounts(final String namespace, final String teamNameFilter, final String uuid,
                                              final String search, final String orderBy, final int pageSize, final int currentPage) {
        ListOrgAccountsDTO dto = new ListOrgAccountsDTO();
        ListPagenationResponse listLdapUser = ldapServiceClient.listLdapMember(namespace,
                StringUtils.isNotBlank(uuid) ?  Arrays.asList(uuid.split(",")) : null,
                StringUtils.isNotBlank(search) ? Arrays.asList(search.split(",")) : null, null,null,
                StringUtils.isNotBlank(orderBy) ? Arrays.asList(orderBy.split(",")) : null, pageSize,
                currentPage);
        if (listLdapUser != null) {
            dto.setCount(listLdapUser.getRowCount());
            dto.setPageSize(listLdapUser.getPageSize());
            dto.setNumPages(listLdapUser.getCurrentPage());
            dto.setNext(listLdapUser.getNext());
            dto.setPrevious(listLdapUser.getPrev());
            List<AccountDTO> results = listLdapUser.getResults().stream().map(input -> {
//                return new AccountDTO(input.getUsername(), input.getUserType(),input.getDescription(),
//                		input.getCreateTime(), input.getCreateTime(), input.getProjects());
                return new AccountDTO(input.getUsername(), input.getUserType(),input.getMobile(),input.getDescription(), input.getCreateTime(),
                        input.getCreateTime(), input.getName(), input.getProjects());
            }).collect(Collectors.toList());
            dto.setResults(results);// 成员集合
        }
        LOGGER.info("method[listOrgAccounts] Query ldap users success");
        return dto;
    }

    /**
     * 更新公司logo
     * @param logoFile logo流对象字符串
     * @param namespace 空间名称
     * @throws IOException 异常
     */
    public void fileUpload(final String logoFile, final String namespace) {
        if (StringUtils.isBlank(logoFile)) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "logo_file",
                    new String[] { BadRequestFieldMessageEnum.LOGO_FILE_IS_INVALID.getMessage() });
        }
        UpdateLdapAdminRequest request = new UpdateLdapAdminRequest();
        request.setJpegPhoto(logoFile);
        ldapServiceClient.updateLdapAdmin(namespace, request);
        LOGGER.info("method[fileUpload] Update ldap user {} company LOGO success", namespace);
    }

    /**
     * 访问数据库验证角色是否存在
     * @param roles 角色集合
     */
    private boolean validateParameterRolesAccessDB(final List<AccountRoleDTO> roles) {
        // 新增成员时,允许不关联角色
        boolean isAdmin = false;
        if (CollectionUtils.isNotEmpty(roles)) {
            int index = 0;
            for (AccountRoleDTO accountRoleDTO : roles) {
                // 验证角色是否存在,不存在则抛出资源不存在异常
                Role role = roleDao.getRoleByUUID(accountRoleDTO.getUuid());
                if (role == null) {
                    throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,
                            "roles." + index + ".uuid",
                            new String[] { BadRequestFieldMessageEnum.ROLE_NOT_EXIST.getMessage() });
                }
                if (role.getAdminRole() == 1) {
                    isAdmin = true;
                }
            }
        }
        return isAdmin;
    }

    /**
     * 验证角色UUID是否合法和角色集合中不能有重复的角色名称
     * @param roles 角色集合
     * @throws BadRequestFieldException 请求字段不合法的异常
     */
    private void validateParameterRoles(final List<AccountRoleDTO> roles) throws BadRequestFieldException {
        // 新增成员时,允许不关联角色
        if (CollectionUtils.isNotEmpty(roles)) {
            Set<String> setRoleUuid = new HashSet<>();
            int index = 0;
            for (AccountRoleDTO accountRoleDTO : roles) {
                // 验证roleUuid是否为合法的UUID字符串
                if (!RegexUtils.hasMatchesRegexUuids(accountRoleDTO.getUuid())) {
                    throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,
                            "roles." + index + ".uuid",
                            new String[] { BadRequestFieldMessageEnum.dynamicMessage(
                                    BadRequestFieldMessageEnum.UUID_REQUEST_INVALID,
                                    accountRoleDTO.getUuid()) });
                }
                // 角色集合中不能含有重复的角色
                if (setRoleUuid.contains(accountRoleDTO.getUuid())) {
                    throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,
                            "roles." + index + ".name",
                            new String[] { BadRequestFieldMessageEnum.dynamicMessage(
                                    BadRequestFieldMessageEnum.DUPLICATED_ROLENAME_IN_THE_PAYLOAD,
                                    accountRoleDTO.getName()) });
                }
                setRoleUuid.add(accountRoleDTO.getUuid());
            }
        }
    }

    /**
     * 封装根账号(namesapce)DTO对象
     * @param user LDAP用户对象
     * @return DTO对象
     */
    private OrgDetailDTO packageNamespaceInfo(final GetLdapUserResponse user) {
        OrgDetailDTO dto = new OrgDetailDTO();
        if (user != null) {
            dto.setCompany(user.getCompany());
            dto.setCreatedAt(user.getCreateTime());
            dto.setUpdatedAt(user.getUpdateTime());
            dto.setUsername(user.getUsername());
            dto.setName(user.getName());
            dto.setLogoFile(user.getJpegPhoto());
            dto.setProjects(user.getProjects());
            dto.setMobile(user.getMobile());
            dto.setEmail(user.getMail());
            dto.setUserType(user.getUserType());
            dto.setDescription(user.getDescription());
        }
        return dto;
    }

}

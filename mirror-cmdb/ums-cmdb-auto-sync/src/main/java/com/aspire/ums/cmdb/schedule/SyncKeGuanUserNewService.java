package com.aspire.ums.cmdb.schedule;

import com.aspire.mirror.common.util.DateUtil;
import com.aspire.ums.cmdb.common.StringUtils;
import com.aspire.ums.cmdb.sync.client.LdapServiceClient;
import com.aspire.ums.cmdb.sync.client.UserServiceClient;
import com.aspire.ums.cmdb.sync.util.Md5Util;
import com.aspire.ums.cmdb.vo.KgLdapUser;
import com.aspire.ums.cmdb.vo.KgLdapUserMapper;
import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.ldap.dto.GetLdapUserResponse;
import com.migu.tsg.microservice.atomicservice.ldap.dto.InsertLdapMemberRequest;
import com.migu.tsg.microservice.atomicservice.ldap.dto.ListPagenationResponse;
import com.migu.tsg.microservice.atomicservice.ldap.dto.UpdateLdapMemberRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserBatchCreateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserCreateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserUpdateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.PagedResultsControl;
import javax.naming.ldap.PagedResultsResponseControl;
import java.util.*;

/**
 * @Author: huanggongrui
 * @Description: 同步科管部用户到CMDB定时任务
 * @Date: create in 2020/7/21 18:29
 */
@Component
@Slf4j
@ConditionalOnExpression("${schedule.kgSync.flag:false}")
public class SyncKeGuanUserNewService {

    @Value("${ldapconfig.namespace:alauda}")
    private String namespace;
    private static final String USER_TYPE = "user.alauda";
    @Value("${kgSync.user.roleId:4bf87b9a-b885-431f-a891-b9440c0fd1d3}")
    private String userRoleId;
    @Value("${kgSync.user.deptId:1001}")
    private String deptId;
    @Value("${kgSync.user.initPwd:1234qwery}")
    private String initPwd;

    @Autowired
    private LdapTemplate ldapTemplate;

    @Autowired
    private UserServiceClient userClient;

    @Resource
    private LdapServiceClient ldapUserServiceClient;

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    @Async
    @Scheduled(cron = "${kgSync.user.cron: 0 2 * * * ?}")
    public void syncUser() {
        // get user data from ldap server
        log.info("研究院用户同步开始");
        List<KgLdapUser> kgLdapUserList = getUserListFromLdap();
        if (!CollectionUtils.isEmpty(kgLdapUserList)) {
            syncEpcUserToOsa(kgLdapUserList);
            log.info("研究院用户同步完毕");
        } else {
            log.info("研究院无用户需同步");
        }
    }

    /**
     * 从科管ldap获取ldap用户, 分页查找用户
     * @since 2020-08-17 15:22:22
     * @return
     */
    private List<KgLdapUser> getUserListFromLdap() {
        List<KgLdapUser> rtnList = Lists.newArrayList();
        try {
//            List<KgLdapUser> rtn11List = ldapTemplate.search("ou=users,dc=cmri", "(&(objectClass=cmcc-abstractPerson))", SearchControls.ONELEVEL_SCOPE, new KgLdapUserMapper());
            SearchControls schCtrls = new SearchControls();
            //指定检索范围
            /*
             * 0:OBJECT_SCOPE,搜索指定的命名对象。
             * 1:ONELEVEL_SCOPE,只搜索指定命名对象的一个级别，这是缺省值。
             * 2:SUBTREE_SCOPE,搜索以指定命名对象为根结点的整棵树  **/
            schCtrls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            int pageSize = 500;
            byte[] cookie = null;
            ContextSource contextSource = ldapTemplate.getContextSource();
            DirContext ctx = contextSource.getReadWriteContext();
            LdapContext lCtx = (LdapContext) ctx;
            //分页
            lCtx.setRequestControls(new Control[] { new PagedResultsControl(  pageSize, Control.CRITICAL) });
            int totalResults = 0;
            KgLdapUserMapper kgLdapUserMapper = new KgLdapUserMapper();
            do {
                //搜索的结果的枚举属性
                NamingEnumeration<SearchResult> results = lCtx.search("ou=users,dc=cmri", "(&(objectClass=cmcc-abstractPerson))", schCtrls);
                while (null != results && results.hasMoreElements()) {//结果不为空且有值
                    SearchResult sr = results.next();
                    Attributes attrs = sr.getAttributes();
                    KgLdapUser kgLdapUser = kgLdapUserMapper.mapFromAttributes(attrs);
                    rtnList.add(kgLdapUser);
                    totalResults++;
                }
                //cookie是一个字节数组，包含了通过PagedResultsControl下一次调用服务器时所需的信息
                cookie = parseControls(lCtx.getResponseControls());
                lCtx.setRequestControls(new Control[] { new PagedResultsControl(  pageSize, cookie, Control.CRITICAL) });
            } while ((cookie != null) && (cookie.length != 0));
            lCtx.close();
            log.info("Total = " + totalResults);

            log.info("同步研究院ldap用户总数: {}", rtnList.size());
        } catch (Exception e) {
            log.error("获取研究院ldap数据失败", e);
        }
        return rtnList;
    }

    private byte[] parseControls(Control[] controls)
            throws NamingException {
        byte[] cookie = null;
        if (controls != null) {
            for (int i = 0; i < controls.length; i++) {
                if (controls[i] instanceof PagedResultsResponseControl) {
                    PagedResultsResponseControl prrc = (PagedResultsResponseControl) controls[i];
                    cookie = prrc.getCookie();
                    System.out.println(">>Next Page \n");
                }
            }
        }
        return (cookie == null) ? new byte[0] : cookie;
    }

    private InsertLdapMemberRequest convertToAnotherLdap(KgLdapUser user) {
        mapperFactory.classMap(KgLdapUser.class, InsertLdapMemberRequest.class)
                .field("cn", "name")
                .byDefault().register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        InsertLdapMemberRequest request = mapper.map(user, InsertLdapMemberRequest.class);
        request.setUsername(user.getUid());
        request.setType(USER_TYPE);
        request.setDept(user.getO());
        request.setPassword(StringUtils.isEmpty(user.getUserPassword()) ? initPwd:user.getUserPassword());
        request.setDescription("uim " + user.getRole() + " user");
        return request;
    }

    private UserCreateRequest convertLdapUser(KgLdapUser domain) {
        mapperFactory.classMap(KgLdapUser.class, UserCreateRequest.class)
                .field("cn", "name")
                .field("uid", "code")
                .field("uid", "ldapId")
                .field("gender", "sex")
                // .field("telephoneNumber", "phone")
                .byDefault().register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        UserCreateRequest userData = mapper.map(domain, UserCreateRequest.class);
        String id = Md5Util.getMD5String(domain.getUid());
        String o = StringUtils.isEmpty(domain.getO())?deptId : domain.getO();
        userData.setDeptId(o);
        List<String> oList = Lists.newArrayList();
        oList.add(o);
        userData.setDeptIds(oList);
        userData.setRoles(userRoleId);
        userData.setDescr("uim " + domain.getRole() + " user");
        userData.setNamespace(namespace);
        userData.setUserType(1);
        userData.setUserId(id);
        return userData;
    }

    private void syncEpcUserToOsa(List<KgLdapUser> ldapDataList) {
        List<String> projectss = new ArrayList();
        List<String> orderBy = new ArrayList();
        int pageSize = 200000;
        int currentPage = 1;

        log.info("SyncCmicUserData: filter cmic users data start");
        List<InsertLdapMemberRequest> addList = new ArrayList<>();
        List<UserCreateRequest> userCreateRequestList = Lists.newArrayList();

        ListPagenationResponse response = ldapUserServiceClient.listLdapMember(namespace, null, null, projectss, orderBy,
                pageSize, currentPage);
        List<GetLdapUserResponse> results = response.getResults();
        log.info("SyncCmicUserData listLdapMember");
        if (results.size() > 0) {
            boolean flag;
            for (KgLdapUser ldapData: ldapDataList) {
                flag = false;
                String userName = ldapData.getUid();
                for (GetLdapUserResponse result: results) {
                    if (!StringUtils.isEmpty(userName) && userName.equals(result.getUsername())) {
                        flag = true;
                        String password = ldapData.getUserPassword();
                        String mail = ldapData.getMail();
                        String mobile = ldapData.getMobile();
                        String o = ldapData.getO();
                        String cn = ldapData.getCn();
                        if ((!StringUtils.isEmpty(password) && !password.equals(result.getPassword())) ||
                                (!StringUtils.isEmpty(mail) && !mail.equals(result.getMail())) ||
                                (!StringUtils.isEmpty(mobile) && !mobile.equals(result.getMobile())) ||
                                (!StringUtils.isEmpty(cn) && !cn.equals(result.getName())) ||
                                (!StringUtils.isEmpty(o) && !o.equals(result.getDept()))) {
                            UpdateLdapMemberRequest request = new UpdateLdapMemberRequest();
                            request.setOldPassword(result.getPassword());
                            request.setNewPassword(password);
                            request.setMail(mail);
                            request.setMobile(mobile);
                            request.setUpdateTime(true);
                            request.setDept(o);
                            request.setName(cn);
                            request.setDescription("uim " + ldapData.getRole() + " user at " + DateUtil.formatDate(DateUtil.DATE_TIME_CH_FORMAT));
                            log.info("update 研究院用户, username is : {}", userName);
                            // 修改
                            try {
                                ldapUserServiceClient.updateLdapMember(namespace, result.getUsername(), request);
                                UserVO userVO = userClient.findByLdapId(userName);
//                                String id = Md5Util.getMD5String(userName);
//                                List<UserVO> userVOList = userClient.listByPrimaryKeyArrays(id);
                                if (userVO != null && !StringUtils.isEmpty(userVO.getUuid())) {
                                    UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
                                    userUpdateRequest.setMail(mail);
                                    userUpdateRequest.setMobile(mobile);
                                    userUpdateRequest.setDeptId(o);
                                    userUpdateRequest.setName(cn);
                                    List<String> oList = Lists.newArrayList();
                                    oList.add(o);
                                    userUpdateRequest.setDeptIds(oList);
                                    userUpdateRequest.setDescr("update at " + DateUtil.formatDate(DateUtil.DATE_TIME_CH_FORMAT));
                                    userClient.modifyByPrimaryKey(userVO.getUuid(), userUpdateRequest);
                                } else {
                                    userCreateRequestList.add(convertLdapUser(ldapData));
                                }
                            } catch (Exception e) {
                                log.error("更新失败 {}", e);
                            }
                        }
                    }
                }
                if (!flag) {
                    addList.add(convertToAnotherLdap(ldapData));
                    userCreateRequestList.add(convertLdapUser(ldapData));
                }
            }
        } else {
            for (KgLdapUser ldapData: ldapDataList) {
                addList.add(convertToAnotherLdap(ldapData));
                userCreateRequestList.add(convertLdapUser(ldapData));
            }
        }

        // 批量新增
        try {
            if (!addList.isEmpty()) {
                ldapUserServiceClient.insertLdapMembers(namespace, addList);
            }
            if (!userCreateRequestList.isEmpty()) {
                UserBatchCreateRequest user = new UserBatchCreateRequest();
                user.setListUser(userCreateRequestList);
                userClient.batchCreatedUser(user);
            }
        } catch (Exception e) {
            log.error("批量插入用户错误， {}", e);
            // 失败回滚
            if (!addList.isEmpty()) {
                for (InsertLdapMemberRequest user: addList) {
                    ldapUserServiceClient.deleteLdapMember(namespace, user.getUsername());
                }
            }
        }

        boolean flag;
        for (GetLdapUserResponse result: results) {
            String userName = result.getUsername();
            String description = result.getDescription();
            flag = false;
            for (KgLdapUser ldapData : ldapDataList) {
                String kgUserName = ldapData.getUid();
                if (!StringUtils.isEmpty(userName) && userName.equals(kgUserName)) {
                    flag = true;
                    break;
                }
            }

            if (!flag && (!StringUtils.isEmpty(description) && description.startsWith("uim"))) {
                try {
                    ldapUserServiceClient.deleteLdapMember(namespace, userName);
                    UserVO userVO = userClient.findByLdapId(userName);
                    if (userVO != null && !StringUtils.isEmpty(userVO.getUuid())) {
                        userClient.deleteByPrimaryKey(userVO.getUuid());
                    }
                } catch (Exception e) {
                    log.error("删除用户错误，用户名：{}，{}", userName, e);
                    try {
                        InsertLdapMemberRequest insertLdapMemberRequest = new InsertLdapMemberRequest();
                        insertLdapMemberRequest.setDept(result.getDept());
                        insertLdapMemberRequest.setDescription(result.getDescription());
                        insertLdapMemberRequest.setMail(result.getMail());
                        insertLdapMemberRequest.setMobile(result.getMobile());
                        insertLdapMemberRequest.setName(result.getName());
                        insertLdapMemberRequest.setPassword(result.getPassword());
                        insertLdapMemberRequest.setProjects(result.getProjects());
                        insertLdapMemberRequest.setUsername(result.getUsername());
                        List<InsertLdapMemberRequest> list = Lists.newArrayList();
                        ldapUserServiceClient.insertLdapMembers(namespace, list);
                    } catch (Exception e1) {
                        log.error("回滚用户错误，用户名：{}，{}", userName, e);
                    }

                }

            }
        }
    }
}

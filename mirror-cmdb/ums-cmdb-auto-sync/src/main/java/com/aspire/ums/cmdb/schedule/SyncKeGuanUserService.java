package com.aspire.ums.cmdb.schedule;

import com.aspire.mirror.common.entity.PageResult;
import com.aspire.ums.cmdb.sync.client.LdapServiceClient;
import com.aspire.ums.cmdb.sync.client.RbacServiceClient;
import com.aspire.ums.cmdb.sync.payload.CmdbOptType;
import com.aspire.ums.cmdb.sync.util.HttpUtils;
import com.aspire.ums.cmdb.sync.util.JsonMapper;
import com.aspire.ums.cmdb.sync.util.Md5Util;
import com.aspire.ums.cmdb.vo.UserData;
import com.aspire.ums.cmdb.vo.UserVo;
import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.ldap.dto.InsertLdapMemberRequest;
import com.migu.tsg.microservice.atomicservice.ldap.dto.UpdateLdapMemberRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserBatchCreateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserCreateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserPageRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserUpdateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.UserDTO;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @Author: huanggongrui
 * @Description: 同步科管部用户到CMDB定时任务
 * @Date: create in 2020/7/21 18:29
 */
@Component
@Slf4j
@ConditionalOnExpression("${kgSync.user.syncFlag:false}")
public class SyncKeGuanUserService {

    // TODO 有test标志的上线前改改
    private static final String DEFAULT_NAMESPACE = "alauda";
    private static final String ID_PREFIX = "kg_";
    private static final String USER_EXTERNAL = "external";
    private static final String USER_GUEST = "guest";
    private static final String USER_INTERNAL = "internal";
    private static boolean firstTimeFlag = true;
    @Value("${kgSync.user.group: osn-users}")
    private String groupName; // test: osn-users
    @Value("${kgSync.user.url: http://10.1.5.179/rest/uim/person/group/}")
    private String userSyncUrl;
    @Value("${kgSync.user.roleId: f1def95e-fc74-4981-a8e0-977c55d2fa00}")
    private String userRoleId;

    @Autowired
    private RbacServiceClient userClient;

    @Resource
    private LdapServiceClient ldapUserServiceClient;

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    @Async
//    @Scheduled(cron = "${kgSync.user.cron: 0 0/20 * * * ?}")
    // @ConditionalOnProperty(name = "kgSync.user.syncFlag", havingValue = "true")
//    @ConditionalOnExpression("${kgSync.user.syncFlag:false}")
    public void syncUser() {
        List<UserData> userList = getUserList();
        // for test
        // List<UserData> userList = getUserListFromFile();

        if (!CollectionUtils.isEmpty(userList)) {
            List<UserData> addList = Lists.newArrayList();
            List<UserData> updateList = Lists.newArrayList();
            List<UserData> delList = Lists.newArrayList();
            for (int i = 0; i < userList.size(); i++) {
                // changeFlag: 1是新增，2是更新，3是删除
                UserData userData = userList.get(i);
                userData.setUserName(ID_PREFIX + userData.getUserName());
                if (firstTimeFlag) { // 第一次全部同步
                    userData.setChangeFlag(1);
                    addList.add(userData);
                    continue;
                }
                if (userData.getChangeFlag() == 1) {
                    String id = Md5Util.getMD5String(userData.getUserName());
                    try { // 非第一次同步新增用户判断是否已存在
                        if (userClient.findByPrimaryKey(id) == null) {
                            addList.add(userData);
                        }
                    } catch (Exception e) {
                        log.error("由id获取用户信息失败", e);
                    }
                } else if (userData.getChangeFlag() == 2) {
                    updateList.add(userData);
                } else if (userData.getChangeFlag() == 3) {
                    delList.add(userData);
                }
            }
            log.info("科管部用户同步,待新增的数量:[{}]", addList.size());
            log.info("科管部用户同步,待修改的数量:[{}]", updateList.size());
            log.info("科管部用户同步,待删除的数量:[{}]", delList.size());
            dealWithUser(addList, CmdbOptType.OPT_ADD);
            dealWithUser(updateList, CmdbOptType.OPT_MODIFY);
            dealWithUser(delList, CmdbOptType.OPT_DEL);
        } else {
            log.info("科管部用户同步,无用户需同步");
        }
    }

    /**
     * 处理入库逻辑
     *
     * @param list
     * @param optType
     */
    private void dealWithUser(List<UserData> list, CmdbOptType optType) {
        if (!CollectionUtils.isEmpty(list)) {
            UserBatchCreateRequest createRequest = new UserBatchCreateRequest();
            List<UserCreateRequest> userList = Lists.newArrayList();
            for (UserData domain : list) {
                UserCreateRequest request = convert(domain);
                userList.add(request);
            }
            createRequest.setListUser(userList);
            if (CmdbOptType.OPT_ADD == optType) {
                userClient.batchCreatedUser(createRequest);
                addLdapUser(list);
            }
            if (CmdbOptType.OPT_MODIFY == optType) {
                list.forEach(e -> {
                    UserUpdateRequest updateRequest = convert4Update(e);
                    userClient.modifyByPrimaryKey(Md5Util.getMD5String(e.getUserName()), updateRequest);
                });
                updateLdapUser(list);
            }
            if (CmdbOptType.OPT_DEL == optType) {
                userList.forEach(e -> userClient.deleteByPrimaryKey(e.getUserId()));
                deleteLdapUser(list);
            }
        }
    }

    private UserCreateRequest convert(UserData domain) {
        mapperFactory.classMap(UserData.class, UserCreateRequest.class)
                .field("zhcnName", "name")
                .field("userName", "code")
                .field("email", "mail")
                .field("userName", "ldapId")
                .field("gender", "sex")
                .byDefault().register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        UserCreateRequest request = mapper.map(domain, UserCreateRequest.class);
        String id = Md5Util.getMD5String(domain.getUserName());
        request.setUserId(id);
        request.setNamespace(DEFAULT_NAMESPACE);
        request.setUserType(1);
        request.setDescr("科管部" + domain.getAvatar() + "用户");
        request.setRoles(userRoleId);
        return request;
    }

    private void addLdapUser(List<UserData> list) {
        List<InsertLdapMemberRequest> request = Lists.newArrayList();
        list.forEach(e -> {
            request.add(convertLdap(e));
        });
        ldapUserServiceClient.insertLdapMembers(DEFAULT_NAMESPACE, request);
    }

    private InsertLdapMemberRequest convertLdap(UserData entity) {
        mapperFactory.classMap(UserData.class, InsertLdapMemberRequest.class)
                .field("zhcnName", "name")
                .field("userName", "username")
                .field("email", "mail")
                .byDefault().register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        InsertLdapMemberRequest request = mapper.map(entity, InsertLdapMemberRequest.class);
        request.setType("user.alauda");
        request.setProjects(Arrays.asList("miguums_p1"));
        return request;
    }

    private void updateLdapUser(List<UserData> list) {
        list.forEach(e -> {
            UpdateLdapMemberRequest request = convertLdap4Update(e);
            ldapUserServiceClient.updateLdapMember(DEFAULT_NAMESPACE, e.getUserName(), request);
        });
    }

    private UpdateLdapMemberRequest convertLdap4Update(UserData entity) {
        mapperFactory.classMap(UserData.class, UpdateLdapMemberRequest.class)
                .field("zhcnName", "name")
                .field("email", "mail")
                .byDefault().register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        UpdateLdapMemberRequest request = mapper.map(entity, UpdateLdapMemberRequest.class);
        return request;
    }

    private void deleteLdapUser(List<UserData> list) {
        list.forEach(e -> ldapUserServiceClient.deleteLdapMember(DEFAULT_NAMESPACE, e.getUserName()));
    }

    private UserUpdateRequest convert4Update(UserData entity) {
        mapperFactory.classMap(UserData.class, UserUpdateRequest.class)
                .field("userName", "code")
                .field("userName", "ldapId")
                .field("email", "mail")
                .byDefault().register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        UserUpdateRequest request = mapper.map(entity, UserUpdateRequest.class);
        request.setNamespace(DEFAULT_NAMESPACE);
        request.setUserType(1);
        request.setUuid(Md5Util.getMD5String(entity.getUserName()));
        return request;
    }

    /**
     * 获取科管部用户
     *
     * @return
     */
    private List<UserData> getUserList() {
        UserPageRequest userPageRequest = new UserPageRequest();
        userPageRequest.setDescr("科管用户");
        userPageRequest.setPageNo(1);
        userPageRequest.setPageSize(10);
        PageResult<UserDTO> pageRtn = userClient.pageList(userPageRequest);
        List<UserData> allUser = Lists.newArrayList();
        // userSyncUrl = "http://10.1.5.179/rest/uim/person/group/";
        log.info("同步科管部{}组用户请求: {}", groupName, userSyncUrl + groupName);
        Object resp = null;
        try {
            if (pageRtn.getCount() > 0) {
                firstTimeFlag = false;
                long time = LocalDateTime.now().minusDays(2).toEpochSecond(ZoneOffset.of("+8"));
                resp = HttpUtils.httpGet(userSyncUrl + groupName+"?t=" + time);
            } else { // first time to sync user
                firstTimeFlag = true;
                resp = HttpUtils.httpGet(userSyncUrl + groupName);
            }
        } catch (Exception e) {
            log.error("同步科管部{}组用户异常: {}", groupName, e);
        }
        log.info("同步科管部{}组用户请求返回结果: {}", groupName, resp);
        UserVo userVo = null;
        try {
            if (resp != null)
                userVo = new JsonMapper().readValue(resp.toString(), UserVo.class);
        } catch (IOException e) {
            log.error("同步科管部请求返回结果解析异常", e);
        }
        if (userVo != null) {
            if (!CollectionUtils.isEmpty(userVo.getExternalPerson())) {
                for (UserData userData : userVo.getExternalPerson()) {
                    userData.setAvatar(USER_EXTERNAL);
                }
                allUser.addAll(userVo.getExternalPerson());

            }
            if (!CollectionUtils.isEmpty(userVo.getGuest())) {
                for (UserData userData : userVo.getGuest()) {
                    userData.setAvatar(USER_GUEST);
                }
                allUser.addAll(userVo.getGuest());
            }
            if (!CollectionUtils.isEmpty(userVo.getInternalPerson())) {
                for (UserData userData : userVo.getInternalPerson()) {
                    userData.setAvatar(USER_INTERNAL);
                }
                allUser.addAll(userVo.getInternalPerson());
            }
        }
        return allUser;
    }
}

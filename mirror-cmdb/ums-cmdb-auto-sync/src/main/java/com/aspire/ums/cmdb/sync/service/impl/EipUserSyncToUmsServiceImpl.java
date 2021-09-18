package com.aspire.ums.cmdb.sync.service.impl;

import com.aspire.ums.cmdb.sync.client.LdapServiceClient;
import com.aspire.ums.cmdb.sync.client.RbacServiceClient;
import com.aspire.ums.cmdb.sync.entity.User;
import com.aspire.ums.cmdb.sync.service.EipSyncService;
import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.ldap.dto.InsertLdapMemberRequest;
import com.migu.tsg.microservice.atomicservice.ldap.dto.UpdateLdapMemberRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserBatchCreateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserCreateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service("eipUserSyncToUmsService")
public class EipUserSyncToUmsServiceImpl implements EipSyncService<User> {

    private static final Logger logger = LoggerFactory.getLogger(EipUserSyncToUmsServiceImpl.class);

    @Autowired
    private LdapServiceClient ldapServiceClient;
    @Autowired
    private RbacServiceClient rbacServiceClient;

    @Value("${ldapconfig.namespace:alauda}")
    private String namespace;

    @Value("${rabc.sysadmin.roleid}")
    private String ROLE_ID;

    /**
     * 更新用户权限信息
     * @param list
     */
    private void rbacInsert(List<User> list) {
        List<UserCreateRequest> userCreateRequestList = Lists.newArrayList();
        for (User user : list) {
            UserCreateRequest rbacUser = new UserCreateRequest();
            rbacUser.setLdapId(user.getLoginName());
            rbacUser.setNamespace(namespace);
            rbacUser.setName(user.getName());
            // 设置用户类型为正式用户
            rbacUser.setUserType(1);
            rbacUser.setMail(user.getEmail());
            rbacUser.setMobile(user.getMobile());
            rbacUser.setDeptId(user.getDeptId());
            rbacUser.setRoles(ROLE_ID);
            rbacUser.setCode(user.getLoginName());
            userCreateRequestList.add(rbacUser);
        }
        logger.info("after add ldap menbers , userlist is :" + userCreateRequestList.size());
        if (!CollectionUtils.isEmpty(userCreateRequestList)) {
            UserBatchCreateRequest userBatchCreateRequest = new UserBatchCreateRequest();
            userBatchCreateRequest.setListUser(userCreateRequestList);
            rbacServiceClient.batchCreatedUser(userBatchCreateRequest);
        }
    }

    @Override
    public void add(List<User> list) {
        for (User user : list) {
            try {
                List<InsertLdapMemberRequest> request = new ArrayList<>();
                InsertLdapMemberRequest insert = new InsertLdapMemberRequest();
                // request.add((InsertLdapMemberRequest)ClassConvertUtil.convertClass(user,InsertLdapMemberRequest.class));
                insert.setMobile(user.getMobile());
                insert.setName(user.getName());
                insert.setUsername(user.getLoginName());
                insert.setMail(user.getEmail());
                insert.setPassword(user.getPassword());
                request.add(insert);
                ldapServiceClient.insertLdapMembers(namespace, request);
            } catch (Exception e) {
                logger.error("LDAP 录入EIP用户 {} 信息出错!", user.getName(), e);
            }
        }
        rbacInsert(list);
    }

    @Override
    public void modify(List<User> list) {
        for (User user : list) {
            try {
                UpdateLdapMemberRequest req = new UpdateLdapMemberRequest();
                req.setName(user.getName());
                req.setNewPassword(user.getPassword());
                req.setMail(user.getEmail());
                req.setMobile(user.getMobile());
                ldapServiceClient.updateLdapMember(namespace, user.getLoginName(), req);
            } catch (Exception e) {
                logger.error("LDAP 更新EIP用户 {} 信息出错!", user.getName(), e);
            }
        }
        rbacInsert(list);
    }

    @Override
    public void delete(List<User> list) {
        for (User user : list) {
            try {
                ldapServiceClient.deleteLdapMember(namespace, user.getLoginName());
            } catch (Exception e) {
                logger.error("LDAP 删除EIP用户 {} 信息出错!", user.getName(), e);
            }
        }
    }
}

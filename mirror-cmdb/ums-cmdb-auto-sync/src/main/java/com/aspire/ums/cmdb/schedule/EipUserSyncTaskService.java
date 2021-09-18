package com.aspire.ums.cmdb.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.aspire.ums.cmdb.sync.client.LdapServiceClient;
import com.aspire.ums.cmdb.sync.entity.EIPUser;
import com.aspire.ums.cmdb.sync.entity.Office;
import com.aspire.ums.cmdb.sync.entity.User;
import com.aspire.ums.cmdb.sync.service.EipSyncService;
import com.aspire.ums.cmdb.sync.util.HttpUtils;
import com.migu.tsg.microservice.atomicservice.ldap.dto.GetLdapUserResponse;
import com.migu.tsg.microservice.atomicservice.ldap.dto.ListPagenationResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@ConditionalOnExpression("${schedule.eipUser.flag:false}")
public class EipUserSyncTaskService {

    private static final Logger logger = LoggerFactory.getLogger(EipUserSyncTaskService.class);

    @Value("${eip.sync.user.url}")
    private String usrInfoGetUrl;
    @Autowired
    private LdapServiceClient ldapServiceClient;

    @Value("${ldapconfig.namespace:alauda}")
    private String namespace;

    @Value("${eip.sync.user.default-password:123456}")
    private String umsDefaultPassword;

    @Value("${cmic.org.user.default.user.type}")
    private static String DEFAULT_USER_TYPE;


    private static final String KEY_USERS_ADD = "addList";
    private static final String KEY_USERS_ADDLISTPROCESS = "addListProcess";
    private static final String KEY_USERS_MODI = "modiList";
    private static final String KEY_USERS_DEL = "delList";

    @Resource(name = "eipUserSyncToUmsService")
    private EipSyncService<User> eipUserSyncToUmsService;
    @Resource(name = "eipUserSyncToBpmService")
    private EipSyncService<User> eipUserSyncToBpmService;

    @Scheduled(cron = "${eip.sync.user.cron}")
    public void syncEIPUser() {
        String userInfoListStr = HttpUtils.httpGet(usrInfoGetUrl);
        if (StringUtils.isNotEmpty(userInfoListStr)) {
            ArrayList<EIPUser> userInfoList = JSON.parseObject(userInfoListStr, new TypeReference<ArrayList<EIPUser>>(){});
            if (CollectionUtils.isEmpty(userInfoList)) {
                logger.info("没有需要同步的用户数据!");
                return;
            }
            Map<String, List<User>> usersGroup = groupEipUserToHandle(userInfoList);
            // 同步到 UMS
            syncUms(usersGroup);
            // 同步到 BPM
            syncBpm(usersGroup);
        }
    }

    private void syncUms(Map<String, List<User>> usersGroup) {
        eipUserSyncToUmsService.add(usersGroup.get(KEY_USERS_ADD));
        eipUserSyncToUmsService.modify(usersGroup.get(KEY_USERS_MODI));
        eipUserSyncToUmsService.delete(usersGroup.get(KEY_USERS_DEL));
    }

    private void syncBpm(Map<String, List<User>> usersGroup) {
        eipUserSyncToBpmService.add(usersGroup.get(KEY_USERS_ADD));
        eipUserSyncToBpmService.modify(usersGroup.get(KEY_USERS_MODI));
        eipUserSyncToBpmService.delete(usersGroup.get(KEY_USERS_DEL));
    }

    private Map<String, List<User>> groupEipUserToHandle(ArrayList<EIPUser> eipUserList) {
        Map<String, List<User>> returnMap = new HashMap<>();
        List<User> addList = new ArrayList<>();
        List<User> modiList = new ArrayList<>();
        List<User> delList = new ArrayList<>();

        List<String> projects = new ArrayList();
        List<String> orderBy = new ArrayList();
        int pageSize = Integer.MAX_VALUE;
        int currentPage = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date createDate = new Date();
        String remark = sdf.format(createDate);
        String markAdd = remark + "_add";
        String markUpdate = remark + "_update";
        String markDelete = remark + "_delete";
        ListPagenationResponse response = ldapServiceClient.listLdapMember(namespace, null, null, projects, orderBy,
                pageSize, currentPage);
        List<GetLdapUserResponse> results = response.getResults();
        try {
            if (!CollectionUtils.isEmpty(results)) { // 当查出ldap数据列表非空时
                List<User> list = new ArrayList<>();
                for (GetLdapUserResponse result : results) {
                    User user = new User();
                    user.setLoginName(result.getUsername());
                    user.setMobile(result.getMobile());
                    user.setName(result.getName());
                    user.setEmail(result.getMail());
                    user.setPassword(result.getPassword());
                    user.setCreateDate(sdf.parse(result.getCreateTime()));
                    list.add(user);
                }
                Map<String, User> cnmsUserMap = new HashMap<>(list.size());
                for (User user : list) {
                    cnmsUserMap.put(user.getLoginName(), user);
                }
                Set<String> eipUserSet = new HashSet<>();
                for (EIPUser eipUser : eipUserList) {
                    String id = eipUser.getUserCode();
                    String username = eipUser.getUserLogin();
                    eipUserSet.add(username);
                    if (cnmsUserMap.containsKey(username)) { // 如果已存在该用户, 则修改 (密码不作修改)
                        User user = cnmsUserMap.get(username);
                        user.setEmail(eipUser.getMail());
                        user.setMobile(eipUser.getTel());
                        user.setName(eipUser.getUserName());
                        user.setDeptId(eipUser.getDeptId());
                        user.setRemarks(markUpdate);
                        modiList.add(user);
                    } else { // 如果不存在该用户, 则新增
                        Office o = new Office();
                        o.setId("1");
                        User user = new User(id, o, // 物理公司
                                o, // 物理部门
                                username,
                                umsDefaultPassword,
                                eipUser.getUserName(),
                                eipUser.getMail(),
                                eipUser.getTel(),
                                DEFAULT_USER_TYPE,
                                markAdd,
                                createDate
                        );
                        user.setDeptId(eipUser.getDeptId());
                        addList.add(user);
                    }
                }

                // 过滤已删除的用户
                for (GetLdapUserResponse result : results) {
                    String username = result.getUsername();
                    if (!eipUserSet.contains(username)) {
                        User user = cnmsUserMap.get(username);
                        user.setDelFlag("1");
                        user.setRemarks(markDelete);
                        delList.add(user);
                    }
                }
            } else { // 当查出ldap数据列表为空时，直接进行新增用户操作
                for (EIPUser eipUser : eipUserList) {
                    Office o = new Office();
                    o.setId("1");
                    User user = new User(eipUser.getUserCode(), o, // 物理公司
                            o, // 物理部门
                            eipUser.getUserLogin(),
                            umsDefaultPassword,
                            eipUser.getUserName(),
                            eipUser.getMail(),
                            eipUser.getTel(),
                            DEFAULT_USER_TYPE,
                            markAdd,
                            createDate
                    );
                    user.setDeptId(eipUser.getDeptId());
                    addList.add(user);
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        returnMap.put(KEY_USERS_ADD, addList);
        returnMap.put(KEY_USERS_ADDLISTPROCESS, new ArrayList<>());
        returnMap.put(KEY_USERS_MODI, modiList);
        returnMap.put(KEY_USERS_DEL, delList);
        return returnMap;
    }
}

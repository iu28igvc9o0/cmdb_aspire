package com.aspire.ums.cmdb.sync.service.impl;

import com.aspire.ums.cmdb.sync.entity.User;
import com.aspire.ums.cmdb.sync.service.EipSyncService;
import com.aspire.ums.cmdb.sync.util.UmsWebServiceUtils;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("eipUserSyncToBpmService")
public class EipUserSyncToBpmServiceImpl implements EipSyncService<User> {

    private static final Logger logger = LoggerFactory.getLogger(EipUserSyncToBpmServiceImpl.class);

    @Autowired
    private UmsWebServiceUtils umsWebServiceUtils;

    @Override
    public void add(List<User> list) {
        for (User user : list) {
            // 同步新增用户
            Map<String, Object> syscUserResult = umsWebServiceUtils.syscUserData(user,
                    UmsWebServiceUtils.UMS_USER_OPERATE_TYPE_ADD);
            if ((Boolean) syscUserResult.get("result")) {
                logger.info("sync eip user to bpm success, user name:" + user.getLoginName());
            } else {
                logger.error("sync eip user to bpm failed, user name:" + user.getLoginName());
            }
        }
    }

    @Override
    public void modify(List<User> list) {
        for (User user : list) {
            Map<String, Object> syscUserResult = umsWebServiceUtils.syscUserData(user, UmsWebServiceUtils.UMS_USER_OPERATE_TYPE_MODI);
            if ((Boolean) syscUserResult.get("result")) {
                logger.info("update eip user to bpm success, user name:" + user.getLoginName());
            } else {
                logger.error("update eip user to bpm failed, user name:" + user.getLoginName());
                if (null != syscUserResult.get("message")) {
                    String msg = syscUserResult.get("message").toString();

                    if (msg.contains("账号不存在")) {
                        logger.info("try re-add portal user to bpm , user name:" + user.getLoginName());
//                        addList.add(user);
                        List<User> users = Lists.newArrayList();
                        users.add(user);
                        add(users);
                    }
                }
            }
        }
    }

    @Override
    public void delete(List<User> list) {
        for (User user : list) {
            Map<String, Object> deleteResult = umsWebServiceUtils.syscUserData(user,
                    UmsWebServiceUtils.UMS_USER_OPERATE_TYPE_DEL);
            if ((Boolean) deleteResult.get("result")) {
                logger.info("delete eip user to bpm success, user name:" + user.getLoginName());
            } else {
                logger.error("delete eip user to bpm failed, user name:" + user.getLoginName());
            }
        }
    }
}

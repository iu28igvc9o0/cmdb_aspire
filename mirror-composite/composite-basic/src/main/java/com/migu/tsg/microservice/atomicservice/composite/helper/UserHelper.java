package com.migu.tsg.microservice.atomicservice.composite.helper;

import com.aspire.mirror.common.entity.PageResult;
import com.migu.tsg.microservice.atomicservice.composite.biz.UserBiz;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.CompUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author baiwenping
 * @Title: UserHelper
 * @Package com.migu.tsg.microservice.atomicservice.composite.helper
 * @Description: TODO
 * @date 2021/3/15 20:13
 */
@Component
@Slf4j
public class UserHelper {

    @Autowired
    private List<UserBiz> userBizList;

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    public CompUserVo findByLdapId(String username) {
        for (UserBiz userBiz: userBizList) {
            if (!userBiz.isBasic()) {
                return userBiz.findByLdapId(username);
            }
        }
        return userBizList.get(0).findByLdapId(username);
    }

    /**
     * 查询用户分页
     * @param pageNo
     * @param pageSize
     * @param search
     * @return
     */
    public PageResult<CompUserVo> pageList(Integer pageNo, Integer pageSize, String search) {
        for (UserBiz userBiz: userBizList) {
            if (!userBiz.isBasic()) {
                return userBiz.pageList(pageNo, pageSize, search);
            }
        }
        return userBizList.get(0).pageList(pageNo, pageSize, search);
    }

}

package com.migu.tsg.microservice.atomicservice.composite.biz;

import com.aspire.mirror.common.entity.PageResult;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.CompUserVo;

/**
 * 用户相关服务
 * @author baiwenping
 * @Title: UserBasicBiz
 * @Package com.migu.tsg.microservice.atomicservice.composite.biz
 * @Description: TODO
 * @date 2021/3/15 20:15
 */
public interface UserBiz {
    /**
     * 判断是否基础组件，基础服务不调用外部接口
     */
    boolean isBasic();

    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    CompUserVo findByLdapId(String username);

    /**
     * 查询用户分页
     * @param pageNo
     * @param pageSize
     * @param search
     * @return
     */
    PageResult<CompUserVo> pageList(int pageNo, int pageSize, String search);
}

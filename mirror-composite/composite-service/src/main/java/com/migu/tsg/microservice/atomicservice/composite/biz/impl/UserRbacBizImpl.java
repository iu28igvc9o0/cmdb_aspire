package com.migu.tsg.microservice.atomicservice.composite.biz.impl;

import com.aspire.mirror.common.entity.PageResult;
import com.migu.tsg.microservice.atomicservice.composite.biz.UserBiz;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.UserServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.CompUserVo;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserPageRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.UserDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author baiwenping
 * @Title: UserRbacBizImpl
 * @Package com.migu.tsg.microservice.atomicservice.composite.biz.impl
 * @Description: TODO
 * @date 2021/3/15 20:25
 */
@Component
public class UserRbacBizImpl implements UserBiz {

    @Autowired
    private UserServiceClient userServiceClient;
    /**
     * 判断是否基础组件，基础服务不调用外部接口
     */
    @Override
    public boolean isBasic() {
        return false;
    }

    /**
     * 根据用户名查找用户
     *
     * @param username
     * @return
     */
    @Override
    public CompUserVo findByLdapId(String username) {
        UserVO userVO = userServiceClient.findByLdapId(username);
        return PayloadParseUtil.jacksonBaseParse(CompUserVo.class, userVO);
    }

    /**
     * 查询用户分页
     *
     * @param pageNo
     * @param pageSize
     * @param search
     * @return
     */
    @Override
    public PageResult<CompUserVo> pageList(int pageNo, int pageSize, String search) {
        UserPageRequest userPageRequest = new UserPageRequest();
        userPageRequest.setPageNo(pageNo);
        userPageRequest.setPageSize(pageSize);
        userPageRequest.setSearch(search);
        PageResult<UserDTO> page = userServiceClient.pageList(userPageRequest);
        PageResult<CompUserVo> pageResult = new PageResult();
        pageResult.setCount(page.getCount());
        pageResult.setPageSize(page.getPageSize());
        pageResult.setCurPage(page.getCurPage());
        pageResult.setPageCount(page.getCount());
        pageResult.setResult(PayloadParseUtil.jacksonBaseParse(CompUserVo.class, page.getResult()));
        return pageResult;
    }


}

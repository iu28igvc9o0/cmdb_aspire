package com.migu.tsg.microservice.atomicservice.composite.biz.impl;

import com.aspire.mirror.common.entity.PageResult;
import com.migu.tsg.microservice.atomicservice.composite.biz.UserBiz;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.CompUserVo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author baiwenping
 * @Title: UserBasicBizImpl
 * @Package com.migu.tsg.microservice.atomicservice.composite.biz.impl
 * @Description: TODO
 * @date 2021/3/15 20:16
 */
@Component
public class UserBasicBizImpl implements UserBiz {

    /**
     * 判断是否基础组件，基础服务不调用外部接口
     */
    @Override
    public boolean isBasic() {
        return true;
    }

    /**
     * 根据用户名查找用户
     *
     * @param username
     * @return
     */
    @Override
    public CompUserVo findByLdapId(String username) {
        return getBasicUser(username);
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
        PageResult<CompUserVo> pageResult = new PageResult();
        pageResult.setCount(pageNo * pageSize + 1);
        pageResult.setPageSize(pageSize);
        pageResult.setCurPage(pageNo);
        pageResult.setPageCount(1);
        List<CompUserVo> list = new ArrayList<>(1);
        list.add(getBasicUser(search));
        pageResult.setResult(list);
        return pageResult;
    }

    private CompUserVo getBasicUser(String username){
        CompUserVo userVO = new CompUserVo();
        userVO.setName(username);
        userVO.setLdapId(username);
        userVO.setCode(username);
        return userVO;
    }

}

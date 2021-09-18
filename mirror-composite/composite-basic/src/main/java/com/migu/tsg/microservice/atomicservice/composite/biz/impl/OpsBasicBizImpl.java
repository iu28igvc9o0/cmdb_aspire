package com.migu.tsg.microservice.atomicservice.composite.biz.impl;

import com.migu.tsg.microservice.atomicservice.composite.biz.OpsBiz;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author baiwenping
 * @Title: OpsBasicBizImpl
 * @Package com.migu.tsg.microservice.atomicservice.composite.biz.impl
 * @Description: TODO
 * @date 2021/3/16 18:02
 */
@Component
public class OpsBasicBizImpl implements OpsBiz {
    /**
     * 判断是否基础组件，基础服务不调用外部接口
     */
    @Override
    public boolean isBasic() {
        return true;
    }

    @Override
    public List<String> queryAllChildGroup(List<String> groupIdList) {
        return new ArrayList<>();
    }
}

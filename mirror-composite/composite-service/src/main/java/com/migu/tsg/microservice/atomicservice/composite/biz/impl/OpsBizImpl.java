package com.migu.tsg.microservice.atomicservice.composite.biz.impl;

import com.aspire.mirror.ops.api.domain.ChildGroupQueryModel;
import com.migu.tsg.microservice.atomicservice.composite.biz.OpsBiz;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.opsmanage.OpsGroupClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author baiwenping
 * @Title: OpsBizImpl
 * @Package com.migu.tsg.microservice.atomicservice.composite.biz.impl
 * @Description: TODO
 * @date 2021/3/16 18:03
 */
@Component
public class OpsBizImpl implements OpsBiz {
    @Autowired
    protected OpsGroupClient opsGroupClient;
    /**
     * 判断是否基础组件，基础服务不调用外部接口
     */
    @Override
    public boolean isBasic() {
        return false;
    }

    @Override
    public List<String> queryAllChildGroup(List<String> groupIdList) {
        ChildGroupQueryModel childGroupQueryModel = new ChildGroupQueryModel();
        childGroupQueryModel.setGroupIdList(groupIdList);
        return opsGroupClient.queryAllChildGroup(childGroupQueryModel);
    }
}

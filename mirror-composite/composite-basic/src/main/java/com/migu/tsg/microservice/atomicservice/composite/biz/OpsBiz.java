package com.migu.tsg.microservice.atomicservice.composite.biz;

import java.util.List;

/**
 * @author baiwenping
 * @Title: OpsBiz
 * @Package com.migu.tsg.microservice.atomicservice.composite.biz
 * @Description: TODO
 * @date 2021/3/16 18:01
 */
public interface OpsBiz {
    /**
     * 判断是否基础组件，基础服务不调用外部接口
     */
    boolean isBasic();

    List<String> queryAllChildGroup(List<String> groupIdList);
}

package com.migu.tsg.microservice.atomicservice.composite.biz;

import com.migu.tsg.microservice.atomicservice.composite.vo.cmdb.ResultVo;

import java.util.List;
import java.util.Map;

/**
 * @author baiwenping
 * @Title: InstanceBiz
 * @Package com.migu.tsg.microservice.atomicservice.composite.biz
 * @Description: TODO
 * @date 2021/3/16 15:17
 */
public interface InstanceBiz {
    /**
     * 判断是否基础组件，基础服务不调用外部接口
     */
    boolean isBasic();

    /**
     * 根据条件查询实例详情
     * @param params
     * @return
     */
    Map<String, Object> getInstanceDetail(Map<String, Object> params);

    /**
     * 查询cmdb统计数据
     * @param params
     * @param name
     * @param responseType
     * @return
     */
    Object getCmdbData(Map<String, Object> params, String name, String responseType);

    /**
     * 查询listV3接口实例列表
     * @param params
     * @param moduleType
     * @return
     */
    ResultVo<Map<String, Object>> getInstanceListV3(Map<String, Object> params, String moduleType);

    /**
     * 根据id集合查询机房
     * @param ids
     * @return
     */
    List<Map<String, String>> getRoomByIds(String ids);

    /**
     * 根据id集合查询业务系统
     * @param ids
     * @return
     */
    List<Map<String, String>> getBizSystemByIds(String ids);
}

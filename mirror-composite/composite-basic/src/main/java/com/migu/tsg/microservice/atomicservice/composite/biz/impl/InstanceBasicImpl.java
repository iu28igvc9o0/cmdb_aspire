package com.migu.tsg.microservice.atomicservice.composite.biz.impl;

import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.biz.InstanceBiz;
import com.migu.tsg.microservice.atomicservice.composite.vo.cmdb.ResultVo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author baiwenping
 * @Title: InstanceBasicImpl
 * @Package com.migu.tsg.microservice.atomicservice.composite.biz.impl
 * @Description: TODO
 * @date 2021/3/16 15:17
 */
@Component
public class InstanceBasicImpl implements InstanceBiz {
    /**
     * 判断是否基础组件，基础服务不调用外部接口
     */
    @Override
    public boolean isBasic() {
        return true;
    }

    /**
     * 根据条件查询实例详情
     *
     * @param params
     * @return
     */
    @Override
    public Map<String, Object> getInstanceDetail(Map<String, Object> params) {
        return params;
    }

    /**
     * 查询cmdb统计数据
     *
     * @param params
     * @param name
     * @param responseType
     * @return
     */
    @Override
    public Object getCmdbData(Map<String, Object> params, String name, String responseType) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("cpu_core_number", 11763);
        map.put("memory_size",743302);
        if ("list".equalsIgnoreCase(responseType)) {
            List<Map<String, Object>> list = new ArrayList<>(1);
            return list;
        } else if ("map".equalsIgnoreCase(responseType)) {
            return map;
        }
        return null;
    }

    /**
     * 查询listV3接口实例列表
     *
     * @param params
     * @param moduleType
     * @return
     */
    @Override
    public ResultVo<Map<String, Object>> getInstanceListV3(Map<String, Object> params, String moduleType) {
        ResultVo<Map<String, Object>> resultVo = new ResultVo<>();
        List<Map<String, Object>> list = new ArrayList<>(1);
        resultVo.setData(list);
        resultVo.setTotalSize(0);
        return resultVo;
    }

    /**
     * 根据id集合查询机房
     *
     * @param ids
     * @return
     */
    @Override
    public List<Map<String, String>> getRoomByIds(String ids) {
        return new ArrayList<>();
    }

    /**
     * 根据id集合查询业务系统
     *
     * @param ids
     * @return
     */
    @Override
    public List<Map<String, String>> getBizSystemByIds(String ids) {
        return new ArrayList<>();
    }
}

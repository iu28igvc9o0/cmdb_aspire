package com.migu.tsg.microservice.atomicservice.composite.biz.impl;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.restful.payload.StatisticRequestEntity;
import com.migu.tsg.microservice.atomicservice.composite.biz.InstanceBiz;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.BizSystemClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.instance.InstanceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.restful.common.ICommonRestfulClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import com.migu.tsg.microservice.atomicservice.composite.vo.cmdb.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author baiwenping
 * @Title: InstanceCmdbImpl
 * @Package com.migu.tsg.microservice.atomicservice.composite.biz.impl
 * @Description: TODO
 * @date 2021/3/16 15:18
 */
@Component
public class InstanceCmdbImpl implements InstanceBiz {
    @Autowired
    private ICommonRestfulClient commonRestfulClient;
    @Autowired
    private InstanceClient instanceClient;
    @Autowired
    protected BizSystemClient bizSystemClient;
    /**
     * 判断是否基础组件，基础服务不调用外部接口
     */
    @Override
    public boolean isBasic() {
        return false;
    }

    /**
     * 根据条件查询实例详情
     *
     * @param params
     * @return
     */
    @Override
    public Map<String, Object> getInstanceDetail(Map<String, Object> params) {
        return commonRestfulClient.getInstanceDetail(params);
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
        StatisticRequestEntity entity = new StatisticRequestEntity();
        entity.setName(name);
        entity.setParams(params);
        entity.setResponseType(responseType);
        return commonRestfulClient.getInstanceStatistics(entity);
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
        Result<Map<String, Object>> listV3 = instanceClient.getInstanceListV3(params, moduleType);
        return PayloadParseUtil.jacksonBaseParse(ResultVo.class, listV3);
    }

    /**
     * 根据id集合查询机房
     *
     * @param ids
     * @return
     */
    @Override
    public List<Map<String, String>> getRoomByIds(String ids) {
        return instanceClient.getRoomByIds(ids);
    }

    /**
     * 根据id集合查询业务系统
     *
     * @param ids
     * @return
     */
    @Override
    public List<Map<String, String>> getBizSystemByIds(String ids) {
        return bizSystemClient.getBizSystemByIds(ids);
    }

}

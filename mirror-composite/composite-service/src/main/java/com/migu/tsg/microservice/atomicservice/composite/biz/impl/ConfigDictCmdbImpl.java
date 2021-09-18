package com.migu.tsg.microservice.atomicservice.composite.biz.impl;

import com.migu.tsg.microservice.atomicservice.composite.biz.ConfigDictBiz;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.ConfigDictClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import com.migu.tsg.microservice.atomicservice.composite.dao.po.ConfigDict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author baiwenping
 * @Title: ConfigDictCmdbImpl
 * @Package com.migu.tsg.microservice.atomicservice.composite.biz.impl
 * @Description: TODO
 * @date 2021/3/16 16:29
 */
@Component
public class ConfigDictCmdbImpl implements ConfigDictBiz {
    @Autowired
    private ConfigDictClient configDictService;

    /**
     * 判断是否基础组件，基础服务不调用外部接口
     */
    @Override
    public boolean isBasic() {
        return false;
    }

    /**
     * 获取字典值列表
     *
     * @param type   字典类型
     * @param pid    父字典ID
     * @param pValue 父字典值
     * @param pType  父字典类型
     * @return 字典值列表
     */
    @Override
    public List<ConfigDict> selectDictsByType(String type, String pid, String pValue, String pType) {
        return PayloadParseUtil.jacksonBaseParse(ConfigDict.class, configDictService.getDictsByType(type, pid, pValue, pType));
    }

    /**
     * 查询所有字典类型
     *
     * @return
     */
    @Override
    public List<String> getDictType() {
        return configDictService.getDictType();
    }

}

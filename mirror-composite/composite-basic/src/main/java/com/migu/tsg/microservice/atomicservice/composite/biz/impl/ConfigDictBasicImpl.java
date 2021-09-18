package com.migu.tsg.microservice.atomicservice.composite.biz.impl;

import com.migu.tsg.microservice.atomicservice.composite.biz.ConfigDictBiz;
import com.migu.tsg.microservice.atomicservice.composite.dao.ConfigDictMapper;
import com.migu.tsg.microservice.atomicservice.composite.dao.po.ConfigDict;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author baiwenping
 * @Title: ConfigDictBasicImpl
 * @Package com.migu.tsg.microservice.atomicservice.composite.biz.impl
 * @Description: TODO
 * @date 2021/3/16 16:28
 */
@Component
public class ConfigDictBasicImpl implements ConfigDictBiz {
    @Autowired
    private ConfigDictMapper configDictMapper;

    /**
     * 判断是否基础组件，基础服务不调用外部接口
     */
    @Override
    public boolean isBasic() {
        return true;
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
        List<ConfigDict> resultList = null;
        if (StringUtils.isEmpty(type)) {
            return resultList;
        }

        resultList = configDictMapper.selectDictsByType(type, pid, pValue, pType);
        return resultList;
    }

    /**
     * 查询所有字典类型
     *
     * @return
     */
    @Override
    public List<String> getDictType() {
        List<Map<String, String>> dictTypeList = configDictMapper.getDictType();
        return dictTypeList.stream().map(item ->
                MapUtils.getString(item, "col_name")
        ).collect(Collectors.toList());
    }

}

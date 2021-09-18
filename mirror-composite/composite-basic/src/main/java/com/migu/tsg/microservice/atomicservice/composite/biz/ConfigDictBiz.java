package com.migu.tsg.microservice.atomicservice.composite.biz;

import com.migu.tsg.microservice.atomicservice.composite.dao.po.ConfigDict;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author baiwenping
 * @Title: ConfigDictBiz
 * @Package com.migu.tsg.microservice.atomicservice.composite.biz.impl
 * @Description: TODO
 * @date 2021/3/16 16:27
 */
public interface ConfigDictBiz {
    /**
     * 判断是否基础组件，基础服务不调用外部接口
     */
    boolean isBasic();

    /**
     * 获取字典值列表
     *
     * @param type   字典类型
     * @param pid    父字典ID
     * @param pValue 父字典值
     * @param pType  父字典类型
     * @return 字典值列表
     */
    List<ConfigDict> selectDictsByType(String type, String pid, String pValue, String pType);

    /**
     * 查询所有字典类型
     * @return
     */
    List<String> getDictType();


}

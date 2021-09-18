package com.migu.tsg.microservice.atomicservice.composite.biz;

import com.migu.tsg.microservice.atomicservice.composite.dao.po.CodeDict;
import com.migu.tsg.microservice.atomicservice.composite.service.dict.DictDTO;

import java.util.List;

/**
 * 字典业务层
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.common.server.biz
 * 类名称:    DictBiz.java
 * 类描述:    字典业务层
 * 创建人:    JinSu
 * 创建时间:  2018/11/12 10:42
 * 版本:      v1.0
 */
public interface DictBiz {

    /**
     * 所有字典数据
     * @return
     */
    DictDTO findAll();

    /**
     * 所有字典数据
     * @return
     */
    DictDTO findList();

    List<CodeDict> getCodeDictByCodeType(String codeType);
}

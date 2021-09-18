package com.aspire.mirror.common.server.service;


import com.aspire.mirror.common.api.dto.model.ConfigDict;

import java.util.List;

public interface ConfigDictService {


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

}

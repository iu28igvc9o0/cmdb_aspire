package com.migu.tsg.microservice.atomicservice.composite.dao;

import com.migu.tsg.microservice.atomicservice.composite.dao.po.ConfigDict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CollectService
 * Author:   ws
 * Date:     2019/4/1
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Mapper
public interface ConfigDictMapper {

    List<ConfigDict> selectDictsByType(@Param(value = "colName") String colName,
                                       @Param(value = "pid") String pid,
                                       @Param(value = "pValue") String pValue,
                                       @Param(value = "pType") String pType);

    List<Map<String, String>> getDictType();
}

package com.aspire.mirror.common.server.dao;

import com.aspire.mirror.common.server.dao.po.CodeDict;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 字典dao层
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.common.server.dao
 * 类名称:    CodeDictDao.java
 * 类描述:    字典dao层
 * 创建人:    JinSu
 * 创建时间:  2018/11/12 11:04
 * 版本:      v1.0
 */
@Mapper
public interface CodeDictDao {
    List<CodeDict> fetchCodeDict();

    List<CodeDict> getCodeDictByCodeType(String codeType);
}

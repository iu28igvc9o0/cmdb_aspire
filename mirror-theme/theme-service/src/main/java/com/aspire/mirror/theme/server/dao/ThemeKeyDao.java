package com.aspire.mirror.theme.server.dao;

import com.aspire.mirror.theme.server.dao.po.ThemeKey;
import org.apache.ibatis.annotations.Mapper;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.server.dao
 * 类名称:    ThemeKeyDao.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/1/21 15:00
 * 版本:      v1.0
 */
@Mapper
public interface ThemeKeyDao {
    /**
     * 新增
     */
    void insert(ThemeKey themeKey);

    /**
     * 删除
     */
    void deleteByThemeId(String themeId);
    /**
     * 查询
     */
    ThemeKey selectByThemeId(String themeId);
}

package com.aspire.mirror.theme.server.dao;

import com.aspire.mirror.common.auth.GeneralAuthConstraintsAware;
import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.theme.server.dao.po.BizTheme;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 包名:     com.aspire.mirror.theme.server.dao
 * 类名:     BizThemeDao
 * 描述:     数据访问对象
 * 作者:     金素
 * 时间:     2018-10-22 16:28:59
 */
@Mapper
public interface BizThemeDao {
    /**
     * 新增
     */
    void insert(BizTheme bizTheme);

    /**
     * 分页查询
     */
    List<BizTheme> pageList(Page page);

    /**
     * 详情
     */
    BizTheme selectByPrimaryKey(@Param("themeId")String id, @Param("authParam") GeneralAuthConstraintsAware authParam);

    /**
     * 修改
     */
    void updateByPrimaryKey(BizTheme bizTheme);

    /**
     * 删除
     */
    void deleteByPrimaryKey(String id);

    /**
     * 查询数据条数
     *
     * @param page 分页对象
     * @return int 影响数据条数
     */
    int pageListCount(Page page);

    /**
     * 列表查询
     *
     * @param bizTheme 主题数据
     * @return List<BizTheme> 主题列表
     */
    List<BizTheme> select(BizTheme bizTheme);

    /**
     * 根据主题编码查找主题
     *
     * @param themeCode 主题编码
     * @return BizTheme 主题数据
     */
    BizTheme selectByThemeCode(String themeCode);
}

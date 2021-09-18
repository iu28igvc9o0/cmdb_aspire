package com.aspire.mirror.theme.server.dao;

import com.aspire.mirror.theme.server.dao.po.BizThemeDim;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 包名:     com.aspire.webbas.announce.dao
 * 类名:     BizThemeDimDao
 * 描述:     数据访问对象
 * 作者:     金素
 * 时间:     2018-10-23 20:39:16
 */
@Mapper
public interface BizThemeDimDao {

    /**
     * 根据维度id集合查询
     *
     * @param dimIds 维度ID集合
     * @return 维度列表
     */
    List<BizThemeDim> selectByPrimaryKeyArrays(String[] dimIds);

    BizThemeDim selectByPrimaryKey(String id);

    /**
     * 批量插入
     *
     * @param dimList 维度列表
     */
    int insertBatch(List<BizThemeDim> dimList);

    /**
     * 查询维度
     *
     * @param bizThemeDim 维度对象
     * @return 维度列表
     */
    List<BizThemeDim> select(BizThemeDim bizThemeDim);

    int insert(BizThemeDim bizThemeDim);

    /**
     * 根据主题ID删除维度数据
     * @param themeId 主题ID
     * @return int 删除数据条数
     */
    int deleteByThemeId(String themeId);

    List<BizThemeDim> selectByThemeId(String themeId);
}

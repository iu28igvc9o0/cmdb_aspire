package com.aspire.mirror.template.server.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.template.server.dao.po.Functions;

/**
 * monitor_functions数据访问层接口
 *
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.server.dao
 * 类名称:    FunctionsDao.java
 * 类描述:    monitor_functions数据访问层接口
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
@Mapper
public interface FunctionsDao {

    /**
     * 新增数据
     *
     * @param functions monitor_functionsPO对象
     * @return int 新增数据条数
     */
    int insert(Functions functions);

    /**
     * 批量新增monitor_functions数据
     *
     * @param listFunctions monitor_functionsPO集合对象
     * @return int 新增数据条数
     */
    int insertByBatch(List<Functions> listFunctions);
    /**
     * 根据主键删除数据
     *
     * @param functionId 主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(@Param(value = "functionId") String functionId);

    /**
     * 根据主键数组批量删除数据
     *
     * @param functionIdArrays 主键数组
     * @return int 删除数据条数
     */
    int deleteByPrimaryKeyArrays(String[] functionIdArrays);

    /**
     * 根据条件删除数据
     *
     * @param functions  monitor_functionsPO对象
     * @return int 删除数据条数
     */
    int delete(Functions functions);

    /**
     * 根据参数选择性更新数据
     *
     * @param functions monitor_functionsPO对象
     * @return int 数据条数
     */
    int updateByPrimaryKeySelective(Functions functions);

    /**
     * 根据主键更新数据
     *
     * @param functions monitor_functionsPO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(Functions functions);

    /**
     * 根据主键查询
     *
     * @param functionId 主键
     * @return Functions 返回对象
     */
    Functions selectByPrimaryKey(@Param(value = "functionId") String functionId);

    /**
     * 根据主键数组查询
     *
     * @param functionIdArrays 主键数组
     * @return List<Functions> 返回集合对象
     */
    List<Functions> selectByPrimaryKeyArrays(String[] functionIdArrays);
    /**
     * 根据po实体查询列表
     *
     * @param functions monitor_functionsPO对象
     * @return List<Functions>  返回集合
     */
    List<Functions> select(Functions functions);

    /**
     * 根据po实体查询条数
     *
     * @param functions monitor_functionsPO对象
     * @return int 数据条数
     */
    int selectCount(Functions functions);

    /**
     * 根据监控项ID集合删除数据
     * @param itemIdArrays 监控项ID集合
     * @return 数据条数
     */
    int deleteByItemIdArrays(String[] itemIdArrays);
}

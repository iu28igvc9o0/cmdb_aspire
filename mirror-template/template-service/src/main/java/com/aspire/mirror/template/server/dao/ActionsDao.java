package com.aspire.mirror.template.server.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.template.server.dao.po.Actions;

/**
 * 动作数据访问层接口
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.server.dao
 * 类名称:    ActionsDao.java
 * 类描述:    动作数据访问层接口
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
@Mapper
public interface ActionsDao {

    /**
     * 新增数据
     *
     * @param actions 动作PO对象
     * @return int 新增数据条数
     */
    int insert(Actions actions);

    /**
     * 批量新增动作数据
     *
     * @param listActions 动作PO集合对象
     * @return int 新增数据条数
     */
    int insertByBatch(List<Actions> listActions);

    /**
     * 根据主键删除数据
     *
     * @param actionId 主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(@Param(value = "actionId") String actionId);

    /**
     * 根据主键数组批量删除数据
     *
     * @param actionIdArrays 主键数组
     * @return int 删除数据条数
     */
    int deleteByPrimaryKeyArrays(String[] actionIdArrays);

    /**
     * 根据条件删除数据
     *
     * @param actions 动作PO对象
     * @return int 删除数据条数
     */
    int delete(Actions actions);

    /**
     * 根据参数选择性更新数据
     *
     * @param actions 动作PO对象
     * @return int 数据条数
     */
    int updateByPrimaryKeySelective(Actions actions);

    /**
     * 根据主键更新数据
     *
     * @param actions 动作PO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(Actions actions);

    /**
     * 根据主键查询
     *
     * @param actionId 主键
     * @return Actions 返回对象
     */
    Actions selectByPrimaryKey(@Param(value = "actionId") String actionId);

    /**
     * 根据主键数组查询
     *
     * @param actionIdArrays 主键数组
     * @return List<Actions> 返回集合对象
     */
    List<Actions> selectByPrimaryKeyArrays(String[] actionIdArrays);

    /**
     * 根据po实体查询列表
     *
     * @param actions 动作PO对象
     * @return List<Actions>  返回集合
     */
    List<Actions> select(Actions actions);

    /**
     * 根据po实体查询条数
     *
     * @param actions 动作PO对象
     * @return int 数据条数
     */
    int selectCount(Actions actions);

    List<Actions> selectByPrimaryKeyArraysAndProxyIdentity(@Param("actionIdArray") String[] actionIdArray, @Param("proxyIdentity")  String proxyIdentity);
} 

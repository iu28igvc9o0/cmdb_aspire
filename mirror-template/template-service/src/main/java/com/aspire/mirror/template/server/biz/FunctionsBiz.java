package com.aspire.mirror.template.server.biz;

import java.util.List;

import com.aspire.mirror.template.server.dao.po.Functions;
import com.aspire.mirror.template.api.dto.model.FunctionsDTO;

/**
 * monitor_functions业务层接口
 *
 * 项目名称: mirror平台
 * 包:      com.aspire.mirror.template.server.biz
 * 类名称:   FunctionsBiz.java
 * 类描述:   monitor_functions业务逻辑层接口
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 12:14:48
 */
public interface FunctionsBiz {

    /**
     * 新增数据
     *
     * @param functionsDTO monitor_functionsDTO对象
     * @return int 新增数据条数
     */
    int insert(FunctionsDTO functionsDTO);

    /**
     * 批量新增monitor_functions数据
     *
     * @param listFunctionsDTO monitor_functionsDTO集合对象
     * @return int 新增数据条数
     */
    int insertByBatch(List<FunctionsDTO> listFunctionsDTO);
    /**
     * 根据主键删除数据
     *
     * @param functionId 主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(String functionId);

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
     * @param functionsDTO  monitor_functionsDTO对象
     * @return int 删除数据条数
     */
    int delete(FunctionsDTO functionsDTO);

    /**
     * 根据参数选择性更新数据
     *
     * @param functionsDTO monitor_functionsDTO对象
     * @return int 数据条数
     */
    int updateByPrimaryKeySelective(FunctionsDTO functionsDTO);

    /**
     * 根据主键更新数据
     *
     * @param functionsDTO monitor_functionsDTO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(FunctionsDTO functionsDTO);

    /**
     * 根据主键查询
     *
     * @param functionId 主键
     * @return FunctionsDTO 返回对象
     */
    FunctionsDTO selectByPrimaryKey(String functionId);

    /**
     * 根据主键数组查询
     *
     * @param functionIdArrays 主键数组
     * @return List<FunctionsDTO> 返回集合对象
     */
    List<FunctionsDTO> selectByPrimaryKeyArrays(String[] functionIdArrays);
    /**
     * 根据dto实体查询列表
     *
     * @param functionsDTO monitor_functionsDTO对象
     * @return List<Functions>  返回集合
     */
    List<FunctionsDTO> select(FunctionsDTO functionsDTO);

    /**
     * 根据DTO实体查询条数
     *
     * @param functionsDTO monitor_functionsDTO对象
     * @return int 数据条数
     */
    int selectCount(FunctionsDTO functionsDTO);

} 

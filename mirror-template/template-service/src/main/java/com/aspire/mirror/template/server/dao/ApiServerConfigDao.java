package com.aspire.mirror.template.server.dao;

import com.aspire.mirror.template.server.dao.po.ApiServerConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * monitor_api_server_config数据访问层接口
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.server.dao
 * 类名称:    ApiServerConfigDao.java
 * 类描述:    monitor_api_server_config数据访问层接口
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
@Mapper
public interface ApiServerConfigDao {

    /**
     * 新增数据
     *
     * @param apiServerConfig monitor_api_server_configPO对象
     * @return int 新增数据条数
     */
    int insert(ApiServerConfig apiServerConfig);

    /**
     * 批量新增monitor_api_server_config数据
     *
     * @param listApiServerConfig monitor_api_server_configPO集合对象
     * @return int 新增数据条数
     */
    int insertByBatch(List<ApiServerConfig> listApiServerConfig);

    /**
     * 根据主键删除数据
     *
     * @param apiServerId 主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(@Param(value = "apiServerId") String apiServerId);

    /**
     * 根据主键数组批量删除数据
     *
     * @param apiServerIdArrays 主键数组
     * @return int 删除数据条数
     */
    int deleteByPrimaryKeyArrays(String[] apiServerIdArrays);

    /**
     * 根据条件删除数据
     *
     * @param apiServerConfig monitor_api_server_configPO对象
     * @return int 删除数据条数
     */
    int delete(ApiServerConfig apiServerConfig);

    /**
     * 根据参数选择性更新数据
     *
     * @param apiServerConfig monitor_api_server_configPO对象
     * @return int 数据条数
     */
    int updateByPrimaryKeySelective(ApiServerConfig apiServerConfig);

    /**
     * 根据主键更新数据
     *
     * @param apiServerConfig monitor_api_server_configPO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(ApiServerConfig apiServerConfig);

    /**
     * 根据主键查询
     *
     * @param apiServerId 主键
     * @return ApiServerConfig 返回对象
     */
    ApiServerConfig selectByPrimaryKey(@Param(value = "apiServerId") String apiServerId);

    /**
     * 根据主键数组查询
     *
     * @param apiServerIdArrays 主键数组
     * @return List<ApiServerConfig> 返回集合对象
     */
    List<ApiServerConfig> selectByPrimaryKeyArrays(String[] apiServerIdArrays);

    /**
     * 根据机房id数组查询. <br/>
     *
     * @param roomIdArr 机房ID数组
     * @return List<ApiServerConfig> 机房列表数据
     */
    List<ApiServerConfig> selectByRoomIdArrays(String[] roomIdArr);

    /**
     * 根据po实体查询列表
     *
     * @param apiServerConfig monitor_api_server_configPO对象
     * @return List<ApiServerConfig>  返回集合
     */
    List<ApiServerConfig> select(ApiServerConfig apiServerConfig);

    /**
     * 根据po实体查询条数
     *
     * @param apiServerConfig monitor_api_server_configPO对象
     * @return int 数据条数
     */
    int selectCount(ApiServerConfig apiServerConfig);

} 

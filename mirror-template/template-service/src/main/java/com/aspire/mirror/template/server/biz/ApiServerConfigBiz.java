package com.aspire.mirror.template.server.biz;

import java.util.List;

import com.aspire.mirror.template.api.dto.model.ApiServerConfigDTO;

/**
 * monitor_api_server_config业务层接口
 * <p>
 * 项目名称: mirror平台
 * 包:      com.aspire.mirror.template.server.biz
 * 类名称:   ApiServerConfigBiz.java
 * 类描述:   monitor_api_server_config业务逻辑层接口
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 12:14:48
 */
public interface ApiServerConfigBiz {

    /**
     * 新增数据
     *
     * @param apiServerConfigDTO monitor_api_server_configDTO对象
     * @return int 新增数据条数
     */
    int insert(ApiServerConfigDTO apiServerConfigDTO);

    /**
     * 批量新增monitor_api_server_config数据
     *
     * @param listApiServerConfigDTO monitor_api_server_configDTO集合对象
     * @return int 新增数据条数
     */
    int insertByBatch(List<ApiServerConfigDTO> listApiServerConfigDTO);

    /**
     * 根据主键删除数据
     *
     * @param apiServerId 主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(String apiServerId);

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
     * @param apiServerConfigDTO monitor_api_server_configDTO对象
     * @return int 删除数据条数
     */
    int delete(ApiServerConfigDTO apiServerConfigDTO);

    /**
     * 根据参数选择性更新数据
     *
     * @param apiServerConfigDTO monitor_api_server_configDTO对象
     * @return int 数据条数
     */
    int updateByPrimaryKeySelective(ApiServerConfigDTO apiServerConfigDTO);

    /**
     * 根据主键更新数据
     *
     * @param apiServerConfigDTO monitor_api_server_configDTO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(ApiServerConfigDTO apiServerConfigDTO);

    /**
     * 根据主键查询
     *
     * @param apiServerId 主键
     * @return ApiServerConfigDTO 返回对象
     */
    ApiServerConfigDTO selectByPrimaryKey(String apiServerId);

    /**
     * 根据主键数组查询
     *
     * @param apiServerIdArrays 主键数组
     * @return List<ApiServerConfigDTO> 返回集合对象
     */
    List<ApiServerConfigDTO> selectByPrimaryKeyArrays(String[] apiServerIdArrays);

    /**
     * 根据机房id列表查询. <br/>
     *
     * @param roomIdArr
     * @return
     */
    List<ApiServerConfigDTO> selectByRoomIdArrays(String[] roomIdArr);

    /**
     * 根据dto实体查询列表
     *
     * @param apiServerConfigDTO monitor_api_server_configDTO对象
     * @return List<ApiServerConfig>  返回集合
     */
    List<ApiServerConfigDTO> select(ApiServerConfigDTO apiServerConfigDTO);

    /**
     * 根据DTO实体查询条数
     *
     * @param apiServerConfigDTO monitor_api_server_configDTO对象
     * @return int 数据条数
     */
    int selectCount(ApiServerConfigDTO apiServerConfigDTO);

    /**
     * 根据机房查询对象
     *
     * @param room 机房
     * @return
     */
    ApiServerConfigDTO selectByRoom(String room);
} 

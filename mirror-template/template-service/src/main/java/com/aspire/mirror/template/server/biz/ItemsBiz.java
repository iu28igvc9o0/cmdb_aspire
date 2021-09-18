package com.aspire.mirror.template.server.biz;

import java.util.List;
import java.util.Map;

import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.template.server.dao.po.Items;
import com.aspire.mirror.template.api.dto.model.ItemsDTO;

/**
 * 监控项业务层接口
 *
 * 项目名称: mirror平台
 * 包:      com.aspire.mirror.template.server.biz
 * 类名称:   ItemsBiz.java
 * 类描述:   监控项业务逻辑层接口
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 12:14:47
 */
public interface ItemsBiz {

    /**
     * 新增数据
     *
     * @param itemsDTO 监控项DTO对象
     * @return int 新增数据条数
     */
    String insert(ItemsDTO itemsDTO);

    /**
     * 批量新增监控项数据
     *
     * @param listItemsDTO 监控项DTO集合对象
     * @return int 新增数据条数
     */
    List<ItemsDTO> insertByBatch(List<ItemsDTO> listItemsDTO);
    /**
     * 根据主键删除数据
     *
     * @param itemId 主键
     * @return int 删除数据条数
     */
//    int deleteByPrimaryKey(String itemId);

    /**
     * 根据主键数组批量删除数据
     *
     * @param itemIdArrays 主键数组
     * @return int 删除数据条数
     */
    int deleteByPrimaryKeyArrays(String[] itemIdArrays);

    /**
     * 根据条件删除数据
     *
     * @param itemsDTO  监控项DTO对象
     * @return int 删除数据条数
     */
//    int delete(ItemsDTO itemsDTO);

    /**
     * 根据参数选择性更新数据
     *
     * @param itemsDTO 监控项DTO对象
     * @return int 数据条数
     */
//    int updateByPrimaryKeySelective(ItemsDTO itemsDTO);

    /**
     * 根据主键更新数据
     *
     * @param itemsDTO 监控项DTO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(ItemsDTO itemsDTO);

    /**
     * 根据主键查询
     *
     * @param itemId 主键
     * @return ItemsDTO 返回对象
     */
    ItemsDTO selectByPrimaryKey(String itemId);

    /**
     * 根据主键数组查询
     *
     * @param itemIdArrays 主键数组
     * @return List<ItemsDTO> 返回集合对象
     */
    List<ItemsDTO> selectByPrimaryKeyArrays(String[] itemIdArrays);
    /**
     * 根据dto实体查询列表
     *
     * @param itemsDTO 监控项DTO对象
     * @return List<Items>  返回集合
     */
    List<ItemsDTO> select(ItemsDTO itemsDTO);

    /**
     * 根据DTO实体查询条数
     *
     * @param itemsDTO 监控项DTO对象
     * @return int 数据条数
     */
    int selectCount(ItemsDTO itemsDTO);

    /**
     * 分页查询
     * @param pageRequest page请求
     * @return 返回page结果对象
     */
    PageResponse<ItemsDTO> pageList(PageRequest pageRequest);

    /**
     * 查询最后修改监控值
     * @param itemId 监控项ID
     * @param sysCode 业务编码
     * @return String 监控结果
     */
    String findLastUpValueByItemId(String itemId, String sysCode);

    /**
     * 获取主题计算结果
     * @param itemId 监控项ID
     * @param startTime 计算开始时间
     * @param endTime 计算结束时间
     * @return List<Map<String,Object>> 主题计算结果
     */
    List<Map<String,Object>> getThemeCalcResult(String itemId, String startTime, String endTime);

    List<ItemsDTO> selectByPrimaryKeyArraysAndProxyIdentity(String[] itemIdArray, String proxyIdentity);
}

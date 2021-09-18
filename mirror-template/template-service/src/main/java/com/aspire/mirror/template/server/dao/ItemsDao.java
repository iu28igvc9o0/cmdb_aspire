package com.aspire.mirror.template.server.dao;

import java.util.List;

import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.template.api.dto.model.ItemExt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.template.server.dao.po.Items;

/**
 * 监控项数据访问层接口
 *
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.server.dao
 * 类名称:    ItemsDao.java
 * 类描述:    监控项数据访问层接口
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:47
 */
@Mapper
public interface ItemsDao {

    /**
     * 新增数据
     *
     * @param items 监控项PO对象
     * @return int 新增数据条数
     */
    int insert(Items items);

    /**
     * 批量新增监控项数据
     *
     * @param listItems 监控项PO集合对象
     * @return int 新增数据条数
     */
    int insertByBatch(List<Items> listItems);
    /**
     * 根据主键删除数据
     *
     * @param itemId 主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(@Param(value = "itemId") String itemId);

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
     * @param items  监控项PO对象
     * @return int 删除数据条数
     */
    int delete(Items items);

    /**
     * 根据参数选择性更新数据
     *
     * @param items 监控项PO对象
     * @return int 数据条数
     */
    int updateByPrimaryKeySelective(Items items);

    /**
     * 根据主键更新数据
     *
     * @param items 监控项PO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(Items items);

    /**
     * 根据主键查询
     *
     * @param itemId 主键
     * @return Items 返回对象
     */
    Items selectByPrimaryKey(@Param(value = "itemId") String itemId);

    /**
     * 根据主键数组查询
     *
     * @param itemIdArrays 主键数组
     * @return List<Items> 返回集合对象
     */
    List<Items> selectByPrimaryKeyArrays(String[] itemIdArrays);
    /**
     * 根据po实体查询列表
     *
     * @param items 监控项PO对象
     * @return List<Items>  返回集合
     */
    List<Items> select(Items items);

    /**
     * 根据po实体查询条数
     *
     * @param items 监控项PO对象
     * @return int 数据条数
     */
    int selectCount(Items items);

    /**
     * 根据模板ID删除巡检项
     * @param templateId 模板Id
     * @return int 数据条数
     */
    int deleteByTemplateId(String templateId);

    /**
     * 根据模板ID集合查询指标信息
     * @param templateIdArrays 模板ID集合
     * @return 指标列表
     */
    List<Items> selectByTemplateIdArrays(String[] templateIdArrays);

    /**
     * 根据page对象查询数量
     * @param page
     * @return 条数
     */
    int pageListCount(Page page);

    /**
     * 根据page对象查询监控项列表
     * @param page
     * @return 监控项列表
     */
    List<Items> pageList(Page page);

    /**
     * 根据模板Id集合删除触发器信息
     * @param templateIdArrays 模板Id集合
     * @return 影响数据条数
     */
    int deleteByTemplateIdArrays(String[] templateIdArrays);

    Items selectItemsBySpecialParam(@Param("thridSystemId") String thridSystemId, @Param("zabbixItemId") String zabbixItemId);

    List<Items> selectByPrimaryKeyArraysAndProxyIdentity(@Param("itemIdArray") String[] itemIdArray, @Param("proxyIdentity") String proxyIdentity);

    void batchInsertExt(List<ItemExt> itemExts);

    void updateItemExtByItemId(ItemExt itemExt);

    void deleteExtByItemIdArrays(String[] itemIdArrays);

    ItemExt getItemExtByItemId(@Param("itemId") String itemId);
}

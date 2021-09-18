package com.aspire.mirror.alert.server.dao.primarySecondary;

import com.aspire.mirror.alert.server.dao.primarySecondary.po.AlertPrimarySecondary;
import com.aspire.mirror.alert.server.vo.primarySecondary.AlertPrimarySecondaryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @author
 * @date 2019-08-14 19:35:33
 */
@Mapper
public interface AlertPrimarySecondaryMapper {
    /**
     * 获取所有实例
     *
     * @return 返回所有实例数据
     */
    List<AlertPrimarySecondary> list();

    /**
     * 获取所有生效的屏蔽规则实例
     *
     * @return 返回所有实例数据
     */
    List<AlertPrimarySecondary> listEffective();

    /**
     * 获取所有实例
     *
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<AlertPrimarySecondary> listByEntity(AlertPrimarySecondary entity);

    /**
     * 根据主键ID 获取数据信息
     *
     * @param id 实例信息
     * @return 返回实例信息的数据信息
     */
    AlertPrimarySecondary get(@Param("id") String id);

    /**
     *
     * @param name
     * @return
     */
    List<AlertPrimarySecondary> checkName(@Param("name") String name);

    /**
     * 新增实例
     *
     * @param entity 实例数据
     * @return
     */
    void insert(AlertPrimarySecondary entity);

    /**
     * 修改实例
     *
     * @param entity 实例数据
     * @return
     */
    void update(AlertPrimarySecondary entity);

    /**
     * 删除实例
     *
     * @param ids 实例数据
     * @return
     */
    void delete(@Param("ids") String... ids);

    /**
     * 启动/停用屏蔽规则
     *
     * @param status
     * @param ids
     */
    void status(@Param("status") String status, @Param("ids") String... ids);

    /**
     * 分页查询
     *
     * @param entity
     * @return
     */
    List<AlertPrimarySecondary> findPageWithResult(AlertPrimarySecondaryVo entity);

    /**
     * 分页查询计数
     *
     * @param entity
     * @return
     */
    Integer findPageWithCount(AlertPrimarySecondaryVo entity);
    
    /**
     * 分页查询
     *
     * @param entity
     * @return
     */
    List<Map<String,Object>> findprimayRalate(Map<String,Object> params);

    /**
     * 分页查询计数
     *
     * @param entity
     * @return
     */
    Integer findprimayRalateCount(Map<String,Object> params);
}
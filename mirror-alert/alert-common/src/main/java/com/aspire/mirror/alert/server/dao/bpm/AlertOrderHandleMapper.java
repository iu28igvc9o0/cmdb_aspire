package com.aspire.mirror.alert.server.dao.bpm;

import com.aspire.mirror.alert.server.dao.bpm.po.AlertOrderHandle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述：
 *
 * @author
 * @date 2019-08-14 19:35:33
 */
@Mapper
public interface AlertOrderHandleMapper {
    /**
     * 获取所有实例
     *
     * @return 返回所有实例数据
     */
    List<AlertOrderHandle> list();

    /**
     * 根据主键ID 获取数据信息
     *
     * @param id 实例信息
     * @return 返回实例信息的数据信息
     */
    AlertOrderHandle get(@Param("id") Integer id);

    /**
     * 根据主键ID 获取数据信息
     *
     * @param orderId 实例信息
     * @return 返回实例信息的数据信息
     */
    List<AlertOrderHandle> getByOrderId(@Param("orderId") String orderId);

    /**
     * 新增实例
     *
     * @param entity 实例数据
     * @return
     */
    void insert(AlertOrderHandle entity);

    /**
     * 修改实例
     *
     * @param entity 实例数据
     * @return
     */
    void update(AlertOrderHandle entity);

    /**
     * 删除实例
     *
     * @param ids 实例数据
     * @return
     */
    void delete(@Param("ids") String... ids);

    void deleteByOrderId(@Param("orderId") String orderId);

    void deleteNotExist();
}
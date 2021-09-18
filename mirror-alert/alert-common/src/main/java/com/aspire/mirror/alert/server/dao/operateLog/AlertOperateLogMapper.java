package com.aspire.mirror.alert.server.dao.operateLog;

import com.aspire.mirror.alert.server.dao.operateLog.po.AlertOperateLog;
import com.aspire.mirror.alert.server.vo.operateLog.AlertOperateLogDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 描述：
 *
 * @author
 * @date 2019-08-14 19:35:34
 */
@Mapper
public interface AlertOperateLogMapper {
    /**
     * 获取所有实例
     *
     * @return 返回所有实例数据
     */
    List<AlertOperateLog> list();

    /**
     * 获取所有实例
     *
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<AlertOperateLog> listByEntity(AlertOperateLog entity);

    /**
     * 根据主键ID 获取数据信息
     *
     * @param id 实例信息
     * @return 返回实例信息的数据信息
     */
    AlertOperateLog get(Long id);

    /**
     * 新增实例
     *
     * @param entity 实例数据
     * @return
     */
    void insert(AlertOperateLog entity);

    /**
     * 批量新增实例
     *
     * @param list 实例数据
     * @return
     */
    void insertBatch(List<AlertOperateLog> list);

    /**
     * 修改实例
     *
     * @param entity 实例数据
     * @return
     */
    void update(AlertOperateLog entity);

    /**
     * 删除实例
     *
     * @param id 实例数据
     * @return
     */
    void delete(Long id);

    /**
     * 分页查询
     *
     * @param entity
     * @return
     */
    List<AlertOperateLog> findPageWithResult(AlertOperateLogDTO entity);

    /**
     * 分页查询计数
     *
     * @param entity
     * @return
     */
    Integer findPageWithCount(AlertOperateLogDTO entity);

}
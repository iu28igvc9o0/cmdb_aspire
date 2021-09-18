package com.aspire.mirror.alert.server.biz.isolate;

import com.aspire.mirror.alert.server.dao.isolate.po.AlertIsolate;
import com.aspire.mirror.alert.server.vo.isolate.AlertIsolateVo;
import com.aspire.mirror.common.entity.PageResponse;

import java.util.List;

/**
 * 描述：
 *
 * @author
 * @date 2019-08-14 19:35:33
 */
public interface IAlertIsolateBiz {
    /**
     * 获取所有实例
     *
     * @return 返回所有实例数据
     */
    List<AlertIsolate> list();

    /**
     * 根据主键ID 获取数据信息
     *
     * @param id 实例信息
     * @return 返回实例信息的数据信息
     */
    AlertIsolate get(String id);

    /**
     * 新增实例
     *
     * @param entity 实例数据
     * @return
     */
    void insert(AlertIsolate entity);

    /**
     * 修改实例
     *
     * @param entity 实例数据
     * @return
     */
    void update(AlertIsolate entity);

    /**
     * 删除实例
     *
     * @param ids 实例数据
     * @return
     */
    void delete(String operater, String... ids);

    /**
     * 启动/停用屏蔽规则
     *
     * @param status
     * @param ids
     */
    void status(String status, String operater, String... ids);

    /**
     * 分页查询
     *
     * @param alertIsolateVo
     * @return
     */
    PageResponse<AlertIsolate> findPage(AlertIsolateVo alertIsolateVo);
}
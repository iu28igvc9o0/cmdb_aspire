package com.aspire.mirror.alert.server.biz.derive;

import com.aspire.mirror.alert.server.dao.derive.po.AlertDerive;
import com.aspire.mirror.alert.server.vo.derive.AlertDeriveVo;
import com.aspire.mirror.common.entity.PageResponse;

import java.util.List;

/**
 * 描述：
 *
 * @author
 * @date 2019-08-14 19:35:33
 */
public interface IAlertDeriveBiz {
    /**
     * 获取所有实例
     *
     * @return 返回所有实例数据
     */
    List<AlertDerive> list();

    /**
     * 根据主键ID 获取数据信息
     *
     * @param id 实例信息
     * @return 返回实例信息的数据信息
     */
    AlertDerive get(String id);

    /**
     * 校验名称是否可用
     * @param name
     * @return
     */
    boolean checkName (String name);

    /**
     * 新增实例
     *
     * @param entity 实例数据
     * @return
     */
    void insert(AlertDerive entity);

    /**
     * 修改实例
     *
     * @param entity 实例数据
     * @return
     */
    void update(AlertDerive entity);

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
     * @param alertDeriveVo
     * @return
     */
    PageResponse<AlertDerive> findPage(AlertDeriveVo alertDeriveVo);

    /**
     * 拷贝衍生规则
     */
    void copyDerive(String id);
}
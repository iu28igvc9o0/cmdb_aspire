package com.aspire.mirror.alert.server.biz.primarySecondary;

import com.aspire.mirror.alert.server.dao.primarySecondary.po.AlertPrimarySecondary;
import com.aspire.mirror.alert.server.vo.primarySecondary.AlertPrimarySecondaryVo;
import com.aspire.mirror.common.entity.PageResponse;

import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @author
 * @date 2019-08-14 19:35:33
 */
public interface IAlertPrimarySecondaryBiz {
    /**
     * 获取所有实例
     *
     * @return 返回所有实例数据
     */
    List<AlertPrimarySecondary> list();

    /**
     * 根据主键ID 获取数据信息
     *
     * @param id 实例信息
     * @return 返回实例信息的数据信息
     */
    AlertPrimarySecondary get(String id);

    /**
     * 校验名称是否可用
     * @param name
     * @return
     */
    boolean checkName(String name);

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
     * @param alertPrimarySecondaryVo
     * @return
     */
    PageResponse<AlertPrimarySecondary> findPage(AlertPrimarySecondaryVo alertPrimarySecondaryVo);

    /**
     * 拷贝衍生规则
     */
    void copyDerive(String id);

    /**
     * 查询主次告警相关的业务系统
     * @param alertType
     * @param primaryId
     * @param pageFlag
     * @param pageNo
     * @param pageSize
     * @return
     */
	PageResponse<Map<String, Object>> listByConfigId(int alertType, String primaryId, boolean pageFlag, Integer pageNo,
			Integer pageSize);
}
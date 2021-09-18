package com.aspire.mirror.alert.server.biz.operateLog;

import com.aspire.mirror.alert.server.dao.operateLog.po.AlertOperateLog;
import com.aspire.mirror.alert.server.vo.operateLog.AlertOperateLogDTO;
import com.aspire.mirror.common.entity.PageResponse;

import java.util.List;

/**
* 描述：
* @author
* @date 2019-08-14 19:35:34
*/
public interface IAlertOperateLogBiz {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<AlertOperateLog> list();

    /**
     * 根据主键ID 获取数据信息
     * @param id 实例信息
     * @return 返回实例信息的数据信息
     */
    AlertOperateLog get(Long id);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(AlertOperateLog entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(AlertOperateLog entity);

    /**
     * 删除实例
     * @param id 实例数据
     * @return
     */
    void delete(Long id);
    /**
     * 分页查询
     * @param alertOperateLogDTO
     * @return
     */
    PageResponse<AlertOperateLog> findPage (AlertOperateLogDTO alertOperateLogDTO);

    /**
     * 新增告警规则操作记录
     *
     * @param content
     * @param operater
     * @param operateType
     * @param operateTypeDesc
     * @param relationIds
     */
    void insertOperateLog(String content, String operater, String operateType, String operateTypeDesc, String operateModel, String operateModelDesc, String... relationIds);
}
package com.aspire.ums.cmdb.ipAudit.service;

import com.aspire.ums.cmdb.ipAudit.payload.IpAuditUpdateRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author: huanggongrui
 * @Description: T: 稽核对象类; E: 列表响应类; V: 列表请求类
 * @Date: create in 2020/5/23 18:19
 */
public interface BaseIpAuditService<T, E, V> {
    /**
     * 新增
     *
     * @param data
     */
    void add(T data);

    /**
     * 批量新增
     *
     * @param data
     */
    void batchAdd(List<T> data);

    /**
     * 修改
     *
     * @param data
     */
    void update(T data);

    /**
     * 删除
     *
     * @param data
     */
    void delete(T data);

    /**
     * 根据条件查询
     *
     * @param data
     * @return
     */
    List<E> getList(V data);

    /**
     * 根据条件统计
     *
     * @param data
     * @return
     */
    int getListCount(V data);

    /**
     * 生成稽核数据
     */
    void generateAuditData();
    /**
     * 统计数据
     */
    Map<String, Object> getStatisticsData(V req);

    /**
     * 更新处理状态
     * @param request
     */
    void updateProcess(IpAuditUpdateRequest request);
}

package com.aspire.ums.cmdb.ipAudit.mapper;

import com.aspire.ums.cmdb.ipAudit.entity.CmdbIpEntity;
import com.aspire.ums.cmdb.ipAudit.entity.IpAuditSurvivingUnplanIpV6;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditCommonReq;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditCommonResp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IpAuditSurvivingUnplanIpV6Mapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<IpAuditCommonResp> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<IpAuditCommonResp> listByEntity(IpAuditCommonReq entity);

    /**
     * 获取总数，用于分页
     * @param data 查询条件
     * @return 查询的数量
     */
    int getListCount(IpAuditCommonReq data);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    IpAuditCommonResp get(IpAuditSurvivingUnplanIpV6 entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(IpAuditSurvivingUnplanIpV6 entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(IpAuditSurvivingUnplanIpV6 entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(IpAuditSurvivingUnplanIpV6 entity);

    Long getMaxCheckNum();

    void batchInsertIp(List<IpAuditSurvivingUnplanIpV6> list);

    List<String> getIpRepositoryIpv6s();

    /**
     * 列表顶部总数查询逻辑
     * @param req 基础查询条件
     */
    Map<String, Object> getStatisticsData(IpAuditCommonReq  req);

    List<Map<String,String>> getUnIpv6sByUnStreated(long checkNum);

}
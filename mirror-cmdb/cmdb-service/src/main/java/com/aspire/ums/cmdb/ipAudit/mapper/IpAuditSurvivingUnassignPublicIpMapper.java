package com.aspire.ums.cmdb.ipAudit.mapper;

import com.aspire.ums.cmdb.ipAudit.entity.IpAuditSurvivingUnassignPublicIp;
import com.aspire.ums.cmdb.ipAudit.payload.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* 描述：ip稽核--存活未分配公网IP dao
* @author fanwenhui
*/
@Mapper
public interface IpAuditSurvivingUnassignPublicIpMapper {
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
    List<UnassignPublicIpResp> listByEntity(UnassignPublicIpReq entity);

    /**
     * 获取总数，用于分页
     * @param data 查询条件
     * @return 查询的数量
     */
    int getListCount(UnassignPublicIpReq data);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    IpAuditCommonResp get(IpAuditSurvivingUnassignPublicIp entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(IpAuditSurvivingUnassignPublicIp entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(IpAuditSurvivingUnassignPublicIp entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(IpAuditSurvivingUnassignPublicIp entity);

    void batchInsertIp(List<IpAuditSurvivingUnassignPublicIp> list);

    /**
     * 列表顶部总数查询逻辑
     * @param req 基础查询条件
     */
    Map<String, Object> getStatisticsData(UnassignPublicIpReq req);

    /**
     * 获取公网IP地址库的所有已存在IP
     */
    List<Map<String,String>> getIpRepositoryPublicIps();

    /**
     * 更新公网IP的处理状态
     */
    void updateProcess(IpAuditUpdateRequest req);

    void updatePublicIpInfo(IpAuditUpdateRequest req);

    void updatePublicSegmentInfo(IpAuditUpdateRequest req);

    Long getMaxCheckNum();

    void batchUpdatePublicIpStatus(@Param("list") List<String> list);

}
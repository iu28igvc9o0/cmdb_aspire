package com.aspire.ums.cmdb.ipAudit.mapper;

import com.aspire.ums.cmdb.ipAudit.entity.IpAuditSurvivingUnassignIpV6;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditCommonResp;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditUpdateRequest;
import com.aspire.ums.cmdb.ipAudit.payload.UnassignIpV6Req;
import com.aspire.ums.cmdb.ipAudit.payload.UnassignIpV6Resp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* 描述：ip稽核--IPV6存活未分配IP dao
* @author fanwenhui
*/
@Mapper
public interface IpAuditSurvivingUnassignIpV6Mapper {
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
    List<UnassignIpV6Resp> listByEntity(UnassignIpV6Req entity);

    /**
     * 获取总数，用于分页
     * @param data 查询条件
     * @return 查询的数量
     */
    int getListCount(UnassignIpV6Req data);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    IpAuditCommonResp get(IpAuditSurvivingUnassignIpV6 entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(IpAuditSurvivingUnassignIpV6 entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(IpAuditSurvivingUnassignIpV6 entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(IpAuditSurvivingUnassignIpV6 entity);

    void batchInsertIp(List<IpAuditSurvivingUnassignIpV6> list);

    /**
     * 列表顶部总数查询逻辑
     * @param req 基础查询条件
     */
    Map<String, Object> getStatisticsData(UnassignIpV6Req req);

    /**
     * 更新公网IP的处理状态
     */
    void updateProcess(IpAuditUpdateRequest req);

    void updateIpv6IpInfo(IpAuditUpdateRequest req);

    void updateIpv6SegmentInfo(IpAuditUpdateRequest req);

    Long getMaxCheckNum();

    List<Map<String,String>> getIpRepositoryIpv6s();

    void batchUpdateIpv6Status(@Param("list") List<String> list);
}
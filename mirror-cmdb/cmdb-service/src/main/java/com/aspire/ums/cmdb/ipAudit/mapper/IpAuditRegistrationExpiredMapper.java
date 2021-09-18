package com.aspire.ums.cmdb.ipAudit.mapper;

import com.aspire.ums.cmdb.ipAudit.entity.CmdbIpRepositoryDto;
import com.aspire.ums.cmdb.ipAudit.entity.IpAuditRegistrationExpired;
import com.aspire.ums.cmdb.ipAudit.payload.*;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* 描述：
* @author huanggongrui
* @date 2020-05-22 14:33:58
*/
@Mapper
public interface IpAuditRegistrationExpiredMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<IpAuditRegistrationExpired> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<IpAuditRegistrationExpired> listByEntity(IpAuditRegistrationExpired entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    IpAuditRegistrationExpired get(IpAuditRegistrationExpired entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(IpAuditRegistrationExpired entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(IpAuditRegistrationExpired entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(IpAuditRegistrationExpired entity);

    List<CmdbIpRepositoryDto> getIpRepositoryInnerIp();

    List<CmdbIpRepositoryDto> getIpRepositoryPublicIp();

    List<CmdbIpRepositoryDto> getIpRepositoryIpv6();

    Long getMaxCheckNum(@Param(value = "ipType") String ipType);

    void batchInsertIp(List<IpAuditRegistrationExpired> list);

    List<RegistrationExpiredIpResp> getListByPage(RegistrationExpiredIpReq data);

    int getListCountByPage(RegistrationExpiredIpReq data);

    Map<String, Object> getStatisticsData(RegistrationExpiredIpReq req);

    List<IpAuditRegistrationExpired> getListByIds(List<String> idList);

    void updateProcess(IpAuditUpdateRequest request);

    List<RegistrationExpiredPublicIpResp> listByEntity4PublicIp(RegistrationExpiredPublicIpReq req);

    int getListCount4PublicIp(RegistrationExpiredPublicIpReq data);

    Map<String, Object> getStatisticsData4PublicIp(RegistrationExpiredPublicIpReq req);

    List<RegistrationExpiredIpv6Resp> listByEntity4Ipv6(RegistrationExpiredIpv6Req req);

    int getListCount4Ipv6(RegistrationExpiredIpv6Req data);

    Map<String, Object> getStatisticsData4Ipv6(RegistrationExpiredIpv6Req req);
}
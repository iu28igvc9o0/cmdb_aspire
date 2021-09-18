package com.aspire.ums.cmdb.ipAudit.mapper;

import com.aspire.ums.cmdb.ipAudit.entity.IpAuditRecordedUnsurvivingIntranetIp;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditUpdateRequest;
import com.aspire.ums.cmdb.ipAudit.payload.RecordedUnsurvivingIntranetIpReq;
import com.aspire.ums.cmdb.ipAudit.payload.RecordedUnsurvivingIntranetIpResp;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
* 描述：
* @author
* @date 2020-05-21 16:49:59
*/
@Mapper
public interface IpAuditRecordedUnsurvivingIntranetIpMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<IpAuditRecordedUnsurvivingIntranetIp> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<IpAuditRecordedUnsurvivingIntranetIp> listByEntity(IpAuditRecordedUnsurvivingIntranetIp entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    IpAuditRecordedUnsurvivingIntranetIp get(IpAuditRecordedUnsurvivingIntranetIp entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(IpAuditRecordedUnsurvivingIntranetIp entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(IpAuditRecordedUnsurvivingIntranetIp entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(IpAuditRecordedUnsurvivingIntranetIp entity);

    Long getMaxCheckNum();

    void batchInsertIp(List<IpAuditRecordedUnsurvivingIntranetIp> list);

    List<RecordedUnsurvivingIntranetIpResp> getListByPage(RecordedUnsurvivingIntranetIpReq data);

    int getListCountByPage(RecordedUnsurvivingIntranetIpReq data);

    Map<String, Object> getStatisticsData(RecordedUnsurvivingIntranetIpReq req);

    List<IpAuditRecordedUnsurvivingIntranetIp> getListByIds(List<String> idList);

    void updateProcess(IpAuditUpdateRequest request);
}
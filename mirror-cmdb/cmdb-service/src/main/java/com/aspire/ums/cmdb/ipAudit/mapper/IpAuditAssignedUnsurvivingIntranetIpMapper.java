package com.aspire.ums.cmdb.ipAudit.mapper;
import com.aspire.ums.cmdb.ipAudit.entity.IpAuditAssignedUnsurvivingIntranetIp;
import com.aspire.ums.cmdb.ipAudit.payload.AssignedUnsurvivingIntranetIpReq;
import com.aspire.ums.cmdb.ipAudit.payload.AssignedUnsurvivingIntranetIpResp;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditUpdateRequest;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
* 描述：
* @author
* @date 2020-05-21 20:22:15
*/
@Mapper
public interface IpAuditAssignedUnsurvivingIntranetIpMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<IpAuditAssignedUnsurvivingIntranetIp> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<IpAuditAssignedUnsurvivingIntranetIp> listByEntity(IpAuditAssignedUnsurvivingIntranetIp entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    IpAuditAssignedUnsurvivingIntranetIp get(IpAuditAssignedUnsurvivingIntranetIp entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(IpAuditAssignedUnsurvivingIntranetIp entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(IpAuditAssignedUnsurvivingIntranetIp entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(IpAuditAssignedUnsurvivingIntranetIp entity);

    Long getMaxCheckNum();

    void batchInsertIp(List<IpAuditAssignedUnsurvivingIntranetIp> list);

    Map<String, Object> getStatisticsData(AssignedUnsurvivingIntranetIpReq req);

    List<AssignedUnsurvivingIntranetIpResp> getListByPage(AssignedUnsurvivingIntranetIpReq data);

    int getListCountByPage(AssignedUnsurvivingIntranetIpReq data);

    List<IpAuditAssignedUnsurvivingIntranetIp> getListByIds(List<String> idList);

    void updateProcess(IpAuditUpdateRequest request);
}
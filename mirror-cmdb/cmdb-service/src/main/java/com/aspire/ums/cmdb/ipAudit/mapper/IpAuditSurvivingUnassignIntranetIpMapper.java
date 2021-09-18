package com.aspire.ums.cmdb.ipAudit.mapper;

import com.aspire.ums.cmdb.ipAudit.entity.CmdbIpRepositoryInnerIp;
import com.aspire.ums.cmdb.ipAudit.entity.IpAuditSurvivingUnassignIntranetIp;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditUpdateRequest;
import com.aspire.ums.cmdb.ipAudit.payload.SurvivingUnassignIntranetIpReq;
import com.aspire.ums.cmdb.ipAudit.payload.SurvivingUnassignIntranetIpResp;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * 描述：
 *
 * @author huanggongrui
 * @date 2020-05-22 10:49:26
 */
@Mapper
public interface IpAuditSurvivingUnassignIntranetIpMapper {
    /**
     * 获取所有实例
     *
     * @return 返回所有实例数据
     */
    List<IpAuditSurvivingUnassignIntranetIp> list();

    /**
     * 获取所有实例
     *
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<IpAuditSurvivingUnassignIntranetIp> listByEntity(IpAuditSurvivingUnassignIntranetIp entity);

    /**
     * 根据主键ID 获取数据信息
     *
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    IpAuditSurvivingUnassignIntranetIp get(IpAuditSurvivingUnassignIntranetIp entity);

    /**
     * 新增实例
     *
     * @param entity 实例数据
     * @return
     */
    void insert(IpAuditSurvivingUnassignIntranetIp entity);

    /**
     * 修改实例
     *
     * @param entity 实例数据
     * @return
     */
    void update(IpAuditSurvivingUnassignIntranetIp entity);

    /**
     * 删除实例
     *
     * @param entity 实例数据
     * @return
     */
    void delete(IpAuditSurvivingUnassignIntranetIp entity);

    Long getMaxCheckNum();

    void updateIpRepository(CmdbIpRepositoryInnerIp cmdbIpRepositoryInnerIp);

    void batchInsertIp(List<IpAuditSurvivingUnassignIntranetIp> list);

    List<SurvivingUnassignIntranetIpResp> getListByPage(SurvivingUnassignIntranetIpReq data);

    int getListCountByPage(SurvivingUnassignIntranetIpReq data);

    Map<String, Object> getStatisticsData(SurvivingUnassignIntranetIpReq req);

    List<IpAuditSurvivingUnassignIntranetIp> getListByIds(List<String> idList);

    void updateProcess(IpAuditUpdateRequest request);
}
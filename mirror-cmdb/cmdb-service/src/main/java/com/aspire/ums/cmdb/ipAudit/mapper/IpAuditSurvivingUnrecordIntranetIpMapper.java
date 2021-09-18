package com.aspire.ums.cmdb.ipAudit.mapper;
import com.aspire.ums.cmdb.ipAudit.entity.CmdbIpEntity;
import com.aspire.ums.cmdb.ipAudit.entity.IpAuditSurvivingUnrecordIntranetIp;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditUpdateRequest;
import com.aspire.ums.cmdb.ipAudit.payload.SurvivingUnrecordIntranetIpReq;
import com.aspire.ums.cmdb.ipAudit.payload.SurvivingUnrecordIntranetIpResp;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
* 描述：
* @author
* @date 2020-05-21 11:20:10
*/
@Mapper
public interface IpAuditSurvivingUnrecordIntranetIpMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<IpAuditSurvivingUnrecordIntranetIp> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<IpAuditSurvivingUnrecordIntranetIp> listByEntity(IpAuditSurvivingUnrecordIntranetIp entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    IpAuditSurvivingUnrecordIntranetIp get(IpAuditSurvivingUnrecordIntranetIp entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(IpAuditSurvivingUnrecordIntranetIp entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(IpAuditSurvivingUnrecordIntranetIp entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(IpAuditSurvivingUnrecordIntranetIp entity);

    /**
     * 获取检测批次
     * @return
     */
    Long getMaxCheckNum();

    /**
     * 获取CMDB所有类别的IP
     * @return
     */
    List<CmdbIpEntity> getCmdbAllIp();

    /**
     * 获取全量存活IP
     * @return
     */
    List<CmdbIpEntity> getCollectionIp();

    void batchInsertIp(List<IpAuditSurvivingUnrecordIntranetIp> needAddList);

    List<String> getIpDcListByCheckNum(Long maxCheckNum);

    List<SurvivingUnrecordIntranetIpResp> getListByPage(SurvivingUnrecordIntranetIpReq data);

    int getListCountByPage(SurvivingUnrecordIntranetIpReq data);

    Map<String, Object> getStatisticsData(SurvivingUnrecordIntranetIpReq req);

    List<IpAuditSurvivingUnrecordIntranetIp> getListByIds(List<String> idList);

    void updateProcess(IpAuditUpdateRequest request);

    List<SurvivingUnrecordIntranetIpResp> getIpRepositoryList(SurvivingUnrecordIntranetIpReq req);
}
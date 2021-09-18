package com.aspire.ums.cmdb.ipAudit.mapper;
import com.aspire.ums.cmdb.ipAudit.entity.CmdbIpEntity;
import com.aspire.ums.cmdb.ipAudit.entity.IpAuditSurvivingUnplannedIntranetIp;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditUpdateRequest;
import com.aspire.ums.cmdb.ipAudit.payload.SurvivingUnplannedIntranetIpReq;
import com.aspire.ums.cmdb.ipAudit.payload.SurvivingUnplannedIntranetIpResp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
* 描述：
* @author
* @date 2020-05-15 16:53:39
*/
@Mapper
public interface IpAuditSurvivingUnplannedIntranetIpMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<IpAuditSurvivingUnplannedIntranetIp> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<IpAuditSurvivingUnplannedIntranetIp> listByEntity(IpAuditSurvivingUnplannedIntranetIp entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    IpAuditSurvivingUnplannedIntranetIp get(IpAuditSurvivingUnplannedIntranetIp entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(IpAuditSurvivingUnplannedIntranetIp entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(IpAuditSurvivingUnplannedIntranetIp entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(IpAuditSurvivingUnplannedIntranetIp entity);

    Long getMaxCheckNum();

    /**
     * cmdb all kind of ip
     * @return
     */
    List<CmdbIpEntity> getCmdbAllIp();

    /**
     * get all collection ip
     * @return
     */
    List<CmdbIpEntity> getCollectionIp();

    /**
     * all ip repository intranet ip
     * @return
     */
    List<String> getIpRepositoryIntranetIp();

    void batchInsertIp(List<IpAuditSurvivingUnplannedIntranetIp> list);

    Map<String, Object> getStatisticsData(SurvivingUnplannedIntranetIpReq req);

    int getListCountByPage(SurvivingUnplannedIntranetIpReq data);

    List<SurvivingUnplannedIntranetIpResp> getListByPage(SurvivingUnplannedIntranetIpReq data);

    List<IpAuditSurvivingUnplannedIntranetIp> getListByIds(List<String> idList);

    void updateProcess(IpAuditUpdateRequest request);

    IpAuditSurvivingUnplannedIntranetIp getEntityByIpAndDc(HashMap<String, String> param);
}
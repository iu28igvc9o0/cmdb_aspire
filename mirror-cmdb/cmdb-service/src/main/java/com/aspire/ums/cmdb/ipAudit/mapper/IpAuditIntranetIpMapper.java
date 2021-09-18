package com.aspire.ums.cmdb.ipAudit.mapper;
import com.aspire.ums.cmdb.ipAudit.entity.CmdbIpAddressPool;
import com.aspire.ums.cmdb.ipAudit.entity.CmdbIpEntity;
import com.aspire.ums.cmdb.ipAudit.entity.CmdbIpRepositoryInnerIp;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditUpdateRequest;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* 描述： ip稽核内网IP通用查询mapper
* @author huanggongrui
* @date 2020-05-21 16:53:39
*/
@Mapper
public interface IpAuditIntranetIpMapper {
    /**
     * cmdb ip entity of all kind of ip
     * @return
     */
    List<CmdbIpEntity> getCmdbAllIpEntity1();
    List<CmdbIpEntity> getCmdbAllIpEntity2();
    List<CmdbIpEntity> getCmdbAllIpEntity3();
    List<CmdbIpEntity> getCmdbAllIpEntity4();
    List<CmdbIpEntity> getCmdbAllIpEntity5();

    /**
     * cmdb all kind of ip
     * @return
     */
    List<String> getCmdbAllIp1();
    List<String> getCmdbAllIp2();
    List<String> getCmdbAllIp3();
    List<String> getCmdbAllIp4();
    List<String> getCmdbAllIp5();
    List<String> getCmdbAllIp6();

    /**
     * get all collection ip entity
     * @return
     */
    List<CmdbIpAddressPool> getCollectionIpEntity(String ipType);

    /**
     * get all collection ip
     * @return
     */
    List<String> getCollectionIp();

    /**
     * all ip repository intranet ip entity
     * @return
     */
    List<CmdbIpRepositoryInnerIp> getIpRepositoryIntranetIpEntity();

    /**
     * all intranet ip of ip repository
     * @return
     */
    List<String> getIpRepositoryIntranetIp();

    /**
     * all assigned ip repository intranet ip entity
     * @return
     */
    List<CmdbIpRepositoryInnerIp> getIpRepositoryAssignedIntranetIp();

    List<CmdbIpEntity> getAllSurvivingIpEntity1();
    List<CmdbIpEntity> getAllSurvivingIpEntity2();
    List<CmdbIpEntity> getAllSurvivingIpEntity3();
    List<CmdbIpEntity> getAllSurvivingIpEntity4();
    List<CmdbIpEntity> getAllSurvivingIpEntity5();
    List<CmdbIpEntity> getAllSurvivingIpEntity6();

    /**
     * 查询其他IP表关联主机资产表，获取资产管理的IPv6
     */
    List<CmdbIpEntity> getCmdbAllIp4Ipv6();

    /**
     * 查询从自动化采集过来的ipv6
     * {ipTypeName:IP类型名称,ipType:Ip类型}
     * -eg: {ipTypeName:存活IP扫描,ipType:ipv6}
     */
    List<CmdbIpEntity> getAutoIpv6(Map<String,String> param);

    /**
     * 通过防火墙IP关联管理资产表的设备IP、业务ip1、业务ip2、consoleip
     */
    List<CmdbIpEntity> getPublicIpInfoList();

    List<Map<String, String>> getBusinessConfigDict(Map<String, String> param);

    List<Map<String, String>> getNetworkSegmentConfigDict();

    List<Map<String, String>> getNetworkSegmentSubConfigDict(@Param("pid") String pid);

    List<Map<String, String>> getFinancialBusinessConfigDict();

    void updateIpRepositoryInnerSegmentInfo(IpAuditUpdateRequest param);

    void updateIpRepositoryInnerIpInfo(IpAuditUpdateRequest param);

    String getDictOfAssigned();

    List<Map<String, String>> getIpUseStatusTypeDict();

    List<Map<String, String>> getDeviceStatusConfigDict();
}
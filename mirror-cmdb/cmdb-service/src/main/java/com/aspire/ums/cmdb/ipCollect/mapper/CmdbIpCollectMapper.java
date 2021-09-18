package com.aspire.ums.cmdb.ipCollect.mapper;

import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.ipCollect.payload.entity.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 查询存活IP
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/5 17:57
 */
@Repository
public interface CmdbIpCollectMapper {

    /**
     * 分页查询
     *
     * @return
     */
    List<CmdbIpCollectResponse> findPage(CmdbIpCollectRequest request);

    /**
     * 条件查询统计
     *
     * @param request
     * @return
     */
    Integer findPageCount(CmdbIpCollectRequest request);

    /**
     * 头栏统计
     *
     * @param request
     * @return
     */
    CmdbIpCollectTopTotalResponse findTopTotal(CmdbIpCollectRequest request);

    /**
     * 获取资源池列表
     *
     * @return
     */
    List<String> getResource();

    void updateInnerIpInfo(@Param("list") List<Map<String,Object>> list);
    void updateIpv6Info(@Param("list") List<Map<String,Object>> list);
    void updatePublicIpInfo(@Param("list") List<Map<String,Object>> list);
    void updateIpv6Info4Del(@Param("list") List<String> list);
    void updatePublicIpInfo4Del(@Param("list") List<String> list);

    List<CmdbIpInfo> getCmdbAllIpInfo1(@Param("delFlag") String delFlag);
    List<CmdbIpInfo> getCmdbAllIpInfo2(@Param("delFlag") String delFlag);
    List<CmdbIpInfo> getCmdbAllIpInfo3(@Param("delFlag") String delFlag);
    List<CmdbIpInfo> getCmdbAllIpInfo4(@Param("delFlag") String delFlag);
    List<CmdbIpInfo> getCmdbAllIpInfo5(@Param("delFlag") String delFlag);
    List<CmdbIpInfo> getAutoInnerIpInfo();

    List<CmdbIpInfo> getPublicIpIpInfo();

    List<CmdbIpInfo> getAllAutoIpv6();
    List<CmdbIpInfo> getCmdbAllIp4Ipv6();

    List<CmdbIpInfo> getAutoIpInfo4FirstSurvivalTime();

    List<CmdbIpCollectResponse> getIpClashList4Now();

    String getCmdbIpHisPartName(@Param("tableName") String tableName,@Param("partName") String partName);

    void deletePartDataByPartName(@Param("tableName") String tableName,@Param("partName") String partName);

    void alterCmdbIpAddressHisPart(Map<String,String> partMap);
    void alterCmdbIpConfHisPart(Map<String,String> partMap);
    void alterCmdbIpArpHisPart(Map<String,String> partMap);

    int getCmdbIpAddressHisCount4NowDate();
    int getCmdbIpConfHisCount4NowDate();
    int getCmdbIpArpHisCount4NowDate();
    int getCmdbIpAddressCount();
    int getCmdbIpConfCount();
    int getCmdbIpArpCount();

    void saveCmdbIpAddressHis();
    void saveCmdbIpConfHis();
    void saveCmdbIpArpHis();

    void deleteCmdbIpAddressPreDay();
    void deleteCmdbIpConfPreDay();
    void deleteCmdbIpArpPreDay();

    List<Map<String,Object>> getCmdbInfoListByIpAndIdc(@Param("list") List<Map<String,Object>> list);
    void updateCmdbListInfo(@Param("list") List<Map<String,Object>> list);
    void updateInnerIpInfoNotInCmdb();

    void updateInnerIpFreeCount();

    List<String> findCmdbAssetSurvial2Kafka();

    List<Map<String,String>> findCmdbReportSql(@Param("type") String type);

    List<Map<String, String>> findDeviceAssetSurvivalList();

    void updateDeviceAsset2Survival(Map<String, Object> params);

    List<String> findDeviceAssetUnSurvivalList();

    void updateSurvivalStatus(Map<String, Object> params);

    void saveSurvivalMonitor(List<CmdbSurvivalStatusMonitor> list);
    void updateAllInnerIpSurvival(@Param("statusId") String statusId);

    void deleteCmdbIpAddressNowDay(@Param("nowDay") String nowDay);
    void deleteCmdbIpConfNowDay(@Param("nowDay") String nowDay);
    void deleteCmdbIpArpNowDay(@Param("nowDay") String nowDay);

    /**
     * 先把主机资源全部insert到检测报表，然后update 匹配到自动化的资产.
     * 
     * @param
     * @return
     */
    void saveHostServerAutoStatusReport();

    /**
     * 更新 自动化采集异常设备
     * 
     * @param
     * @return
     */
    void updateHostServerAutoStatusReport();

    /**
     * 自动化采集异常设备-查询待派工单的列表.
     * 
     * @param
     * @return
     */
    List<Map<String, Object>> queryHostServerSendOrderList();

    /**
     * 自动化采集异常设备-更新工单信息.
     * 
     * @param
     * @return
     */
    void updateHostServerOrderStatus(Map<String, Object> params);

    List<Map<String,String>> findAssetOtherIp();

    List<Map<String,String>> findDeviceUpdateInfo();

    List<BaseIpCollectEntity> findBaseIpCollectBaseEntity();

    List<Map<String,String>> findCarrierNetworkWithInnerNeworkIpLibrary(@Param("flag") String flag);

    List<Map<String,String>> findCarrierNetworkWithOtherIP(@Param("list") List<String> list);

    List<String> findCarrierNetworkByInnerNeworkIpLibrary();

    List<Map<String,String>> findCarrierNetworkWithInnerNeworkIpLibraryRelated(@Param("flag") String flag);

    int updateCarrierNetworkForAsset(@Param("list") List<Map<String, String>> subIdList);


}

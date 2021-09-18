package com.aspire.ums.cmdb.ipCollect.service;

import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.ipCollect.payload.entity.BaseIpCollectEntity;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpCollectRequest;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpCollectResponse;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpCollectTopTotalResponse;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/6/5 19:46
 */
public interface CmdbIpCollectService {

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
    List<Map> getResource();

    /**
     * 获取来源列表
     *
     * @return
     */
    List<Map> getSource();

    /**
     * 批量新增存活IP
     *
     * @param obj
     * @param clazz
     * @param <T>
     * @param autoFlag 是否自动化调用接口,1-是,2-不是
     * @param updateFlag 是否更新地址库信息,1-需要更新
     * @param ipClashFlag 是否需要处理冲突IP,1-需要处理
     */
    <T extends BaseIpCollectEntity> void batchAdd(List<T> obj, Class<T> clazz,String autoFlag,String updateFlag,String ipClashFlag);

    /**
     * 根据instanceIds批量逻辑删除
     *
     * @param instanceIds
     * @param clazz
     * @param <T>
     */
    <T extends BaseIpCollectEntity> void batchDeleteByInstanceId(List<String> instanceIds, Class<T> clazz);

    /**
     * 修改（map）
     *
     * @param obj
     * @param clazz
     * @param <T>
     */
    <T extends BaseIpCollectEntity> void modifyForMap(Map<String, Object> obj, Class<T> clazz);

    /**
     * 修改（对象）
     *
     * @param obj
     * @param clazz
     * @param <T>
     */
    <T extends BaseIpCollectEntity> void modifyForEntity(T obj, Class<T> clazz);

    /**
     * 根据instanceId返回实例基类
     *
     * @param instanceId
     * @param clazz
     * @param <T>
     * @return
     */
    <T extends BaseIpCollectEntity> BaseIpCollectEntity findBaseByInstanceId(String instanceId, Class<T> clazz);

    /**
     * 根据instanceId集合返回实例集合
     *
     * @param instanceId
     * @param clazz
     * @param <T>
     * @return
     */
    <T extends BaseIpCollectEntity> List<T> findEntityByInstanceId(List<String> instanceId, Class<T> clazz);

    /**
     * 获取所有instanceId
     * @param clazz
     * @param <T>
     * @return
     */
    <T extends BaseIpCollectEntity> List<String> getAllInstanceId(Class<T> clazz);

    void updateCmdbAssetAllIpInfo();

    void updatePublicIpInfo();

    void updateIpv6Info();

    void updateIpBusinessByAsset();

    String getIpUpdateConfig(String config);

    /**
     * 构建数据字典的map实体
     * @param config 数据字典编码
     */
    Map<String,String> buildConfig4Map(String config);

    void updateFirstSurvivalTime4IpInfo();

    /**
     * 通过新实例的IP查找当天系统的存活IP是否存在，有就比对mac地址
     * @param ip 新实例的IP
     * @param clazz 存活类型
     * @param <T> 基类
     */
    <T extends BaseIpCollectEntity> BaseIpCollectEntity findDataByIpList(String ip, Class<T> clazz);

    /**
     * 构建并保存当天的冲突IP
     */
    void buildAndSaveIpClashList4Now();

    /**
     * 新增当天的存活IP历史表分区
     */
    void alterCmdbIpPart(String type);

    /**
     * 更新资产的存活信息（存活状态和最近存活时间）
     */
    void updateCmdbAssetSurvialInfo();

    /**
     * 更新内网IP的可用数量
     */
    void updateInnerIpFreeCount();

    /**
     * 查询资产的存活信息不为空的资产并推送到kafka上，同步更新旧网管
     */
    void findCmdbAssetSurvial2Kafka();

    /**
     * 全量同步网段-端口组数据
     */
    void synAllNetworkPortGroup();

    /**
     * 更新cmdb资产为 已存活 存活时间和存活状态.
     *
     * @param
     * @return
     */
    void updateCmdbAssetSurvival();

    /**
     * 更新cmdb资产为 未存活
     *
     * @param
     * @return
     */
    void updateCmdbAsset2UnSurvival();

    /**
     * 自动化采集异常设备报表.
     * 
     * @param
     * @return
     */
    void reportCmdbAutoStatus();

    /**
     * 更新IP地址库的所有IP存活状态为未存活
     */
    void synAllInnerIpSurvival();

    void deleteCmdbIpAddressNowDay(String nowDay);
    void deleteCmdbIpConfNowDay(String nowDay);
    void deleteCmdbIpArpNowDay(String nowDay);

    /**
     * 更新同步资产的其他IP
     */
    void updateAssetOtherIp();

    /**
     * 通过存活IP,更新IP地址库的存活和分配信息
     */
    void updateBaseCollectIp();

    /**
     * 初始化资产表的承载网IP:
     * IP管理资产表根据资产表的管理ip、业务ip1、业务ip2、consoleip、other_ip（自动化采集ip）和关联表的“其他ip”匹配，同步资产表的承载网IP字段，若不存在则追加
     */
    void initCarrierNetworkForAsset();

}

package com.aspire.ums.cmdb.ipCollect.service;

import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbVipCollectEntity;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbVipCollectRequest;

import java.util.List;
import java.util.Map;

/**
 * 虚拟IP采集 service
 *
 * @Author: fanshenquan
 * @Datetime: 2020/5/18 18:59
 */
public interface CmdbVipCollectService {

    void batchAdd(List<CmdbVipCollectEntity> data);


    void modify(CmdbVipCollectEntity data);


    void updateByInstanceId(Map<String, Object> param);


    void deleteByInstanceId(String instanceId);


    void batchDeleteByInstanceId(List<String> instanceId);


    List<CmdbVipCollectEntity> findData(CmdbVipCollectRequest data);


    int findDataCount(CmdbVipCollectRequest data);


    CmdbVipCollectEntity findDataByInstanceId(String instanceId);


    List<Map<String, String>> getResource();

    List<String> getAllInstanceId();

    List<CmdbVipCollectEntity> findByInstanceIdList(List<String> instanceIdList);

    List<Map<String, String>> getUseType();

    /**
     * 查询所有vip
     *
     * @return
     */
    List<String> findAllVip();
}

package com.aspire.ums.cmdb.IpResource.service;

import com.aspire.ums.cmdb.ipResource.payload.*;

import java.util.List;
import java.util.Map;

/**
 * 主机资源资产表查询-资产管理流程
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/19 09:45
 */
public interface CmdbAssetInfoService {
    /**
     * 资产管理流程-管理IP、关联资产 弹窗列表数据
     *
     * @param param
     * @return
     */
    List<AssetInfoResp> getAssetList(AssetInfoParam param);

    /**
     * 资产管理流程-管理IP、关联资产 弹窗列表数据总数
     *
     * @param param
     * @return
     */
    Integer getAssetListCount(AssetInfoParam param);

    /**
     * 资产管理流程-资产回收 回填数据
     *
     * @param param
     * @return
     */
    List<AssetInfoBackfillResp> getBackfillAsset(AssetInfoParam param);

    /**
     * 资产管理流程-资产回收 回填数据总数
     *
     * @param param
     * @return
     */
    Integer getBackfillAssetCount(AssetInfoParam param);

    /**
     * 资产管理流程-原IP、回收IP 弹窗列表
     *
     * @param param
     * @return
     */
    List<AssetInfoIpResp> getAssetIpList(AssetIpInfoParam param);

    /**
     * 资产管理流程-原IP、回收IP 弹窗列表总数
     *
     * @param param
     * @return
     */
    Integer getAssetIpListCount(AssetIpInfoParam param);

    /**
     * 更新is_delete 状态，逻辑删除
     *
     * @param assetIds
     */
    void updateIsDelete(String assetIds);

    List<Map<String,String>> getKvmTemplateList(Map<String,String> param);

    List<Map<String,String>> getOneBusinessAndAloneBusiness(Map<String,String> param);

}

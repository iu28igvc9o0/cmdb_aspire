package com.aspire.ums.cmdb.IpResource.mapper;

import com.aspire.ums.cmdb.IpResource.entity.CmdbAssetInfoQuery;
import com.aspire.ums.cmdb.ipResource.payload.AssetInfoBackfillResp;
import com.aspire.ums.cmdb.ipResource.payload.AssetInfoIpResp;
import com.aspire.ums.cmdb.ipResource.payload.AssetInfoResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 主机资源资产表查询-资产管理流程
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/18 16:09
 */
public interface CmdbAssetInfoMapper {

    /**
     * 资产管理流程-管理IP、关联资产 弹窗列表数据
     *
     * @return
     */
    List<AssetInfoResp> getAssetList(CmdbAssetInfoQuery query);

    /**
     * 资产管理流程-管理IP、关联资产 弹窗列表数据总数
     *
     * @param query
     * @return
     */
    Integer getAssetListCount(CmdbAssetInfoQuery query);

    /**
     * 资产管理流程-资产回收 回填数据
     *
     * @param query
     * @return
     */
    List<AssetInfoBackfillResp> getBackfillAsset(CmdbAssetInfoQuery query);

    /**
     * 资产管理流程-资产回收 回填数据总数
     *
     * @param query
     * @return
     */
    Integer getBackfillAssetCount(CmdbAssetInfoQuery query);

    /**
     * 资产管理流程-原IP、回收IP 弹窗列表
     *
     * @param query
     * @return
     */
    List<AssetInfoIpResp> getAssetIpList(CmdbAssetInfoQuery query);

    /**
     * 资产管理流程-原IP、回收IP 弹窗列表总数
     *
     * @param query
     * @return
     */
    Integer getAssetIpListCount(CmdbAssetInfoQuery query);

    /**
     * 更新is_delete 状态，逻辑删除
     *
     * @param idArr
     */
    void updateIsDelete(@Param("idArr") String[] idArr);

    List<Map<String,String>> getKvmTemplateList(Map<String,String> param);

    List<Map<String,String>> getOneBusinessAndAloneBusiness(Map<String,String> param);

}

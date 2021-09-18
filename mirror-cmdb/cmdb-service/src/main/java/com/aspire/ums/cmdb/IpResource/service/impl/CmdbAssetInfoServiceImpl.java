package com.aspire.ums.cmdb.IpResource.service.impl;

import com.aspire.ums.cmdb.IpResource.entity.CmdbAssetInfoQuery;
import com.aspire.ums.cmdb.IpResource.mapper.CmdbAssetInfoMapper;
import com.aspire.ums.cmdb.IpResource.service.CmdbAssetInfoService;
import com.aspire.ums.cmdb.ipResource.payload.*;
import com.aspire.ums.cmdb.util.StringUtils;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/6/19 09:52
 */
@Slf4j
@Service("CmdbAssetInfoService")
public class CmdbAssetInfoServiceImpl implements CmdbAssetInfoService {
    @Autowired
    private CmdbAssetInfoMapper assetInfoMapper;

    @Override
    public List<AssetInfoResp> getAssetList(AssetInfoParam param) {
        CmdbAssetInfoQuery query = new CmdbAssetInfoQuery();
        BeanUtils.copyProperties(param, query);
        if (StringUtils.isNotEmpty(param.getIps())) {
            query.setManageIpArr(param.getIps().split(","));
        }
        return assetInfoMapper.getAssetList(query);
    }

    @Override
    public Integer getAssetListCount(AssetInfoParam param) {
        CmdbAssetInfoQuery query = new CmdbAssetInfoQuery();
        BeanUtils.copyProperties(param, query);
        if (StringUtils.isNotEmpty(param.getIps())) {
            query.setManageIpArr(param.getIps().split(","));
        }
        return assetInfoMapper.getAssetListCount(query);
    }

    @Override
    public List<AssetInfoBackfillResp> getBackfillAsset(AssetInfoParam param) {
        CmdbAssetInfoQuery query = new CmdbAssetInfoQuery();
        BeanUtils.copyProperties(param, query);
        // query.setAssetIdArr(param.getAssetIds().split(","));
        if (StringUtils.isNotEmpty(param.getIps())) {
            query.setManageIpArr(param.getIps().split(","));
        }
        List<AssetInfoBackfillResp> assetList = assetInfoMapper.getBackfillAsset(query);
        // 处理ip中的特殊符号
        assetList.stream().forEach(item -> item.setRecycleIps(handleOtherIp(item.getRecycleIps(), param.getRecoveryType(), item)));
        return assetList;
    }

    @Override
    public Integer getBackfillAssetCount(AssetInfoParam param) {
        CmdbAssetInfoQuery query = new CmdbAssetInfoQuery();
        BeanUtils.copyProperties(param, query);
        // query.setAssetIdArr(param.getAssetIds().split(","));
        if (StringUtils.isNotEmpty(param.getIps())) {
            query.setManageIpArr(param.getIps().split(","));
        }
        return assetInfoMapper.getBackfillAssetCount(query);
    }

    @Override
    public List<AssetInfoIpResp> getAssetIpList(AssetIpInfoParam param) {
        CmdbAssetInfoQuery query = new CmdbAssetInfoQuery();
        BeanUtils.copyProperties(param, query);
        query.setManageIpArr(StringUtils.isEmpty(param.getManageIp()) ? null : param.getManageIp().split(","));
        query.setIps(StringUtils.isEmpty(param.getIps()) ? null : param.getIps().split(","));
        query.setIpTypeArr(param.getIpType().split(","));
        List<AssetInfoIpResp> dataList = assetInfoMapper.getAssetIpList(query);
        // 处理ip中的特殊符号
        dataList.stream().forEach(item -> item.setIp(handleOtherIp(item.getIp(), param.getRecoveryType(), item)));

        // 拆分多个其他IP
        List<AssetInfoIpResp> dataList1 = new ArrayList<>();
        for (AssetInfoIpResp data : dataList) {
            String ip = data.getIp();
            String[] split = ip.split(",");
            if (split.length > 1) {
                for (String s : split) {
                    AssetInfoIpResp data1 = new AssetInfoIpResp();
                    BeanUtils.copyProperties(data,data1);
                    data1.setIp(s);
                    dataList1.add(data1);
                }
            } else {
                dataList1.add(data);
            }
        }
        return dataList1;
    }

    @Override
    public Integer getAssetIpListCount(AssetIpInfoParam param) {
        CmdbAssetInfoQuery query = new CmdbAssetInfoQuery();
        BeanUtils.copyProperties(param, query);
        query.setManageIpArr(StringUtils.isEmpty(param.getManageIp()) ? null : param.getManageIp().split(","));
        query.setIps(StringUtils.isEmpty(param.getIps()) ? null : param.getIps().split(","));
        return assetInfoMapper.getAssetIpListCount(query);
    }

    @Override
    public void updateIsDelete(String assetIds) {
        assetInfoMapper.updateIsDelete(assetIds.split(","));
    }

    @Override
    public List<Map<String, String>> getKvmTemplateList(Map<String, String> param) {
        return assetInfoMapper.getKvmTemplateList(param);
    }

    @Override
    public List<Map<String, String>> getOneBusinessAndAloneBusiness(Map<String, String> param) {
        return assetInfoMapper.getOneBusinessAndAloneBusiness(param);
    }

    /**
     * 处理ip中的;和换行符
     *
     * @param ipStr
     * @param recoveryType
     * @param item
     * @return
     */
    private String handleOtherIp(String ipStr, String recoveryType, AssetInfoBaseResp item) {
        // 1、去除换行符和替换;
        String recycleIps = ipStr.replaceAll("(\r\n)|\n", ";").replaceAll(";", ",");
        // 2、去除多余空格，并拆分数组
        String[] array = recycleIps.replaceAll(" ", "").split(",");
        // 3、过滤空串并去重
        Set<String> ipSet = Stream.of(array).filter(e -> StringUtils.isNotEmpty(e)).collect(Collectors.toSet());
        // 4、ip回收需去除管理ip和业务ip1
        // if (("2").equals(recoveryType)) {
        //     ipSet = ipSet.stream().filter(e -> (!e.equals(item.getManageIp()) || !e.equals(item.getBusinessIp1()))).collect(Collectors.toSet());
        // }
        return Joiner.on(",").join(ipSet);
    }

}

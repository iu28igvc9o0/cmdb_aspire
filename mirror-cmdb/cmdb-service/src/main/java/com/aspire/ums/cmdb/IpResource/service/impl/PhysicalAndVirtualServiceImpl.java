package com.aspire.ums.cmdb.IpResource.service.impl;

import com.aspire.ums.cmdb.IpResource.entity.*;
import com.aspire.ums.cmdb.IpResource.mapper.CmdbAddressLibraryMapper;
import com.aspire.ums.cmdb.IpResource.service.PhysicalAndVirtualService;
import com.aspire.ums.cmdb.dict.mapper.ConfigDictMapper;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.ipResource.payload.*;
import com.aspire.ums.cmdb.sync.entity.CmdbBusinessLine;
import com.aspire.ums.cmdb.sync.mapper.CmdbBusinessLineMapper;
import com.aspire.ums.cmdb.util.IpUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.idc.mapper.CmdbIdcManagerMapper;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/6/16 18:16
 */
@Slf4j
@Service("PhysicalAndVirtualService")
public class PhysicalAndVirtualServiceImpl implements PhysicalAndVirtualService {
    @Autowired
    private CmdbAddressLibraryMapper addressLibraryMapper;
    @Autowired
    private CmdbBusinessLineMapper businessLineMapper;// 独立业务线
    @Autowired
    private ConfigDictMapper configDictMapper; //字典表

    @Override
    public List<CmdbNetworkSegmentEntity> getNetworkSegmentList(SegmentInfoParam param) {
        CmdbNetworkSegmentQuery segmentQuery = new CmdbNetworkSegmentQuery();
        BeanUtils.copyProperties(param, segmentQuery);
        // 反查中文名
        // if(!StringUtils.isEmpty(param.getAloneBusiness())) {
        //     CmdbBusinessLine cmdbBusinessLine = businessLineMapper.getByBusinessCode(param.getAloneBusiness());
        //     segmentQuery.setAloneBusiness(cmdbBusinessLine.getBusinessName());
        // }
        // CmdbIdcManager cmdbIdcManager = idcManagerMapper.getByIdcCode(param.getIdcVal());
        // segmentQuery.setIdcVal(cmdbIdcManager.getIdcName());
        return addressLibraryMapper.findNetworkSegment(segmentQuery);
    }

    @Override
    public Integer getNetworkSegmentListCount(SegmentInfoParam param) {
        CmdbNetworkSegmentQuery segmentQuery = new CmdbNetworkSegmentQuery();
        BeanUtils.copyProperties(param, segmentQuery);
        return addressLibraryMapper.findNetworkSegmentCount(segmentQuery);
    }

    @Override
    public List<CmdbIpAddressEntity> getIpAddressList(IpInfoParam param) {
        CmdbIpAddressQuery query = new CmdbIpAddressQuery();
        BeanUtils.copyProperties(param, query);
        // 如果不是物理机子表也不是管理网段的 才查询未分配状态的ip
        if(!"管理网段".equals(param.getSegmentType()) && !"1".equals(param.getDeviceType())) {
            query.setAssignStatus("未分配");
        }
        return addressLibraryMapper.findIpAddress(query);
    }

    @Override
    public Integer getIpAddressListCount(IpInfoParam param) {
        CmdbIpAddressQuery query = new CmdbIpAddressQuery();
        BeanUtils.copyProperties(param, query);
        // 如果不是物理机子表也不是管理网段的 才查询未分配状态的ip
        if(!"管理网段".equals(param.getSegmentType()) && !"1".equals(param.getDeviceType())) {
            query.setAssignStatus("未分配");
        }
        return addressLibraryMapper.findIpAddressCount(query);
    }

    @Override
    public Map<String, Object> ipAutoAssign(AutoAllocateIpParam param) {
        // 查询网段
        SegmentInfoParam segmentInfoParam = new SegmentInfoParam();
        segmentInfoParam.setAloneBusiness(param.getAloneBusiness());
        segmentInfoParam.setIdcVal(param.getIdcVal());
        segmentInfoParam.setDeviceCount(param.getDeviceCount());
        List<CmdbNetworkSegmentEntity> segmentEntities = this.getNetworkSegmentList(segmentInfoParam);
        // 自动分配IP
        Map<String, Object> resultMap = Maps.newHashMap();
        if (param.getDeviceType().equals("1")) {
            resultMap.put("manageIpList", Joiner.on(",").join(assignIP(segmentEntities, "1", param, "1")));
            // resultMap.put("businessIp1List", Joiner.on(",").join(assignIP(segmentEntities, "2", param)));
            // resultMap.put("businessIp2List", "");
            resultMap.put("consoleIpList", Joiner.on(",").join(assignIP(segmentEntities, "4", param, "1")));
        } else {
            resultMap.put("manageIpList", Joiner.on(",").join(assignIP(segmentEntities, "1", param, "1")));
            resultMap.put("businessIp1List", Joiner.on(",").join(assignIP(segmentEntities, "2", param, "1")));
            // resultMap.put("businessIp2List", "");
            List<String> segmentList = assignIP(segmentEntities, "2", param, "2");
            resultMap.put("segmentList", segmentList.isEmpty() ? "" : segmentList.get(0));
        }
        return resultMap;
    }

    @Override
    public List<SegmentIpInfoResp> getSegmentIpList(SegmentIpInfoParam param) {
        SegmentIpInfoQuery query = new SegmentIpInfoQuery();
        BeanUtils.copyProperties(param, query);
        query.setAssignStatus("未分配");
        query.setIpArr(StringUtils.isEmpty(param.getIps()) ? null : param.getIps().split(","));
        return addressLibraryMapper.getSegmentIpList(query);
    }

    @Override
    public Integer getSegmentIpListCount(SegmentIpInfoParam param) {
        SegmentIpInfoQuery query = new SegmentIpInfoQuery();
        BeanUtils.copyProperties(param, query);
        query.setAssignStatus("未分配");
        query.setIpArr(StringUtils.isEmpty(param.getIps()) ? null : param.getIps().split(","));
        return addressLibraryMapper.getSegmentIpListCount(query);
    }

    @Override
    public void updateIpInfo(IpInfoUpdateParam param) {
        CmdbIpAddressUpdate update = new CmdbIpAddressUpdate();
        BeanUtils.copyProperties(param, update);
        // 获取独立业务
//        List<CmdbBusinessLine> aloneList = businessLineMapper.aloneList();
        // 获取独立业务子模块
//        List<CmdbBusinessLine> subBusinessList = businessLineMapper.subBusinessList();
        // 分配状态 - 字典表 - ipAllocationStatusType
        List<ConfigDict> dictList1 = configDictMapper.selectDictsByTypeOrPid("ipAllocationStatusType", "");
        String assignStatus = param.getAssignStatus();
        if (StringUtils.isNotEmpty(assignStatus)) {
            for (ConfigDict configDict : dictList1) {
                if (configDict.getValue().equals(assignStatus)) {
                    update.setAssignStatus(configDict.getId());
                }
            }
        }
        // 存活状态 - 字典表 - survival_status
        List<ConfigDict> dictList2 = configDictMapper.selectDictsByTypeOrPid("survival_status", "");
        String survivalStatus = param.getSurvivalStatus();
        if (StringUtils.isNotEmpty(survivalStatus)) {
            for (ConfigDict configDict : dictList2) {
                if (configDict.getValue().equals(survivalStatus)) {
                    update.setSurvivalStatus(configDict.getId());
                }
            }
        }
        // 是否录入CMDB - 字典表 - whether
//        List<ConfigDict> dictList3 = configDictMapper.selectDictsByTypeOrPid("whether", "");

        param.getIpIdcRelMap().forEach(maps -> {
            String[] ipArr = maps.getIps().split(",");
            List<String> ipList = Lists.newArrayList();
            List<String> ipv6List = Lists.newArrayList();
            Arrays.stream(ipArr).forEach(ip -> {
                if (IpUtils.isValidIpv6Addr(ip)) {
                    ipv6List.add(ip);
                } else {
                    ipList.add(ip);
                }
            });

            update.setIdcVal(maps.getIdc());
            if (!CollectionUtils.isEmpty(ipv6List)) {
                update.setIps(ipv6List);
                addressLibraryMapper.updateIpv6Ip(update);
            }
            if (!CollectionUtils.isEmpty(ipList)) {
                update.setIps(ipList);
                addressLibraryMapper.updateInnerIp(update);
                addressLibraryMapper.updatePublicIp(update);
            }
        });
    }

    /**
     * 自动分配IP
     *
     * @param segmentEntities 网段地址实体列表
     * @param type            类型
     * @param param           入参实体
     * @return
     */
    private List<String> assignIP(List<CmdbNetworkSegmentEntity> segmentEntities, String type, AutoAllocateIpParam param, String rtnType) {
        Integer deviceCount = param.getDeviceCount();
        String deviceType = param.getDeviceType();
        List<CmdbNetworkSegmentEntity> filterEntity;
        CmdbIpAddressQuery query = new CmdbIpAddressQuery();
        query.setAssignStatus("未分配");
        switch (type) {
            case "1": // 管理IP 默认自动分配内网地址的管理网段->带内，用途子类为 1-“宿主机带内管理(EXSI)” 2-"虚拟机带内管理-VMware" 的IP
                filterEntity = segmentEntities.stream().filter(item -> item.getInnerSegmentType().contains("管理网段") && item.getInnerSegmentSubType().contains("带内")).collect(Collectors.toList());
                if (deviceType.equals("1")) {
                    query.setInnerIpSubUse("宿主机带内管理(EXSI)");
                } else {
                    query.setInnerIpSubUse("虚拟机带内管理");//虚拟机带内管理-VMware
                }
                break;
            case "2": // 业务IP1 默认自动分配内网地址的业务网段，用途分类为 1-“物理机” 2-"虚拟机-VMware" 的IP
                filterEntity = segmentEntities.stream().filter(item -> item.getInnerSegmentType().contains("业务网段")).collect(Collectors.toList());
                if (deviceType.equals("1")) {
                    query.setInnerIpUse("物理机");
                } else {
                    query.setInnerIpUse("虚拟机-VMware");
                }
                break;
            // case "3": // 业务IP2 默认自动不分配IP
            //     break;
            case "4": // console IP 默认自动分配内网地址的管理网段->带外，用途分类为“宿主机带外管理”的IP
                filterEntity = segmentEntities.stream().filter(item -> item.getInnerSegmentType().contains("管理网段") && item.getInnerSegmentSubType().contains("带外")).collect(Collectors.toList());
                query.setInnerIpUse("宿主机带外管理");
                break;
            default:
                filterEntity = Lists.newArrayList();
                break;
        }
        List<String> segmentList = filterEntity.stream().map(e -> e.getInstanceId()).collect(Collectors.toList());
        // 查询未分配IP
        List<String> ipList = Lists.newArrayList();
        /*for (String segmentId : segmentList) {
            if (ipList.size() < deviceCount) {
                query.setSegmentId(segmentId);
                List<CmdbIpAddressEntity> ipAddressEntities = addressLibraryMapper.findIpAddress(query);
                ipList.addAll(ipAddressEntities.stream().map(e -> e.getIp()).collect(Collectors.toList()));
            } else {
                break;
            }
        }*/
        for (String segmentId : segmentList) {
            query.setSegmentId(segmentId);
            List<CmdbIpAddressEntity> ipAddressEntities = addressLibraryMapper.findIpAddress(query);
            if (ipAddressEntities.size() >= deviceCount) {
                if (rtnType.equals("1")) {//返回ip
                    ipList.addAll(ipAddressEntities.stream().map(e -> e.getIp()).collect(Collectors.toList()));
                    break;
                } else {
                    ipList.addAll(filterEntity.stream().filter(e -> e.getInstanceId().equals(segmentId))
                            .map(e -> e.getSegmentAddress()).collect(Collectors.toList()));
                }
            }
        }
        return ipList.size() <= deviceCount ? ipList : ipList.subList(0, deviceCount);
    }

}

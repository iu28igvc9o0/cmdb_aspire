package com.aspire.mirror.alert.server.biz.scanComparision.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.mirror.alert.server.biz.scanComparision.AlertScanComparisionBiz;
import com.aspire.mirror.alert.server.clientservice.CmdbCollectUnknowClient;
import com.aspire.mirror.alert.server.dao.scanComparision.AlertScanComparisionDao;
import com.aspire.mirror.alert.server.vo.scanComparision.AlertScanComparisionDetailVo;
import com.aspire.mirror.alert.server.vo.scanComparision.AlertScanComparisionReqVo;
import com.aspire.mirror.alert.server.util.StringUtils;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknown;
import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknownQuery;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class AlertScanComparisionBizImpl implements AlertScanComparisionBiz {

    @Autowired
    private AlertScanComparisionDao alertScanComparisionDao;
    @Autowired
    private CmdbCollectUnknowClient cmdbCollectUnknowClient;

    @Override
    public PageResponse<AlertScanComparisionDetailVo> getScanComparisionList(AlertScanComparisionReqVo request) {
        int pageNum = request.getPageNum();
        int pageSize = request.getPageSize();
        request.setIndex((pageNum - 1) * pageSize);
        PageResponse<AlertScanComparisionDetailVo> response = new PageResponse<AlertScanComparisionDetailVo>();
        response.setCount(alertScanComparisionDao.getScanComparisionCount(request));
        response.setResult(alertScanComparisionDao.getScanComparisionList(request));
        return response;
    }

    @Override
    public void deleteScanComparisionById(List<String> request) {
        alertScanComparisionDao.deleteScanComparisionById(request);
    }

    @Override
    public void synScanComparision(List<Map<String, String>> request) {
        List<CmdbCollectUnknown> cmdbCollectUnknowns = Lists.newArrayList();
        for (Map<String, String> map : request) {
            CmdbCollectUnknown cmdbCollectUnknown = new CmdbCollectUnknown();
            cmdbCollectUnknown.setIp(map.get("deviceIp"));
            cmdbCollectUnknown.setIdcType(map.get("idcType"));
            cmdbCollectUnknown.setDataFrom("扫描对账");
            cmdbCollectUnknown.setCommitUser(map.get("userName"));
            cmdbCollectUnknown.setCommitTime(new Date());
            cmdbCollectUnknowns.add(cmdbCollectUnknown);
        }
        // 调用cmdb接口同步数据
        List<Map<String, Object>> maps = cmdbCollectUnknowClient.insertByBatch(cmdbCollectUnknowns);
        for (Map<String, Object> map : maps) {
            if (!(boolean)map.get("success")) {
                log.error("[Alert-Service >>> ]Syn Scan Comparision is error {}",map.get("msg"));
            }
        }
        // 修改同步状态
        alertScanComparisionDao.updateSynStatus(maps);
    }

    @Override
    public List<Map<String, Object>> exportScanComparision(AlertScanComparisionReqVo request) {
        return alertScanComparisionDao.exportScanComparision(request);
    }

    @Override
    public void alertScanComparisonSchedule() {
        // 定时扫描告警表，查询出来设备id为空的数据
        List<Map<String, String>> alerts = alertScanComparisionDao.getAlerts();
        List<AlertScanComparisionDetailVo> insert = Lists.newArrayList();
        for (Map<String, String> map : alerts) {
            // 判断数据是否在告警扫描对账表中是否存在
            AlertScanComparisionDetailVo alertScanComparisionDetailByIpAndPool =
                    alertScanComparisionDao.getAlertScanComparisionDetailByIpAndPool(map.get("deviceIp"), map.get("idcType"));
            if (null == alertScanComparisionDetailByIpAndPool) {
                AlertScanComparisionDetailVo alertScanComparisionDetailVo = new AlertScanComparisionDetailVo();
                alertScanComparisionDetailVo.setId(UUID.randomUUID().toString());
                alertScanComparisionDetailVo.setDeviceIp(map.get("deviceIp"));
                alertScanComparisionDetailVo.setIdcType(map.get("idcType"));
                alertScanComparisionDetailVo.setSynStatus("2");
                alertScanComparisionDetailVo.setCurMoniTime(map.get("curMoniTime"));
                insert.add(alertScanComparisionDetailVo);
            } else {
                alertScanComparisionDao.updateById(alertScanComparisionDetailByIpAndPool.getId(),map.get("curMoniTime"));
            }
        }
        // 数据库
        if (CollectionUtils.isNotEmpty(insert)) alertScanComparisionDao.insertScanComparision(insert);
    }
    
  

    @Override
    public void compareCmdb() {
        // 获取未同步的数据
        AlertScanComparisionReqVo dto = new AlertScanComparisionReqVo();
        dto.setSynStatus("2");
        List<Map<String, Object>> maps = alertScanComparisionDao.exportScanComparision(dto);
        List<String> ids = Lists.newArrayList();
        for (Map<String, Object> map : maps) {
            // 获取cmdb：未知设备数据
            String device = String.valueOf(map.get("deviceIp"));
            CmdbCollectUnknownQuery cmdbCollectUnknownQuery = new CmdbCollectUnknownQuery();
            cmdbCollectUnknownQuery.setIp(device);
            if (null != map.get("idcType")) {
                device += String.valueOf(map.get("idcType"));
                cmdbCollectUnknownQuery.setIdcType(String.valueOf(map.get("idcType")));
            }
            Result<CmdbCollectUnknown> list = cmdbCollectUnknowClient.list(cmdbCollectUnknownQuery);
            // 判断未知设备列表是否已经存在
            if (list.getCount() > 0) {
                List<String> devices = this.getDeviceIps(list.getData());
                if (devices.contains(device)) ids.add(String.valueOf(map.get("id")));
            }

        }
        // 删除已经存在未知设备中的数据
        if (CollectionUtils.isNotEmpty(ids)) alertScanComparisionDao.deleteScanComparisionById(ids);
    }

    private List<String> getDeviceIps (List<CmdbCollectUnknown> data) {
        List<String> response = Lists.newArrayList();
        for (CmdbCollectUnknown cmdbCollectUnknown : data) {
            String device = cmdbCollectUnknown.getIp();
            if (StringUtils.isNotEmpty(cmdbCollectUnknown.getIdcType())) device += cmdbCollectUnknown.getIdcType();
            response.add(device);
        }
        return response;
    }
}

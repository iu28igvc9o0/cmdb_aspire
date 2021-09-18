package com.aspire.mirror.alert.server.biz.faultReport.impl;

import com.aspire.mirror.alert.server.biz.faultReport.IFaultReportManageBiz;
import com.aspire.mirror.alert.server.dao.faultReport.FaultReportManageMapper;
import com.aspire.mirror.alert.server.dao.faultReport.po.FaultReportManage;
import com.aspire.mirror.common.entity.PageResponse;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FaultReportManageBizImpl implements IFaultReportManageBiz {
    @Autowired
    private FaultReportManageMapper faultReportManageMapper;

    @Override
    public void insert(FaultReportManage manage) {
        setTimely(manage);
        faultReportManageMapper.insert(manage);
    }

    @Override
    public void update(FaultReportManage manage) {
        setTimely(manage);
        faultReportManageMapper.update(manage);
    }

    private void setTimely (FaultReportManage manage) {
        Date now = new Date();
        Date reportFinishTime = manage.getReportFinishTime();
        Date reportPlainFinish = manage.getReportPlainFinish();
        if (reportPlainFinish == null && reportFinishTime == null) {
            manage.setReportTimely(null);
        } else if (reportPlainFinish == null) {
            manage.setReportTimely("1");
        } else if ((reportPlainFinish.before(now) && reportFinishTime == null) || (reportFinishTime != null && reportPlainFinish.before(reportFinishTime))) {
            manage.setReportTimely("2");
        } else {
            manage.setReportTimely("1");
        }

        Date faultReportTime = manage.getFaultReportTime();
        Date faultHappenTime = manage.getFaultHappenTime();
        if (faultReportTime == null && faultHappenTime == null) {
            manage.setFaultReportTimely(null);
        } else  if (faultReportTime != null && faultHappenTime != null && (faultReportTime.getTime() - faultHappenTime.getTime()) > 30 * 60 * 1000) {
            manage.setFaultReportTimely("2");
        } else {
            manage.setFaultReportTimely("1");
        }
        Date reportPlatformRecoverytime = manage.getReportPlatformRecoverytime();
        if (reportPlatformRecoverytime != null && faultHappenTime != null && reportPlatformRecoverytime.after(faultHappenTime)) {
            double v = (reportPlatformRecoverytime.getTime() - faultHappenTime.getTime()) / 36000 / 100.0;
            manage.setReportPlatformRecoveryhours(v);
        } else {
            manage.setReportPlatformRecoveryhours(null);
        }
    }

    @Override
    public FaultReportManage selectById(Integer id) {
        FaultReportManage manage = faultReportManageMapper.selectById(id);
        setTimely(manage);
        return manage;
    }

    @Override
    public List<FaultReportManage> selectByIds(List<Integer> ids) {
        List<FaultReportManage> manageList = faultReportManageMapper.selectByIds(ids);
        for (FaultReportManage manage: manageList) {
            setTimely(manage);
        }
        return manageList;
    }

    @Override
    public PageResponse<FaultReportManage> selectListByParams(String faultReportUser,
                                                              String reportUser,
                                                              String faultHappenTimeFrom,
                                                              String faultHappenTimeTo,
                                                              Integer pageSize,
                                                              Integer pageNum) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("faultReportUser", faultReportUser);
        params.put("reportUser", reportUser);
        params.put("faultHappenTimeFrom", faultHappenTimeFrom);
        params.put("faultHappenTimeTo", faultHappenTimeTo);
        params.put("pageSize", pageSize);
        params.put("begin", (pageNum - 1) * pageSize);
        List<FaultReportManage> faultReportManageList = faultReportManageMapper.pageList(params);
        for (FaultReportManage manage: faultReportManageList) {
            setTimely(manage);
        }
        PageResponse<FaultReportManage> pageResponse = new PageResponse<>();
        pageResponse.setCount(faultReportManageMapper.pageCount(params));
        pageResponse.setResult(faultReportManageList);
        return pageResponse;
    }

    public List<FaultReportManage> queryExports(String faultReportUser,
                                                String reportUser,
                                                String faultHappenTimeFrom,
                                                String faultHappenTimeTo) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("faultReportUser", faultReportUser);
        params.put("reportUser", reportUser);
        params.put("faultHappenTimeFrom", faultHappenTimeFrom);
        params.put("faultHappenTimeTo", faultHappenTimeTo);
        List<FaultReportManage> manageList = faultReportManageMapper.pageList(params);
        for (FaultReportManage manage: manageList) {
            setTimely(manage);
        }
        return manageList;
    }
}

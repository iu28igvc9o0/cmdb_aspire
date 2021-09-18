package com.aspire.mirror.alert.server.biz.faultReport;

import com.aspire.mirror.alert.server.dao.faultReport.po.FaultReportManage;
import com.aspire.mirror.common.entity.PageResponse;

import java.util.List;

public interface IFaultReportManageBiz {
    void insert(FaultReportManage manage);

    void update(FaultReportManage manage);

    FaultReportManage selectById(Integer id);

    List<FaultReportManage> selectByIds(List<Integer> ids);

    PageResponse<FaultReportManage> selectListByParams(String faultReportUser,
                                                       String reportUser,
                                                       String faultHappenTimeFrom,
                                                       String faultHappenTimeTo,
                                                       Integer pageSize,
                                                       Integer pageNum);

    List<FaultReportManage> queryExports(String faultReportUser,
                                                       String reportUser,
                                                       String faultHappenTimeFrom,
                                                       String faultHappenTimeTo);
}

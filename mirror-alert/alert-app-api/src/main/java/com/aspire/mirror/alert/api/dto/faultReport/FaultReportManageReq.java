package com.aspire.mirror.alert.api.dto.faultReport;

import lombok.Data;

import java.util.Date;

@Data
public class FaultReportManageReq {
    private Integer id;
    private String faultId;
    private String faultReportUser;
    private String faultReporMobile;
    private String faultReportEmail;
    private String faultReportBizsys;
    private Date faultReportTime;
    private Date faultHappenTime;
    private String faultReportTimely;
    private String faultIdcType;
    private String faultOrderId;
    private String faultContent;
    private Date faultRegtime;
    private String reportTitle;
    private String reportUser;
    private String reportMobile;
    private String reportEmail;
    private String reportBizsys;
    private String reportWnId;
    private String reportOrderId;
    private Date reportCreateTime;
    private Date reportPlainFinish;
    private Date reportFinishTime;
    private String reportTimely;
    private Date reportPlatformRecoverytime;
    private Double reportPlatformRecoveryhours;
    private Date reportBizsysRecoverytime;
    private Double reportBizsysRecoveryhours;
    private Integer reportAffectBizsyss;
    private String reportAffectDescribe;
    private String reportType;
    private String reportResson;
    private String reportProducer;
    private String reportNature;
    private String reportDeductPoints;
    private String reportEnclosure;
    private String reportFollowPlan;
    private String reportFollowPlanExplain;
    private String reportRemark;
}

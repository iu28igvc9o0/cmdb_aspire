package com.migu.tsg.microservice.atomicservice.composite.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 工单配置
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.config
 * 类名称:    OrderConfig.java
 * 类描述:    工单配置
 * 创建人:    JinSu
 * 创建时间:  2019/9/2 11:44
 * 版本:      v1.0
 */
@Component
@ConfigurationProperties(prefix = "order.config")
public class OrderConfig implements Serializable {
    private String orderBase;

    private String orderBaseForPortal;

    private String orderBaseForModel;

    private String trendsUrl;

    private String allTypeUrl;

    private String distributionUrl;

    private String orderNumUrl;

    private String orderStatisticsUrl;

    private String instListByDeviceUrl;

    private String instOverviewUrl;

    private String homepageProInstAnalysisUrl;

    private String instRuntimeAnalysisUrl;

    private String getInstListByDefKeyUrl;

    private String getInstListByDefKeyAndStatusUrl;
    
    private String recentAnnouncementUrl;
    
    private String getBpmInsDataUrl;

    private String getBpmInsDataNewUrl;

    private String reminderUrl;

    private String publicNoticeFindListUrl;

    private String publicNoticeFindListHomePageUrl;

    private String publicNoticeCreateUrl;

    private String publicNoticeNoticeUrl;

    private String dutyCheckInUrl;

    private String deskLogsUrl;

    private String publicNoticeUpdateStatusUrl;

    private String startWithZxgllcUrl;

    private String startDraftWithzxgllc;

    private String statistWithZxgllcUrl;

    private String listWithZxgllcUrl;

    private String remindListUrl;

    private String dropDataWithZxgllcUrl;

    private String dropDataToDepWithZxgllcUrl;

    private String dropDataToUserWithZxgllcUrl;

    private String exportRemindListUrl;

    private String orderTimelinessGroupByTenantUrl;
    private String orderTimelinessStatisticsUrl;
    private String monthInTimeRateUrl;
    private String allTypeOrderUrl;
    
    //UMS流程首页
    private String bpmInsData;
    private String officialDocumentList;
    private String instEfficiencyShow;
    private String instEfficiencyReport;
    private String workTop;
    private String workAssessmentReport;
    private String departmentInstCloseInfo;
    private String instSearch;
    private String alarmStatistics;

    public String getPublicNoticeNoticeUrl() {
        return orderBase + publicNoticeNoticeUrl;
    }

    public void setPublicNoticeNoticeUrl(String publicNoticeNoticeUrl) {
        this.publicNoticeNoticeUrl = publicNoticeNoticeUrl;
    }

    public String getPublicNoticeUpdateStatusUrl() {
        return orderBase + publicNoticeUpdateStatusUrl;
    }

    public void setPublicNoticeUpdateStatusUrl(String publicNoticeUpdateStatusUrl) {
        this.publicNoticeUpdateStatusUrl = publicNoticeUpdateStatusUrl;
    }

    public String getBpmInsData() {
		return orderBase + bpmInsData;
	}

	public void setBpmInsData(String bpmInsData) {
		this.bpmInsData = bpmInsData;
	}

	public String getOfficialDocumentList() {
		return orderBase + officialDocumentList;
	}

	public void setOfficialDocumentList(String officialDocumentList) {
		this.officialDocumentList = officialDocumentList;
	}

	public String getInstEfficiencyShow() {
		return orderBase + instEfficiencyShow;
	}

	public void setInstEfficiencyShow(String instEfficiencyShow) {
		this.instEfficiencyShow = instEfficiencyShow;
	}

	public String getInstEfficiencyReport() {
		return orderBase + instEfficiencyReport;
	}

	public void setInstEfficiencyReport(String instEfficiencyReport) {
		this.instEfficiencyReport = instEfficiencyReport;
	}

	public String getWorkTop() {
		return orderBase + workTop;
	}

	public void setWorkTop(String workTop) {
		this.workTop = workTop;
	}

	public String getWorkAssessmentReport() {
		return orderBase + workAssessmentReport;
	}

	public void setWorkAssessmentReport(String workAssessmentReport) {
		this.workAssessmentReport = workAssessmentReport;
	}

	public String getDepartmentInstCloseInfo() {
		return orderBase + departmentInstCloseInfo;
	}

	public void setDepartmentInstCloseInfo(String departmentInstCloseInfo) {
		this.departmentInstCloseInfo = departmentInstCloseInfo;
	}

	public String getInstSearch() {
		return orderBase + instSearch;
	}

	public void setInstSearch(String instSearch) {
		this.instSearch = instSearch;
	}

	public String getAlarmStatistics() {
		return orderBase + alarmStatistics;
	}

	public void setAlarmStatistics(String alarmStatistics) {
		this.alarmStatistics = alarmStatistics;
	}

	public String getOrderTimelinessGroupByTenantUrl() {
        return orderBase + orderTimelinessGroupByTenantUrl;
    }

    public void setOrderTimelinessGroupByTenantUrl(String orderTimelinessGroupByTenantUrl) {
        this.orderTimelinessGroupByTenantUrl = orderTimelinessGroupByTenantUrl;
    }

    public String getOrderTimelinessStatisticsUrl() {
        return orderBase + orderTimelinessStatisticsUrl;
    }

    public void setOrderTimelinessStatisticsUrl(String orderTimelinessStatisticsUrl) {
        this.orderTimelinessStatisticsUrl = orderTimelinessStatisticsUrl;
    }

    public String getMonthInTimeRateUrl() {
        return orderBase + monthInTimeRateUrl;
    }

    public void setMonthInTimeRateUrl(String monthInTimeRateUrl) {
        this.monthInTimeRateUrl = monthInTimeRateUrl;
    }

    public String getAllTypeOrderUrl() {
        return orderBase + allTypeOrderUrl;
    }

    public void setAllTypeOrderUrl(String allTypeOrderUrl) {
        this.allTypeOrderUrl = allTypeOrderUrl;
    }

    public String getGetBpmInsDataNewUrl() {
        return orderBase + getBpmInsDataNewUrl;
    }

    public void setGetBpmInsDataNewUrl(String getBpmInsDataNewUrl) {
        this.getBpmInsDataNewUrl = getBpmInsDataNewUrl;
    }

    public String getDropDataToUserWithZxgllcUrl() {
        return orderBase + dropDataToUserWithZxgllcUrl;
    }

    public void setDropDataToUserWithZxgllcUrl(String dropDataToUserWithZxgllcUrl) {
        this.dropDataToUserWithZxgllcUrl = dropDataToUserWithZxgllcUrl;
    }

    public String getExportRemindListUrl() {
        return orderBase + exportRemindListUrl;
    }

    public void setExportRemindListUrl(String exportRemindListUrl) {
        this.exportRemindListUrl = exportRemindListUrl;
    }

    public String getDropDataToDepWithZxgllcUrl() {
        return orderBase + dropDataToDepWithZxgllcUrl;
    }

    public void setDropDataToDepWithZxgllcUrl(String dropDataToDepWithZxgllcUrl) {
        this.dropDataToDepWithZxgllcUrl = dropDataToDepWithZxgllcUrl;
    }

    public String getDropDataWithZxgllcUrl() {
        return orderBase + dropDataWithZxgllcUrl;
    }

    public void setDropDataWithZxgllcUrl(String dropDataWithZxgllcUrl) {
        this.dropDataWithZxgllcUrl = dropDataWithZxgllcUrl;
    }

    public String getRemindListUrl() {
        return orderBase + remindListUrl;
    }

    public void setRemindListUrl(String remindListUrl) {
        this.remindListUrl = remindListUrl;
    }

    public String getStatistWithZxgllcUrl() {
        return orderBase + statistWithZxgllcUrl;
    }

    public void setStatistWithZxgllcUrl(String statistWithZxgllcUrl) {
        this.statistWithZxgllcUrl = statistWithZxgllcUrl;
    }

    public String getListWithZxgllcUrl() {
        return orderBase + listWithZxgllcUrl;
    }

    public void setListWithZxgllcUrl(String listWithZxgllcUrl) {
        this.listWithZxgllcUrl = listWithZxgllcUrl;
    }

    public String getStartWithZxgllcUrl() {
        return orderBase + startWithZxgllcUrl;
    }

    public void setStartWithZxgllcUrl(String startWithZxgllcUrl) {
        this.startWithZxgllcUrl = startWithZxgllcUrl;
    }

    public String getStartDraftWithzxgllc() {
        return orderBase + startDraftWithzxgllc;
    }

    public void setStartDraftWithzxgllc(String startDraftWithzxgllc) {
        this.startDraftWithzxgllc = startDraftWithzxgllc;
    }

    public String getGetInstDetailListForDeskUrl() {
        return orderBase + getInstDetailListForDeskUrl;
    }

    public void setGetInstDetailListForDeskUrl(String getInstDetailListForDeskUrl) {
        this.getInstDetailListForDeskUrl = getInstDetailListForDeskUrl;
    }

    private String getInstDetailListForDeskUrl;

    public String getInstListByDeviceUrl() {
        return instListByDeviceUrl;
    }

    public void setInstListByDeviceUrl(String instListByDeviceUrl) {
        this.instListByDeviceUrl = instListByDeviceUrl;
    }

    public String getAllFlowDefListUrl() {
        return allFlowDefListUrl;
    }

    public void setAllFlowDefListUrl(String allFlowDefListUrl) {
        this.allFlowDefListUrl = allFlowDefListUrl;
    }

    private String allFlowDefListUrl;

    public String getOrderBase() {
        return orderBase;
    }

    public void setOrderBase(String orderBase) {
        this.orderBase = orderBase;
    }

    public String getTrendsUrl() {
        return orderBase + trendsUrl;
    }

    public void setTrendsUrl(String trendsUrl) {
        this.trendsUrl = trendsUrl;
    }

    public String getAllTypeUrl() {
        return orderBaseForModel + allTypeUrl;
    }

    public void setAllTypeUrl(String allTypeUrl) {
        this.allTypeUrl = allTypeUrl;
    }

    public String getDistributionUrl() {
        return orderBase + distributionUrl;
    }

    public void setDistributionUrl(String distributionUrl) {
        this.distributionUrl = distributionUrl;
    }

    public String getOrderNumUrl() {
        return orderBase + orderNumUrl;
    }

    public void setOrderNumUrl(String orderNumUrl) {
        this.orderNumUrl = orderNumUrl;
    }

    public String getOrderStatisticsUrl() {
        return orderBase + orderStatisticsUrl;
    }

    public void setOrderStatisticsUrl(String orderStatisticsUrl) {
        this.orderStatisticsUrl = orderStatisticsUrl;
    }

    public String getInstOverviewUrl() {
        return orderBase + instOverviewUrl;
    }

    public void setInstOverviewUrl(String instOverviewUrl) {
        this.instOverviewUrl = instOverviewUrl;
    }

    public String getHomepageProInstAnalysisUrl() {
        return orderBase + homepageProInstAnalysisUrl;
    }

    public void setHomepageProInstAnalysisUrl(String homepageProInstAnalysisUrl) {
        this.homepageProInstAnalysisUrl = homepageProInstAnalysisUrl;
    }

    public String getInstRuntimeAnalysisUrl() {
        return orderBase + instRuntimeAnalysisUrl;
    }

    public void setInstRuntimeAnalysisUrl(String instRuntimeAnalysisUrl) {
        this.instRuntimeAnalysisUrl = instRuntimeAnalysisUrl;
    }

    public String getGetInstListByDefKeyUrl() {
        return orderBase + getInstListByDefKeyUrl;
    }

    public void setGetInstListByDefKeyUrl(String getInstListByDefKeyUrl) {
        this.getInstListByDefKeyUrl = getInstListByDefKeyUrl;
    }

    public String getGetInstListByDefKeyAndStatusUrl() {
        return orderBase + getInstListByDefKeyAndStatusUrl;
    }

    public void setGetInstListByDefKeyAndStatusUrl(String getInstListByDefKeyAndStatusUrl) {
        this.getInstListByDefKeyAndStatusUrl = getInstListByDefKeyAndStatusUrl;
    }

	public String getRecentAnnouncementUrl() {
		return orderBase + recentAnnouncementUrl;
	}

	public void setRecentAnnouncementUrl(String recentAnnouncementUrl) {
		this.recentAnnouncementUrl = recentAnnouncementUrl;
	}

	public String getGetBpmInsDataUrl() {
		return orderBase + getBpmInsDataUrl;
	}

	public void setGetBpmInsDataUrl(String getBpmInsDataUrl) {
		this.getBpmInsDataUrl = getBpmInsDataUrl;
	}
    public String getReminderUrl() {
        return orderBase + reminderUrl;
    }

    public void setReminderUrl(String reminderUrl) {
        this.reminderUrl = reminderUrl;
    }

    public String getPublicNoticeFindListUrl() {
        return orderBase + publicNoticeFindListUrl;
    }

    public void setPublicNoticeFindListUrl(String publicNoticeFindListUrl) {
        this.publicNoticeFindListUrl = publicNoticeFindListUrl;
    }

    public String getPublicNoticeFindListHomePageUrl() {
        return orderBase + publicNoticeFindListHomePageUrl;
    }

    public void setPublicNoticeFindListHomePageUrl(String publicNoticeFindListHomePageUrl) {
        this.publicNoticeFindListHomePageUrl = publicNoticeFindListHomePageUrl;
    }

    public String getPublicNoticeCreateUrl() {
        return orderBaseForModel + publicNoticeCreateUrl;
    }

    public void setPublicNoticeCreateUrl(String publicNoticeCreateUrl) {
        this.publicNoticeCreateUrl = publicNoticeCreateUrl;
    }

    public String getDutyCheckInUrl() {
        return orderBaseForPortal + dutyCheckInUrl;
    }

    public void setDutyCheckInUrl(String dutyCheckInUrl) {
        this.dutyCheckInUrl = dutyCheckInUrl;
    }

    public String getOrderBaseForPortal() {
        return orderBaseForPortal;
    }

    public void setOrderBaseForPortal(String orderBaseForPortal) {
        this.orderBaseForPortal = orderBaseForPortal;
    }

    public String getOrderBaseForModel() {
        return orderBaseForModel;
    }

    public void setOrderBaseForModel(String orderBaseForModel) {
        this.orderBaseForModel = orderBaseForModel;
    }

    public String getDeskLogsUrl() {
        return orderBaseForPortal + deskLogsUrl;
    }

    public void setDeskLogsUrl(String deskLogsUrl) {
        this.deskLogsUrl = deskLogsUrl;
    }
}

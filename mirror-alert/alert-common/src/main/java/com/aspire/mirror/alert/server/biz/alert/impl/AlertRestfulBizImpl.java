package com.aspire.mirror.alert.server.biz.alert.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.mirror.alert.server.biz.alert.AlertRestfulBiz;
import com.aspire.mirror.alert.server.dao.monthReport.AlertMonthReportSyncDao;
import com.aspire.mirror.alert.server.dao.alert.AlertRestfulDao;
import com.aspire.mirror.alert.server.dao.alert.po.AlertStandardization;
import com.aspire.mirror.alert.server.dao.alert.po.KpiBook;
import com.aspire.mirror.alert.server.dao.alert.po.KpiListData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class AlertRestfulBizImpl implements AlertRestfulBiz {
	@Autowired
	private AlertRestfulDao alertRestfulDao;

	@Autowired
	private AlertMonthReportSyncDao alertMonthReportSyncDao;

	@Override
	public void insertBookAlerts(AlertStandardization stand) {
		String condtions = stand.getConditions();
		if (StringUtils.isNotBlank(condtions)) {
			StringBuffer sb = new StringBuffer();
			String[] arr = condtions.split(";");
			for (String a : arr) {
				String[] cArr = a.split(":");
				if (cArr[0].equals("idcType")) {
					sb.append("and idc_type='" + cArr[1] + "'");
				}
				if (cArr[0].equals("pod")) {
					sb.append("and pod_name='" + cArr[1] + "'");
				}
				if (cArr[0].equals("roomId")) {
					sb.append("and source_room='" + cArr[1] + "'");
				}
				if (cArr[0].equals("source")) {
					sb.append("and source='" + cArr[1] + "'");
				}
			}
			stand.setConditions(sb.toString());
		}

		String extraCols = stand.getExtraCols();
		if (StringUtils.isNotBlank(extraCols)) {
			StringBuffer sb = new StringBuffer();
			String[] arr = extraCols.split(";");
			for (String a : arr) {

				String[] cArr = a.split(":");
				sb.append(",'").append(cArr[0]).append("'").append(" ").append(cArr[1]);

			}
			stand.setExtraCols(sb.toString().substring(1));
		}

		String displayCols = stand.getDisplayCols();

		StringBuffer sbCols = new StringBuffer();
		String[] arr = displayCols.split(";");
		for (String a : arr) {
			if (a.equals("idcType")) {
				sbCols.append(",idc_type");
			}
			if (a.equals("pod")) {
				sbCols.append(",pod_name");
			}
			if (a.equals("roomId")) {
				sbCols.append(",source_room");
			}
			if (a.equals("alertId")) {
				sbCols.append(",alert_id");
			}
			if (a.equals("deviceId")) {
				sbCols.append(",device_id");
			}
			if (a.equals("deviceIp")) {
				sbCols.append(",device_ip");
			}
			if (a.equals("deviceClass")) {
				sbCols.append(",device_class");
			}
			if (a.equals("deviceType")) {
				sbCols.append(",device_type");
			}
			if (a.equals("deviceMfrs")) {
				sbCols.append(",device_mfrs");
			}
			if (a.equals("deviceModel")) {
				sbCols.append(",device_model");
			}
			if (a.equals("hostName")) {
				sbCols.append(",host_name");
			}
			if (a.equals("projectName")) {
				sbCols.append(",project_name");
			}
			if (a.equals("bizSys")) {
				sbCols.append(",biz_sys");
			}
			if (a.equals("moniObject")) {
				sbCols.append(",moni_object");
			}
			if (a.equals("moniIndex")) {
				sbCols.append(",moni_index");
			}
			if (a.equals("alertLevel")) {
				sbCols.append(",alert_level");
			}
			if (a.equals("curMoni_time")) {
				sbCols.append(",cur_moni_time");
			}
			if (a.equals("curMoni_value")) {
				sbCols.append(",cur_moni_value");
			}
			if (a.equals("itemId")) {
				sbCols.append(",item_id");
			}
			if (a.equals("alertStartTime")) {
				sbCols.append(",alert_start_time");
			}
			if (a.equals("alertEndTime")) {
				sbCols.append(",alert_end_time");
			}
			if (a.equals("remark")) {
				sbCols.append(",remark");
			}
			if (a.equals("objectType")) {
				sbCols.append(",object_type");
			}

		}
		stand.setDisplayCols(sbCols.toString().substring(1));

		alertRestfulDao.insertBookAlerts(stand);
	}

	@Override
	public List<AlertStandardization> getBookAlerts(String source) {
		return alertRestfulDao.getBookAlerts(source);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void insertBookKpiList(KpiBook kpi) {

		alertRestfulDao.insertKpiBook(kpi);
		List<KpiListData> list = kpi.getKpiList();
		for (KpiListData d : list) {
			d.setKpi_id(kpi.getId());
		}
		alertRestfulDao.insertKpiList(list);
	}

}

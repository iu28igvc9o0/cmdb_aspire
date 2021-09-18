package com.aspire.mirror.inspection.server.biz.impl;

import static java.lang.String.format;
import static org.apache.commons.lang.StringUtils.join;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.aspire.mirror.inspection.server.clientservice.OpsServiceClient;
import com.aspire.mirror.inspection.server.clientservice.payload.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.mirror.common.util.DateUtil;
import com.aspire.mirror.inspection.api.dto.model.ReportDTO;
import com.aspire.mirror.inspection.api.dto.model.ReportItemDTO;
import com.aspire.mirror.inspection.api.dto.model.TaskDTO;
import com.aspire.mirror.inspection.api.dto.model.TaskObjectDTO;
import com.aspire.mirror.inspection.server.biz.ReportBiz;
import com.aspire.mirror.inspection.server.biz.ReportItemBiz;
import com.aspire.mirror.inspection.server.biz.TaskBiz;
import com.aspire.mirror.inspection.server.biz.TaskExecuteBiz;
import com.aspire.mirror.inspection.server.biz.TaskObjectBiz;
import com.aspire.mirror.inspection.server.biz.cmdbadapt.CmdbAdaptFacade;
import com.aspire.mirror.inspection.server.biz.cmdbadapt.CommonCmdbDevice;
import com.aspire.mirror.inspection.server.clientservice.ItemCollectApiClient;
import com.aspire.mirror.inspection.server.clientservice.TemplateApiClient;
import com.aspire.mirror.inspection.server.dao.po.Report;
import com.aspire.mirror.inspection.server.dao.po.ReportItem;

import lombok.extern.slf4j.Slf4j;

/**
* 巡检任务执行实现    <br/>
* Project Name:inspection-service
* File Name:TaskExecuteBizImpl.java
* Package Name:com.aspire.mirror.inspection.server.biz.impl
* ClassName: TaskExecuteBizImpl <br/>
* date: 2018年8月30日 下午4:05:12 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Slf4j
@Service
public class TaskExecuteBizImpl implements TaskExecuteBiz {
	@Autowired
	private TemplateApiClient		templateApiClient;
	@Autowired
	private CmdbAdaptFacade			cmdbFacade;
	@Autowired
	private ItemCollectApiClient	itemCollectApiClient;
	@Autowired
	private TaskBiz					taskBiz;
	@Autowired
	private TaskObjectBiz			taskObjectBiz;
	@Autowired
	private ReportBiz				reportBiz;
	@Autowired
	private ReportItemBiz			reportItemBiz;

	@Autowired
	private OpsServiceClient opsServiceClient;
//	@Autowired
//	private GetItemValueFacade	getItemValueFacade;

	@Override
	public void executeInspectionTask(String taskId) {
		log.info("Begin to execute inspection task with id '{}'", taskId);
		List<TaskObjectDTO> taskObjectList = taskObjectBiz.findByTaskId(taskId);
		if (CollectionUtils.isEmpty(taskObjectList)) {
			throw new RuntimeException(
					format("There is no Task object records refered to the task with Id %s", taskId));
		}
		List<String> tempIdList = getTemplateIdList(taskObjectList);
		// 查询出脚本参数
		List<ItemsDetailResponse> itemList = templateApiClient.listItemsByTemplateIds(join(tempIdList, ','));
		
		// 初始化报表数据
		List<ReportItemDTO> reportItemList = initReportRelatedData(taskId, itemList, taskObjectList);
		
		// 获取指标值
		FetchObjectItemsValueRequest request = constructDeviceItemValueFetchRequest(reportItemList);
		
		List<ObjectItemValueWrap> objectItemValList = itemCollectApiClient.fetchObjectItemsValues(request);
		if (CollectionUtils.isEmpty(objectItemValList)) {
			log.error("FetchObjectItemValues has response with empty result. ");
			return;
		}
		
		// 回填值到reportItem
		for (ReportItemDTO reportItem : reportItemList) {
			for (ObjectItemValueWrap objectItemVal : objectItemValList) {
				if (reportItem.getObjectType().equals(objectItemVal.getObjectType())
						&& reportItem.getObjectId().equals(objectItemVal.getObjectId())
						&& reportItem.getKey().equals(objectItemVal.getItemKey())) {
					reportItem.setClock(objectItemVal.getClock());
					reportItem.setNs(objectItemVal.getNs());
					reportItem.setPreValue(objectItemVal.getPreValue());
					reportItem.setValue(objectItemVal.getValue());
				}
			}
		}
		// 拿到值后 再入库
		for (ReportItemDTO reportItem : reportItemList) {
			reportItemBiz.insert(reportItem);
		}
	}
	
	private FetchObjectItemsValueRequest constructDeviceItemValueFetchRequest(List<ReportItemDTO> reportItemList) {
		List<ObjectItemInfo> objectItemList = new ArrayList<>();
		Set<String> deviceIdSet = new HashSet<>();
		
		for (ReportItemDTO item : reportItemList) {
			if (TaskObjectDTO.OBJECT_TYPE_DEVICE.equals(item.getObjectType())) {
				deviceIdSet.add(item.getObjectId());
			}
			ObjectItemInfo objectItem = new ObjectItemInfo();
			//金素添加
			objectItem.setReportId(item.getReportId());
			objectItem.setObjectType(item.getObjectType());
			objectItem.setObjectId(item.getObjectId());
			objectItem.setItemId(item.getItemId());
			objectItem.setItemKey(item.getKey());
			objectItem.setApiServerType(item.getServerType());
			objectItem.setScriptParam(item.getScriptParam());
			objectItem.setCustomizeParam(item.getCustomizeParam());
			objectItemList.add(objectItem);
		}
		
		// 机房
		List<String> roomIdList = new ArrayList<>();
		
		if (deviceIdSet.size() > 0) {
			// 填充 IP 和 机房  信息
			List<CommonCmdbDevice> deviceDetailList = cmdbFacade.listCmdbDeviceByIds(join(deviceIdSet, ','));
			for (ObjectItemInfo objectItem : objectItemList) {
				for (CommonCmdbDevice device : deviceDetailList) {
					if (TaskObjectDTO.OBJECT_TYPE_DEVICE.equals(objectItem.getObjectType())
							&& objectItem.getObjectId().equals(device.getDeviceId())) {
						objectItem.setExtendObj(device.getDeviceIp());
						objectItem.setRoom(device.getRoomId());
					}
				}
			}
			for (CommonCmdbDevice device : deviceDetailList) {
				if (!roomIdList.contains(device.getRoomId())) {
					roomIdList.add(device.getRoomId());
				}
			}
		}
		
		//TODO 填充其它ObjectType需要的属性, 比如业务item关联room等等
		
		FetchObjectItemsValueRequest request = new FetchObjectItemsValueRequest();
		request.setObjectItemList(objectItemList);
		if (roomIdList.size() > 0) {
			List<MonitorApiServerConfig> apiSvrList 
						= templateApiClient.listApiSvrConfigsByRoomIds(join(roomIdList, ","));
			request.setApiServerConfigList(apiSvrList);
		}
		return request;
	}
	
	private List<String> getTemplateIdList(List<TaskObjectDTO> taskDeviceList) {
		List<String> tempIdList = new ArrayList<>();
		for (TaskObjectDTO model : taskDeviceList) {
			if (!tempIdList.contains(model.getTemplateId())) {
				tempIdList.add(model.getTemplateId());
			}
		}
		return tempIdList;
	}
	
	/**
	* 初始化报表相关数据. <br/>
	*
	* 作者： pengguihua
	* @param taskId
	* @param itemList
	* @param taskObjectList
	*/  
	private List<ReportItemDTO> initReportRelatedData(
			String taskId, List<ItemsDetailResponse> itemList, List<TaskObjectDTO> taskObjectList) {
		TaskDTO taskDto = taskBiz.selectByPrimaryKey(taskId);
		Date now = new Date();
		
		ReportDTO report = new ReportDTO();
		report.setReportId(UUID.randomUUID().toString());
		report.setTaskId(taskId);
		report.setName(taskDto.getName() + "_" + DateUtil.format(now, DateUtil.DATE_TIME_FORMAT));
		report.setCreateTime(now);
		report.setStatus(Report.STATUS_RUNNING);
		report.setBizStatus(Report.BIZ_STATUS_RUNNING);
		reportBiz.insert(report);
		
		List<ReportItemDTO> reportItemList = new ArrayList<>();
		for (ItemsDetailResponse item : itemList) {
			for (TaskObjectDTO targetObject : taskObjectList) {
				if (item.getTemplateId().equals(targetObject.getTemplateId())) {
					ReportItemDTO reportItem = new ReportItemDTO();
					reportItem.setReportId(report.getReportId());
					reportItem.setObjectType(targetObject.getObjectType());
					reportItem.setObjectId(targetObject.getObjectId());
					reportItem.setItemId(item.getItemId());
					reportItem.setServerType(item.getSysType());
					reportItem.setKey(item.getKey());
					reportItem.setStatus(ReportItem.STATUS_NORESULT);
					reportItem.setScriptParam(item.getScriptParam());
					reportItem.setCustomizeParam(item.getCustomizeParam());
					reportItemList.add(reportItem);
				}
			}
		}
		// jinsu update
//		for (ReportItemDTO reportItem : reportItemList) {
//			reportItemBiz.insert(reportItem);
//		}
		return reportItemList;
	}
}

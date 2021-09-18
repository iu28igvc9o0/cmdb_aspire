package com.aspire.mirror.inspection.server.controller ;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.util.FieldUtil;
import com.aspire.mirror.inspection.api.dto.ReportItemBatchCreateRequest;
import com.aspire.mirror.inspection.api.dto.ReportItemCreateRequest;
import com.aspire.mirror.inspection.api.dto.ReportItemCreateResponse;
import com.aspire.mirror.inspection.api.dto.ReportItemPageRequest;
import com.aspire.mirror.inspection.api.dto.ReportItemUpdateRequest;
import com.aspire.mirror.inspection.api.dto.ReportTaskDetailResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.inspection.api.dto.ReportItemDetailResponse;
import com.aspire.mirror.inspection.api.dto.model.ReportItemDTO;
import com.aspire.mirror.inspection.api.service.ReportItemService;
import com.aspire.mirror.inspection.server.biz.ReportItemBiz;
import com.google.common.collect.Lists;

/**
 * inspection_report_item控制层实现类
 *
 * 项目名称: 微服务运维平台
 * 包:      com.aspire.mirror.inspection.server.controller
 * 类名称:   ReportItemController.java
 * 类描述:   inspection_report_item业务逻辑层实现类
 * 创建人:   ZhangSheng
 * 创建时间: 2018-07-27 13:48:08
 */
@RestController
@CacheConfig(cacheNames = "ReportItemCache")
public class ReportItemController implements ReportItemService {
	
	
	@Override
	public PageResponse<ReportItemDetailResponse> listByReportId(@PathVariable("report_id")String reportId) {
		if(StringUtils.isEmpty(reportId)) {
			 LOGGER.warn("listByPrimaryKey param taskId is null");
	         throw new RuntimeException("taskId is null");
		}
		List<ReportItemDTO> reportItemDTOs=reportItemBiz.selectByReportId(reportId);
		List<ReportItemDetailResponse> list =Lists.newArrayList();
		for (ReportItemDTO  reportItemDTO: reportItemDTOs) {
			ReportItemDetailResponse itemDetailResponse =new ReportItemDetailResponse();
			BeanUtils.copyProperties(reportItemDTO,itemDetailResponse);
			list.add(itemDetailResponse);
		}
		PageResponse<ReportItemDetailResponse> pageResponse =new PageResponse<ReportItemDetailResponse>();
		pageResponse.setCount(reportItemBiz.selectCountByReportId(reportId));
		pageResponse.setResult(list);
		return pageResponse;
	}

	@Override
	public int updateReportItem(@RequestBody ReportItemUpdateRequest reportItemVO) {
		if (reportItemVO == null) {
			LOGGER.warn("batchUpdateReportItem param reportItemVO is null");
			return 0;
		}
		if (StringUtils.isEmpty(reportItemVO.getReportItemId())) {
			LOGGER.warn("batchUpdateReportItem param reportItemId is null");
			return 0;
		}
		ReportItemDTO reportItemDTO = new ReportItemDTO();
		BeanUtils.copyProperties(reportItemVO, reportItemDTO);
		return reportItemBiz.updateByPrimaryKeySelective(reportItemDTO);
	}

	@Override
	public ReportItemCreateResponse createReportItem(@RequestBody ReportItemCreateRequest reportItemVO) {
		if (reportItemVO == null) {
			LOGGER.warn("createReportItem param reportItemVO is null");
			return null;
		}
		ReportItemDTO reportItemDTO = new ReportItemDTO();
		BeanUtils.copyProperties(reportItemVO, reportItemDTO);
		long reportItemId = reportItemBiz.insert(reportItemDTO);
		ReportItemCreateResponse response = new ReportItemCreateResponse();
		BeanUtils.copyProperties(reportItemVO, response);
		response.setReportItemId(reportItemId);
		return response;
	}

	@Override
	public Integer batchCreateReportItem(@RequestBody ReportItemBatchCreateRequest reportItemCreateBatchRequest) {
		if (reportItemCreateBatchRequest == null || reportItemCreateBatchRequest.getReportItemList() == null) {
			LOGGER.warn("batchCreateReportItem param reportItemList is null");
			return null;
		}
		List<ReportItemDTO> reportItemDTOList = Lists.newArrayList();
		for (ReportItemCreateRequest createRequest : reportItemCreateBatchRequest.getReportItemList()) {
			ReportItemDTO reportItemDTO = new ReportItemDTO();
			BeanUtils.copyProperties(createRequest, reportItemDTO);
			reportItemDTOList.add(reportItemDTO);
		}
		if (reportItemDTOList.size() > 0) {
			return reportItemBiz.insertBatch(reportItemDTOList);
		}
		return 0;
	}

	/**
	 * 分页查询
	 * @param pageRequest
	 * @return
	 */
	@Override
	public PageResponse<ReportItemDetailResponse> pageList(@RequestBody ReportItemPageRequest pageRequest) {
		PageResponse<ReportItemDetailResponse> pageResponse = new PageResponse<>();
		if(null==pageRequest) {
			LOGGER.error("list param reportPageRequest is null");
			pageResponse.setCount(0);
			pageResponse.setResult(new ArrayList<>());
			return pageResponse;
		}
		List<ReportItemDetailResponse> detailResponses =Lists.newArrayList();

		PageRequest page =new PageRequest();
		page.setPageNo(pageRequest.getPageNo());
		page.setPageSize(pageRequest.getPageSize());
		Map<String, Object> map = FieldUtil.getFiledMap(pageRequest);
		for (String key : map.keySet()) {
			page.addFields(key, map.get(key));
		}
		PageResponse<ReportItemDTO> pageResult = reportItemBiz.pageList(page);
		if (pageResult.getResult() != null) {
			for (ReportItemDTO reportItemDTO : pageResult.getResult()) {

				ReportItemDetailResponse reportItemDetailResponse = new ReportItemDetailResponse();
				BeanUtils.copyProperties(reportItemDTO, reportItemDetailResponse);
				if (reportItemDTO.getReportItemExt() != null) {
					reportItemDetailResponse.setExecStatus(reportItemDTO.getReportItemExt().getExecStatus());
					reportItemDetailResponse.setLog(reportItemDTO.getReportItemExt().getLog());
					reportItemDetailResponse.setDeviceName(reportItemDTO.getReportItemExt().getDeviceName());
					reportItemDetailResponse.setIdcType(reportItemDTO.getReportItemExt().getIdcType());
					reportItemDetailResponse.setBizSystem(reportItemDTO.getReportItemExt().getBizSystem());
					reportItemDetailResponse.setName(reportItemDTO.getReportItemExt().getItemName());
					//特殊处理  存在描述时设置巡检
//					if (!StringUtils.isEmpty(reportItemDTO.getResultDesc())) {
//						reportItemDetailResponse.setResultName(reportItemDTO.getResultDesc());
//					}
					if (reportItemDTO.getObjectId().indexOf(":") == -1) {
						reportItemDetailResponse.setIp(reportItemDTO.getObjectId());
					} else {
						reportItemDetailResponse.setIp(reportItemDTO.getObjectId().substring(reportItemDTO.getObjectId().indexOf(":") + 1, reportItemDTO.getObjectId().length()));
					}
				}
				detailResponses.add(reportItemDetailResponse);
			}
		}

		pageResponse.setResult(detailResponses);
		pageResponse.setCount(pageResult.getCount());
		return pageResponse;
	}



	/** slf4j*/
    private static final Logger LOGGER= LoggerFactory.getLogger(ReportItemController.class);
    
    @Autowired
    private ReportItemBiz reportItemBiz;

}
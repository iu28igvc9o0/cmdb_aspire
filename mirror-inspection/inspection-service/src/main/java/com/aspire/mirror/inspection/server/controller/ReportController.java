package com.aspire.mirror.inspection.server.controller ;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.aspire.mirror.inspection.server.biz.ReportBiz;
import com.aspire.mirror.inspection.api.dto.model.ReportDTO;
import com.aspire.mirror.inspection.api.dto.model.ReportTaskDTO;
import com.aspire.mirror.inspection.api.service.ReportService;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.inspection.api.dto.ReportDetailResponse;
import com.aspire.mirror.inspection.api.dto.ReportPageRequest;
import com.aspire.mirror.inspection.api.dto.ReportTaskDetailResponse;
/**
 * inspection_report控制层实现类
 *
 * 项目名称: 微服务运维平台
 * 包:      com.aspire.mirror.inspection.server.controller
 * 类名称:   ReportController.java
 * 类描述:   inspection_report业务逻辑层实现类
 * 创建人:   ZhangSheng
 * 创建时间: 2018-07-27 13:48:08
 */
@RestController
@CacheConfig(cacheNames = "ReportCache")
public class ReportController implements ReportService {
	/**
     * 根据主键查找inspection_report详情信息
     * @param reportId inspection_report主键
     * @return ReportVO inspection_report详情响应对象
     */
    public ReportDetailResponse findByPrimaryKey(@PathVariable("report_id") final String reportId){
        if (StringUtils.isEmpty(reportId)) {
            LOGGER.warn("findByPrimaryKey param reportId is null");
            return null;
        }
        ReportDTO reportDTO = reportBiz.selectByPrimaryKey(reportId);
        ReportDetailResponse reportDetailResponse = new ReportDetailResponse();
        if (null != reportDTO) {
            BeanUtils.copyProperties(reportDTO, reportDetailResponse);
        }

        return reportDetailResponse;
    }
	/**
	 * 根据任务id查询巡检报告
	 * @param  taskId:任务id
	 * @param  reportPageRequest:巡检报告查询请求信息
	 * @return PageResponse<ReportTaskDetailResponse>:巡检任务响应页面对象
	 */
	@Override
	public PageResponse<ReportTaskDetailResponse> list(final @Validated @RequestBody ReportPageRequest reportPageRequest) {
		PageResponse<ReportTaskDetailResponse> pageResponse = new PageResponse<ReportTaskDetailResponse>();
		if(null==reportPageRequest) {
			LOGGER.error("list param reportPageRequest is null");
			pageResponse.setCount(0);
			pageResponse.setResult(new ArrayList<ReportTaskDetailResponse>());
            return pageResponse;
		}
		List<ReportTaskDetailResponse> detailResponses =Lists.newArrayList();
		PageRequest page =new PageRequest();
		try {
			page.addFields("task_id", reportPageRequest.getTaskId());
			page.addFields("name",reportPageRequest.getName());
			page.addFields("status", reportPageRequest.getStatus());
			page.addFields("inspection_time_start",reportPageRequest.getInspectionTimeStart());
			page.addFields("inspection_time_end",reportPageRequest.getInspectionTimeEnd());
			page.setPageNo(reportPageRequest.getPageNo());
			page.setPageSize(reportPageRequest.getPageSize());
			List<ReportTaskDTO> listReportDTO =reportBiz.selectByPage(page);
			for (ReportTaskDTO reportDTO : listReportDTO) {
				ReportTaskDetailResponse detailResponse =new ReportTaskDetailResponse();
				BeanUtils.copyProperties(reportDTO,detailResponse);
				detailResponses.add(detailResponse);
			}
			pageResponse.setCount(reportBiz.selectCount(page));
			pageResponse.setResult(detailResponses);
		} catch (Exception e) {
			 LOGGER.error("list error:" + e.getMessage(), e);
			 pageResponse.setCount(0);
			 pageResponse.setResult(new ArrayList<ReportTaskDetailResponse>());
	         return pageResponse;
		}
		return pageResponse;
	}

	/**
	 * 重新生成巡检报告
	 * @param reportId 报告ID
	 */
	@Override
	public void regenerate(@PathVariable("report_id") String reportId, @RequestBody Map<String, String> triggerMap) {
		if (StringUtils.isEmpty(reportId)) {
			return;
		}
		reportBiz.regenerate(reportId, triggerMap);
	}

		/**
		 * 修改报告文件地址
		 * @param reportId
		 * @param filePath
		 */
		@Override
		public void updateReportFilePath(@RequestParam("report_id") String reportId, @RequestParam("file_path") String filePath) {
			if (StringUtils.isEmpty(reportId)) {
				LOGGER.warn("ReportCotroller[updateReportFilePath] param reportId is empty");
				return;
			}
			if (StringUtils.isEmpty(filePath)) {
				LOGGER.warn("ReportCotroller[updateReportFilePath] param filePath is empty");
				return;
			}
			reportBiz.updateReportFilePath(reportId, filePath);
		}

	/** slf4j*/
    private static final Logger LOGGER= LoggerFactory.getLogger(ReportController.class);
    
    @Autowired
    private ReportBiz reportBiz;

}
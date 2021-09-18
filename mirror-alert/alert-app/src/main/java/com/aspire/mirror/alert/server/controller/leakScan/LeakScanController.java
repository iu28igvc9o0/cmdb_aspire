package com.aspire.mirror.alert.server.controller.leakScan;

import com.aspire.mirror.alert.server.biz.leakScan.SecurityLeakScanBiz;
import com.aspire.mirror.alert.server.vo.leakScan.LeakScanSummaryVo;
import com.aspire.mirror.alert.server.vo.leakScan.SecurityLeakScanRecordVo;
import com.aspire.mirror.alert.api.dto.leakScan.*;
import com.aspire.mirror.alert.api.service.leakScan.LeakScanService;
import com.aspire.mirror.alert.server.vo.leakScan.SecurityLeakScanReportVo;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.FieldUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LeakScanController implements LeakScanService {

    private static final Logger logger = LoggerFactory.getLogger(LeakScanController.class);

    @Autowired
    private SecurityLeakScanBiz securityLeakScanBiz;

    @Override
    public PageResponse<LeakScanSummaryResponse> leakScanSummary(@RequestBody LeakScanSummaryRequest request) {
        // 业务系统
        if (StringUtils.isNotEmpty(request.getBizSys())) {
            request.setBizSysList(Arrays.asList(request.getBizSys().split(",")));
        }
        PageRequest page = new PageRequest();
        BeanUtils.copyProperties(request, page);
        Map<String, Object> map = FieldUtil.getFiledMap(request);
        for (String key : map.keySet()) {
            page.addFields(key, map.get(key));
        }
        PageResponse<LeakScanSummaryVo> summaryDTOPageResponse = securityLeakScanBiz.summaryList(page);
        List<LeakScanSummaryResponse> responseList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(summaryDTOPageResponse.getResult())) {
            for (LeakScanSummaryVo summaryDTO : summaryDTOPageResponse.getResult()) {
                LeakScanSummaryResponse summaryResponse = new LeakScanSummaryResponse();
                BeanUtils.copyProperties(summaryDTO, summaryResponse);
                responseList.add(summaryResponse);
            }
        }
        PageResponse<LeakScanSummaryResponse> response = new PageResponse<>();
        response.setCount(summaryDTOPageResponse.getCount());
        response.setResult(responseList);
        return response;
    }

    @Override
    public List<Map<String, Object>> export(@RequestBody LeakScanSummaryRequest request) throws IllegalAccessException {
        List<Map<String, Object>> dataLists = Lists.newArrayList();
        if (request == null) {
            logger.error("Alert query param pageRequset is null or query type is empty !");
            return dataLists;
        }
        PageRequest page = new PageRequest();
        BeanUtils.copyProperties(request, page);
        Map<String, Object> map = FieldUtil.getFiledMap(request);
        for (String key : map.keySet()) {
            page.addFields(key, map.get(key));
        }
        List<LeakScanSummaryVo> dtoList = securityLeakScanBiz.exportList(page);
        List<Map<String, Object>> dataList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(dtoList)) {
            for (LeakScanSummaryVo obj : dtoList) {
                Map<String, Object> dtoMap = new HashMap<String, Object>();
                Field[] declaredFields = obj.getClass().getDeclaredFields();
                for (Field field : declaredFields) {
                    field.setAccessible(true);
                    dtoMap.put(field.getName(), field.get(obj));
                }
                dataList.add(dtoMap);
            }
        }
        return dataList;
    }

    @Override
    public PageResponse<LeakScanReportResponse> leakScanReports(@RequestBody LeakScanReportRequest reportRequest) {
        PageRequest page = new PageRequest();
        BeanUtils.copyProperties(reportRequest, page);
        Map<String, Object> map = FieldUtil.getFiledMap(reportRequest);
        for (String key : map.keySet()) {
            page.addFields(key, map.get(key));
        }
        PageResponse<SecurityLeakScanReportVo> reportDTOPageResponse = securityLeakScanBiz.reportList(page);
        List<LeakScanReportResponse> responseList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(reportDTOPageResponse.getResult())) {
            for (SecurityLeakScanReportVo reportDTO : reportDTOPageResponse.getResult()) {
                LeakScanReportResponse reportResponse = new LeakScanReportResponse();
                BeanUtils.copyProperties(reportDTO, reportResponse);
                responseList.add(reportResponse);
            }
        }
        PageResponse<LeakScanReportResponse> response = new PageResponse<>();
        response.setCount(reportDTOPageResponse.getCount());
        response.setResult(responseList);
        return response;
    }

    @Override
    public LeakScanRecordResponse getLeakScanRecord(@PathVariable(value = "id") String id) {
        SecurityLeakScanRecordVo recordDTO = securityLeakScanBiz.getSecurityLeakScanRecordById(id);
        LeakScanRecordResponse response = new LeakScanRecordResponse();
        if (recordDTO != null) {
            BeanUtils.copyProperties(recordDTO, response);
        }
        return response;
    }

    @Override
    public void modifyBpmRepairStat(@PathVariable(value = "id") String bpmId) {
        securityLeakScanBiz.setBpmReapirStat(bpmId, 1);
    }

	@Override
	public PageResponse<LeakScanSummaryResponse> leakScanDetailByDate(@RequestBody LeakScanSummaryRequest request) {
		// 业务系统
        if (StringUtils.isNotEmpty(request.getBizSys())) {
            request.setBizSysList(Arrays.asList(request.getBizSys().split(",")));
		} /*
			 * else { request.setBizSysList(null); }
			 */
        PageRequest page = new PageRequest();
        BeanUtils.copyProperties(request, page);
        Map<String, Object> map = FieldUtil.getFiledMap(request);
        for (String key : map.keySet()) {
            page.addFields(key, map.get(key));
        }
        PageResponse<LeakScanSummaryVo> summaryDTOPageResponse = securityLeakScanBiz.getLeakScanDetailByDate(page);
        List<LeakScanSummaryResponse> responseList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(summaryDTOPageResponse.getResult())) {
            for (LeakScanSummaryVo summaryDTO : summaryDTOPageResponse.getResult()) {
                LeakScanSummaryResponse summaryResponse = new LeakScanSummaryResponse();
                BeanUtils.copyProperties(summaryDTO, summaryResponse);
                responseList.add(summaryResponse);
            }
        }
        PageResponse<LeakScanSummaryResponse> response = new PageResponse<>();
        response.setCount(summaryDTOPageResponse.getCount());
        response.setResult(responseList);
        return response;
	}

	@Override
	public List<Map<String, Object>> exportDetail(@RequestBody LeakScanSummaryRequest request) throws IllegalAccessException {
		 List<Map<String, Object>> dataLists = Lists.newArrayList();
	        if (request == null) {
	            logger.error("Alert query param pageRequset is null or query type is empty !");
	            return dataLists;
	        }
	        PageRequest page = new PageRequest();
	        BeanUtils.copyProperties(request, page);
	        Map<String, Object> map = FieldUtil.getFiledMap(request);
	        for (String key : map.keySet()) {
	            page.addFields(key, map.get(key));
	        }
	        List<LeakScanSummaryVo> dtoList = securityLeakScanBiz.exportList(page);
	        List<Map<String, Object>> dataList = Lists.newArrayList();
	        if (CollectionUtils.isNotEmpty(dtoList)) {
	            for (LeakScanSummaryVo obj : dtoList) {
	                Map<String, Object> dtoMap = new HashMap<String, Object>();
	                Field[] declaredFields = obj.getClass().getDeclaredFields();
	                for (Field field : declaredFields) {
	                    field.setAccessible(true);
	                    dtoMap.put(field.getName(), field.get(obj));
	                }
	                dataList.add(dtoMap);
	            }
	        }
	        return dataList;
	}

	@Override
	public Map<String, Object> leaksRankDistribute(@RequestParam(value = "beginDate", required = false) String beginDate,
                                                   @RequestParam(value = "endDate", required = false) String endDate) {
		return securityLeakScanBiz.leaksRankDistribute(beginDate, endDate);
	}

	@Override
	public PageResponse<Map<String, Object>> leakStatByBiz(@RequestParam(value = "beginDate", required = false) String beginDate,
                                                   @RequestParam(value = "endDate", required = false) String endDate) {
		return securityLeakScanBiz.leakStatByBiz(beginDate, endDate,null,null);
	}

	@Override
	public PageResponse<Map<String, Object>> leakStatListByBiz(@RequestParam("beginDate")String beginDate, 
			@RequestParam("endDate")String endDate, @RequestParam(value="rankType"
		    		,required=false)String rankType,@RequestParam(value="begin"
		    		,required=false) Integer begin,@RequestParam(value="pageSize"
		    		,required=false)Integer pageSize){
		Integer end = pageSize;
		Integer start = begin;
		if(null!= begin &&pageSize!= null ) {
			if(begin !=0 ) {
				start = (begin-1) * pageSize;
				end = begin * pageSize;
			}
			
		}
		
		if(StringUtils.isBlank(rankType)) {
			return securityLeakScanBiz.leakStatByBiz(beginDate, endDate,start,end); 
		}
		if(rankType.equals("high")) {
			rankType = "high_leaks";
		}else if(rankType.equals("middle")) {
			rankType = "medium_leaks";
		}else if(rankType.equals("low")) {
			rankType = "low_leaks";
		}else{
			return null;
		}
		return securityLeakScanBiz.leakStatListByBiz( beginDate, endDate, rankType,begin,pageSize);
	}

	@Override
	public PageResponse<Map<String, Object>> leakTrend(@RequestParam(value = "beginDate",required = false) String beginDate,
                                               @RequestParam(value = "endDate",required = false) String endDate
                                               ,@RequestParam(value="begin",required=false) Integer begin
                                               ,@RequestParam(value="pageSize",required=false)Integer pageSize) {
		Integer end = pageSize;
		Integer start = begin;
		if(null!= begin &&pageSize!= null ) {
			if(begin !=0 ) {
				start = (begin-1) * pageSize;
				end = begin * pageSize;
			}
			
		}
		return securityLeakScanBiz.leakTrend(beginDate, endDate,start,end);
	}

	@Override
	public Map<String, Object> leakSummary(@RequestParam(value = "beginDate",required = false) String beginDate,
                                           @RequestParam(value = "endDate",required = false) String endDate)  {
		return securityLeakScanBiz.leakSummary(beginDate, endDate);
	}

    @Override
    public List<Map<String, Object>> getLeakByBizSystem() {
        return securityLeakScanBiz.getLeakByBizSystem();
    }

    @Override
    public List<Map<String, Object>> getLeakByIp() {
        return securityLeakScanBiz.getLeakByIp();
    }

    @Override
    public List<Map<String,Object>> getLeakByIdcType() {
        return securityLeakScanBiz.getLeakByIdcType();
    }
}

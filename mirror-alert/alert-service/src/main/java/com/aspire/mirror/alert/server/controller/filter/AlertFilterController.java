package com.aspire.mirror.alert.server.controller.filter;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.aspire.mirror.alert.server.vo.alert.AlertsVo;
import com.aspire.mirror.alert.server.util.TransformUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.alert.api.dto.filter.AlertFilterDataRequest;
import com.aspire.mirror.alert.api.dto.filter.AlertFilterDataResponse;
import com.aspire.mirror.alert.api.dto.filter.AlertFilterResponse;
import com.aspire.mirror.alert.api.dto.alert.AlertsDetailResponse;
import com.aspire.mirror.alert.api.dto.filter.AlertFilterDTO;
import com.aspire.mirror.alert.api.dto.filter.AlertFilterSceneDTO;
import com.aspire.mirror.alert.api.service.filter.AlertFilterService;
import com.aspire.mirror.alert.server.biz.filter.AlertFilterBiz;
import com.aspire.mirror.alert.server.dao.filter.po.AlertFilter;
import com.aspire.mirror.alert.server.dao.filter.po.AlertFilterScene;
import com.aspire.mirror.alert.server.util.Utils;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.FieldUtil;
import com.google.common.collect.Lists;

/**
 * 告警控制层
 * <p>
 * 项目名称: mirror平台 包: com.aspire.mirror.alert.server.controller 类名称:
 * AlertsHisController.java 类描述: 告警控制层 创建人: JinSu 创建时间: 2018/9/14 17:51 版本: v1.0
 */
@RestController
public class AlertFilterController implements AlertFilterService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AlertFilterController.class);
	@Autowired
	private AlertFilterBiz alertFilterBiz;

	@Override
	public PageResponse<AlertFilterDTO> pageList(@RequestBody AlertFilterResponse pageRequset) {
		if (pageRequset == null) {
			LOGGER.warn("pageList param pageRequset is null");
			return null;
		}
		PageRequest page = new PageRequest();
		BeanUtils.copyProperties(pageRequset, page, Utils.getNullPropertyNames(pageRequset));
		Map<String, Object> map = FieldUtil.getFiledMap(pageRequset);
		for (String key : map.keySet()) {
			page.addFields(key, map.get(key));
		}
		PageResponse<AlertFilter> pageResult = alertFilterBiz.pageList(page);
		List<AlertFilterDTO> listAlert = Lists.newArrayList();
		if (!CollectionUtils.isEmpty(pageResult.getResult())) {
			for (AlertFilter filter : pageResult.getResult()) {
				AlertFilterDTO filterDTO = new AlertFilterDTO();
				BeanUtils.copyProperties(filter, filterDTO);
				listAlert.add(filterDTO);
			}
		}

		PageResponse<AlertFilterDTO> result = new PageResponse<AlertFilterDTO>();
		result.setCount(pageResult.getCount());
		result.setResult(listAlert);
		return result;
	}

	@Override
	public AlertFilterDTO findByPrimaryKey(@PathVariable("filter_id") String filterId) {
		if (StringUtils.isEmpty(filterId)) {
			LOGGER.warn("findByPrimaryKey param filterId is null");
			return null;
		}
		AlertFilter filter = alertFilterBiz.selectByPrimaryKey(filterId);
		if (null == filter) {
			return null;
		}
		AlertFilterDTO filterDTO = new AlertFilterDTO();
		BeanUtils.copyProperties(filter, filterDTO);
		return filterDTO;
	}

	@Override
	public ResponseEntity<String> create(@RequestBody AlertFilterDTO createRequest) {
		if (createRequest == null) {
			LOGGER.error("create createRequest is null");
			throw new RuntimeException("create param is null");
		}
		AlertFilter filter = new AlertFilter();
		BeanUtils.copyProperties(createRequest, filter);
		filter.setCreated_at(new Date());
		alertFilterBiz.insert(filter);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> update(@PathVariable("filter_id") String filterId,
			@RequestBody AlertFilterDTO updateRequest) {
		if (StringUtils.isEmpty(filterId)) {
			LOGGER.warn("update param filterId is null");
			return null;
		}
		if (updateRequest == null) {
			LOGGER.warn("update updateRequest note is null");
			return null;
		}
		AlertFilter filter = new AlertFilter();
		BeanUtils.copyProperties(updateRequest, filter);
		alertFilterBiz.update(filter);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> delete(@PathVariable("filter_id") String filterId) {
		if (StringUtils.isEmpty(filterId)) {
			LOGGER.error("delete filterId is null");
			throw new RuntimeException("delete filterId is null");
		}
		alertFilterBiz.delete(filterId);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	@Override
	public AlertFilterDTO findByName(@PathVariable("filter_name") String filterName) {
		if (StringUtils.isEmpty(filterName)) {
			LOGGER.error("findByName filterName is null");
			throw new RuntimeException("findByName filterName is null");
		}
		AlertFilter filter = alertFilterBiz.getByName(filterName);
		if (null == filter) {
			return null;
		}
		AlertFilterDTO filterDTO = new AlertFilterDTO();
		BeanUtils.copyProperties(filter, filterDTO);
		return filterDTO;
	}

	@Override
	public List<AlertFilterDTO> getAllFilter(@RequestParam(value = "filterFlag", required = false) boolean filterFlag,
			@RequestParam(value = "operateUser", required = false) String operateUser) {
		List<AlertFilterDTO> listFilter = Lists.newArrayList();
		List<AlertFilter> filters = alertFilterBiz.getAllFilter(filterFlag,operateUser);
		if (!CollectionUtils.isEmpty(filters)) {
			for (AlertFilter filter : filters) {
				if (filterFlag) {
					if (null == filter.getSceneNum() || filter.getSceneNum() == 0) {
						continue;
					}
					AlertFilterDTO filterDTO = new AlertFilterDTO();
					BeanUtils.copyProperties(filter, filterDTO);
					listFilter.add(filterDTO);
				} else {
					AlertFilterDTO filterDTO = new AlertFilterDTO();
					BeanUtils.copyProperties(filter, filterDTO);
					listFilter.add(filterDTO);
				}
			}
		}
		return listFilter;
	}

	@Override
	public List<AlertFilterDataResponse> queryAlertCount(@PathVariable("filter_id") String filterId,
			@RequestParam(value = "userName", required = false) String userName) {
		if (StringUtils.isEmpty(filterId)) {
			LOGGER.error("queryAlert filterId is null");
			throw new RuntimeException("queryAlert filterId is null");
		}

		List<AlertFilterScene> sceneData = alertFilterBiz.getSceneByid(Integer.parseInt(filterId), userName);
		List<AlertFilterDataResponse> listAlert = Lists.newArrayList();
		for (AlertFilterScene s : sceneData) {
			String condition = s.getOptionCondition();
			PageRequest page = new PageRequest();
			page.addFields("condition", getCondition(condition));
			int count = alertFilterBiz.filterSelectCount(page);
			AlertFilterDataResponse obj = new AlertFilterDataResponse();
			obj.setCount(count);
			obj.setSceneId(s.getId());
			obj.setSceneName(s.getName());
			obj.setCondition(s.getOptionCondition());
			listAlert.add(obj);
		}
		return listAlert;
	}

	public String getCondition(String optionCondition) {
		JSONObject filterOption = JSON.parseObject(optionCondition);
		StringBuffer orSb = new StringBuffer();
		JSONArray orlist = filterOption.getJSONArray("data");
		for (int i = 0; i < orlist.size(); i++) {
			StringBuffer andSb = new StringBuffer();
			JSONObject andJson = orlist.getJSONObject(i);
			JSONArray andlist = andJson.getJSONArray("data");
			for (int j = 0; j < andlist.size(); j++) {
				JSONObject val = andlist.getJSONObject(j);
				String filterItemName = val.getString("filterItemName");
				String operate = val.getString("operate");
				String value = val.getString("value");
				String jdbcType = val.getString("jdbcType");
				if (andSb.length() == 0) {
					andSb.append("(").append(filterItemName).append(" ").append(operate).append(" ");
				} else {
					andSb.append(" and ").append(filterItemName).append(" ").append(operate).append(" ");
				}
				if (operate.equalsIgnoreCase("in") || operate.indexOf("not") == 0) {
					JSONArray valJson = JSON.parseArray(value);
					for (int k = 0; k < valJson.size(); k++) {
						String valStr = valJson.getString(k);
						if (!jdbcType.equalsIgnoreCase("number")) {
							valStr = "'" + valStr + "'";
						}
						if (k == 0) {
							andSb.append("(").append(valStr);
						} else {
							andSb.append(",").append(valStr);
						}
					}
					andSb.append(")");

				} else {
					if (!jdbcType.equalsIgnoreCase("number")) {
						if (operate.equalsIgnoreCase("like")) {
							value = "'%" + value + "%'";
						} else {
							value = "'" + value + "'";
						}
					}
					andSb.append(value);
				}
			}
			andSb.append(")");
			if (orSb.length() == 0) {
				orSb.append("( ");
				orSb.append(andSb);
			} else {
				orSb.append(" or ").append(andSb);
			}
		}
		if (orSb.length() > 0) {
			orSb.append(")");
		}
		return orSb.toString();
	}

	@Override
	public PageResponse<AlertsDetailResponse> queryAlertData(@RequestBody AlertFilterDataRequest pageRequset) {
		if (pageRequset == null) {
			LOGGER.warn("queryAlertData param pageRequset is null");
			return null;
		}
		PageRequest page = new PageRequest();
		BeanUtils.copyProperties(pageRequset, page);
		page.setPageNo(pageRequset.getPageNo());
		page.setPageSize(pageRequset.getPageSize());
		page.addFields("condition", getCondition(pageRequset.getCondition()));
		page.addFields("operationStatus", pageRequset.getOperationStatus());
		PageResponse<AlertsVo> pageResult = alertFilterBiz.filterSelect(page);
		PageResponse<AlertsDetailResponse> result = new PageResponse<>();
		result.setCount(pageResult.getCount());
		result.setResult(TransformUtils.transform(AlertsDetailResponse.class, pageResult.getResult()));
		return result;
	}

	@Override
	public ResponseEntity<String> copy(@RequestBody AlertFilterDTO createRequest) {
		if (createRequest == null) {
			LOGGER.error("copy createRequest is null");
			throw new RuntimeException("copy param is null");
		}
		AlertFilter filter = new AlertFilter();
		BeanUtils.copyProperties(createRequest, filter);
		filter.setCreated_at(new Date());
		alertFilterBiz.copy(filter);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	@Override
	public List<AlertFilterSceneDTO> querySceneByFilterId(@PathVariable("filter_id") String filterId) {
		if (StringUtils.isEmpty(filterId)) {
			LOGGER.warn("querySceneByFilterId param filterId is null");
			return null;
		}
		List<AlertFilterScene> scenes = alertFilterBiz.getSceneByid(Integer.parseInt(filterId), null);
		List<AlertFilterSceneDTO> listAlert = Lists.newArrayList();
		if (!CollectionUtils.isEmpty(scenes)) {
			for (AlertFilterScene filter : scenes) {
				AlertFilterSceneDTO filterDTO = new AlertFilterSceneDTO();
				BeanUtils.copyProperties(filter, filterDTO);
				listAlert.add(filterDTO);
			}
		}
		return listAlert;
	}
	
}

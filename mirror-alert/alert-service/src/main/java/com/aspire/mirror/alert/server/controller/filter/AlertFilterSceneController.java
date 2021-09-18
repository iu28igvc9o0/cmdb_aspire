package com.aspire.mirror.alert.server.controller.filter;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

import com.aspire.mirror.alert.api.dto.filter.AlertFilterSceneResponse;
import com.aspire.mirror.alert.api.dto.filter.AlertFilterOptionDTO;
import com.aspire.mirror.alert.api.dto.filter.AlertFilterSceneDTO;
import com.aspire.mirror.alert.api.service.filter.AlertFilterSceneService;
import com.aspire.mirror.alert.server.biz.filter.AlertFilterSceneBiz;
import com.aspire.mirror.alert.server.dao.filter.po.AlertFilterOption;
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
public class AlertFilterSceneController implements AlertFilterSceneService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AlertFilterSceneController.class);
	@Autowired
	private AlertFilterSceneBiz alertFilterSceneBiz;

	@Override
	public PageResponse<AlertFilterSceneDTO> pageList(@RequestBody AlertFilterSceneResponse pageRequset) {
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
		PageResponse<AlertFilterScene> pageResult = alertFilterSceneBiz.pageList(page);
		List<AlertFilterSceneDTO> listAlert = Lists.newArrayList();
		if (!CollectionUtils.isEmpty(pageResult.getResult())) {
			for (AlertFilterScene filterScene : pageResult.getResult()) {
				AlertFilterSceneDTO filterSceneDTO = new AlertFilterSceneDTO();
				BeanUtils.copyProperties(filterScene, filterSceneDTO);
				listAlert.add(filterSceneDTO);
			}
		}

		PageResponse<AlertFilterSceneDTO> result = new PageResponse<AlertFilterSceneDTO>();
		result.setCount(pageResult.getCount());
		result.setResult(listAlert);
		return result;
	}

	@Override
	public AlertFilterSceneDTO findByPrimaryKey(@PathVariable("filterScene_id") String filterSceneId) {
		if (StringUtils.isEmpty(filterSceneId)) {
			LOGGER.warn("findByPrimaryKey param alertId is null");
			return null;
		}
		AlertFilterScene filterScene = alertFilterSceneBiz.selectByPrimaryKey(filterSceneId);
		if (null == filterScene) {
			return null;
		}
		AlertFilterSceneDTO filterSceneDTO = new AlertFilterSceneDTO();
		BeanUtils.copyProperties(filterScene, filterSceneDTO);
		return filterSceneDTO;
	}

	@Override
	public ResponseEntity<String> create(@RequestBody AlertFilterSceneDTO createRequest) {
		if (createRequest == null) {
			LOGGER.error("create createRequest is null");
			throw new RuntimeException("create param is null");
		}
		/*
		 * Gson gson = new Gson(); JsonObject filterOption =
		 * gson.fromJson(createRequest.getFilterOption(), JsonObject.class);
		 * StringBuffer orSb = new StringBuffer(); JsonArray orlist =
		 * filterOption.getAsJsonArray("data"); for (int i = 0; i < orlist.size(); i++)
		 * { StringBuffer andSb = new StringBuffer(); JsonObject andJson =
		 * orlist.get(i).getAsJsonObject(); JsonArray andlist =
		 * andJson.getAsJsonArray("data"); for (int j = 0; j < andlist.size(); j++) {
		 * JsonObject val = andlist.get(j).getAsJsonObject(); String filterItemName =
		 * val.get("filterItemName").getAsString(); String operate =
		 * val.get("operate").getAsString(); String value =
		 * val.get("value").getAsString(); String jdbcType =
		 * val.get("jdbcType").getAsString(); if(jdbcType.equalsIgnoreCase("string")) {
		 * value = "'"+value+"'"; } if(andSb.length() == 0) {
		 * andSb.append("(").append(filterItemName).append(" ").append(operate).
		 * append(" ").append(value); }else {
		 * andSb.append(" and ").append(filterItemName).append(" ").append(operate).
		 * append(" ").append(value); } } andSb.append(")"); if(orSb.length() == 0) {
		 * orSb.append(andSb); }else { orSb.append(" or ").append(andSb); } }
		 */
		createRequest.setOptionCondition(createRequest.getFilterOption());
		AlertFilterScene filterScene = new AlertFilterScene();
		BeanUtils.copyProperties(createRequest, filterScene);
		filterScene.setCreated_at(new Date());
		alertFilterSceneBiz.insert(filterScene);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> update(@PathVariable("filterScene_id") String filterSceneId,
			@RequestBody AlertFilterSceneDTO updateRequest) {
		if (StringUtils.isEmpty(filterSceneId)) {
			LOGGER.warn("update param FilterSceneId is null");
			return null;
		}
		if (updateRequest == null) {
			LOGGER.warn("update updateRequest note is null");
			return null;
		}
		updateRequest.setOptionCondition(updateRequest.getFilterOption());
		AlertFilterScene filterScene = new AlertFilterScene();
		BeanUtils.copyProperties(updateRequest, filterScene);
		alertFilterSceneBiz.update(filterScene);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> delete(@PathVariable("filterScene_id") String filterSceneId) {
		if (StringUtils.isEmpty(filterSceneId)) {
			LOGGER.error("delete filterSceneId is null");
			throw new RuntimeException("delete filterSceneId is null");
		}
		alertFilterSceneBiz.delete(filterSceneId);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	@Override
	public AlertFilterSceneDTO findByName(@PathVariable("filterScene_name") String filterSceneName,
			@RequestParam("filter_id") String filterId) {
		if (StringUtils.isEmpty(filterSceneName)) {
			LOGGER.error("findByName FilterSceneName is null");
			throw new RuntimeException("findByName FilterSceneName is null");
		}
		AlertFilterScene filterScene = alertFilterSceneBiz.getByName(filterSceneName, filterId);
		if (null == filterScene) {
			return null;
		}
		AlertFilterSceneDTO filterSceneDTO = new AlertFilterSceneDTO();
		BeanUtils.copyProperties(filterScene, filterSceneDTO);
		return filterSceneDTO;
	}

	@Override
	public List<AlertFilterOptionDTO> getAllOptions() {
		List<AlertFilterOptionDTO> listOption = Lists.newArrayList();
		List<AlertFilterOption> options = alertFilterSceneBiz.getAllOptions();
		if (!CollectionUtils.isEmpty(options)) {
			for (AlertFilterOption option : options) {
				AlertFilterOptionDTO optionDTO = new AlertFilterOptionDTO();
				BeanUtils.copyProperties(option, optionDTO);
				listOption.add(optionDTO);
			}
		}
		return listOption;
	}

	public List<AlertFilterOptionDTO> getOptionsByQueryType(@PathVariable("query_type") String queryType) {
		List<AlertFilterOption> options = alertFilterSceneBiz.getOptionsByQueryType(queryType);
		return TransformUtils.transform(AlertFilterOptionDTO.class, options);
	}

}

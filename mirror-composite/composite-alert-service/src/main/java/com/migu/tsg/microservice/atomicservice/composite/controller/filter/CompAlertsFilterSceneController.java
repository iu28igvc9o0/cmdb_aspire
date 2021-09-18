package com.migu.tsg.microservice.atomicservice.composite.controller.filter;

import java.util.List;
import com.aspire.mirror.alert.api.dto.filter.AlertFilterOptionDTO;
import com.aspire.mirror.alert.api.dto.filter.AlertFilterSceneDTO;
import com.aspire.mirror.alert.api.dto.filter.AlertFilterSceneResponse;
import com.aspire.mirror.composite.payload.filter.ComAlertFilterOption;
import com.aspire.mirror.composite.payload.filter.ComAlertFilterScene;
import com.aspire.mirror.composite.payload.filter.ComAlertFilterSceneRequest;
import com.aspire.mirror.composite.service.filter.ICompAlertsFilterSceneService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.filter.AlertsFilterSceneServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceV2Controller;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.aspire.mirror.common.entity.PageResponse;
import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;

/**
 * 历史告警
 * <p>
 * 项目名称: mirror平台 包:
 * com.migu.tsg.microservice.atomicservice.composite.controller.alert 类名称:
 * CompAlertsHisController.java 类描述: 历史告警控制层 创建人: JinSu 创建时间: 2018/9/19 20:10
 * 版本: v1.0
 */
@RestController
public class CompAlertsFilterSceneController extends CommonResourceV2Controller implements ICompAlertsFilterSceneService {
	private Logger logger = LoggerFactory.getLogger(CompAlertsFilterSceneController.class);

	@Autowired
	private AlertsFilterSceneServiceClient alertsFilterSceneService;

	@Override
	@ResAction(action = "view", resType = "alert_filterScene")
	public PageResponse<ComAlertFilterScene> pageList(@RequestBody ComAlertFilterSceneRequest pageRequset) {
		logger.info("method[pageList] request body is {}", pageRequset);
		/*
		 * RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		 * if (!(authCtx.getUser().isAdmin() ||
		 * authCtx.getUser().getNamespace().equals(authCtx.getUser().getUsername()))) {
		 * pageRequset.setOperateUser(authCtx.getUser().getUsername()); }
		 */
		// 调原子层
		AlertFilterSceneResponse alertsPageRequset = PayloadParseUtil.jacksonBaseParse(AlertFilterSceneResponse.class, pageRequset);
		PageResponse<AlertFilterSceneDTO> pageResponse = alertsFilterSceneService.pageList(alertsPageRequset);
		PageResponse<ComAlertFilterScene> response = new PageResponse<ComAlertFilterScene>();
		response.setCount(pageResponse.getCount());
		List<ComAlertFilterScene> alertList = Lists.newArrayList();
		if (!CollectionUtils.isEmpty(pageResponse.getResult())) {
			for (AlertFilterSceneDTO detailResponse : pageResponse.getResult()) {
				ComAlertFilterScene competailResponse = PayloadParseUtil.jacksonBaseParse(ComAlertFilterScene.class, detailResponse);
				alertList.add(competailResponse);
			}
		}
		response.setResult(alertList);
		return response;
	}

	@Override
	@ResAction(action = "view", resType = "alert_FilterScene")
	public ComAlertFilterScene findByPrimaryKey(@PathVariable("filterScene_id") String filterSceneId) {
		if (StringUtils.isEmpty(filterSceneId)) {
			logger.warn("findByPrimaryKey param FilterSceneId is null");
			return null;
		}
		AlertFilterSceneDTO filterScene = alertsFilterSceneService.findByPrimaryKey(filterSceneId);
		if (null == filterScene) {
			return null;
		}
		return PayloadParseUtil.jacksonBaseParse(ComAlertFilterScene.class, filterScene);
	}

	@Override
	@ResAction(action = "create", resType = "alert_FilterScene")
	public ResponseEntity<String> create(@RequestBody ComAlertFilterScene createRequest) {
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		logger.info("[pageList]Username is {}.", authCtx.getUser().getUsername());
		if (createRequest == null) {
			logger.error("create createRequest is null");
			throw new RuntimeException("create param is null");
		}
		AlertFilterSceneDTO obj = alertsFilterSceneService.findByName(createRequest.getName(),
				createRequest.getFilterId().toString());
		if (null != obj) {
			throw new RuntimeException("告警名称已经存在,请重新命名");
		}
		createRequest.setEditer("");
		AlertFilterSceneDTO competailResponse = PayloadParseUtil.jacksonBaseParse(AlertFilterSceneDTO.class, createRequest);
		competailResponse.setCreater(authCtx.getUser().getUsername());
		return alertsFilterSceneService.create(competailResponse);
	}

	@Override
	@ResAction(action = "update", resType = "alert_FilterScene")
	public ResponseEntity<String> update(@PathVariable("filterScene_id") String filterSceneId,
			@RequestBody ComAlertFilterScene updateRequest) {
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		if (StringUtils.isEmpty(filterSceneId)) {
			logger.warn("update param FilterSceneId is null");
			return null;
		}
		if (updateRequest == null) {
			logger.warn("update updateRequest note is null");
			return null;
		}
		AlertFilterSceneDTO FilterScene = PayloadParseUtil.jacksonBaseParse(AlertFilterSceneDTO.class, updateRequest);
		FilterScene.setEditer(authCtx.getUser().getUsername());
		return alertsFilterSceneService.update(filterSceneId, FilterScene);
	}

	@Override
	@ResAction(action = "delete", resType = "alert_FilterScene")
	public ResponseEntity<String> delete(@PathVariable("filterScene_id") String FilterSceneId) {
		if (StringUtils.isEmpty(FilterSceneId)) {
			logger.error("delete FilterSceneId is null");
			throw new RuntimeException("delete FilterSceneId is null");
		}
		return alertsFilterSceneService.delete(FilterSceneId);
	}

	@Override
	@ResAction(action = "view", resType = "alert_FilterScene")
	public ComAlertFilterScene findByName(@PathVariable("filterScene_name") String filterSceneName,
			@RequestParam("filter_id") String filterId) {
		if (StringUtils.isEmpty(filterSceneName)) {
			logger.error("findByName FilterSceneName is null");
			throw new RuntimeException("findByName FilterSceneName is null");
		}
		AlertFilterSceneDTO filterScene = alertsFilterSceneService.findByName(filterSceneName, filterId);
		if (null == filterScene) {
			return null;
		}
		ComAlertFilterScene comFilterScene = PayloadParseUtil.jacksonBaseParse(ComAlertFilterScene.class, filterScene);
		return comFilterScene;
	}

	@Override
	@ResAction(action = "query", resType = "alert_FilterScene")
	public List<ComAlertFilterOption> getAllOptions() {
		List<AlertFilterOptionDTO> options = alertsFilterSceneService.getAllOptions();
		List<ComAlertFilterOption> optionsList = Lists.newArrayList();
		if (null == options) {
			return optionsList;
		}
		optionsList = PayloadParseUtil.jacksonBaseParse(ComAlertFilterOption.class, options);
		return optionsList;
	}

	@Override
	@ResAction(action = "query", resType = "alert_FilterScene")
	public List<ComAlertFilterOption> getOptionsByQueryType(@PathVariable("query_type") String queryType) {
		List<AlertFilterOptionDTO> options = alertsFilterSceneService.getOptionsByQueryType(queryType);
		List<ComAlertFilterOption> optionsList = Lists.newArrayList();
		if (null == options) {
			return optionsList;
		}
		return PayloadParseUtil.jacksonBaseParse(ComAlertFilterOption.class, options);
	}

}

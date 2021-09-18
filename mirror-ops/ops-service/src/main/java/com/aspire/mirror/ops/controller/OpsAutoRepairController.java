package com.aspire.mirror.ops.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.ops.api.domain.GeneralResponse;
import com.aspire.mirror.ops.api.domain.OpsAutoRepairStatusEnum;
import com.aspire.mirror.ops.api.domain.PageListQueryResult;
import com.aspire.mirror.ops.api.domain.autorepair.OpsApItemDTO;
import com.aspire.mirror.ops.api.domain.autorepair.OpsApItemTypeDTO;
import com.aspire.mirror.ops.api.domain.autorepair.OpsApItemTypeDTO.OpsAutoRepairItemTypeQueryModel;
import com.aspire.mirror.ops.api.domain.autorepair.OpsApSchemePipelineDTO.SchemePipeExecStatusEnum;
import com.aspire.mirror.ops.api.domain.autorepair.OpsApSchemePipelineDTO.SchemePipeFinishActionEnum;
import com.aspire.mirror.ops.api.domain.autorepair.OpsApSchemePipelineDTO.SchemePipeJudgeTypeEnum;
import com.aspire.mirror.ops.api.domain.autorepair.OpsAutoRepairExecuteLogDTO;
import com.aspire.mirror.ops.api.domain.autorepair.OpsAutoRepairExecuteLogDTO.OpsApExecHistoryQueryModel;
import com.aspire.mirror.ops.api.domain.autorepair.OpsAutoRepairSchemeDTO;
import com.aspire.mirror.ops.api.domain.autorepair.OpsAutoRepairSchemeDTO.OpsAutoRepairSchemeQueryModel;
import com.aspire.mirror.ops.api.service.IOpsAutoRepairService;
import com.aspire.mirror.ops.biz.OpsAutoRepairBiz;
import com.aspire.mirror.ops.biz.model.OpsActionAgentResult.AgentOpsResultDetail.AspNodeResultFlagEnum;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsAutoRepairController
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年2月19日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@RestController
public class OpsAutoRepairController implements IOpsAutoRepairService {
	@Autowired
	private OpsAutoRepairBiz		autoRepairBiz;
	
	@Override
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void syncApItemList(@RequestBody List<OpsApItemTypeDTO> apItemSyncList) {
		autoRepairBiz.syncApItemTypeList(apItemSyncList);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<Map<Integer, String>> loadApStatusList() {
		List<Map<Integer, String>> result = new ArrayList<Map<Integer, String>>();
		for (OpsAutoRepairStatusEnum status : OpsAutoRepairStatusEnum.values()) {
			result.add(Collections.singletonMap(status.getStatusCode(), status.getLabel()));
		}
		return result;
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public PageListQueryResult<OpsApItemTypeDTO> queryOpsAutoRepairItemTypeList(
			@RequestBody OpsAutoRepairItemTypeQueryModel queryParam) {
		return autoRepairBiz.queryOpsAutoRepairItemTypeList(queryParam);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<OpsApItemDTO> queryOpsAutoRepairItemListByItemType(@PathVariable("itemTypeId") Long itemTypeId) {
		return autoRepairBiz.queryOpsAutoRepairItemListByItemType(itemTypeId);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<Map<Integer, String>> loadApPipeFinishActionList() {
		List<Map<Integer, String>> result = new ArrayList<>();
		for (SchemePipeFinishActionEnum action : SchemePipeFinishActionEnum.values()) {
			result.add(Collections.singletonMap(action.getCode(), action.getLabel()));
		}
		return result;
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<Map<String, String>> loadApPipeFinishJudgeTypeList() {
		List<Map<String, String>> result = new ArrayList<>();
		for (SchemePipeJudgeTypeEnum judgeType : SchemePipeJudgeTypeEnum.values()) {
			result.add(Collections.singletonMap(judgeType.getCode(), judgeType.getLabel()));
		}
		return result;
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<Map<String, String>> loadApPipeFinishJudgeDropdownList(@PathVariable("judgeType") String judgeType) {
		List<Map<String, String>> result = new ArrayList<>();
		if (SchemePipeJudgeTypeEnum.EXEC_STATUS.getCode().equals(judgeType)) {
			for (SchemePipeExecStatusEnum execStatus : SchemePipeExecStatusEnum.values()) {
				result.add(Collections.singletonMap(execStatus.getCode(), execStatus.getLabel()));
			}
		} 
		else if (SchemePipeJudgeTypeEnum.ASPNODE_RESULT.getCode().equals(judgeType)) {
			for (AspNodeResultFlagEnum execStatus : AspNodeResultFlagEnum.values()) {
				result.add(Collections.singletonMap(String.valueOf(execStatus.getCode()), execStatus.getLabel()));
			}
		}
		return result;
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse saveOpsAutoRepairScheme(@RequestBody OpsAutoRepairSchemeDTO dto) {
		return autoRepairBiz.saveOpsAutoRepairScheme(dto);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
    public GeneralResponse removeOpsAutoRepairScheme(@PathVariable("schemeId") String joinSchemeIds) {
		String[] schemeIdArr = StringUtils.split(joinSchemeIds, ',');
		List<Long> schemeIdList = Arrays.asList(schemeIdArr).stream().map(schemeId -> {
			return Long.valueOf(schemeId);
		}).collect(Collectors.toList());
		return autoRepairBiz.removeOpsAutoRepairScheme(schemeIdList);
    }
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public PageListQueryResult<OpsAutoRepairSchemeDTO> queryOpsAutoRepairSchemeList(
					@RequestBody OpsAutoRepairSchemeQueryModel queryParam) {
		return autoRepairBiz.queryOpsAutoRepairSchemeList(queryParam);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse manualHandleApSchemeExecute(
			@PathVariable("schemeExecLogId") Long schemeExecLogId, @PathVariable("manualStatus") Integer manualStatus) {
		if (manualStatus == 1) {
			return autoRepairBiz.mannualActiveApSchemeNextPipe(schemeExecLogId);
		}
		return autoRepairBiz.mannualTerminateApSchemeExecution(schemeExecLogId);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public PageListQueryResult<OpsAutoRepairExecuteLogDTO> queryOpsAutoRepairExecHistory(
				@RequestBody OpsApExecHistoryQueryModel queryParam) {
		return autoRepairBiz.queryOpsAutoRepairExecHistory(queryParam);
	}
}

/**
 *
 * 项目名： composite-service 
 * <p/> 
 *
 * 文件名:  CompOpsAutoRepairController.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年3月16日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2020 卓望公司-版权所有<br/>
 *   
 */
package com.migu.tsg.microservice.atomicservice.composite.controller.opsmanage;

import java.util.List;
import java.util.Map;

import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ResourceAuthHelper;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.composite.service.opsmanage.ICompOpsAutoRepairService;
import com.aspire.mirror.ops.api.domain.GeneralResponse;
import com.aspire.mirror.ops.api.domain.PageListQueryResult;
import com.aspire.mirror.ops.api.domain.autorepair.OpsApItemDTO;
import com.aspire.mirror.ops.api.domain.autorepair.OpsApItemTypeDTO;
import com.aspire.mirror.ops.api.domain.autorepair.OpsApItemTypeDTO.OpsAutoRepairItemTypeQueryModel;
import com.aspire.mirror.ops.api.domain.autorepair.OpsAutoRepairExecuteLogDTO;
import com.aspire.mirror.ops.api.domain.autorepair.OpsAutoRepairExecuteLogDTO.OpsApExecHistoryQueryModel;
import com.aspire.mirror.ops.api.domain.autorepair.OpsAutoRepairSchemeDTO;
import com.aspire.mirror.ops.api.domain.autorepair.OpsAutoRepairSchemeDTO.OpsAutoRepairSchemeQueryModel;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.opsmanage.OpsAutoRepairClient;

/** 
 *
 * 项目名称: composite-service 
 * <p/>
 * 
 * 类名: CompOpsAutoRepairController
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年3月13日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@RestController
public class CompOpsAutoRepairController implements ICompOpsAutoRepairService {
	@Autowired
	private OpsAutoRepairClient autoRepairClient;

	@Autowired
	protected ResourceAuthHelper resAuthHelper;


	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<Map<Integer, String>> loadApStatusList() {
		return autoRepairClient.loadApStatusList();
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public PageListQueryResult<OpsApItemTypeDTO> queryOpsAutoRepairItemTypeList(
			@RequestBody OpsAutoRepairItemTypeQueryModel queryParam) {
		return autoRepairClient.queryOpsAutoRepairItemTypeList(queryParam);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<OpsApItemDTO> queryOpsAutoRepairItemListByItemType(@PathVariable("itemTypeId") Long itemTypeId) {
		return autoRepairClient.queryOpsAutoRepairItemListByItemType(itemTypeId);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<Map<Integer, String>> loadApPipeFinishActionList() {
		return autoRepairClient.loadApPipeFinishActionList();
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<Map<String, String>> loadApPipeFinishJudgeTypeList() {
		return autoRepairClient.loadApPipeFinishJudgeTypeList();
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<Map<String, String>> loadApPipeFinishJudgeDropdownList(@PathVariable("judgeType") String judgeType) {
		return autoRepairClient.loadApPipeFinishJudgeDropdownList(judgeType);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	@ResAction(action = "create", resType = "selfHealing")
	public GeneralResponse saveOpsAutoRepairScheme(@RequestBody OpsAutoRepairSchemeDTO autoRepairScheme) {
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

		return autoRepairClient.saveOpsAutoRepairScheme(autoRepairScheme);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	@ResAction(action = "delete", resType = "selfHealing")
	public GeneralResponse removeOpsAutoRepairScheme(@PathVariable("schemeId") String joinSchemeIds) {
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

		return autoRepairClient.removeOpsAutoRepairScheme(joinSchemeIds);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	@ResAction(action = "view", resType = "selfHealing")
	public PageListQueryResult<OpsAutoRepairSchemeDTO> queryOpsAutoRepairSchemeList(@RequestBody OpsAutoRepairSchemeQueryModel queryParam) {
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

		return autoRepairClient.queryOpsAutoRepairSchemeList(queryParam);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse manualHandleApSchemeExecute(
			@PathVariable("schemeExecLogId") Long schemeExecLogId, @PathVariable("manualStatus") Integer manualStatus) {
		return autoRepairClient.manualHandleApSchemeExecute(schemeExecLogId, manualStatus);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	@ResAction(action = "view", resType = "selfHealingLog")
	public PageListQueryResult<OpsAutoRepairExecuteLogDTO> queryOpsAutoRepairExecHistory(@RequestBody OpsApExecHistoryQueryModel queryParam) {
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

		return autoRepairClient.queryOpsAutoRepairExecHistory(queryParam);
	}
}

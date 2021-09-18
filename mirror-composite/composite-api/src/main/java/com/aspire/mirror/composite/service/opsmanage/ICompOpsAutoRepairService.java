package com.aspire.mirror.composite.service.opsmanage;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aspire.mirror.ops.api.domain.GeneralResponse;
import com.aspire.mirror.ops.api.domain.OpsAccount;
import com.aspire.mirror.ops.api.domain.PageListQueryResult;
import com.aspire.mirror.ops.api.domain.autorepair.OpsApItemDTO;
import com.aspire.mirror.ops.api.domain.autorepair.OpsApItemTypeDTO;
import com.aspire.mirror.ops.api.domain.autorepair.OpsApItemTypeDTO.OpsAutoRepairItemTypeQueryModel;
import com.aspire.mirror.ops.api.domain.autorepair.OpsAutoRepairExecuteLogDTO;
import com.aspire.mirror.ops.api.domain.autorepair.OpsAutoRepairExecuteLogDTO.OpsApExecHistoryQueryModel;
import com.aspire.mirror.ops.api.domain.autorepair.OpsAutoRepairSchemeDTO;
import com.aspire.mirror.ops.api.domain.autorepair.OpsAutoRepairSchemeDTO.OpsAutoRepairSchemeQueryModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: ICompOpsAutoRepairService
 * <p/>
 *
 * 类功能描述: Ops动作执行接口
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月30日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Api(value = "ops告警自愈")
@RequestMapping(value = "/v1/ops-service/opsAutoRepair")
public interface ICompOpsAutoRepairService {
	
	@GetMapping(value = "/loadApStatusList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取自愈状态列表", notes = "获取自愈状态列表",
            response = GeneralResponse.class, tags = {"Ops Auto Repair service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    List<Map<Integer, String>> loadApStatusList();
	
	@PostMapping(value = "/queryOpsAutoRepairItemTypeList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询自愈指标类型列表", notes = "查询自愈指标类型列表", 
    				response = GeneralResponse.class, tags = {"Ops Auto Repair service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsApItemTypeDTO.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
	PageListQueryResult<OpsApItemTypeDTO> queryOpsAutoRepairItemTypeList(@RequestBody OpsAutoRepairItemTypeQueryModel queryParam);

	
	@PostMapping(value = "/queryOpsAutoRepairItemListByItemType/{itemTypeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据指标类型id查询自愈指标列表", notes = "根据指标类型id查询自愈指标列表", 
    				response = GeneralResponse.class, tags = {"Ops Auto Repair service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsApItemDTO.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
	List<OpsApItemDTO> queryOpsAutoRepairItemListByItemType(@PathVariable("itemTypeId") Long itemTypeId);
	

	@GetMapping(value = "/loadApSchemePipeFinishActionList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取自愈作业完毕动作列表", notes = "获取自愈作业完毕动作列表", 
    			  response = GeneralResponse.class, tags = {"Ops Auto Repair service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = Map.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
	List<Map<Integer, String>> loadApPipeFinishActionList();
	
	@GetMapping(value = "/loadApPipeFinishJudgeTypeList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取自愈作业完成判断类型列表", notes = "获取自愈作业完成判断类型列表", 
    			  response = GeneralResponse.class, tags = {"Ops Auto Repair service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = Map.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
	List<Map<String, String>> loadApPipeFinishJudgeTypeList();
	
	@GetMapping(value = "/loadApPipeFinishJudgeDropdownList/{judgeType}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取自愈作业判断类型下拉列表", notes = "获取自愈作业判断类型下拉列表", 
    			  response = GeneralResponse.class, tags = {"Ops Auto Repair service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = Map.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
	List<Map<String, String>> loadApPipeFinishJudgeDropdownList(@PathVariable("judgeType") String judgeType);
	
	@PostMapping(value = "/saveOpsAutoRepairScheme", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "保存自愈方案", notes = "保存自愈方案", response = GeneralResponse.class, tags = {"Ops Auto Repair service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse saveOpsAutoRepairScheme(@RequestBody OpsAutoRepairSchemeDTO autoRepairScheme);
	
	@DeleteMapping(value = "/removeOpsAutoRepairScheme/{schemeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "删除自愈方案", notes = "删除自愈方案", response = GeneralResponse.class, tags = {"Ops Auto Repair service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse removeOpsAutoRepairScheme(@PathVariable("schemeId") String joinSchemeIds);
	
	@PostMapping(value = "/queryOpsAutoRepairSchemeList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询自愈方案列表", notes = "查询自愈方案列表", response = GeneralResponse.class, tags = {"Ops Auto Repair service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsAccount.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
	PageListQueryResult<OpsAutoRepairSchemeDTO> queryOpsAutoRepairSchemeList(@RequestBody OpsAutoRepairSchemeQueryModel queryParam);
	
	@PutMapping(value = "/manualHandleApSchemeExecute/{schemeExecLogId}/{manualStatus}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "人工处理自愈流程", notes = "人工处理自愈流程", response = GeneralResponse.class, tags = {"Ops Auto Repair service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsAccount.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
	public GeneralResponse manualHandleApSchemeExecute(
			@PathVariable("schemeExecLogId") Long schemeExecLogId, @PathVariable("manualStatus") Integer manualStatus);
	
	@PostMapping(value = "/queryOpsAutoRepairExecHistory", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询自愈方案执行历史", notes = "查询自愈方案执行历史", response = GeneralResponse.class, tags = {"Ops Auto Repair service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsAccount.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
	PageListQueryResult<OpsAutoRepairExecuteLogDTO> queryOpsAutoRepairExecHistory(@RequestBody OpsApExecHistoryQueryModel queryParam);
}

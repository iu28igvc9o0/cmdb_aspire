package com.aspire.mirror.composite.service.opsmanage;

import java.util.List;
import java.util.Map;

import com.aspire.mirror.ops.api.domain.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.aspire.mirror.ops.api.domain.OpsAgentStepInstanceResult.OpsAgentStepInstanceResultQueryModel;
import com.aspire.mirror.ops.api.domain.OpsLabel.OpsLabelQueryModel;
import com.aspire.mirror.ops.api.domain.OpsPipelineDTO.OpsPipelineQueryModel;
import com.aspire.mirror.ops.api.domain.OpsPipelineRunJob.OpsPipelineRunJobQueryModel;
import com.aspire.mirror.ops.api.domain.SimpleAgentHostInfo.SimpleAgentHostQueryModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: IOpsManageService
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
@Api(value = "ops服务管理")
@RequestMapping(value = "/v1/ops-service/opsManage/")
public interface ICompOpsManageService {
	
	@GetMapping(value = "/getAgentHostInfoLoadSource", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取agent主机信息加载源", notes = "获取agent主机信息加载源",
            response = SimpleAgentHostInfo.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    public GeneralResponse getAgentHostInfoLoadSource();
	
	@PostMapping(value = "/fetchUserAuthedAgentHostList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取当前用户有权限的agent主机列表", notes = "获取当前用户有权限的agent主机列表", 
    			  response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = SimpleAgentHostInfo.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
	PageListQueryResult<SimpleAgentHostInfo> fetchUserAuthedAgentHostList(@RequestBody SimpleAgentHostQueryModel queryParam);
	
	@GetMapping(value = "/loadOpsStatusList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取操作状态列表", notes = "获取操作状态列表", 
    			  response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = SimpleAgentHostInfo.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
	List<Map<Integer, String>> loadOpsStatusList();
	
	@GetMapping(value = "/loadOpsTriggerWayList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取ops操作触发方式列表", notes = "获取ops操作触发方式列表", 
    			  response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = SimpleAgentHostInfo.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
	List<Map<Integer, String>> loadOpsTriggerWayList();
	
	@PostMapping(value = "/saveOpsAccount", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "保存账户", notes = "保存账户", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse saveOpsAccount(@RequestBody OpsAccount account);
	
	@DeleteMapping(value = "/removeOpsAccount/{accountName}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "删除账户", notes = "删除账户", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse removeOpsAccount(@PathVariable("accountName") String accountName);
	
	@PostMapping(value = "/queryOpsAccountList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询账户列表", notes = "查询账户列表", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsAccount.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    List<OpsAccount> queryOpsAccountList(@RequestBody OpsAccountQueryModel queryParam);
	
	@PostMapping(value = "/saveOpsLabel", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "保存分类标签", notes = "保存分类标签", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse saveOpsLabel(@RequestBody OpsLabel label);
    
    @DeleteMapping(value = "/removeLabel/{labelCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "删除分类标签", notes = "删除分类标签", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse removeOpsLabel(@PathVariable("labelCode") String labelCode);
    
    @PostMapping(value = "/queryOpsLabelList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询分类标签列表", notes = "查询分类标签列表", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsLabel.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    List<OpsLabel> queryOpsLabelList(@RequestBody OpsLabelQueryModel queryParam);

    @PostMapping(value = "/saveScript", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "保存脚本", notes = "保存脚本", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse saveOpsScript(@RequestBody OpsScript script);

    @PostMapping(value = "/copyScript", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "克隆脚本", notes = "克隆脚本", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse copyScript(@RequestBody OpsScript script);
    
    @DeleteMapping(value = "/removeScript/{scriptId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "删除脚本", notes = "删除脚本", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse removeOpsScript(@PathVariable("scriptId") Long scriptId);
    
    @PostMapping(value = "/queryOpsScriptList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询脚本列表", notes = "查询脚本列表", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsScript.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    PageListQueryResult<OpsScript> queryOpsScriptList(@RequestBody OpsScriptQueryModel queryParam);
    
    @GetMapping(value = "/queryOpsScriptById", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据句id查询脚本", notes = "根据句id查询脚本", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsScript.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    OpsScript queryOpsScriptById(@RequestParam("scriptId") Long scriptId);
    
    @PostMapping(value = "/readLocalScriptFile", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "读取本地脚本文件", notes = "读取本地脚本文件", response = GeneralResponse.class, tags = {"Ops Manage service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	GeneralResponse readLocalScriptFile(@RequestParam("file") MultipartFile file);
    
    @PostMapping(value = "/saveOpsPipeline", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "保存作业", notes = "保存作业", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse saveOpsPipeline(@RequestBody OpsPipelineDTO pipeline);

    @PostMapping(value = "/copyOpsPipeline", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "克隆作业", notes = "克隆作业", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse copyOpsPipeline(@RequestBody OpsPipelineDTO pipeline);
    
    @DeleteMapping(value = "/removePipeline/{pipelineId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "删除作业", notes = "删除作业", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse removeOpsPipeline(@PathVariable("pipelineId") Long pipelineId);
    
    @PostMapping(value = "/queryOpsPipelineList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询作业列表", notes = "查询作业列表", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsPipelineDTO.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    PageListQueryResult<OpsPipelineDTO> queryOpsPipelineList(@RequestBody OpsPipelineQueryModel queryParam);
    
    @GetMapping(value = "/queryOpsPipelineById/{pipelineId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据id查询作业", notes = "根据id查询作业", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsPipelineDTO.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    OpsPipelineDTO queryOpsPipelineById(@PathVariable("pipelineId") Long pipelineId);
    
    @GetMapping(value = "/queryOpsStepListByPipelineId/{pipelineId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询步骤定义列表", notes = "查询步骤定义列表", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsStepDTO.class),
    		@ApiResponse(code = 500, message = "Unexpected error")})
    List<OpsStepDTO> queryOpsStepListByPipelineId(@PathVariable("pipelineId") Long pipelineId);
    
    @PostMapping(value = "/queryOpsPipelineInstanceList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询执行历史", notes = "查询执行历史", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsPipelineInstanceDTO.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    PageListQueryResult<OpsPipelineInstanceDTO> queryOpsPipelineInstanceList(@RequestBody OpsPipelineInstanceQueryParam queryParam);
    
    @GetMapping(value = "/queryOpsPipelineInstanceById/{pipelineInstanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据id查询作业执行实例详情", notes = "根据id查询作业执行实例详情", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsPipelineDTO.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    OpsPipelineInstanceDTO queryOpsPipelineInstanceById(@PathVariable("pipelineInstanceId") Long pipelineInstanceId);
    
    @PostMapping(value = "/queryStepInstListByPipelineInstId/{pipelineInstanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询执行步骤列表", notes = "查询执行步骤列表", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsStepInstanceDTO.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    List<OpsStepInstanceDTO> queryStepInstListByPipelineInstId(@PathVariable("pipelineInstanceId") Long pipelineInstanceId);
    
    @GetMapping(value = "/queryStepInstanceById", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据id查询执行步骤实例", notes = "根据id查询执行步骤实例", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsStepInstanceDTO.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    OpsStepInstanceDTO queryStepInstanceById(@RequestParam("stepInstId") Long stepInstId);
    
    @PostMapping(value = "/queryOpsStepInstanceAgentRunResultList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询步骤执行主机详情列表", notes = "查询步骤执行主机详情列表", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsAgentStepInstanceResult.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    PageListQueryResult<OpsAgentStepInstanceResult> queryOpsStepAgentRunResultList(@RequestBody OpsAgentStepInstanceResultQueryModel param);
    
    @PutMapping(value = "/realtimeScriptExecute", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "实时脚本执行", notes = "实时脚本执行", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse executeRealtimeScript(@RequestBody OpsScriptExecuteActionModel requestData);
    
    @PutMapping(value = "/realtimeFileTransfer", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "实时文件分发", notes = "实时文件分发", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse executeRealtimeFileTransfer(@RequestBody OpsFileTransferActionModel requestData);
    
	@PostMapping(value = "/uploadFile4Transfer", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "上传本地文件", notes = "上传本地文件", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse uploadFile4Transfer(@RequestParam("file") MultipartFile file);
    
    @PutMapping(value = "/pipelineExecute/{pipelineId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "作业执行", notes = "作业执行", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse executePipeline(@PathVariable("pipelineId") Long pipelineId, 
    		@RequestBody(required=false) Map<String, Object> params);
    
    @PutMapping(value = "/manualHandleOpsStepInstance/{stepInstanceId}/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "手动处理作业步骤执行", notes = "手动处理作业步骤执行", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse manualHandleOpsStepInstance(@PathVariable("stepInstanceId") Long stepInstanceId, 
    			@PathVariable("status") Integer status);
    
    @PutMapping(value = "/executeIndexValueScriptCollect", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "指标脚本采集", notes = "指标脚本采集", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse executeIndexValueScriptCollect(@RequestBody OpsIndexValueCollectRequest indexCollectData);
    
    @PostMapping(value = "/saveOpsPipelineRunJob", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "保存定时作业", notes = "保存定时作业", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse saveOpsPipelineRunJob(@RequestBody OpsPipelineRunJob runJob);
    
    @DeleteMapping(value = "/removeOpsPipelineRunJob/{jobId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "删除定时作业", notes = "删除定时作业", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse removeOpsPipelineRunJob(@PathVariable("jobId") Long jobId);
    
    @PostMapping(value = "/queryOpsPipelineRunJobList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询定时作业列表", notes = "查询删除定时作业列表", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    PageListQueryResult<OpsPipelineRunJob> queryOpsPipelineRunJobList(@RequestBody OpsPipelineRunJobQueryModel queryParam);
    
    @PutMapping(value = "/schedulePipelineCronJob/{jobId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "启动定时作业", notes = "启动定时作业", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse schedulePipelineCronJob(@PathVariable("jobId") Long jobId);
    
    @PutMapping(value = "/unSchedulePipelineCronJob/{jobId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "停止定时作业", notes = "停止定时作业", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse unSchedulePipelineCronJob(@PathVariable("jobId") Long jobId);
    
    @PutMapping(value = "/executePipelineCronJob/{jobId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "定时作业执行回调接口", notes = "定时作业执行回调接口", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse executePipelineCronJob(@PathVariable("jobId") Long jobId);

    @PutMapping(value="/reviewSensitiveApply/{pipelineInstanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "指令审核申请", notes = "指令审核申请", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse reviewSensitiveApply(@PathVariable("pipelineInstanceId") Long pipelineInstanceId);

    @PostMapping(value = "/savePipelineScenes", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "场景保存", notes = "场景保存", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse savePipelineScences(@RequestBody OpsPipelineScenes scenes, @RequestParam(name = "scenes_picture", required = false)
            MultipartFile picture);

    @GetMapping(value = "/pipelineScenesAllList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "场景列表", notes = "场景列表", response = GroupScenes.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GroupScenes.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    List<GroupScenes> pipelineScenesAllList();

    @GetMapping(value = "/pipelineScenesById", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "场景详情", notes = "场景详情", response = GroupScenes.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GroupScenes.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    OpsPipelineScenes pipelineScenesById(@RequestParam("pipeline_scenes_id") Long pipelineScenesId);

    @DeleteMapping(value = "/deletePipelineScenes", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "场景删除", notes = "场景删除", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse deletePipelineScenes(@RequestParam("scenes_ids") String scenesIds);

    @PostMapping(value = "/downloadLogFile", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "下载日志", notes = "下载日志", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    void downloadLogFile(@RequestBody Map<String, String> downParam);

    @GetMapping(value = "/getPipelineInstanceLog", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取实例日志", notes = "获取实例日志", response = OpsPipelineInstanceLog.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsPipelineInstanceLog.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    OpsPipelineInstanceLog getPipelineInstanceLog(@RequestParam("pipelineInstanceId") Long pipelineInstanceId);

    @GetMapping(value = "/logPackageApply", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "执行日志打包申请", notes = "执行日志打包申请", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse logPackageApply(@RequestParam("pipelineInstanceId") Long pipelineInstanceId);
//    Summary
//    @PutMapping(value = "/realtimeOsFileTransfer", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value = "实时Os文件分发", notes = "实时Os文件分发", response = GeneralResponse.class, tags = {"Ops Manage service API"})
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
//            @ApiResponse(code = 500, message = "Unexpected error")})
//    GeneralResponse realtimeOsFileTransfer(@RequestBody OpsFileTransferActionModel requestData);
    @PostMapping(value = "/downloadSummaryOutput", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "下载汇总产出", notes = "下载汇总产出", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    void downloadSummaryOutput(@RequestParam("pipelineInstanceId") Long pipelineInstanceId);

    @GetMapping(value = "/getParamAllList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取参数列表", notes = "获取参数列表", response = OpsParam.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsParam.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    List<OpsParam> getParamAllList();
    
    @PostMapping(value = "/searchParamList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据条件查询参数列表", notes = "根据条件查询参数列表", response = OpsParam.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsParam.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageListQueryResult<OpsParam> searchParamList(@RequestBody OpsParamQueryModel paramModel);
    
    @PostMapping(value = "/createOpsParam", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "新增参数", notes = "新增参数", response = OpsParam.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse createOpsParam(@RequestBody OpsParam createModel);
    
    @PutMapping(value = "/updateOpsParam", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改参数", notes = "修改参数", response = OpsParam.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse updateOpsParam(@RequestBody OpsParam updateModel);
    
    @DeleteMapping(value = "/deleteOpsParamById/{paramId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据id删除参数", notes = "根据id删除参数", response = OpsParam.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse deleteOpsParamById(@PathVariable("paramId") Long paramId);
    
    @GetMapping(value = "/loadAllParamTypeList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取参数类型列表", notes = "获取参数类型列表", response = OpsParam.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsParamType.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    public List<OpsParamType> loadAllParamTypeList();
    
    @GetMapping(value = "/queryReferParamListByEntityId/{entityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取参数类型列表", notes = "获取参数类型列表", response = OpsParam.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsParamReference.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    public List<OpsParamReference> queryReferParamListByEntityId(@PathVariable("entityId") Long entityId);

    @PutMapping(value = "/auditPipeline", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "审核作业", notes = "审核作业", response = OpsParam.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsParam.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse auditPipeline(@RequestParam("pipelineId") Long pipelineId, @RequestParam("auditStatus") String auditStatus, @RequestParam(name = "auditDesc", required = false) String auditDesc);

    @PutMapping(value = "/auditScript", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "审核脚本", notes = "审核脚本", response = OpsParam.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsParam.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse auditScript(@RequestParam("scriptId") Long scriptId, @RequestParam("auditStatus") String auditStatus, @RequestParam(name = "auditDesc") String auditDesc);

    @PutMapping(value = "/continueExecInstance", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "继续执行作业实例", notes = "继续执行作业实例", response = OpsParam.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsParam.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse continueExecInstance(@RequestParam("pipelineInstanceId") Long pipelineInstanceId);


    @GetMapping(value = "/getPipelineHisListByPipelineId", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据作业id获取作业历史列表", notes = "根据作业id获取作业历史列表", response = OpsParam.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsParam.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    List<OpsPipelineHis> getPipelineHisListByPipelineId(@RequestParam("pipelineId") Long pipelineId);

    @GetMapping(value = "/getScriptHisListByScriptId", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据脚本id获取脚本历史列表", notes = "根据脚本id获取脚本历史列表", response = OpsParam.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsParam.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    List<OpsScriptHis> getScriptHisListByScriptId(@RequestParam("scriptId") Long scriptId);

    @GetMapping(value = "/queryOpsStepHisListByPipelineHisId", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询历史作业步骤列表", notes = "查询历史作业步骤列表", response = OpsStepHis.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsStepHis.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    List<OpsStepHis> queryOpsStepHisListByPipelineHisId(@RequestParam("pipelineHisId") Long pipelineHisId);

    @GetMapping(value = "/queryOpsPipelineHisById", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询作业历史详情", notes = "查询作业历史详情", response = OpsPipelineHis.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsPipelineHis.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    OpsPipelineHis queryOpsPipelineHisById(@RequestParam("pipelineHisId") Long pipelineHisId);

    @GetMapping(value = "/queryOpsScriptHisById", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据id查询脚本历史详情", notes = "根据id查询脚本历史详情", response = OpsScriptHis.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsScriptHis.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    OpsScriptHis queryOpsScriptHisById(@RequestParam("scriptHisId") Long scriptHisId);

    @PutMapping(value = "/updatePipelineVersion", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "作业版本变更", notes = "作业版本变更", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse updatePipelineVersion(@RequestParam("pipelineHisId") Long pipelineHisId);

    @PutMapping(value = "/updateScriptVersion", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "脚本版本变更", notes = "脚本版本变更", response = GeneralResponse.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse updateScriptVersion(@RequestParam("scriptHisId") Long scriptHisId);

    @PostMapping(value = "/getNormalAgentHostList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取本服务详细agent设备列表", notes = "获取本服务详细agent设备列表",
            response = NormalAgentHostInfo.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = NormalAgentHostInfo.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageListQueryResult<NormalAgentHostInfo> getNormalAgentHostList(@RequestBody AgentHostQueryModel queryParam);

    @GetMapping(value = "/getUsernameListByAgentIp", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据agentIp查询本地存在账号信息列表", notes = "根据agentIp查询本地存在账号信息列表",
            response = String.class, tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = String.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    List<String> getUsernameListByAgentIp(@RequestParam("agentIp") String agentIp);

    @PostMapping(value = "/exportUserPassword/{downloadPassword}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "导出用户密码", notes = "导出用户密码", tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    void exportUserPassword(@PathVariable("downloadPassword") String downloadPassword, @RequestBody AgentHostQueryModel queryParam);

    @GetMapping(value = "/exportStepTargetIp/{step_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "导出步骤设备ip", notes = "导出步骤设备ip", tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    void exportStepTargetIp(@PathVariable("step_id") Long stepId);

    @GetMapping(value = "/downloadTargetHostTemplate", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "", notes = "", tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    void downloadTargetHostTemplate(HttpServletResponse response);

    @PostMapping(value = "/importTargetHost", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "导入目标机器设备", notes = "导入目标机器设备", tags = {"Ops Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    Map<String, Object> importTargetHost(@RequestParam("file") MultipartFile file, HttpServletRequest request);
}

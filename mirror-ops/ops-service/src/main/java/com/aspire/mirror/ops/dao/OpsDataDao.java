/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  OpsManageDataDao.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月29日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.ops.api.domain.OpsAccount;
import com.aspire.mirror.ops.api.domain.OpsAccountQueryModel;
import com.aspire.mirror.ops.api.domain.OpsAgentStepInstanceResult;
import com.aspire.mirror.ops.api.domain.OpsLabel;
import com.aspire.mirror.ops.api.domain.OpsLabel.OpsLabelQueryModel;
import com.aspire.mirror.ops.api.domain.OpsParam;
import com.aspire.mirror.ops.api.domain.OpsParamQueryModel;
import com.aspire.mirror.ops.api.domain.OpsParamReference;
import com.aspire.mirror.ops.api.domain.OpsParamType;
import com.aspire.mirror.ops.api.domain.OpsParamValue;
import com.aspire.mirror.ops.api.domain.OpsParamValueDetail;
import com.aspire.mirror.ops.api.domain.OpsPipelineDTO.OpsPipelineQueryModel;
import com.aspire.mirror.ops.api.domain.OpsPipelineHis;
import com.aspire.mirror.ops.api.domain.OpsPipelineInstanceLog;
import com.aspire.mirror.ops.api.domain.OpsPipelineInstanceQueryParam;
import com.aspire.mirror.ops.api.domain.OpsPipelineRunJob;
import com.aspire.mirror.ops.api.domain.OpsPipelineRunJob.OpsPipelineRunJobQueryModel;
import com.aspire.mirror.ops.api.domain.OpsPipelineScenes;
import com.aspire.mirror.ops.api.domain.OpsScript;
import com.aspire.mirror.ops.api.domain.OpsScriptHis;
import com.aspire.mirror.ops.api.domain.OpsScriptQueryModel;
import com.aspire.mirror.ops.api.domain.OpsStepHis;
import com.aspire.mirror.ops.domain.OpsPipeline;
import com.aspire.mirror.ops.domain.OpsPipelineAuditInfo;
import com.aspire.mirror.ops.domain.OpsPipelineInstance;
import com.aspire.mirror.ops.domain.OpsScriptAuditInfo;
import com.aspire.mirror.ops.domain.OpsStep;
import com.aspire.mirror.ops.domain.OpsStepInstance;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsDataDao
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月29日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Mapper
public interface OpsDataDao {
	
	OpsAccount queryAccountByName(@Param("accountName") String accountName);
	void insertOpsAccount(OpsAccount account);
	List<OpsAccount> queryOpsAccountList(OpsAccountQueryModel param);
	void removeOpsAccount(@Param("accountName") String accountName);
	
	void insertOpsLabel(OpsLabel queryParam);
	void updateOpsLabel(OpsLabel queryParam);
	void removeOpsLabel(@Param("code") String code);
	List<OpsLabel> queryOpsLabelList(OpsLabelQueryModel queryParam);
	OpsLabel queryOpsLabelByCode(@Param("code") String code);
	Integer queryOpsLabelReferCount(@Param("code") String code);
	
	void insertOpsScript(OpsScript script);
	Integer queryOpsScriptCountByName(@Param("scriptName") String scriptName);
	OpsScript queryOpsScriptById(@Param("scriptId") Long scriptId);
	
	List<OpsScript> queryOpsScriptList(@Param("param") OpsScriptQueryModel scriptParam, 
			@Param("resFilterMap") Map<String, List<String>> resFilterMap);
	Integer queryOpsScriptListTotalSize(@Param("param") OpsScriptQueryModel queryParam, 
			@Param("resFilterMap") Map<String, List<String>> resFilterMap);
	
	void updateOpsScript(OpsScript script);
	void removeOpsScriptById(@Param("scriptId") Long scriptId);
	void removeOpsScriptByStepId(@Param("stepId") Long stepId);
	void removeOpsScriptByPipelineId(@Param("pipelineId") Long pipelineId);
	void removePipelineById(@Param("pipelineId") Long pipelineId);
	void removeAllOpsStepsByPipelineId(@Param("pipelineId") Long pipelineId);
	void insertOpsPipeline(OpsPipeline pipeline);
	void updateOpsPipeline(OpsPipeline pipeline);
	List<OpsPipeline> queryOpsPipelineList(@Param("param") OpsPipelineQueryModel queryParam, @Param("resFilterMap") Map<String, List<String>> resFilterMap);
	Integer queryOpsPipelineTotalCount(@Param("param") OpsPipelineQueryModel queryParam, @Param("resFilterMap") Map<String, List<String>> resFilterMap);
	void insertOpsStep(OpsStep step);
	void updateOpsStep(OpsStep step);
	void removeOpsStepById(@Param("stepId") Long stepId);
	
	OpsStepInstance queryStepInstanceById(@Param("stepInstanceId") Long stepInstanceId);
	
	List<OpsStep> queryOpsStepListByPipelineId(@Param("pipelineId") Long pipelineId);
	Integer queryOpsStepCountByName(@Param("stepName") String stepName);
	
	List<OpsPipelineInstance> queryOpsPipelineInstanceList(@Param("param") OpsPipelineInstanceQueryParam queryParam, @Param("resFilterMap") Map<String, List<String>> resFilterMap);
	Integer queryOpsPipelineInstanceTotalSize(@Param("param") OpsPipelineInstanceQueryParam queryParam, @Param("resFilterMap") Map<String, List<String>> resFilterMap);
	
	List<OpsStepInstance> queryStepInstListByPipelineInstId(@Param("pipelineInstanceId") Long pipelineInstanceId);
	
	OpsStepInstance queryStepInstanceByPipelineOrder(@Param("pipelineInstanceId") Long pipelineInstanceId, @Param("order") int order);
	
	List<OpsStepInstance> queryTimeoutStepInstanceList(@Param("checkTime") Long checkTime);
	
	void insertStepInstance(OpsStepInstance stepInstance);
	
	void updateStepInstance(OpsStepInstance updateData);
	
	OpsPipeline queryPipelineById(@Param("pipelineId") Long pipelineId);
	
	OpsPipelineInstance queryPipelineInstanceById(@Param("pipelineInstanceId") Long pipelineInstanceId);
	
	void insertPipelineInstance(OpsPipelineInstance pipelineInstance);
	
	void updatePipelineInstance(OpsPipelineInstance pipelineInstance);
	
	OpsAgentStepInstanceResult queryAgentStepInstanceResultByKeys(
			@Param("stepInstanceId") Long stepInstanceId, @Param("targetHost") String targetHost);
	void insertAgentStepInstanceResult(OpsAgentStepInstanceResult agentStepInstanceResult);
	void updateAgentStepInstanceResult(OpsAgentStepInstanceResult agentStepInstanceResult);
	
	List<OpsAgentStepInstanceResult> queryOpsStepAgentRunResultList(OpsAgentStepInstanceResult param);
	Integer queryOpsStepAgentRunResultTotalSize(OpsAgentStepInstanceResult param);
	
	void insertOpsPipelineRunJob(OpsPipelineRunJob runJob);
	void updateOpsPipelineRunJob(OpsPipelineRunJob runJob);
	void removeOpsPipelineRunJob(@Param("jobId") Long jobId);
	OpsPipelineRunJob queryOpsPipelineRunJobById(@Param("jobId") Long jobId);
	List<OpsPipelineRunJob> queryOpsPipelineRunJobList(OpsPipelineRunJobQueryModel queryParam);
	Integer queryOpsPipelineRunJobTotalSize(OpsPipelineRunJobQueryModel queryParam);

	OpsStep queryOpsStepById(Long stepId);

	List<OpsScript> queryOpsScriptListByPipelineId(@Param("pipelineId") Long pipelineId);

    Integer queryPipelineScenesCountByName(String scenesName);

	void insertPipelineScences(OpsPipelineScenes scenes);

	void updatePipelineScences(OpsPipelineScenes scenes);

	List<OpsPipelineScenes> queryPipelineScenesByIds(List<Long> objectIdList);

	void deletePipelineScenesByIds(@Param("scenesIds") String scenesIds);

    OpsPipelineScenes pipelineScenesById(Long pipelineScenesId);

	List<OpsPipelineScenes> queryPipelineScenesByPipelineId(@Param("pipelineId") Long pipelineId);

	void insertPipelineInstanceLog(OpsPipelineInstanceLog opsPipelineInstanceLog);

	void updatePipelineInstanceLog(OpsPipelineInstanceLog opsPipelineInstanceLog);

    OpsPipelineInstanceLog getPipelineInstanceLog(@Param("pipelineInstanceId") Long pipelineInstanceId);

    List<OpsParamType> loadAllParamTypeList();
    
    List<OpsParam> getParamList(OpsParamQueryModel opsParam);
    
    OpsParam queryOpsParamById(@Param("paramId") Long paramId);
    
    OpsParam queryOpsParamByCode(@Param("paramCode") String paramCode);
    
    void insertOpsParam(OpsParam param);
    
    void updateOpsParam(OpsParam param);
    
    Integer queryOpsParamReferCount(@Param("paramId") Long paramId); 
    
    void deleteOpsParamById(@Param("paramId") Long paramId); 

    List<OpsParamReference> queryReferParamListByEntityId(@Param("entityId") Long entityId);
    
    void cleanEntityParamRefers(@Param("entityId") Long entityId);
    
    void insertEntityParamRefer(OpsParamReference paramRefer);
    
    void batchInsertParamValue(List<OpsParamValue> paramValueList);

	void updateValidByHostAndStepInstId(OpsParamValue opsParamValue);

	List<OpsParamValueDetail> queryParamValueList(OpsParamValue queryParam);

	OpsParamValue queryLastValidParamValueByIpAndPipelineIdAndParam(@Param("pipelineId") Long pipelineId, @Param("agentIp")String ip, @Param("scriptParam") String scriptParam);

	void auditPipeline(OpsPipelineAuditInfo opsPipelineAuditInfo);

	void auditScript(OpsScriptAuditInfo opsPipelineAuditInfo);

    void insertOpsPipelineHis(OpsPipelineHis opsPipelineHis);

	void insertOpsScriptHis(OpsScriptHis opsScriptHis);

	void insertOpsStepHis(OpsStepHis opsPipelineHis);

	void updateOpsScriptHis(OpsScriptHis opsScriptHis);

	List<OpsPipelineHis> queryPipelineHisListByPipelineId(Long pipelineId);

	List<OpsScriptHis> queryScriptHisListByScriptId(Long scriptId);

	List<OpsStepHis> queryOpsStepHisListByPipelineHisId(@Param("pipelineHisId") Long pipelineHisId);

	OpsPipelineHis queryOpsPipelineHisById(@Param("pipelineHisId") Long pipelineHisId);

	OpsScriptHis queryOpsScriptHisById(@Param("scriptHisId") Long scriptHisId);

    List<String> queryUsernameListByAgentIp(@Param("agentIp") String agentIp);

	Integer getParamListTotalSize(OpsParamQueryModel paramModel);
}

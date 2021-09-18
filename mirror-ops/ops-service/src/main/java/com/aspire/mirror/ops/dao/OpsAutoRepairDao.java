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

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.ops.api.domain.autorepair.OpsApItemTypeDTO.OpsAutoRepairItemTypeQueryModel;
import com.aspire.mirror.ops.api.domain.autorepair.OpsApSchemePipelineDTO;
import com.aspire.mirror.ops.api.domain.autorepair.OpsAutoRepairExecuteLogDTO.OpsApExecHistoryQueryModel;
import com.aspire.mirror.ops.api.domain.autorepair.OpsAutoRepairSchemeDTO;
import com.aspire.mirror.ops.api.domain.autorepair.OpsAutoRepairSchemeDTO.OpsAutoRepairSchemeQueryModel;
import com.aspire.mirror.ops.domain.OpsApItem;
import com.aspire.mirror.ops.domain.OpsApItemType;
import com.aspire.mirror.ops.domain.OpsApSchemeItem;
import com.aspire.mirror.ops.domain.OpsAutoRepairExecuteLog;

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
public interface OpsAutoRepairDao {
	List<OpsApItemType> queryOpsAutoRepairItemTypeList(OpsAutoRepairItemTypeQueryModel queryParam);
	Integer queryOpsAutoRepairItemTypeTotalSize(OpsAutoRepairItemTypeQueryModel queryParam);
	OpsApItemType queryApItemTypeByUqKeys(@Param("sourceMark") String sourceMark, @Param("apItemType") String apItemType);
	void insertOpsApItemType(OpsApItemType apItemType);
	void updateOpsApItemType(OpsApItemType apItemType);
	void deleteApItemTypeByUqKeys(@Param("sourceMark") String sourceMark, @Param("apItemType") String apItemType);

	
	List<OpsApItem> queryOpsAutoRepairItemListByItemTypeId(@Param("apItemTypeId") Long apItemTypeId);
	OpsApItem queryOpsAutoRepairItemByUqKeys(@Param("sourceMark") String sourceMark, 
			@Param("apItemType") String apItemType, @Param("apItem") String apItem);
	List<OpsApItem> queryReferApItemListByItemTypeKeys(@Param("sourceMark") String sourceMark, @Param("apItemType") String apItemType);
	void insertOpsApItem(OpsApItem apItem);
	void updateOpsApItem(OpsApItem apItem);
	void deleteOpsApItemByUqKeys(@Param("apItemTypeId") Long apItemTypeId, @Param("apItem") String apItem);
	void deleteAllOpsApItemsByApTypeKeys(@Param("sourceMark") String sourceMark, @Param("apItemType") String apItemType);
	
	
	OpsAutoRepairSchemeDTO queryAutoRepairSchemeById(@Param("schemeId") Long schemeId);
	Integer queryAutoRepairSchemeCountByName(@Param("schemeName") String schemeName);
	OpsAutoRepairSchemeDTO queryAutoRepairSchemeByName(@Param("schemeName") String schemeName);
	void insertAutoRepairScheme(OpsAutoRepairSchemeDTO autoRepairScheme);
	void updateAutoRepairScheme(OpsAutoRepairSchemeDTO autoRepairScheme);
	void removeAutoRepairSchemeById(@Param("schemeId") Long schemeId);
	Integer queryAutoRepairSchemeTotalSize(OpsAutoRepairSchemeQueryModel queryParam);
	List<OpsAutoRepairSchemeDTO> queryAutoRepairSchemeList(OpsAutoRepairSchemeQueryModel queryParam);
	
	void insertOpsApSchemeItem(OpsApSchemeItem schemeItem);
	void updateOpsApSchemeItem(OpsApSchemeItem schemeItem);
	void deleteOpsApSchemeItemById(@Param("schemeItemId")Long schemeItemId);
	void deleteAllOpsApSchemeItemsBySchemeId(@Param("schemeId") Long schemeId);
	List<OpsApSchemeItem> queryReferItemListBySchemeId(@Param("schemeId") Long schemeId);
	OpsApSchemeItem querySchemeReferItemByUqKeys(@Param("apItemTypeId")Long apItemTypeId, @Param("apItem")String apItem);
	OpsApSchemeItem querySchemeReferItemBySourceItemType(
			@Param("sourceMark")String sourceMark, @Param("apItemType")String apItemType, @Param("apItem")String apItem);
	
	
	void insertOpsApSchemePipeline(OpsApSchemePipelineDTO schemePipeline);
	void updateOpsApSchemePipeline(OpsApSchemePipelineDTO schemePipeline);
	void deleteOpsApSchemePipelineById(@Param("schemePipelineId") Long schemePipelineId);
	void deleteAllOpsApSchemePipelineBySchemeId(@Param("schemeId") Long schemeId);
	List<OpsApSchemePipelineDTO> queryReferPipelineListBySchemeId(@Param("schemeId") Long schemeId);
	OpsApSchemePipelineDTO queryReferPipelineBySchemeIdAndOrder(
							@Param("schemeId") Long schemeId, @Param("currPipeOrd") Integer currPipeOrd);
	OpsApSchemePipelineDTO queryReferPipelineByUqKeys(@Param("schemeId") Long schemeId, @Param("pipelineId") Long pipelineId);
	
	
	void insertOpsAutoRepairExecuteResult(OpsAutoRepairExecuteLog autoRepairResult);
	void updateOpsAutoRepairExecuteResult(OpsAutoRepairExecuteLog autoRepairResult);
	OpsAutoRepairExecuteLog queryAutoRepairExecuteResultByPipelineInstanceId(@Param("pipelineInstanceId") Long pipelineInstanceId);
	OpsAutoRepairExecuteLog queryAutoRepairExecuteResultById(@Param("schemeExecLogId") Long schemeExecLogId);
	List<OpsAutoRepairExecuteLog> queryOpsAutoRepairExecHistoryList(OpsApExecHistoryQueryModel queryParam);
	Integer queryOpsAutoRepairExecHistoryListTotalSize(OpsApExecHistoryQueryModel queryParam);
}

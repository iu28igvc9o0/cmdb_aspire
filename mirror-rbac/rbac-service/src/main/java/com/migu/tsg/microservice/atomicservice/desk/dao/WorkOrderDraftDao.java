package com.migu.tsg.microservice.atomicservice.desk.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.migu.tsg.microservice.atomicservice.desk.dao.po.WorkOrderDraft;

/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.desk.dao<br>
* 类名称: WorkOrderDraftDao.java <br>
* 类描述: 服务台工单起草配置持久层<br>
* 创建人: Tongzhihong <br>
* 创建时间: 2020年9月16日下午4:09:21 <br>
* 版本: v1.0
*/
@Mapper
public interface WorkOrderDraftDao {

	List<WorkOrderDraft> getUserDraftList(@Param("account")String account);

	List<WorkOrderDraft> getDraftListByClassfyId(@Param("classifyId")int classifyId);

	void syncBpmDefIdByKey(@Param("bpmDefKey")String bpmDefKey, @Param("bpmDefId")String bpmDefId);
}

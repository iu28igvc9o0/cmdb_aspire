package com.aspire.webbas.epm.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.webbas.epm.core.config.ProcessConfig;
import com.aspire.webbas.epm.core.constant.EpmConstant;
import com.aspire.webbas.epm.core.dao.EpmProcessDao;
import com.aspire.webbas.epm.core.dao.EpmTaskDao;
import com.aspire.webbas.epm.core.entity.EpmProcess;
import com.aspire.webbas.epm.core.entity.EpmTask;
import com.aspire.webbas.epm.core.pojo.Choice;
import com.aspire.webbas.epm.core.pojo.Node;

/**
 * 流程查询类
 * @author wanglei
 *
 */
@Component
@Transactional
public class EpmProcessViewService {
	private static final Logger LOGGER = LoggerFactory.getLogger(EpmProcessViewService.class);

	/**
	 * 流程实例数据访问接口
	 */
	@Autowired
	private EpmProcessDao epmProcessDao;

	/**
	 * 任务数据访问接口
	 */
	@Autowired
	private EpmTaskDao epmTaskDao;

	/**
	 * 获取节点的decision信息（choice的value、name值）
	 * @param name 流程定义xml文件的流程定义名称<process name="epmProcess">
	 * @param status 节点状态值，如果是开始节点则传null
	 * @return 选择器的value和name对应的map
	 */
	public Map<String, String> getDecision(String name, String status) {
		Map<String, Object> m = ProcessConfig.getProcessDefinition(name);
		Node node = (Node) m.get(status);
		List<Choice> list = node.getChoiceList();
		//返回map
		Map<String, String> returnMap = new HashMap<String, String>();
		for (Choice c : list) {
			returnMap.put(c.getValue(), c.getName());
		}

		return returnMap;
	}

	/**
	 * 获取待处理任务（人）
	 * @param ownerType 流程定义xml文件的流程定义名称<process name="epmProcess">
	 * @param ownerId 业务ID
	 * @return 待处理任务列表
	 */
	public List<EpmTask> getPendTask(String ownerType, String ownerId) {
		EpmTask taskC = new EpmTask();
		taskC.setOwnerType(ownerType);
		taskC.setOwnerId(ownerId);
		taskC.setIsDeal(EpmConstant.NO);
		List<EpmTask> list = epmTaskDao.list(taskC);
		return list;
	}

	/**
	 * 任务列表查询
	 * @param epmTask 任务对象查询条件
	 * @return .
	 */
	public List<EpmTask> listTask(EpmTask epmTask) {
		List<EpmTask> list = epmTaskDao.list(epmTask);
		return list;
	}

	/**
	 * 任务数查询
	 * @param epmTask 任务对象查询条件
	 * @return 列表数
	 */
	public int countTask(EpmTask epmTask) {
		return epmTaskDao.count(epmTask);
	}

	/**
	 * 流程列表查询
	 * @param epmProcess 流程对象查询条件
	 * @return 流程列表
	 */
	public List<EpmProcess> listProcess(EpmProcess epmProcess) {
		List<EpmProcess> list = epmProcessDao.list(epmProcess);
		return list;
	}

	/**
	 * 流程列表查询
	 * @param epmProcess 流程对象查询条件
	 * @return 列表数
	 */
	public int countProcess(EpmProcess epmProcess) {
		return epmProcessDao.count(epmProcess);
	}

	/**
	 * 获取最后一个任务
	 * @param task 查询条件，主要属性为ownerType，ownerId
	 * @return 返回EpmTask对象
	 */
	public EpmTask getLastTask(EpmTask task) {
		return epmTaskDao.getLastTask(task);
	}

	/**
	 * 通过ID获取任务
	 * @param taskId 任务ID
	 * @return 返回EpmTask对象
	 */
	public EpmTask getTask(String taskId) {
		return epmTaskDao.get(taskId);
	}
}

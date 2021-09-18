package com.aspire.webbas.epm.core.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.webbas.epm.core.entity.EpmProcess;
import com.aspire.webbas.epm.core.entity.EpmTask;
import com.aspire.webbas.epm.core.pojo.DefineProcess;

/**
 * 流程引擎服务类 1、支持会签并或处理（通过处理配置参数，可动态控制下一环节会签并或处理） 2、支持流程并发交叉
 * 3、支持热插拔，流程变更不需要导入工作流操作 4、支持流程随意回退跳转（只需要修改数据库表状态即可） 5、支持流程环节流转拦截器处理
 * 6、支持自定义动态流程/环节流转 7、支持并发子流程打回到主/父流程节点
 * 相关问题解决 1、解决网络延迟页面连续多次点击处理/审批按钮 或
 * 同时打开相同页面进行处理/审批的情况，同一个节点进行重复流转到下一节点导致流程状态和下一节点不一致的问题
 * 2、解决多处理人同时处理同一节点，处理人与处理结果不好对应的问题
 * 3、解决并发子流程中，用户可以看到多条流程记录（实现方式：如果只有查看权限的用户只能看到主流程，有处理权限的用户可以查看到待处理的子流程记录）
 */

@Component
@Transactional
public class EpmProcessService {
	private static final Logger LOGGER = LoggerFactory.getLogger(EpmProcessService.class);

	/**
	 * 流程数据处理接口
	 */
	@Autowired
	private EpmProcessDealService epmProcessDealService;

	/**
	 * 流程数据查看接口
	 */
	@Autowired
	private EpmProcessViewService epmProcessViewService;

	/**
	 * 启动工作流
	 * @param name 流程名称，即流程定义xml中的process标签的name属性值
	 * @param ownerId 业务ID（工作流关联的业务模块的对象ID）
	 * @param process
	 *            流程对象参数（非必填，可传空值） role; //下一节点处理角色 viewer; //下一节点查看角色 choice;
	 *            //选择值 reviewer; //处理完成告知人 paramMap; //Map参数提供给拦截器使用
	 * @return List<EpmProcess> 下一环节流程对象（因为可能出现并发流程的情况，所以是列表）
	 */
	public List<EpmProcess> start(String name, String ownerId, EpmProcess process) throws Exception {
		return epmProcessDealService.start(name, ownerId, process);
	}

	/**
	 * @param name
	 *            流程名
	 * @param ownerId
	 *            业务ID
	 * @param process
	 *            流程参数
	 * @param defineProcess
	 *            自定义模版对象
	 * @return 流程列表
	 * @throws Exception
	 *             异常
	 */
	public List<EpmProcess> start(String name, String ownerId, EpmProcess process, DefineProcess defineProcess)
			throws Exception {
		return epmProcessDealService.start(name, ownerId, process, defineProcess);
	}

	/**
	 * 流程流转（流程从当前环节流转到下一环节）
	 * @param taskId
	 *            任务ID（流程流转时，会为每个环节创建任务，下一环节处理时需要传入该任务ID）
	 * @param process
	 *            流程对象参数（非必填，可传空值） role; //下一节点处理角色 viewer; //下一节点查看角色 choice;
	 *            //选择值 reviewer; //处理完成告知人 paramMap; //Map参数提供给拦截器使用
	 * @return List<EpmProcess> 下一环节流程对象（因为可能出现并发流程的情况，所以是列表）
	 */
	public List<EpmProcess> next(String taskId, EpmProcess process) throws Exception {
		return epmProcessDealService.next(taskId, process);
	}

	/**
	 * @param name 选择节点名
	 * @param status 状态
	 * @return map对象
	 */
	public Map<String, String> getDecision(String name, String status) {
		return epmProcessViewService.getDecision(name, status);
	}

	/**
	 * @param ownerType 业务类型
	 * @param ownerId 业务ID
	 * @return 任务列表
	 */
	public List<EpmTask> getPendTask(String ownerType, String ownerId) {
		return epmProcessViewService.getPendTask(ownerType, ownerId);
	}

	/**
	 * @param epmTask 任务对象
	 * @return 任务列表
	 */
	public List<EpmTask> listTask(EpmTask epmTask) {
		return epmProcessViewService.listTask(epmTask);
	}

	/**
	 * @param epmTask 任务对象
	 * @return 结果数
	 */
	public int countTask(EpmTask epmTask) {
		return epmProcessViewService.countTask(epmTask);
	}

	/**
	 * @param epmProcess 流程对象
	 * @return 流程列表
	 */
	public List<EpmProcess> listProcess(EpmProcess epmProcess) {
		return epmProcessViewService.listProcess(epmProcess);
	}

	/**
	 * @param epmProcess 流程对象
	 * @return 结果数
	 */
	public int countProcess(EpmProcess epmProcess) {
		return epmProcessViewService.countProcess(epmProcess);
	}

	/**
	 * 获取最后一个任务
	 * @param task 任务对象
	 * @return 任务对象
	 */
	public EpmTask getLastTask(EpmTask task) {
		return epmProcessViewService.getLastTask(task);
	}

	/**
	 * @param taskId 任务ID
	 * @return 任务对象
	 */
	public EpmTask getTask(String taskId) {
		return epmProcessViewService.getTask(taskId);
	}

}

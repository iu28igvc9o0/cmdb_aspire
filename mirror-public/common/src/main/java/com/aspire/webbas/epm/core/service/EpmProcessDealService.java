package com.aspire.webbas.epm.core.service;

import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
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
import com.aspire.webbas.epm.core.interceptor.NodeLeaveInterceptor;
import com.aspire.webbas.epm.core.pojo.Choice;
import com.aspire.webbas.epm.core.pojo.DefineProcess;
import com.aspire.webbas.epm.core.pojo.Interceptor;
import com.aspire.webbas.epm.core.pojo.Node;
import com.google.gson.Gson;

/**
 * @author wanglei
 */
@Component
@Transactional
public class EpmProcessDealService {
	private static final Logger LOGGER = LoggerFactory.getLogger(EpmProcessDealService.class);

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
	 * 启动工作流
	 * @param name
	 *            流程名称，即流程定义xml中的process标签的name属性值
	 * @param ownerId
	 *            业务ID（工作流关联的业务模块的对象ID）
	 * @param process
	 *            流程对象参数（非必填，可传空值） role; //下一节点处理角色 viewer; //下一节点查看角色 choice;
	 *            //选择值 reviewer; //处理完成告知人 paramMap; //Map参数提供给拦截器使用
	 * @return List<EpmProcess> 下一环节流程对象（因为可能出现并发流程的情况，所以是列表）
	 */
	public List<EpmProcess> start(String name, String ownerId, EpmProcess process) throws Exception {
		/* 异常处理 */
		if (StringUtils.isEmpty(name)) {
			throw new Exception("start方法入参name不能为空");
		}
		if (StringUtils.isEmpty(ownerId)) {
			throw new Exception("start方法入参ownerId不能为空");
		}

		/*//根据业务需要不做效验
		EpmProcess processC = new EpmProcess();
		processC.setName(name);
		processC.setOwnerId(ownerId);
		List<EpmProcess> list = epmProcessDao.list(processC);
		if (list != null && list.size() >= 1) {
			throw new Exception("该业务单流程已创建");
		}*/

		// 兼容性处理
		if (process == null) {
			process = new EpmProcess();
		}

		/* 获取流程定义相关配置 */
		Map<String, Object> map = ProcessConfig.getProcessDefinition(name);
		Node start = (Node) map.get("start");

		/* 创建流程 */
		EpmProcess mainProcess = new EpmProcess();
		// EPM2.0.0 改造，使用uuid实现新增
		// mainProcess.setProcessId(epmProcessDao.getNextId());
		mainProcess.setProcessId(UUID.randomUUID().toString());
		mainProcess.setCreateTime(new Date());

		mainProcess.setRootId(mainProcess.getProcessId());
		mainProcess.setName(name);
		mainProcess.setOwnerId(ownerId);
		mainProcess.setStatus(EpmConstant.STATUS_DEFAULT_VALUE);

		// 如果包含自定义流程，将该路程加入，并入库
		if (process.getDefineContent() != null) {
			mainProcess.setDefineContent(process.getDefineContent());
		}

		// 分支流向选择处理
		choose(mainProcess, start, process);

		epmProcessDao.add(mainProcess); // 新增流程

		List<EpmProcess> returnlist = getNextProcess(mainProcess);
		intercept(start, returnlist, process); // 调用拦截器
		return returnlist;
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
		if (defineProcess != null) {
			process.setDefineContent(defineProcess.toJson());
		}

		// 将流程定义放入到内存中
		if (defineProcess != null) {
			ProcessConfig.putProcess(name, defineProcess.getProcess());
			process.setDefineContent(defineProcess.toJson());
		}

		return this.start(name, ownerId, process);
	}

	/**
	 * 分支流向选择处理
	 * @param mainProcess 主流程
	 * @param node 当前节点对象
	 * @param process 传入流程参数
	 */
	private void choose(EpmProcess currProcess, Node node, EpmProcess process) {
		String choseValue = StringUtils.isNotEmpty(process.getChoice()) ? process.getChoice()
		: EpmConstant.CHOICE_DEFAULT_VALUE; //如果存在选择分支的情况，则根据choice值流转到下一个节点，如果不存在选择分支的情况，则根据to属性值流转到下一个节点（默认choice）
		List<Choice> choiceList = node.getChoiceList();
		for (Choice c : choiceList) { /* 判断是否为并发分支流程 */
			// 设置处理类型，以传入参数的为主，配置的为辅
			String dealType = StringUtils.isNotEmpty(process.getDealType()) ? process.getDealType() : c.getDealType();
			dealType = EpmConstant.DEAL_TYPE_OR.equals(dealType) ? EpmConstant.DEAL_TYPE_OR : EpmConstant.DEAL_TYPE_AND;
			// 设置处理角色，以传入参数的为主，配置的为辅
			String role = StringUtils.isNotEmpty(process.getRole()) ? process.getRole() : c.getRole();
			role = StringUtils.isNotEmpty(role) ? role : "null,"; // 单任务的时候将其变成多任务，只是处理角色只有一个
			// wanglei 20141110 添加待查看人，如果入参无viewer从choice中取
			String viewer = StringUtils.isEmpty(process.getViewer()) ? c.getViewer() : process.getViewer();
			viewer = StringUtils.isNotEmpty(viewer) ? viewer : "null,";
			if (choseValue.equals(c.getValue()) && EpmConstant.PROCESS_TYPE_FORK.equals(c.getProcessType())) { //并发分支的情况
				EpmProcess subProcess = new EpmProcess(); // 创建子流程
				subProcess.setProcessId(UUID.randomUUID().toString());
				subProcess.setCreateTime(new Date());
				subProcess.setParentId(currProcess.getProcessId());
				subProcess.setRootId(currProcess.getProcessId());
				subProcess.setName(currProcess.getName());
				subProcess.setOwnerId(currProcess.getOwnerId());
				subProcess.setPreStatus(currProcess.getStatus());
				subProcess.setStatus(c.getTo()); // 设置流程状态
				currProcess.setDealType(dealType); // 设置可选属性 多人处理类型，and或or处理,默认为and
				epmProcessDao.add(subProcess); // 新增子流程
				String[] roleArr = role.split(","); // 创建子任务
				String[] viewerArr = viewer.split(",");
				for (int i = 0; i < roleArr.length; i++) {
					EpmTask subTask = new EpmTask();
					subTask.setTaskId(UUID.randomUUID().toString());
					subTask.setCreateTime(new Date());
					subTask.setProcessId(subProcess.getProcessId());
					subTask.setParentId(currProcess.getProcessId());
					subTask.setRootId(currProcess.getRootId());
					subTask.setStatus(c.getTo());
					subTask.setOwnerType(subProcess.getName());
					subTask.setOwnerId(subProcess.getOwnerId());
					subTask.setRole(roleArr[i].equals("null") ? null : roleArr[i]);
					if (viewerArr.length > i) { // wanglei 20141110 添加待查看人字段，如果入参无viewer从choice中取
						subTask.setViewer(viewerArr[i].equals("null") ? null : viewerArr[i]);
					}
					String isMulti = roleArr.length > 1 ? EpmConstant.YES : EpmConstant.NO; // 是否为多个处理
					String isOr = EpmConstant.DEAL_TYPE_OR.equals(dealType) ? EpmConstant.YES : EpmConstant.NO; //是否为或处理
					subTask.setDealType(isMulti + isOr);
					epmTaskDao.add(subTask); // 新增子任务
				}
				continue;
			} // 下面为非并发分支，但有汇总的情况
			if (choseValue.equals(c.getValue()) && EpmConstant.PROCESS_TYPE_JOIN.equals(c.getProcessType())) {
				//1、当前流程父流程下面的子流程数目 2、如果有多个，则完成当前任务后，删除当前子流程实例 3、如果只有一个，则完成当前任务后，删除当前子流程实例，修改父流程实例状态，之后创建待处理任务
				String parentId = currProcess.getParentId(); // 1、当前流程父流程下面的子流程数目
				if (StringUtils.isNotEmpty(parentId)) {
					EpmProcess processC = new EpmProcess();
					processC.setParentId(parentId);
					int count = epmProcessDao.count(processC);
					EpmProcess parentProcess = epmProcessDao.get(parentId);
					if (count > 1) { // 2、如果有多个，则完成当前任务后，删除当前子流程实例
						epmProcessDao.del(currProcess.getProcessId());
						break;
					} else if (count == 1) { // 3、如果只有一个，删除当前子流程实例，修改父流程实例状态，之后创建待处理任务
						epmProcessDao.del(currProcess.getProcessId());
						currProcess.copyToSelf(parentProcess);
					}
				}
			}
			if (choseValue.equals(c.getValue())) { // 非并发分支，没有汇总的情况（start没有汇总的情况）
				currProcess.setPreStatus(currProcess.getStatus()); // 根据choice的value流转到对应的节点
				currProcess.setStatus(c.getTo());
				currProcess.setViewer(c.getViewer()); // 查看抄送对象
				currProcess.setDealType(dealType); //多人处理类型，and或or处理,默认为and
				Node end = (Node) ProcessConfig.getProcessDefinition(currProcess.getName()).get("end");
				if (end.getStatus().equals(c.getTo())) { /* 如果流转环节已经为end节点，则不创建子任务 */
					break;
				}
				String[] roleArr = role.split(","); // 创建子任务
				String[] viewerArr = viewer.split(",");
				for (int i = 0; i < roleArr.length; i++) {
					EpmTask subTask = new EpmTask();
					subTask.setTaskId(UUID.randomUUID().toString());
					subTask.setCreateTime(new Date());
					subTask.setProcessId(currProcess.getProcessId());
					subTask.setParentId(currProcess.getParentId());
					subTask.setRootId(currProcess.getRootId());
					subTask.setStatus(c.getTo());
					subTask.setOwnerType(currProcess.getName());
					subTask.setOwnerId(currProcess.getOwnerId());
					subTask.setRole(roleArr[i].equals("null") ? null : roleArr[i]);
					if (viewerArr.length > i) { // wanglei 20141110 添加待查看人字段，如果入参无viewer从choice中取
						subTask.setViewer(viewerArr[i].equals("null") ? null : viewerArr[i]);
					}
					String isMulti = roleArr.length > 1 ? EpmConstant.YES : EpmConstant.NO; // 是否为多个处理
					String isOr = EpmConstant.DEAL_TYPE_OR.equals(dealType) ? EpmConstant.YES : EpmConstant.NO; //是否为或处理
					subTask.setDealType(isMulti + isOr);
					epmTaskDao.add(subTask); // 新增子任务
				}
				break;
			}
		}
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
		/* 异常处理 */
		if (StringUtils.isEmpty(taskId)) {
			throw new Exception("next方法入参taskId不能为空");
		}

		if (process == null) {
			process = new EpmProcess();
		}

		/*
		 * 1、判断该环节是否已经处理 2、处理任务，如果多任务未全部执行则返回 3、处理任务，如果任务全部执行完成则向下执行 4、分支流向选择处理
		 * 5、存储流程信息
		 */
		// 1、判断该任务是否已经处理过了
		EpmTask task = epmTaskDao.get(taskId);
		if (task.getIsDeal().equals(EpmConstant.YES)) {
			throw new Exception("该环节已处理");
		}

		// 2、结束当前任务
		process.copyToTask(task);
		task.setDealTime(new Date());
		task.setIsDeal(EpmConstant.YES); // 将任务状态设置为已处理

		epmTaskDao.mod(task);
		// 如果为或处理，将其他任务一起结束，否则只结束当前任务
		if (task.getDealType().substring(1).equals(EpmConstant.YES)) {
			EpmTask taskMod = new EpmTask();
			taskMod.setOwnerId(task.getOwnerId());
			taskMod.setOwnerType(task.getOwnerType());
			taskMod.setStatus(task.getStatus());
			taskMod.setIsFinish(EpmConstant.NO);
			taskMod.setIsDeal(EpmConstant.NO);
			for (EpmTask t : epmTaskDao.list(taskMod)) {
				t.setDealTime(new Date());
				t.setIsDeal(EpmConstant.YES); // 将任务状态设置为已处理
				epmTaskDao.mod(t);
			}
		}

		// 如果多任务未全部执行则返回
		EpmTask taskC = new EpmTask();
		taskC.setProcessId(task.getProcessId());
		taskC.setStatus(task.getStatus());
		taskC.setIsDeal(EpmConstant.NO);
		if (epmTaskDao.count(taskC) > 0) {
			return null;
		}

		/* 任务所在环节的所有任务是都已经处理完成，则将任务完成装设置为已完成 */
		taskC = new EpmTask();
		taskC.setProcessId(task.getProcessId());
		taskC.setStatus(task.getStatus());
		taskC.setIsDeal(EpmConstant.YES);
		taskC.setIsFinish(EpmConstant.NO);
		for (EpmTask t : epmTaskDao.list(taskC)) {
			t.setIsFinish(EpmConstant.YES);
			epmTaskDao.mod(t);
		}

		// 3、处理任务，如果任务全部执行完成则向下执行
		// 获取当前流程信息
		EpmProcess currProcess = epmProcessDao.get(task.getProcessId());
		Map<String, Object> map = null;
		Node node = null;
		// 2017-8-17 如果是自定义流程则将流程定义加载到内存中
		String defineContent = currProcess.getDefineContent();
		if (defineContent != null && defineContent.length() > 0
				&& ProcessConfig.getProcessDefinition(currProcess.getName()) == null) {
			Gson g = new Gson();
			Map<String, Object> defineMap = g.fromJson(defineContent, Map.class);
			Set<Entry<String, Object>> s = defineMap.entrySet();
			Iterator<Entry<String, Object>> iterator = s.iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> en = iterator.next();
				defineMap.put(en.getKey(), g.fromJson(g.toJson(en.getValue()), Node.class));
			}

			ProcessConfig.putProcess(currProcess.getName(), defineMap);
		}

		// 获取当前节点配置
		map = ProcessConfig.getProcessDefinition(currProcess.getName());
		node = (Node) map.get(currProcess.getStatus());

		// 4、分支流向选择处理
		choose(currProcess, node, process);

		// 5、存储流程信息
		epmProcessDao.mod(currProcess); // 修改流程

		List<EpmProcess> returnlist = getNextProcess(currProcess);
		intercept(node, returnlist, process); // 调用拦截器

		return returnlist;
	}

	/**
	 * 获取下一待处理环节流程，并调用拦截器
	 * @param currProcess
	 * @return
	 */
	private List<EpmProcess> getNextProcess(EpmProcess currProcess) throws Exception {
		EpmProcess returnProcess = epmProcessDao.get(currProcess.getProcessId());

		if (returnProcess == null) { // 在有分支流程的情况下，当前分支流程结束，其他分支流程还存在的时候，当前分支流程将查询为空
			return null;
		}

		EpmTask taskC = new EpmTask();
		taskC.setProcessId(returnProcess.getProcessId());
		taskC.setIsDeal(EpmConstant.NO);
		returnProcess.setTaskList(epmTaskDao.list(taskC));

		EpmProcess processC = new EpmProcess();
		processC.setParentId(returnProcess.getProcessId());
		List<EpmProcess> returnProcessList = epmProcessDao.list(processC);
		if (returnProcessList != null && returnProcessList.size() > 0) {
			for (EpmProcess p : returnProcessList) {
				taskC = new EpmTask();
				taskC.setProcessId(p.getProcessId());
				taskC.setIsDeal(EpmConstant.NO);
				p.setTaskList(epmTaskDao.list(taskC));
			}
		} else {
			returnProcessList = new ArrayList<EpmProcess>();
			returnProcessList.add(returnProcess);
		}

		return returnProcessList;
	}

	/**
	 * 调用拦截器
	 * @param node
	 *            当前节点配置，通过该配置获取拦截器
	 * @param returnProcessList
	 */
	private void intercept(Node node, List<EpmProcess> returnProcessList, EpmProcess inputProcess) throws Exception {
		if (returnProcessList == null) { // 在有分支流程的情况下，当前分支流程结束，其他分支流程还存在的时候，当前分支流程将查询为空
			return;
		}

		for (EpmProcess p : returnProcessList) {
			// 获取拦截器相关配置信息
			List<Interceptor> l = node.getInterceptorList();
			for (Interceptor iterceptor : l) {
				Class c = Class.forName(iterceptor.getBean());
				NodeLeaveInterceptor leaveInterceptor = (NodeLeaveInterceptor) c.newInstance();
				// 调用拦截器
				iterceptor.getMap().put("choice", inputProcess.getChoice());

				// 用流程中的paramMap覆盖到配置文件中的param
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.putAll(iterceptor.getMap());
				// 使用传递的map值覆盖在流程配置文件中配置的参数值
				paramMap.putAll(inputProcess.getParamMap());

				leaveInterceptor.execute(p, paramMap);
			}
		}
	}

}

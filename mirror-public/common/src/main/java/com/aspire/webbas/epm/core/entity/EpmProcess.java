package com.aspire.webbas.epm.core.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程对象
 * 
 * @author wanglei
 */
public class EpmProcess {
    private String processId;
    private String name;
    private String parentId;
    private String rootId;
    private String status;
    private String dealType;

    private String preStatus;
    private Date createTime;
    private Date lastUpdateTime;
    private String ownerId;

    /** 额外添加属性 */
    private String role; // 下一节点处理角色
    private String viewer; // 下一节点查看角色
    private String choice; // 选择值
    private List<EpmTask> taskList; // 任务列表
    private String dealer; // 本环节处理人
    private String dealResult;
    private String remark;
    private String fileGroupId;
    private String reviewer; // 处理完成告知人
    private String extra1; // 扩展字段1
    private String extra2; // 扩展字段2
    private String extra3; // 扩展字段3
    private Map<String, Object> paramMap = new HashMap<String, Object>(); // Map参数提供给拦截器使用

    private String defineContent; // 自定义流程流程定义内容
    private String isDefine; // 查询条件

    private String isEnd;

    /**
     * @return .
     */
    public String getProcessId() {
        return processId;
    }

    /**
     * @param processId
     *            .
     */
    public void setProcessId(String processId) {
        this.processId = processId;
    }

    /**
     * @return .
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            .
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return .
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * @param parentId
     *            .
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     * @return .
     */
    public String getRootId() {
        return rootId;
    }

    /**
     * @param rootId
     *            .
     */
    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    /**
     * @return .
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     *            .
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return .
     */
    public String getDealType() {
        return dealType;
    }

    /**
     * @param dealType
     *            .
     */
    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    /**
     * @return .
     */
    public String getViewer() {
        return viewer;
    }

    /**
     * @param viewer
     *            .
     */
    public void setViewer(String viewer) {
        this.viewer = viewer;
    }

    /**
     * @return .
     */
    public String getPreStatus() {
        return preStatus;
    }

    /**
     * @param preStatus
     *            .
     */
    public void setPreStatus(String preStatus) {
        this.preStatus = preStatus;
    }


    /**
     * @return .
     */
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * @param ownerId
     *            .
     */
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * @return .
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role
     *            .
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return .
     */
    public String getChoice() {
        return choice;
    }

    /**
     * @param choice
     *            .
     */
    public void setChoice(String choice) {
        this.choice = choice;
    }

    /**
     * @return .
     */
    public List<EpmTask> getTaskList() {
        return taskList;
    }

    /**
     * @param taskList
     *            .
     */
    public void setTaskList(List<EpmTask> taskList) {
        this.taskList = taskList;
    }

    /**
     * @return .
     */
    public String getDealer() {
        return dealer;
    }

    /**
     * @param dealer
     *            .
     */
    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    /**
     * @return .
     */
    public String getDealResult() {
        return dealResult;
    }

    /**
     * @param dealResult
     *            .
     */
    public void setDealResult(String dealResult) {
        this.dealResult = dealResult;
    }

    /**
     * @return .
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     *            .
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return .
     */
    public String getFileGroupId() {
        return fileGroupId;
    }

    /**
     * @param fileGroupId
     *            .
     */
    public void setFileGroupId(String fileGroupId) {
        this.fileGroupId = fileGroupId;
    }

    /**
     * @return .
     */
    public String getReviewer() {
        return reviewer;
    }

    /**
     * @param reviewer
     *            .
     */
    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    /**
     * @return .
     */
    public String getExtra1() {
        return extra1;
    }

    /**
     * @param extra1
     *            .
     */
    public void setExtra1(String extra1) {
        this.extra1 = extra1;
    }

    /**
     * @return .
     */
    public String getExtra2() {
        return extra2;
    }

    /**
     * @param extra2
     *            .
     */
    public void setExtra2(String extra2) {
        this.extra2 = extra2;
    }

    /**
     * @return .
     */
    public String getExtra3() {
        return extra3;
    }

    /**
     * @param extra3
     *            .
     */
    public void setExtra3(String extra3) {
        this.extra3 = extra3;
    }

    /**
     * @return .
     */
    public String getIsEnd() {
        return isEnd;
    }

    /**
     * @param isEnd
     *            .
     */
    public void setIsEnd(String isEnd) {
        this.isEnd = isEnd;
    }

    /**
     * @return .
     */
    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    /**
     * @param paramMap
     *            .
     */
    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    /**
     * @param process
     *            .
     */
    public void copyToSelf(EpmProcess process) {
        this.setProcessId(process.getProcessId());
        this.setName(process.getName());
        this.setParentId(process.getParentId());
        this.setRootId(process.getRootId());
        this.setStatus(process.getStatus());
        this.setDealType(process.getDealType());
        this.setViewer(process.getViewer());
        this.setPreStatus(process.getPreStatus());
        this.setOwnerId(process.getOwnerId());
        this.setReviewer(process.getReviewer());
        this.setExtra1(process.getExtra1());
        this.setExtra2(process.getExtra2());
        this.setExtra3(process.getExtra3());
    }

    /**
     * @return .
     */
    public String getDefineContent() {
        return defineContent;
    }

    /**
     * @param defineContent
     *            .
     */
    public void setDefineContent(String defineContent) {
        this.defineContent = defineContent;
    }

    /**
     * @return .
     */
    public String getIsDefine() {
        return isDefine;
    }

    /**
     * @param isDefine
     *            .
     */
    public void setIsDefine(String isDefine) {
        this.isDefine = isDefine;
    }

    public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	/**
     * 将流程流转参数传递给task
     * 
     * @param task
     *            任务对象
     */
    public void copyToTask(EpmTask task) {
        task.setChoice(this.getChoice());
        task.setDealer(this.getDealer());
        task.setDealResult(this.getDealResult());
        task.setRemark(this.getRemark());
        task.setFileGroupId(this.getFileGroupId());
        task.setReviewer(this.getReviewer());
        task.setExtra1(this.getExtra1());
        task.setExtra2(this.getExtra2());
        task.setExtra3(this.getExtra3());
    }

}

package com.aspire.webbas.epm.core.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.aspire.webbas.epm.core.entity.EpmTask;

/**
 * 项目名称:  [webbas-component-epm]
 * 包名:     [com.aspire.webbas.epm.core.dao]
 * 类名称:    [Task]
 * 类描述:    [任务DAO]
 * 创建人:    [王磊]
 * 创建时间:  [2014年8月30日 下午6:45:27]
 */
@Repository
public interface EpmTaskDao {
	/**
	 * 列表查询
	 * @param task 任务对象
	 * @return 任务列表
	 */
	List<EpmTask> list(EpmTask task);

	/**
	 * 记录数查询
	 * @param task 任务对象
	 * @return 列表数
	 */
	int count(EpmTask task);

	/**
	 * 查看详情
	 * @param id id
	 * @return 任务对象
	 */
	EpmTask get(String id);

	/**
	 * 新增
	 * @param task 任务对象
	 * @return 新增数
	 */
	int add(EpmTask task);

	/**
	 * 修改
	 * @param task 任务对象
	 * @return 修改数
	 */
	int mod(EpmTask task);

	/**
	 * 删除
	 * @param id id
	 * @return 删除数
	 */
	int del(String id);

	/**
	 * 获取最后一个任务
	 * @param task 任务对象
	 * @return 任务对象
	 */
	EpmTask getLastTask(EpmTask task);
}

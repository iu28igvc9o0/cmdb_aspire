package com.aspire.webbas.epm.core.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.aspire.webbas.epm.core.entity.EpmProcess;

/**
 * 项目名称:  [webbas-component-epm]
 * 包名:      [com.aspire.webbas.epm.core.dao]
 * 类名称:    [ProcessDao]
 * 类描述:    [流程DAO]
 * 创建人:    [王磊]
 * 创建时间:  [2014年8月30日 下午6:44:01]
 */
@Repository
public interface EpmProcessDao {
	/**
	 * 列表查询
	 * @param process 流程对象
	 * @return 流程列表
	 */
	List<EpmProcess> list(EpmProcess process);

	/**
	 * 列表数
	 * @param process 流程对象
	 * @return 列表数
	 */
	int count(EpmProcess process);

	/**
	 * 详情
	 * @param id id
	 * @return 流程独享
	 */
	EpmProcess get(String id);

	/**
	 * 新增
	 * @param process 流程对象
	 * @return 插入数
	 */
	int add(EpmProcess process);

	/**
	 * 修改
	 * @param process 流程对象
	 * @return 修改数
	 */
	int mod(EpmProcess process);

	/**
	 * 删除
	 * @param id id
	 * @return 删除数
	 */
	int del(String id);
}

package com.aspire.mirror.log.server.util;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页工具类
 * 项目名称: 微服务运维平台（log-service 模块）
 * 包: com.aspire.mirror.log.server.util
 * 类名称: Pagenation.java
 * 类描述: 分页工具类
 * 创建人: sunke
 * 创建时间: 2017年8月28日 下午4:18:19
 */
@NoArgsConstructor
@Data
public class Pagenation {

	// 传参或指定
	private int currentPage;
	// 每页显示记录数
	private int pageSize;
	// 总记录数
	private int rowCount;
	// 总页数
	private int pageCount;
	// 当前页面开始行
	private int startRow;
	// 首页
	private int first = 1;
	// 最后一页
	private int last;
	// 下一页
	private int next;
	// 上一页
	private int prev;
	/**
	 * 默认每页记录数
	 */
	private static final int DEFAULT_SIZE = 10;

	/**
	 * 构造器
	 * @param size 每页记录数
	 * @param page 当前页数
	 * @param rowCount 总条数
	 */
	public Pagenation(int size, int page, int rowCount) {
		if (page <=  0) {
		    page = 1;
		}
		this.pageSize = DEFAULT_SIZE;
		if (size > 0) {
		    pageSize = size;
		}
		this.rowCount = rowCount;
		this.pageCount =  rowCount / pageSize;
		this.currentPage = 1;
		if (page > pageCount) {
		    currentPage = pageCount;
		} else {
		    currentPage = page;
		}
		this.currentPage = Math.min(this.currentPage, pageCount);
		this.currentPage = Math.max(1, this.currentPage);

		this.startRow = (this.currentPage - 1) * size;
		this.last = this.pageCount;
		this.next = Math.min(this.pageCount, this.currentPage + 1);
		this.prev = Math.max(1, this.currentPage - 1);
	}

}

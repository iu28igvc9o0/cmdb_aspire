package com.migu.tsg.microservice.atomicservice.ldap.util;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 项目名称: ld-service <br>
* 包: com.migu.tsg.microservice.atomicservice.ldap.util <br>
* 类名称: Pagenation.java <br>
* 类描述: <br>
* 创建人: WangSheng <br>
* 创建时间: 2017年11月8日下午12:19:08 <br>
* 版本: v1.0
*/
@NoArgsConstructor
@Data
public class Pagenation {

    /**
     * 传参或指定
     */
    private int currentPage;

    /**
     * 每页显示记录数
     */
    private int pageSize;

    /**
     * 总记录数
     */
    private int rowCount;

    /**
     * 总页数
     */
    private int pageCount;

    /**
     * 当前页面开始行
     */
    private int startRow;

    /**
     * 首页
     */
    private int first = 1;

    /**
     * 最后一页
     */
    private int last;

    /**
     * 下一页
     */
    private int next;

    /**
     * 上一页
     */
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
        if (page <= 0) {
            page = 1;
        }
        this.pageSize = DEFAULT_SIZE;
        if (size > 0) {
            pageSize = size;
        }
        this.rowCount = rowCount;
        this.pageCount = (rowCount - 1) / pageSize + 1;
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

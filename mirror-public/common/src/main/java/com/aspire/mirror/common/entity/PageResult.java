package com.aspire.mirror.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 公用page返回对象
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.common.entity
 * 类名称:    PageResponse.java
 * 类描述:    公用page返回对象
 * 创建人:    JinSu
 * 创建时间:  2018/7/28 15:41
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
public class PageResult<T> {

    /**
     * 总数
     */
    private int count;
    /**
     * 总页数
     */
    private int pageCount;
    /**
     * 当前页
     */
    private int curPage;

    /**
     * 页面记录数
     */
    private int pageSize;
    /**
     * 列表对象
     */
    private List<T> result;
}

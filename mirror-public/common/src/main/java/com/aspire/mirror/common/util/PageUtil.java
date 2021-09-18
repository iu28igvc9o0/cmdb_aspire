package com.aspire.mirror.common.util;

import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * 分页工具
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.common.entity
 * 类名称:    PageUtil.java
 * 类描述:    分页工具类
 * 创建人:    WuFan
 * 创建时间:  2017/08/23 19:42
 * 版本:      v1.0
 */
public class PageUtil {

    /**
     * 将分页请求转换成列表适配器
     * <p>分页请求必须存在，且当前页数和每页条数必须设置</p>
     *
     * @param pageRequest 分页请求，不允许为<code>null</code>
     * @param <T> 数据对象
     * @param clazz clazz
     * @return 列表适配器
     */
    public static final <T> Page convert(final PageRequest pageRequest, final T clazz) {
        Page page = convert(pageRequest);

        Map<String, Object> paramsMap = page.getParams();
        paramsMap.putAll(BeanUtil.toMap(clazz));
        page.setParams(paramsMap);
        return page;
    }

    /**
     * 将分页请求转换成列表适配器
     * <p>分页请求必须存在，且当前页数和每页条数必须设置</p>
     *
     * @param page 分页请求，不允许为<code>null</code>
     * @param <T> 数据对象
     * @param clazz clazz
     * @return 列表适配器
     */
    public static final <T> Page convert(final Page page, final T clazz) {
        Map<String, Object> paramsMap = page.getParams();
        paramsMap.putAll(BeanUtil.toMap(clazz));
        page.setParams(paramsMap);
        return page;
    }

    /**
     * 将分页请求转换成列表适配器
     * <p>分页请求必须存在，且当前页数和每页条数必须设置</p>
     *
     * @param pageRequest 分页请求，不允许为<code>null</code>
     * @return 列表适配器
     */
    public static final Page convert(final PageRequest pageRequest) {
        Page page = new Page();

        if (!StringUtils.isEmpty(pageRequest.getOrder())) {
            page.setOrder(pageRequest.getOrder());
        }
        if (!StringUtils.isEmpty(pageRequest.getSortName())) {
            page.setSortName(pageRequest.getSortName());
        }
        if (!StringUtils.isEmpty(pageRequest.getAssembleOrderBy())) {
            page.setAssembleOrderBy(pageRequest.getAssembleOrderBy());
        }
        page.setPageNo(pageRequest.getPageNo());
        page.setPageSize(pageRequest.getPageSize());

        Map<String, Object> dynamicQueryFields = pageRequest.getDynamicQueryFields();
        if (!dynamicQueryFields.isEmpty()) {
            page.setParams(dynamicQueryFields);
        }

        return page;
    }

    /**
     * 分页对象
     * @param pageNo 页码
     * @param pageSize 每页数据
     * @return Page 分页对象
     */
    public static final Page convert(final Integer pageNo, final Integer pageSize) {
        Page page = new Page();

        if (null != pageNo) {
            page.setPageNo(pageNo);
        }
        if (null != pageSize) {
            page.setPageSize(pageSize);
        }

        return page;
    }

}
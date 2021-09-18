package com.aspire.ums.cmdb.collect.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: BaseMapper
 * Author:   zhu.juwang
 * Date:     2019/4/1 14:44
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface BaseMapper<T> {
    /**
     * 根据主键ID获取对象
     * @param id 主键ID
     * @return
     */
    T get(@Param("id") String id);

    /**
     * 新增对象
     * @param vo 对象
     */
    void insertVO(@Param("vo") T vo);

    /**
     * 修改对象
     * @param vo 对象
     */
    void updateVO(@Param("vo") T vo);

    /**
     * 删除对象
     * @param vo 对象
     */
    void deleteVO(@Param("vo") T vo);
}

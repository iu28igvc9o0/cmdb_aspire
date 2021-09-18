package com.aspire.mirror.theme.server.dao;

import org.apache.ibatis.annotations.Mapper;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.server.dao
 * 类名称:    LogThemeFlushTimeDao.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/6/26 20:41
 * 版本:      v1.0
 */
@Mapper
public interface LogThemeFlushTimeDao {
    /**
     * 获取刷新时间
     * @return
     */
    String getFlushTime();

    /**
     * 设置刷新时间
     */
    void setFlushTime(String flushTime);
}

package com.aspire.ums.cmdb.sqlManage.mapper;

import com.aspire.ums.cmdb.sqlManage.CmdbSqlManage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: SqlManageMapper
 * Author:   zhu.juwang
 * Date:     2020/6/22 10:50
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Mapper
public interface SqlManageMapper {
    /**
     * 根据统计接口名称 获取SQL信息
     * @param managerName 接口名称
     * @return
     */
    CmdbSqlManage getByName(@Param("name") String managerName);
}

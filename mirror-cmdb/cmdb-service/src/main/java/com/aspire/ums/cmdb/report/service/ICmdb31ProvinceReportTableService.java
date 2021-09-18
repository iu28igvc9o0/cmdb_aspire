package com.aspire.ums.cmdb.report.service;

import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceTable;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICmdb31ProvinceReportTableService
 * Author:   hangfang
 * Date:     2020/4/20
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface ICmdb31ProvinceReportTableService {

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<Cmdb31ProvinceTable> listByOwnerAndPage(String resourceOwner, String showPage);

    /**
     * 根据表id获取详情
     * @param id 实例信息
     * @return 返回实例信息的数据信息
     */
    Cmdb31ProvinceTable getById(String id, String showPage);

    /**
     * 根据归属和表名获取表
     * @param name 实例信息
     * @return 返回实例信息的数据信息
     */
    Cmdb31ProvinceTable getByName(String name, String showPage);

    /**
     * 获取表头
     * @return 返回所有实例数据
     */
    Map<String, Object> getTitlesByParams(Map<String, String> queryParams);
}

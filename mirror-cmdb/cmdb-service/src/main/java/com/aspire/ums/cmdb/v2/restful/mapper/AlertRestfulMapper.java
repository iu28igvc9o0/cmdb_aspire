package com.aspire.ums.cmdb.v2.restful.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: AlertRestfulMapper
 * Author:   zhu.juwang
 * Date:     2019/12/10 16:40
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Mapper
public interface AlertRestfulMapper {
    /**
     * 根据资源池统计设备数量
     * @param params 请求参数
     */
    List<Map<String, Object>> statisticsDeviceByIdcType(Map<String, Object> params);

    /**
     * 根据一级部门统计设备数量
     * @param params 请求参数
     */
    List<Map<String, Object>> statisticsDeviceByDepartment1(Map<String, Object> params);

    /**
     * 根据业务系统统计设备数量
     * @param params 请求参数
     */
    List<Map<String, Object>> statisticsDeviceByBizSystem(@Param("params") Map<String, Object> params, 
    		@Param("resFilterMap") Map<String, List<String>> resFilterMap);

    /**
     * 统计一级部门的设备数量, 提供给资源利用率接口
     */
    List<Map<String, Object>> statisticsDepartment1ForAvailability();

    /**
     * 统计二级部门的设备数量, 提供给资源利用率接口
     * @param department1 一级部门名称
     */
    List<Map<String, Object>> statisticsDepartment2ForAvailability(@Param("department1") String department1);
}

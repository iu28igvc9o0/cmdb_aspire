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
public interface BPMRestfulMapper {
    /**
     * 根据资源池统计设备数量
     * @param params 请求参数
     */
    List<Map<String, Object>> queryBizSystemQuote(Map<String, Object> params);

    /**
     * 根据系统账号获取业务系统列表
     * @param account 系统账号
     * @return
     */
    List<Map<String, Object>> getBizSystemListByAccount(@Param("account") String account,
                                                        @Param("bizSystem") String bizSystem,
                                                        @Param("currentPage") int currentPage,
                                                        @Param("pageSize") int pageSize);

    /**
     * 根据系统账号获取业务系统列表
     * @param account 系统账号
     * @return
     */
    int getBizSystemListByAccountCount(@Param("account") String account, @Param("bizSystem") String bizSystem);

    /**
     * 根据组织结构的部门ID, 获取对应CMDB部门下的所有业务系统
     * @param deptIdSet
     * @return
     */
    List<Map<String, Object>> getBizSystemListByOrgDepartmentIds(@Param("deptList") List<String> deptIdSet,
                                                                 @Param("bizSystem") String bizSystem,
                                                                 @Param("currentPage") int currentPage,
                                                                 @Param("pageSize") int pageSize);

    /**
     * 根据组织结构的部门ID, 获取对应CMDB部门下的所有业务系统
     * @param deptIdSet
     * @return
     */
    int getBizSystemListByOrgDepartmentIdsCount(@Param("deptList") List<String> deptIdSet,@Param("bizSystem") String bizSystem);
}

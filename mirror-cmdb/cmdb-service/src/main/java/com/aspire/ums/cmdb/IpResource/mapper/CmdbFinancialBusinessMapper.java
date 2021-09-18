package com.aspire.ums.cmdb.IpResource.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/6/16 20:39
 */
public interface CmdbFinancialBusinessMapper {

    /**
     * 获取财务业务线
     *
     * @return
     */
    List<Map<String, Object>> getFinanciaList(@Param("moduleId") String moduleId);

    /**
     * 根据ID查询财务业务线
     *
     * @param financiaId
     * @return
     */
    Map<String, Object> getFinanciaById(@Param("financiaId") String financiaId);

    /**
     * 级联获取独立业务线
     *
     * @param financiaId
     * @return
     */
    List<Map<String, Object>> getBusinessByFinanciaId(@Param("financiaId") String financiaId);

}

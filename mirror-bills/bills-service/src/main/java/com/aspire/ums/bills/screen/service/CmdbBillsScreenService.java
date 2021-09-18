package com.aspire.ums.bills.screen.service;


import com.aspire.ums.bills.screen.payload.ScreenResponse;

import java.util.List;
import java.util.Map;

/**
 * @projectName: CmdbBillsScreenService
 * @description: 接口
 * @author: luowenbo
 * @create: 2020-08-05 19:49
 **/
public interface CmdbBillsScreenService {

    /**
     *  查询该租户下资源类型的月账单统计
     * @param chargeTime 月报时间
     * @param department 部门
     * @param idcType   资源池
     * @param resourceType  资源类型
     * @return
     */
    ScreenResponse<Map<String,Object>> getMonthBillsWithResourceType(String chargeTime,
                                                                     String department,
                                                                     String idcType,
                                                                     String resourceType);

    /**
     *  查询业务系统-资源池的配额和总价
     * @param chargeTime 月报时间
     * @param department 部门
     * @return
     */
    ScreenResponse<List<Map<String, Object>>> getQuotaAndBillsWithBiz(String chargeTime,String department);

    /**
     * 查询业务系统-资源池 具体设备的配额和总价
     * @param chargeTime 月报时间
     * @param department 部门
     * @param bizSystem 业务系统
     * @param idcType   资源池
     * @param resourceType  资源类型
     * @return
     */
    ScreenResponse<List<Map<String,Object>>> getQuotaAndBillsWithResourceType(String chargeTime,
                                                                        String department,
                                                                        String bizSystem,
                                                                        String idcType,
                                                                        String resourceType);

    /**
     * 统计一年的缴费情况
     * @param year
     * @return
     */
    ScreenResponse<Map<String,Object>> getAccountRevenueRecord(String year,String department);

    /**
     *  获取月单价
     * @param bizSystem
     * @param department
     * @param idcType
     * @return
     */
    ScreenResponse<List<Map<String,Object>>> getUnitPrice(String bizSystem,String department,String idcType);

    /**
     *  获取租户账号余额信息
     * @param department
     * @param monthEnd
     * @return
     */
    ScreenResponse<Map<String,Object>> getAccountInfo(String department,String monthEnd);

    /**
     *  下载账单明细
     * @param department
     * @param chargeTime
     * @return
     */
    List<Map<String,Object>> exportBillsDetail(String department,String chargeTime);

    /**
     *  租户PDF账单数据
     * @param department
     * @param chargeTime
     * @return
     */
    List<Map<String,Object>> getSpecificBillsWithResourceType(String department,String chargeTime);
}

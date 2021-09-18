package com.aspire.ums.bills.calculate.service;

import com.aspire.mirror.common.entity.PageResult;
import com.aspire.mirror.common.entity.Result;
import com.aspire.ums.bills.calculate.payload.CmdbBillsMonthBill;
import com.aspire.ums.bills.calculate.payload.CmdbBillsMonthRecord;
import com.aspire.ums.bills.calculate.payload.CmdbBillsMonthRecordRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @projectName: CmdbBillsMonthRecordService
 * @description: 类
 * @author: luowenbo
 * @create: 2020-08-03 15:37
 **/
public interface CmdbBillsMonthRecordService {

    /**
     * 批量新增月账单
     * @param recordList
     */
    void batchInsert(List<CmdbBillsMonthRecord> recordList);

    void insert(CmdbBillsMonthRecord cmdbBillsMonthRecord);

    void delete(String chargeTime);

    /**
     * 更新月账单
     * @param record
     * @return Map<String,Object>
     */
    Map<String,Object> update(CmdbBillsMonthRecord record);

    /**
     * 月账单列表
     * @param chargeTime
     * @return
     */
    List<Map<String,Object>> listBillsWithMonth(String chargeTime);

    List<Map<String,Object>> listMonthBillsWithDepartment(String type,String department,String chargeTime);

    /**
     某个租户缴费明细列表
      */
    Map<String, Object> listMonthBillsByDepartment(String department, String chargeTime);

    Result updateRealPay(CmdbBillsMonthRecord record);

    /**
     * 冲销功能，新增月账单价格
     *
     * @param monthBill 冲销信息
     * @return
     */
    Map<String, Object> insertBillMonth(CmdbBillsMonthBill monthBill);

    /**
     * 冲销功能，更改月账单价格
     * @param monthBill 冲销信息
     * @return
     */
    Map<String, Object> updateBillMonth(CmdbBillsMonthBill monthBill);

    void deleteMonthBill(String chargeTime);

    void insertMonthBill(CmdbBillsMonthBill monthBill);
}

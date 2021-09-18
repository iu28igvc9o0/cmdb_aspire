package com.aspire.ums.bills.calculate.service;

import com.aspire.ums.bills.calculate.payload.CmdbBillsDayRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @projectName: CmdbBillsDayRecordService
 * @description: 接口
 * @author: luowenbo
 * @create: 2020-08-03 15:42
 **/
public interface CmdbBillsDayRecordService {

    /**
     * 日账单新增
     * @param record
     */
    void insert(CmdbBillsDayRecord record);

    /**
     * 日账单批量新增
     * @param list
     */
    void batchInsert(List<CmdbBillsDayRecord> list);

    /**
     * 依据月份，统计日账单，生成月账单
     * @param chargeTime
     * @return
     */
    List<CmdbBillsDayRecord> getDayBillsRecordWithMonth(String chargeTime);
}

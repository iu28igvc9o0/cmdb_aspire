package com.aspire.ums.bills.calculate.mapper;

import com.aspire.ums.bills.calculate.payload.CmdbBillsMonthBill;
import com.aspire.ums.bills.calculate.payload.CmdbBillsMonthRecord;
import com.aspire.ums.bills.calculate.payload.CmdbBillsMonthRecordRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CmdbBillsMonthRecordMapper {

    int batchInsert(List<CmdbBillsMonthRecord> list);

    int deleteByPrimaryKey(String id);

    int delete(@Param("chargeTime") String chargeTime);

    int deleteMonthBill(@Param("chargeTime") String chargeTime);

    int insert(CmdbBillsMonthRecord record);

    CmdbBillsMonthRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CmdbBillsMonthRecord record);

    int updateByPrimaryKey(CmdbBillsMonthRecord record);

    int updateByCombinationUnique(CmdbBillsMonthRecordRequest req);

    List<Map<String,Object>> listBillsWithMonth(@Param("chargeTime") String chargeTime,
                                                        @Param("tableName") String tableName);

    List<Map<String,Object>> listMonthBillsWithDepartment(@Param("type") String type,
                                                          @Param("innerDepartmentFlag") String department,
                                                          @Param("chargeTime") String chargeTime,
                                                          @Param("tableName") String tableName);

    List<Map<String,Object>> listMonthBillsByDepartment(@Param("departmentId") String departmentId,
                                                        @Param("chargeTime") String chargeTime,
                                                        @Param("tableName") String tableName);

    int updateRealPay(CmdbBillsMonthRecord record);

    CmdbBillsMonthRecord listBillsRealPayWithUnique(@Param("department") String department,
                                                    @Param("chargeTime") String chargeTime);

    void updateBillMonth(CmdbBillsMonthBill monthBill);

    void insertBillMonth(CmdbBillsMonthBill monthBill);
}
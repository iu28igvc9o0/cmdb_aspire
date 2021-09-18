package com.aspire.ums.bills.screen.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author wangyihan
 * @date 2020-08-14 19:21
 */
@Repository
public interface CmdbBillsOperationScreenMapper {

    List<Map<String, String>> getDeptTypeList(@Param("tablePrefix") String tablePrefix);

    List<Map<String, String>> getTotalExpense(@Param("chargeTime") String chargeTime,
                                              @Param("tablePrefix") String tablePrefix);

    List<Map<String, String>> getResourcesMonthBills(@Param("idcId") String idcId,
                                                     @Param("chargeTime") String chargeTime,
                                                     @Param("deptTypeId") String deptTypeId,
                                                     @Param("tablePrefix") String tablePrefix);

    List<Map<String, String>> getMonthBills(@Param("deptTypeId") String deptTypeId,
                                            @Param("chargeTime") String chargeTime,
                                            @Param("tablePrefix") String tablePrefix);

}

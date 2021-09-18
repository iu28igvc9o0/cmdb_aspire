package com.aspire.ums.bills.discount.mapper;

import com.aspire.ums.bills.discount.payload.CmdbBillsDiscount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author wangyihan
 * @date 2020-08-04 10:13
 */
@Repository
public interface CmdbBillsDiscountMapper {

    List<Map<String, String>> queryDeptTreeData(@Param("tablePrefix") String tablePrefix);

    List<Map<String, String>> getPoolTreeData(@Param("tablePrefix") String tablePrefix);

    List<Map<String, String>> getResourceTreeData(@Param("tablePrefix") String tablePrefix);

    List<Map<String, String>> getBusinessList(@Param("tablePrefix") String tablePrefix,
                                              @Param("dept1") String dept1,
                                              @Param("dept2") String dept2);

    List<Map<String, Object>> getDiscountList(@Param("type") String type);
    Map<String, Object> getDiscountById(@Param("tablePrefix") String tablePrefix, @Param("id") String id);

    Integer commitDiscount(@Param("list") List<CmdbBillsDiscount> discountList);

    List<CmdbBillsDiscount> queryDiscounts();

    Map<String, Object> queryDiscount(@Param("idList") List<String> idList);

}

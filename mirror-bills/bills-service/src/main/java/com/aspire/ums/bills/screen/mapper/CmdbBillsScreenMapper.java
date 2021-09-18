package com.aspire.ums.bills.screen.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @projectName: CmdbBillsScreenMapper
 * @description: 接口
 * @author: luowenbo
 * @create: 2020-08-05 19:41
 **/
@Mapper
public interface CmdbBillsScreenMapper {

    List<Map<String,Object>> getMonthBillsWithResourceType(@Param("chargeTime") String chargeTime,
                                                           @Param("department") String department,
                                                           @Param("idcType") String idcType,
                                                           @Param("resourceType") String resourceType,
                                                           @Param("tableName") String tableName);

    List<Map<String,Object>> getQuotaAndBillsWithBiz(@Param("chargeTime") String chargeTime,
                                                     @Param("department") String department,
                                                     @Param("tableName") String tableName);

    Map<String,Object> getQuotaWithBiz(@Param("bizSystem") String bizSystem,
                                       @Param("idcType") String idcType,
                                       @Param("department") String department,
                                       @Param("tableName") String tableName);

    List<Map<String,Object>> getQuotaAndBillsWithResourceType(@Param("chargeTime") String chargeTime,
                                                        @Param("department") String department,
                                                        @Param("bizSystem") String bizSystem,
                                                        @Param("idcType") String idcType,
                                                        @Param("resourceType") String resourceType,
                                                        @Param("tableName") String tableName);

    List<Map<String,Object>> getAccountRevenueRecord(@Param("year") String year,
                                                     @Param("department") String department);

    List<Map<String,Object>> getUnitPrice(@Param("bizSystem") String bizSystem,
                                          @Param("department") String department,
                                          @Param("idcType") String idcType);

    Map<String,Object> getAccountInfo(@Param("department") String department,
                                      @Param("monthBegin") String monthBegin,
                                      @Param("monthEnd") String monthEnd,
                                      @Param("tableName") String tableName);

    List<Map<String,Object>> getAllQuota(@Param("department") String department,
                                         @Param("tableName") String tableName);

    List<Map<String,Object>> getAllPrice(@Param("tableName") String tableName);

    List<Map<String,Object>> getAllMonthBills(@Param("department") String department,
                                              @Param("chargeTime") String chargeTime,
                                              @Param("tableName") String tableName);

    List<Map<String,Object>> getSpecificBillsWithResourceType(@Param("department") String department,
                                                              @Param("chargeTime") String chargeTime,
                                                              @Param("resourceType") String resourceType,
                                                              @Param("tableName") String tableName);
}

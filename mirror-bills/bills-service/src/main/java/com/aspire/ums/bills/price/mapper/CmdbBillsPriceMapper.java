package com.aspire.ums.bills.price.mapper;

import com.aspire.ums.bills.price.payload.CmdbBillsPriceRequest;
import com.aspire.ums.bills.price.payload.CmdbBillsPriceResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author wangyihan
 * @date 2020-08-04 11:29
 */
@Mapper
@Repository
public interface CmdbBillsPriceMapper {

    List<Map<String, String>> queryIdcList(@Param("tablePrefix") String tablePrefix);

    List<Map<String, String>> queryDeviceTypeList(@Param("tablePrefix") String tablePrefix);

    Integer insertPrice(@Param("domain") CmdbBillsPriceRequest price);

    List<CmdbBillsPriceResponse> queryPriceByCondition(@Param("idcId") String idcId,
                                                       @Param("deviceTypeId") String deviceTypeId,
                                                       @Param("tablePrefix") String tablePrefix);

    CmdbBillsPriceResponse queryPriceDetailById(@Param("id") String id,
                                                @Param("tablePrefix") String tablePrefix);
    Integer updatePrice(@Param("domain") CmdbBillsPriceRequest price);

    Integer deletePrice(@Param("id") String id);

    List<CmdbBillsPriceResponse> queryPriceList(@Param("tablePrefix") String tablePrefix);

    List<Map<String, String>> getPriceUnitList(@Param("tablePrefix") String tablePrefix);

    List<Map<String, String>> getDevicePriceList(@Param("idcId") String idcId,
                                                 @Param("tablePrefix") String tablePrefix);
}

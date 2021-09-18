package com.aspire.ums.cmdb.resource.mapper;

import com.aspire.ums.cmdb.resource.entity.CmdbBusiness;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CmdbBusinessMapper {

	List<CmdbBusiness> getBusinessByPId(@Param(value = "pId") String pId);

	List<CmdbBusiness> getAllBusinesses();

	List<Map<String, Object>> getBrand();

	List<Object> findAlarmIsHostedType(@Param(value = "deviceIp") String deviceIp);

	List<Object> findBiz2ByBiz1(@Param(value = "biz1") String biz1);

	List<Map<String, String>> findContactByBiz(@Param(value = "business_code1") String business_code1 ,@Param(value = "business_code2") String business_code2 );

	List<Map<String, String>> findBusinessCodeAndName(@Param(value = "id") List<String> id);

	List<Map<String, String>> getFirstBusiness(@Param(value = "id") List<String> id);

	List<Map<String, String>> findAlarmByDiffValue(@Param(value = "ip") List<String> ips, @Param(value = "codes") List<String> codes);

	List<Map<String, String>> getBussCodeByMoniObject(@Param(value = "moniObject") String moniObject);

}

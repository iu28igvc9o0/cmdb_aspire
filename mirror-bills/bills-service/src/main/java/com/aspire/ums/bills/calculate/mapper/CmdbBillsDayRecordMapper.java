package com.aspire.ums.bills.calculate.mapper;


import com.aspire.ums.bills.calculate.payload.CmdbBillsDayRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CmdbBillsDayRecordMapper {

    int deleteByPrimaryKey(String id);

    int batchInsert(List<CmdbBillsDayRecord> list);

    int insert(CmdbBillsDayRecord record);

    CmdbBillsDayRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CmdbBillsDayRecord record);

    int updateByPrimaryKey(CmdbBillsDayRecord record);

    List<CmdbBillsDayRecord> getDayBillsRecordWithMonth(@Param("chargeTime") String chargeTime);
}
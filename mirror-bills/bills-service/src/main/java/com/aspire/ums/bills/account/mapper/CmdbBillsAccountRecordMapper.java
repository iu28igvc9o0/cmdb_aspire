package com.aspire.ums.bills.account.mapper;

import com.aspire.ums.bills.account.payload.CmdbBillsAccountRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CmdbBillsAccountRecordMapper {

    List<CmdbBillsAccountRecord> getAccountRecord(@Param("account") String account,
                                                  @Param("dealMonth") String dealMonth,
                                                  @Param("dealType") String dealType);

    int deleteByPrimaryKey(String id);

    int insert(CmdbBillsAccountRecord record);

    CmdbBillsAccountRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CmdbBillsAccountRecord record);

    int updateByPrimaryKey(CmdbBillsAccountRecord record);
}
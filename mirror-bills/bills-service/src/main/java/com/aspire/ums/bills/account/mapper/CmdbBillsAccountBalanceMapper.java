package com.aspire.ums.bills.account.mapper;

import com.aspire.ums.bills.account.payload.CmdbBillsAccountBalance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CmdbBillsAccountBalanceMapper {

    int deleteByPrimaryKey(String id);

    int insert(CmdbBillsAccountBalance record);

    CmdbBillsAccountBalance selectByPrimaryKey(@Param("tableName") String tableName,@Param("id")String id);

    int updateByPrimaryKeySelective(CmdbBillsAccountBalance record);

    int updateByPrimaryKey(CmdbBillsAccountBalance record);

    CmdbBillsAccountBalance selectByDepartment(@Param("tableName") String tableName,
                                               @Param("department") String department);

    String selectNextNumber();

}
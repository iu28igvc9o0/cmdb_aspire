package com.aspire.ums.cmdb.cmic.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.ums.cmdb.cmic.entity.CmdbOperationLog;

@Mapper
public interface CmdbOperationLogMapper {

	String findMenuNameByUrl(@Param("menuUrl") String menuUrl);

	void addOperationLog(CmdbOperationLog cmdbOperationLog);

}

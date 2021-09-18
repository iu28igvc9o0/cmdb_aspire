package com.aspire.cdn.esdatawrap.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TestDao {
	public Map<String, Object> testQuery(@Param("id") String id);
}

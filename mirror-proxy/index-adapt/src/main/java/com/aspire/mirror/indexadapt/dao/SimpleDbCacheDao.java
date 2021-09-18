package com.aspire.mirror.indexadapt.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SimpleDbCacheDao {
	public String getCacheValByKey(@Param(value = "cacheKey") String cacheKey);
	public void removeCacheKey(@Param(value = "cacheKey") String cacheKey);
	public void putCacheEntry(@Param(value = "cacheKey") String cacheKey, @Param(value = "cacheValue") String cacheVal);
}

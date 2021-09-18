package com.aspire.mirror.indexadapt.helper;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aspire.mirror.indexadapt.dao.SimpleDbCacheDao;

/**
* 简单缓存帮助类    <br/>
* Project Name:index-proxy
* File Name:SimpleCacheHelper.java
* Package Name:com.aspire.mirror.indexproxy.helper
* ClassName: SimpleCacheHelper <br/>
* date: 2019年7月1日 下午5:27:15 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Component
public class SimpleCacheHelper {
	@Autowired(required = false)
	private RedissonClient		redissonClient;

	@Autowired(required = false)
	private SimpleDbCacheDao	dbCacheDao;

	/**
	* 获取缓存值. <br/>
	*
	* 作者： pengguihua
	* @param cacheKey
	* @return
	*/  
	public String getValueByKey(String cacheKey) {
		if (redissonClient != null) {
			RBucket<String> bucket = redissonClient.getBucket(cacheKey);
			if (!bucket.isExists()) {
				return null;
			}
			return bucket.get();
		}
		if (dbCacheDao != null) {
			return dbCacheDao.getCacheValByKey(cacheKey);
		}
		return null;
	}
	
	public void putCacheEntry(String cacheKey, String cacheVal) {
		if (redissonClient != null) {
			RBucket<String> bucket = redissonClient.getBucket(cacheKey);
			bucket.set(cacheVal);
			return;
		}
		if (dbCacheDao != null) {
			dbCacheDao.putCacheEntry(cacheKey, cacheVal);
			return;
		}
	}
	
	/**
	* 移除缓存. <br/>
	*
	* 作者： pengguihua
	* @param cacheKey
	*/  
	public void removeCacheKey(String cacheKey) {
		if (redissonClient != null) {
			RBucket<String> bucket = redissonClient.getBucket(cacheKey);
			bucket.delete();
			return;
		}
		if (dbCacheDao != null) {
			dbCacheDao.removeCacheKey(cacheKey);
		}
	}
}

package com.aspire.mirror.indexadapt.helper;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
* 分布式锁帮助类    <br/>
* Project Name:index-proxy
* File Name:DistributeLockHelper.java
* Package Name:com.aspire.mirror.indexproxy.helper
* ClassName: DistributeLockHelper <br/>
* date: 2019年7月1日 下午4:52:51 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Component
public final class DistributeLockHelper {
	@Autowired(required=false)
	private RedissonClient redissonClient;
	
	public Lock getLock(String lockName) {
		if (redissonClient != null) {
			return redissonClient.getLock(lockName);
		}
		return new NoLockImpl();
	}
	
	/**
	* 此类只是一个空壳对象, 只是作为代码占位使用，只能在单实例单线程中使用  <br/>
	* Project Name:index-proxy
	* File Name:DistributeLockHelper.java
	* Package Name:com.aspire.mirror.indexproxy.helper
	* ClassName: NoLockImpl <br/>
	* date: 2019年7月1日 下午5:02:34 <br/>
	* @author pengguihua
	* @version DistributeLockHelper
	* @since JDK 1.6
	*/ 
	private static class NoLockImpl implements Lock {
		@Override
		public void lock() {
			// do nothing 
		}

		@Override
		public void lockInterruptibly() throws InterruptedException {
			// do nothing 
		}

		@Override
		public boolean tryLock() {
			return true;
		}

		@Override
		public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
			return true;
		}

		@Override
		public void unlock() {
			// do nothing 
		}

		@Override
		public Condition newCondition() {
			return null;
		}
	}
}

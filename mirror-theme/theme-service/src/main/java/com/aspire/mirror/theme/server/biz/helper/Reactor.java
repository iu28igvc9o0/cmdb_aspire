/**
 * 
 */
package com.aspire.mirror.theme.server.biz.helper;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lupeng
 *
 */
public class Reactor {

	private static Reactor reactor = null;

	private final ExecutorService service;

	private Reactor() {
		service = new ThreadPoolExecutor(20, 400, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(10000),
				new ThreadFactory() {
					private AtomicInteger inc = new AtomicInteger(1);

					@Override
					public Thread newThread(Runnable r) {
						return new Thread(r, "thread_pool_auto_etl_" + inc.getAndIncrement());
					}
				}, new ThreadPoolExecutor.DiscardPolicy());
	}

	public static synchronized Reactor getInstance() {
		if (reactor == null) {
			reactor = new Reactor();
		}
		return reactor;
	}

	public void submit(Runnable task) {
		service.submit(task);
	}

	public Future<String> submit(Callable<String> task) {
		return service.submit(task);
	}

	public void shutdown() {
		service.shutdown();
	}

	public void execute(Runnable task) {
		service.execute(task);
	}
}

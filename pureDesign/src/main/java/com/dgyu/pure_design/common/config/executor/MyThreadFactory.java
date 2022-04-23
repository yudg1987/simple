package com.dgyu.pure_design.common.config.executor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author dgyu
 *
 * @version 1.0
 *
 */
public class MyThreadFactory implements ThreadFactory
{
	private final ThreadGroup group;
	private final AtomicInteger threadNumber = new AtomicInteger(1);
	private final String namePrefix;

	public MyThreadFactory(String para)
	{
		SecurityManager s = System.getSecurityManager();
		group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		namePrefix = para + "-pool-thread-";
	}


	@Override
	public Thread newThread(Runnable r)
	{
		Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
		if (t.isDaemon())
		{
			t.setDaemon(false);
		}
		if (t.getPriority() != Thread.NORM_PRIORITY)
		{
			t.setPriority(Thread.NORM_PRIORITY);
		}
		return t;
	}
}

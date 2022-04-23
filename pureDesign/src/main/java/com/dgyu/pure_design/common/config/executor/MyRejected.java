package com.dgyu.pure_design.common.config.executor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * @Auther: dgyu
 * @Date: 2020/2/26
 * @Description:
 */
public class MyRejected implements RejectedExecutionHandler
{

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor)
	{
		System.out.println("自定义拒绝策略.....");
		System.out.println("当前被拒绝的任务------" + Thread.currentThread().getName() + ":" + r.toString());
	}
}

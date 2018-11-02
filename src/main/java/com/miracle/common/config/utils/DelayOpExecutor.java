package com.miracle.common.config.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DelayOpExecutor {

	private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	private DelayOpExecutor()
	{}
	
	private static DelayOpExecutor instance = new DelayOpExecutor();
	
	public static DelayOpExecutor getInstance()
	{
		return instance;
	}
	
	public void submitDelayJob(Runnable command, int delayInSecond)
	{
		executor.schedule(command, delayInSecond, TimeUnit.SECONDS);
	}
}

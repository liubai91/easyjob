package com.easyjob.util;

import org.quartz.JobExecutionContext;

public class ThreadLocalAccessUtils {
	
	private static ThreadLocal<JobExecutionContext> jobContext = new ThreadLocal<JobExecutionContext>();
	private static ThreadLocal<Throwable> jobException = new ThreadLocal<Throwable>();

	public static void setJobContextForCurrentThread(JobExecutionContext context){
		jobContext.set(context);
	}
	
	public static JobExecutionContext getJobContextForCurrentThread(){
		return jobContext.get();
	}

	/*
	 * 因为线程复用，所以让异常只能被取一次，保证各job间异常不错乱
	 */
	public static Throwable getJobException() {
		Throwable exception = jobException.get();
		jobException.set(null);
		return exception;
	}

	public static void setJobException(Throwable jobException) {
		ThreadLocalAccessUtils.jobException.set(jobException);
	}

}

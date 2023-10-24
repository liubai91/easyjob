package com.easyjob.base;


import com.easyjob.core.TaskManager;
import com.easyjob.entity.JobLog;
import com.easyjob.service.IJobLogPersist;
import com.easyjob.util.JobLogUtils;
import com.easyjob.util.ThreadLocalAccessUtils;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerListener;
import org.springframework.util.MethodInvoker;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class TaskListener implements TriggerListener {
	
	private IJobLogPersist service;
	private Map<String,JobLog> logs = new ConcurrentHashMap<String,JobLog>();
	
	public TaskListener(IJobLogPersist service) {
		this.service = service;
	}

	@Override
	public String getName() {
		return "taskStatusListener";
	}

	@Override
	public void triggerFired(Trigger trigger, JobExecutionContext context) {
		String key = context.getFireInstanceId();
		JobLog log = new JobLog();
		log.setName(trigger.getJobKey().getName());
		log.setStartTime(System.currentTimeMillis());
		logs.put(key, log);
		String fireInstanceId = context.getFireInstanceId();
		Thread thread = Thread.currentThread();
		Map<String, Thread> threads = TaskManager.getThreads();
		threads.put(fireInstanceId,thread);
	}

	@Override
	public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
		return false;
	}

	@Override
	public void triggerMisfired(Trigger trigger) {
		
	}

	@Override
	public void triggerComplete(Trigger trigger, JobExecutionContext context, CompletedExecutionInstruction triggerInstructionCode) {
		Throwable exception = ThreadLocalAccessUtils.getJobException();
		JobLog log = logs.get(context.getFireInstanceId());
		long endtime = System.currentTimeMillis();
		if(log==null){
			return;
		}
		if(exception==null){
			log.setState("正常");
		}else{
			log.setState("异常");
			StringWriter errors = new StringWriter();
			exception.printStackTrace(new PrintWriter(errors));
			log.setExceptionDetail(errors.toString());
			log.setExceptionBrief(exception.getCause().getMessage());
		}
		log.setEndTime(endtime);
		log.setDuration(endtime-log.getStartTime());
		log.setLog(JobLogUtils.getLog());
		logs.remove(context.getFireInstanceId());
		if(service!=null){
			service.persistJobLog(log);
		}

		String fireInstanceId = context.getFireInstanceId();
		Map<String, Thread> threads = TaskManager.getThreads();
		threads.remove(fireInstanceId);
		MethodInvoker methodInvoker = (MethodInvoker) context.getMergedJobDataMap().get("methodInvoker");
		Class<?> targetClass = methodInvoker.getTargetClass();
		String targetMethod = methodInvoker.getTargetMethod();
	}
	
	

}

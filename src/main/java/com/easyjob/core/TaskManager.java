package com.easyjob.core;


import com.easyjob.base.MethodInvokingJobDetailLogSupportFactoryBean;
import com.easyjob.base.TaskListener;
import com.easyjob.entity.Job;
import com.easyjob.entity.JobLog;
import com.easyjob.entity.JobType;
import com.easyjob.entity.TaskObjectInfo;
import com.easyjob.service.IJobLogPersist;
import com.easyjob.service.ISchedulerInitialStatus;
import com.easyjob.service.impl.JobLogPersistImpl;
import com.easyjob.service.impl.SchedulerInitialStatusImpl;
import com.easyjob.util.MethodArgumentUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.quartz.TriggerBuilder.newTrigger;


public class TaskManager implements ApplicationContextAware,ApplicationListener<ContextRefreshedEvent>{


	private IJobLogPersist logPersistService;
	private ISchedulerInitialStatus schedulerService;
	private Scheduler scheduler;
	private ApplicationContext beanRegisty;
	protected static Map<String, TaskObjectInfo> eligibleTasks = new HashMap<String,TaskObjectInfo>();
	private static final Map<String,Thread> threads = new ConcurrentHashMap<>();

	public TaskManager() {
	}

	//@PostConstruct
	public void initScheduler(){
		boolean enable = true;
		if(schedulerService!=null){
			enable = schedulerService.getSchedulerState();
		}
		if(enable){
			startScheduler();
		}
	}
	
	public boolean startScheduler() {
		try {
			if (isSchedulerRunning()) {
				return true;
			}
			
			SchedulerFactory sf = new StdSchedulerFactory();

			scheduler = sf.getScheduler();
			scheduler.start();
			scheduler.getListenerManager().addTriggerListener(new TaskListener(logPersistService));
			
			List<Job> jobs = null;
			if(schedulerService!=null){
				jobs = schedulerService.getJobsToSchedule();
			}
			if(jobs!=null){
				for(Job job : jobs){
					scheduleJob(job);
				}
			}
		} catch (SchedulerException e) {
			stopScheduler();
			return false;
		}
		return true;
	}
	
	public boolean stopScheduler() {
		if (scheduler == null) {
			return true;
		}
		try {
			for (Thread thread : threads.values()) {
				thread.stop();
			}
			if (!scheduler.isShutdown()) {
				scheduler.shutdown();
			}
		} catch (SchedulerException e) {
		} finally {
			scheduler = null;
		}
		return true;
	}
	
	public boolean isSchedulerRunning() {
		boolean isRunning = false;
		if(scheduler!=null){
			try {
				isRunning = scheduler.isStarted();
			} catch (SchedulerException e) {
				isRunning = false;
				stopScheduler();
			}
		}
		return isRunning;
	}

	public boolean isJobExists(Job task){
		boolean isExist = false;
		try {
			JobDetail job = scheduler.getJobDetail(new JobKey(task.getName()));
			if(job!=null){
				isExist = true;
			}
		} catch (SchedulerException e) {
		}
		return isExist;
	}
	
	/**
	 * @param task 调度方法
	 * @return 当方法返回true时，表示task处于调度状态
	 */
	public boolean scheduleJob(Job task) {
		if(task==null||!isSchedulerRunning()){
			return false;
		}
		//已经存在的时候，方法直接返回
		if(isJobExists(task)){
			return true;
		}
		
		return doScheduleJob(task);
	}

	private boolean doScheduleJob(Job task) {
		try {
			MethodInvokingJobDetailLogSupportFactoryBean jdfb = new MethodInvokingJobDetailLogSupportFactoryBean();
			jdfb.setTargetObject(beanRegisty.getBean(task.getTargetObject()));
			jdfb.setTargetMethod(task.getTargetMethod());
			jdfb.setName(task.getName());
			Object[] arguments = MethodArgumentUtils.generateMethodParams(task);
			jdfb.setArguments(arguments);
			jdfb.afterPropertiesSet();
			JobDetail jd = jdfb.getObject();
			JobDataMap jobdata = new JobDataMap();
			jobdata.put("strategy", task.getStrategy());
			JobType jobType = task.getTriggerType();
			jobType = jobType == null ? JobType.RIGHTNOW : jobType;
			TriggerBuilder builder = newTrigger().withIdentity(task.getName(), "lian-med").usingJobData(jobdata);
			switch(jobType){
			case RIGHTNOW:
				builder.startNow();
				break;
			case REGULAR:
				builder.withSchedule(CronScheduleBuilder.cronSchedule(task.getCronExpression()));
				break;
			case DELAY:
				builder.startAt(task.getFireTime());
				break;
			}
			Trigger trigger = builder.build();
			scheduler.scheduleJob(jd, trigger);
		} catch (Exception e) {
			if(logPersistService!=null){
				JobLog log = new JobLog();
				log.setName(task.getName());
				log.setStartTime(System.currentTimeMillis());
				log.setEndTime(System.currentTimeMillis());
				log.setDuration(0L);
				log.setState("异常");
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				log.setExceptionDetail(errors.toString());
				log.setExceptionBrief(e.getCause()==null?"":e.getCause().getMessage());
				logPersistService.persistJobLog(log);
			}
			return false;
		}
		return true;
	}

	
	
	/**
	 * @param task 调度方法
	 * @return 当方法返回true时，表示task从调度中心删除成功
	 */
	public boolean unscheduleJob(Job task) {
		try {
			//存在的时候，才需要停止调度
			if(isJobExists(task)){
				scheduler.deleteJob(new JobKey(task.getName()));
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public List<Map<String,Object>> getRunningJobs(){
		List<Map<String,Object>> jobs = new ArrayList<Map<String,Object>>();
		if(scheduler==null){
			return jobs;
		}
		try {
			List<JobExecutionContext> runningjobs = scheduler.getCurrentlyExecutingJobs();
			for(JobExecutionContext job : runningjobs){
				Map<String,Object> item = new HashMap<String,Object>();
				item.put("name", job.getJobDetail().getKey().getName());
				item.put("startTime", job.getFireTime());
				jobs.add(item);
			}
		} catch (SchedulerException e) {
		}
		return jobs;
	}

	public Map<String,String> getScheduledJobs() {
		Map<String,String> jobs = new HashMap<String,String>();
		if(!isSchedulerRunning()){
			return jobs;
		}
		try {
			for (String groupName : scheduler.getJobGroupNames()) {
				for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
					List<? extends Trigger> triggers  = scheduler.getTriggersOfJob(jobKey);  
					for(Trigger trigger : triggers){
						Trigger.TriggerState state = scheduler.getTriggerState(trigger.getKey());
						if(Trigger.TriggerState.NORMAL.equals(state)){
							String jobName = jobKey.getName();
							String jobGroup = jobKey.getGroup();
							jobs.put(jobName, jobGroup);
						}else if(Trigger.TriggerState.COMPLETE.equals(state)){
							scheduler.deleteJob(jobKey);
						}
					}
				}
			}
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jobs;
	}

	public static Map<String, TaskObjectInfo> getEligibleTasks() {
		return eligibleTasks;
	}

	public static Map<String, Thread> getThreads() {
		return threads;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		beanRegisty = applicationContext;
		DataSource dataSource = applicationContext.getBean(DataSource.class);
		logPersistService = new JobLogPersistImpl(dataSource);
		schedulerService = new SchedulerInitialStatusImpl(dataSource);
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(event.getApplicationContext().getParent() == null){
			initScheduler();
		}
	}

}

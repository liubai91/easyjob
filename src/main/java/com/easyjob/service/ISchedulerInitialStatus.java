package com.easyjob.service;



import com.easyjob.entity.Job;

import java.util.List;

/**
 * implement this interface,give you a chance that you can control whether or not launch 
 * Scheduler when application starting.
 * if you don't have an bean of this interface in application context,default scheduler launch 
 * @author nhl220
 *
 */
public interface ISchedulerInitialStatus {

	/**
	 * scheduler will be launch when returned value is true
	 * @return
	 */
	boolean getSchedulerState();
	
	/**
	 * when scheduler launch,these jobs will be schedule
	 * @return
	 */
	List<Job> getJobsToSchedule();
	
}

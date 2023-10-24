package com.easyjob.service;


import com.easyjob.entity.JobLog;

/**
 * implement this interface,give you chance to persit job execution log
 * @author nhl220
 *
 */
public interface IJobLogPersist {

	boolean persistJobLog(JobLog jobLog);

}

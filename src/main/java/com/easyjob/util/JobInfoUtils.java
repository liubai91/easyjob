package com.easyjob.util;

import com.easyjob.dto.JobInfo;
import com.easyjob.entity.Job;
import com.easyjob.entity.JobExceptionStrategy;
import com.easyjob.entity.JobType;

public abstract class JobInfoUtils {


    public static Job convertTo(JobInfo jobInfo) {
        if(jobInfo!=null) {
            Job job = new Job();
            job.setName(jobInfo.getName());
            job.setDescription(jobInfo.getDescription());
            job.setTriggerType(JobType.valueOf(jobInfo.getTriggerType()));
            job.setStrategy(JobExceptionStrategy.valueOf(jobInfo.getStrategy()));
            job.setTargetObject(jobInfo.getTargetObject());
            job.setTargetMethod(jobInfo.getTargetMethod());
            job.setTargetParams(jobInfo.getTargetParams());
            job.setCronExpression(jobInfo.getCronExpression());
            job.setFireTime(jobInfo.getFireTime());
            return job;
        }
        return  null;
    }

}

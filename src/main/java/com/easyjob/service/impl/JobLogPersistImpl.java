package com.easyjob.service.impl;

import com.easyjob.dao.JobLogDao;
import com.easyjob.entity.JobLog;
import com.easyjob.service.IJobLogPersist;

import javax.sql.DataSource;

public class JobLogPersistImpl implements IJobLogPersist {

    private JobLogDao jobLogDao;

    public JobLogPersistImpl(DataSource dataSource) {
        this.jobLogDao = new JobLogDao(dataSource);
    }

    @Override
    public boolean persistJobLog(JobLog jobLog) {

        try {

            int updateRowCount = jobLogDao.insert(jobLog);
            return updateRowCount>0 ? true : false;


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

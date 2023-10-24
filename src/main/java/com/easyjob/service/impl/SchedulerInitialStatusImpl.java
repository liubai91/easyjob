package com.easyjob.service.impl;

import com.easyjob.dao.JobInfoDao;
import com.easyjob.dto.JobCriteria;
import com.easyjob.dto.JobInfo;
import com.easyjob.entity.Job;
import com.easyjob.service.ISchedulerInitialStatus;
import com.easyjob.util.JobInfoUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SchedulerInitialStatusImpl implements ISchedulerInitialStatus {


    private DataSource dataSource;

    public SchedulerInitialStatusImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean getSchedulerState() {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        Integer status = 0 ;
        try {
            status = queryRunner.query("select status from easyjob_scheduler limit 1", new ResultSetHandler<Integer>() {
                @Override
                public Integer handle(ResultSet resultSet) throws SQLException {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    }
                    return 0;
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return status>0?true:false;
    }

    @Override
    public List<Job> getJobsToSchedule() {
        JobInfoDao jobInfoDao = new JobInfoDao(dataSource);
        JobCriteria criteria = new JobCriteria();
        criteria.setEnable(true);
        List<JobInfo> jobInfos = new ArrayList<>();
        try {
            jobInfos = jobInfoDao.selectList(criteria);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        List<Job> result = jobInfos.stream().map(JobInfoUtils::convertTo).collect(Collectors.toList());
        return result;
    }
}

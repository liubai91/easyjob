package com.easyjob.dao;

import com.easyjob.dto.JobCriteria;
import com.easyjob.dto.Page;
import com.easyjob.entity.JobLog;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class JobLogDao {

    private static final String INSERT_SQL = "insert into easyjob_joblog(name,start_time,end_time,duration,state,luna_log,exception_brief,exception_detail) value (?,?,?,?,?,?,?,?)";

    private static final String QUERY_SQL = "select id,name,start_time startTime,end_time endTime,duration,state,luna_log log,exception_brief exceptionBrief,exception_detail exceptionDetail from easyjob_joblog ";

    private static final String COUNT_SQL = "select count(1) from easyjob_joblog ";

    private static final String DELETE_SQL = "delete from easyjob_joblog where id = ?";


    private DataSource dataSource;

    private QueryRunner queryRunner;

    public JobLogDao(DataSource dataSource) {
        this.dataSource = dataSource;
        this.queryRunner = new QueryRunner(dataSource);
    }

    public int insert(JobLog jobLog) throws SQLException {

        return queryRunner.update(INSERT_SQL
                ,jobLog.getName(),jobLog.getStartTime(),jobLog.getEndTime(),jobLog.getDuration()
                ,jobLog.getState(),jobLog.getLog(),jobLog.getExceptionBrief(),jobLog.getExceptionDetail());

    }

    public Page<JobLog> selectPage(JobCriteria criteria) throws SQLException {
        StringBuffer datasql = new StringBuffer(QUERY_SQL);
        StringBuffer countsql = new StringBuffer(COUNT_SQL);
        cancatWhereConditon(criteria, datasql);
        cancatWhereConditon(criteria, countsql);

        datasql.append(" order by startTime desc");
        concatPage(criteria, datasql);
        Long count = queryRunner.query(countsql.toString(), new ResultSetHandler<Long>() {
            @Override
            public Long handle(ResultSet resultSet) throws SQLException {
                resultSet.next();
                return resultSet.getLong(1);
            }
        });

        List<JobLog> result = queryRunner.query(datasql.toString(), new BeanListHandler<JobLog>(JobLog.class,new BasicRowProcessor()));
        Page page = new Page();
        page.setCount(count);
        page.setData(result);

        return page;
    }

    private void concatPage(JobCriteria criteria, StringBuffer datasql) {
        if(criteria.getPageCur()==null||criteria.getPageSize()==null) {
            return;
        }
        datasql.append(" limit ").append((criteria.getPageCur()-1)* criteria.getPageSize())
                .append(",").append(criteria.getPageSize());
    }

    private void cancatWhereConditon(JobCriteria criteria, StringBuffer sql) {
        if((criteria.getName()==null||criteria.getName().trim().length()==0)&&
                (criteria.getState()==null||criteria.getState().trim().length()==0)) {
            return;
        }
        sql.append(" where ");
        boolean conditionNeadAnd = false;
        if(criteria.getName()!=null && criteria.getName().trim().length()>0) {
            if(conditionNeadAnd) {
                sql.append(" and ");
            }
            sql.append(" name like '%").append(criteria.getName()).append("%' ");
            conditionNeadAnd = true;
        }
        if(criteria.getState()!=null && criteria.getState().trim().length()>0) {
            if(conditionNeadAnd) {
                sql.append(" and ");
            }
            sql.append(" state like '%").append(criteria.getState()).append("%' ");
            conditionNeadAnd = true;
        }
    }
}

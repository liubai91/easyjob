package com.easyjob.dao;

import com.easyjob.dto.JobCriteria;
import com.easyjob.dto.JobInfo;
import com.easyjob.dto.Page;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JobInfoDao {

    private static final String INSERT_SQL = "insert into easyjob_job(name,description,trigger_type,target_object,target_method,target_params,target_class,cron_expression,fire_time,strategy,luna_enable) value (?,?,?,?,?,?,?,?,?,?,?)";

    private static final String UPDATE_SQL = "update easyjob_job set name=? ,description=? , trigger_type=? ,target_object=? ,target_method=? ,target_params=? ,target_class=?, cron_expression=?, fire_time=?, strategy=?, luna_enable=? where id = ?";

    private static final String QUERY_SQL = "select id,name,description, trigger_type triggerType,target_object targetObject, target_method targetMethod, target_params targetParams, target_class targetClass, cron_expression cronExpression, fire_time fireTime, luna_enable enable, strategy  from easyjob_job ";

    private static final String COUNT_SQL = "select count(1) from easyjob_job ";

    private static final String DELETE_SQL = "delete from easyjob_job where id = ?";
    private DataSource dataSource;

    private QueryRunner queryRunner;

    public JobInfoDao(DataSource dataSource) {
        this.dataSource = dataSource;
        this.queryRunner = new QueryRunner(dataSource);
    }

    public int insert(JobInfo jobInfo) throws SQLException {
        return queryRunner.update(INSERT_SQL
                ,jobInfo.getName(),jobInfo.getDescription(),jobInfo.getTriggerType(),jobInfo.getTargetObject()
                ,jobInfo.getTargetMethod(),jobInfo.getTargetParams(),jobInfo.getTargetClass(),jobInfo.getCronExpression()
                ,jobInfo.getFireTime(), jobInfo.getStrategy(),jobInfo.getEnable());

    }

    public int update(JobInfo jobInfo) throws SQLException {

        return queryRunner.update(UPDATE_SQL
                ,jobInfo.getName(),jobInfo.getDescription(),jobInfo.getTriggerType(),jobInfo.getTargetObject()
                ,jobInfo.getTargetMethod(),jobInfo.getTargetParams(),jobInfo.getTargetClass(),jobInfo.getCronExpression()
                ,jobInfo.getFireTime(), jobInfo.getStrategy(),jobInfo.getEnable(),jobInfo.getId());

    }

    public JobInfo selectById(Long id) throws SQLException {
        StringBuffer sql = new StringBuffer(QUERY_SQL);

        sql.append(" where id = ?");

        JobInfo result = queryRunner.query(sql.toString(),
                    id, new BeanHandler<JobInfo>(JobInfo.class, new BasicRowProcessor()));

        return result;
    }

    public List<JobInfo> selectList(JobCriteria criteria) throws SQLException {
        StringBuffer datasql = new StringBuffer(QUERY_SQL);

        cancatWhereConditon(criteria,datasql);
        concatPage(criteria,datasql);

        List<JobInfo> result = queryRunner.query(datasql.toString(), new BeanListHandler<JobInfo>(JobInfo.class,new BasicRowProcessor()));

        return result;
    }


    public Page<JobInfo> selectPage(JobCriteria criteria) throws SQLException {
        StringBuffer datasql = new StringBuffer(QUERY_SQL);
        StringBuffer countsql = new StringBuffer(COUNT_SQL);
        cancatWhereConditon(criteria, datasql);
        cancatWhereConditon(criteria, countsql);
        concatPage(criteria, datasql);
        Long count = queryRunner.query(countsql.toString(), new ResultSetHandler<Long>() {
            @Override
            public Long handle(ResultSet resultSet) throws SQLException {
                resultSet.next();
                return resultSet.getLong(1);
            }
        });
        List<JobInfo> result = queryRunner.query(datasql.toString(), new BeanListHandler<JobInfo>(JobInfo.class,new BasicRowProcessor()));
        Page page = new Page();
        page.setCount(count);
        page.setData(result);

        return page;
    }

    public int deleteById(Long id) throws SQLException {
        return queryRunner.update(DELETE_SQL, id);
    }

    private static void concatPage(JobCriteria criteria, StringBuffer datasql) {
        if(criteria.getPageCur()==null||criteria.getPageSize()==null) {
            return;
        }
        datasql.append(" limit ").append((criteria.getPageCur()-1)* criteria.getPageSize())
                .append(",").append(criteria.getPageSize());
    }

    private static void cancatWhereConditon(JobCriteria criteria, StringBuffer sql) {
        if((criteria.getName()==null||criteria.getName().trim().length()==0)
                &&criteria.getEnable()==null) {
            return;
        }
        sql.append(" where ");

        boolean conditionNeadAnd = false;
        if(criteria.getName()!=null&& criteria.getName().trim().length()>0) {
            if(conditionNeadAnd) {
                sql.append(" and ");
            }
            sql.append(" name like '%").append(criteria.getName()).append("%' ");
            conditionNeadAnd = true;
        }
        if(criteria.getEnable()!=null) {
            if(conditionNeadAnd) {
                sql.append(" and ");
            }
            sql.append(" luna_enable = ").append(criteria.getEnable()?1:0);
            conditionNeadAnd = true;
        }
    }
}

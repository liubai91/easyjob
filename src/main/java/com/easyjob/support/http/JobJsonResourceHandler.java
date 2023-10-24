package com.easyjob.support.http;

import com.easyjob.core.TaskManager;
import com.easyjob.dao.JobInfoDao;
import com.easyjob.dao.JobLogDao;
import com.easyjob.dto.JobCriteria;
import com.easyjob.dto.JobInfo;
import com.easyjob.dto.Page;
import com.easyjob.entity.*;
import com.easyjob.util.JobInfoUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.beans.factory.BeanFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobJsonResourceHandler {

    private BeanFactory beanFactory;
    private ObjectMapper objectMapper;

    private JobInfoDao jobInfoDao;

    private JobLogDao jobLogDao;

    private DataSource dataSource;

    public JobJsonResourceHandler(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.dataSource = beanFactory.getBean(DataSource.class);
        this.jobInfoDao = new JobInfoDao(dataSource);
        this.jobLogDao = new JobLogDao(dataSource);
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    public void handle(String resourceName, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {

        response.setContentType("application/json");

        if (resourceName.equals("/jobmetadata.json")) {
            handleJobMetadata(response);
        } else if (resourceName.equals("/listjob.json")) {
            handleJobList(request,response);
        } else if (resourceName.equals("/getjob.json")) {
            handleJobget(request,response);
        }else if (resourceName.equals("/savejob.json")) {
            handleJobSave(request,response);
        } else if (resourceName.equals("/deljob.json")) {
            handleJobDel(request,response);
        } else if (resourceName.equals("/listjoblogs.json")) {
            handleJobLogsList(request,response);
        } else if(resourceName.equals("/startscheduler.json")){
            handleStartScheduler(request,response);
        } else if(resourceName.equals("/stopscheduler.json")){
            handleStopScheduler(request,response);
        } else if(resourceName.equals("/statusscheduler.json")){
            handleStatusScheduler(request,response);
        } else if(resourceName.equals("/listrunnningjob.json")){
            handleListRunningJobs(request,response);
        } else if(resourceName.equals("/schejob.json")){
            handleScheJob(request,response);
        } else if(resourceName.equals("/unschejob.json")){
            handleUnScheJob(request,response);
        }

    }

    private void handleJobget(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String ids = request.getParameter("id");
        Long id = Long.valueOf(ids);
        JobInfo jobInfo = jobInfoDao.selectById(id);
        objectMapper.writeValue(response.getWriter(),jobInfo);
    }

    private void handleUnScheJob(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        TaskManager taskManager = beanFactory.getBean(TaskManager.class);
        String ids = request.getParameter("id");
        Long id = Long.valueOf(ids);
        JobInfo jobInfo = jobInfoDao.selectById(id);
        Job job = JobInfoUtils.convertTo(jobInfo);
        if(job!=null) {
            boolean isSchedule = taskManager.unscheduleJob(job);
            if(isSchedule) {
                jobInfo.setEnable(false);
                jobInfoDao.update(jobInfo);
            }
            objectMapper.writeValue(response.getWriter(),isSchedule);
        }
    }

    private void handleScheJob(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        TaskManager taskManager = beanFactory.getBean(TaskManager.class);
        String ids = request.getParameter("id");
        Long id = Long.valueOf(ids);
        JobInfo jobInfo = jobInfoDao.selectById(id);
        Job job = JobInfoUtils.convertTo(jobInfo);
        if(job!=null) {
            boolean isSchedule = taskManager.scheduleJob(job);
            if(isSchedule) {
                jobInfo.setEnable(isSchedule);
                jobInfoDao.update(jobInfo);
            }
            objectMapper.writeValue(response.getWriter(),isSchedule);
        }
    }

    private void handleStatusScheduler(HttpServletRequest request, HttpServletResponse response) throws IOException {

        TaskManager taskManager = beanFactory.getBean(TaskManager.class);
        Map<String,Object> ret = new HashMap<String,Object>();
        ret.put("flag", taskManager.isSchedulerRunning());
        objectMapper.writeValue(response.getWriter(),ret);

    }

    private void handleListRunningJobs(HttpServletRequest request, HttpServletResponse response) throws IOException {

        TaskManager taskManager = beanFactory.getBean(TaskManager.class);
        List<Map<String, Object>> runningJobs = taskManager.getRunningJobs();
        objectMapper.writeValue(response.getWriter(),runningJobs);

    }

    private void handleStopScheduler(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        TaskManager taskManager = beanFactory.getBean(TaskManager.class);
        boolean success = taskManager.stopScheduler();
        if(success) {
            QueryRunner queryRunner = new QueryRunner(dataSource);
            queryRunner.update("update easyjob_scheduler set status =0");
        }
        objectMapper.writeValue(response.getWriter(),"success");

    }

    private void handleStartScheduler(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {

        TaskManager taskManager = beanFactory.getBean(TaskManager.class);
        boolean success = taskManager.startScheduler();
        if(success) {
            QueryRunner queryRunner = new QueryRunner(dataSource);
            queryRunner.update("update easyjob_scheduler set status =1");
        }
        objectMapper.writeValue(response.getWriter(),"success");

    }

    private void handleJobLogsList(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {

        JobCriteria jobCriteria = objectMapper.readValue(request.getInputStream(), JobCriteria.class);
        Page page = jobLogDao.selectPage(jobCriteria);
        objectMapper.writeValue(response.getWriter(),page);

    }

    private void handleJobDel(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {

        Long id = Long.valueOf(request.getParameter("id"));
        int update = jobInfoDao.deleteById(id);
        objectMapper.writeValue(response.getWriter(),update>0?"success":"failure");

    }

    private void handleJobSave(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {

        int result = 0;
        JobInfo jobInfo = objectMapper.readValue(request.getInputStream(), JobInfo.class);
        if(jobInfo.getId()==null) {
            //默认设置方法为未调度
            jobInfo.setEnable(false);
            result = jobInfoDao.insert(jobInfo);
        } else {
            result = jobInfoDao.update(jobInfo);
        }
        objectMapper.writeValue(response.getWriter(),result>0?"success":"failure");

    }

    private void handleJobList(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {

        JobCriteria jobCriteria = objectMapper.readValue(request.getInputStream(), JobCriteria.class);
        Page page = jobInfoDao.selectPage(jobCriteria);
        objectMapper.writeValue(response.getWriter(),page);

    }

    private void handleJobMetadata(HttpServletResponse response) throws IOException {
        TaskManager taskManager = beanFactory.getBean(TaskManager.class);
        List<TaskObjectInfo> tasks = new ArrayList<TaskObjectInfo>();
        Map<String,TaskObjectInfo> taskinfo = taskManager.getEligibleTasks();
        for(String key : taskinfo.keySet()) {
            tasks.add(taskinfo.get(key));
        }

        objectMapper.writeValue(response.getWriter(),tasks);
    }
}

package com.easyjob.entity;

import java.util.Date;

public class Job {
	
	private String name;
	private String description;
	private JobType triggerType;
	private String targetObject;
	private String targetMethod;
	private String targetParams;
	private String targetClass;
	private String cronExpression;
	private Date fireTime;
	private Boolean enable;
	private JobExceptionStrategy strategy;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public JobExceptionStrategy getStrategy() {
		return strategy;
	}
	public void setStrategy(JobExceptionStrategy strategy) {
		this.strategy = strategy;
	}
	public JobType getTriggerType() {
		return triggerType;
	}
	public void setTriggerType(JobType triggerType) {
		this.triggerType = triggerType;
	}
	public String getTargetObject() {
		return targetObject;
	}
	public void setTargetObject(String targetObject) {
		this.targetObject = targetObject;
	}
	public String getTargetMethod() {
		return targetMethod;
	}
	public void setTargetMethod(String targetMethod) {
		this.targetMethod = targetMethod;
	}
	public String getTargetParams() {
		return targetParams;
	}
	public void setTargetParams(String targetParams) {
		this.targetParams = targetParams;
	}
	public String getTargetClass() {
		return targetClass;
	}
	public void setTargetClass(String targetClass) {
		this.targetClass = targetClass;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public Boolean getEnable() {
		return enable;
	}
	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
	public Date getFireTime() {
		return fireTime;
	}
	public void setFireTime(Date fireTime) {
		this.fireTime = fireTime;
	}

}

package com.easyjob.entity;


public class JobLog {

	private Long id;
	
	private String name;
	private Long startTime;
	private Long endTime;
	private Long duration;
	private String state;
	private String log;
	private String exceptionDetail;
	private String exceptionBrief;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	public String getExceptionDetail() {
		return exceptionDetail;
	}
	public void setExceptionDetail(String exceptionDetail) {
		this.exceptionDetail = exceptionDetail;
	}
	public String getExceptionBrief() {
		return exceptionBrief;
	}
	public void setExceptionBrief(String exceptionBrief) {
		this.exceptionBrief = exceptionBrief;
	}

}

package com.easyjob.entity;

import java.util.List;

public class TaskMethodInfo {
	
	private String name;
	private String alias;
	private String description;
	private List<TaskParamInfo> params;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<TaskParamInfo> getParams() {
		return params;
	}
	public void setParams(List<TaskParamInfo> params) {
		this.params = params;
	}
	

}

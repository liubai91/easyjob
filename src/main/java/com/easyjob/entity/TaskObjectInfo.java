package com.easyjob.entity;

import java.util.List;

public class TaskObjectInfo {
	
	private String name;
	private String alias;
	private List<TaskMethodInfo> methods;
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
	public List<TaskMethodInfo> getMethods() {
		return methods;
	}
	public void setMethods(List<TaskMethodInfo> methods) {
		this.methods = methods;
	}

}

package com.easyjob.entity;

public enum JobType {
	
	RIGHTNOW(1,"立即"),
	DELAY(2,"指定时间"),
	REGULAR(3,"定期循环执行");
	private Integer code;
	private String desc;
	private JobType(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	

}

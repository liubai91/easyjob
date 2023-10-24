package com.easyjob.entity;

public enum JobExceptionStrategy {
	
	GOON(1,"继续"),
	BREAK(2,"中止");
	private Integer code;
	private String desc;
	private JobExceptionStrategy(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
}

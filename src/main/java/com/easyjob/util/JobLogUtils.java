package com.easyjob.util;

import org.quartz.JobExecutionContext;

import java.util.LinkedHashMap;
import java.util.Map;

public class JobLogUtils {
	
	public static void addLog(String point,String content){
		JobExecutionContext context = ThreadLocalAccessUtils.getJobContextForCurrentThread();
		if(context==null){
			return ;
		}
		Map<String,String> log = (Map<String, String>) context.get("lm-log");
		if(log==null){
			log = new LinkedHashMap<String, String>();
			context.put("lm-log", log);
		}
		log.put(point, content);
	}
	
	public static String getLog(){
		JobExecutionContext context = ThreadLocalAccessUtils.getJobContextForCurrentThread();
		Map<String,String> log = (Map<String, String>) context.get("lm-log");
		if(log==null){
			return null;
		}
		StringBuffer data = new StringBuffer();
		data.append("{");
		boolean hasPrev = false;
		for(String point : log.keySet()){
			if(hasPrev){
				data.append(",");
			}
			data.append(String.format("\"%s\":\"%s\"", point, log.get(point)));
			hasPrev = true;
		}
		data.append("}");
		return data.toString();
	}

}

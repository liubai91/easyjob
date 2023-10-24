package com.easyjob.util;


import com.easyjob.core.TaskManager;
import com.easyjob.entity.Job;
import com.easyjob.entity.TaskMethodInfo;
import com.easyjob.entity.TaskObjectInfo;
import com.easyjob.entity.TaskParamInfo;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class MethodArgumentUtils {
	
	public static Object[] generateMethodParams(Job task) {
		List<Object> arguments = new ArrayList<Object>();
		TaskObjectInfo targetObject = TaskManager.getEligibleTasks().get(task.getTargetObject());
		Assert.notNull(targetObject, "任务对象未找到");
		TaskMethodInfo targetMethod = null;
		for(TaskMethodInfo method : targetObject.getMethods()){
			if(method.getName().equals(task.getTargetMethod())){
				targetMethod = method;
				break;
			}
		}
		Assert.notNull(targetObject, "任务方法未找到");
		List<TaskParamInfo> paramInfos = targetMethod.getParams();
		if(paramInfos!=null&&paramInfos.size()>0){
			String[] values = task.getTargetParams().split(",",-1);
			for(int i=0; i<paramInfos.size(); i++){
				TaskParamInfo paramInfo = paramInfos.get(i);
				if(paramInfo.getType()==Integer.class||paramInfo.getType()==int.class){
					arguments.add(Integer.valueOf(values[i]));
				}else if(paramInfo.getType()==String.class){
					arguments.add(values[i]);
				}else if(paramInfo.getType()==Float.class||paramInfo.getType()==float.class){
					arguments.add(Float.valueOf(values[i]));
				}else if(paramInfo.getType()==Double.class||paramInfo.getType()==double.class){
					arguments.add(Double.valueOf(values[i]));
				}else if(paramInfo.getType()==Boolean.class||paramInfo.getType()==boolean.class){
					arguments.add(Boolean.valueOf(values[i]));
				}else if(paramInfo.getType()==Character.class||paramInfo.getType()==char.class){
					arguments.add(Character.valueOf(values[i].charAt(0)));
				}
			}
		}
		return arguments.toArray(new Object[0]);
	}

}

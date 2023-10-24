package com.easyjob.core;


import com.easyjob.annotation.Task;
import com.easyjob.annotation.TaskGroup;
import com.easyjob.annotation.TaskParam;
import com.easyjob.entity.TaskMethodInfo;
import com.easyjob.entity.TaskObjectInfo;
import com.easyjob.entity.TaskParamInfo;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class TaskAnnotationProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		Class<?> leafClass = bean.getClass();
		if(leafClass.isAnnotationPresent(TaskGroup.class)){
			TaskObjectInfo taskObjectInfo = extractTaskObjectInfo(beanName, leafClass);
			TaskManager.eligibleTasks.put(beanName, taskObjectInfo);
		}
		return bean;
	}

	private TaskObjectInfo extractTaskObjectInfo(String beanName, Class<?> leafClass) {
		TaskGroup tgAnnotation = (TaskGroup) leafClass.getAnnotation(TaskGroup.class);
		TaskObjectInfo info = new TaskObjectInfo();
		info.setName(beanName);
		info.setAlias(tgAnnotation.decription().equals("")?beanName:tgAnnotation.decription());
		List<TaskMethodInfo> tasks = new ArrayList<TaskMethodInfo>();
		Method[] methods = ReflectionUtils.getAllDeclaredMethods(leafClass);
		for(Method method : methods){
			if(method.isAnnotationPresent(Task.class)){
				TaskMethodInfo task = extractTaskMethodInfo(method);
				tasks.add(task);
			}
		}
		info.setMethods(tasks);
		return info;
	}

	private TaskMethodInfo extractTaskMethodInfo(Method method) {
		String alias = method.getAnnotation(Task.class).value();
		TaskMethodInfo task = new TaskMethodInfo();
		task.setName(method.getName());
		task.setAlias(alias.equals("") ? method.getName() : alias);
		task.setDescription(method.getAnnotation(Task.class).description());
		List<TaskParamInfo> params = extractTaskParamInfos(method);
		task.setParams(params);
		return task;
	}

	private List<TaskParamInfo> extractTaskParamInfos(Method method) {
		List<TaskParamInfo> params = new ArrayList<TaskParamInfo>();
		Class<?>[] paramtypes = method.getParameterTypes();
		Annotation[][] paramAnnos = method.getParameterAnnotations();
		for(int i = 0; i<paramtypes.length; i++){
			String paramName = null;
			Annotation[] annos = paramAnnos[i];
			for(Annotation anno : annos){
				if(anno instanceof TaskParam){
					paramName = ((TaskParam) anno).value();
				}
			}
			if(paramName==null){
				paramName = "参数"+(i+1);
			}
			TaskParamInfo param = new TaskParamInfo();
			param.setType(paramtypes[i]);
			param.setName(paramName);
			params.add(param);
		}
		return params;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}

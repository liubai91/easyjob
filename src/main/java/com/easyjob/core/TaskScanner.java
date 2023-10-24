package com.easyjob.core;


import com.easyjob.annotation.TaskGroup;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public class TaskScanner extends ClassPathBeanDefinitionScanner {

	public TaskScanner(BeanDefinitionRegistry registry) {
		super(registry);
	}

	@Override
	protected void registerDefaultFilters() {
		 this.addIncludeFilter(new AnnotationTypeFilter(TaskGroup.class));
	}
	
	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		return super.isCandidateComponent(beanDefinition) && beanDefinition.getMetadata()
				.hasAnnotation(TaskGroup.class.getName());
	}
	

}

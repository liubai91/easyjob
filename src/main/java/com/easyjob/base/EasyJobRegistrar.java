package com.easyjob.base;

import com.easyjob.core.TaskAnnotationProcessor;
import com.easyjob.core.TaskManager;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class EasyJobRegistrar implements ImportBeanDefinitionRegistrar {


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        AbstractBeanDefinition taskAnnotationProcessorbeanDefinition = BeanDefinitionBuilder.genericBeanDefinition(TaskAnnotationProcessor.class).getBeanDefinition();
        registry.registerBeanDefinition("easyjobTaskAnnotationProcessor", taskAnnotationProcessorbeanDefinition);

        AbstractBeanDefinition taskManagerBeanDefinition = BeanDefinitionBuilder.genericBeanDefinition(TaskManager.class).getBeanDefinition();
        registry.registerBeanDefinition("easyJobTaskManager",taskManagerBeanDefinition);


    }
}

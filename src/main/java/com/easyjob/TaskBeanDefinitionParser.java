package com.easyjob;


import com.easyjob.core.TaskAnnotationProcessor;
import com.easyjob.core.TaskManager;
import com.easyjob.core.TaskScanner;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

public class TaskBeanDefinitionParser implements BeanDefinitionParser {

	public BeanDefinition parse(Element element, ParserContext parserContext) {

		String basePackage = element.getAttribute("base-package");
		String[] basePackages = StringUtils.tokenizeToStringArray(basePackage,
				ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
		TaskScanner scanner = new TaskScanner(parserContext.getRegistry());
		scanner.scan(basePackages);

		AnnotatedBeanDefinition taskManagerDefinition  = new AnnotatedGenericBeanDefinition(TaskManager.class);
		BeanDefinitionHolder taskManagerDefinitionHolder = new BeanDefinitionHolder(taskManagerDefinition, "taskManagerlm");
		BeanDefinitionReaderUtils.registerBeanDefinition(taskManagerDefinitionHolder, parserContext.getRegistry());
		
		AnnotatedBeanDefinition taskAnnotationProcessorDefinition = new AnnotatedGenericBeanDefinition(TaskAnnotationProcessor.class);
		BeanDefinitionHolder taskAnnotationProcessorDefinitionHolder = new BeanDefinitionHolder(taskAnnotationProcessorDefinition, "taskgroupHandler");
		BeanDefinitionReaderUtils.registerBeanDefinition(taskAnnotationProcessorDefinitionHolder, parserContext.getRegistry());
		return null;
	}

}

package com.easyjob;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class LmtaskNamespaceHandler extends NamespaceHandlerSupport {

	public void init() {
		
		registerBeanDefinitionParser("task",new TaskBeanDefinitionParser());

	}

}

package com.easyjob.annotation;

import java.lang.annotation.*;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Task {
	String value() default "";
	String description() default "";
}

package com.easyjob.annotation;


import com.easyjob.base.EasyJobRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(EasyJobRegistrar.class)
public @interface EnableEasyJob {
}

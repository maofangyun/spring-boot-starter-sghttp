package com.seago.http.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface WebClient {

	String name() default "";

	String url() default "";

	boolean dismiss404() default false;

	Class<?> fallbackFactory() default void.class;

}

package cn.gotom.commons;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface Note {
	String value() default "";

	String memo() default "";
}

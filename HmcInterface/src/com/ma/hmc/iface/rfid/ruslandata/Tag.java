package com.ma.hmc.iface.rfid.ruslandata;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface Tag {
	int tag();

	int len();
	
	int used();
	
	String comment() default "";
}

package ru.aoit.hmc.rfid.ruslandata;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface Tag {
	int tag();

	int len();

	String comment() default "";
}

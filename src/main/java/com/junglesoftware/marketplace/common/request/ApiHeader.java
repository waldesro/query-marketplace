package com.junglesoftware.marketplace.common.request;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Documented
@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiHeader {
  public String name();
  public String description();
  public boolean required() default false;
  public String defaultValue() default StringUtils.EMPTY;
}

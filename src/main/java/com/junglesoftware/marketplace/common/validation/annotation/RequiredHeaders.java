package com.junglesoftware.marketplace.common.validation.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

/**
 * This annotation allows a controller to specify the additional headers to have the 
 * framework verify are present in each request. Compatible headers are specified in the 
 * {@link HeaderValidation} enum.
 * 
 * This annotation can be used at the Type or Method level. If both are present, it will validate
 * for all headers specified at every level. In addition, headers required for every call in an application
 * can be set in application.properties.
 * 
 * eg.:
 * headerValidation.EHI_CALLER_ID=true
 */

@Target({METHOD, TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredHeaders {
  HeaderValidation[] value();
}

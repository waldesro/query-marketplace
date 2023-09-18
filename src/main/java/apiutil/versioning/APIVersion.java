package apiutil.versioning;

import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@RequestMapping
@Retention(RetentionPolicy.RUNTIME)
public @interface APIVersion {
  int[] versions() default 1;
}

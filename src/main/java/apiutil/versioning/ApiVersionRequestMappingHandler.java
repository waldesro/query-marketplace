package apiutil.versioning;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

public class ApiVersionRequestMappingHandler extends RequestMappingHandlerMapping {
  private final String prefix;

  public ApiVersionRequestMappingHandler() {
    this.prefix = "v";
  }

//  @Override
//  protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
//    RequestMappingInfo info = super.getMappingForMethod(method, handlerType);
//    if(info == null) return null;
//
//    APIVersion methodAnnotation = AnnotationUtils.findAnnotation(method, APIVersion.class);
//    RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(handlerType, RequestMapping.class);
//    if(methodAnnotation != null) {
//      RequestCondition<?> methodCondition = getCustomMethodCondition(method);
//      // Concatenate our ApiVersion with the usual request mapping
//      info = createApiVersionInfo(requestMapping, methodAnnotation, methodCondition).combine(info);
//    } else {
//      APIVersion typeAnnotation = AnnotationUtils.findAnnotation(handlerType, APIVersion.class);
//      if(typeAnnotation != null) {
//        RequestCondition<?> typeCondition = getCustomTypeCondition(handlerType);
//        // Concatenate our ApiVersion with the usual request mapping
//        info = createApiVersionInfo(requestMapping, typeAnnotation, typeCondition).combine(info);
//      }
//    }
//
//    return info;
//  }
//  protected RequestMappingInfo createApiVersionInfo(
//      RequestMapping requestMapping, APIVersion annotation, @Nullable RequestCondition<?> customCondition) {
//    int[] values = annotation.versions();
//    String[] patterns = new String[values.length];
//    for(int i=0; i<values.length; i++) {
//      // Build the URL prefix
//      patterns[i] = prefix+values[i];
//    }
//
//    RequestMappingInfo.Builder builder = RequestMappingInfo
//        .paths(resolveEmbeddedValuesInPatterns(requestMapping.path()))
//        .methods(requestMapping.method())
//        .params(requestMapping.params())
//        .headers(requestMapping.headers())
//        .consumes(requestMapping.consumes())
//        .produces(requestMapping.produces())
//        .mappingName(requestMapping.name());
//
//    if (customCondition != null) {
//      builder.customCondition(customCondition);
//    }
//
//    return builder.build();
//  }
}

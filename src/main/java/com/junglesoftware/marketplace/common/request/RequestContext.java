package com.junglesoftware.marketplace.common.request;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Data
@Component
@RequestScope
public class RequestContext {
  //The following fields are used for routing requests
  @ApiHeader(name = "Accept",
      description = "Content type and API versioning. Please refer to the Versioning Strategy for additional details.",
      required = true)
  private String accept;

  @ApiHeader(name = "Authorization",
      description = "SSF Token",
      required = true)
  private String authorization;

  // The following fields are used for request processing
  @ApiHeader(name = "Locale",
      description = "The locale of the invoking client, in ISO3 with IETF formatting (using hyphens, not underscores).",
      defaultValue = "en_US")
  private Locale locale = Locale.getDefault();
  
  // Optional request headers
  @ApiHeader(name = "Content-Type",
      description = "Specifies the media type of the request content.")
  private String contentType;
  @ApiHeader(name = "Api-Key",
      description = "The assigned client API key for the application. Has various uses, such as an additional security measure and for analytics.")
  private String apiKey;
  
  private String ifMatch;
  private String ifNoneMatch;
  private String ifModifiedSince;
  private String ifUnmodifiedSince;
  private String ifRange;

  // The following fields are used for timing requests
  private long executionStartTime;
  private long sla;

  private String servicePath;

  // The following fields are used for tracking requests
  @ApiHeader(name = "Calling-Application",
      description = "The name of the calling application",
      required = true)
  private String callingApplication;
  
  //This field is provided to the user to have a location to store request scoped data
  private Map<String, Object> customData = new HashMap<>();

  //This field is provided for user to add custom data in perf log, in addition to predefined log data.
  private Map<String, String> customPerfLogData = new HashMap<>();
  
  //This field is used to determine whether current RQ and RS should be logged in Request-Response.log file
  private boolean logRqRs = false;
  
  private MessageFormat responseMessageFormat;

  private boolean inBody;

}

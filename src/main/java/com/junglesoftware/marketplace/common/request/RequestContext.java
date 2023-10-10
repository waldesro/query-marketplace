package com.junglesoftware.marketplace.common.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.junglesoftware.marketplace.common.response.MessageFormat;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

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
  @ApiHeader(name = "Ehi-Locale",
      description = "The locale of the invoking client, in ISO3 with IETF formatting (using hyphens, not underscores).",
      defaultValue = "en_US")
  private Locale locale = Locale.getDefault();
  
  // Optional request headers
  @ApiHeader(name = "Content-Type",
      description = "Specifies the media type of the request content.")
  private String contentType;
  @ApiHeader(name = "Ehi-Api-Key",
      description = "The assigned client API key for the application. Has various uses, such as an additional security measure and for analytics.")
  private String ehiApiKey;
  @ApiHeader(name = "Ehi-Caller-Id",
      description = "Could be the session ID, an Employee ID, an upstream client name, etc.")
  private String ehiCallerId;
  @ApiHeader(name = "Ehi-Device-Location-Id",
      description = "A value identifying the location of the calling client device, such as a group+branch or station ID.")
  private String ehiDeviceLocationId;
  
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
  @ApiHeader(name = "Ehi-Calling-Application",
      description = "The name of the calling application",
      required = true)
  private String callingApplication;
  @ApiHeader(name = "X-B3-SpanId",
      description = "A unique ID to identify the specific request from client to service.",
      required = true)
  private String spanId;
  @ApiHeader(name = "X-B3-TraceId",
      description = "A unique ID to identify a group of related downstream requests which may span from a single client request.",
      required = true)
  private String traceId;
  @ApiHeader(name = "Ehi-Workflow-Id",
      description = "This should be a user session, a UI workflow such as opening a ticket, or other useful value.",
      required = true)
  private String workflowId;

  @ApiHeader(name = "Ehi-Origin-Identity",
      description = "The identity of the person whose actions triggered this request or initiated this business workflow.")
  private String ehiOriginIdentity;
  
  //This field is provided to the user to have a location to store request scoped data
  private Map<String, Object> customData = new HashMap<>();

  //This field is provided for user to add custom data in perf log, in addition to predefined log data.
  private Map<String, String> customPerfLogData = new HashMap<>();
  
  //This field is used to determine whether current RQ and RS should be logged in Request-Response.log file
  private boolean logRqRs = false;
  
  private MessageFormat responseMessageFormat;

  private boolean inBody;

  // Ehi-Messages text before it is encoded on the response header
  private String plainEhiMessages;

  @JsonIgnore
  private JwtClaims jwtClaims;
}

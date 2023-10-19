package com.junglesoftware.marketplace.common.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
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
  
  private String ifMatch;
  private String ifNoneMatch;
  private String ifModifiedSince;
  private String ifUnmodifiedSince;
  private String ifRange;


  private String servicePath;
  
  //This field is provided to the user to have a location to store request scoped data
  private Map<String, Object> customData = new HashMap<>();

  @JsonIgnore
  private JwtClaimsSet jwtClaims;
}

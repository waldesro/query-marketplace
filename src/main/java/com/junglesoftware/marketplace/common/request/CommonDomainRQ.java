package com.junglesoftware.marketplace.common.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@SuperBuilder
public abstract class CommonDomainRQ {
    private RequestContext requestContext;
}

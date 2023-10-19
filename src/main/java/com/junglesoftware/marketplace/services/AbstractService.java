package com.junglesoftware.marketplace.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public abstract class AbstractService <T_INPUT, T_OUTPUT>{
    public final T_OUTPUT process(T_INPUT request) throws Exception {

    }
}

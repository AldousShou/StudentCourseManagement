package com.shoumh.core.configuration;

import com.shoumh.core.controller.interceptor.RequestLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Component
public class SpringMvcSupport extends WebMvcConfigurationSupport {

    @Autowired
    private RequestLimitInterceptor interceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }
}

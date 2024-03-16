package com.shoumh.core.configuration;

import com.shoumh.core.controller.interceptor.RequestLimitInterceptor;
import com.shoumh.core.controller.interceptor.ServletTimerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Component
public class SpringMvcSupport extends WebMvcConfigurationSupport {

    @Autowired
    private RequestLimitInterceptor requestLimitInterceptor;

    @Autowired
    private ServletTimerInterceptor servletTimerInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLimitInterceptor);
        registry.addInterceptor(servletTimerInterceptor);
    }
}

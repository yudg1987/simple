package com.dgyu.pure_design.common.config;


import io.undertow.UndertowOptions;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

/**
 * 解决只要url中包含特殊字符，如"|"、“<”、“{}”、中文等，程序均无法打开。
 * @author dgyu
 * @date 2022/42/15
 **/
@Component
public class MyWebServerCustomizer implements WebServerFactoryCustomizer<UndertowServletWebServerFactory> {

    @Override
    public void customize(UndertowServletWebServerFactory factory) {
        factory.addBuilderCustomizers(builder -> builder.setServerOption(UndertowOptions.ALLOW_UNESCAPED_CHARACTERS_IN_URL, Boolean.TRUE)); //url配置
        factory.addBuilderCustomizers(builder -> builder.setServerOption(UndertowOptions.ALLOW_EQUALS_IN_COOKIE_VALUE, Boolean.TRUE)); //cookie配置
    }

}


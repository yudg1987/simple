package com.dgyu.pure_design;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@MapperScan(basePackages = {"com.dgyu.pure_design.mapper"})
@SpringBootApplication
public class PureDesignApplication {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext run = SpringApplication.run(PureDesignApplication.class, args);
        Environment env = run.getEnvironment();
        log.info(
                "\n----------------------------------------------------------\n\t"
                        + "Application '{}' is running!!!! :\n\t"
                        + "服务地址: \t\thttp://{}:{}\n\t"
                        + "swagger地址: \thttp://{}:{}{}/doc.html\n\t"
                        + "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                env.getProperty("server.servlet.context-path")
        );
    }

}


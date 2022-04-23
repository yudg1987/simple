package com.dgyu.pure_design.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author dgyu
 * @date 2022/09/08
 **/
@Component
@Getter
public class ApplicationConfig {

    /*资源 jcy:检察院 fy:法院 用来获取起诉书或者起诉意见书*/
    @Value("${server.resource}")
    private String resource;

    //环境 test:测试 production:生产
    @Value("${server.model}")
    private String model;

    /**
     * 核心线程数量
     */
    @Value("${threadPool.corePoolSize}")
    private Integer corePoolSize;
    /**
     * 线程池维护线程的最大数量
     */
    @Value("${threadPool.maxPoolSize}")
    private int maxPoolSize;
    /**
     * 线程池维护线程的等待队列大小
     */
    @Value("${threadPool.queueSize}")
    private Integer queueSize;

    /**
     * 临时文件存储位置  根据实际情况修改
     */
    @Value("${file.temp_path}")
    private String tmpPath;

}

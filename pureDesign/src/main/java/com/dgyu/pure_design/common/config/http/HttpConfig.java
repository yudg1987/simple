package com.dgyu.pure_design.common.config.http;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p>
 * http相关配置
 * </p>
 *
 * @author dgyu
 * @date 2022-12-28
 */
@Component
@Getter
public class HttpConfig {

    /**
     *
     */
    @Value("${http.timeout}")
    private int timeout;
}

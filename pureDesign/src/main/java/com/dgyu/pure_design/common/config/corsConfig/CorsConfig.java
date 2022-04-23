package com.dgyu.pure_design.common.config.corsConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
public class CorsConfig
{

	public CorsConfig()
	{
	}


	@Bean
	public CorsFilter corsFilter()
	{
		// 1. 添加cors配置信息
		CorsConfiguration config = new CorsConfiguration();
		// config.addAllowedOrigin("http://localhost:8080");
		config.addAllowedOrigin("*");

		// 设置是否发送cookie信息
		config.setAllowCredentials(true);

		// 设置允许请求的方式
		config.addAllowedMethod("*");

		// 设置允许的header
		config.addAllowedHeader("*");
		//暴露哪些头部信息（因为跨域访问默认不能获取全部头部信息）
//        config.addExposedHeader("*");

		config.addExposedHeader("Content-Type");
		config.addExposedHeader( "X-Requested-With");
		config.addExposedHeader("accept");
		config.addExposedHeader("Origin");
		config.addExposedHeader( "Access-Control-Request-Method");
		config.addExposedHeader("Access-Control-Request-Headers");
		config.addExposedHeader("Content-Disposition");


		// 2. 为url添加映射路径
		UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
		corsSource.registerCorsConfiguration("/**", config);

		// 3. 返回重新定义好的corsSource
		return new CorsFilter(corsSource);
	}

}

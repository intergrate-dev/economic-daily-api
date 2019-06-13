package com.founder.econdaily.common.config;

import com.founder.econdaily.common.interceptor.SysInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class WebAppConfigurer extends WebMvcConfigurerAdapter {

	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
		return new MethodValidationPostProcessor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		String[] patterns = new String[] { "/auth/login","/*.html","/swagger-resources/**", "/error", "/test/*"};
		registry.addInterceptor(new SysInterceptor())
		                         .addPathPatterns("/**")
		                         .excludePathPatterns(patterns);
		super.addInterceptors(registry);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*")
				.allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE","OPTIONS")
				.allowCredentials(false).maxAge(3600);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new JwtTokenArgumentResolver());
	}
}
package com.founder.econdaily.common.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@Configuration
@EnableSwagger2
//@Profile("dev")
public class SwaggerConfigure {

    @Bean(value = "authApi")
    public Docket authApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("用户认证")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.founder.econdaily.modules.auth"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean(value = "newsPaperApi")
    public Docket newsPaperApi() {
        List<Parameter> aParameters = globalToken();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("电子报接口")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.founder.econdaily.modules.newspaper"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(aParameters);
    }

    @Bean(value = "magazineApi")
    public Docket magazineApi() {
        List<Parameter> aParameters = globalToken();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("期刊接口")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.founder.econdaily.modules.magazine"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(aParameters);
    }

    @Bean(value = "historySourceApi")
    public Docket historySourceApi() {
        List<Parameter> aParameters = globalToken();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("历史资源接口")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.founder.econdaily.modules.historySource"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(aParameters);
    }

    private List<Parameter> globalToken() {
        ParameterBuilder aParameterBuilder = new ParameterBuilder();
        aParameterBuilder.name("token").description("token").modelRef(new ModelRef("string"))
                .parameterType("header").required(true).build();
        List<Parameter> aParameters = Lists.newArrayList();
        aParameters.add(aParameterBuilder.build());
        return aParameters;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("swagger文档")
                .termsOfServiceUrl("http://localhost:8086/economic-daily-api")
                .version("1.1").build();
    }
}

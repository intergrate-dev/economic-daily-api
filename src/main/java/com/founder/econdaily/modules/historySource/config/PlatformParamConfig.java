package com.founder.econdaily.modules.historySource.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Created by yuan-pc on 2018/3/27.
 */
//@Component
@Configuration
public class PlatformParamConfig {
    public static final HashMap<String, String> configsMap = new HashMap<String, String>();
    public static final String PAPER_ROOT = "paperRoot";

    @Value("${platform.dev.paperRootPath}")
    private String paperRootPathDev;

    @Value("${platform.prod.paperRootPath}")
    private String paperRootPathProd;

    public static final String MAGAZINE_ROOT = "magazineRoot";

    @Value("${platform.dev.magazineRootPath}")
    private String magazineRootPathDev;

    @Value("${platform.prod.magazineRootPath}")
    private String magazineRootPathProd;


    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("define.yml"));
        propertySourcesPlaceholderConfigurer.setProperties(yaml.getObject());
        return propertySourcesPlaceholderConfigurer;
    }

    @Bean
    @Profile("dev")
    public String getPropertyConfigDev() {
        configsMap.put(PAPER_ROOT, paperRootPathDev);
        configsMap.put(MAGAZINE_ROOT, magazineRootPathDev);
        return null;
    }

    @Bean
    @Profile("prod")
    public String getPropertyConfigProd() {
        configsMap.put(PAPER_ROOT, paperRootPathProd);
        configsMap.put(MAGAZINE_ROOT, magazineRootPathProd);
        return null;
    }

    @Bean
    public String getPropertyConfig() {
        return null;
    }



}

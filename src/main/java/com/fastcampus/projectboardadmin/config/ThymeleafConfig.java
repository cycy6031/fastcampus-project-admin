package com.fastcampus.projectboardadmin.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;

@Configuration
public class ThymeleafConfig {

    @Bean
    public SpringResourceTemplateResolver templateResolver(
        SpringResourceTemplateResolver templateResolver,
        Thymeleaf3Properties thymeleaf3Properties
    ) {
        templateResolver.setUseDecoupledLogic(thymeleaf3Properties.decoupledLogic());

        return templateResolver;
    }

    @ConfigurationProperties("spring.thymeleaf3")
    public record Thymeleaf3Properties(boolean decoupledLogic){}
}

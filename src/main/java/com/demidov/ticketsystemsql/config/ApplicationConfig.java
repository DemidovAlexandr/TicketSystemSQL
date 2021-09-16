package com.demidov.ticketsystemsql.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

@Configuration
@Profile("default")
public class ApplicationConfig {

    @Bean
    public Jackson2RepositoryPopulatorFactoryBean repositoryPopulator() {

        Resource sourceData = new ClassPathResource("populator.json");
        Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
        factory.setResources(new Resource[]{sourceData});

        return factory;
    }

    @Bean
    public JavaTimeModule javaTimeModule() {
        return new JavaTimeModule();
    }
}

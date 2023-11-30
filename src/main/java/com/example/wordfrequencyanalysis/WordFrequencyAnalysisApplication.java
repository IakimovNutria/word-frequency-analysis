package com.example.wordfrequencyanalysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class WordFrequencyAnalysisApplication {

    public static void main(String[] args) {
        SpringApplication.run(WordFrequencyAnalysisApplication.class, args);
    }

}

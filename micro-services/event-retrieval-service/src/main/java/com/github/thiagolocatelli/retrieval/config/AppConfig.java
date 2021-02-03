package com.github.thiagolocatelli.retrieval.config;

import com.github.thiagolocatelli.retrieval.domain.PulsarTransfer;
import io.github.majusko.pulsar.producer.ProducerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    public static final String TOPIC_NAME = "clearing-events";

    @Bean
    public ProducerFactory producerFactory() {
        return new ProducerFactory()
                .addProducer(TOPIC_NAME, PulsarTransfer.class);
    }

}

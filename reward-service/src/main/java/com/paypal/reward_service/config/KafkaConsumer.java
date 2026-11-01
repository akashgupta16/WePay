package com.paypal.reward_service.config;


import com.paypal.reward_service.entity.Transaction;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumer {

    @Bean
    public ConsumerFactory<String, Transaction> consumerFactory() {

        Map<String, Object> props = new HashMap<>();

        // Kafka basics
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "reward-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Key deserializer
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);

        // Value deserializer (Spring Kafka 4.x way)
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                org.springframework.kafka.support.serializer.ErrorHandlingDeserializer.class);

        // Delegate JSON deserializer (STRING-BASED → no deprecation)
        props.put("spring.deserializer.value.delegate.class",
                "org.springframework.kafka.support.serializer.JsonDeserializer");

        // JSON configuration (replaces your setter calls)
        props.put("spring.json.value.default.type",
                "com.paypal.reward_service.entity.Transaction");
        props.put("spring.json.trusted.packages", "*");
        props.put("spring.json.use.type.headers", false);

        return new DefaultKafkaConsumerFactory<>(props);
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Transaction> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, Transaction> factory= new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());
        return factory;
    }


}
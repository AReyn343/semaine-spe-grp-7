package com.example.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    public static final String TOPIC_MATCH_FINISHED    = "match-events";
    public static final String TOPIC_PLAYER_BANNED     = "player-banned";
    public static final String TOPIC_SCORE_UPDATED     = "score-events";
    public static final String TOPIC_SERVER_OVERLOADED = "server-events";

    @Value("${spring.kafka.bootstrap-servers:kafka:9092}")
    private String bootstrapServers;

    // ── Topics ────────────────────────────────────────────────
    @Bean public NewTopic matchEventsTopic()  { return TopicBuilder.name(TOPIC_MATCH_FINISHED).partitions(3).replicas(1).build(); }
    @Bean public NewTopic playerBannedTopic() { return TopicBuilder.name(TOPIC_PLAYER_BANNED).partitions(1).replicas(1).build(); }
    @Bean public NewTopic scoreEventsTopic()  { return TopicBuilder.name(TOPIC_SCORE_UPDATED).partitions(3).replicas(1).build(); }
    @Bean public NewTopic serverEventsTopic() { return TopicBuilder.name(TOPIC_SERVER_OVERLOADED).partitions(1).replicas(1).build(); }

    // ── Consumer Factory String/String (robuste, pas de pb JSON) ─
    @Bean
    public ConsumerFactory<String, String> stringConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "monitoring-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> stringKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stringConsumerFactory());
        return factory;
    }
}

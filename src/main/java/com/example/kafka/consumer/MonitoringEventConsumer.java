package com.example.kafka.consumer;

import com.example.kafka.config.KafkaConfig;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Consumer Kafka — deserialisation brute (String) pour eviter les erreurs JSON.
 * Incremente les Counters Micrometer a chaque message recu.
 */
@Component
public class MonitoringEventConsumer {

    private final MeterRegistry registry;

    private Counter matchFinishedCounter;
    private Counter playerBannedCounter;
    private Counter scoreUpdatedCounter;
    private Counter serverOverloadedCounter;
    private Counter eloGainCounter;
    private Counter eloLossCounter;

    public MonitoringEventConsumer(MeterRegistry registry) {
        this.registry = registry;
    }

    @PostConstruct
    public void initCounters() {
        matchFinishedCounter    = Counter.builder("kafka_match_finished_total")
                .description("Matchs termines via Kafka").tag("app","groupe7").register(registry);
        playerBannedCounter     = Counter.builder("kafka_player_banned_total")
                .description("Bans via Kafka").tag("app","groupe7").register(registry);
        scoreUpdatedCounter     = Counter.builder("kafka_score_updated_total")
                .description("MAJ ELO via Kafka").tag("app","groupe7").register(registry);
        serverOverloadedCounter = Counter.builder("kafka_server_overloaded_total")
                .description("Serveurs surcharges via Kafka").tag("app","groupe7").register(registry);
        eloGainCounter          = Counter.builder("kafka_elo_gain_total")
                .description("Gains ELO via Kafka").tag("app","groupe7").register(registry);
        eloLossCounter          = Counter.builder("kafka_elo_loss_total")
                .description("Pertes ELO via Kafka").tag("app","groupe7").register(registry);
        System.out.println("[Kafka Consumer] Counters Micrometer initialises");
    }

    // Deserialisation en String brut — evite tout probleme de type JSON
    @KafkaListener(topics = KafkaConfig.TOPIC_MATCH_FINISHED, groupId = "monitoring-group",
                   containerFactory = "stringKafkaListenerContainerFactory")
    public void onMatchFinished(ConsumerRecord<String, String> record) {
        matchFinishedCounter.increment();
        System.out.printf("[Kafka Consumer] MatchFinished recu | partition=%d offset=%d%n",
                record.partition(), record.offset());
    }

    @KafkaListener(topics = KafkaConfig.TOPIC_PLAYER_BANNED, groupId = "monitoring-group",
                   containerFactory = "stringKafkaListenerContainerFactory")
    public void onPlayerBanned(ConsumerRecord<String, String> record) {
        playerBannedCounter.increment();
        System.out.printf("[Kafka Consumer] PlayerBanned recu | offset=%d%n", record.offset());
    }

    @KafkaListener(topics = KafkaConfig.TOPIC_SCORE_UPDATED, groupId = "monitoring-group",
                   containerFactory = "stringKafkaListenerContainerFactory")
    public void onScoreUpdated(ConsumerRecord<String, String> record) {
        scoreUpdatedCounter.increment();
        String payload = record.value();
        // Detecter gain/perte ELO depuis le JSON brut
        if (payload != null && payload.contains("\"eloDelta\":")) {
            try {
                int idx   = payload.indexOf("\"eloDelta\":") + 11;
                int end   = payload.indexOf(",", idx);
                if (end < 0) end = payload.indexOf("}", idx);
                int delta = Integer.parseInt(payload.substring(idx, end).trim());
                if (delta > 0) eloGainCounter.increment();
                else           eloLossCounter.increment();
            } catch (Exception ignored) {}
        }
    }

    @KafkaListener(topics = KafkaConfig.TOPIC_SERVER_OVERLOADED, groupId = "monitoring-group",
                   containerFactory = "stringKafkaListenerContainerFactory")
    public void onServerOverloaded(ConsumerRecord<String, String> record) {
        serverOverloadedCounter.increment();
        System.out.printf("[Kafka Consumer] ServerOverloaded recu | offset=%d%n", record.offset());
    }
}

package com.example.kafka.producer;

import com.example.kafka.config.KafkaConfig;
import com.example.kafka.event.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class GameEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public GameEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishMatchFinished(MatchFinishedEvent event) {
        kafkaTemplate.send(KafkaConfig.TOPIC_MATCH_FINISHED, event.getMatchId(), event);
        System.out.printf("[Kafka] → match-events | %s mode=%s region=%s%n",
                event.getMatchId(), event.getMode(), event.getRegion());
    }

    public void publishPlayerBanned(PlayerBannedEvent event) {
        kafkaTemplate.send(KafkaConfig.TOPIC_PLAYER_BANNED, event.getPlayerId(), event);
        System.out.printf("[Kafka] → player-banned | %s reason=%s%n",
                event.getUsername(), event.getReason());
    }

    public void publishScoreUpdated(ScoreUpdatedEvent event) {
        kafkaTemplate.send(KafkaConfig.TOPIC_SCORE_UPDATED, event.getPlayerId(), event);
        System.out.printf("[Kafka] → score-events | %s elo %d→%d (delta=%+d)%n",
                event.getUsername(), event.getOldElo(), event.getNewElo(), event.getEloDelta());
    }

    public void publishServerOverloaded(ServerOverloadedEvent event) {
        kafkaTemplate.send(KafkaConfig.TOPIC_SERVER_OVERLOADED, event.getServerId(), event);
        System.out.printf("[Kafka] → server-events | %s load=%.0f%% region=%s%n",
                event.getServerId(), event.getLoad() * 100, event.getRegion());
    }
}

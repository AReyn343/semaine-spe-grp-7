package com.example.simulator;

import com.example.kafka.event.*;
import com.example.kafka.producer.GameEventProducer;
import com.example.metier.entity.Match;
import com.example.metier.entity.PlayerStats;
import com.example.metier.entity.ServerStatus;
import com.example.metier.service.DataLoaderService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class GameSimulator {

    private final DataLoaderService  dataLoader;
    private final GameEventProducer  producer;
    private final Random             random    = new Random();
    private final HttpClient         http      = HttpClient.newHttpClient();

    private final ScheduledExecutorService dataScheduler    = Executors.newSingleThreadScheduledExecutor();
    private final ScheduledExecutorService trafficScheduler = Executors.newSingleThreadScheduledExecutor();

    private static final String BASE_URL = "http://localhost:8080";

    private static final String[] CHAMPIONS = {
        "Ahri","Zed","Jinx","Thresh","Lee Sin","Darius","Lux",
        "Yasuo","Vayne","Blitzcrank","Orianna","Katarina","Ezreal",
        "Morgana","Vi","Caitlyn","Syndra","Jhin","Yone","Garen"
    };
    private static final String[] LANES   = {"TOP","JUNGLE","MID","BOT","SUPPORT"};
    private static final String[] MODES   = {"RANKED_SOLO","RANKED_SOLO","RANKED_SOLO","RANKED_FLEX","NORMAL_DRAFT","ARAM"};
    private static final String[] REGIONS = {"EUW","EUW","EUW","NA","KR"};
    private static final String[] BAN_REASONS = {"Comportement toxique","Triche detectee","Abandon de partie","Spam chat"};

    private static final String[] ENDPOINTS = {
        "/api/v1/dashboard","/api/v1/matches","/api/v1/matches/search?mode=RANKED_SOLO",
        "/api/v1/matches/search?region=EUW","/api/v1/matches/search?status=IN_PROGRESS",
        "/api/v1/players","/api/v1/players/top?limit=5",
        "/api/v1/monitoring/health","/api/v1/monitoring/metrics/current","/api/v1/monitoring/alerts",
    };

    private int tickCount = 0;

    public GameSimulator(DataLoaderService dataLoader, GameEventProducer producer) {
        this.dataLoader = dataLoader;
        this.producer   = producer;
    }

    @PostConstruct
    public void start() {
        dataScheduler.scheduleAtFixedRate(this::tickData,       2, 1,   TimeUnit.SECONDS);
        trafficScheduler.scheduleAtFixedRate(this::tickTraffic, 3, 500, TimeUnit.MILLISECONDS);
        System.out.println("[LoL Simulator] Demarre avec Kafka — tick 1s + trafic 500ms");
    }

    @PreDestroy
    public void stop() {
        dataScheduler.shutdownNow();
        trafficScheduler.shutdownNow();
    }

    private void tickData() {
        try {
            tickCount++;
            updateServers();
            updateMatches();
            updatePlayers();
            if (random.nextInt(20) == 0) simulateBan();
            if (tickCount % 10 == 0) {
                long online = dataLoader.getServers().stream().mapToInt(ServerStatus::getConnectedPlayers).sum();
                long active = dataLoader.getMatches().stream().filter(m -> "IN_PROGRESS".equals(m.getStatus())).count();
                System.out.printf("[LoL Simulator] %d joueurs | %d matchs en cours%n", online, active);
            }
        } catch (Exception e) {
            System.err.println("[LoL Simulator] Erreur tick : " + e.getMessage());
        }
    }

    private void tickTraffic() {
        try {
            int count = 1 + random.nextInt(4);
            for (int i = 0; i < count; i++) callAsync(ENDPOINTS[random.nextInt(ENDPOINTS.length)]);
            if (random.nextInt(15) == 0) callPost("/api/v1/monitoring/alerts/evaluate");
        } catch (Exception ignored) {}
    }

    private void callAsync(String path) {
        try { http.sendAsync(HttpRequest.newBuilder().uri(URI.create(BASE_URL + path)).GET().build(), HttpResponse.BodyHandlers.discarding()); } catch (Exception ignored) {}
    }

    private void callPost(String path) {
        try { http.sendAsync(HttpRequest.newBuilder().uri(URI.create(BASE_URL + path)).POST(HttpRequest.BodyPublishers.noBody()).build(), HttpResponse.BodyHandlers.discarding()); } catch (Exception ignored) {}
    }

    private void updateServers() {
        double timeWave = 0.6 + 0.2 * Math.sin(tickCount * 0.02);
        for (ServerStatus server : dataLoader.getServers()) {
            double newLoad = Math.max(0.15, Math.min(0.98,
                server.getLoad() * 0.85 + timeWave * 0.15 + (random.nextDouble() - 0.5) * 0.08));
            server.setLoad(Math.round(newLoad * 100.0) / 100.0);
            server.setConnectedPlayers(Math.max(1000, server.getConnectedPlayers() + (int)((random.nextDouble() - 0.45) * 2000)));
            server.setActiveMatches(server.getConnectedPlayers() / 10);

            if      (newLoad > 0.95) server.setHealthStatus("DOWN");
            else if (newLoad > 0.88) {
                server.setHealthStatus("DEGRADED");
                if (random.nextInt(2) == 0)
                    producer.publishServerOverloaded(new ServerOverloadedEvent(
                        server.getServerId(), server.getRegion(), newLoad, server.getConnectedPlayers()));
            } else server.setHealthStatus("HEALTHY");
        }
    }

    private void updateMatches() {
        List<Match> matches = dataLoader.getMatches();
        for (Match match : matches) {
            if ("IN_PROGRESS".equals(match.getStatus())) {
                match.setDurationSeconds(match.getDurationSeconds() + 1);
                if (random.nextInt(30) == 0) match.setScoreTeamA(match.getScoreTeamA() + 1);
                if (random.nextInt(30) == 0) match.setScoreTeamB(match.getScoreTeamB() + 1);
                if (match.getDurationSeconds() >= 300 + random.nextInt(300)) {
                    match.setStatus("FINISHED");
                    producer.publishMatchFinished(new MatchFinishedEvent(
                        match.getId(), match.getMode(), match.getRegion(),
                        match.getScoreTeamA(), match.getScoreTeamB(), match.getDurationSeconds()));
                }
            }
        }
        if (random.nextInt(20) == 0) {
            String region = REGIONS[random.nextInt(REGIONS.length)];
            Match m = new Match();
            m.setId(region + "-" + (7000000000L + random.nextInt(999999999)));
            m.setMode(MODES[random.nextInt(MODES.length)]);
            m.setStatus("IN_PROGRESS");
            m.setRegion(region);
            m.setServerId("srv-" + region.toLowerCase() + "-0" + (1 + random.nextInt(2)));
            m.setDurationSeconds(0); m.setScoreTeamA(0); m.setScoreTeamB(0);
            m.setCreatedAt(LocalDateTime.now());
            matches.add(m);
        }
    }

    private void updatePlayers() {
        for (PlayerStats player : dataLoader.getPlayerStats()) {
            player.setPlayTimeSeconds(player.getPlayTimeSeconds() + 1);
            if (random.nextInt(60) == 0) player.setKills(Math.max(0, player.getKills() + 1));
            if (random.nextInt(90) == 0) player.setDeaths(Math.max(0, player.getDeaths() + 1));
            if (random.nextInt(45) == 0) player.setAssists(Math.max(0, player.getAssists() + 1));
            if (random.nextInt(2)  == 0) player.setCs(player.getCs() + 1);
            if (random.nextInt(30) == 0) player.setVisionScore(player.getVisionScore() + 1);
            player.setDamageDealt(Math.max(0, player.getDamageDealt() + random.nextInt(500) - 100));

            double kda = (player.getKills() + player.getAssists()) / (double) Math.max(1, player.getDeaths());
            player.setAverageScore(Math.round(kda * 10.0) / 10.0);

            int oldElo   = player.getElo();
            int eloDelta = (int)((kda / 3.0 - 1.0) * 8) + random.nextInt(5) - 2;
            int newElo   = Math.max(0, oldElo + eloDelta);
            player.setElo(newElo);

            if (random.nextInt(5) == 0 && eloDelta != 0)
                producer.publishScoreUpdated(new ScoreUpdatedEvent(
                    player.getPlayerId(), player.getUsername(), oldElo, newElo, "sim-" + tickCount));

            player.setWinRate(Math.max(0.1, Math.min(0.99,
                Math.round((player.getWinRate() + (random.nextDouble() - 0.5) * 0.005) * 1000.0) / 1000.0)));

            if (random.nextInt(120) == 0) {
                player.setChampion(CHAMPIONS[random.nextInt(CHAMPIONS.length)]);
                player.setLane(LANES[random.nextInt(LANES.length)]);
                player.setKills(0); player.setDeaths(0); player.setAssists(0);
                player.setCs(0); player.setDamageDealt(0);
            }
        }
    }

    private void simulateBan() {
        List<PlayerStats> players = dataLoader.getPlayerStats();
        if (players.isEmpty()) return;
        PlayerStats target = players.get(random.nextInt(players.size()));
        producer.publishPlayerBanned(new PlayerBannedEvent(
            target.getPlayerId(), target.getUsername(),
            BAN_REASONS[random.nextInt(BAN_REASONS.length)], 1 + random.nextInt(72)));
    }
}

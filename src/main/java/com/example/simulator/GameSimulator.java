package com.example.simulator;

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

/**
 * Simulateur League of Legends.
 * Simule l'activité réelle d'une plateforme LoL :
 * - Matchs ranked/normal qui progressent en temps réel
 * - Stats joueurs (K/D/A, CS, vision) qui évoluent
 * - Charge serveurs par région (EUW, NA, KR)
 * - Pics de connexion simulés (soirée EU, nuit NA...)
 * - Trafic HTTP généré pour animer Grafana
 */
@Component
public class GameSimulator {

    private final DataLoaderService dataLoader;
    private final Random random = new Random();
    private final HttpClient http = HttpClient.newHttpClient();

    private final ScheduledExecutorService dataScheduler    = Executors.newSingleThreadScheduledExecutor();
    private final ScheduledExecutorService trafficScheduler = Executors.newSingleThreadScheduledExecutor();

    private static final String BASE = "http://localhost:8080";

    // ── Données LoL ───────────────────────────────────────────────

    private static final String[] CHAMPIONS = {
        "Ahri", "Zed", "Jinx", "Thresh", "Lee Sin", "Darius", "Lux",
        "Yasuo", "Vayne", "Blitzcrank", "Orianna", "Katarina", "Ezreal",
        "Morgana", "Vi", "Caitlyn", "Syndra", "Jhin", "Yone", "Garen"
    };

    private static final String[] LANES   = {"TOP", "JUNGLE", "MID", "BOT", "SUPPORT"};
    private static final String[] MODES   = {"RANKED_SOLO", "RANKED_SOLO", "RANKED_SOLO", "RANKED_FLEX", "NORMAL_DRAFT", "ARAM"};
    private static final String[] REGIONS = {"EUW", "EUW", "EUW", "NA", "KR"};
    private static final String[] TIERS   = {"IRON", "BRONZE", "SILVER", "GOLD", "PLATINUM", "DIAMOND", "MASTER", "CHALLENGER"};

    private static final String[] ENDPOINTS = {
        "/api/dashboard",
        "/api/matches",
        "/api/matches/search?mode=RANKED_SOLO",
        "/api/matches/search?region=EUW",
        "/api/matches/search?status=IN_PROGRESS",
        "/api/players",
        "/api/players/top?limit=5",
        "/api/monitoring/health",
        "/api/monitoring/metrics/current",
        "/api/monitoring/alerts",
    };

    private int tickCount = 0;

    public GameSimulator(DataLoaderService dataLoader) {
        this.dataLoader = dataLoader;
    }

    @PostConstruct
    public void start() {
        dataScheduler.scheduleAtFixedRate(this::tickData,       2, 1,   TimeUnit.SECONDS);
        trafficScheduler.scheduleAtFixedRate(this::tickTraffic, 3, 500, TimeUnit.MILLISECONDS);
        System.out.println("[LoL Simulator] Démarré — simulation League of Legends");
    }

    @PreDestroy
    public void stop() {
        dataScheduler.shutdownNow();
        trafficScheduler.shutdownNow();
    }

    // ── Tick données ──────────────────────────────────────────────

    private void tickData() {
        try {
            tickCount++;
            updateServers();
            updateMatches();
            updatePlayers();

            // Log périodique toutes les 10s
            if (tickCount % 10 == 0) {
                long online  = dataLoader.getServers().stream().mapToInt(ServerStatus::getConnectedPlayers).sum();
                long active  = dataLoader.getMatches().stream().filter(m -> "IN_PROGRESS".equals(m.getStatus())).count();
                System.out.printf("[LoL Simulator] %d joueurs connectés | %d matchs en cours%n", online, active);
            }
        } catch (Exception e) {
            System.err.println("[LoL Simulator] Erreur données : " + e.getMessage());
        }
    }

    // ── Tick trafic HTTP ──────────────────────────────────────────

    private void tickTraffic() {
        try {
            int count = 1 + random.nextInt(4);
            for (int i = 0; i < count; i++) {
                callAsync(ENDPOINTS[random.nextInt(ENDPOINTS.length)]);
            }
            if (random.nextInt(15) == 0) callPost("/api/monitoring/alerts/evaluate");
        } catch (Exception e) {
            System.err.println("[LoL Simulator] Erreur trafic : " + e.getMessage());
        }
    }

    private void callAsync(String path) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(BASE + path)).GET().build();
            http.sendAsync(req, HttpResponse.BodyHandlers.discarding());
        } catch (Exception ignored) {}
    }

    private void callPost(String path) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(BASE + path))
                    .POST(HttpRequest.BodyPublishers.noBody()).build();
            http.sendAsync(req, HttpResponse.BodyHandlers.discarding());
        } catch (Exception ignored) {}
    }

    // ── Mise à jour serveurs (pic soirée EU simulé) ───────────────

    private void updateServers() {
        // Simulation d'un pic soirée : charge oscille entre 0.4 et 0.95
        double timeWave = 0.6 + 0.2 * Math.sin(tickCount * 0.02);

        for (ServerStatus server : dataLoader.getServers()) {
            double base  = timeWave + (random.nextDouble() - 0.5) * 0.15;
            double noise = (random.nextDouble() - 0.5) * 0.08;
            double newLoad = Math.max(0.15, Math.min(0.98, server.getLoad() * 0.85 + base * 0.15 + noise));
            server.setLoad(Math.round(newLoad * 100.0) / 100.0);

            // Joueurs : varie selon la région (KR = beaucoup, NA = moins la nuit)
            int baseVariation = server.getRegion().equals("KR") ? 3000 : 2000;
            int playerDelta   = (int)((random.nextDouble() - 0.45) * baseVariation);
            int newPlayers    = Math.max(1000, server.getConnectedPlayers() + playerDelta);
            server.setConnectedPlayers(newPlayers);

            // Matchs actifs proportionnels aux joueurs (10 joueurs/match en moyenne)
            server.setActiveMatches(newPlayers / 10);

            // Santé
            if      (newLoad > 0.95) server.setHealthStatus("DOWN");
            else if (newLoad > 0.88) server.setHealthStatus("DEGRADED");
            else                      server.setHealthStatus("HEALTHY");
        }
    }

    // ── Mise à jour matchs LoL ────────────────────────────────────

    private void updateMatches() {
        List<Match> matches = dataLoader.getMatches();

        for (Match match : matches) {
            if ("IN_PROGRESS".equals(match.getStatus())) {
                match.setDurationSeconds(match.getDurationSeconds() + 1);

                // Kills toutes les ~30s en moyenne
                if (random.nextInt(30) == 0) match.setScoreTeamA(match.getScoreTeamA() + 1);
                if (random.nextInt(30) == 0) match.setScoreTeamB(match.getScoreTeamB() + 1);

                // Durée typique LoL : 25-40 min (1500-2400s)
                int targetDuration = 1500 + random.nextInt(900);
                if (match.getDurationSeconds() >= targetDuration) {
                    match.setStatus("FINISHED");
                }
            }
        }

        // Nouveau match toutes les ~20s en moyenne (5% / tick)
        if (random.nextInt(20) == 0) {
            String region  = REGIONS[random.nextInt(REGIONS.length)];
            String mode    = MODES[random.nextInt(MODES.length)];
            long   gameNum = 7000000000L + random.nextInt(999999999);

            Match m = new Match();
            m.setId(region + "-" + gameNum);
            m.setMode(mode);
            m.setStatus("IN_PROGRESS");
            m.setRegion(region);
            m.setServerId("srv-" + region.toLowerCase() + "-0" + (1 + random.nextInt(2)));
            m.setDurationSeconds(0);
            m.setScoreTeamA(0);
            m.setScoreTeamB(0);
            m.setCreatedAt(LocalDateTime.now());
            matches.add(m);
        }
    }

    // ── Mise à jour stats joueurs LoL ─────────────────────────────

    private void updatePlayers() {
        for (PlayerStats player : dataLoader.getPlayerStats()) {
            player.setPlayTimeSeconds(player.getPlayTimeSeconds() + 1);

            // KDA qui évolue (kills, deaths, assists)
            if (random.nextInt(60) == 0) player.setKills(Math.max(0, player.getKills() + 1));
            if (random.nextInt(90) == 0) player.setDeaths(Math.max(0, player.getDeaths() + 1));
            if (random.nextInt(45) == 0) player.setAssists(Math.max(0, player.getAssists() + 1));

            // CS (creep score) +1 toutes les ~2s
            if (random.nextInt(2) == 0) player.setCs(player.getCs() + 1);

            // Vision score augmente lentement
            if (random.nextInt(30) == 0) player.setVisionScore(player.getVisionScore() + 1);

            // Dégâts infligés
            int damageDelta = random.nextInt(500) - 100;
            player.setDamageDealt(Math.max(0, player.getDamageDealt() + damageDelta));

            // KDA score (kills + assists) / max(deaths, 1)
            double kda = (player.getKills() + player.getAssists()) / (double) Math.max(1, player.getDeaths());
            player.setAverageScore(Math.round(kda * 10.0) / 10.0);

            // ELO : gain/perte selon les performances
            double performance = kda / 3.0; // KDA 3.0 = neutre
            int eloDelta = (int)((performance - 1.0) * 8) + random.nextInt(5) - 2;
            player.setElo(Math.max(0, player.getElo() + eloDelta));

            // Win rate légère variation
            double wrDelta = (random.nextDouble() - 0.5) * 0.005;
            double newWr   = Math.max(0.1, Math.min(0.99, player.getWinRate() + wrDelta));
            player.setWinRate(Math.round(newWr * 1000.0) / 1000.0);

            // Champion aléatoire de temps en temps (nouveau match)
            if (random.nextInt(120) == 0) {
                player.setChampion(CHAMPIONS[random.nextInt(CHAMPIONS.length)]);
                player.setLane(LANES[random.nextInt(LANES.length)]);
                player.setKills(0);
                player.setDeaths(0);
                player.setAssists(0);
                player.setCs(0);
                player.setDamageDealt(0);
            }
        }
    }
}

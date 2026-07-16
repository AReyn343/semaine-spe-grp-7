package com.example.metier.service;

import com.example.metier.entity.Match;
import com.example.metier.entity.PlayerStats;
import com.example.metier.entity.ServerStatus;
import com.example.util.JsonLoader;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Charge les datasets JSON au démarrage de l'application.
 * Toutes les données sont gardées en mémoire (pas de DB pour la partie métier).
 */
@Service
public class DataLoaderService {

    private final JsonLoader jsonLoader;

    private List<Match>        matches;
    private List<PlayerStats>  playerStats;
    private List<ServerStatus> servers;

    public DataLoaderService(JsonLoader jsonLoader) {
        this.jsonLoader = jsonLoader;
    }

    @PostConstruct
    public void load() {
        matches     = jsonLoader.loadList("matches.json",      Match.class);
        playerStats = jsonLoader.loadList("player-stats.json", PlayerStats.class);
        servers     = jsonLoader.loadList("servers.json",      ServerStatus.class);

        System.out.printf("[DataLoader] %d matchs | %d joueurs | %d serveurs chargés%n",
                matches.size(), playerStats.size(), servers.size());
    }

    public List<Match>        getMatches()     { return matches; }
    public List<PlayerStats>  getPlayerStats() { return playerStats; }
    public List<ServerStatus> getServers()     { return servers; }
}

package com.example.metier.dao;

import com.example.metier.entity.Match;
import com.example.util.JsonLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

// import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Repository
public class MatchRepository {

    private final List<Match> matches = new CopyOnWriteArrayList<>();
    private final JsonLoader<Match> jsonLoader;

    @Value("${app.data.directory:./data}")
    private String dataDirectory;

    public MatchRepository(JsonLoader<Match> jsonLoader) {
        this.jsonLoader = jsonLoader;
    }

    @PostConstruct
    public void init() {
        // Charger les données depuis le fichier JSON au démarrage
        List<Match> loadedMatches = jsonLoader.loadFromFile(dataDirectory + "/matches.json", Match.class);
        matches.addAll(loadedMatches);
    }

    public List<Match> findAll() {
        return new ArrayList<>(matches);
    }

    public Optional<Match> findById(String id) {
        return matches.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst();
    }

    public Match save(Match match) {
        // Vérifier si le match existe déjà
        Optional<Match> existing = findById(match.getId());
        if (existing.isPresent()) {
            // Mise à jour
            matches.remove(existing.get());
            matches.add(match);
        } else {
            // Ajout
            matches.add(match);
        }

        // Sauvegarder dans le fichier JSON
        jsonLoader.saveToFile(dataDirectory + "/matches.json", matches);
        return match;
    }

    public List<Match> findByStatus(String status) {
        return matches.stream()
                .filter(m -> status.equals(m.getStatus()))
                .collect(Collectors.toList());
    }

    public List<Match> findByRegion(String region) {
        return matches.stream()
                .filter(m -> region.equals(m.getRegion()))
                .collect(Collectors.toList());
    }

    public List<Match> findByMode(String mode) {
        return matches.stream()
                .filter(m -> mode.equals(m.getMode()))
                .collect(Collectors.toList());
    }

    public void deleteById(String id) {
        matches.removeIf(m -> m.getId().equals(id));
        jsonLoader.saveToFile(dataDirectory + "/matches.json", matches);
    }
}
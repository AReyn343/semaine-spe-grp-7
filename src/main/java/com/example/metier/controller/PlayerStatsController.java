package com.example.metier.controller;

import com.example.metier.dto.PlayerStatsDto;
import com.example.metier.service.PlayerStatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerStatsController {

    private final PlayerStatsService playerStatsService;

    public PlayerStatsController(PlayerStatsService playerStatsService) {
        this.playerStatsService = playerStatsService;
    }

    /** GET /api/players — tous les joueurs */
    @GetMapping
    public List<PlayerStatsDto> getAll() {
        return playerStatsService.findAll();
    }

    /** GET /api/players/{id}/stats — stats d'un joueur */
    @GetMapping("/{id}/stats")
    public ResponseEntity<PlayerStatsDto> getStats(@PathVariable String id) {
        return playerStatsService.findByPlayerId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** GET /api/players/top?limit=5 — classement ELO */
    @GetMapping("/top")
    public List<PlayerStatsDto> top(@RequestParam(defaultValue = "5") int limit) {
        return playerStatsService.topByElo(limit);
    }
}

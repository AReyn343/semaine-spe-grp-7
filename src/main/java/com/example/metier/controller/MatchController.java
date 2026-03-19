package com.example.metier.controller;

import com.example.metier.dto.MatchDto;
import com.example.metier.dto.MatchFilterDto;
import com.example.metier.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    /** GET /api/matches — tous les matchs */
    @GetMapping
    public List<MatchDto> getAll() {
        return matchService.findAll();
    }

    /** GET /api/matches/{id} — un match par ID */
    @GetMapping("/{id}")
    public ResponseEntity<MatchDto> getById(@PathVariable String id) {
        return matchService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/matches/search
     * Paramètres optionnels : mode, status, region, minDuration, maxDuration
     * Exemple : /api/matches/search?mode=RANKED&region=EU
     */
    @GetMapping("/search")
    public List<MatchDto> search(
            @RequestParam(required = false) String mode,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Integer minDuration,
            @RequestParam(required = false) Integer maxDuration) {

        MatchFilterDto filter = new MatchFilterDto();
        filter.setMode(mode);
        filter.setStatus(status);
        filter.setRegion(region);
        filter.setMinDuration(minDuration);
        filter.setMaxDuration(maxDuration);

        return matchService.search(filter);
    }

    /** GET /api/matches/stats — métriques agrégées */
    @GetMapping("/stats")
    public ResponseEntity<?> stats() {
        return ResponseEntity.ok(java.util.Map.of(
                "total",           matchService.countTotal(),
                "active",          matchService.countActive(),
                "avgDurationSecs", Math.round(matchService.averageDurationSeconds())
        ));
    }
}

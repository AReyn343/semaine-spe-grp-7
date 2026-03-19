# Groupe 7 — Microservice Monitoring Jeu Vidéo

## Démarrage

```bash
docker compose up --build -d
```

## URLs disponibles

| Service        | URL                                          | Auth         |
|----------------|----------------------------------------------|--------------|
| Dashboard      | http://localhost:8080/api/dashboard          | —            |
| Matchs         | http://localhost:8080/api/matches            | —            |
| Joueurs top 5  | http://localhost:8080/api/players/top        | —            |
| Health         | http://localhost:8080/api/monitoring/health  | —            |
| Alertes        | http://localhost:8080/api/monitoring/alerts  | —            |
| Métriques      | http://localhost:8080/api/monitoring/metrics/catalog | —  |
| Prometheus     | http://localhost:8080/actuator/prometheus    | —            |
| Grafana        | http://localhost:3000                        | admin/admin  |
| phpMyAdmin     | http://localhost:8081                        | root/root    |
| Prometheus UI  | http://localhost:9090                        | —            |

## Évaluer les alertes manuellement

```bash
curl -X POST http://localhost:8080/api/monitoring/alerts/evaluate
```

## Recherche de matchs filtrée

```bash
curl "http://localhost:8080/api/matches/search?mode=RANKED&region=EU"
```

## Architecture

```
metier/       → simulation backend jeu (données JSON)
monitoring/   → supervision générique (Micrometer + alertes)
securite/     → Spring Security
util/         → JsonLoader, MapperUtil
```

## Répartition
- **Momo** : entités + datasets JSON + utils
- **Mimi** : services metier + controllers + DTOs
- **Lala** : monitoring + Micrometer + Prometheus + Grafana
- **Vivi** : docker-compose + intégration finale

# Groupe 7 — Microservice Monitoring Jeu Vidéo (LoL)

## Démarrage standard (1 instance)

```bash
docker compose up --build -d
```

## Démarrage avec scaling (3 instances app)

```bash
docker compose up --build --scale app=3 -d
```

## Vérifier le scaling

```bash
# Voir les instances tournantes
docker compose ps

# Voir la répartition Nginx en temps réel
docker compose logs nginx -f

# Voir les logs de toutes les instances app
docker compose logs app -f
```

## Scaler à chaud (sans rebuild)

```bash
# Passer à 5 instances
docker compose up --scale app=5 -d --no-recreate

# Réduire à 1 instance
docker compose up --scale app=1 -d --no-recreate
```

## URLs

| Service        | URL                                          | Auth         |
|----------------|----------------------------------------------|--------------|
| API (via Nginx)| http://localhost:8080/api/v1/dashboard       | —            |
| Auth login     | POST http://localhost:8080/api/v1/auth/login | JSON body    |
| Prometheus     | http://localhost:9090                        | —            |
| Grafana        | http://localhost:3000                        | admin/admin  |
| phpMyAdmin     | http://localhost:8081                        | root/root    |

## Tester le JWT

```bash
# Obtenir un token
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Utiliser le token
curl http://localhost:8080/api/v1/auth/me \
  -H "Authorization: Bearer <token>"
```

## Réinitialiser Grafana (si problème de mot de passe)

```bash
docker compose down
docker volume rm semaine-spe-grp-7_grafana_data
docker compose up -d
```

## Répartition des tâches

- **Momo** : entités métier, datasets JSON LoL, DDD
- **Mimi** : API REST v1, JWT, SecurityConfig
- **Lala** : monitoring Micrometer, Prometheus, Grafana
- **Vivi** : Docker, Nginx, scaling, intégration finale

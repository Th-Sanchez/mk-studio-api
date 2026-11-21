# mk-studio-api

API REST do **MK STUDIO** construída em **Java 25 (LTS) + Spring Boot 4.1.1 + Maven + PostgreSQL + Flyway**.

## Pré-requisitos
- Docker Desktop (para PostgreSQL local)
- Java 25 (LTS) + Maven (ou IntelliJ IDEA com JDK 25 configurado)

## Como rodar localmente

1. Subir o PostgreSQL:
```bash
docker compose up -d
```

2. (Opcional) Configurar variáveis de ambiente com base em `.env.example` — em desenvolvimento local, o `application.yml` já possui valores padrão funcionais.

3. Rodar a aplicação:
```bash
mvn spring-boot:run
```

4. Verificar saúde da API:
```text
GET http://localhost:8080/api/health
```

# AI Agent Changelog

> This file is written **automatically** by `.agents/hooks/ai-log.sh` (repository hooks for
> Claude Code and Cursor; explicit commands for every other agent — see `AGENTS.md`).
>
> Purpose: let the evaluators distinguish the candidate's own work from work delegated to AI.
>
> - **Append-only.** Never edit, reorder or delete existing entries. This is audited from the git history.
> - **All entries must be written in English.**
> - Never write secrets or personal data here.

---

## Entry format

One block per agent turn, one row per changed file:

```
---
### Turn YYYY-MM-DD HH:MM:SS ±HHMM · <Agent>
- **Source**: hook | command | manual
- **Session**: <agent session id or n/a>
- **Prompt**: <user prompt, truncated, secrets masked>
- **Summary**: <one-line summary written by the agent>

| File | Change | Additions | Removals |
|---|---|---|---|
| `relative/path/to/file` | created / modified / deleted | +N | -N |
```

`Source: manual` is only for entries appended by hand when the logger cannot run.

---

## Change log

<!-- AI agent entries will be inserted below this line -->

---
### Turn 2026-10-21 19:39:17 -0300 · Other-ChatGPT
- **Source**: command
- **Session**: n/a
- **Prompt**: n/a
- **Summary**: n/a
- **Note**: turn closed by the pre-commit hook (agent did not run record before the commit)

| File | Change | Additions | Removals |
|---|---|---|---|
| `backend/src/main/resources/application.properties` | modified | +7 | -0 |
| `docker-compose.yml` | created | +15 | -0 |

---
### Turn 2026-10-22 02:02:41 -0300 · Other-ChatGPT
- **Source**: command
- **Session**: n/a
- **Prompt**: n/a
- **Summary**: Modeled users, purchase requests, approval levels, state enums and request history entities

| File | Change | Additions | Removals |
|---|---|---|---|
| `backend/src/main/java/com/murilo/backend/domain/enums/ApprovalLevel.java` | created | +7 | -0 |
| `backend/src/main/java/com/murilo/backend/domain/enums/ApproverLevel.java` | created | +6 | -0 |
| `backend/src/main/java/com/murilo/backend/domain/enums/HistoryAction.java` | created | +8 | -0 |
| `backend/src/main/java/com/murilo/backend/domain/enums/RequestCategory.java` | created | +10 | -0 |
| `backend/src/main/java/com/murilo/backend/domain/enums/RequestStatus.java` | created | +8 | -0 |
| `backend/src/main/java/com/murilo/backend/domain/enums/Role.java` | created | +7 | -0 |
| `backend/src/main/java/com/murilo/backend/domain/model/PurchaseRequest.java` | created | +135 | -0 |
| `backend/src/main/java/com/murilo/backend/domain/model/RequestHistory.java` | created | +76 | -0 |
| `backend/src/main/java/com/murilo/backend/domain/model/User.java` | created | +82 | -0 |

---
### Turn 2026-10-22 04:08:22 -0300 · Other-ChatGPT
- **Source**: command
- **Session**: n/a
- **Prompt**: n/a
- **Summary**: Added Spring Data repositories and JWT dependency

| File | Change | Additions | Removals |
|---|---|---|---|
| `backend/pom.xml` | modified | +7 | -0 |
| `backend/src/main/java/com/murilo/backend/repository/PurchaseRequestRepository.java` | created | +30 | -0 |
| `backend/src/main/java/com/murilo/backend/repository/RequestHistoryRepository.java` | created | +12 | -0 |
| `backend/src/main/java/com/murilo/backend/repository/UserRepository.java` | created | +13 | -0 |

---
### Turn 2026-10-22 16:04:15 -0300 · Other-ChatGPT
- **Source**: command
- **Session**: n/a
- **Prompt**: n/a
- **Summary**: Implemented JWT authentication with registration, login, BCrypt password hashing, protected current-user endpoint and correct 401/403 security handling

| File | Change | Additions | Removals |
|---|---|---|---|
| `backend/src/main/java/com/murilo/backend/config/SecurityConfig.java` | created | +82 | -0 |
| `backend/src/main/java/com/murilo/backend/controller/AuthController.java` | created | +47 | -0 |
| `backend/src/main/java/com/murilo/backend/domain/model/User.java` | modified | +1 | -1 |
| `backend/src/main/java/com/murilo/backend/dto/auth/AuthResponse.java` | created | +9 | -0 |
| `backend/src/main/java/com/murilo/backend/dto/auth/LoginRequest.java` | created | +16 | -0 |
| `backend/src/main/java/com/murilo/backend/dto/auth/RegisterRequest.java` | created | +29 | -0 |
| `backend/src/main/java/com/murilo/backend/dto/auth/UserResponse.java` | created | +26 | -0 |
| `backend/src/main/java/com/murilo/backend/security/JwtAuthenticationFilter.java` | created | +85 | -0 |
| `backend/src/main/java/com/murilo/backend/security/JwtService.java` | created | +51 | -0 |
| `backend/src/main/java/com/murilo/backend/service/AuthService.java` | created | +111 | -0 |
| `backend/src/main/resources/application.properties` | modified | +4 | -1 |

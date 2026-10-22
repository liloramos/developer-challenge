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

---
### Turn 2026-10-22 17:21:38 -0300 · Other-ChatGPT
- **Source**: command
- **Session**: n/a
- **Prompt**: n/a
- **Summary**: Implemented purchase request workflow with approval levels, state transitions, role authorization, pagination, filtering, request history and centralized API error handling

| File | Change | Additions | Removals |
|---|---|---|---|
| `backend/src/main/java/com/murilo/backend/controller/PurchaseRequestController.java` | created | +162 | -0 |
| `backend/src/main/java/com/murilo/backend/domain/enums/ApprovalLevel.java` | modified | +2 | -2 |
| `backend/src/main/java/com/murilo/backend/domain/enums/HistoryAction.java` | modified | +1 | -1 |
| `backend/src/main/java/com/murilo/backend/dto/common/ApiErrorResponse.java` | created | +14 | -0 |
| `backend/src/main/java/com/murilo/backend/dto/common/ApiResponse.java` | created | +7 | -0 |
| `backend/src/main/java/com/murilo/backend/dto/request/CreatePurchaseRequestRequest.java` | created | +31 | -0 |
| `backend/src/main/java/com/murilo/backend/dto/request/PurchaseRequestResponse.java` | created | +40 | -0 |
| `backend/src/main/java/com/murilo/backend/dto/request/RequestActionRequest.java` | created | +13 | -0 |
| `backend/src/main/java/com/murilo/backend/dto/request/RequestHistoryResponse.java` | created | +27 | -0 |
| `backend/src/main/java/com/murilo/backend/exception/BusinessRuleException.java` | created | +8 | -0 |
| `backend/src/main/java/com/murilo/backend/exception/ForbiddenException.java` | created | +8 | -0 |
| `backend/src/main/java/com/murilo/backend/exception/GlobalExceptionHandler.java` | created | +168 | -0 |
| `backend/src/main/java/com/murilo/backend/exception/ResourceNotFoundException.java` | created | +8 | -0 |
| `backend/src/main/java/com/murilo/backend/repository/RequestHistoryRepository.java` | modified | +3 | -1 |
| `backend/src/main/java/com/murilo/backend/service/PurchaseRequestService.java` | created | +386 | -0 |

---
### Turn 2026-10-22 18:10:43 -0300 · Other-ChatGPT
- **Source**: command
- **Session**: n/a
- **Prompt**: n/a
- **Summary**: Built React frontend foundation with JWT login, purchase request listing, status filtering, pagination, new request form, Kingspan-inspired styling and backend CORS configuration

| File | Change | Additions | Removals |
|---|---|---|---|
| `backend/src/main/java/com/murilo/backend/config/SecurityConfig.java` | modified | +43 | -1 |
| `frontend/.gitignore` | created | +24 | -0 |
| `frontend/README.md` | created | +16 | -0 |
| `frontend/eslint.config.js` | created | +21 | -0 |
| `frontend/index.html` | created | +13 | -0 |
| `frontend/package-lock.json` | created | +2505 | -0 |
| `frontend/package.json` | created | +28 | -0 |
| `frontend/public/favicon.svg` | created | +1 | -0 |
| `frontend/public/icons.svg` | created | +24 | -0 |
| `frontend/src/App.css` | created | +184 | -0 |
| `frontend/src/App.jsx` | created | +49 | -0 |
| `frontend/src/api/api.js` | created | +40 | -0 |
| `frontend/src/assets/hero.png` | created (binary) | n/a | n/a |
| `frontend/src/assets/react.svg` | created | +1 | -0 |
| `frontend/src/assets/vite.svg` | created | +1 | -0 |
| `frontend/src/components/StatusBadge.jsx` | created | +16 | -0 |
| `frontend/src/index.css` | created | +1239 | -0 |
| `frontend/src/main.jsx` | created | +13 | -0 |
| `frontend/src/pages/LoginPage.jsx` | created | +154 | -0 |
| `frontend/src/pages/NewRequestPage.jsx` | created | +280 | -0 |
| `frontend/src/pages/RequestsPage.jsx` | created | +348 | -0 |
| `frontend/vite.config.js` | created | +7 | -0 |

---
### Turn 2026-10-22 19:19:27 -0300 · Other-ChatGPT
- **Source**: command
- **Session**: n/a
- **Prompt**: n/a
- **Summary**: Completed purchase request detail workflow with request history, contextual approval actions, cancellation, approval-level UI rules, protected routes and final login styling

| File | Change | Additions | Removals |
|---|---|---|---|
| `frontend/src/App.jsx` | modified | +15 | -3 |
| `frontend/src/components/ProtectedRoute.jsx` | created | +20 | -0 |
| `frontend/src/index.css` | modified | +591 | -23 |
| `frontend/src/pages/LoginPage.jsx` | modified | +0 | -2 |
| `frontend/src/pages/RequestDetailPage.jsx` | created | +556 | -0 |

---
### Turn 2026-10-22 20:19:58 -0300 · Other-ChatGPT
- **Source**: command
- **Session**: n/a
- **Prompt**: n/a
- **Summary**: Documented the solution architecture, business rules, execution instructions, validation scenarios and transparent AI usage

| File | Change | Additions | Removals |
|---|---|---|---|
| `AI_USAGE.md` | created | +45 | -0 |
| `SOLUTION.md` | created | +447 | -0 |

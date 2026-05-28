# BetterBlueprint

A desktop health tracking application that logs daily metrics, computes personalized health scores, and generates AI-driven wellness insights via the Gemini API. Built as a final project for CSC207 (Software Design) at the University of Toronto by a team of six.

**Team:** Danni Luo, Akshar Patel, Parthiv Paul, Hetvi Soni, Daniel Yap, Rachel Zhu

[![Java](https://img.shields.io/badge/Java-11-orange?logo=openjdk)](https://openjdk.org/)
[![Maven](https://img.shields.io/badge/Maven-3.6%2B-C71A36?logo=apachemaven)](https://maven.apache.org/)
[![JUnit](https://img.shields.io/badge/JUnit-5.8.1-25A162?logo=junit5)](https://junit.org/junit5/)
[![License](https://img.shields.io/badge/license-MIT-blue)](#license)

---

## What It Is

Most health apps collect data without telling users what to do with it. BetterBlueprint closes that gap: users log five daily metrics (sleep, steps, water, exercise, calories), and the application computes a 0-100 health score and sends those metrics to Gemini to generate concrete, personalized feedback. A separate goals module applies the Mifflin-St Jeor basal metabolic rate equation to produce a daily calorie target calibrated to the user's current weight, target weight, and timeframe.

The architecture follows Clean Architecture strictly. Business logic in the use case layer has zero coupling to Swing, file I/O, or HTTP. Every external system (CSV persistence, JSON persistence, Gemini API) sits behind an interface, making each use case independently testable with mock implementations.

---

## Features

- **Health score calculation** -- five weighted metrics (sleep, steps, water intake, exercise, calories) produce a single 0-100 daily score. The score is persisted to JSON alongside the raw metrics.
- **Gemini AI feedback** -- the score and raw metrics are sent to the Gemini API via an async `SwingWorker` call, preventing UI freezes during the network round-trip. The response is streamed back to the presenter as plain text.
- **BMR-based goal setting** -- `GoalsInteractor` implements the Mifflin-St Jeor equation. Given age, height, current weight, target weight, and a timeframe in days, it calculates daily maintenance calories and the deficit or surplus needed to hit the target.
- **Health history view** -- retrieves all persisted metrics for the current user by date, renders them in a scrollable panel, and computes per-metric averages across the selected period.
- **AI health insights** -- `HealthInsightsInteractor` aggregates the user's historical averages and passes them to Gemini with a structured prompt, returning trend-aware recommendations rather than single-day observations.
- **Settings and password management** -- users can update any combination of age, height, and weight without supplying all three. The settings use case validates each field independently and reports only the fields that changed.
- **Multi-user session handling** -- `AppBuilder.resetUserViewModels()` wipes all ViewModels on logout, preventing data leakage between sessions in a shared-machine scenario.

---

## Tech Stack

| Layer | Technology | Why |
|-------|-----------|-----|
| Language | Java 11 | Strong static typing catches null and type errors at compile time; Clean Architecture interfaces map naturally to Java's interface/implementation model |
| UI | Java Swing, `CardLayout` | No browser or server infrastructure needed for a desktop course project; `CardLayout` gives single-panel view switching without managing multiple windows |
| Build | Maven 3.6+ | Reproducible dependency resolution and a standard project layout that every team member's IDE understands without configuration |
| HTTP | OkHttp 4.12.0 | Handles connection pooling, timeouts (30 s), and async callbacks in fewer lines than `HttpURLConnection`; the callback API maps cleanly onto the presenter pattern |
| JSON | org.json 20240303 | Lightweight with no reflection overhead; health metrics serialise and deserialise with explicit field access, making the data shape visible in code |
| AI | Google Gemini API (REST, no SDK) | Free tier sufficient for course use; accessed directly over HTTP via OkHttp to avoid adding a vendor SDK dependency |
| Testing | JUnit Jupiter 5.8.1 | Nested test class support and `@BeforeEach` lifecycle hooks reduce mock-wiring boilerplate per test method |
| Persistence | CSV (users), JSON (health metrics) | No database infrastructure to install; the two formats reflect their data shapes: users are tabular rows, health metrics are structured daily objects |

---

## Architecture

BetterBlueprint follows Clean Architecture, dividing the codebase into four concentric layers. Dependencies point inward only: the UI knows about use cases, use cases know about entities, and nothing in the inner layers references Swing, file I/O, or HTTP.

```
┌─────────────────────────────────────────────────────────┐
│  View Layer (Swing)                                     │
│  LoginView, SignupView, DashboardView, MetricsInputView │
│  GoalsView, HealthInsightsView, HealthHistoryView       │
│  SettingsView, ChangePasswordView                       │
├─────────────────────────────────────────────────────────┤
│  Interface Adapter Layer                                │
│  Controllers  ── translate UI events into InputData    │
│  Presenters   ── translate OutputData into ViewModels  │
├─────────────────────────────────────────────────────────┤
│  Use Case Layer (business logic, framework-free)        │
│  LoginInteractor       SignupInteractor                 │
│  InputMetricsInteractor  DailyHealthScoreInteractor     │
│  GoalsInteractor         HealthInsightsInteractor       │
│  HealthHistoryInteractor SettingsInteractor             │
│  ChangePasswordInteractor                               │
├─────────────────────────────────────────────────────────┤
│  Entity Layer                                           │
│  User (username, password, age, height, weight)        │
│  HealthMetrics (date, sleep, steps, water, exercise,   │
│                 calories)                               │
└─────────────────────────────────────────────────────────┘
         |                    |                  |
         v                    v                  v
  FileUserDataAccessObject  HealthMetrics    GeminiAPIService
  (CSV, per-user rows)      DataAccessObject  (OkHttp REST,
                            (JSON, per-date)   SwingWorker async)
```

`AppBuilder` (`app/AppBuilder.java`, 366 lines) is the composition root. It constructs every data access object, service, use case interactor, presenter, controller, and view, wires them together, and exposes the assembled `JFrame`. No class outside `AppBuilder` performs its own dependency construction. Swapping the CSV implementation for a database implementation means changing one line in `AppBuilder` with no changes to any interactor or presenter.

---

## How It Works

1. **Startup** -- `Main.java` calls `AppBuilder`, which reads `users.csv` into `FileUserDataAccessObject` and initialises `HealthMetricsDataAccessObject` (lazy JSON file creation per user). The assembled `JFrame` opens on the login panel.

2. **Authentication** -- `LoginInteractor` looks up the username in `FileUserDataAccessObject`, compares the stored password, sets the current user on the data access object, and calls `LoginOutputBoundary.prepareSuccessView()`. The presenter triggers `CardLayout` to switch to the dashboard. `SignupInteractor` validates that the username is not already taken, constructs a `User` via `UserFactory`, and persists it.

3. **Logging metrics** -- `InputMetricsInteractor` receives a date and five numeric fields. Each field is validated (sleep must be 0-24 h; all others non-negative). A `HealthMetrics` object is constructed and written to the user's JSON file via `HealthMetricsDataAccessObject`. The presenter confirms success or surfaces the first validation error.

4. **Scoring** -- `DailyHealthScoreInteractor` retrieves the day's `HealthMetrics`, passes it to `HealthScoreCalculator`, and persists the resulting integer score. It then calls `GeminiAPIService.generateFeedbackAsync()`, which serialises the metrics into a prompt, fires an OkHttp POST inside a `SwingWorker`, and invokes the presenter callback on the Event Dispatch Thread when the response arrives.

5. **Goal calculation** -- `GoalsInteractor` reads the user's age, height, and weight from `FileUserDataAccessObject`, applies the Mifflin-St Jeor equation to derive basal metabolic rate, multiplies by an activity factor, then computes the daily calorie surplus or deficit needed to reach the target weight in the requested timeframe. The result is passed to `GoalsPresenter` as a structured `GoalsOutputData` object.

6. **Insights** -- `HealthInsightsInteractor` fetches all persisted metrics for the user, computes per-metric averages, and passes the summary to `GeminiAPIService` with a trend-analysis prompt. The async response is delivered to the presenter identically to the daily score flow.

7. **Logout** -- the logout controller calls `AppBuilder.resetUserViewModels()`, which replaces every `ViewModel` reference with a fresh instance, clears the current user on the data access objects, and switches the view back to the login panel.

---

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6+
- A Gemini API key (free tier at [aistudio.google.com](https://aistudio.google.com/app/apikey))

### Installation

```bash
git clone https://github.com/parthiv-2006/BetterBlueprint.git
cd BetterBlueprint
mvn clean install
```

### Configuration

The application reads one environment variable:

| Variable | Description |
|----------|-------------|
| `GEMINI_API_KEY` | Google Gemini REST API key. The app throws `IllegalStateException` at startup if this is unset. |

**Windows (PowerShell, current session):**
```powershell
$env:GEMINI_API_KEY = "your-api-key-here"
```

**Windows (permanent, user-level):**
```powershell
[System.Environment]::SetEnvironmentVariable('GEMINI_API_KEY', 'your-api-key-here', 'User')
```

**macOS / Linux:**
```bash
export GEMINI_API_KEY="your-api-key-here"
```

**IntelliJ IDEA:** Run > Edit Configurations > Environment variables > `GEMINI_API_KEY=your-api-key-here`.

### Running Locally

```bash
mvn exec:java -Dexec.mainClass="app.Main"
```

Or open the project in IntelliJ IDEA and run `app/Main.java` directly.

---

## Testing

```bash
mvn test
```

**17 test files, JUnit Jupiter 5.8.1.** All tests use hand-written inner mock classes that implement the relevant data access and presenter interfaces. No external mocking library is required, and no API keys or file system state are needed to run the suite.

| Test file | What it covers |
|-----------|---------------|
| `DailyHealthScoreInteractorTest` | Score retrieval, null metrics handling, calculator exception, persistence exception, correct metric forwarding |
| `DailyHealthScoreInputDataTest` | Input data construction and field access |
| `DailyHealthScoreOutputDataTest` | Output data construction and field access |
| `GoalsInteractorTest` | BMR calculation correctness, underweight/overweight targets, invalid timeframe rejection |
| `GoalsInputDataTest` | Input data construction and field access |
| `GoalsOutputDataTest` | Output data construction and field access |
| `HealthHistoryInteractorTest` | History retrieval, empty history handling, date ordering |
| `HealthHistoryInputDataTest` | Input data construction and field access |
| `HealthHistoryOutputDataTest` | Output data construction and field access |
| `HealthInsightsInteractorTest` | Insights retrieval, Gemini callback success and failure paths |
| `HealthInsightsInputDataTest` | Input data construction and field access |
| `HealthInsightsOutputDataTest` | Output data construction and field access |
| `InputMetricsInteractorTest` | Field validation (sleep range, non-negative constraints), persistence path |
| `SettingsInteractorTest` | Partial field updates (any combination of age/height/weight), validation, dynamic output messages |
| `SettingsInputDataTest` | Input data construction and field access |
| `SettingsOutputDataTest` | Output data construction and field access |
| `GeminiAPIServiceTest` | Mock service with valid, empty, and null data; async callback success and error paths |

---

## Project Structure

```
src/
├── main/java/
│   ├── app/
│   │   ├── Main.java                       Entry point; builds and displays the JFrame
│   │   └── AppBuilder.java                 Composition root; wires all 126+ dependencies
│   ├── entity/
│   │   ├── User.java                       Domain model: username, password, age, height, weight
│   │   ├── UserFactory.java                Validates and constructs User instances
│   │   └── HealthMetrics.java              Domain model: date + five daily metrics
│   ├── use_case/
│   │   ├── login/                          LoginInputBoundary, LoginInteractor, LoginOutputBoundary,
│   │   │                                   LoginInputData, LoginOutputData
│   │   ├── signup/                         SignupInteractor + data classes
│   │   ├── input_metrics/                  InputMetricsInteractor + data classes
│   │   ├── daily_health_score/             DailyHealthScoreInteractor, HealthScoreCalculator,
│   │   │                                   + data classes
│   │   ├── goals/                          GoalsInteractor (Mifflin-St Jeor BMR) + data classes
│   │   ├── health_insights/                HealthInsightsInteractor (async Gemini) + data classes
│   │   ├── healthHistory/                  HealthHistoryInteractor + data classes
│   │   ├── settings/                       SettingsInteractor (partial field updates) + data classes
│   │   └── change_password/                ChangePasswordInteractor + data classes
│   ├── interface_adapter/
│   │   ├── (one controller + one presenter per use case)
│   │   └── ViewManagerModel.java           Drives CardLayout view switching
│   ├── data_access/
│   │   ├── FileUserDataAccessObject.java   CSV persistence; reads all rows at startup
│   │   └── HealthMetricsDataAccessObject.java  Per-user JSON files; lazy creation
│   ├── services/
│   │   └── GeminiAPIService.java           OkHttp POST to Gemini REST; SwingWorker async delivery
│   └── view/
│       └── (one Swing panel per screen; reads from ViewModels)
└── test/java/
    ├── services/GeminiAPIServiceTest.java
    └── use_case/
        ├── daily_health_score/             3 test files
        ├── goals/                          3 test files
        ├── healthHistory/                  3 test files
        ├── health_insights/                3 test files
        ├── input_metrics/                  1 test file
        └── settings/                       3 test files
```

---

## Known Limitations

- **File-based persistence does not scale past a single machine.** `users.csv` and per-user JSON files live on the local filesystem. Two users on different machines cannot share data. Replacing `FileUserDataAccessObject` with a JDBC implementation would require changes only in `AppBuilder`.
- **No concurrent access protection.** If two processes open the same `users.csv` simultaneously, writes can corrupt the file. The single-process desktop model makes this unlikely in practice but not impossible.
- **Gemini API key is required at startup.** If `GEMINI_API_KEY` is unset, the application throws `IllegalStateException` before the UI appears. A graceful degradation mode (score calculation without AI feedback) was not implemented within the course timeline.
- **No CI pipeline.** There is no GitHub Actions workflow. Tests run locally via `mvn test`. A pipeline with automated test execution on pull requests was out of scope for the course.
- **Password storage is plaintext.** Passwords are stored as strings in `users.csv`. Hashing with bcrypt was not implemented; this is a known gap acceptable for a course project with no external users.
- **Health score weights are hardcoded.** The five metric weights inside `HealthScoreCalculator` are constants. A user preference system for adjusting weights was scoped out during development.

---

## What We Would Build Next

1. **Database-backed persistence** -- swapping `FileUserDataAccessObject` and `HealthMetricsDataAccessObject` for JDBC or JPA implementations would require changes only in `AppBuilder`. SQLite would add zero infrastructure overhead while enabling concurrent access, indexed queries, and proper cascading deletes on account removal.

2. **Password hashing** -- replacing plaintext storage with bcrypt (via `bcprov-jdk15on`) is a single-class change inside `UserFactory` and the `FileUserDataAccessObject` write path. The use case layer would not change.

3. **CI pipeline** -- a GitHub Actions workflow running `mvn test` on every pull request would catch regressions before merge. The 17-test suite already runs in under 3 seconds with all services mocked, making CI feedback effectively instant.

4. **Configurable score weights** -- exposing the `HealthScoreCalculator` weights as user-editable preferences (persisted to the user's JSON file) would make the 0-100 score reflect each user's health priorities rather than a fixed formula. The calculator interface already accepts a `HealthMetrics` object, so the change is contained to the calculator implementation and a new settings panel.

5. **Trend visualisation** -- a `JFreeChart` line chart of the user's daily health score over 30 days would make the `HealthHistoryInteractor` output immediately actionable. The history data is already aggregated and returned; only the view layer would change.

---

## License

MIT

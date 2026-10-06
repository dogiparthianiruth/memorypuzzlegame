# Project Report

## Memory Puzzle — Java OOP Desktop Application

**Course:** Java Object-Oriented Programming
**Degree:** B.Tech Computer Science and Engineering
**Language:** Java 25 LTS
**GUI Framework:** Java Swing / AWT

---

## Abstract

Memory Puzzle is a fully functional, polished desktop game developed as a Java OOP project. The application demonstrates all core Object-Oriented Programming principles through a well-structured, three-layer MVC architecture. The game challenges players to match pairs of cards across three difficulty levels, tracking time, moves, and score. Results are persisted to a local file-based leaderboard. The user interface is built entirely with custom-painted Java Swing components, providing a modern dark-themed appearance without any external libraries.

---

## 1. Introduction

Memory puzzle games — also known as Concentration — have existed as card games for centuries and as software games since the earliest home computers. The objective is simple: find all matching pairs of face-down cards by remembering where each symbol is located. The challenge lies in remembering positions as the grid grows larger.

This project reimplements the classic game as a Java desktop application, with the primary goal of demonstrating Object-Oriented Programming concepts in a context that is both practical and visually impressive.

---

## 2. Problem Statement

Students studying Java OOP often submit projects that use Java syntax without genuinely applying OOP principles. The challenge is to build something that:

1. Uses encapsulation correctly (not just adding getters to public fields).
2. Uses inheritance meaningfully (not forced inheritance for its own sake).
3. Demonstrates polymorphism at runtime.
4. Separates concerns so that the game logic and the UI are independent.
5. Looks professional enough to demonstrate to faculty and peers.

---

## 3. Objectives

1. Build a complete, playable Memory Puzzle game.
2. Apply all four OOP pillars in a natural, justified way.
3. Implement a clean MVC architecture separating model, view, and controller.
4. Persist high scores using Java File I/O without any database.
5. Create a polished modern UI using only Java Swing and custom painting.
6. Ensure zero crashes from normal or abnormal user behaviour.
7. Produce complete academic documentation.

---

## 4. Proposed System

The proposed system is a single-window desktop Java application with the following modules:

| Module | Responsibility |
|--------|---------------|
| Splash Screen | Branded loading screen with progress animation |
| Main Menu | Navigation hub with all major options |
| Player Setup | Name input + difficulty selection |
| Game Board | Interactive card grid with live HUD |
| Victory Screen | Final stats + confetti animation |
| High Scores | Persistent top-10 leaderboard |
| How to Play | Rules and scoring reference |
| Settings | Toggles + score reset |

---

## 5. Features

### Gameplay
- Three difficulty levels: Easy (4×4), Medium (6×6), Hard (8×8)
- Cards randomly shuffled on every game and restart
- Visual card flip animation using trigonometric scaling
- 700ms delay before unmatched cards flip back
- Click-guard prevents selecting a third card during delay

### Scoring
- +100 per correct match
- −10 per wrong pair (minimum 0)
- Time bonus up to +300
- Move bonus up to +500

### UI/UX
- Dark modern theme with purple and cyan accents
- Custom-painted RoundedButton, RoundedPanel, CardButton, StatCard
- Anti-aliased rendering throughout
- Hover and press effects on all interactive elements
- Confetti particle animation on victory
- Single-window navigation via CardLayout

### Persistence
- Scores saved to `~/MemoryPuzzleScores.txt`
- Graceful handling of missing/corrupted file
- Top 10 by score, sorted automatically

---

## 6. Technologies

| Technology | Version / Details |
|-----------|-----------------|
| Java SE | 25 LTS |
| Java Swing | Built into JDK |
| Java AWT | Built into JDK |
| java.util.Collections | Sorting, shuffling |
| java.util.ArrayList | Dynamic card/score lists |
| java.nio.file / java.io | File I/O |
| javax.swing.Timer | EDT-safe timers |

No Maven, no Gradle, no external JARs. One `javac` command compiles everything.

---

## 7. System Requirements

- **OS:** Windows 10/11 (also works on macOS/Linux)
- **Java:** JDK 11 or later (tested on Java 25)
- **RAM:** 256 MB minimum
- **Disk:** < 5 MB
- **Display:** 1280×720 minimum recommended

---

## 8. OOP Concepts Applied

### 8.1 Encapsulation
Every model class (`Card`, `Game`, `Player`, `Score`) uses `private` fields with public getter methods. State changes happen only through purpose-built methods (`reveal()`, `markMatched()`, `start()`, `restart()`). This prevents any external code from putting a model object into an invalid state.

### 8.2 Abstraction
`GameLevel` is an abstract class that defines the contract for all difficulty levels without specifying concrete values. Classes that use `GameLevel` (such as `Game` and `GameController`) work through this abstract interface and never need to know the concrete type.

### 8.3 Inheritance
Three concrete classes extend `GameLevel`:
- `EasyLevel` (4×4, 8 pairs, card size 110px)
- `MediumLevel` (6×6, 18 pairs, card size 90px)
- `HardLevel` (8×8, 32 pairs, card size 75px)

Each inherits shared fields and the constructor from `GameLevel`, overriding only `getDescription()` and `getCardSize()`.

Custom UI components use inheritance too: `StatCard` extends `RoundedPanel`, which extends `JPanel`.

### 8.4 Polymorphism
The `Game` class holds a `GameLevel level` reference. Regardless of which concrete level was passed in, `level.getGridRows()`, `level.getPairs()`, `level.getDescription()` all work correctly via runtime dispatch. Adding a new difficulty requires only one new class — no changes to `Game` or `GameController`.

### 8.5 Composition
`Game` is composed of `List<Card>` (a game is made of cards). `GameBoard` is composed of `List<CardButton>`. `GameController` is composed of `Game`, `GameBoard`, `ScoreManager`, and two `Timer` objects. This mirrors the real-world "has-a" relationships.

---

## 9. System Architecture

```
┌──────────────────────────────────────────────────────┐
│                       VIEW (ui/)                      │
│  SplashScreen  MainMenu  GameBoard  CardButton        │
│  StatCard  VictoryDialog  HighScorePanel  ...         │
└────────────────────────┬─────────────────────────────┘
                         │  calls / updates
                         ▼
┌──────────────────────────────────────────────────────┐
│               CONTROLLER (controller/)                │
│               GameController                          │
└────────────────────────┬─────────────────────────────┘
                         │  reads / modifies
                         ▼
┌──────────────────────────────────────────────────────┐
│                    MODEL (model/, level/)              │
│  Card  Game  Player  Score  GameState  GameLevel      │
│  EasyLevel  MediumLevel  HardLevel                    │
└──────────────────────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────┐
│                    UTILITY (util/)                    │
│  ScoreManager (File I/O)    UIUtils (design system)  │
└──────────────────────────────────────────────────────┘
```

---

## 10. Class Descriptions

| Class | Package | Role |
|-------|---------|------|
| `Main` | (default) | Entry point; starts splash screen |
| `Card` | model | Represents a single card (symbol, state) |
| `Game` | model | Core game logic; manages cards, score, state |
| `Player` | model | Holds player name |
| `Score` | model | Serialisable score record; CSV I/O |
| `GameState` | model | Enum: IDLE/RUNNING/WAITING/WON |
| `GameLevel` | level | Abstract difficulty contract |
| `EasyLevel` | level | 4×4 concrete difficulty |
| `MediumLevel` | level | 6×6 concrete difficulty |
| `HardLevel` | level | 8×8 concrete difficulty |
| `GameController` | controller | Mediates model ↔ view |
| `SplashScreen` | ui | Intro loading window |
| `MainMenu` | ui | Navigation frame with CardLayout |
| `PlayerSetupPanel` | ui | Name input + difficulty selection |
| `GameBoard` | ui | Game grid view + HUD |
| `CardButton` | ui | Custom-painted card widget |
| `StatCard` | ui | HUD stat display widget |
| `VictoryDialog` | ui | End-game modal with confetti |
| `HighScorePanel` | ui | Leaderboard table |
| `HowToPlayPanel` | ui | Instructions screen |
| `SettingsPanel` | ui | Settings toggles + reset |
| `RoundedButton` | ui | Custom-painted rounded button |
| `RoundedPanel` | ui | Custom-painted rounded panel |
| `ScoreManager` | util | File-based score persistence |
| `UIUtils` | util | Shared colours, fonts, paint helpers |

---

## 11. Game Algorithm

```
1. Build card list: pick N symbols, duplicate each, shuffle.
2. Display grid of face-down CardButtons.
3. Start clock timer.
4. Repeat until all pairs matched:
   a. Wait for first card click.
   b. Reveal card; store as firstSelected.
   c. Wait for second card click.
   d. Reveal card; store as secondSelected. moves++.
   e. Compare symbols:
      - Match:   markMatched both; score+=100; matches++
      - No match: score=max(0,score-10); hide both after 700ms.
5. All pairs matched → finalise game:
   - Apply time and move bonuses.
   - Save score.
   - Show VictoryDialog.
```

---

## 12. Scoring Algorithm

```java
// Per match:
score = Math.max(0, score + 100);

// Per wrong pair:
score = Math.max(0, score - 10);

// On game completion (applyBonuses):
bestTime  = pairs * 3;        // heuristic: 3 seconds per pair
timeBonus = max(0, 300 - (elapsedSec - bestTime) * 2);

minMoves  = pairs;            // perfect game: one try per pair
moveBonus = max(0, 500 - (moves - minMoves) * 10);

finalScore = score + timeBonus + moveBonus;
```

---

## 13. File Handling

Score file path: `System.getProperty("user.home") + "/MemoryPuzzleScores.txt"`

**Writing:** `Files.newBufferedWriter(path)` writes CSV lines. Uses try-with-resources to guarantee the writer is closed.

**Reading:** `Files.newBufferedReader(path)` reads line by line. Each line is parsed by `Score.fromCsvLine()`. Malformed lines return `null` and are skipped.

**Error tolerance:**
- File missing → return empty list
- IOException → return partial list
- Corrupt line → skip that entry

---

## 14. UI Design

The UI uses a custom design system defined in `UIUtils.java`:

| Token | Value | Use |
|-------|-------|-----|
| `BG_DEEP` | #0F172A | Main background |
| `BG_PANEL` | #1E293B | Panel surfaces |
| `BG_CARD` | #334155 | Card backs |
| `ACCENT_PRIMARY` | #8B5CF6 | Buttons, highlights |
| `ACCENT_SECONDARY` | #06B6D4 | Info labels |
| `SUCCESS` | #22C55E | Match feedback |
| `ERROR` | #EF4444 | No-match feedback |
| `TEXT_PRIMARY` | #F8FAFC | Main text |
| `TEXT_SECONDARY` | #94A3B8 | Subtext |

All components are custom-painted using `Graphics2D` with anti-aliasing. No default Swing button or panel painting is visible to the user.

---

## 15. Testing

See `TEST_CASES.md` for the full test suite (20 test cases).

Key test areas:
- Application launch
- Screen navigation
- Player name validation
- All three difficulty levels
- Card interaction rules
- Timer behaviour
- Score calculation
- Victory condition
- High score persistence
- Corrupted score file recovery
- Settings reset confirmation

---

## 16. Advantages

1. Zero external dependencies — runs anywhere Java is installed.
2. Clean OOP architecture — easy to extend or modify.
3. Genuine polymorphism — new difficulty levels require one new class.
4. Graceful error handling — file I/O failures never crash the game.
5. Modern UI — custom painting gives full control over appearance.
6. Single JAR/folder deployment — no installer needed.

---

## 17. Limitations

1. No audio — `javax.sound.sampled` support can be added without architectural changes.
2. No keyboard navigation — only mouse input.
3. Emoji rendering depends on the system font ("Segoe UI Emoji" on Windows).
4. No network leaderboard — scores are local only.
5. No undo feature.

---

## 18. Future Enhancements

1. **Sound effects** — card flip, match success, game win using `javax.sound.sampled`.
2. **Multiple themes** — light mode, nature theme, space theme.
3. **Card image packs** — replace emoji with animal photos or flags.
4. **Keyboard support** — Tab/Arrow navigation + Enter to flip.
5. **Countdown preview** — briefly show all cards before the game starts.
6. **Statistics tracking** — games played, win rate, best time per difficulty.
7. **Online leaderboard** — REST API integration.
8. **Executable JAR** — package with `jar` tool for one-click launch.

---

## 19. Conclusion

This project successfully demonstrates all five core Java OOP principles in a cohesive, working application. The architecture separates concerns clearly across Model, View, and Controller layers. The custom Swing UI provides a modern, polished appearance that rivals commercial casual games in visual quality. The project is a complete, self-contained software artefact that is ready for academic submission, demonstration, and viva examination.

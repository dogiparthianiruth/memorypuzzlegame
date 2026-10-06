# Memory Puzzle

A polished desktop Memory Puzzle game built with **Java Swing** as a B.Tech CSE Java OOP college project.

---

## 1. Project Overview

Memory Puzzle is a classic card-matching game where the player flips cards two at a time to find matching pairs. The game tracks time, moves, and score, saves results to a local leaderboard, and features a modern dark-themed UI built entirely with custom Swing components — no external libraries required.

---

## 2. Objectives

- Demonstrate core Java OOP principles in a real, working application.
- Build a polished, professional-looking game using only the Java Standard Library.
- Implement persistent high-score storage using Java File I/O.
- Provide a clear MVC architecture suitable for academic evaluation.

---

## 3. Features

| Feature | Details |
|---------|---------|
| 3 Difficulty Levels | Easy (4×4), Medium (6×6), Hard (8×8) |
| Custom Animated Cards | Flip animation, hover effects, matched state |
| Live HUD | Real-time Time, Moves, Matches, Score |
| Scoring System | +100 match, −10 wrong, time bonus, move bonus |
| Persistent Leaderboard | Top-10 scores saved to `~/MemoryPuzzleScores.txt` |
| Splash Screen | Animated progress bar intro |
| Victory Dialog | Confetti animation + final stats |
| Settings | Sound/animation toggles + score reset |
| How To Play | Full instruction screen |
| Modern Dark UI | Custom-painted Swing components throughout |

---

## 4. Technologies

- **Language:** Java 25 LTS
- **GUI:** Java Swing + AWT
- **Concurrency:** `javax.swing.Timer` (EDT-safe)
- **Collections:** `java.util.ArrayList`, `java.util.Collections`
- **File I/O:** `java.nio.file.Files`, `java.io.BufferedReader/Writer`
- **Build:** `javac` (no Maven/Gradle required)

---

## 5. System Requirements

- Java 11 or later (tested on Java 25)
- Windows 10/11 (also runs on macOS/Linux)
- VS Code with Language Support for Java (optional)

---

## 6. Installation

No installation required. Clone or download the project folder.

---

## 7. How to Run

### Option A — PowerShell script (recommended)
```powershell
cd java
pwsh -File run.ps1
```

### Option B — Manual commands
```powershell
# From the java/ folder:
mkdir out
$files = Get-ChildItem -Recurse -Filter *.java src | Select-Object -ExpandProperty FullName
javac -encoding UTF-8 -d out $files
java -cp out Main
```

---

## 8. Project Structure

```
java/
├── src/
│   ├── Main.java                   ← Entry point
│   ├── model/
│   │   ├── Card.java               ← Single card state
│   │   ├── Game.java               ← Core game logic
│   │   ├── GameState.java          ← Enum: IDLE/RUNNING/WAITING/WON
│   │   ├── Player.java             ← Player name
│   │   └── Score.java              ← Serialisable score record
│   ├── level/
│   │   ├── GameLevel.java          ← Abstract base
│   │   ├── EasyLevel.java          ← 4×4
│   │   ├── MediumLevel.java        ← 6×6
│   │   └── HardLevel.java          ← 8×8
│   ├── controller/
│   │   └── GameController.java     ← Mediates model ↔ view
│   ├── ui/
│   │   ├── SplashScreen.java
│   │   ├── MainMenu.java
│   │   ├── PlayerSetupPanel.java
│   │   ├── GameBoard.java
│   │   ├── CardButton.java
│   │   ├── StatCard.java
│   │   ├── HighScorePanel.java
│   │   ├── HowToPlayPanel.java
│   │   ├── SettingsPanel.java
│   │   ├── VictoryDialog.java
│   │   ├── RoundedButton.java
│   │   └── RoundedPanel.java
│   └── util/
│       ├── UIUtils.java            ← Colours, fonts, paint helpers
│       └── ScoreManager.java       ← File-based persistence
├── out/                            ← Compiled .class files
├── run.ps1                         ← Build + run script
└── README.md
```

---

## 9. Architecture

The project follows the **Model-View-Controller (MVC)** pattern:

- **Model** (`model/`, `level/`): Pure Java classes with no Swing imports. Manages all game state.
- **View** (`ui/`): Custom Swing components that display state. Never modify the model directly.
- **Controller** (`controller/GameController`): Receives user input from the View, applies it to the Model, then instructs the View to update.

---

## 10. Game Rules

1. Click any hidden card to reveal it.
2. Click a second card.
3. If both show the same symbol → they stay revealed (matched).
4. If they differ → both flip back face-down after 700 ms.
5. Match all pairs to win.

---

## 11. Scoring

| Event | Points |
|-------|--------|
| Correct match | +100 |
| Wrong pair | −10 (minimum 0) |
| Time bonus | up to +300 |
| Move bonus | up to +500 |

---

## 12. OOP Concepts Used

| Concept | Where |
|---------|-------|
| Encapsulation | All model classes (private fields + getters) |
| Abstraction | `GameLevel` abstract class |
| Inheritance | `EasyLevel`, `MediumLevel`, `HardLevel` extend `GameLevel` |
| Polymorphism | `Game` uses `GameLevel` reference; method overriding |
| Composition | `Game` contains `List<Card>`; `GameBoard` contains `CardButton` list |
| MVC | Model / View / Controller separation |

---

## 13. Future Enhancements

- Background music and sound effects
- Multiple colour themes
- Online leaderboard
- Card image themes (animals, flags, etc.)
- Undo last move feature
- Accessibility: keyboard-only navigation

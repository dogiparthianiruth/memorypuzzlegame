# Class Diagram — Memory Puzzle

## Inheritance Hierarchy

```
          GameLevel  <<abstract>>
          ──────────────────────
          # name     : String
          # gridRows : int
          # gridCols : int
          # pairs    : int
          ──────────────────────
          + getName()     : String
          + getGridRows() : int
          + getGridCols() : int
          + getPairs()    : int
          + getDescription() : String  <<abstract>>
          + getCardSize()    : int     <<abstract>>
               /      |      \
              /       |       \
   EasyLevel   MediumLevel   HardLevel
   ─────────   ───────────   ─────────
   4×4 grid    6×6 grid      8×8 grid
   +getDesc()  +getDesc()    +getDesc()
   +getSize()  +getSize()    +getSize()
```

---

## Model Layer

```
  Player                    Card
  ──────────                ──────────────────────
  - name : String           - id       : int
  ──────────                - symbol   : String
  + getName()               - revealed : boolean
  + setName()               - matched  : boolean
                            ──────────────────────
                            + reveal()
                            + hide()
                            + markMatched()
                            + reset()
                            + getSymbol() : String
                            + isRevealed(): boolean
                            + isMatched() : boolean

  GameState  <<enum>>
  ──────────────────
  IDLE
  RUNNING
  WAITING
  WON

  Score
  ─────────────────────
  - playerName : String
  - difficulty : String
  - score      : int
  - moves      : int
  - time       : String
  ─────────────────────
  + toCsvLine()  : String
  + fromCsvLine(): Score  (static)
  + compareTo()           (Comparable)
```

---

## Game (Core Model — Composition)

```
  Game
  ──────────────────────────────────────────
  - player        : Player        (has-a)
  - level         : GameLevel     (has-a, polymorphic)
  - cards         : List<Card>    (has-a, Composition)
  - firstSelected : Card
  - secondSelected: Card
  - moves         : int
  - matches       : int
  - score         : int
  - state         : GameState
  - startTimeMillis : long
  - endTimeMillis   : long
  ──────────────────────────────────────────
  + start()
  + restart()
  + selectCard(Card) : boolean
  + evaluateSelection() : boolean
  + hideUnmatched()
  + clearSelection()
  + isComplete() : boolean
  + finalise()
  + getFormattedTime() : String
  + getScore() : int
  + getMoves() : int
  + getMatches() : int
  + getState() : GameState
  + getPlayer() : Player
  + getLevel() : GameLevel
  + getCards() : List<Card>
```

---

## Controller Layer

```
  GameController
  ──────────────────────────────
  - game         : Game
  - board        : GameBoard
  - scoreManager : ScoreManager
  - clockTimer   : Timer
  - flipBackTimer: Timer
  ──────────────────────────────
  + startGame(Game, GameBoard)
  + restartGame()
  + stopAllTimers()
  + onCardClicked(Card)
  ──── private ────
  - scheduleEvaluation()
  - evaluate()
  - onGameWon()
  - startClockTimer()

  GameController ──uses──> Game
  GameController ──uses──> GameBoard
  GameController ──uses──> ScoreManager
```

---

## View Layer (UI)

```
  JWindow
    └── SplashScreen
          - progressBar : JProgressBar
          - loadTimer   : Timer

  JFrame
    └── MainMenu
          - rootPanel   : JPanel  (CardLayout)
          - controller  : GameController
          - scoreManager: ScoreManager
          ┌── PlayerSetupPanel
          ├── GameBoard
          ├── HighScorePanel
          ├── HowToPlayPanel
          ├── SettingsPanel
          └── (VictoryDialog – spawned as JDialog)

  JPanel
    ├── RoundedPanel                 (custom-painted)
    │     └── StatCard              (extends RoundedPanel)
    ├── PlayerSetupPanel
    ├── GameBoard
    │     ├── List<CardButton>      (composition)
    │     └── StatCard × 4
    ├── HighScorePanel
    ├── HowToPlayPanel
    └── SettingsPanel

  JButton
    ├── RoundedButton               (custom-painted)
    └── CardButton                  (custom-painted, holds Card)

  JDialog
    └── VictoryDialog
          - confettiTimer : Timer
```

---

## Utility Layer

```
  ScoreManager
  ────────────────────────────────
  - SCORE_FILE : Path  (~/MemoryPuzzleScores.txt)
  - MAX_SCORES : int = 10
  ────────────────────────────────
  + saveScore(Score)
  + loadScores() : List<Score>
  + clearScores()

  UIUtils  <<utility, not instantiable>>
  ────────────────────────────────────
  + BG_DEEP, BG_PANEL, ...     (Color constants)
  + font(), titleFont(), ...   (Font factories)
  + applyRenderingHints(Graphics2D)
  + paintGradientBackground(...)
  + fillRoundRect(...)
  + setLookAndFeel()
```

---

## Relationship Summary

```
Relationship       Type          Description
─────────────────────────────────────────────────────────
Game ──── Card      Composition   Game is made of Cards
Game ──── Player    Composition   Game has a Player
Game ──── GameLevel Composition   Game has a GameLevel (polymorphic)
GameController ──── Game          Association (Controller uses Model)
GameController ──── GameBoard     Association (Controller drives View)
EasyLevel ──── GameLevel          Inheritance (IS-A)
MediumLevel ──── GameLevel        Inheritance (IS-A)
HardLevel ──── GameLevel          Inheritance (IS-A)
StatCard ──── RoundedPanel        Inheritance (IS-A, specialised panel)
CardButton ──── Card              Association (View holds Model ref)
```

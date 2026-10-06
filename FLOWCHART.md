# Flowcharts — Memory Puzzle

---

## 1. Application Startup Flow

```
[Launch JVM]
      │
      ▼
[UIUtils.setLookAndFeel()]
      │
      ▼
[SwingUtilities.invokeLater()]
      │
      ▼
[new SplashScreen(callback)]
      │
      ▼
[SplashScreen.start()]  ──── Timer fires every 20ms
      │                           │
      │             progress += 2 ▼
      │             progressBar updates
      │
      ▼  (progress >= 100)
[splash.dispose()]
      │
      ▼
[new MainMenu()]
      │
      ▼
[mainMenu.setVisible(true)]
      │
      ▼
[MAIN MENU shown]
```

---

## 2. Starting a Game

```
[Main Menu]
      │
      ▼  click PLAY GAME
[PlayerSetupPanel shown]
      │
      ├── Enter player name
      ├── Select difficulty card (Easy / Medium / Hard)
      │
      ▼  click START GAME
[Validate: name empty?]
      │
      ├── YES → show warning dialog → stay on setup
      │
      └── NO
            │
            ▼
      [new GameLevel (based on selection)]
      [new Player(name)]
      [new Game(player, level)]
            │
            ▼
      [new GameBoard(game, controller, mainMenu)]
            │
            ▼
      [rootPanel adds GameBoard panel]
      [window resized for difficulty]
      [CardLayout.show GAME panel]
            │
            ▼
      [controller.startGame(game, board)]
            │
            ▼
      [game.start()]  — sets state=RUNNING, records startTime
            │
            ▼
      [Clock Timer started]  — fires every 1 second
            │
            ▼
      [GAME BOARD shown — player can click cards]
```

---

## 3. Selecting Cards (Player Interaction)

```
[Player clicks a CardButton]
         │
         ▼
[controller.onCardClicked(card)]
         │
         ├── game.state == WON?     → ignore
         ├── game.state == WAITING? → ignore (flip-back in progress)
         ├── card.isMatched()?      → ignore
         ├── card.isRevealed()?     → ignore (same card clicked again)
         │
         └── All checks pass
               │
               ▼
         [game.selectCard(card)]
               │
               ├── firstSelected == null?
               │         │
               │         ▼  YES
               │   [firstSelected = card]
               │   [card.reveal()]
               │   [board.syncCardState(card, animate=true)]
               │   [board.updateHud()]
               │   [state stays RUNNING]
               │   [WAIT for second click]
               │
               └── firstSelected != null
                         │
                         ▼  YES (second card)
                   [secondSelected = card]
                   [card.reveal()]
                   [moves++]
                   [state = WAITING]
                   [board.syncCardState(card, animate=true)]
                   [board.updateHud()]
                         │
                         ▼
                   [scheduleEvaluation()]
                   [flipBackTimer starts: 700ms]
```

---

## 4. Matching Cards

```
[flipBackTimer fires after 700ms]
         │
         ▼
[evaluate()]
         │
         ▼
[game.evaluateSelection()]
         │
         ├── firstSelected.symbol == secondSelected.symbol?
         │
         │  YES (MATCH)                    NO (NO MATCH)
         │     │                                │
         │     ▼                                ▼
         │ [first.markMatched()]         [score -= 10]
         │ [second.markMatched()]        [board.showMatchFeedback(false)]
         │ [matches++]                   [game.hideUnmatched()]
         │ [score += 100]                [first.hide(), second.hide()]
         │ [board.syncCardState(×2)]     [board.syncCardState(×2, animate)]
         │ [board.showMatchFeedback(true)]
         │ [game.clearSelection()]
         │ [state = RUNNING]
         │         │
         │         ▼
         │ [game.isComplete()?]
         │         │
         │         ├── YES → onGameWon()
         │         └── NO  → continue playing
         │
         └── [board.updateHud()]
```

---

## 5. Winning the Game

```
[game.isComplete() == true]
         │
         ▼
[controller.stopAllTimers()]  — clockTimer, flipBackTimer stopped
         │
         ▼
[game.finalise()]
    ├── endTimeMillis = now
    ├── state = WON
    └── applyBonuses()
            ├── timeBonus  (max 300)
            └── moveBonus  (max 500)
         │
         ▼
[new Score(player, level, score, moves, time)]
         │
         ▼
[scoreManager.saveScore(score)]
    ├── loadScores()
    ├── add new score
    ├── Collections.sort() — highest first
    ├── trim to top 10
    └── write to ~/MemoryPuzzleScores.txt
         │
         ▼
[board.updateHud()]
         │
         ▼
[board.showVictory(score)]
         │
         ▼
[new VictoryDialog(owner, score, ...)]
    ├── Confetti Timer starts
    ├── Show final stats
    └── Two buttons:
            ├── PLAY AGAIN → stopConfetti, dispose, controller.restartGame()
            └── MAIN MENU  → stopConfetti, dispose, mainMenu.showMainMenu()
```

---

## 6. Saving High Scores

```
[scoreManager.saveScore(newScore)]
         │
         ▼
[scoreManager.loadScores()]
         │
         ├── File exists?
         │      │
         │      ├── NO  → return empty ArrayList
         │      │
         │      └── YES → BufferedReader reads each line
         │                  │
         │                  └── Score.fromCsvLine(line)
         │                          │
         │                          ├── Malformed? → return null → skip
         │                          └── Valid?     → add to list
         │
         ▼
[scores.add(newScore)]
         │
         ▼
[Collections.sort(scores)]  — natural order: highest score first
         │
         ▼
[scores.size() > 10?]
    ├── YES → subList(0, 10)
    └── NO  → keep all
         │
         ▼
[BufferedWriter writes CSV lines to file]
         │
         ▼
[Done — game never crashes on I/O errors]
```

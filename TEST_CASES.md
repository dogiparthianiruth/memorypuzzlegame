# Test Cases — Memory Puzzle

## Test Suite Summary

| Total | Passed | Failed |
|-------|--------|--------|
| 20    | 20     | 0      |

All tests verified by compile-time static analysis and manual execution.

---

## TC01 — Application Launch

| Field | Details |
|-------|---------|
| **Test ID** | TC01 |
| **Test Name** | Application Launch |
| **Description** | Launch the application via `java -cp out Main` |
| **Steps** | 1. Run `java -cp out Main` |
| **Expected** | Splash screen appears, progress bar fills, Main Menu opens after ~2s |
| **Status** | ✅ PASS |
| **Notes** | SplashScreen disposes and MainMenu.setVisible(true) called in callback |

---

## TC02 — Main Menu Navigation

| Field | Details |
|-------|---------|
| **Test ID** | TC02 |
| **Test Name** | Main Menu Buttons |
| **Description** | All five main menu buttons are clickable and navigate correctly |
| **Steps** | Click each button in order: PLAY GAME, HIGH SCORES, HOW TO PLAY, SETTINGS, EXIT |
| **Expected** | Each button opens the correct screen; EXIT terminates the application |
| **Status** | ✅ PASS |

---

## TC03 — Empty Player Name Validation

| Field | Details |
|-------|---------|
| **Test ID** | TC03 |
| **Test Name** | Empty Name Guard |
| **Description** | Clicking START GAME with an empty name field should be rejected |
| **Steps** | 1. Click PLAY GAME. 2. Leave name field empty. 3. Click START GAME |
| **Expected** | Warning dialog shown: "Please enter your name to continue." Game does not start. |
| **Status** | ✅ PASS |
| **Notes** | `name.isEmpty()` check in `PlayerSetupPanel.handleStart()` |

---

## TC04 — Whitespace-Only Name Validation

| Field | Details |
|-------|---------|
| **Test ID** | TC04 |
| **Test Name** | Whitespace Name Guard |
| **Description** | Name containing only spaces should be rejected |
| **Steps** | 1. Enter "   " in name field. 2. Click START GAME |
| **Expected** | Warning dialog shown. Game does not start. |
| **Status** | ✅ PASS |
| **Notes** | `name.trim().isEmpty()` in PlayerSetupPanel |

---

## TC05 — Easy Difficulty Launch

| Field | Details |
|-------|---------|
| **Test ID** | TC05 |
| **Test Name** | Easy Mode Board |
| **Description** | Easy mode creates a 4×4 grid with 16 cards |
| **Steps** | 1. Enter name. 2. Select Easy. 3. Click START GAME |
| **Expected** | 4×4 grid of cards displayed. HUD shows "Matches: 0 / 8". |
| **Status** | ✅ PASS |
| **Notes** | `EasyLevel.getPairs() == 8`, `getGridRows() == 4`, `getGridCols() == 4` |

---

## TC06 — Medium Difficulty Launch

| Field | Details |
|-------|---------|
| **Test ID** | TC06 |
| **Test Name** | Medium Mode Board |
| **Description** | Medium mode creates a 6×6 grid with 36 cards |
| **Steps** | 1. Enter name. 2. Select Medium. 3. Click START GAME |
| **Expected** | 6×6 grid displayed. HUD shows "Matches: 0 / 18". |
| **Status** | ✅ PASS |

---

## TC07 — Hard Difficulty Launch

| Field | Details |
|-------|---------|
| **Test ID** | TC07 |
| **Test Name** | Hard Mode Board |
| **Description** | Hard mode creates an 8×8 grid with 64 cards |
| **Steps** | 1. Enter name. 2. Select Hard. 3. Click START GAME |
| **Expected** | 8×8 grid displayed. HUD shows "Matches: 0 / 32". |
| **Status** | ✅ PASS |

---

## TC08 — Matching Pair

| Field | Details |
|-------|---------|
| **Test ID** | TC08 |
| **Test Name** | Correct Card Match |
| **Description** | Clicking two cards with the same symbol marks them matched |
| **Steps** | 1. Start a game. 2. Click two cards with the same symbol |
| **Expected** | Both cards stay revealed with green tint. Matches counter increments. Score +100. Feedback "✓ Match found!" shown. |
| **Status** | ✅ PASS |

---

## TC09 — Non-Matching Pair

| Field | Details |
|-------|---------|
| **Test ID** | TC09 |
| **Test Name** | Wrong Card Pair |
| **Description** | Clicking two cards with different symbols hides them both after delay |
| **Steps** | 1. Start a game. 2. Click two cards with different symbols |
| **Expected** | Both revealed briefly. After 700ms both flip face-down. Score −10. Feedback "✗ No match" shown. |
| **Status** | ✅ PASS |

---

## TC10 — Same Card Click Prevention

| Field | Details |
|-------|---------|
| **Test ID** | TC10 |
| **Test Name** | Same Card Double-Click Guard |
| **Description** | Clicking an already-revealed card should be ignored |
| **Steps** | 1. Click one card. 2. Click the same card again |
| **Expected** | The second click is ignored. Moves counter does not increment. |
| **Status** | ✅ PASS |
| **Notes** | `card.isRevealed()` guard in `game.selectCard()` |

---

## TC11 — Third Card Click Prevention

| Field | Details |
|-------|---------|
| **Test ID** | TC11 |
| **Test Name** | Third Card Guard |
| **Description** | While two cards are face-up and waiting to be evaluated, no third card can be selected |
| **Steps** | 1. Click card A. 2. Click card B. 3. Immediately click card C |
| **Expected** | Click on card C is ignored. Only A and B are processed. |
| **Status** | ✅ PASS |
| **Notes** | `game.getState() == WAITING` guard in `onCardClicked()` |

---

## TC12 — Matched Card Click Prevention

| Field | Details |
|-------|---------|
| **Test ID** | TC12 |
| **Test Name** | Matched Card Guard |
| **Description** | Clicking a matched (permanently revealed) card does nothing |
| **Steps** | 1. Successfully match a pair. 2. Click one of the matched cards |
| **Expected** | Nothing happens. Moves counter unchanged. |
| **Status** | ✅ PASS |
| **Notes** | `card.isMatched()` guard in `game.selectCard()` |

---

## TC13 — Timer Starts and Updates

| Field | Details |
|-------|---------|
| **Test ID** | TC13 |
| **Test Name** | Game Clock |
| **Description** | Timer starts when game begins and updates the Time HUD every second |
| **Steps** | 1. Start a game. 2. Wait 5 seconds without clicking |
| **Expected** | Time HUD increments every second: 00:01, 00:02, ... 00:05 |
| **Status** | ✅ PASS |

---

## TC14 — Timer Stops on Main Menu

| Field | Details |
|-------|---------|
| **Test ID** | TC14 |
| **Test Name** | Timer Cleanup |
| **Description** | Clicking Main Menu during a game stops the timer |
| **Steps** | 1. Start a game. 2. Click ⌂ Main Menu. 3. Return to main menu. 4. Wait 10 seconds |
| **Expected** | Timer stopped; no background thread continues running |
| **Status** | ✅ PASS |
| **Notes** | `controller.stopAllTimers()` called in Main Menu button handler |

---

## TC15 — Restart Game

| Field | Details |
|-------|---------|
| **Test ID** | TC15 |
| **Test Name** | Restart Functionality |
| **Description** | Clicking Restart resets all cards and counters |
| **Steps** | 1. Play a few moves. 2. Click ↺ Restart |
| **Expected** | All cards face-down. Time=00:00, Moves=0, Matches=0/N, Score=0. New shuffle. |
| **Status** | ✅ PASS |

---

## TC16 — Victory Condition

| Field | Details |
|-------|---------|
| **Test ID** | TC16 |
| **Test Name** | Win Detection |
| **Description** | Matching all pairs triggers the victory screen |
| **Steps** | 1. Match every pair of cards |
| **Expected** | VictoryDialog opens with "YOU DID IT!", final stats, confetti animation. Timer stops. |
| **Status** | ✅ PASS |

---

## TC17 — High Score Persistence

| Field | Details |
|-------|---------|
| **Test ID** | TC17 |
| **Test Name** | Score Save and Load |
| **Description** | Winning a game saves the score, which appears on the leaderboard |
| **Steps** | 1. Win a game. 2. Open High Scores |
| **Expected** | Player's name, difficulty, score, moves, and time are displayed in the table. |
| **Status** | ✅ PASS |

---

## TC18 — Corrupt Score File Recovery

| Field | Details |
|-------|---------|
| **Test ID** | TC18 |
| **Test Name** | Malformed Score File |
| **Description** | A corrupted score file entry should be silently skipped |
| **Steps** | 1. Edit `~/MemoryPuzzleScores.txt` and add `BADDATA,,,X` as a line. 2. Open High Scores |
| **Expected** | Application does not crash. Valid entries displayed. Corrupt line skipped. |
| **Status** | ✅ PASS |
| **Notes** | `Score.fromCsvLine()` returns null for malformed lines |

---

## TC19 — Missing Score File Recovery

| Field | Details |
|-------|---------|
| **Test ID** | TC19 |
| **Test Name** | Missing Score File |
| **Description** | Opening High Scores when the score file does not exist |
| **Steps** | 1. Delete `~/MemoryPuzzleScores.txt`. 2. Open High Scores |
| **Expected** | High Scores screen opens normally with "No scores yet" message. No crash. |
| **Status** | ✅ PASS |
| **Notes** | `Files.exists(SCORE_FILE)` check in `ScoreManager.loadScores()` |

---

## TC20 — Reset High Scores

| Field | Details |
|-------|---------|
| **Test ID** | TC20 |
| **Test Name** | Settings — Reset Scores |
| **Description** | Resetting high scores deletes all saved scores |
| **Steps** | 1. Win at least one game. 2. Open Settings. 3. Click Reset High Scores. 4. Confirm YES. 5. Open High Scores |
| **Expected** | Confirmation dialog shown. After confirmation, High Scores shows "No scores yet". |
| **Status** | ✅ PASS |
| **Notes** | `scoreManager.clearScores()` uses `Files.deleteIfExists()` |

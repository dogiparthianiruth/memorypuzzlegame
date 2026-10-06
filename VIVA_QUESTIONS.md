# Viva Questions — Memory Puzzle (Java OOP)

40+ likely exam/viva questions with clear, student-friendly answers.

---

## Section 1: General / Language

**Q1. Why did you choose Java for this project?**
Java is platform-independent (Write Once, Run Anywhere), has built-in Swing for desktop GUI, a rich standard library for file I/O and collections, and is the primary language taught in our OOP course. No external frameworks or internet access are needed.

---

**Q2. Why did you use Swing and not JavaFX?**
JavaFX requires a separate SDK on some Java distributions, while Swing is built into the core JDK. Swing is sufficient for a polished desktop game and is fully covered in Java SE documentation. JavaFX also has a steeper learning curve.

---

**Q3. What is the JDK and JRE?**
The JDK (Java Development Kit) contains the compiler (`javac`) and the JRE. The JRE (Java Runtime Environment) contains the JVM that runs compiled `.class` files. To compile and run this project, the JDK is needed.

---

**Q4. What does `SwingUtilities.invokeLater()` do?**
It schedules a task to run on the Event Dispatch Thread (EDT). All Swing component creation and updates must happen on the EDT to avoid concurrency bugs. `invokeLater()` is the correct way to start a Swing application from `main()`.

---

## Section 2: OOP Fundamentals

**Q5. What is Object-Oriented Programming?**
OOP is a programming paradigm where the program is structured around objects — instances of classes — that combine data (fields) and behaviour (methods). The four pillars are Encapsulation, Abstraction, Inheritance, and Polymorphism.

---

**Q6. What is a class? Give an example from your project.**
A class is a template/blueprint for creating objects. Example: `Card.java` defines that every card has a symbol, a revealed status, and a matched status. It also defines the behaviour: `reveal()`, `hide()`, `markMatched()`.

---

**Q7. What is an object? Give an example.**
An object is one specific instance of a class with its own field values. Example: `new Card(0, "🍎")` creates a Card object with id=0 and symbol "🍎". Another `new Card(1, "🍎")` creates a second, separate object — same class, different instance.

---

**Q8. What is encapsulation? Where is it used?**
Encapsulation means making fields `private` and providing `public` methods to access or change them in a controlled way. In `Card.java` all three fields (`symbol`, `revealed`, `matched`) are private. The only way to change the card's state is through `reveal()`, `hide()`, or `markMatched()`. This prevents illegal state changes.

---

**Q9. Why can't I just make all fields public?**
Public fields allow any class to set them to any value, including invalid states (e.g., setting `matched = true` while `revealed = false`). Private fields with controlled setters/methods enforce the rules.

---

**Q10. What is abstraction? Where is it used?**
Abstraction hides complex details behind a simple interface. In this project, `GameLevel` is an abstract class that declares `getDescription()` and `getCardSize()` as abstract methods. The `Game` class works with any `GameLevel` without needing to know if it's Easy, Medium, or Hard.

---

**Q11. What is the difference between an abstract class and an interface?**

| Feature | Abstract Class | Interface |
|---------|---------------|-----------|
| Can have constructors | Yes | No |
| Can have concrete methods | Yes | Yes (default methods in Java 8+) |
| Can have fields | Yes | Only constants |
| Multiple inheritance | No (single extends) | Yes (multiple implements) |

`GameLevel` was made an abstract class (not an interface) because it has concrete shared fields like `name`, `gridRows`, `gridCols`, `pairs` and a shared constructor that all subclasses use.

---

**Q12. What is inheritance? Where is it used?**
Inheritance lets a child class acquire fields and methods from a parent class. `EasyLevel`, `MediumLevel`, and `HardLevel` all extend `GameLevel`. They inherit `name`, `gridRows`, `gridCols`, `pairs`, and all the getter methods for free. Each only needs to implement `getDescription()` and `getCardSize()`.

---

**Q13. What is method overriding?**
Overriding is when a subclass provides its own version of a method defined in the parent class. The `@Override` annotation confirms this. Example: `EasyLevel.getDescription()` overrides `GameLevel.getDescription()`. The subclass version is called instead of the parent's version.

---

**Q14. What is polymorphism? Where is it used?**
Polymorphism means "many forms." A `GameLevel` reference can hold an `EasyLevel`, `MediumLevel`, or `HardLevel` object. When `level.getDescription()` is called, Java automatically calls the correct subclass version. This is runtime (dynamic) polymorphism. It means `GameController` and `Game` don't need `if/else` chains for each difficulty.

---

**Q15. What is composition? Where is it used?**
Composition is a "has-a" relationship where one class contains objects of another class as fields. `Game` contains `List<Card>` — a Game HAS cards. `GameBoard` (view) contains `List<CardButton>`. Unlike inheritance ("is-a"), composition models things that belong to each other rather than things that are subtypes.

---

## Section 3: Architecture

**Q16. What is MVC architecture?**
Model-View-Controller separates the application into three responsibilities:
- **Model** — data and game rules (no Swing)
- **View** — what the player sees (Swing panels, buttons)
- **Controller** — receives input from View, updates Model, tells View to refresh

This separation makes the code easier to test, modify, and understand.

---

**Q17. Why does the Game class have no Swing imports?**
Because `Game` is the Model layer. The Model should only contain game rules and state. Keeping Swing out means the model can be unit-tested without a display, and the UI could be replaced (e.g., with a web frontend) without touching the game logic.

---

**Q18. How does GameController know which card was clicked?**
`GameBoard` creates `CardButton` objects and registers an `ActionListener` on each:
```java
btn.addActionListener(e -> controller.onCardClicked(card));
```
When a button is clicked, the lambda calls `controller.onCardClicked(card)`, passing the model `Card` object. The Controller then applies the click to the Game model.

---

**Q19. Why use packages?**
Packages group related classes together (`model`, `level`, `controller`, `ui`, `util`) and prevent naming conflicts. They also enforce access control — `package-private` members are only visible within their package.

---

## Section 4: Game Logic

**Q20. How does card matching work?**
1. Player clicks card 1 → `game.selectCard(card1)` → `firstSelected = card1`, card1 revealed.
2. Player clicks card 2 → `game.selectCard(card2)` → `secondSelected = card2`, state = WAITING, moves++.
3. After 700ms, `game.evaluateSelection()` compares `firstSelected.getSymbol()` with `secondSelected.getSymbol()`.
4. Equal → both marked matched, score +100, matches++.
5. Not equal → both hidden, score −10.

---

**Q21. How is randomisation done?**
In `Game.buildCards()`, a list of symbols (each appearing twice) is created, then `Collections.shuffle(symbolsToUse)` randomises the order. `Collections.shuffle()` uses a cryptographically-seeded `Random` internally.

---

**Q22. How do you prevent clicking a third card while two are face-up?**
The `GameState` enum is set to `WAITING` while two cards are face-up. At the start of `onCardClicked()`:
```java
if (game.getState() == GameState.WAITING) return;
```
This drops all card clicks until the flip-back timer fires and the state returns to `RUNNING`.

---

**Q23. How do you prevent clicking the same card twice?**
In `game.selectCard()`:
```java
if (card.isRevealed()) return false;
```
A card that is already revealed cannot be selected again.

---

**Q24. How is the score calculated?**
- Correct match: +100
- Wrong pair: −10 (minimum 0)
- On win: time bonus (up to +300) and move bonus (up to +500) are added.
- Time bonus formula: `max(0, 300 - (elapsedSeconds - bestTime) * 2)`
- Move bonus formula: `max(0, 500 - (moves - pairs) * 10)`

---

**Q25. How does the Timer work?**
`javax.swing.Timer` fires `ActionListener` events on the EDT at a specified interval. The clock timer fires every 1000ms and calls `board.updateHud()`. The flip-back timer fires once after 700ms (`setRepeats(false)`) to hide unmatched cards.

---

**Q26. What happens when all pairs are matched?**
`game.isComplete()` returns `true` when `matches == level.getPairs()`. The controller calls `game.finalise()` (which stops the clock and applies bonuses), saves the score, and calls `board.showVictory(score)`. The VictoryDialog is displayed modally.

---

**Q27. How does Restart work?**
`controller.restartGame()`:
1. Stops all timers.
2. Calls `game.restart()` which reshuffles cards and resets counters.
3. Calls `board.rebuildCards()` which rebuilds the card button grid.
4. Starts the clock timer again.

The same `Game` and `GameBoard` objects are reused — no new objects created.

---

**Q28. How do you handle the case where the player closes a dialog by pressing X?**
`VictoryDialog` has `setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE)`. The player must click PLAY AGAIN or MAIN MENU. This prevents the game from being stuck in a WON state without navigating anywhere.

---

## Section 5: File Handling

**Q29. How are high scores stored?**
In a plain-text CSV file at `~/MemoryPuzzleScores.txt` (user's home directory). Each line is:
```
PlayerName,Difficulty,Score,Moves,Time
```
Example: `Aniruth,Medium,1450,22,02:34`

---

**Q30. How do you handle a missing score file?**
`ScoreManager.loadScores()` calls `Files.exists(SCORE_FILE)` first. If the file doesn't exist, it returns an empty `ArrayList` instead of crashing.

---

**Q31. How do you handle a corrupted score entry?**
`Score.fromCsvLine()` wraps `Integer.parseInt()` in a `try/catch (NumberFormatException)`. If a line is malformed it returns `null`, and `loadScores()` skips null entries. The game continues loading valid entries.

---

**Q32. What Java I/O classes did you use?**
- `java.nio.file.Files` — for `newBufferedReader()`, `newBufferedWriter()`, `exists()`, `deleteIfExists()`
- `java.nio.file.Paths` — to construct the file path
- `java.io.BufferedReader` — line-by-line reading
- `java.io.BufferedWriter` — buffered writing

`BufferedReader/Writer` are more efficient than `FileReader/Writer` for line-based I/O.

---

**Q33. Why store scores in the user's home directory?**
It's a location the application always has write access to on any OS, without needing administrator permissions. `System.getProperty("user.home")` returns the correct home folder on Windows, macOS, and Linux.

---

## Section 6: UI / Swing

**Q34. What is the Event Dispatch Thread (EDT)?**
Swing is single-threaded. All GUI operations must happen on the EDT. The `javax.swing.Timer` fires on the EDT automatically. For background work, `SwingWorker` or `SwingUtilities.invokeLater()` should be used.

---

**Q35. How did you make buttons look rounded?**
`RoundedButton` extends `JButton`, disables the default content area paint (`setContentAreaFilled(false)`), and overrides `paintComponent(Graphics g)` to manually draw a rounded rectangle using `Graphics2D.fillRoundRect()` with anti-aliasing enabled.

---

**Q36. How does the card flip animation work?**
`CardButton` has a `flipProgress` float (0.0 = hidden, 1.0 = revealed). A `javax.swing.Timer` increments or decrements `flipProgress` in steps. In `paintComponent`, the drawn card width is scaled by `Math.abs(Math.cos(flipProgress * PI))` — this squishes the card to zero width at 0.5 (the midpoint), then expands it, creating a 3D-like flip illusion.

---

**Q37. What is CardLayout?**
`CardLayout` is a Swing layout manager that stacks multiple panels in the same space. Only one panel is visible at a time. `cardLayout.show(container, name)` switches the visible panel. This is how the game switches between Main Menu, Game Board, High Scores, etc. without opening new windows.

---

**Q38. What is `Graphics2D` and why use it?**
`Graphics2D` extends `Graphics` with more control: anti-aliasing hints, `BasicStroke` for line thickness, `GradientPaint` for gradients, and `RenderingHints` for high-quality text and shape rendering. All custom-painted components cast `Graphics g` to `Graphics2D` and enable anti-aliasing for smooth edges.

---

**Q39. Why are fields in UIUtils `public static final`?**
`UIUtils` is a utility class with no instances. All colours and factories are shared constants that any class can use. `public` — visible everywhere; `static` — belongs to the class, not an instance; `final` — value never changes after initialisation.

---

**Q40. What would you improve if you had more time?**
- Add actual audio using `javax.sound.sampled` (the architecture already has a sound toggle).
- Support keyboard navigation (Tab to select cards, Enter to flip).
- Add a theme switcher (light mode).
- Add a countdown before the game starts so the player can briefly preview all cards.
- Add animated transitions between screens.
- Write unit tests for the model layer with JUnit.

---

## Bonus Questions

**Q41. What is the difference between `List` and `ArrayList`?**
`List` is the interface; `ArrayList` is one concrete implementation. Programming to the interface (`List<Card> cards = new ArrayList<>()`) makes it easy to swap the implementation (e.g., to `LinkedList`) without changing any other code.

**Q42. What does `Collections.shuffle()` do?**
It randomly permutes the elements of a `List` in place, using a default `Random` source. It is called each time a new game or restart happens so the card layout is always different.

**Q43. What is an enum? How is GameState used?**
An enum is a special class with a fixed set of named constants. `GameState` has four constants: `IDLE`, `RUNNING`, `WAITING`, `WON`. The controller and model check this state to decide what actions are allowed, replacing fragile boolean flags with a clear, type-safe representation.

**Q44. What is the `Comparable` interface and where is it used?**
`Comparable<T>` requires implementing `compareTo(T other)`. `Score implements Comparable<Score>` defines natural ordering as highest score first. This allows `Collections.sort(scores)` to sort the leaderboard correctly without needing a separate `Comparator`.

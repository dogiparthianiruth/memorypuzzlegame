# OOP Concepts — Memory Puzzle

A student-friendly explanation of every OOP concept used in this project.

---

## 1. Class

**Definition:** A class is a blueprint that describes the data (fields) and behaviour (methods) of a group of objects.

**In this project:**
`Card.java` is a class. It defines what every card has: a symbol, a revealed flag, a matched flag. It also defines what every card can do: reveal, hide, markMatched.

**Why useful:** Without classes we would repeat the same variables and logic for every single card. With a class we define it once and create as many card objects as we need.

---

## 2. Object

**Definition:** An object is one specific instance of a class. It has its own copy of the class's fields.

**In this project:**
When a game starts, `new Card(0, "🍎")` creates one Card object with id=0 and symbol 🍎. Another call creates a different object.

**Why useful:** Each card on the board is a separate object with its own state (revealed or not, matched or not) even though they all share the same Card class blueprint.

---

## 3. Encapsulation

**Definition:** Keeping the internal data of a class private, and only allowing access through controlled public methods (getters/setters).

**In this project:**
In `Card.java`:
```java
private String  symbol;
private boolean revealed;
private boolean matched;
```
You cannot write `card.revealed = true` from outside the class. You must call `card.reveal()`. This prevents invalid state changes.

**Why useful:** It protects the card from being set to an impossible state (e.g., revealed AND matched by two different callers simultaneously).

---

## 4. Abstraction

**Definition:** Hiding complex implementation details and exposing only what the caller needs to know.

**In this project:**
`GameLevel` is an abstract class. It says: "every difficulty level MUST provide a name, a grid size, a description, and a card size" — but it does not say HOW each level provides those values.

```java
public abstract class GameLevel {
    public abstract String getDescription();
    public abstract int    getCardSize();
}
```

`Game.java` works with `GameLevel level` without ever needing to know whether the level is Easy, Medium, or Hard.

**Why useful:** The game logic is written once and works with any difficulty, present or future.

---

## 5. Inheritance

**Definition:** A child class (subclass) extends a parent class (superclass), inheriting its fields and methods, and optionally overriding them.

**In this project:**
```
GameLevel  (abstract parent)
  ├── EasyLevel   (4×4)
  ├── MediumLevel (6×6)
  └── HardLevel   (8×8)
```

`EasyLevel extends GameLevel` means EasyLevel automatically has the `name`, `gridRows`, `gridCols`, and `pairs` fields from GameLevel. It only needs to provide its own `getDescription()` and `getCardSize()`.

**Why useful:** Avoids repeating the common level structure in all three classes.

---

## 6. Polymorphism

**Definition:** One variable can refer to objects of different types, and calling a method on that variable will execute the correct version for the actual object type at runtime.

**In this project:**
```java
GameLevel level = new EasyLevel();     // or new MediumLevel() or new HardLevel()
System.out.println(level.getDescription());
```
The same call `level.getDescription()` prints different things depending on which object `level` actually points to. This is **runtime polymorphism** via method overriding.

`GameController` and `Game` both accept a `GameLevel` reference. You can pass any of the three levels and the code works without any `if/else` checks.

**Why useful:** Adding a new difficulty (e.g., "Expert 10×10") requires only one new class — no changes to Game or GameController.

---

## 7. Composition

**Definition:** A class is "composed of" other objects as its fields. A "has-a" relationship rather than an "is-a" relationship.

**In this project:**
`Game` contains a `List<Card>` (composition). A game HAS cards.
`GameBoard` contains a `List<CardButton>` (composition). The board IS composed of card buttons.

```java
// Inside Game.java
private final List<Card> cards;   // Game is COMPOSED of Card objects
```

**Why useful:** Represents the real-world relationship accurately. A game doesn't "extend" Card; it owns a collection of cards.

---

## 8. Constructor

**Definition:** A special method that runs automatically when an object is created with `new`. It initialises the object's fields.

**In this project:**
```java
public Card(int id, String symbol) {
    this.id       = id;
    this.symbol   = symbol;
    this.revealed = false;
    this.matched  = false;
}
```
Every time a card is created, it starts hidden and unmatched — guaranteed by the constructor.

**Why useful:** Ensures objects are always in a valid state from the moment they are created.

---

## 9. Method Overriding

**Definition:** A subclass provides its own implementation of a method that is defined in the parent class.

**In this project:**
```java
// GameLevel declares:
public abstract String getDescription();

// EasyLevel overrides:
@Override
public String getDescription() { return "Best for beginners"; }

// HardLevel overrides:
@Override
public String getDescription() { return "For memory masters"; }
```

Also, every custom Swing component overrides `paintComponent(Graphics g)` to draw custom visuals.

**Why useful:** Lets subclasses specialise behaviour without changing the parent or any code that uses the parent type.

---

## 10. Access Modifiers

| Modifier | Visible To |
|----------|------------|
| `private` | Only the same class |
| `protected` | Same class + subclasses + same package |
| `public` | Everyone |
| (none) | Same package only |

**In this project:**
- All model fields are `private` (encapsulation).
- `GameLevel` fields like `name`, `gridRows` are `protected` so subclasses can read them.
- All public methods form the class's API.

---

## 11. ArrayList

**Definition:** A resizable array from `java.util`. It can grow and shrink at runtime unlike a fixed-size array.

**In this project:**
```java
private final List<Card> cards = new ArrayList<>();
```
The number of cards varies by difficulty (16, 36, or 64), so we use ArrayList instead of a hardcoded array.

Also used in `ScoreManager` to hold loaded Score objects before sorting.

---

## 12. Exception Handling

**Definition:** Handling runtime errors gracefully using `try/catch` so the program does not crash.

**In this project:**
In `ScoreManager.loadScores()`:
```java
try (BufferedReader reader = Files.newBufferedReader(SCORE_FILE)) {
    // read scores
} catch (IOException e) {
    // Return what we have — do not crash
}
```
In `Score.fromCsvLine()`:
```java
try {
    int sc = Integer.parseInt(parts[2].trim());
} catch (NumberFormatException e) {
    return null; // Corrupted line — skip it
}
```
**Why useful:** The score file might be missing, empty, or contain a corrupted line. The game must handle this gracefully.

---

## 13. Event Handling

**Definition:** Responding to user actions (clicks, key presses) via listener interfaces.

**In this project:**
Every `CardButton` registers an `ActionListener`:
```java
btn.addActionListener(e -> controller.onCardClicked(card));
```
When the player clicks a card, the lambda calls `GameController.onCardClicked()` which drives the entire card-matching logic.

---

## 14. Swing Timer

**Definition:** `javax.swing.Timer` fires an `ActionListener` at a fixed interval on the Event Dispatch Thread. This is Swing-safe unlike `java.util.Timer`.

**In this project:**
Three timers are used:
1. **Clock timer** — fires every 1 second, updates the Time HUD stat.
2. **Flip-back timer** — fires once after 700 ms, hides unmatched cards.
3. **Card flip timer** (per CardButton) — fires every 18 ms to animate the flip.

Each timer is stopped when no longer needed to prevent background execution.

---

## 15. MVC Architecture

**Definition:** Model-View-Controller separates an application into three layers:
- **Model** — data and business logic
- **View** — what the user sees
- **Controller** — connects the two

**In this project:**

| Layer | Classes |
|-------|---------|
| Model | `Card`, `Game`, `Player`, `Score`, `GameState`, `GameLevel` hierarchy |
| View  | All `ui/` classes (`GameBoard`, `CardButton`, `StatCard`, etc.) |
| Controller | `GameController` |

**Why useful:**
- The Model has no Swing imports — it can be tested without a display.
- The View has no game rules — swapping to a different UI only requires replacing View classes.
- The Controller is the only class that "knows about" both the Model and the View.

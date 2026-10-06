package model;

/**
 * Player.java
 * Holds the current player's name.
 *
 * OOP: Encapsulation – name is private with getter/setter.
 */
public class Player {

    private String name;

    public Player(String name) {
        this.name = (name != null && !name.isBlank()) ? name.trim() : "Player";
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = (name != null && !name.isBlank()) ? name.trim() : "Player";
    }

    @Override
    public String toString() {
        return "Player{name='" + name + "'}";
    }
}

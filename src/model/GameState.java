package model;

/**
 * GameState.java
 * Enum that represents the lifecycle states of a game session.
 *
 * OOP: Used by Game and GameController for state management.
 */
public enum GameState {
    /** Game has been created but not started. */
    IDLE,

    /** Game is actively being played. */
    RUNNING,

    /** Two cards are face-up and awaiting the hide-delay. */
    WAITING,

    /** All pairs have been matched – player won. */
    WON
}

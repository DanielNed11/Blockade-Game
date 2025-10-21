package be.kdg.integration2.mvpglobal.model;

import java.util.Random;

/**
 * Represents a six-sided die associated with a specific game color.
 * <p>
 * Each {@code Dice} can be rolled to generate a random value between 1 and 6.
 * The die is linked to an {@link ElementColors} value, allowing it to represent a specific player's
 * or element's movement potential in the game.
 */
public class Dice {
    private int value;
    private final Random die;
    private final ElementColors color;

    /**
     * Constructs a new die with a fixed associated color.
     * Initializes the internal random number generator.
     *
     * @param color the color associated with this die, used for identifying the related player or element
     */
     Dice(ElementColors color) {
        die = new Random();
        this.color = color;
    }

     void rollDie() {
        value = die.nextInt(6) + 1;
    }

    public int getValue() {
        return value;
    }

     ElementColors getColor() {
        return color;
    }
}

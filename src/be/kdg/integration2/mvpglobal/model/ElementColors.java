package be.kdg.integration2.mvpglobal.model;

import javafx.scene.paint.Color;

/**
 * Represents all element colors used in the game, including player-controlled colors and special-purpose ones.
 * <p>
 * Each {@code ElementColors} constant is associated with a specific JavaFX {@link Color}, used for rendering
 * game pieces and dice in the UI layer.
 * <p>
 */
public enum ElementColors {

    // Pieces and dice
    BLUE(Color.BLUE),
    RED(Color.RED),
    GREEN(Color.GREEN),
    YELLOW(Color.web("#bdc708")),

    //Used only for Pieces
    BLACK(Color.web("#1C1C1C")),
    CLEAR(Color.rgb(220, 220, 220, 0.5));

    private final Color color;

    ElementColors(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}

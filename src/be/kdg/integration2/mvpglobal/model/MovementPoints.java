package be.kdg.integration2.mvpglobal.model;

import java.util.EnumMap;
import java.util.Map;

/**
 * Represents a data structure that tracks movement points for each {@link ElementColors} value.
 * <p>
 * Movement points are used during a turn to determine how far pieces of a given color can move.
 * This class provides functionality to add, consume, and reset points per color.
 * <p>
 * Internally, it uses an {@link EnumMap} to optimize memory and performance for the fixed enum key set.
 */
public class MovementPoints {

    private final Map<ElementColors, Integer> points;

    /**
     * Constructs a new {@code MovementPoints} object, initializing
     * all color values to zero.
     */
    public MovementPoints() {
        points = new EnumMap<>(ElementColors.class);
        for (ElementColors color : ElementColors.values()) {
            points.put(color, 0);
        }
    }

    /**
     * Adds a number of movement points to a specific color.
     *
     * @param color  the color to assign points to
     * @param amount the number of points to add
     */
    public void addPoints(ElementColors color, int amount) {
        points.put(color, points.getOrDefault(color, 0) + amount);
    }

    /**
     * Attempts to use a specific number of movement points for a color.
     * <p>
     * If the color is not registered or has insufficient points, an error is thrown or no deduction occurs.
     *
     * @param color        the color to consume points from
     * @param takingPoints how many points to deduct
     * @throws IllegalArgumentException if the color is not found
     */
    public void usePoint(ElementColors color, int takingPoints) {
        if (!points.containsKey(color)) {
            throw new IllegalArgumentException("Color " + color + " not found in movement points.");
        }

        int current = points.get(color);
        if (current > 0) {
            points.put(color, current - takingPoints);
        }
    }

    public int getPoints(ElementColors color) {
        return points.getOrDefault(color, 0);
    }

    /**
     * Resets all movement points for every color to zero.
     */
    public void reset() {
        for (ElementColors color : ElementColors.values()) {
            points.put(color, 0);
        }
    }

    public Map<ElementColors, Integer> getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return "MovementPoints" + points;
    }
}

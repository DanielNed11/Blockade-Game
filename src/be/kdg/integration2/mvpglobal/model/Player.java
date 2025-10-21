package be.kdg.integration2.mvpglobal.model;

/**
 * Represents a player in the game, either human or AI.
 * <p>
 * A player is identified by a name and controls two distinct {@link ElementColors}.
 * This class is used by both human and computer player types as a shared base.
 */
public class Player {

    private String name;
    private ElementColors controlledColorOne;
    private ElementColors controlledColorTwo;

    /**
     * Constructs a {@code Player} with a name and two controlled colors.
     *
     * @param name                the name of the player
     */
    public Player(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ElementColors getControlledColorOne() {
        return controlledColorOne;
    }

    public void setControlledColorOne(ElementColors controlledColorOne) {
        this.controlledColorOne = controlledColorOne;
    }

    public ElementColors getControlledColorTwo() {
        return controlledColorTwo;
    }

    public void setControlledColorTwo(ElementColors controlledColorTwo) {
        this.controlledColorTwo = controlledColorTwo;
    }
}

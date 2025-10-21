package be.kdg.integration2.mvpglobal.model;

public class HumanPlayer extends Player{

    private final int playerID;

    public HumanPlayer (String name, int playerID) {
        super(name);
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }
}

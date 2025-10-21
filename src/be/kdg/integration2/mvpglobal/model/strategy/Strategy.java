package be.kdg.integration2.mvpglobal.model.strategy;

import be.kdg.integration2.mvpglobal.model.GameSession;

public interface Strategy {
    void makeAIMove(GameSession session);
}

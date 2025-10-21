package be.kdg.integration2.mvpglobal.model.strategy;

import be.kdg.integration2.mvpglobal.model.GameSession;
import be.kdg.integration2.mvpglobal.model.strategy.ruleBasedSystem.InferenceEngine;

public class RuleBasedStrategy implements Strategy {

InferenceEngine inferenceEngine = new InferenceEngine();
    @Override
    public void makeAIMove(GameSession session) {

        inferenceEngine.determineFacts(session);
        inferenceEngine.applyRules(session);
    }

}

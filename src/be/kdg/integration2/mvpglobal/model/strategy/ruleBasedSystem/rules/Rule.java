package be.kdg.integration2.mvpglobal.model.strategy.ruleBasedSystem.rules;

import be.kdg.integration2.mvpglobal.model.GameSession;
import be.kdg.integration2.mvpglobal.model.strategy.ruleBasedSystem.facts.FactHandler;

public abstract class Rule {
    public abstract boolean conditionRule(FactHandler facts);
    public abstract boolean actionRule(FactHandler facts, GameSession game);
}

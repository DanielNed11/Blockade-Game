package be.kdg.integration2.mvpglobal.model.strategy.ruleBasedSystem.rules;

import be.kdg.integration2.mvpglobal.model.GameSession;
import be.kdg.integration2.mvpglobal.model.strategy.ruleBasedSystem.facts.FactHandler;

import java.util.ArrayList;
import java.util.List;

public class RulesHandler {
    private final List<Rule> rules;

    public RulesHandler() {
        this.rules = new ArrayList<>();
        rules.add(new MakeBlockade());
        rules.add(new SelectSmallToBlockade());
        rules.add(new MidToLarge());
        rules.add(new SelectMid());
        rules.add(new MakeRandomMove());
    }

    public boolean fireActionRule(int index, FactHandler facts, GameSession game) {
        return rules.get(index).actionRule(facts, game);
    }

    public boolean checkConditionRule(int index, FactHandler facts) {
        return rules.get(index).conditionRule(facts);
    }

    public int numberOfRules() {
        return rules.size();
    }

}
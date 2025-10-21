package be.kdg.integration2.mvpglobal.model.strategy.ruleBasedSystem;

import be.kdg.integration2.mvpglobal.model.GameSession;
import be.kdg.integration2.mvpglobal.model.Tile;
import be.kdg.integration2.mvpglobal.model.strategy.ruleBasedSystem.facts.FactHandler;
import be.kdg.integration2.mvpglobal.model.strategy.ruleBasedSystem.facts.FactValues;
import be.kdg.integration2.mvpglobal.model.strategy.ruleBasedSystem.rules.RulesHandler;

public class InferenceEngine {
    private final FactHandler currentFacts = new FactHandler();
    private final RulesHandler currentRules = new RulesHandler();

    public void determineFacts(GameSession gameSession) {
        currentFacts.resetFacts();
        currentFacts.setFactsEvolved(false);



        if (gameSession.getSelectedTile() != null) {
            Tile selectedTile = gameSession.getSelectedTile();
            if (gameSession.canBlockade(selectedTile)) {
                currentFacts.addFact(FactValues.FORM_BLOCKADE);

            } else if (gameSession.checkForOpenLargeWithReachableMid(selectedTile)) {
                currentFacts.addFact(FactValues.MOVE_MID_TO_LARGE);

            } else {
                currentFacts.addFact(FactValues.MAKE_RANDOM_MOVE);

            }
        } else {
            if (gameSession.checkForSmallAndStackedLargeMediumSameColor()) {
                currentFacts.addFact(FactValues.SELECT_SMALL_TO_BLOCKADE);
                System.out.println("1");
            }
            else if (gameSession.checkForOpenLargeAndMidFromSameColor()) {
                currentFacts.addFact(FactValues.SELECT_MEDIUM);
                System.out.println("2");
            } else if (!gameSession.checkForSmallAndStackedLargeMediumSameColor() && !gameSession.checkForOpenLargeAndMidFromSameColor()){
                currentFacts.addFact(FactValues.MAKE_RANDOM_MOVE);
                System.out.println("3");
            }
        }


    }

    public void applyRules(GameSession game) {

        if (currentFacts.factsObserved()) {
            for (int attempt = 0; attempt < currentRules.numberOfRules(); attempt++) {

                for (int i = 0; i < currentRules.numberOfRules(); i++) {
                    if (currentRules.checkConditionRule(i, currentFacts)) {
                        if (currentRules.fireActionRule(i, currentFacts, game)) {
                            break;
                        } else {
                            currentFacts.resetFacts();
                        }
                    }
                }
            }
        }
    }
}
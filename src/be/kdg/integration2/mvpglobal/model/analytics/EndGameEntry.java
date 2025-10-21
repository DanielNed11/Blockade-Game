package be.kdg.integration2.mvpglobal.model.analytics;

public record EndGameEntry(String winner, int playTimeSeconds, int turnForPlayer, int turnForComputer, double avgPlayer,
                           double avgComputer) {
}

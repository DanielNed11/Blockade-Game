package be.kdg.integration2.mvpglobal.model.analytics;

public record LeaderboardEntry(String playerName, int totalWins, int totalLosses, double winLossRatio) {
}
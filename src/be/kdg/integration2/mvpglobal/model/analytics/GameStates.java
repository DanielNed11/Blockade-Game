package be.kdg.integration2.mvpglobal.model.analytics;


import java.time.Duration;
import java.time.LocalDateTime;

public class GameStates {
    private final int moveNumber;
    private final LocalDateTime moveStartTime;
    private final LocalDateTime moveEndTime;
    private final Duration moveDuration;
    private final String moveDescription;
    private String winner;
    private int turnCount;
    private int avgTurnDuration;

    public GameStates(int moveNumber, LocalDateTime moveStartTime,
                          LocalDateTime moveEndTime, String moveDescription) {
        this.moveNumber = moveNumber;
        this.moveStartTime = moveStartTime;
        this.moveEndTime = moveEndTime;
        this.moveDuration = Duration.between(moveStartTime, moveEndTime);
        this.moveDescription = moveDescription;
    }

    public GameStates(int moveNumber, LocalDateTime moveStartTime,
                          Duration moveDuration, String moveDescription) {
        this.moveNumber = moveNumber;
        this.moveStartTime = moveStartTime;
        this.moveEndTime = moveStartTime.plus(moveDuration);
        this.moveDuration = moveDuration;
        this.moveDescription = moveDescription;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public LocalDateTime getMoveStartTime() {
        return moveStartTime;
    }

    public LocalDateTime getMoveEndTime() {
        return moveEndTime;
    }

    public Duration getMoveDuration() {
        return moveDuration;
    }

    public String getMoveDescription() {
        return moveDescription;
    }

    public double getDurationInSeconds() {
        return moveDuration.toMillis() / 1000.0;
    }

    @Override
    public String toString() {
        return "GameStatistics{" +
                "moveNumber=" + moveNumber +
                ", moveDuration=" + moveDuration +
                ", moveDescription='" + moveDescription + '\'' +
                '}';
    }
}

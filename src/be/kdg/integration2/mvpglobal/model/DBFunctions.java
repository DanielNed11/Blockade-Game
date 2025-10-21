package be.kdg.integration2.mvpglobal.model;

import be.kdg.integration2.mvpglobal.model.analytics.EndGameEntry;
import be.kdg.integration2.mvpglobal.model.analytics.LeaderboardEntry;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBFunctions {

    // Info we need to connect to the database
    /**Connecting to the Database is no longer possible since the server is closed*/
    private static final String URL = "";
    private static final String USER = "";
    private static final String PASSWORD = "";

    public static Connection connectToDatabase() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static HumanPlayer register(String playerName, String playerEmail) throws SQLException {
        String statement = "INSERT INTO players (player_name, player_email) VALUES (?, ?) RETURNING player_name, player_email";
        return executePlayerQuery(statement, playerName, playerEmail, true);
    }

    public static HumanPlayer login(String playerName, String playerEmail) throws SQLException {
        String statement = "SELECT player_name, player_email FROM players WHERE player_name = ? AND player_email = ?";
        return executePlayerQuery(statement, playerName, playerEmail, false);
    }

    private static HumanPlayer executePlayerQuery(String query, String playerName, String playerEmail, boolean isPlayerCreated) throws SQLException {
        try (Connection conn = connectToDatabase();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, playerName);
            ps.setString(2, playerEmail);

            ResultSet rs = ps.executeQuery();

            int playerId;

            try (PreparedStatement psID = conn.prepareStatement
                    ("SELECT player_id FROM players WHERE player_name = ? AND player_email = ?")) {

                psID.setString(1, playerName);
                psID.setString(2, playerEmail);

                ResultSet rsID = psID.executeQuery();

                if (rsID.next()) {
                    playerId = rsID.getInt("player_id");
                } else {
                    throw new SQLException("Error retrieving player from the database");
                }
            }

            if (rs.next()) {
                return new HumanPlayer(rs.getString("player_name"), playerId);
            } else if (!isPlayerCreated) {
                throw new SQLException("Player with name " + playerName + " and " + playerEmail + " does not exist");
            } else {
                throw new SQLException("Player with name " + playerName + " already exists");
            }
        }
    }

    public static List<LeaderboardEntry> leaderboard() throws SQLException {
        List<LeaderboardEntry> leaderboardEntries = new ArrayList<>();
        String statement = """
                    SELECT
                        p.player_name,
                        COUNT(CASE WHEN g.winner = p.player_name THEN 1 END) AS total_wins,
                        COUNT(CASE WHEN g.winner IS NOT NULL AND g.winner != p.player_name THEN 1 END) AS total_losses,
                        CASE
                            WHEN COUNT(CASE WHEN g.winner IS NOT NULL AND g.winner != p.player_name THEN 1 END) = 0
                                THEN COUNT(CASE WHEN g.winner = p.player_name THEN 1 END)
                            ELSE
                                COUNT(CASE WHEN g.winner = p.player_name THEN 1 END) * 1.0 /
                                COUNT(CASE WHEN g.winner IS NOT NULL AND g.winner != p.player_name THEN 1 END)
                        END AS win_loss_ratio
                    FROM players p
                    LEFT JOIN games g ON p.player_id = g.playerid
                    GROUP BY p.player_id, p.player_name
                    ORDER BY total_wins DESC;
                """;

        try (Connection conn = connectToDatabase();
             PreparedStatement ps = conn.prepareStatement(statement);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String playerName = rs.getString("player_name");
                int totalWins = rs.getInt("total_wins");
                int totalLosses = rs.getInt("total_losses");
                double winLossRatio = rs.getDouble("win_loss_ratio");

                LeaderboardEntry entry = new LeaderboardEntry(
                        playerName, totalWins, totalLosses, winLossRatio
                );
                leaderboardEntries.add(entry);
            }
        } catch (SQLException e) {
            throw new SQLException("Error retrieving leaderboard: " + e.getMessage(), e);
        }

        return leaderboardEntries;
    }

    public static int beginGame(int playerId) throws SQLException {
        String sql =
                "INSERT INTO games (start_time, playerid) " +
                        "VALUES (CURRENT_TIMESTAMP, ?)";
        try (Connection conn = connectToDatabase();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, playerId);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Creating game failed, no ID obtained.");
                }
            }
        }
    }

    // SAVE A MOVE
    public static void saveMove(
            int gameId,
            String playerType,
            int fromRow,
            int fromCol,
            int toRow,
            int toCol,
            int moveNumber,
            int duration
    ) throws SQLException {
        String sql = """
                    INSERT INTO moves (
                      gameid,
                      player_type,
                      from_row,
                      from_col,
                      to_row,
                      to_col,
                      move_number,
                      move_duration
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = connectToDatabase();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, gameId);
            ps.setString(2, playerType);
            ps.setInt(3, fromRow);
            ps.setInt(4, fromCol);
            ps.setInt(5, toRow);
            ps.setInt(6, toCol);
            ps.setInt(7, moveNumber);
            ps.setInt(8, duration);

            ps.executeUpdate();
        }
    }

    public static void saveEndGame(int gameId, String winnerName, int moveCount) throws SQLException {
        String updateSql = """
                    UPDATE games
                    SET
                        end_time = CURRENT_TIMESTAMP,
                        winner = ?,
                        number_of_turns = ?
                    WHERE gameid = ?
                """;

        try (Connection conn = connectToDatabase();
             PreparedStatement ps = conn.prepareStatement(updateSql)) {

            ps.setString(1, winnerName);
            ps.setInt(2, moveCount);
            ps.setInt(3, gameId);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated != 1) {
                throw new SQLException("Failed to update game record for gameId = " + gameId);
            }
        }
    }

    public static EndGameEntry getEndGameSummary(int gameId) throws SQLException {
        String gameQuery = """
        SELECT
            g.winner,
            EXTRACT(EPOCH FROM (g.end_time - g.start_time))::INT AS total_play_time_seconds,
            p1.player_name AS player_1_name,
            COUNT(CASE WHEN m.player_type = 'Player' THEN 1 END) AS player_1_moves,
            AVG(CASE WHEN m.player_type = 'Player' THEN m.move_duration END)::FLOAT AS player_1_avg_move_duration,
            COUNT(CASE WHEN m.player_type = 'Computer' THEN 1 END) AS computer_moves,
            AVG(CASE WHEN m.player_type = 'Computer' THEN m.move_duration END)::FLOAT AS computer_avg_move_duration
        FROM games g
        JOIN moves m ON g.gameid = m.gameid
        JOIN players p1 ON g.playerid = p1.player_id
        WHERE g.gameid = ?
        GROUP BY g.winner, g.start_time, g.end_time, p1.player_name
    """;

        try (Connection conn = connectToDatabase();
             PreparedStatement ps = conn.prepareStatement(gameQuery)) {

            ps.setInt(1, gameId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new EndGameEntry(
                            rs.getString("winner"),
                            rs.getInt("total_play_time_seconds"),
                            rs.getInt("player_1_moves"),
                            rs.getInt("computer_moves"),
                            rs.getDouble("player_1_avg_move_duration"),
                            rs.getDouble("computer_avg_move_duration")
                    );
                } else {
                    throw new SQLException("No summary found for gameId: " + gameId);
                }
            }
        }
    }

    public static Map<String, List<Integer>> getMoveDurationsByPlayer(int gameId) throws SQLException {
        String sql = """
        SELECT player_type, move_duration
        FROM moves
        WHERE gameid = ?
        ORDER BY move_number
    """;

        Map<String, List<Integer>> durations = new HashMap<>();
        durations.put("Player", new ArrayList<>());
        durations.put("Computer", new ArrayList<>());

        try (Connection conn = connectToDatabase();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, gameId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String type = rs.getString("player_type");
                    int duration = rs.getInt("move_duration");
                    durations.get(type).add(duration);
                }
            }
        }
        return durations;
    }

}
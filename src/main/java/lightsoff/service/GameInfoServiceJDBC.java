package lightsoff.service;

import lightsoff.entity.GameInfo;

import java.sql.*;

public class GameInfoServiceJDBC implements GameInfoService{
    public static final String URL = "jdbc:postgresql://localhost:5433/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "panda_25";

    public static final String SELECT = "SELECT game, info FROM game_info WHERE game = ?";
    public static  final String DELETE = "DELETE FROM game_info";
    public static final String INSERT = "INSERT INTO game_info (game, info) VALUES (?, ?)";

    @Override
    public void addGameInfo(GameInfo gameInfo) throws GameInfoException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(INSERT)
        ) {
            statement.setString(1, gameInfo.getGame());
            statement.setString(2, gameInfo.getInfo());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GameInfoException("Problem inserting Game info", e);
        }
    }

    @Override
    public String getGameInfo(String game) throws GameInfoException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT)
        ) {
            statement.setString(1, game);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(2);
                }
                return "Empty";
            }
        } catch (SQLException e) {
            throw new GameInfoException("Problem selecting game info", e);
        }
    }

    @Override
    public void reset() throws GameInfoException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(DELETE)
        ) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GameInfoException("Problem deleting Game info", e);
        }
    }
}

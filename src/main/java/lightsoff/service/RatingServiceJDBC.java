package lightsoff.service;

import lightsoff.entity.Rating;

import java.sql.*;

public class RatingServiceJDBC implements RatingService{
    public static final String URL = "jdbc:postgresql://localhost:5433/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "panda_25";
    public static final String SELECT = "SELECT game, player, rating, ratedOn FROM rating WHERE game = ? AND player = ? ORDER BY rating DESC LIMIT 10";
    public static final String SELECT_AVG = "SELECT AVG(rating) from rating WHERE game = ?";
    public static final String DELETE = "DELETE FROM rating";
    public static final String INSERT = "INSERT INTO rating (game, player, rating, ratedOn) VALUES (?, ?, ?, ?)";
    public static final String UPDATE_RATING = "UPDATE rating SET rating = ? WHERE game = ? AND player = ?";

    @Override
    public void setRating(Rating rating) throws RatingException {
        if(getRating(rating.getGame(),rating.getPlayer()) == -1){ //if it does not exist in table
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement statement = connection.prepareStatement(INSERT);
            ) {
                 statement.setString(1,rating.getGame());
                 statement.setString(2,rating.getPlayer());
                 statement.setInt(3,rating.getRating());
                 statement.setTimestamp(4,new Timestamp(rating.getRatedOn().getTime()));
                 statement.executeUpdate();
            } catch (SQLException e) {
                throw new RatingException("Problem inserting new rating", e);
            }
        }
        else{   //if it exists
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement statement = connection.prepareStatement(UPDATE_RATING);
            ) {
                statement.setInt(1,rating.getRating());
                statement.setString(2,rating.getGame());
                statement.setString(3,rating.getPlayer());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RatingException("Problem updating new rating", e);
            }
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT_AVG);
        ) {
            statement.setString(1,game);
            try (ResultSet rs = statement.executeQuery()) {
                if(rs.next()){
                    return rs.getInt(1);
                }
                return -1;
            }
        } catch (SQLException e) {
            throw new RatingException("Problem getting average rating", e);
        }
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT);
        ) {
            statement.setString(1, game);
            statement.setString(2, player);
            try (ResultSet rs = statement.executeQuery()) {
                if(rs.next()){
                    return rs.getInt(3);
                }
                return -1;
            }
        } catch (SQLException e) {
            throw new RatingException("Problem selecting rating", e);
        }
    }

    @Override
    public void reset() throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new RatingException("Problem deleting rating", e);
        }
    }
}

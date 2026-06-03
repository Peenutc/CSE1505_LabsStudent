package tudelft.wis.idm_tasks.boardGameTracker;

import tudelft.wis.idm_tasks.boardGameTracker.interfaces.BgtDataManager;
import tudelft.wis.idm_tasks.boardGameTracker.interfaces.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class BgtData implements BgtDataManager {
    String username = "postgres";
    String password = "Huanchen6!";
    Connection connection = null;

    public Connection getConnection() throws SQLException, ClassNotFoundException{
            String connectionString = "jdbc:postgresql://localhost:5432/boardgame_tracker";
            if(connection==null){
                Properties prop = new Properties();
                prop.setProperty("user", username);
                prop.setProperty("password", password);
                connection = DriverManager.getConnection(connectionString, prop);
            }
            return connection;
    }


    public BgtData(){
        setupTables();
    }

    public void setupTables(){
        String sqlPlayers = "CREATE TABLE IF NOT EXISTS players (" +
                "player_id SERIAL PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL, " +
                "nickname VARCHAR(255) NOT NULL);";

        String sqlBoardGames ="CREATE TABLE IF NOT EXISTS boardgames (" +
                "boardgame_id SERIAL PRIMARY KEY," +
                "name VARCHAR(255) NOT NULL," +
                "bggURL VARCHAR(255) NOT NULL);";

        String sqlPlayerGames ="CREATE TABLE IF NOT EXISTS player_games (" +
                "player_id INT REFERENCES players(player_id) ON DELETE CASCADE" +
                "game_id INT REFERENCES boardgames(boardgame_id) ON DELETE CASCADE" +
                "PRIMARY KEY (player_id, game_id));";

        String sqlSessions ="CREATE TABLE IF NOT EXISTS play_sessions(" +
                "session_id SERIAL PRIMARY KEY," +
                "session_date DATE NOT NULL," +
                "play_time INT NOT NULL," +
                "game_id INT REFERENCES boardgames(boardgame_id) ON DELETE CASCADE" +
                "host_id INT REFERENCES players(player_id) ON DELETE CASCADE" +
                "winner_id INT REFERENCES players(player_id) ON DELETE CASCADE);";

        String sqlPlayerSessions ="CREATE TABLE IF NOT EXISTS players_sessions(" +
                "player_id INT REFERENCES players(player_id) ON DELETE CASCADE" +
                "session_id INT REFERENCES session(session_id) ON DELETE CASCADE" +
                "PRIMARY KEY (player_id, session_id));";
    }


    @Override
    public Player createNewPlayer(String name, String nickname) throws BgtException {
        String sql = "INSERT INTO players (name, nickname) VALUES (?, ?);";

        try(Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            stmt.setString(1, name);
            stmt.setString(2, nickname);

            stmt.executeQuery();

            try(ResultSet generatedKeys = stmt.getGeneratedKeys()){
                if(generatedKeys.next()){
                    int generatedId = generatedKeys.getInt(1);
                    return new
                }
            }
        }
    }
}

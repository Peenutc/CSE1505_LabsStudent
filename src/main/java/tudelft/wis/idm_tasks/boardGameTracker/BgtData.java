package tudelft.wis.idm_tasks.boardGameTracker;

import tudelft.wis.idm_tasks.boardGameTracker.interfaces.BgtDataManager;
import tudelft.wis.idm_tasks.boardGameTracker.interfaces.BoardGame;
import tudelft.wis.idm_tasks.boardGameTracker.interfaces.PlaySession;
import tudelft.wis.idm_tasks.boardGameTracker.interfaces.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
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
                "player_id INT REFERENCES players(player_id) ON DELETE CASCADE," +
                "game_id INT REFERENCES boardgames(boardgame_id) ON DELETE CASCADE," +
                "PRIMARY KEY (player_id, game_id));";

        String sqlSessions ="CREATE TABLE IF NOT EXISTS play_sessions(" +
                "session_id SERIAL PRIMARY KEY," +
                "session_date DATE NOT NULL," +
                "play_time INT NOT NULL," +
                "game_id INT REFERENCES boardgames(boardgame_id) ON DELETE CASCADE," +
                "host_id INT REFERENCES players(player_id) ON DELETE CASCADE," +
                "winner_id INT REFERENCES players(player_id) ON DELETE CASCADE);";

        String sqlPlayerSessions ="CREATE TABLE IF NOT EXISTS session_players(" +
                "player_id INT REFERENCES players(player_id) ON DELETE CASCADE," +
                "session_id INT REFERENCES play_sessions(session_id) ON DELETE CASCADE," +
                "PRIMARY KEY (player_id, session_id));";


        try(Connection conn = getConnection();
        Statement stmt = conn.createStatement()){
            stmt.execute(sqlPlayers);
            stmt.execute(sqlBoardGames);
            stmt.execute(sqlPlayerGames);
            stmt.execute(sqlSessions);
            stmt.execute(sqlPlayerSessions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public Player createNewPlayer(String name, String nickname) throws BgtException {
        String sql = "INSERT INTO players (name, nickname) VALUES (?, ?);";

        try(Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            stmt.setString(1, name);
            stmt.setString(2, nickname);

            stmt.executeUpdate();

            try(ResultSet generatedKeys = stmt.getGeneratedKeys()){
                if(generatedKeys.next()){
                    int generatedId = generatedKeys.getInt(1);
                    return new Player_JDBC(generatedId, name, nickname, this);
                }else{
                    throw new BgtException("Creating player failed");
                }
            } catch (Exception e) {
                throw new BgtException("Failed");
            }
        }
    }

    @Override
    public Collection<Player> findPlayersByName(String name) throws BgtException {
        List<Player> players = new ArrayList<>();

        String sql = "SELECT * FROM players WHERE name = ?;";

        try(Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, name);

            ResultSet rs =stmt.executeQuery();

            while(rs.next()){
                int id = rs.getInt("player_id");
                String Pname = rs.getString(name);
                String nickname = rs.getString(nickname);
                players.add(new Player_JDBC(id, Pname, nickname, this));
            }

        } catch (Exception e) {
            throw new BgtException();
        }

        return players;
    }

    @Override
    public BoardGame createNewBoardgame(String name, String bggURL) throws BgtException {
        String sql = "INSERT INTO boardgames (name, bggURL) VALUES (?, ?);";

        try(Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS) ){
            stmt.setString(1,name);
            stmt.setString(2,bggURL);
            stmt.executeUpdate();
            try(ResultSet generatedKeys = stmt.getGeneratedKeys()){
                if(generatedKeys.next()){
                    int generatedId = generatedKeys.getInt(1);
                    return new Boardgame_JDBC(generatedId, name, bggURL);
                }else{
                    throw new BgtException();
                }
            }
        }catch(Exception e){
            throw new BgtException();
        }
    }

    @Override
    public Collection<BoardGame> findGamesByName(String name) throws BgtException {
        List<BoardGame> boardGames = new ArrayList<>();
        String sql = "SELECT * FROM boardgames WHERE name = ?;";

        try(Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                int id = rs.getInt("boardgame_id");
                String Bname = rs.getString("name");
                String bggURL = rs.getString("bggURL");

                boardGames.add(new Boardgame_JDBC(id, Bname, bggURL));
            }
        }catch(Exception e){
            throw new BgtException();
        }
        return boardGames;
    }

    @Override
    public PlaySession createNewPlaySession(Date date, Player host, BoardGame game, int playtime,
                                            Collection<Player> players ,Player winner) throws BgtException {
        //TODO
    }

    @Override
    public Collection<PlaySession> findSessionByDate(Date date) throws BgtException {
        //TODO
    }

    @Override
    public void persistPlaySession(PlaySession session) {

        // @TODO: Implement this method.
    }
    @Override
    public void persistBoardGame(BoardGame game){

    // @TODO: Implement this method.

}

}

package tudelft.wis.idm_tasks.boardGameTracker;

import tudelft.wis.idm_tasks.boardGameTracker.interfaces.BoardGame;
import tudelft.wis.idm_tasks.boardGameTracker.interfaces.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

public class Player_JDBC implements Player {

    private int id;
    private String name;
    private String nickname;
    private BgtData dataManager;

    public Player_JDBC(int id, String name, String nickname, BgtData dataManager) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.dataManager = dataManager;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getPlayerName() {
        return name;
    }

    @Override
    public String getPlayerNickName() {
        return nickname;
    }

    @Override
    public Collection<BoardGame> getGameCollection() {
        ArrayList<BoardGame> boardGames = new ArrayList<>();

        String sql ="SELECT b.boardgame_id, b.name, b.bggURL " +
                "FROM boardgames b JOIN  player_games pg " +
                "ON b.game_id = pg.game_id " +
                "WHERE pg.player_id = ?";

        try(Connection conn = dataManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, this.id);

            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    BoardGame game = new Boardgame_JDBC(rs.getInt(1), rs.getString(2), rs.getString(3));
                    boardGames.add(game);
                }
            }catch(Exception e){
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return boardGames;
    }


    @Override
    public String toVerboseString() {
        String boardGames = "";

        for(BoardGame game: getGameCollection()){
            boardGames += game.toVerboseString();
        }

        return "id: " + id + "name: " + name + "nickame: " + nickname + "boardGames: " + boardGames;
    }
}

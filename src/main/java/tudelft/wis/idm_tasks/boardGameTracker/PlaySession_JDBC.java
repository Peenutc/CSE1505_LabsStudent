package tudelft.wis.idm_tasks.boardGameTracker;

import tudelft.wis.idm_tasks.boardGameTracker.interfaces.BoardGame;
import tudelft.wis.idm_tasks.boardGameTracker.interfaces.PlaySession;
import tudelft.wis.idm_tasks.boardGameTracker.interfaces.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class PlaySession_JDBC implements PlaySession {

    private int id;
    private Date date;
    private int playtime;
    private BoardGame game;
    private Player host;
    private Player winner;
    private BgtData dataManager;

    public PlaySession_JDBC(int id, Date date, int playtime, BoardGame game, Player host, Player winner, BgtData dataManager) {
        this.id =id;
        this.date =date;
        this.playtime =playtime;
        this.game=game;
        this.host=host;
        this.winner=winner;
        this.dataManager=dataManager;
    }

    public int getId() {
        return id;
    }

    @Override
    public Player getHost(){
        return host;
    }

    @Override
    public BoardGame getGame(){
        return game;
    }

    @Override
    public Player getWinner(){
        return winner;
    }

    @Override
    public int getPlaytime(){
        return playtime;
    }


    @Override
    public Date getDate(){
        return date;
    }

    @Override
    public Collection<Player> getAllPlayers(){
        ArrayList<Player> players=new ArrayList<>();

        String sql = "SELECT p.player_id, p.name, p.nickname " +
                "FROM player p JOIN session_players sp " +
                "ON sp.player_id = p.player_id " +
                "WHERE sp.session_id = ?";

        try(Connection conn = dataManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, this.id);

            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Player player = new Player_JDBC(rs.getInt(1), rs.getString(2), rs.getString(3) , this.dataManager);
                players.add(player);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return players;
    }
}

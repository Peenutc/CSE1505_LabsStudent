package tudelft.wis.idm_tasks.boardGameTracker;

import tudelft.wis.idm_tasks.boardGameTracker.interfaces.BoardGame;
import tudelft.wis.idm_tasks.boardGameTracker.interfaces.Player;

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

        String sql =
    }
}

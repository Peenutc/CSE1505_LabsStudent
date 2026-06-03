package tudelft.wis.idm_tasks.boardGameTracker;

import tudelft.wis.idm_tasks.boardGameTracker.interfaces.BoardGame;

public class Boardgame_JDBC  implements BoardGame {

    private int id;
    private String name;
    private String bggURL;

    public Boardgame_JDBC(int id, String name, String bggURL) {
        this.id = id;
        this.name = name;
        this.bggURL = bggURL;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBGG_URL() {
        return bggURL;
    }

    @Override
    public String toVerboseString() {
        return id + " " + name + " " + bggURL;
    }
}

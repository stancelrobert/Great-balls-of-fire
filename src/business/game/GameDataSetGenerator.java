package business.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Robert on 25.04.2017.
 */
public class GameDataSetGenerator {
    private List<Player> players = new ArrayList<>(Game.MAX_PLAYERS_NUMBER);
    private Player benchmarkPlayer;
    private PlayerMovementTask movementTask;

    public void setBenchmarkPlayer(Player benchmarkPlayer, PlayerMovementTask movementTask) {
        this.benchmarkPlayer = benchmarkPlayer;
        this.movementTask = movementTask;

    }

    public GameDataSetGenerator(List<Player> players) {
        this.players = players;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    @Override
    public String toString() {
        String s = "";
        Player p2;

        s += getPlayerString(benchmarkPlayer);

        for (int i = 0; i < players.size(); i++) {
            p2 = players.get(i);
            if (p2 != benchmarkPlayer) {
                s += getPlayerString(p2);
            }
        }

        if (movementTask.isUpPressed()) {
            if (movementTask.isLeftPressed()) {
                s += "4";
            }
            else if (movementTask.isRightPressed()) {
                s += "5";
            }
            else {
                s += "0";
            }
        }
        else if (movementTask.isDownPressed()) {
            if (movementTask.isLeftPressed()) {
                s += "6";
            }
            else if (movementTask.isRightPressed()) {
                s += "7";
            }
            else {
                s += "2";
            }
        }
        else if (movementTask.isLeftPressed()) {
            s += "1";
        }
        else if (movementTask.isRightPressed()) {
            s += "3";
        }
        else {
            s += "8";
        }

        return s;
    }

    private String getPlayerString(Player p2) {
        String s = "";
        s += p2.getCoords().getX() + ",";
        s += p2.getCoords().getY() + ",";
        s += p2.getRotation() + ",";
        s += p2.getSpeedXY().getX() + ",";
        s += p2.getSpeedXY().getY() + ",";
        s += (p2.isActive() ? "1" : "0") + ",";
        return s;
    }
}

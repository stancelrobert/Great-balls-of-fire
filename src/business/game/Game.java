package business.game;

import business.server.Server;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 03.04.2017.
 */
public class Game {
    public enum InstanceType { GUEST, HOST }
    private static final int MAX_PLAYERS_NUMBER = 3;
    private List<Player> players = new ArrayList<>(MAX_PLAYERS_NUMBER);
    private final InstanceType instanceType;
    private final int port = 5000;
    private Server server;

    public Game(InstanceType instanceType) {
        this.instanceType = instanceType;
        this.init();
    }

    private void init() {
        switch (this.instanceType) {
            case HOST: this.initServer(); break;
            case GUEST: this.initClient(); break;
        }
    }

    private void initServer() {
        try {
            this.server = new Server<Player>(5000, MAX_PLAYERS_NUMBER);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initClient() {

    }

    public void addPlayer(Player player) {
        players.add(player);
    }
}

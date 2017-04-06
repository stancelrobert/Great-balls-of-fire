package business.game;

import business.server.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private ExecutorService executor = Executors.newFixedThreadPool(MAX_PLAYERS_NUMBER);

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
        executor.submit(() -> {
            Point coords = player.getCoords();
            double pX, pY;
            while (!Thread.interrupted()) {
                pX = coords.getX() - 292.5;
                pY = coords.getY() - 310;
                if (Math.sqrt(pX*pX+pY*pY) > 195.0) {
                    System.out.println("Player poza plansza" + pX + " " + pY + " " + Math.sqrt(pX*pX+pY*pY));
                }
                try {
                    Thread.sleep(50);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

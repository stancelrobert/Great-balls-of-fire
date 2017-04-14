package business.game;

import business.client.Client;
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
    private Client<Player> client;

    public Game(InstanceType instanceType) {
        this.instanceType = instanceType;
        //init();
    }

    private void init() {
        switch (this.instanceType) {
            case HOST: initServer(); break;
            case GUEST: initClient(); break;
        }
    }

    private void initServer() {
        try {
            server = new Server<Player>(port, MAX_PLAYERS_NUMBER);
            server.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        initClient();

    }

    private void initClient() {
        client = new Client<>(6000);
        client.start();
    }

    public void addPlayer(Player player) {
        players.add(player);
        executor.submit(() -> {
            Point coords = player.getCoords();
            double pX, pY;
            while (!Thread.interrupted()) {
                pX = coords.getX() - 292.5;
                pY = coords.getY() - 310;
                //System.out.println(pX + " " + pY);
                if (Math.sqrt(pX*pX+pY*pY) > 195.0) {
                    //System.out.println("Player poza plansza" + pX + " " + pY + " " + Math.sqrt(pX*pX+pY*pY));
                }
                try {
                    Thread.sleep(1000);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

package business.game;


import business.server.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by Robert on 03.04.2017.
 */
public class Game {
    public static final int MAX_PLAYERS_NUMBER = 3;
    public static final double BOARD_RADIUS = 230;
    public static final double PLAYER_RADIUS = 25.0;
    public static final double ACCELERATION = 700.0;
    public static final double MAX_SPEED = 450.0;
    public static final double ROTATION_SPEED = 360.0;
    public static final double PASSIVE_ACCELERATION = 300.0;
    public static final String[] COLORS = { "#FF0000", "#00FF00", "#0000FF"};

    private List<Player> players = new ArrayList<>(MAX_PLAYERS_NUMBER);
    private Map<Player, PlayerMovementTask> playersMovementTasks = new HashMap<>(MAX_PLAYERS_NUMBER);
    private ExecutorService playersMovementExecutor = Executors.newFixedThreadPool(MAX_PLAYERS_NUMBER+1);

    public void newRound() {
        playersMovementExecutor.shutdownNow();
        playersMovementExecutor = Executors.newFixedThreadPool(MAX_PLAYERS_NUMBER+1);


        initPlayers();

        playersMovementExecutor.submit(this::collision);

        for (Map.Entry<Player, PlayerMovementTask> entry : playersMovementTasks.entrySet()) {
            playersMovementExecutor.submit(entry.getValue());
        }
    }

    private void collision() {
        Server.print("Collision method activated.");

        Player p1, p2;
        while (!Thread.interrupted()) {
            for (int i = 0; i < players.size(); i++) {
                for (int j = i+1; j < players.size(); j++) {
                    p1 = players.get(i);
                    p2 = players.get(j);

                    if (areColliding(p1, p2)) {
                        Server.print("Serving collision.");
                        serveCollision(p1, p2);
                        Server.print("Collision served.");
                    }
                }
            }
            try {
                Thread.sleep(10);
            }
            catch (Exception e) {
                Server.print("Sleep interrupted.");
                break;
            }
        }

    }

    private void serveCollision(Player p1, Player p2) {
        playersMovementTasks.get(p1).setActive(false);
        playersMovementTasks.get(p2).setActive(false);

        Vector v1 = new Vector(Math.cos(p1.getSpeed()), Math.sin(p1.getSpeed()));
        Vector v2 = new Vector(Math.cos(p2.getSpeed()), Math.sin(p2.getSpeed()));
        Vector v3 = new Vector(p2.getCoords().getX() - p1.getCoords().getX(),
                                p2.getCoords().getY() - p1.getCoords().getY());
        double b1 = Math.atan2(v1.getY(), v1.getX()) - Math.atan2(v3.getY(), v3.getX());
        double a1 = 90 - b1;
        double cosa1 = Math.cos(a1);
        double sina1 = Math.sin(a1);
        double cosb1 = Math.cos(b1);
        double sinb1 = Math.sin(b1);
        Vector u11 = new Vector(Math.cos(v1.getX()*cosa1 + v1.getY()*sina1),
                                Math.cos(-v1.getX()*sina1 + v1.getY()*cosa1));
        Vector u12 = new Vector(Math.cos(v1.getX()*cosb1 + v1.getY()*sinb1),
                Math.cos(-v1.getX()*sinb1 + v1.getY()*cosb1));

        v3.setLocation(-v3.getX(), -v3.getY());

        double b2 = Math.atan2(v2.getY(), v2.getX()) - Math.atan2(v3.getY(), v3.getX());
        double a2 = 90 - b2;
        double cosa2 = Math.cos(a2);
        double sina2 = Math.sin(a2);
        double cosb2 = Math.cos(b2);
        double sinb2 = Math.sin(b2);
        Vector u21 = new Vector(Math.cos(v2.getX()*cosa2 + v2.getY()*sina2),
                Math.cos(-v2.getX()*sina2 + v2.getY()*cosa2));
        Vector u22 = new Vector(Math.cos(v2.getX()*cosb2 + v2.getY()*sinb2),
                Math.cos(-v2.getX()*sinb2 + v2.getY()*cosb2));

        Vector newv1 = new Vector(u11.getX()+u22.getX(), u11.getY()+u22.getY());
        Vector newv2 = new Vector(u12.getX()+u21.getX(), u12.getY()+u21.getY());

        p1.setRotation(Math.atan2(newv1.getY(), newv1.getX()));
        p2.setRotation(Math.atan2(newv2.getY(), newv2.getX()));

        p1.setSpeed(Math.sqrt(newv1.getX()*newv1.getX()+newv1.getY()*newv1.getY()));
        p2.setSpeed(Math.sqrt(newv2.getX()*newv2.getX()+newv2.getY()*newv2.getY()));

        playersMovementTasks.get(p1).setActive(true);
        playersMovementTasks.get(p2).setActive(true);

        //Server.print("elo");

        while (areColliding(p1, p2)) {}

    }

    private boolean areColliding(Player p1, Player p2) {
        double p1x, p1y, p2x, p2y, x, y;
        p1x = p1.getCoords().getX();
        p1y = p1.getCoords().getY();
        p2x = p2.getCoords().getX();
        p2y = p2.getCoords().getY();
        x = p1x - p2x;
        y = p1y - p2y;

        if (x*x + y*y <= 4*(Game.PLAYER_RADIUS* Game.PLAYER_RADIUS)) {

            //UWAGAAA

            //Jak odkomentujecie printa to kolizja zaczyna coś działać XDDDDDD

            //Server.print("");//(x*x + y*y + "  " +  4*(Game.PLAYER_RADIUS*Game.PLAYER_RADIUS));
            return true;
        }
        else {
            return false;
        }

    }

    private void initPlayers() {
        double delta_t = 360.0/players.size();
        double rad;
        double initRadius = BOARD_RADIUS - PLAYER_RADIUS - 20;
        double x, y;
        Player player;
        for (int i = 0; i < players.size(); i++) {
            rad = Math.toRadians(0+delta_t*i);
            x = initRadius*Math.sin(rad);
            y = initRadius*Math.cos(rad);
            player = players.get(i);
            player.setCoords(x, y);
            player.setRotation(Math.atan2(y,x) - Math.atan2(0,initRadius));
            player.setColor(COLORS[i]);
        }
    }

    public void addPlayer(Player player) {
        player.setColor(COLORS[players.size()]);
        players.add(player);
        playersMovementTasks.put(player, new PlayerMovementTask(player));
        newRound();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Map<Player, PlayerMovementTask> getPlayersMovementTasks() {
        return playersMovementTasks;
    }
}

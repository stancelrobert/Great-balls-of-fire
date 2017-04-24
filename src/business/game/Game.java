package business.game;


import business.server.Server;
import business.util.DaemonThreadFactory;

import java.util.*;
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

    private static final double COLLISION_TRESHOLD = 4*PLAYER_RADIUS*PLAYER_RADIUS;

    private List<Player> players = new ArrayList<>(MAX_PLAYERS_NUMBER);
    private List<Player> activaPlayers = new ArrayList<>(MAX_PLAYERS_NUMBER);
    private Map<Player, PlayerMovementTask> playersMovementTasks = new HashMap<>(MAX_PLAYERS_NUMBER);
    private ExecutorService executor = Executors.newFixedThreadPool(2, new DaemonThreadFactory());
    private int roundNumber = 0;


    public void newRound() {
        executor.shutdownNow();
        executor = Executors.newFixedThreadPool(2, new DaemonThreadFactory());
        roundNumber++;

        initPlayers();

        executor.submit(this::collision);
        executor.submit(this::movementTask);

//        for (Map.Entry<Player, PlayerMovementTask> entry : playersMovementTasks.entrySet()) {
//            playersMovementExecutor.submit(entry.getValue());
//        }

    }

    private void movementTask() {
        Server.print("Movement task activated.");
        Player player, player2;
        PlayerMovementTask movementTask;
        Iterator<Player> iterator;
        while (activaPlayers.size() > 0 && !Thread.interrupted()) {
            iterator = activaPlayers.iterator();
            while (iterator.hasNext()) {
                try {
                    player = iterator.next();
                    movementTask = playersMovementTasks.get(player);
                    if (player.isActive()) {
                        movementTask.run();
                    }
                    else {
                        iterator.remove();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        Server.print("Movement task ended.");
        if (!Thread.interrupted()) {
            newRound();
        }
    }


    private void collision() {
        Server.print("Collision method activated.");

        Player p1, p2;
        while (!Thread.interrupted()) {
            for (int i = 0; i < players.size() - 1; i++) {
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
        }
    }

    public synchronized void serveCollision(Player p1, Player p2) {
        playersMovementTasks.get(p1).setActive(false);
        playersMovementTasks.get(p2).setActive(false);
        playersMovementTasks.get(p1).setControllable(false);
        playersMovementTasks.get(p2).setControllable(false);

        Vector v1 = p1.getSpeedXY();
        Vector v2 = p2.getSpeedXY();

        Vector v3 = new Vector(p2.getCoords().getX() - p1.getCoords().getX(),
                p2.getCoords().getY() - p1.getCoords().getY());

        double b1 = Vector.getAngle(v1, v2);
        double a1 = Math.PI/2 - b1;

        Vector u11 = Vector.getRotatedVector(v1, -a1);
        u11.scale(Math.cos(a1));

        Vector u12 = Vector.getRotatedVector(v1, b1);
        u12.scale(Math.sin(a1));



        v3.rotate(Math.PI);

        double b2 = Vector.getAngle(v2, v3);
        double a2 = 90 - b2;

        Vector u21 = Vector.getRotatedVector(v2, -a2);
        u21.scale(Math.cos(a2));

        Vector u22 = Vector.getRotatedVector(v2, b2);
        u22.scale(Math.sin(a2));

        Vector newv1 = Vector.addVectors(u11, u22);
        Vector newv2 = Vector.addVectors(u12, u21);

        p1.setSpeedXY(newv1);
        p2.setSpeedXY(newv2);

        playersMovementTasks.get(p1).setActive(true);
        playersMovementTasks.get(p2).setActive(true);

        while (areColliding(p1, p2)) {
            playersMovementTasks.get(p1).run();
            playersMovementTasks.get(p2).run();
        }

        playersMovementTasks.get(p1).setControllable(true);
        playersMovementTasks.get(p2).setControllable(true);


    }

    public boolean areColliding(Player p1, Player p2) {
        if (p1 == p2) {
            return false;
        }

        double p1x, p1y, p2x, p2y, x, y;
        p1x = p1.getCoords().getX();
        p1y = p1.getCoords().getY();
        p2x = p2.getCoords().getX();
        p2y = p2.getCoords().getY();
        x = p1x - p2x;
        y = p1y - p2y;

        if (x*x + y*y <= COLLISION_TRESHOLD) {
            //UWAGAAA
            //Jak odkomentujecie printa to kolizja zaczyna coś działać XDDDDDD

            //Server.print((x*x + y*y) + " < " + COLLISION_TRESHOLD);//(x*x + y*y + "  " +  4*(Game.PLAYER_RADIUS*Game.PLAYER_RADIUS));
            //Server.print(p1);
            //Server.print(p2);
            return true;
        }
        else {
            return false;
        }

    }

    private void initPlayers() {
        activaPlayers.clear();
        double delta_t = 360.0/players.size();
        double rad;
        double initRadius = BOARD_RADIUS - PLAYER_RADIUS - BOARD_RADIUS/2;
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
            player.setActive(true);
            activaPlayers.add(player);
        }
    }

    public void addPlayer(Player player) {
        player.setColor(COLORS[players.size()]);
        players.add(player);
        playersMovementTasks.put(player, new PlayerMovementTask(player, this));
        newRound();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Map<Player, PlayerMovementTask> getPlayersMovementTasks() {
        return playersMovementTasks;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Player> getActivaPlayers() {
        return activaPlayers;
    }

    public void setActivaPlayers(List<Player> activaPlayers) {
        this.activaPlayers = activaPlayers;
    }
}

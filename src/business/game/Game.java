package business.game;


import business.server.Server;
import business.util.DaemonThreadFactory;
import com.google.common.collect.Lists;
import org.bytedeco.javacv.FrameFilter;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.mapdb.elsa.ElsaSerializerBase;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.cpu.nativecpu.NDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.serializer.NormalizerSerializer;
import org.nd4j.linalg.factory.Nd4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
    private List<Player> activePlayers = new ArrayList<>(MAX_PLAYERS_NUMBER);
    private Map<Player, PlayerMovementTask> playersMovementTasks = new HashMap<>(MAX_PLAYERS_NUMBER);
    private ExecutorService newRoundExecutor = Executors.newSingleThreadExecutor(new DaemonThreadFactory());
    private ExecutorService movementTaskExecutor = Executors.newSingleThreadExecutor(new DaemonThreadFactory());
    private ExecutorService botMovementExecutor = Executors.newFixedThreadPool(MAX_PLAYERS_NUMBER, new DaemonThreadFactory());
    private int roundNumber = 0;
    private GameDataSetGenerator dataSetGenerator = new GameDataSetGenerator(players);
    private MultiLayerNetwork model = null;
    private DataNormalization normalizer = null;
    private boolean endRound = false;
    private boolean movementTaskRunning = false;
    private boolean generateData = true;

    public Game() {
        try {
            model = ModelSerializer.restoreMultiLayerNetwork(new File("model.zip"));
            normalizer = NormalizerSerializer.getDefault().restore(new File("normalizer.txt"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void newRound() {
        Server.print("Starting new round.");
        endRound = true;
        while (movementTaskRunning);
        endRound = false;
        roundNumber++;


        initPlayers();
        if (players.size() > 1) {
            movementTaskExecutor.submit(this::movementTask);
        }

        Server.print("Round " + roundNumber + " started.");

    }

    private void movementTask() {
        movementTaskRunning = true;
        Player player, player2;
        PlayerMovementTask movementTask;
        Iterator<Player> iterator;
        while (activePlayers.size() > 1 && !endRound) {
            iterator = activePlayers.iterator();
            while (iterator.hasNext()) {
                player = iterator.next();
                try {
                    movementTask = playersMovementTasks.get(player);
                    if (player.isActive()) {
                        for (int j = 0; j < activePlayers.size(); j++) {
                            player2 = activePlayers.get(j);

                            if (areColliding(player, player2)) {
                                serveCollision(player, player2);
                            }
                        }
                        movementTask.run();

                    }
                    else {
                        iterator.remove();
                    }

                    if (activePlayers.size() == 3 && generateData) {
                        writeStateToFile();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (!endRound) {
            Server.print("Round " + roundNumber + " ended succesfully.");
            endRound = false;
            newRoundExecutor.submit(this::newRound);
        }
        else {
            Server.print("Round " + roundNumber + " ended by interruption.");
        }

        movementTaskRunning = false;

    }

    private String getStateForBenchmarkPlayer(Player player) {
        String s = "";
        Player benchmarkPlayer = players.get(0);
        Player p2;
        PlayerMovementTask movementTask = playersMovementTasks.get(player);

        s += getPlayerString(benchmarkPlayer);

        for (int i = 0; i < players.size(); i++) {
            p2 = players.get(i);
            if (p2 != benchmarkPlayer) {
                s += getPlayerString(p2);
            }
        }

        if (movementTask.isUpPressed()) {
            if (movementTask.isLeftPressed()) { s += "4"; }
            else if (movementTask.isRightPressed()) { s += "5"; }
            else { s += "0"; }
        }
        else if (movementTask.isDownPressed()) {
            if (movementTask.isLeftPressed()) { s += "6"; }
            else if (movementTask.isRightPressed()) { s += "7"; }
            else { s += "2"; }
        }
        else if (movementTask.isLeftPressed()) { s += "1"; }
        else if (movementTask.isRightPressed()) { s += "3"; }
        else { s += "8"; }

        return s;
    }

    private long lastTime;
    private long currTime;

    private void writeStateToFile() {
        currTime = System.nanoTime();
        if (lastTime < currTime - 10000000) {

            String s = getStateForBenchmarkPlayer(players.get(0));

            try {
                Files.write(Paths.get("data.csv"), Arrays.asList(s), StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }

            lastTime = currTime;
        }
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
        int j = 0;
        while (areColliding(p1, p2)) {
            j++;
            playersMovementTasks.get(p1).run();
            playersMovementTasks.get(p2).run();
            if (j > 50) {
                p1.setSpeedXY(new Vector(v3));
                v3.rotate(Math.PI);
                p2.setSpeedXY(new Vector(v3));
                v3.rotate(Math.PI);
                j = 0;
            }
        }

        playersMovementTasks.get(p1).setControllable(true);
        playersMovementTasks.get(p2).setControllable(true);
    }

    public boolean areColliding(Player p1, Player p2) {
        if (p1 == p2) {
            return false;
        }

        double x, y;
        x = p1.getCoords().getX() - p2.getCoords().getX();
        y = p1.getCoords().getY() - p2.getCoords().getY();

        return x*x + y*y <= COLLISION_TRESHOLD;
    }

    private void initPlayers() {
        activePlayers.clear();
        activePlayers = new ArrayList<>(MAX_PLAYERS_NUMBER);
        double delta_t = 360.0/players.size();
        double rad;
        double initRadius = BOARD_RADIUS - PLAYER_RADIUS - BOARD_RADIUS/2;
        double x, y;
        Player player;
        try {
            Thread.sleep(10);
        }
        catch (Exception e) {

        }
        for (int i = 0; i < players.size(); i++) {
            rad = Math.toRadians(0+delta_t*i);
            x = initRadius*Math.sin(rad);
            y = initRadius*Math.cos(rad);
            player = players.get(i);
            player.setCoords(x, y);
            player.getSpeedXY().setLocation(0, 0);
            player.setRotation(Math.atan2(y,x) - Math.atan2(0, initRadius));
            player.setColor(COLORS[i]);
            player.setActive(true);
            playersMovementTasks.get(player).prepare();
            activePlayers.add(player);
        }
        Server.print("Players initialized.");
    }

    public void addPlayer(Player player) {
        player.setColor(COLORS[players.size()]);
        players.add(player);
        PlayerMovementTask task = new PlayerMovementTask(player, this);
        playersMovementTasks.put(player, task);

        dataSetGenerator.setBenchmarkPlayer(player, task);

        newRound();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Map<Player, PlayerMovementTask> getPlayersMovementTasks() {
        return playersMovementTasks;
    }

    public void addBot() {
        Player bot = new Player();
        bot.setBot(true);
        PlayerMovementTask movementTask = new PlayerMovementTask(bot, this);
        bot.setColor(COLORS[players.size()]);
        players.add(bot);
        playersMovementTasks.put(bot, movementTask);
        botMovementExecutor.submit(() -> {
            Random rand = new Random();
            while (!Thread.interrupted()) {
                if (activePlayers.size() == 3 && model != null) {
                    String[] strArray = getStateForBenchmarkPlayer(bot).split(",");
                    double[] doubleArray = new double[strArray.length-1];
                    for(int i = 0; i < strArray.length-1; i++) {
                        doubleArray[i] = Double.parseDouble(strArray[i]);
                    }
                    INDArray in = Nd4j.create(doubleArray);
                    normalizer.transform(in);
                    INDArray out = model.output(in);
                    int idx = maxIndex(out);
                    movementTask.setUpPressed(false);
                    movementTask.setDownPressed(false);
                    movementTask.setLeftPressed(false);
                    movementTask.setRightPressed(false);
                    switch (idx) {
                        case 0:
                            movementTask.setUpPressed(true);
                            break;
                        case 1:
                            movementTask.setLeftPressed(true);
                            break;
                        case 2:
                            movementTask.setDownPressed(true);
                            break;
                        case 3:
                            movementTask.setRightPressed(true);
                            break;
                        case 4:
                            movementTask.setUpPressed(true);
                            movementTask.setLeftPressed(true);
                            break;
                        case 5:
                            movementTask.setUpPressed(true);
                            movementTask.setRightPressed(true);
                            break;
                        case 6:
                            movementTask.setDownPressed(true);
                            movementTask.setLeftPressed(true);
                            break;
                        case 7:
                            movementTask.setDownPressed(true);
                            movementTask.setRightPressed(true);
                            break;
                        case 8:
                            break;
                    }
                }
                else {
                    movementTask.setUpPressed(rand.nextBoolean());
                    movementTask.setDownPressed(rand.nextBoolean());
                    movementTask.setLeftPressed(rand.nextBoolean());
                    movementTask.setRightPressed(rand.nextBoolean());
                }

                try {
                    Thread.sleep(200);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Server.print("Bot initialized.");

        newRound();
    }

    public static int maxIndex(INDArray vals){
        int maxIndex = 0;
        for (int i = 1; i < vals.length(); i++){
            double newnumber = vals.getDouble(i);
            if ((newnumber > vals.getDouble(maxIndex))){
                maxIndex = i;
            }
        }
        return maxIndex;
    }
}

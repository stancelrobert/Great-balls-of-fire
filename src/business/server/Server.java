package business.server;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.*;

public class Server {
    private static final int MAX_PLAYERS_NUMBER = 3;
    private static final int[] PORTS = {6000, 6001, 6002};
    private ExecutorService executor = newFixedThreadPool(MAX_PLAYERS_NUMBER);
    private ArrayList<PlayerHandler> playerHandlers = new ArrayList<>(MAX_PLAYERS_NUMBER);

    public Server() throws SocketException {}

    public void run() throws SocketException {
        PlayerHandler currPlayerHandler;
        for (int port : PORTS) {
            currPlayerHandler = new PlayerHandler(port);
            playerHandlers.add(currPlayerHandler);
            executor.submit(currPlayerHandler);
        }
    }
}

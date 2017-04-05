package business.server;

import com.sun.deploy.util.SessionState;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Robert on 03.04.2017.
 */
public class Server <T extends Serializable> {
    private final int maxClients;
    private final ServerSocket serverSocket;
    private List<ClientHandler> clientHandlers;
    private ExecutorService executor;

    public Server(int port, int maxClients) throws IOException {
        this.maxClients = maxClients;
        this.clientHandlers = new ArrayList<>(maxClients);
        this.executor = Executors.newFixedThreadPool(maxClients);
        this.serverSocket = new ServerSocket(port);
    }

    public void start() {
        printMessage("Server started.");
        while(true) {
            try {
                printMessage("Waiting for client.");
                Socket client = serverSocket.accept();

                printMessage("Client connected.");
                if(client != null) {
                    ClientHandler ch = new ClientHandler(client);
                    clientHandlers.add(ch);
                    executor.submit(ch);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void printMessage(String message) {

    }


}

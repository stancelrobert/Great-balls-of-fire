package business.server;

import business.model.ConnectionData;
import com.sun.deploy.util.SessionState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Robert on 03.04.2017.
 */
public class Server <T extends Serializable> {
    private final int maxClients;
    int port = 4000;

    private DatagramSocket serverSocket;
    private static final int RECEIVE_BUFFER_SIZE = 1024;
    private int connectedClients = 0;
    private List<ClientHandler> clientHandlers;
    private ExecutorService executor;
    private ExecutorService clientAcceptExecutor;



    byte[] receiveBuffer = new byte[RECEIVE_BUFFER_SIZE];

    public Server(int port, int maxClients) throws IOException {
        this.maxClients = maxClients;
        this.port = port;
        this.clientHandlers = new ArrayList<>(maxClients);
        this.executor = Executors.newFixedThreadPool(maxClients);
        this.clientAcceptExecutor = Executors.newSingleThreadExecutor();
        this.serverSocket = new DatagramSocket(port);
    }



    public void start() {
        //rozpoczęcie rejestracji klientów
        //klienci wysyłają numery swoich portów
        //aby potem serwer wiedział do kogo wysyłać


            clientAcceptExecutor.submit(() -> {
                try {
                    printMessage("Server start");
                    printMessage("Connected clients = " + connectedClients);
                    ServerSocket serverSocket = new ServerSocket(port);
                    while (true) {
                        printMessage("Waiting for client.");
                        Socket socket = serverSocket.accept();
                        System.out.println(socket.getInetAddress());
                        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                        ConnectionData<Integer> connectionData = (ConnectionData<Integer>) in.readObject();
                        printMessage("Received clients' port: " + connectionData.getData());


                        ClientHandler clientHandler = new ClientHandler(socket);
                        executor.submit(clientHandler);


                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            });


    }

    private void printMessage(String message) {
        System.out.println(message);
    }
}

package business.server;

import java.net.Socket;

/**
 * Created by Robert on 03.04.2017.
 */
public class ClientHandler implements Runnable {
    Socket client;

    public ClientHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {

    }
}

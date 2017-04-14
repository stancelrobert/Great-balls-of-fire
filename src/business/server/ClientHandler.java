package business.server;

import business.model.ConnectionData;

import java.io.ObjectOutputStream;
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
        try (ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream())) {
            System.out.println("Start sending message.");
            out.writeObject(new ConnectionData<>("Connected succesfully."));
            out.flush();
            System.out.println("Message sent.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

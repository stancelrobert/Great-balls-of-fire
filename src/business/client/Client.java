package business.client;

import business.model.ConnectionData;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Robert on 05.04.2017.
 */
public class Client <T extends Serializable> {
    int port;
    DatagramSocket serverSocket;

    public Client(int port) {
        this.port = port;
    }

    public void start() {
        ConnectionData<Integer> connectionData = new ConnectionData<>(port);
        try (Socket socket = new Socket(InetAddress.getLocalHost(), 5000);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())
        ) {
            System.out.println("Client: wypisuje.");
            out.writeObject(connectionData);
            out.flush();
            System.out.println("Client: wypisal.");

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ConnectionData<String> receivedData = (ConnectionData<String>)in.readObject();


        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listenToServer() {

    }

}

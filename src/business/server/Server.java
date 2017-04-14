package business.server;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Robert on 12.04.2017.
 */
public class Server {
    private int port;
    private int maxClientsNumber;
    private int clientsNumber = 0;
    private ExecutorService waitForClientsExecutor = Executors.newSingleThreadExecutor();
    private ExecutorService clientsHandlersExecutor;

    private int clientPort;
    private InetAddress clientAddress;

    private List<ClientHandler> clientHandlers;

    private ServerEventHandler serverEventHandler;

    public Server(int port) {
        this(port, 3);
    }

    public Server(int port, int maxClientsNumber) {
        this.port = port;
        this.maxClientsNumber = maxClientsNumber;
        this.clientHandlers = new ArrayList<>(maxClientsNumber);
        this.clientsHandlersExecutor = Executors.newFixedThreadPool(maxClientsNumber);
    }

    public void start() {
        waitForClientsExecutor.submit(this::waitForClients);
    }



    public void waitForClients() {
        byte[] receiveData = new byte[1024];
        try (DatagramSocket serverSocket = new DatagramSocket(port)) {
            while (clientsNumber < maxClientsNumber) {
                print("Waiting for client.");
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                ClientInfo clientInfo = new ClientInfo(receivePacket.getAddress(), receivePacket.getPort());

                serverEventHandler.clientConnected(clientInfo);

                print("Client " + clientInfo.getAddress() + " " + clientInfo.getPort() + " connected.");

                ClientHandler clientHandler = new ClientHandler(clientInfo, serverEventHandler);

                clientHandlers.add(clientHandler);
                clientHandler.send("Hello".getBytes());



                print("Sent hello message.");

                clientsHandlersExecutor.submit(clientHandler);

                clientsNumber++;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void sendDataToClients(byte[] data) {
//        for (ClientHandler handler : clientHandlers) {
//            handler.send(data);
//        }
        for (int i = 0; i < clientHandlers.size(); i++) {
            clientHandlers.get(i).send(data);
        }
    }

    public static void print(Object x) {
        String msg = String.valueOf(x);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);

        System.out.println(reportDate + ": " + msg);
    }

    public static byte[] convertToBytes(Object object) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            return bos.toByteArray();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object convertFromBytes(byte[] bytes) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInput in = new ObjectInputStream(bis)) {
            return in.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setServerEventHandler(ServerEventHandler serverEventHandler) {
        this.serverEventHandler = serverEventHandler;
    }
}

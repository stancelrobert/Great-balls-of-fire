package business.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by Robert on 13.04.2017.
 */
public class ClientHandler implements Runnable {
    private ClientInfo clientInfo;
    private ServerEventHandler serverEventHandler;
    private byte[] receivedDataBuffer = new byte[1024];
    private DatagramSocket serverSocket;

    public ClientHandler(ClientInfo clientInfo) {
        try {
            this.serverSocket = new DatagramSocket();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.clientInfo = clientInfo;
    }

    public ClientHandler(ClientInfo clientInfo, ServerEventHandler serverEventHandler) {
        this(clientInfo);
        this.serverEventHandler = serverEventHandler;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                //Server.print("Waiting for message from: " + clientInfo);
                DatagramPacket packet = new DatagramPacket(receivedDataBuffer, receivedDataBuffer.length);
                serverSocket.receive(packet);
                if (serverEventHandler != null) {
                    serverEventHandler.dataReceived(clientInfo, packet.getData());
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void send(byte[] data) {
        DatagramPacket packet = new DatagramPacket(data, data.length, clientInfo.getAddress(), clientInfo.getPort());
        try {
            serverSocket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Server.print("Message sent to: " + clientInfo);
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }
}

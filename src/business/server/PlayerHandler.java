package business.server;

import business.game.Player;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by Robert on 26.03.2017.
 */
public class PlayerHandler implements Runnable {
    private DatagramSocket socket;
    private int port;
    private Player player;


    public PlayerHandler(int port) throws SocketException {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            socket = new DatagramSocket(port);
            byte[] incomingData = new byte[1024];

            while (true) {
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                socket.receive(incomingPacket);
                byte[] data = incomingPacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);
                try {
                    player = (Player) is.readObject();
                    System.out.println("Player object received = "+player);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                InetAddress IPAddress = incomingPacket.getAddress();
                int port = incomingPacket.getPort();
                String reply = "Thank you for the message";
                byte[] replyBytea = reply.getBytes();
                DatagramPacket replyPacket =
                        new DatagramPacket(replyBytea, replyBytea.length, IPAddress, port);
                socket.send(replyPacket);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

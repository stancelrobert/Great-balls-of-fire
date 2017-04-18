package controllers;

import business.GameServerEventHandler;
import business.game.Game;
import business.game.Player;
import business.server.ClientInfo;
import business.server.Server;
import business.server.ServerEventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainController implements Initializable {
    @FXML private BoardController boardController;

    @FXML private MenuController menuController;

    private Stage stage;

    private byte[] bytes = new byte[4];
    DatagramSocket clientSocket;
    ClientInfo clientInfo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuController.setBoardController(boardController);
        menuController.setMainController(this);

        boardController.setMainController(this);
    }

    /**
     * initializes new server and adds local player to service
     */
    public void initServer() {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public void initClient() {
        initKeysEvents(stage.getScene());

        ExecutorService executor = Executors.newFixedThreadPool(2);
        //boardController.addPlayer(player);

        executor.submit(boardController::displayTask);
        executor.submit(() -> {
            try {
                Player[] players = new Player[3];
                int playersNumber = 0;
                clientSocket = new DatagramSocket();
                byte[] sendData = "Hello".getBytes();
                byte[] receiveData = new byte[1024];
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("127.0.0.1"), 4000);
                clientSocket.send(sendPacket);

                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                String msg = new String(receivePacket.getData());

                clientInfo = new ClientInfo(receivePacket.getAddress(), receivePacket.getPort());

                System.out.println("From server: " + msg);
                while (!Thread.interrupted()) {
                    receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    clientSocket.receive(receivePacket);
                    ByteArrayInputStream bis = new ByteArrayInputStream(receivePacket.getData());
                    ObjectInput in = null;
                    try {
                        in = new ObjectInputStream(bis);
                        int newPlayersNumber = in.readInt();
                        for (int i = 0; i < playersNumber; i++) {
                            Player player1 = (Player)in.readObject();
                            players[i].setCoords(player1.getCoords().getX(), player1.getCoords().getY());
                            players[i].setRotation(player1.getRotation());
                            players[i].setSpeed(player1.getSpeed());
                        }

                        if (newPlayersNumber != playersNumber) {
                            System.out.println(newPlayersNumber);
                            if (newPlayersNumber > playersNumber) {
                                for (int i = playersNumber; i < newPlayersNumber; i++) {
                                    players[i] = (Player)in.readObject();
                                    boardController.addPlayer(players[i]);
                                }
                            }
                            else {
                                //usunac tamtych graczy xddd
                            }
                            playersNumber = newPlayersNumber;
                        }

                        //System.out.println(player1);


                    } finally {
                        try {
                            if (in != null) {
                                in.close();
                            }
                        } catch (IOException ex) {
                            // ignore close exception
                        }
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });


    }

    public void initNewGame() {


        Game game = new Game();
        Server server = new Server(4000, 3);
        ServerEventHandler serverEventHandler = new GameServerEventHandler(game);
        server.setServerEventHandler(serverEventHandler);

        server.start();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            while(!Thread.interrupted()) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutput out = null;
                try {
                    out = new ObjectOutputStream(bos);
                    out.writeInt(game.getPlayers().size());
                    for (Player player : game.getPlayers()) {
                        out.writeObject(player);
                    }
                    out.flush();
                    byte[] data = bos.toByteArray();
                    server.sendDataToClients(data);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    try {
                        bos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


    }


    private void initKeysEvents(Scene scene) {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:    bytes[0] = 1; break;
                case DOWN:  bytes[1] = 1; break;
                case LEFT:  bytes[2] = 1; break;
                case RIGHT: bytes[3] = 1; break;
            }

            sendMessage();
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case UP:     bytes[0] = 0; break;
                case DOWN:   bytes[1] = 0; break;
                case LEFT:   bytes[2] = 0; break;
                case RIGHT:  bytes[3] = 0; break;
            }

            sendMessage();
        });
    }

    private void sendMessage() {

        try {
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, clientInfo.getAddress(), clientInfo.getPort());
            clientSocket.send(packet);
            //System.out.println("Sent message to: " + clientInfo);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

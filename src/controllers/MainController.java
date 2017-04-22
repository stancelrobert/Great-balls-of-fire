package controllers;

import business.GameServerEventHandler;
import business.game.Game;
import business.game.Player;
import business.server.ClientInfo;
import business.server.Server;
import business.server.ServerEventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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
    private Parent parent;

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

        Thread displayThread = new Thread(boardController::displayTask);
        displayThread.start();

        Executors.newSingleThreadExecutor().submit(() -> {
            int playersNumber = 0;
            Player[] players = new Player[3];
            byte[] sendData = "Hello".getBytes();
            byte[] receiveData = new byte[1024];
            try {
                clientSocket = new DatagramSocket();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("localhost"), 4000);
                clientSocket.send(sendPacket);

                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                clientInfo = new ClientInfo(receivePacket.getAddress(), receivePacket.getPort());

                while (!Thread.interrupted()) {
                    receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    clientSocket.receive(receivePacket);

                    try (ByteArrayInputStream bis = new ByteArrayInputStream(receivePacket.getData());
                            ObjectInput in = new ObjectInputStream(bis)) {
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


                    }
                    catch (Exception e) {
                        e.printStackTrace();
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


                try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                     ObjectOutput out = new ObjectOutputStream(bos)) {
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

                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setByte(int index) {
        if (bytes[index] != 1) {
            bytes[index] = 1;
            sendMessage();
        }
    }

    private void resetByte(int index) {
        if (bytes[index] != 0) {
            bytes[index] = 0;
            sendMessage();
        }
    }


    private void initKeysEvents(Scene scene) {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:    setByte(0); break;
                case DOWN:  setByte(1); break;
                case LEFT:  setByte(2); break;
                case RIGHT: setByte(3); break;
            }
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case UP:    resetByte(0); break;
                case DOWN:  resetByte(1); break;
                case LEFT:  resetByte(2); break;
                case RIGHT: resetByte(3); break;
            }
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

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public Parent getParent() {
        return this.parent;
    }
}

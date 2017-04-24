package business;

import business.game.Game;
import business.game.Player;
import business.game.PlayerMovementTask;
import business.server.ClientInfo;
import business.server.Server;
import business.server.ServerEventHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Robert on 13.04.2017.
 */
public class GameServerEventHandler extends ServerEventHandler {
    private Game game;
    private Map<ClientInfo, PlayerMovementTask> playerMovementTasks = new HashMap<>(Game.MAX_PLAYERS_NUMBER);

    public GameServerEventHandler(Game game) {
        this.game = game;
    }

    @Override
    public void dataReceived(ClientInfo clientInfo, byte[] data) {
//        String bytes = "";
//        for (int i = 0; i < data.length; i++) {
//            bytes+=data[i];
//        }
//        Server.print("Received data: " + bytes);
        PlayerMovementTask playerMovementTask =  playerMovementTasks.get(clientInfo);
        playerMovementTask.setUpPressed(data[0] == 1 ? true : false);
        playerMovementTask.setDownPressed(data[1] == 1 ? true : false);
        playerMovementTask.setLeftPressed(data[2] == 1 ? true : false);
        playerMovementTask.setRightPressed(data[3] == 1 ? true : false);
    }

    @Override
    public void clientConnected(ClientInfo clientInfo) {
        Player player = new Player();
        game.addPlayer(player);
        playerMovementTasks.put(clientInfo, game.getPlayersMovementTasks().get(player));
        //game.newRound();
        for (Player p : game.getPlayers()) {
            Server.print("Player: " + p);
        }
    }
}

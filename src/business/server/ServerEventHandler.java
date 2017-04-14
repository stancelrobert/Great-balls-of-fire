package business.server;

/**
 * Created by Robert on 13.04.2017.
 */
public abstract class ServerEventHandler {

    public abstract void dataReceived(ClientInfo clientInfo, byte[] data);

    public abstract void clientConnected(ClientInfo clientInfo);
}

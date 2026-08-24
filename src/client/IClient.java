package client;

import java.net.Socket;
import java.util.UUID;

public interface IClient {
    void connect(String host,int port);
    UUID getUUID();
}

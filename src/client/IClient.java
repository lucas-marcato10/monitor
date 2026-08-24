package client;

import java.net.Socket;

public interface IClient {
    Socket createSocket();
    void connect(Socket socket,String host,int port);
}

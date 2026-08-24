package client;

import java.net.Socket;

public interface IClientNode {
    Socket createSocket();
    void connect(Socket socket,String host,int port);
}

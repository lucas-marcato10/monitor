package server.connection;

import java.net.ServerSocket;
import java.net.Socket;

public interface IServer {
    ServerSocket createSocket();
    void bind(ServerSocket serverSocket, String host, int port);
    void listen(ServerSocket serverSocket);
    Socket accept(ServerSocket serverSocket);
}

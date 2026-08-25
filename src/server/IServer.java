package server;

import java.net.ServerSocket;
import java.net.Socket;

public interface IServer {
    ServerSocket createSocket();
    void bind(ServerSocket serverSocket, int port);
    void listen(ServerSocket serverSocket);
    Socket accept(ServerSocket serverSocket);
}
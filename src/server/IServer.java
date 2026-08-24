package server;

import java.net.Socket;
import java.net.ServerSocket;
public interface IServerNode {
    ServerSocket createSocket();
    void bind(ServerSocket serverSocket, int port);
    void listen(ServerSocket serverSocket);
    //Returns to client the Socket.
    Socket accept(ServerSocket serverSocket);
}

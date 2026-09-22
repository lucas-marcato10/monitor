package server.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public interface IServer {
    ServerSocket createSocket() throws IOException;

    void bind(ServerSocket serverSocket, String host, int port)
            throws IOException;

    void listen(ServerSocket serverSocket);

    Socket accept(ServerSocket serverSocket) throws IOException;
}
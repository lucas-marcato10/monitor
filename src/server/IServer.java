package server;

import java.net.Socket;
import java.net.ServerSocket;
public interface IServer {
    void start(int port);
    void stop();
}
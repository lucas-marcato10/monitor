package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
public class Server implements IServer{

    public Server() {
    }

    @Override
    public ServerSocket createSocket() {
        try {
            ServerSocket ss = new ServerSocket();
            System.out.printf("");
            return ss;
        } catch (IOException e) {
            throw new RuntimeException("Falha ao criar ServerSocket", e);
        }
    }

    @Override
    public void bind(ServerSocket serverSocket, int port) {
        try {
            serverSocket.bind(new InetSocketAddress(port));
            System.out.println("Vínculo a porta feito.");
        } catch (IOException e) {
            throw new RuntimeException("Falha ao fazer bind na porta " + port, e);
        }
    }

    @Override
    public void listen(ServerSocket serverSocket) {
        System.out.println("Aguardando conexões.");
    }

    @Override
    public Socket accept(ServerSocket serverSocket) {
        try {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected.");
            return clientSocket;
        }catch (IOException e){
            throw new RuntimeException("Falha no accept", e);
        }
    }
}

package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements IServer {

    public Server() {
    }

    @Override
    public ServerSocket createSocket() {
        try {
            return new ServerSocket();
        } catch (IOException e) {
            throw new RuntimeException("Falha ao criar ServerSocket", e);
        }
    }

    @Override
    public void bind(ServerSocket serverSocket, int port) {
        try {
            serverSocket.bind(new InetSocketAddress(port));
            System.out.println("Vínculo à porta " + port + " feito.");
        } catch (IOException e) {
            throw new RuntimeException("Falha ao fazer bind na porta " + port, e);
        }
    }

    @Override
    public void listen(ServerSocket serverSocket) {
        System.out.println("Aguardando conexões...");
    }

    @Override
    public Socket accept(ServerSocket serverSocket) {
        try {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected.");
            return clientSocket;
        } catch (IOException e) {
            throw new RuntimeException("Falha no accept", e);
        }
    }

    // Método que responde ao server.start(port) da Main.java
    public void start(int port) {
        ServerSocket serverSocket = createSocket();
        bind(serverSocket, port);
        listen(serverSocket);

        while (!serverSocket.isClosed()) {
            try {
                Socket clientSocket = accept(serverSocket);
                ClientHandler handler = new ClientHandler(clientSocket);
                new Thread(handler).start();
            } catch (Exception e) {
                System.out.println("Erro na conexão com cliente: " + e.getMessage());
            }
        }
    }
}
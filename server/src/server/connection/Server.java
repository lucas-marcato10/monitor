package server.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements IServer {

    private static final int MAX_CLIENTES_PADRAO = 10;

    private final AtomicInteger clientesConectados = new AtomicInteger(0);
    private final int maxClientes;
    private final ConcurrentHashMap<UUID, ClientHandler> handlers = new ConcurrentHashMap<>();

    public Server() {
        this(MAX_CLIENTES_PADRAO);
    }

    public Server(int maxClientes) {
        this.maxClientes = maxClientes;
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
    public void bind(ServerSocket serverSocket, String host, int port) {
        try {
            serverSocket.bind(new InetSocketAddress(host, port));
            System.out.println("Vínculo a " + host + ":" + port + " feito.");
        } catch (IOException e) {
            throw new RuntimeException("Falha ao fazer bind em " + host + ":" + port, e);
        }
    }

    @Override
    public void listen(ServerSocket serverSocket) {
        System.out.println("Aguardando conexões... (limite de " + maxClientes + " clientes)");
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

    // Método que responde ao ServerMain
    public void start(String host, int port) {
        ServerSocket serverSocket = createSocket();
        bind(serverSocket, host, port);
        listen(serverSocket);

        while (!serverSocket.isClosed()) {
            try {
                Socket clientSocket = accept(serverSocket);
                int clientPosAccept = clientesConectados.incrementAndGet();
                boolean rejected = clientPosAccept > maxClientes;
                if (rejected) {
                    System.out.println("Conexão recusada: limite de " + maxClientes + " clientes atingido.");
                }
                ClientHandler handler = new ClientHandler(clientSocket, clientesConectados, handlers, rejected);
                handlers.put(handler.getUuid(), handler);
                Thread.ofVirtual().start(handler);
            } catch (Exception e) {
                System.out.println("Erro na conexão com cliente: " + e.getMessage());
            }
        }
    }
}

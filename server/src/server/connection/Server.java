package server.connection;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements IServer {
    private static final int MAX_CLIENTES_PADRAO = 10;

    private final AtomicInteger clientesConectados =
            new AtomicInteger(0);

    private final int maxClientes;

    private final ConcurrentHashMap<UUID, ClientHandler> handlers =
            new ConcurrentHashMap<>();

    public Server() {
        this(MAX_CLIENTES_PADRAO);
    }

    public Server(int maxClientes) {
        if (maxClientes < 1) {
            throw new IllegalArgumentException(
                    "O limite deve ser no minimo 1.");
        }

        this.maxClientes = maxClientes;
    }

    @Override
    public ServerSocket createSocket() throws IOException {
        return new ServerSocket();
    }

    @Override
    public void bind(ServerSocket serverSocket, String host, int port)
            throws IOException {

        InetSocketAddress endereco =
                new InetSocketAddress(host, port);

        if (endereco.isUnresolved()) {
            throw new UnknownHostException(host);
        }

        serverSocket.bind(endereco);

        System.out.println(
                "Vinculo a " + host + ":" + port + " feito.");
    }

    @Override
    public void listen(ServerSocket serverSocket) {
        System.out.println(
                "Aguardando conexoes... (limite de "
                        + maxClientes + " clientes)");
    }

    @Override
    public Socket accept(ServerSocket serverSocket) throws IOException {
        return serverSocket.accept();
    }

    public void start(String host, int port) {
        try (ServerSocket serverSocket = createSocket()) {
            bind(serverSocket, host, port);
            listen(serverSocket);

            while (!serverSocket.isClosed()) {
                Socket clientSocket;

                try {
                    clientSocket = accept(serverSocket);
                } catch (IOException e) {
                    if (serverSocket.isClosed()) {
                        break;
                    }

                    System.out.println(
                            "Falha ao aceitar conexao. Tentando novamente.");

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException interrompido) {
                        Thread.currentThread().interrupt();
                        break;
                    }

                    continue;
                }

                iniciarAtendimento(clientSocket);
            }
        } catch (BindException e) {
            System.out.println(
                    "Nao foi possivel iniciar: porta ocupada "
                            + "ou endereco local indisponivel.");
        } catch (UnknownHostException e) {
            System.out.println(
                    "Host/IP nao encontrado: " + host);
        } catch (IOException | IllegalArgumentException | SecurityException e) {
            System.out.println(
                    "Nao foi possivel iniciar ou manter o servidor: "
                            + e.getMessage());
        }
    }

    private void iniciarAtendimento(Socket clientSocket) {
        // O ClientHandler atual decrementa o contador ao terminar,
        // inclusive quando recusa a conexao por lotacao.
        int quantidade = clientesConectados.incrementAndGet();
        boolean rejected = quantidade > maxClientes;

        ClientHandler handler = null;

        try {
            if (rejected) {
                System.out.println(
                        "Conexao recusada: limite de "
                                + maxClientes + " clientes atingido.");
            }

            handler = new ClientHandler(
                    clientSocket,
                    clientesConectados,
                    handlers,
                    rejected);

            handlers.put(handler.getUuid(), handler);

            Thread.ofVirtual().start(handler);
        } catch (IOException | RuntimeException e) {
            // Sem uma thread iniciada, o servidor precisa fazer a limpeza.
            clientesConectados.decrementAndGet();

            if (handler != null) {
                handlers.remove(handler.getUuid(), handler);
            }

            try {
                clientSocket.close();
            } catch (IOException fechamento) {
                System.out.println(
                        "Falha ao fechar socket da sessao nao iniciada.");
            }

            System.out.println(
                    "Erro ao inicializar sessao do cliente: "
                            + e.getMessage());
        }
    }
}
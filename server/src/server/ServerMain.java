package server;

import server.connection.Server;

public class ServerMain {

    private static final String HOST_PADRAO = "127.0.0.1";
    private static final int PORTA_PADRAO = 12345;
    private static final int MAX_CLIENTES_PADRAO = 10;

    public static void main(String[] args) {
        System.out.println("======================");
        System.out.println("Servidor - Monitoramento de sistema");
        System.out.println("======================");

        String host = args.length >= 1 ? args[0].trim() : HOST_PADRAO;
        int port = args.length >= 2 ? parsePorta(args[1].trim()) : PORTA_PADRAO;
        int maxClientes = args.length >= 3 ? parseMaxClientes(args[2].trim()) : MAX_CLIENTES_PADRAO;

        if (port < 1 || port > 65535) {
            System.out.println("Portas são entre 1 e 65535.");
            return;
        }

        if (maxClientes < 1) {
            System.out.println("O limite de conexões deve ser no mínimo 1.");
            return;
        }

        Server server = new Server(maxClientes);

        System.out.println("Iniciando em " + host + ":" + port + " (max " + maxClientes + " clientes)...");

        server.start(host, port);
    }

    private static int parsePorta(String arg) {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            System.out.println("Porta inválida, usando padrão (" + PORTA_PADRAO + ").");
            return PORTA_PADRAO;
        }
    }

    private static int parseMaxClientes(String arg) {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            System.out.println("Limite de conexões inválido, usando padrão (" + MAX_CLIENTES_PADRAO + ").");
            return MAX_CLIENTES_PADRAO;
        }
    }
}

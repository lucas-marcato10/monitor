package server;

import server.connection.Server;

public class ServerMain {
    //Valores Default
    private static final String HOST_PADRAO = "127.0.0.1";
    private static final int PORTA_PADRAO = 12345;
    private static final int MAX_CLIENTES_PADRAO = 10;

    public static void main(String[] args) {
        System.out.println("======================");
        System.out.println("Servidor - Monitoramento de sistema");
        System.out.println("======================");

        String[] config = parseArgs(args);
        String host = config[0];

        int port = Integer.parseInt(config[1]);
        int maxClientes = Integer.parseInt(config[2]);

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

    private static String[] parseArgs(String[] args) {
        String host = HOST_PADRAO;
        String port = String.valueOf(PORTA_PADRAO);
        String maxClientes = String.valueOf(MAX_CLIENTES_PADRAO);

        for (int i = 0; i < args.length - 1; i++) {
            switch (args[i]) {
                case "--hostname" -> host = args[i + 1];
                case "--port" -> port = args[i + 1];
                case "--max-clients" -> maxClientes = args[i + 1];
            }
        }

        return new String[] { host, port, maxClientes };
    }
}

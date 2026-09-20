package client;

public class ClientMain {
    // Valores Default
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 12345;

    public static void main(String[] args) {
        System.out.println("======================");
        System.out.println("Cliente - Monitoramento de sistema");
        System.out.println("======================");

        String[] config = parseArgs(args);
        String host = config[0];
        int port;

        try {
            port = Integer.parseInt(config[1]);
        } catch (NumberFormatException e) {
            System.out.println("Porta inválida, usando padrão (" + DEFAULT_PORT + ").");
            port = DEFAULT_PORT;
        }

        if (port < 1 || port > 65535) {
            System.out.println("A porta deve estar entre 1 e 65535.");
            return;
        }

        Client client = new Client();
        System.out.println("Conectando-se em " + host + ":" + port + "...");
        client.connect(host, port);
    }

    private static String[] parseArgs(String[] args) {
        String host = DEFAULT_HOST;
        String port = String.valueOf(DEFAULT_PORT);

        for (int i = 0; i < args.length - 1; i++) {
            switch (args[i]) {
                case "--hostname" -> host = args[i + 1];
                case "--port" -> port = args[i + 1];
            }
        }

        return new String[] { host, port };
    }
}
package client;

public class ClientMain {

    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 12345;

    public static void main(String[] args) {
        System.out.println("======================");
        System.out.println("Cliente - Monitoramento de sistema");
        System.out.println("======================");

        String host = args.length >= 1 ? args[0].trim() : DEFAULT_HOST;
        int port = args.length >= 2 ? parsePorta(args[1].trim()) : DEFAULT_PORT;

        if (port < 1 || port > 65535) {
            System.out.println("A porta deve estar entre 1 e 65535.");
            return;
        }

        Client client = new Client();

        System.out.println("Conectando-se em " + host + ":" + port + "...");

        client.connect(host, port);
    }

    private static int parsePorta(String arg) {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            System.out.println("Porta inválida, usando padrão (" + DEFAULT_PORT + ").");
            return DEFAULT_PORT;
        }
    }
}

package client;

import java.util.Scanner;

public class ClientMain {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("======================");
        System.out.println("Cliente - Monitoramento de sistema");
        System.out.println("======================");

        System.out.print("Informe o IP do servidor: ");
        String host = scanner.nextLine().trim();

        if (host.isEmpty()) {
            System.out.println("IP inválido.");
            return;
        }

        System.out.print("Informe a porta do servidor: ");

        int port;

        try {
            port = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Porta inválida.");
            return;
        }

        if (port < 1 || port > 65535) {
            System.out.println("A porta deve estar entre 1 e 65535.");
            return;
        }

        Client client = new Client();

        System.out.println("Conectando-se em " + host + ":" + port + "...");

        client.connect(host, port);
    }
}

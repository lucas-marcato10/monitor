package server;

import server.connection.Server;
import java.util.Scanner;

public class ServerMain {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("======================");
        System.out.println("Servidor - Monitoramento de sistema");
        System.out.println("======================");

        System.out.print("Informe a porta do servidor: ");

        int port;

        try {
            port = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Porta inválida, ente novamente.");
            return;
        }

        if (port < 1 || port > 65535) {
            System.out.println("Portas são entre 1 e 65535.");
            return;
        }

        Server server = new Server();

        System.out.println("Iniciando na porta " + port + "...");

        server.start(port);
    }
}

import client.Client;
import server.Server;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("======================");
        System.out.println("Monitoramento de sistema");
        System.out.println("======================");
        System.out.println("1 - Iniciar servidor");
        System.out.println("2 - Iniciar como cliente");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");

        int option;

        try {
            option = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Escolha entre 1, 2 e 0.");
            return;
        }

        switch (option) {

            case 1:
                startServer(scanner);
                break;

            case 2:
                startClient(scanner);
                break;

            case 0:
                System.out.println("Programa encerrado.");
                break;

            default:
                System.out.println("Escolha outra opção.");
        }
    }

    private static void startServer(Scanner scanner) {

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

    private static void startClient(Scanner scanner) {

        System.out.print("Informe o IP do servidor: ");
        String host = scanner.nextLine().trim();

        if (host.isEmpty()) {
            System.out.println("IP inválido.");
            return;
        }

        System.out.print("Informe a porta do servidor:");

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

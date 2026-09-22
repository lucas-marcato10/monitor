package server;

import server.connection.Server;

public class ServerMain {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 12345;
        int maxClientes = 10;

        try {
            for (int i = 0; i < args.length; i += 2) {
                String opcao = args[i];

                if (i + 1 >= args.length || args[i + 1].startsWith("--")) {
                    throw new IllegalArgumentException(
                            "Faltou informar o valor de " + opcao + ".");
                }

                String valor = args[i + 1];

                switch (opcao) {
                    case "--hostname" -> {
                        if (valor.isBlank()) {
                            throw new IllegalArgumentException(
                                    "O host nao pode estar vazio.");
                        }
                        host = valor;
                    }

                    case "--port" -> {
                        port = Integer.parseInt(valor);

                        if (port < 1 || port > 65535) {
                            throw new IllegalArgumentException(
                                    "A porta deve estar entre 1 e 65535.");
                        }
                    }

                    case "--max-clients" -> {
                        maxClientes = Integer.parseInt(valor);

                        if (maxClientes < 1) {
                            throw new IllegalArgumentException(
                                    "O limite de clientes deve ser no minimo 1.");
                        }
                    }

                    default -> throw new IllegalArgumentException(
                            "Opcao desconhecida: " + opcao);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println(
                    "Argumento numerico invalido: use numeros inteiros dentro do limite permitido.");
            return;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.out.println(
                    "Uso: ServerMain [--hostname 127.0.0.1] [--port 12345] [--max-clients 10]");
            return;
        }

        System.out.println("ServerMain: " + host + ":" + port);
        new Server(maxClientes).start(host, port);
    }
}
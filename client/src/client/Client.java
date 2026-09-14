package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements IClient {

    @Override
    public void connect(String host, int port) {
        try {
            Socket socket = new Socket(host, port);

            // Thread de escuta de mensagens do socket
            Thread threadLeituraSocket = new Thread(() -> {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String mensagem;
                    while ((mensagem = in.readLine()) != null) {
                        System.out.println(mensagem);
                    }
                } catch (Exception e) {
                    System.out.println("Conexão encerrada pelo servidor.");
                }
            });

            // Thread de envio das entradas do teclado
            Thread threadTeclado = new Thread(() -> {
                try (
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    Scanner scanner = new Scanner(System.in)
                ) {
                    while (scanner.hasNextLine()) {
                        String comando = scanner.nextLine();
                        out.println(comando);
                        if (comando.equalsIgnoreCase("Exit")) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Erro ao enviar mensagem: " + e.getMessage());
                }
            });

            threadLeituraSocket.start();
            threadTeclado.start();

        } catch (Exception e) {
            System.err.println("Erro ao conectar ao servidor: " + e.getMessage());
        }
    }
}

package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client implements IClient {

    @Override
    public void connect(String host, int port) {
        try (Socket socket = new Socket(host, port)) {

            // Thread para recebimento de mensagens do socket.
            Thread threadLeituraSocket = new Thread(() -> {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String mensagem;
                    while ((mensagem = in.readLine()) != null) {
                        System.out.println(mensagem);
                    }
                } catch (Exception ignored) {
                } finally {
                    System.out.println("Conexão encerrada pelo servidor.");
                    System.exit(0); // Garante que a aplicação feche sem travar o teclado
                }
            });

            // Thread de input do teclado.
            Thread threadTeclado = new Thread(() -> {
                try {
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    Scanner scanner = new Scanner(System.in);
                    while (scanner.hasNextLine()) {
                        String comando = scanner.nextLine();
                        out.println(comando);
                        if (comando.equalsIgnoreCase("Exit")) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Erro ao enviar mensagem.");
                }
            });

            threadLeituraSocket.start();
            threadTeclado.start();

            threadTeclado.join();
            if (!socket.isClosed()) {
                socket.close();
            }

        } catch (UnknownHostException e) {
            System.err.println("Erro: Host/IP '" + host + "' não encontrado.");
        } catch (ConnectException e) {
            System.err.println("Erro: Não foi possível conectar ao servidor em " + host + ":" + port + ". Verifique se o servidor está ativo.");
        } catch (Exception e) {
            System.err.println("Erro ao conectar ao servidor: " + e.getMessage());
        }
    }
}
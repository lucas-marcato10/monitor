package server.connection;

import server.task.Collector;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientHandler implements Runnable {
    private final Session session;
    private final AtomicInteger clientesConectados;
    private final ConcurrentHashMap<UUID, ClientHandler> handlers;
    private Collector monitorAtual = null;
    private AtomicBoolean rejected = new AtomicBoolean();

    public ClientHandler(Socket socket, AtomicInteger clientesConectados, ConcurrentHashMap<UUID, ClientHandler> handlers, boolean rejected) throws IOException {
        this.session = new Session(socket);
        this.clientesConectados = clientesConectados;
        this.handlers = handlers;
        this.rejected.set(rejected);
    }

    public UUID getUuid() {
        return session.getUuid();
    }

    @Override
    public void run() {
        try {
            String horario = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            if(this.rejected.getAcquire()){
                session.send(horario+": LIMITE DE CONEXÕES ATINGIDO!");
                return;
            }
            session.send(horario + ": CONECTADO!!");
            session.send("--- MENU DE COMANDOS ---");
            session.send("CPU-X      (ex: CPU-5)");
            session.send("memoria-X  (ex: memoria-3)");
            session.send("Quit       (pausa o monitor)");
            session.send("Exit       (encerra conexao)");
            session.send("------------------------");

            String inputLine;
            while ((inputLine = session.read()) != null) {
                inputLine = inputLine.trim();

                if (inputLine.equalsIgnoreCase("Exit")) {
                    pararMonitorAtual();
                    session.send("Encerrando conexao. Ate logo!");
                    break;
                }

                if (inputLine.equalsIgnoreCase("Quit")) {
                    pararMonitorAtual();
                    session.send("Monitoramento interrompido.");
                    continue;
                }

                if (inputLine.toUpperCase().startsWith("CPU-") || inputLine.toLowerCase().startsWith("memoria-")) {
                    pararMonitorAtual();

                    String[] partes = inputLine.split("-");
                    if (partes.length == 2) {
                        try {
                            String comando = partes[0];
                            int tempo = Integer.parseInt(partes[1]);

                            monitorAtual = new Collector(comando, tempo, session);
                            Thread.ofVirtual().start(monitorAtual);
                            session.send("Iniciando monitoramento de " + comando + " a cada " + tempo + "s.");
                        } catch (NumberFormatException e) {
                            session.send("Sintaxe invalida para tempo.");
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Cliente desconectado.");
        } finally {
            pararMonitorAtual();
            session.close();
            handlers.remove(session.getUuid());
            clientesConectados.decrementAndGet();
        }
    }

    private void pararMonitorAtual() {
        if (monitorAtual != null) {
            monitorAtual.pararMonitoramento();
            monitorAtual = null;
        }
    }
}

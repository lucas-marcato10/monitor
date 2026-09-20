package server.connection;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import server.task.Collector;

public class ClientHandler implements Runnable {
    private final Session session;
    private final AtomicInteger clientesConectados;
    private final ConcurrentHashMap<UUID, ClientHandler> handlers;
    private final ConcurrentHashMap<String, Collector> monitors = new ConcurrentHashMap<>();
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
            if (this.rejected.getAcquire()) {
                session.send(horario() + ": LIMITE DE CONEXÕES ATINGIDO!");
                try {
                    Thread.sleep(50); // Garante a entrega no buffer TCP antes do fechamento
                } catch (InterruptedException ignored) {}
                return;
            }

            session.send(horario() + ": CONECTADO!!");
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
                    pararTodosMonitors();
                    session.send(horario() + ": Encerrando conexao. Ate logo!");
                    break;
                }

                if (inputLine.equalsIgnoreCase("Quit")) {
                    pararTodosMonitors();
                    session.send(horario() + ": Monitoramento interrompido.");
                    continue;
                }

                if (inputLine.toUpperCase().startsWith("CPU-") || inputLine.toLowerCase().startsWith("memoria-")) {
                    String[] partes = inputLine.split("-");
                    if (partes.length == 2) {
                        try {
                            String comando = partes[0];
                            int tempo = Integer.parseInt(partes[1]);
                            if (tempo <= 0) {
                                session.send(horario() + ": O tempo deve ser maior que 0.");
                                continue;
                            }
                            String key = comando.toLowerCase();

                            Collector existente = monitors.get(key);
                            if (existente != null) {
                                existente.pararMonitoramento();
                            }

                            Collector novo = new Collector(comando, tempo, session, monitors, key);
                            monitors.put(key, novo);
                            Thread.ofVirtual().start(novo);
                            session.send(horario() + ": Iniciando monitoramento de " + comando + " a cada " + tempo + "s.");
                        } catch (NumberFormatException e) {
                            session.send(horario() + ": Sintaxe invalida para tempo.");
                        }
                    } else {
                        session.send(horario() + ": Sintaxe invalida. Use: CPU-X ou memoria-X");
                    }
                } else {
                    session.send(horario() + ": Comando invalido: " + inputLine);
                }
            }
        } catch (SocketException e) {
            System.out.println(horario() + ": Cliente desconectado abruptamente.");
        } catch (IOException e) {
            System.out.println(horario() + ": Erro na conexão com o cliente.");
        } finally {
            System.out.println(horario() + ": Cliente desconectado.");
            pararTodosMonitors();
            session.close();
            handlers.remove(session.getUuid());
            clientesConectados.decrementAndGet();
        }
    }

    private String horario() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    private void pararTodosMonitors() {
        monitors.values().forEach(Collector::pararMonitoramento);
        monitors.clear();
    }
}
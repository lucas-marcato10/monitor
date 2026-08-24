package server;

import core.MonitorTasks;
import core.Session;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Server implements IServer{
    private final ConcurrentMap<UUID, Session> activeSessions = new ConcurrentHashMap<>();
    private ServerSocket socket;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final MonitorTasks monitor = new MonitorTasks();
    @Override
    public void start(int port) {
        try{
            // Boot Server Socket.
            this.socket = new ServerSocket(port);
            System.out.println("Servidor rodando em: " + port);

            while (true){

                Socket client = this.socket.accept();

                Session session = new Session(client);
                activeSessions.put(session.getUuid(),session);

                session.send(nowTimestamp() + ": CONECTADO!!");
                session.send("Comandos: CPU | RAM | EXIT");
                Thread.startVirtualThread(()->handleSession(session));
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private void handleSession(Session session){
        try {
            String remoteInput;
            while ((remoteInput = session.read()) != null){
                switch(remoteInput.toUpperCase().trim()){
                    case "CPU" -> {
                        String cpuUsage = monitor.collectCpu();
                        session.send(cpuUsage);
                    }
                    case "RAM" -> {
                        String ramUsage = monitor.collectRam();
                        session.send(ramUsage);
                    }

                    case "EXIT" -> {
                        session.send("Desconectando.");
                        session.close();
                    }
                    default -> System.out.println("Comando inválido.");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            //Evita leak de conexão.
            activeSessions.remove(session.getUuid());
            session.close();
            System.out.println("Cliente Desconectado: " + session);
        }
    }

    @Override
    public void stop() {
        try{
            if(socket != null) this.socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void gracefulShutdown(){
        for(Session session : this.activeSessions.values()){
            session.close();
        }
        activeSessions.clear();
    }

    public static String nowTimestamp(){
        return LocalDateTime.now().format(FORMATTER);
    }
}

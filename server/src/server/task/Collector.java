package server.task;

import server.connection.Session;

public class Collector implements Runnable {
    private final String tipo;
    private final int intervaloSegundos;
    private final Session session;
    private final MonitorTasks monitorTasks;
    private volatile boolean rodando = true;

    public Collector(String tipo, int intervaloSegundos, Session session) {
        this.tipo = tipo;
        this.intervaloSegundos = intervaloSegundos;
        this.session = session;
        this.monitorTasks = new MonitorTasks();
    }

    public void pararMonitoramento() {
        this.rodando = false;
    }

    @Override
    public void run() {
        while (rodando && session.isAlive()) {
            try {
                String resposta = "";

                if (tipo.equalsIgnoreCase("CPU")) {
                    resposta = monitorTasks.collectCpu();
                } else if (tipo.equalsIgnoreCase("memoria") || tipo.equalsIgnoreCase("ram")) {
                    resposta = monitorTasks.collectRam();
                }

                if (rodando && resposta != null) {
                    session.send(resposta);
                }

                Thread.sleep(intervaloSegundos * 1000L);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                break;
            }
        }
    }
}

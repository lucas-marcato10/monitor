package client;

import com.sun.source.tree.WhileLoopTree;
import core.Session;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;

public class Client implements IClient{
    private Session clientSession;
    private volatile boolean running = true;
    private Scanner scanner;
    @Override
    public void connect(String host, int port) {
        try {
            Socket socket = new Socket(host, port);
            this.clientSession = new Session(socket);
            System.out.println("Conectado com: " + host + "na porta: " + port);

            Thread.startVirtualThread(this::outputThread);
            Thread.startVirtualThread(this::sendMessageThread);

        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private Runnable sendMessageThread(){
        return()-> {
            System.out.println(">");
            while (running && scanner.hasNextLine()) {
                String command = scanner.nextLine().trim();
                if (command.isEmpty()) {
                    System.out.println(">");
                    continue;
                }
                clientSession.send(command);
                if (command.contentEquals("EXIT")) {
                    System.out.println("Shutting Down");
                    shutdown();
                    break;
                }
            }
            System.out.println("Connection Closed.");
        };
    }
    private Runnable outputThread(){
        return ()->{
            try{
                String msg;
                while (running && (msg= clientSession.read())!=null){
                    System.out.println("From Server: "+msg);
                }
            }catch (IOException e){
                if (running){
                    System.out.println("Lost Connection");
                }
            }
        };
    }

    private void shutdown(){
        this.running = false;
        this.clientSession.close();
        this.scanner.close();
    }

    @Override
    public UUID getUUID() {
        return this.clientSession.getUuid();
    }
}

package server.connection;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

public class Session {
    private BufferedReader input;
    private PrintWriter output;
    private UUID uuid;
    private Socket socket;

    public Session(Socket socket) throws IOException {
        this.socket = socket;
        this.uuid = UUID.randomUUID();
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output = new PrintWriter(socket.getOutputStream(), true);
    }

    public String read() throws IOException {
        return input.readLine();
    }

    public void send(String msg) {
        output.println(msg);
    }

    public boolean isAlive() {
        return socket != null && !socket.isClosed();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void close() {
        try { socket.close(); } catch (IOException ignored) {}
    }
}

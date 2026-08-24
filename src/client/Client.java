package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client implements IClient{
    @Override
    public Socket createSocket() {
        System.out.println("Client: Socket Criado");
        return new Socket();
    }

    @Override
    public void connect(Socket socket, String host, int port) {
        try{
            socket.connect(new InetSocketAddress(host,port));
            System.out.println("Conectado com: " + host + "na porta: " + port);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}

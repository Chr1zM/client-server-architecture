package a1;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class TCPServer {
    private static TCPServer instance;
    private boolean running;
    private final ServerSocket serverSocket;

    private final List<TCPConnection> connections = new ArrayList<>();

    public static TCPServer getInstance() throws IOException {
        if (instance == null) {
            instance = new TCPServer();
        }
        return instance;
    }

    private TCPServer() throws IOException {
        serverSocket = new ServerSocket(7890);
    }


    public void start() throws Exception {
        running = true;

        while (running) {
            connections.add(new TCPConnection(serverSocket.accept()));
            connections.get(connections.size() - 1).start();
        }
    }

    public void broadcast(String message) {
        connections.forEach(con ->
        {
            try {
                con.sendToClient(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) throws Exception {
        TCPServer server = TCPServer.getInstance();
        server.start();
    }
}

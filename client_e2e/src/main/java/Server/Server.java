package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private final Map<String, Connection> clients;
    private static Server instance;

    private Server() {
        clients = new HashMap<>();
    }

    public void addConnection(String name, Connection connection) {
        clients.put(name, connection);
    }

    public Map<String, Connection> getClients() {
        return clients;
    }

    public static synchronized Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("Server gestartet. Warte auf Verbindungen...");

        while (true) {
            Socket socket = serverSocket.accept();
            Connection connection = new Connection(socket);
            connection.start();
        }
    }
}

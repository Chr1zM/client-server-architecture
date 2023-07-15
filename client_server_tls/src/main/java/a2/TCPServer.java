package a2;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class TCPServer {
    private static TCPServer instance;
    private boolean running;
    private final SSLServerSocket serverSocket;
    private SSLSocket clientSocket;
    public static final String ECHO = "echo: ";

    public static TCPServer getInstance() throws IOException {
        if (instance == null) {
            instance = new TCPServer();
        }
        return instance;
    }

    private TCPServer() throws IOException {
        SSLServerSocketFactory serverSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(7890);
    }

    public String waitForMessage() throws IOException {
        DataInputStream inFromClient = new DataInputStream(this.clientSocket.getInputStream());
        return inFromClient.readUTF();
    }

    public void sendToClient(String message) throws IOException {
        DataOutputStream outToClient = new DataOutputStream(this.clientSocket.getOutputStream());
        outToClient.writeUTF(message);
    }

    public void start() throws Exception {
        this.clientSocket = (SSLSocket) this.serverSocket.accept();
        this.running = true;

        System.out.println("Client connected: " + this.clientSocket.getInetAddress().getHostName());

        String clientSentence;
        String echoSentence;

        while (this.running) {
            try {
                clientSentence = waitForMessage();
                if (clientSentence.equals("\\exit")) {
                    this.clientSocket.close();
                    System.out.println("ClientSocket closed: " + this.clientSocket.getInetAddress().getHostName());
                    this.running = false;
                    this.serverSocket.close();
                } else {
                    echoSentence = ECHO + clientSentence;
                    sendToClient(echoSentence);
                }
            } catch (IOException e) {
                System.err.println(Arrays.toString(e.getStackTrace()));
            }
        }
    }


    public static void main(String[] args) throws Exception {
        System.setProperty("javax.net.ssl.keyStore", "src\\main\\resources\\server\\server-keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "abc123");

        TCPServer server = TCPServer.getInstance();
        server.start();
    }
}

package a1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class TCPConnection extends Thread {
    public static final String ECHO = "echo: ";
    private final Socket clientSocket;
    private boolean running;

    public TCPConnection(Socket client) {
        this.clientSocket = client;
        this.running = true;
    }

    public void sendToClient(String message) throws IOException {
        DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());
        outToClient.writeUTF(message);
    }

    public String waitForMessage() throws IOException {
        DataInputStream inFromClient = new DataInputStream(clientSocket.getInputStream());
        return inFromClient.readUTF();
    }

    public void run() {
        System.out.println("Client connected: " + clientSocket.getInetAddress().getHostName());

        String clientSentence;
        String echoSentence;

        while (running) {
            try {
                clientSentence = waitForMessage();
                if (clientSentence.startsWith("\\broadc ")) {
                    echoSentence = ECHO + clientSentence.replace("\\broadc ", "");
                    TCPServer.getInstance().broadcast(echoSentence);
                } else if (clientSentence.equals("\\exit")) {
                    clientSocket.close();
                    System.out.println("ClientSocket closed: " + clientSocket.getInetAddress().getHostName());
                    running = false;
                } else {
                    echoSentence = ECHO + clientSentence;
                    sendToClient(echoSentence);
                }
            } catch (IOException e) {
                System.err.println(Arrays.toString(e.getStackTrace()));
            }
        }
    }
}

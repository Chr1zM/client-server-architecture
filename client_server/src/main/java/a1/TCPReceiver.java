package a1;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class TCPReceiver extends Thread {
    private final Socket socket;
    private final TCPClient client;

    public TCPReceiver(Socket socket, TCPClient client) {
        this.socket = socket;
        this.client = client;
    }

    public void run() {
        String modifiedSentence;
        while (client.isRunning()) {
            try {
                modifiedSentence = waitForNewMessage();
                processReceivedMessage(modifiedSentence);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processReceivedMessage(String modifiedSentence) {
        System.out.println(modifiedSentence + "\n");
    }

    private String waitForNewMessage() throws IOException {
        try {
            DataInputStream inFromServer = new DataInputStream(socket.getInputStream());
            return inFromServer.readUTF();
        } catch (IOException e) {
            if (this.client.isRunning()) {
                System.err.println("[Receiver#waitForNewMessage]: " + e.getMessage());
                client.stop();
            }
            return "";
        }
    }
}

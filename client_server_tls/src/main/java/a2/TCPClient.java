package a2;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;


public class TCPClient {
    private boolean running;
    private final SSLSocket socket;

    public TCPClient(SSLSocket socket) {
        this.socket = socket;
    }

    public void sendToServer(String sentence) throws IOException {
        DataOutputStream outToServer = new DataOutputStream(this.socket.getOutputStream());
        outToServer.writeUTF(sentence);
        if (sentence.endsWith("\\exit")) {
            this.stop();
        }
    }

    public String promptForNewMessage() {
        Scanner inFromUser = new Scanner(System.in);
        return inFromUser.nextLine();
    }

    public void stop() throws IOException {
        this.running = false;
        if (this.socket.isConnected())
            this.socket.close();
    }

    private void processReceivedMessage(String modifiedSentence) {
        System.out.println(modifiedSentence + "\n");
    }

    private String waitForNewMessage() throws IOException {
        try {
            DataInputStream inFromServer = new DataInputStream(this.socket.getInputStream());
            return inFromServer.readUTF();
        } catch (IOException e) {
            if (this.running) {
                System.err.println("[Client#waitForNewMessage]: " + e.getMessage());
                stop();
            }
            return "";
        }
    }

    public void start() throws IOException {
        this.running = true;
        String sentence;

        String modifiedSentence;

        while (this.running) {
            sentence = promptForNewMessage();
            sendToServer(sentence);
            try {
                modifiedSentence = waitForNewMessage();
                processReceivedMessage(modifiedSentence);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("javax.net.ssl.trustStore", "src\\main\\resources\\client\\truststore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "abc123");
        TCPClient client = new TCPClient((SSLSocket) SSLSocketFactory.getDefault().createSocket("localhost", 7890));
        client.start();
    }
}

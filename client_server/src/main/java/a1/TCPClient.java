package a1;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;


public class TCPClient {
    private boolean running;
    private final Socket socket;
    private final TCPReceiver receiver;

    public TCPClient(Socket socket) {
        this.socket = socket;
        this.receiver = new TCPReceiver(this.socket, this);
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

    public void start() throws IOException {
        this.running = true;
        String sentence;

        this.receiver.start();

        while (this.running) {
            sentence = promptForNewMessage();
            sendToServer(sentence);
        }
    }

    public boolean isRunning() {
        return this.running;
    }

    public static void main(String[] args) throws Exception {
        TCPClient client = new TCPClient(new Socket("localhost", 7890));
        client.start();
    }
}

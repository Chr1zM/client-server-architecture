package client;

import com.google.gson.Gson;
import crypto.Crypto;
import messsaging.Message;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.security.PrivateKey;

public class Receiver extends Thread {
    private final DataInputStream in;
    private final PrivateKey priv;

    public Receiver(Socket socket, PrivateKey priv) throws IOException {
        this.in = new DataInputStream(socket.getInputStream());
        this.priv = priv;
    }

    public void run() {
        try {
            while (true) {
                // Servernachrichten einlesen
                String encryptedMessageText = in.readUTF();

                Gson gson = new Gson();
                Message encryptedMessage = gson.fromJson(encryptedMessageText, Message.class);

                byte[] decryptedMessage = Crypto.decrypt(encryptedMessage.getContent(), priv);
                String decryptedMessageText = new String(decryptedMessage);

                System.out.println("Nachricht erhalten: " + decryptedMessageText
                        + " von: " + encryptedMessage.getFrom());
            }
        } catch (SocketException e) {
            System.out.println("Client beendet.");
        } catch (EOFException e) {
            System.out.println("Verbindung zum Server abgebrochen.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

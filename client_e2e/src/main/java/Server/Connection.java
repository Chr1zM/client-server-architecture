package Server;

import com.google.gson.Gson;
import messsaging.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class Connection extends Thread {
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;

    Connection(Socket socket) throws IOException {
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    public void run() {
        try {
            // Name des Clients einlesen, der die Nachrichten schickt
            String fromClientName = dataInputStream.readUTF();

            // Am Server anmelden
            Server server = Server.getInstance();
            server.addConnection(fromClientName, this);

            while (true) {
                // Nachrichten des Clients einlesen
                String messageText = dataInputStream.readUTF();

                Gson gson = new Gson();
                Message message = gson.fromJson(messageText, Message.class);

                String recipient = message.getTo();
                byte[] content = message.getContent();

                System.out.println("Verschlüsselter Inhalt erhalten: " + new String(content));

                // Empfänger-Connection aus dem Server holen
                Connection recipientConnection = server.getClients().get(recipient);

                // Nachricht an den Empfänger weiterleiten
                recipientConnection.sendToClient(message);
            }
        } catch (EOFException e) {
            System.out.println("Client hat die Verbindung beendet.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToClient(Message message) throws IOException {
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(message);

        // JSON-String zum Server schicken
        dataOutputStream.writeUTF(jsonMessage);
    }
}

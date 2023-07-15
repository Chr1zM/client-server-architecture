package client;

import com.google.gson.Gson;
import crypto.Crypto;
import messsaging.Message;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Scanner;

public class Client {

    public Client(String name) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        // Einlesen des eigenen private Key
        PrivateKey privateKey = (PrivateKey) Crypto.readKeyFile(".\\src\\main\\resources\\" + name + ".priv");

        // Socket zum Server aufmachen
        Socket socket = new Socket("localhost", 8888);

        // Den eigenen Clientname per DataOutputstream zum Server schicken
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        outputStream.writeUTF(name);

        // Den Einleseteil in die Thread-Klasse Receiver auslagern und starten
        Receiver receiver = new Receiver(socket, privateKey);
        receiver.start();

        // In einer Schleife vom Scanner Empfänger und Nachrichteninhalt einlesen
        Scanner inFromUser = new Scanner(System.in);
        while (true) {
            System.out.print("Empfänger|Nachricht: ");
            String input = inFromUser.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            String[] parts = input.split("\\|");
            if (parts.length == 2) {
                String to = parts[0];
                String content = parts[1];

                // Den public key des Empfängers einlesen
                PublicKey publicKey = (PublicKey) Crypto.readKeyFile(
                        ".\\src\\main\\resources\\" + to + ".pub"
                );

                // Mit diesem Public Key den Nachrichteninhalt verschlüsseln
                byte[] encryptedContent = Crypto.encrypt(content.getBytes(), publicKey);

                // Message aus Empfänger (to) und Nachrichteninhalt (content) erstellen und zum Server schicken
                Message message = new Message(name, to, encryptedContent);

                // Message in JSON-String umwandeln
                Gson gson = new Gson();
                String jsonMessage = gson.toJson(message);

                // JSON-String zum Server schicken
                outputStream.writeUTF(jsonMessage);
            }
        }
        inFromUser.close();
        outputStream.close();
        socket.close();
    }

    public static void main(String[] args) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, BadPaddingException, InvalidKeyException, ClassNotFoundException {
        if (args.length == 1) {
            String name = args[0];
            new Client(name);
        } else {
            System.out.println("Bitte geben Sie den Namen des Clients als Argument ein.");
        }
    }
}

package meyer.christian.client;

import java.net.*;
import java.util.Scanner;

public class UDPEchoClient {

    public static void main(String[] args) {
        try {
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getLocalHost();

            UDPReceiver receiver = new UDPReceiver(clientSocket);
            receiver.start();

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("Enter message ('/exit' to exit): ");
                String message = scanner.nextLine();
                if (message.equalsIgnoreCase("/exit")) {
                    break;
                }
                byte[] sendData = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, 6789);
                clientSocket.send(sendPacket);

                System.out.println("Sent message: " + message + " (" + sendData.length + " bytes)");
                Thread.sleep(10);
            }
            receiver.stopReceiver();
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/*
Sinnvolles Szenario:
Wenn der Client im Hintergrund weiter arbeiten muss, während man gleichzeitig auf die Antwort
des Servers wartet.

Testen Sie z.B. folgendes Szenario:
    Clientstart
    Text-Eingabe und Senden im Client
    Server-Start
    erneute Text-Eingabe und Senden im Client

Ohne Receiver wartet der Client auf eine Antwort vom Server und hängt sich auf.
Mit Receiver geht es direkt zum neuen Sent, und man wartet nicht auf die Antwort vom Server.
 */
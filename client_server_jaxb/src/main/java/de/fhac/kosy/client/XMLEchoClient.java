package de.fhac.kosy.client;

import de.fhac.kosy.xml.XMLSerialisation;
import de.fhac.kosy.xml.generated.EchoMessage;
import jakarta.xml.bind.JAXBException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class XMLEchoClient {
    public static void main(String[] args) throws UnknownHostException, IOException, JAXBException {
        System.out.println("client starts");
        Socket socket = new Socket("localhost", 5678);

        String senderName = socket.getLocalAddress().getHostName();
        // XMLSerialisation-Objekt initialisieren
        XMLSerialisation xmlSerialisation = new XMLSerialisation(senderName);

        Scanner scanner = new Scanner(System.in);
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        while (scanner.hasNextLine()) {
            // EchoMessage erstellen & Content setzen und als XML verschicken
            EchoMessage echoMessage = xmlSerialisation.getNewMessage();
            echoMessage.setContent(scanner.nextLine());
            dataOutputStream.writeUTF(xmlSerialisation.messageToXMLString(echoMessage));
            // vice versa
            String xmlAntwort = dataInputStream.readUTF();
            EchoMessage antwortObject = xmlSerialisation.xmlStringToMessage(xmlAntwort);
            // Ausgabe von Sender und Content
            System.out.println("antwort:");
            System.out.println(antwortObject.getSender());
            System.out.println(antwortObject.getContent());
        }
        scanner.close();
        socket.close();
    }
}

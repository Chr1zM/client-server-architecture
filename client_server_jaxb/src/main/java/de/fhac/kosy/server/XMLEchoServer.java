package de.fhac.kosy.server;

import de.fhac.kosy.xml.XMLSerialisation;
import de.fhac.kosy.xml.generated.EchoMessage;
import jakarta.xml.bind.JAXBException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class XMLEchoServer {
    public static void main(String[] args) throws IOException, JAXBException {
        System.out.println("server starts");
        ServerSocket serverSocket = new ServerSocket(5678);
        Socket socket = serverSocket.accept();

        String senderName = socket.getInetAddress().getHostName();
        // XMLSerialisation-Objekt initialisieren
        XMLSerialisation xmlSerialisation = new XMLSerialisation(senderName);

        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        while (true) {
            // XML einlesen und daraus EchoMessage mit UpperCase-Content erstellen
            EchoMessage echoMessage = xmlSerialisation.xmlStringToMessage(dataInputStream.readUTF());
            echoMessage.setContent(echoMessage.getContent().toUpperCase());
            // vice versa
            dataOutputStream.writeUTF(xmlSerialisation.messageToXMLString(echoMessage));
        }
    }
}

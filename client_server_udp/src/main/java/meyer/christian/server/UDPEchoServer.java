package meyer.christian.server;

import java.net.*;

public class UDPEchoServer {

    public static void main(String[] args) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(6789);
            System.out.println("UDP server started on port " + serverSocket.getLocalPort());

            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                int clientPort = receivePacket.getPort();
                InetAddress clientAddress = receivePacket.getAddress();

                System.out.println("Received message: " + message + " from " + clientAddress.getHostAddress() + ":" + clientPort);

                String responseMessage = message.toUpperCase();
                byte[] sendData = responseMessage.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);

                serverSocket.send(sendPacket);
                System.out.println("Sent response: " + responseMessage + " to " + clientAddress.getHostAddress() + ":" + clientPort);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

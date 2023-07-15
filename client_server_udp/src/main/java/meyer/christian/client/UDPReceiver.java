package meyer.christian.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPReceiver extends Thread {
    private final DatagramSocket socket;
    private boolean running;

    public UDPReceiver(DatagramSocket socket) {
        this.socket = socket;
        this.running = true;
    }

    public void stopReceiver() {
        this.running = false;
    }

    public void run() {
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        while (this.running) { // start infinite loop to receive packets
            try {
                this.socket.receive(receivePacket); // wait for response packet
                String responseMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received response: " + responseMessage + " (" + receivePacket.getLength() + " bytes)");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

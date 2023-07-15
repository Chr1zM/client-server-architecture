import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Client {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
        Future<Void> connectionResult = client.connect(new InetSocketAddress("localhost", 6789));
        connectionResult.get(); // blocks til connection is established

        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        ByteBuffer sendBuffer;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // write and send message
            sendBuffer = ByteBuffer.wrap(scanner.nextLine().getBytes());
            Future<Integer> writeResult = client.write(sendBuffer);
            writeResult.get(); // blocks til message is send

            // receive message
            Future<Integer> readResult = client.read(readBuffer);
            int bytes = readResult.get();
            String receivedMessage = new String(readBuffer.array(), 0, bytes);
            System.out.println("received: " + receivedMessage);

            // clear buffers for next message
            sendBuffer.clear();
            readBuffer.clear();
        }
    }
}

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class Server {

    public static void main(String[] args) throws IOException {
        AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("localhost", 6789));

        serverSocketChannel.accept(null,
                new CompletionHandler<>() {
                    @Override
                    public void completed(AsynchronousSocketChannel result, Object attachment) {
                        // accept next communication
                        serverSocketChannel.accept(null, this);

                        // Communication handling
                        try {
                            handle(result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failed(Throwable exc, Object attachment) {
                        System.err.println("Fehler bei der Verbindung: ");
                        exc.printStackTrace();
                    }
                });
        // don't close server
        while (true) ;
    }

    private static void handle(AsynchronousSocketChannel client) throws ExecutionException, InterruptedException {
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        while (true) {
            // read received MessageBytes
            Future<Integer> future = client.read(readBuffer);
            int bytes = future.get(); // blocking, waiting for client message
            // turn into a String
            String receiveMessage = new String(readBuffer.array(), 0, bytes).trim();
            // make it uppercase and turn into ByteBuffer
            ByteBuffer sendBuffer = ByteBuffer.wrap(receiveMessage.toUpperCase().getBytes());
            // send message back to client
            client.write(sendBuffer);
            // clear readBuffer for new message
            readBuffer.clear();
        }
    }
}

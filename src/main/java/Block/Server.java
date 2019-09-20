package Block;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @Author: fnbory
 * @Date: 2019/9/20 14:13
 */
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel= ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("127.0.0.1",8890));
        while(true){
            SocketChannel socketChannel=serverSocketChannel.accept(); // accept 阻塞
            BlockHandler handler=new BlockHandler(socketChannel);
            new Thread(handler).start();
        }
    }
}

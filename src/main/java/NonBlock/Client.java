package NonBlock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Author: fnbory
 * @Date: 2019/9/20 15:39
 */
public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel= SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1",8890));
        ByteBuffer buffer=ByteBuffer.wrap("123456678".getBytes());
        socketChannel.write(buffer);

        // 读取响应
        ByteBuffer readBuffer=ByteBuffer.allocate(1024);
        int num;
        // 从channel到buffer，从buffer到bytes
        if((num=socketChannel.read(readBuffer))>0){
            readBuffer.flip();
            byte[] re = new byte[num];   // 将buffer中的字符赋到re
            readBuffer.get(re);

            String result = new String(readBuffer.array(), "UTF-8");
            System.out.println("返回值: " + result);
        }
    }
}

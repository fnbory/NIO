package Block;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Author: fnbory
 * @Date: 2019/9/20 14:25
 */
public class BlockHandler implements  Runnable{

    private  SocketChannel socketChannel;

    public BlockHandler(SocketChannel socketChannel) {
        this.socketChannel=socketChannel;
    }

    @Override
    public void run() {
        ByteBuffer buffer=ByteBuffer.allocate(1024);
        int num;
        try {
            while((num=socketChannel.read(buffer))>0){   // read阻塞等待数据
                buffer.flip();
                byte[] bytes=new byte[num];
                buffer.get(bytes);   // 将buffer中的字符给bytes
                String re=new String(buffer.array(),"UTF-8");
                System.out.println("收到请求"+re);
                ByteBuffer writeBuffer=ByteBuffer.wrap(("我已经收到你的请求，你的请求内容是"+re).getBytes());
                socketChannel.write(writeBuffer);   // 此处write也是阻塞操作
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
